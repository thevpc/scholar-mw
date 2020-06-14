package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.WritablePLMap;

import java.util.List;
import java.util.function.Predicate;

public class DefaultHWSolutionFolder extends AbstractHWSolutionElement implements HWSolutionFolder {
    private HWSolutionFolderHelper childrenHelper = new HWSolutionFolderHelper(this, () -> solution().get());

    public DefaultHWSolutionFolder(String name) {
        name().set(name);
    }

    @Override
    public WritablePLMap<String, HWSolutionElement> children() {
        return childrenHelper.children();
    }

    @Override
    public List<HWProject> findModifiedProjects() {
        return childrenHelper.findModifiedProjects();
    }

    @Override
    public List<HWProject> findProjects() {
        return childrenHelper.findProjects();
    }

    @Override
    public HWSolutionElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    @Override
    public HWSolutionElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    @Override
    public HWSolutionElement remove(String path) {
        return childrenHelper.remove(path);
    }

    @Override
    public void add(HWSolutionElement element, String path) {
        childrenHelper.add(element, path);
    }

    @Override
    public HWProject addProject(String namePrefix, String path) {
        return childrenHelper.addProject(namePrefix, path);
    }

    @Override
    public List<HWSolutionElement> removeDeepComponents(Predicate<HWSolutionElement> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }


}
