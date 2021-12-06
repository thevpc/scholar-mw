package net.thevpc.scholar.hadruwaves.project;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurations;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameters;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;

import java.util.UUID;
import java.util.stream.Collectors;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.scene.AbstractHWProjectComponentMaterial;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectScene;
import net.thevpc.scholar.hadruwaves.project.parameter.HWUnits;
import net.thevpc.scholar.hadruwaves.project.templates.MicrostripProjectTemplate;

public class DefaultHWProject extends AbstractHWSolutionElement implements HWProject {

    /**
     * bound by parent (solution)
     */
    private final WritableString filePath = Props.of("filePath").stringOf(null);

    private String uuid;
    private final WritableValue<HWProjectScene> scene = Props.of("scene").valueOf(HWProjectScene.class, null);
    private final WritableBoolean modified = Props.of("modified").booleanOf(false);
    private final WritableLiMap<String, HWMaterialTemplate> materials = Props.of("materials").lmapOf(String.class, HWMaterialTemplate.class, x -> x.name().get());

    private final DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);

    private final HWParameters parameters = new HWParameters(this);
    private final HWUnits units = new HWUnits("units", this);
    private final HWConfigurations configurations = new HWConfigurations(this);

    public DefaultHWProject() {
        this("hwsp-" + UUID.randomUUID().toString());
    }

    public DefaultHWProject(String uuid) {
        super("project");
        this.uuid = uuid;
        listeners.addDelegate(filePath);
        listeners.addDelegate(name());
        listeners.addDelegate(description());
        listeners.addDelegate(parentPath());
        listeners.addDelegate(scene);
        listeners.addDelegate(configurations, () -> Path.of("configurations"));

        listeners.addDelegate(parameters, () -> Path.of("parameters"));
        listeners.addDelegate(units, () -> Path.of("units"));
        materials.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                HWMaterialTemplate oldValue = event.oldValue();
                HWMaterialTemplate newValue = event.newValue();
                switch (event.eventType()) {
                    case ADD: {
                        newValue.name().onChange(new RePutByNamePropertyListener(newValue));
                        break;
                    }
                    case REMOVE: {
                        oldValue.name().events()
                                .removeIf(x -> x instanceof RePutByNamePropertyListener
                                && ((RePutByNamePropertyListener) x).getMaterial() == newValue);
                        break;
                    }
                }
            }
        });
        parameters.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.eventType()) {
                    case ADD: {
                        Object elem = event.newValue();
                        if (event.newValue() instanceof IndexedNode) {
                            elem = ((IndexedNode) event.newValue()).get();
                        }
                        if (elem instanceof HWParameterValue) {
                            HWParameterValue v = (HWParameterValue) elem;
                            for (HWConfigurationRun r : configurations().findRuns()) {
                                r.parameters().put(v.name().get(), "");
                            }
                        }
                        break;
                    }
                    case REMOVE: {
                        Object elem = event.oldValue();
                        if (event.newValue() instanceof IndexedNode) {
                            elem = ((IndexedNode) event.newValue()).get();
                        }
                        if (elem instanceof HWParameterValue) {
                            HWParameterValue v = (HWParameterValue) elem;
                            for (HWConfigurationRun r : configurations().findRuns()) {
                                r.parameters().remove(v.name().get());
                            }
                        }
                        break;
                    }
                }
            }
        });
        materials.add(new HWMaterialTemplate(Material.VACUUM, this));
        materials.add(new HWMaterialTemplate(Material.PEC, this));
        scene.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                updateMaterials();
            }
        });
        listeners.add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.eventPath().toString().matches("/[^/]+")) {
                    modified.set(true);
                }
            }

        });

    }

    @Override
    public WritableBoolean modified() {
        return modified;
    }

    @Override
    public String uuid() {
        return uuid;
    }

    @Override
    public HWParameters parameters() {
        return parameters;
    }

    @Override
    public HWUnits units() {
        return units;
    }

    @Override
    public HWConfigurations configurations() {
        return configurations;
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    public WritableString filePath() {
        return filePath;
    }

    @Override
    public WritableValue<HWProjectScene> scene() {
        return scene;
    }

    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = Tson.obj("project");
        obj
                .add("uuid", uuid())
                .add("name", name().get())
                .add("description", description().get())
                .add("materials", Tson.array().addAll(materials().values()
                        .stream().map(x -> x.toTsonElement()).collect(Collectors.toList()))
                )
                .add("parameters", parameters().toTsonElement())
                .add("units", units().toTsonElement())
                .add("configurations", configurations().toTsonElement());
        if (scene().get() != null) {
            obj.add("scene", scene().get().toTsonElement());
        }

        return obj.build();
    }

    @Override
    public WritableLiMap<String, HWMaterialTemplate> materials() {
        return materials;
    }

    private class RePutByNamePropertyListener implements PropertyListener {

        private final HWMaterialTemplate material;

        public RePutByNamePropertyListener(HWMaterialTemplate material) {
            this.material = material;
        }

        public HWMaterialTemplate getMaterial() {
            return material;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            String oldName = event.oldValue();
            String newName = event.newValue();
            if (oldName != null) {
                materials.remove(oldName);
            }
            if (newName != null) {
                materials.add(material);
            }
        }
    }

    private File toOldFile(File file) {
        File pf = file.getParentFile();
        String name = file.getName();
        return new File(pf, ".~old~" + name);
    }

    @Override
    public void save() {
        String f = filePath().get();
        HWProjectFileState s = resolveProjectFileState(f);
        switch (s) {
            case INVALID: {
                throw new UncheckedIOException(new IOException("Invalid Project Path"));
            }
        }
        File ff = new File(f);
        File root = ff.getParentFile();
        root.mkdirs();
        if (ff.exists()) {
            try {
                Files.move(ff.toPath(), toOldFile(ff).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        try {
            Tson.writer().write(ff, toTsonElement());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        for (String p : new String[]{
            "code/scala/src/main/scala",
            "code/java/src/main/java",
            "code/hadra",
            "code/resources",
            "code/other",
            "build/cache",
            "results/default",
            "results/saved"
        }) {
            File d = new File(root, p);
            if (!d.exists()) {
                d.mkdirs();
            }
        }
    }

    @Override
    public void load() {
        //TODO
    }

    @Override
    public String defaultFileSuffix() {
        return "hwp.tson";
    }

    protected void updateMaterials() {
        HWProjectScene s = scene.get();
        for (HWProjectComponent c : s.findDeepComponents((x) -> x instanceof AbstractHWProjectComponentMaterial)) {
            AbstractHWProjectComponentMaterial r = (AbstractHWProjectComponentMaterial) c;
            HWMaterialTemplate m = r.material().get();
            if (materials.findAll(x -> x.getKey().equals(m.name().get())).isEmpty()) {
                materials.add(m);
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(name().get());
    }

    @Override
    public void requirePersistent() {
        throw new IllegalArgumentException("You should save project first");
    }

    @Override
    public boolean isPersistent() {
        String s = filePath().get();
        return !StringUtils.isBlank(s) && new File(s).isFile();
    }

    public HWProjectFileState resolveProjectFileState(String path) {
        if (path == null) {
            return HWProjectFileState.INVALID;
        }
        if (StringUtils.isBlank(path)) {
            return HWProjectFileState.INVALID;
        }
        File f = new File(path);
        if (f.exists()) {
            if (f.isFile() && f.getName().endsWith(".hwp.tson")) {
                return HWProjectFileState.VALID;
            }
            return HWProjectFileState.INVALID;
        }
        File p = new File(path).getParentFile();
        if (p == null) {
            return HWProjectFileState.INVALID;
        }
        if (p.isDirectory()) {
            File[] children = p.listFiles();
            if (children == null) {
                return HWProjectFileState.INVALID;
            }
            for (File c : children) {
                if (c.isHidden()
                        || c.getName().startsWith(".")
                        || c.getName().endsWith(".old")
                        || c.getName().endsWith(".back")) {
                    //ignore this;
                } else if (c.isFile()) {
                    if (c.getName().endsWith(".hws.tson")) {
                        //accept solutions
                    } else {
                        return HWProjectFileState.INVALID;
                    }
                } else if (c.isDirectory()) {
                    return HWProjectFileState.INVALID;
                }
            }
            return HWProjectFileState.NOT_FOUND;
        } else {
            return HWProjectFileState.INVALID;
        }
    }

    @Override
    public String fileTypeTitle() {
        return "Project";
    }

    @Override
    public void load(TsonElement tson) {
        //example!!
        new MicrostripProjectTemplate().load(this);
    }
}
