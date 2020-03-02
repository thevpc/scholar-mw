package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.PValue;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.util.List;

public interface StructureNode {
    WritablePValue<String> name();
    WritablePValue<Boolean> enabled();

    List<Element3D> toElements3D(HWProjectEnv env);
}
