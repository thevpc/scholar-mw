package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

public interface Element3DTemplate {
    Element3D toElements3D(HWProjectEnv env);
}
