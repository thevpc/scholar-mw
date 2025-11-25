package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface Element3DTemplate extends NToElement{
    Element3D toElements3D(HWConfigurationRun configuration);
}
