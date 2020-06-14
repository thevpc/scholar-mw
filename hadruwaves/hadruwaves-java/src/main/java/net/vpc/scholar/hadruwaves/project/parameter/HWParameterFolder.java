package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.props.WritablePLMap;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;

public class HWParameterFolder extends AbstractHWParameterElement {
    private HWParameterFolderHelper childrenHelper = new HWParameterFolderHelper(this, () -> project().get());

    public HWParameterFolder(String name) {
        name().set(name);
    }


    public WritablePLMap<String, HWParameterElement> children() {
        return childrenHelper.children;
    }


    public List<HWParameterValue> findRuns() {
        return childrenHelper.findValues();
    }

    public HWParameterElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    public HWParameterElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    public HWParameterElement remove(String path) {
        return childrenHelper.remove(path);
    }

    public void add(HWParameterElement element, String path) {
        childrenHelper.addAt(path, element);
    }

    public List<HWParameterElement> removeDeepComponents(Predicate<HWParameterElement> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }

    public HWParameterValue getValue(String name) {
        return childrenHelper.getValue(name);
    }

    public HWParameterElement removeValue(String name) {
        return childrenHelper.removeValue(name);
    }
    
        @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("Folder")
                .add("name", name().get())
                .add("description", description().get())
                .add("children", 
                        Tson.array().addAll(
                            childrenHelper.children().values().stream().map(x->x.toTsonElement(context)).collect(Collectors.toList())
                        )
                )
                .build();
    }

}