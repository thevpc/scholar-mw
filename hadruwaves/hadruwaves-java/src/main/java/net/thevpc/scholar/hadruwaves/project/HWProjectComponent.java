package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.List;
import net.thevpc.common.props.PValue;
import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

public interface HWProjectComponent extends HWProjectElement{

    PValue<HWProjectComponentGroup> parent();

    PValue<String> parentPath();

    WritablePValue<String> description();

    WritablePValue<Boolean> selected();

    WritablePExpression<Boolean> enabled();

    WritablePValue<Boolean> visible();

    List<Element3D> toElements3D(HWConfigurationRun configuration);

    void updateBoundDomain(HWConfigurationRun configuration, BoundDomain domain);
}
