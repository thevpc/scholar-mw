package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.WritablePList;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.util.List;
import java.util.function.Predicate;

public interface HWScene {
    DomainTemplate domain();

    WritablePList<StructureNode> components();

    List<StructureNode> findDeepComponents(Predicate<StructureNode> filter);

    Domain domain(HWProjectEnv env);

    List<Element3D> toElements3D(HWProjectEnv env);
}
