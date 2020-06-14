package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface Element3DTemplate extends TsonSerializable{
    Element3D toElements3D(HWConfigurationRun configuration);
}
