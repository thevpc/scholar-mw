package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.WritableLiMap;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.thevpc.nuts.elem.NElement;


public class HWParameterFolder extends AbstractHWParameterElement {
    private HWParameterFolderHelper childrenHelper = new HWParameterFolderHelper(this, () -> project().get());

    public HWParameterFolder(String name) {
        name().set(name);
    }


    public WritableLiMap<String, HWParameterElement> children() {
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
    public NElement toElement() {
        return NElement.ofObjectBuilder("Folder")
                .add("name", name().get())
                .add("description", description().get())
                .add("children", 
                        NElement.ofArrayBuilder().addAll(
                            childrenHelper.children().values().stream().map(x->x.toElement()).collect(Collectors.toList())
                        ).build()
                ).build();
    }

}
