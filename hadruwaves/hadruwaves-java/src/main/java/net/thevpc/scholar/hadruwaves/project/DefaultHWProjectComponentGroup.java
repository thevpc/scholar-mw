package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PMapEntry;
import net.thevpc.common.props.WritablePLMap;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.Annotation3D;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public class DefaultHWProjectComponentGroup extends AbstractHWProjectComponent implements HWProjectComponentGroup {

    private HWProjectFolderHelper childrenHelper = new HWProjectFolderHelper(this, () -> solution().get());

    public DefaultHWProjectComponentGroup(String name) {
        name().set(name);
    }

    @Override
    public WritablePLMap<String, HWProjectComponent> children() {
        return childrenHelper.children();
    }

//    @Override
    @Override
    public HWProjectComponent findElement(String path) {
        return childrenHelper.findElement(path);
    }

//    @Override
    @Override
    public HWProjectComponent findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

//    @Override
    @Override
    public HWProjectComponent remove(String path) {
        return childrenHelper.remove(path);
    }

//    @Override
    @Override
    public void add(HWProjectComponent element, String path) {
        childrenHelper.add(element, path);
    }

//    @Override
    @Override
    public List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }

    @Override
    public List<Element3D> toElements3DImpl(HWConfigurationRun configuration) {
        List<Element3D> g = new ArrayList<>();
        for (PMapEntry<String, HWProjectComponent> child : children()) {
            g.addAll(child.getValue().toElements3D(configuration));
        }
        return g;
    }

    @Override
    public void updateBoundDomain(HWConfigurationRun configuration, BoundDomain domain) {
        for (HWProjectComponent elem : children().values()) {
            elem.updateBoundDomain(configuration, domain);
        }
        for (Annotation3D annotation : annotations()) {
            for (Element3D element3D : annotation.toElements3D()) {
                if (element3D != null) {
                    domain.include(element3D);
                }
            }
        }
    }

}
