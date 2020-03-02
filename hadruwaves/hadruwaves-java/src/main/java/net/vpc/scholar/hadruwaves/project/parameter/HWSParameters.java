package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.PropertyListenersImpl;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.Objects;

public class HWSParameters implements TsonSerializable, WithListeners {
    private WritablePValue<String> filePath = Props.of("path").valueOf(String.class, null);
    private WritablePIndexedNode<HWSParameterElement> root = Props.of("root").inodeOf(HWSParameterElement.class, new HWSParameterFolder("root"));
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);

    public HWSParameters() {
        listeners.addDelegate(filePath);
        listeners.addDelegate(root);
        listeners.addDelegate(name);
        listeners.addDelegate(description);
    }


//    public WritablePIndexedNode<HWSParameterElement> root() {
//        return root;
//    }

    public WritablePValue<String> filePath() {
        return filePath;
    }


    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    public WritablePList<WritablePIndexedNode<HWSParameterElement>> children() {
        return root.children();
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

    public WritablePIndexedNode<HWSParameterElement> findElement(String path) {
        return findElement(path, false);
    }

    public WritablePIndexedNode<HWSParameterElement> findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSParameterElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePIndexedNode<HWSParameterElement> u = t.children().findFirst(x -> Objects.equals(x.getPropertyName(), n));
            if (u == null) {
                if (createFolder) {
                    u = Props.of(n).inodeOf(HWSParameterElement.class, createFolder(n));
                    t.children().add(u);
                } else {
                    return null;
                }
            }
            t = u;
        }
        return t;
    }

    public WritablePIndexedNode<HWSParameterElement> remove(String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSParameterElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            int u = t.children().findFirstIndex(x -> Objects.equals(x.getPropertyName(), n));
            if (u < 0) {
                return null;
            }
            if (i == ipath.length - 1) {
                WritablePIndexedNode<HWSParameterElement> ee = t.children().get(u);
                t.children().remove(u);
//                if (t.children().size() == 0) {
//                    ItemPath ppath = ipath.skipLast();
//                    if (ppath != null && ppath.size() > 0) {
//                        remove(ppath.toString());
//                    }
//                }
                return ee;
            }
        }
        return null;
    }

    public void add(HWSParameterElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSParameterElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePIndexedNode<HWSParameterElement> u = t.children().findFirst(x -> Objects.equals(x.getPropertyName(), n));
            if (u == null) {
                u = Props.of(n).inodeOf(HWSParameterElement.class, createFolder(n));
                t.children().add(u);
            }
            t = u;
        }
        WritablePIndexedNode<HWSParameterElement> of = Props.of(element.name().get()).inodeOf(HWSParameterElement.class, element);
        t.children().add(of);
    }

    private HWSParameterFolder createFolder(String name) {
        return new HWSParameterFolder(name);
    }


}
