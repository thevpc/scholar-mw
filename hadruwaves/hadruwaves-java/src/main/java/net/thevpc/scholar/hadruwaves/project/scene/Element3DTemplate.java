package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface Element3DTemplate extends TsonSerializable{
    Element3D toElements3D(HWConfigurationRun configuration);
}
