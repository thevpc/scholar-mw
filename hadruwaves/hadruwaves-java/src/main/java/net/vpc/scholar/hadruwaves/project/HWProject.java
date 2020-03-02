package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.PropertyListenersImpl;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurations;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterElement;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterFolder;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterValue;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameters;
import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWScene;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class HWProject implements TsonSerializable, HWSolutionElement, WithListeners {

    /**
     * bound by parent (solution)
     */
    private WritablePValue<String> filePath = Props.of("filePath").valueOf(String.class, null);

    private String uuid;
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private WritablePValue<HWScene> scene = Props.of("scene").valueOf(HWScene.class, null);
    private WritablePMap<String, HWMaterialTemplate> materialsMap = Props.of("materialsMap").mapOf(String.class, HWMaterialTemplate.class);

    private HWSConfigurations configurations = new HWSConfigurations();

    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private WritablePValue<UnitTypes.Dimension> dimensionUnit = Props.of("dimensionUnit").valueOf(UnitTypes.Dimension.class, UnitTypes.Dimension.mm);
    private WritablePValue<UnitTypes.Frequency> frequencyUnit = Props.of("frequencyUnit").valueOf(UnitTypes.Frequency.class, UnitTypes.Frequency.GHz);
    private HWSParameters parameters = new HWSParameters();

    public HWProject() {
        this("hwsp-"+UUID.randomUUID().toString());
    }

    public HWProject(String uuid) {
        this.uuid = uuid;
        listeners.addDelegate(filePath);
        listeners.addDelegate(name);
        listeners.addDelegate(description);
        listeners.addDelegate(parentPath);
        listeners.addDelegate(scene);
        listeners.addDelegate(configurations,()->"configurations");

        listeners.addDelegate(parameters,()->"parameters");
        listeners.addDelegate(dimensionUnit);
        listeners.addDelegate(frequencyUnit);
        materialsMap.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                HWMaterialTemplate oldValue = event.getNewValue();
                HWMaterialTemplate newValue = event.getNewValue();
                switch (event.getAction()) {
                    case ADD: {
                        newValue.name().listeners().add(new RePutByNamePropertyListener(newValue));
                        break;
                    }
                    case REMOVE: {
                        newValue.name().listeners()
                                .removeIf(x -> x instanceof RePutByNamePropertyListener
                                        && ((RePutByNamePropertyListener) x).getMaterial() == newValue);
                        break;
                    }
                }
            }
        });
        parameters.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        Object elem = event.getNewValue();
                        if(event.getNewValue() instanceof PIndexedNode){
                            elem=((PIndexedNode) event.getNewValue()).get();
                        }
                        if (elem instanceof HWSParameterValue) {
                            HWSParameterValue v = (HWSParameterValue) elem;
                            for (HWSConfigurationRun r : configurations().findRuns()) {
                                r.parameters().put(v.name().get(), "");
                            }
                        }
                        break;
                    }
                    case REMOVE: {
                        Object elem = event.getOldValue();
                        if(event.getNewValue() instanceof PIndexedNode){
                            elem=((PIndexedNode) event.getNewValue()).get();
                        }
                        if (elem instanceof HWSParameterValue) {
                            HWSParameterValue v = (HWSParameterValue) elem;
                            for (HWSConfigurationRun r : configurations().findRuns()) {
                                r.parameters().remove(v.name().get());
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    public String uuid() {
        return uuid;
    }

    public Map<String, HWSParameterValue> getParametersMap() {
        Map<String, HWSParameterValue> m = new HashMap<>();
        Stack<Object> stack = new Stack<>();
        stack.push(this);
        while (!stack.isEmpty()) {
            Object a = stack.pop();
            if (a instanceof HWProject) {
                for (WritablePIndexedNode<HWSParameterElement> child : ((HWProject) a).parameters().children()) {
                    HWSParameterElement c = child.get();
                    if (c instanceof HWSParameterValue) {
                        m.put(c.name().get(), (HWSParameterValue) c);
                    } else {
                        stack.push(child);
                    }
                }
            } else if (a instanceof WritablePIndexedNode) {
                WritablePIndexedNode<HWSParameterElement> n = (WritablePIndexedNode<HWSParameterElement>) a;
                HWSParameterElement o = n.get();
                if (o instanceof HWSParameterFolder) {

                } else if (o instanceof HWSParameterValue) {
                    HWSParameterValue c = (HWSParameterValue) o;
                    m.put(c.name().get(), c);
                }
                for (WritablePIndexedNode<HWSParameterElement> child : n.children()) {
                    stack.push(child);
                }
            }
        }
        return m;
    }

    public HWSParameters parameters() {
        return parameters;
    }

    public WritablePValue<UnitTypes.Dimension> dimensionUnit() {
        return dimensionUnit;
    }

    public WritablePValue<UnitTypes.Frequency> frequencyUnit() {
        return frequencyUnit;
    }

    public HWSConfigurations configurations() {
        return configurations;
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }

    public WritablePValue<String> parentPath() {
        return parentPath;
    }

    public WritablePValue<String> filePath() {
        return filePath;
    }

    public WritablePValue<HWScene> scene() {
        return scene;
    }

    public TsonElement toTsonElement() {
        return Tson.obj("solution")
                .add("path", filePath().get())
                .build();
    }

    public WritablePMap<String, HWMaterialTemplate> materials() {
        return materialsMap;
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
            String oldName = event.getOldValue();
            String newName = event.getNewValue();
            if (oldName != null) {
                materialsMap.remove(oldName);
            }
            if (newName != null) {
                materialsMap.put(newName, material);
            }
        }
    }
}
