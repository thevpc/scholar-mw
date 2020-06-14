package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;

import java.util.List;
import net.vpc.common.props.PValue;
import net.vpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

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
