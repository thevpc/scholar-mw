package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.*;
import net.thevpc.tson.TsonSerializable;

import java.util.List;
import java.util.function.Predicate;

public interface HWSolution extends TsonSerializable, WithListeners, FileObject {

    WritableValue<String> name();

    String uuid();

    WritableValue<HWProject> activeProject();

    //    public WritableNamedNode<HWSolutionElement> root() {
    //        return root;
    //    }
    WritableLiMap<String,HWSolutionElement> children();

    List<HWProject> findModifiedProjects();

    List<HWProject> findProjects();

    WritableValue<String> description();

    HWSolutionElement findElement(String path);

    HWSolutionElement findElement(String path, boolean createFolder);

    HWSolutionElement remove(String path);

    void add(HWSolutionElement element, String path);

    HWProject addProject(String namePrefix, String path);

    List<HWSolutionElement> removeDeepComponents(Predicate<HWSolutionElement> filter, boolean prune);
}
