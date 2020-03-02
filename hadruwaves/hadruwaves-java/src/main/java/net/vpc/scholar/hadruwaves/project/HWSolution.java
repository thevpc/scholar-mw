package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.PropertyListenersImpl;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.UUID;

public class HWSolution implements TsonSerializable, WithListeners {
    private String uuid;
    private WritablePValue<String> filePath = Props.of("path").valueOf(String.class, null);
    private WritablePNamedNode<HWSolutionElement> root = Props.of("root").nnodeOf(HWSolutionElement.class, new HWSolutionFolder("root"));
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<HWProject> activeProject = Props.of("activeProject").valueOf(HWProject.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);


    public HWSolution(String uuid) {
        this.uuid = uuid;
        listeners.addDelegate(filePath);
        listeners.addDelegate(root, () -> null);
        listeners.addDelegate(name);
        listeners.addDelegate(description);
        listeners.addDelegate(activeProject);
    }

    public HWSolution() {
        this("hwss-" + UUID.randomUUID().toString());
    }

    public String uuid() {
        return uuid;
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    public WritablePValue<HWProject> activeProject() {
        return activeProject;
    }

    public WritablePValue<String> filePath() {
        return filePath;
    }

//    public WritablePNamedNode<HWSolutionElement> root() {
//        return root;
//    }

    public WritablePMap<String, WritablePNamedNode<HWSolutionElement>> children() {
        return root.children();
    }


    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<String> description() {
        return description;
    }

    public TsonElement toTsonElement() {
        return Tson.obj("solution")
                .add("path", filePath().get())
                .build();
    }

    public WritablePNamedNode<HWSolutionElement> findElement(String path) {
        return findElement(path, false);
    }

    public WritablePNamedNode<HWSolutionElement> findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSolutionElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSolutionElement> u = t.children().get(n);
            if (u == null) {
                if (createFolder) {
                    u = Props.of(n).nnodeOf(HWSolutionElement.class, createFolder(n));
                    t.children().put(n, u);
                } else {
                    return null;
                }
            }
            t = u;
        }
        return t;
    }

    public WritablePNamedNode<HWSolutionElement> remove(String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSolutionElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSolutionElement> u = t.children().get(n);
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

    public void add(HWSolutionElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePNamedNode<HWSolutionElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePNamedNode<HWSolutionElement> u = t.children().get(n);
            if (u == null) {
                u = Props.of(n).nnodeOf(HWSolutionElement.class, createFolder(n));
                t.children().put(n, u);
            }
            t = u;
        }
        WritablePNamedNode<HWSolutionElement> of = Props.of(element.name().get()).nnodeOf(HWSolutionElement.class, element);
        t.children().put(element.name().get(), of);
    }

    private HWSolutionFolder createFolder(String name) {
        return new HWSolutionFolder(name);
    }


}
