package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.WritablePLMap;

import java.util.List;
import java.util.function.Predicate;

public interface HWSolutionFolder extends HWSolutionElement {
    WritablePLMap<String, HWSolutionElement> children();

    List<HWProject> findModifiedProjects();

    List<HWProject> findProjects();

    HWSolutionElement findElement(String path);

    HWSolutionElement findElement(String path, boolean createFolder);

    HWSolutionElement remove(String path);

    void add(HWSolutionElement element, String path);

    HWProject addProject(String namePrefix, String path);

    List<HWSolutionElement> removeDeepComponents(Predicate<HWSolutionElement> filter, boolean prune);
}
