package net.vpc.scholar.hadruwaves.project.result;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePIndexedNode;
import net.vpc.common.prpbind.WritablePList;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.Objects;

public class HWSResults implements TsonSerializable {
    private WritablePValue<String> filePath = Props.of("path").valueOf(String.class, null);
    private WritablePIndexedNode<HWSResultElement> root = Props.of("root").inodeOf(HWSResultElement.class, new HWSResultFolder("root"));
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<HWSResultItem> selectedResult = Props.of("selectedResult").valueOf(HWSResultItem.class, null);

    public WritablePValue<String> filePath() {
        return filePath;
    }

//    public WritablePIndexedNode<HWSParameterElement> root() {
//        return root;
//    }


    public WritablePValue<HWSResultItem> getSelectedResult() {
        return selectedResult;
    }

    public WritablePList<WritablePIndexedNode<HWSResultElement>> children() {
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

    public WritablePIndexedNode<HWSResultElement> findElement(String path) {
        return findElement(path, false);
    }

    public WritablePIndexedNode<HWSResultElement> findElement(String path, boolean createFolder) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSResultElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePIndexedNode<HWSResultElement> u = t.children().findFirst(x -> Objects.equals(x.getPropertyName(), n));
            if (u == null) {
                if (createFolder) {
                    u = Props.of(n).inodeOf(HWSResultElement.class, createFolder(n));
                    t.children().add(u);
                } else {
                    return null;
                }
            }
            t = u;
        }
        return t;
    }

    public WritablePIndexedNode<HWSResultElement> remove(String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSResultElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            int u = t.children().findFirstIndex(x -> Objects.equals(x.getPropertyName(), n));
            if (u < 0) {
                return null;
            }
            if (i == ipath.length - 1) {
                WritablePIndexedNode<HWSResultElement> ee = t.children().get(u);
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

    public void add(HWSResultElement element, String path) {
        String[] ipath = PlatformUtils.splitPath(path);
        WritablePIndexedNode<HWSResultElement> t = root;
        for (int i = 0; i < ipath.length; i++) {
            String n = ipath[i];
            WritablePIndexedNode<HWSResultElement> u = t.children().findFirst(x -> Objects.equals(x.getPropertyName(), n));
            if (u == null) {
                u = Props.of(n).inodeOf(HWSResultElement.class, createFolder(n));
                t.children().add(u);
            }
            t = u;
        }
        WritablePIndexedNode<HWSResultElement> of = Props.of(element.name().get()).inodeOf(HWSResultElement.class, element);
        t.children().add(of);
    }

    private HWSResultFolder createFolder(String name) {
        return new HWSResultFolder(name);
    }


}
