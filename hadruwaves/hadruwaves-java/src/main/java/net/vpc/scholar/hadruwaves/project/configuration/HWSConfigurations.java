package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.PropertyListenersImpl;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.ArrayList;
import java.util.List;

public class HWSConfigurations implements TsonSerializable, WithListeners {
    private WritablePValue<String> filePath = Props.of("path").valueOf(String.class, null);
    private WritablePNamedNode<HWSConfigurationElement> root = Props.of("root").nnodeOf(HWSConfigurationElement.class, new HWSConfigurationFolder("root"));
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<HWSConfigurationRun> activeConfiguration = Props.of("activeConfiguration").valueOf(HWSConfigurationRun.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);

    public HWSConfigurations() {
        listeners.addDelegate(filePath);
        listeners.addDelegate(root);
        listeners.addDelegate(name);
        listeners.addDelegate(description);
        listeners.addDelegate(activeConfiguration);
        HWSConfigurationRun aDefault = new HWSConfigurationRun("default");
        aDefault.name().vetos().add(new PropertyVeto() {
            @Override
            public void vetoableChange(PropertyEvent event) {
                switch (event.getAction()) {
                    case UPDATE: {
                        if (!"default".equals(event.getNewValue())) {
                            throw new IllegalArgumentException("Default Configuration cannot be renamed");
                        }
                        break;
                    }
                }
            }
        });
        children().put("default", Props.of("default").nnodeOf(HWSConfigurationElement.class, aDefault));
        children().vetos().add(new PropertyVeto() {
            @Override
            public void vetoableChange(PropertyEvent event) {
                switch (event.getAction()) {
                    case REMOVE: {
                        WritablePNamedNode<HWSConfigurationElement> oldValue = event.getOldValue();
                        if ("default".equals(oldValue.get().name().get())) {
                            throw new IllegalArgumentException("Default Configuration cannot be removed");
                        }
                    }
                }
            }
        });
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    public WritablePValue<String> filePath() {
        return filePath;
    }

    public List<HWSConfigurationRun> findRuns() {
        List<HWSConfigurationRun> list = new ArrayList<>();
        for (WritablePNamedNode<HWSConfigurationElement> child : root().findChildren(x -> (x.get() instanceof HWSConfigurationRun), true).values()) {
            HWSConfigurationRun r = (HWSConfigurationRun) child.get();
            list.add(r);
        }
        return list;
    }

    public WritablePNamedNode<HWSConfigurationElement> root() {
        return root;
    }

    public WritablePValue<HWSConfigurationRun> activeConfiguration() {
        return activeConfiguration;
    }

    public WritablePMap<String, WritablePNamedNode<HWSConfigurationElement>> children() {
        return (WritablePMap<String, WritablePNamedNode<HWSConfigurationElement>>) root.children();
    }


    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<String> description() {
        return description;
    }

    public TsonElement toTsonElement() {
        return Tson.obj("Configuration")
                .add("path", filePath().get())
                .build();
    }

    public WritablePNamedNode<HWSConfigurationElement> findElement(String path) {
        return findElement(path, false);
    }

    public WritablePNamedNode<HWSConfigurationElement> findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSConfigurationElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSConfigurationElement> u = t.children().get(n);
            if (u == null) {
                if (createFolder) {
                    u = Props.of(n).nnodeOf(HWSConfigurationElement.class, createFolder(n));
                    t.children().put(n, u);
                } else {
                    return null;
                }
            }
            t = u;
        }
        return t;
    }

    public WritablePNamedNode<HWSConfigurationElement> remove(String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSConfigurationElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSConfigurationElement> u = t.children().get(n);
            if (u == null) {
                return null;
            }
            if (i == ipath.length - 1) {
                t.children().remove(n);
//                if (t.children().size() == 0) {
//                    ItemPath ppath = ipath.skipLast();
//                    if (ppath != null && ppath.size() > 0) {
//                        remove(ppath.toString());
//                    }
//                }
                return u;
            }
        }
        return null;
    }

    public void add(HWSConfigurationElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSConfigurationElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSConfigurationElement> u = t.children().get(n);
            if (u == null) {
                u = Props.of(n).nnodeOf(HWSConfigurationElement.class, createFolder(n));
                t.children().put(n, u);
            }
            t = u;
        }
        WritablePNamedNode<HWSConfigurationElement> of = Props.of(element.name().get()).nnodeOf(HWSConfigurationElement.class, element);
        t.children().put(element.name().get(), of);
    }

    private HWSConfigurationFolder createFolder(String name) {
        return new HWSConfigurationFolder(name);
    }


}
