package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruwaves.project.HWProjectComponent;
import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePList;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;

import java.util.List;
import java.util.function.Predicate;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWProjectScene extends TsonSerializable{

    PValue<HWProject> project();

    PValue<HWSolution> solution();

    DomainTemplate domain();

    WritablePList<HWProjectComponent> components();

    List<HWProjectComponent> findDeepComponents(Predicate<HWProjectComponent> filter);

    List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter);

    Domain domain(HWConfigurationRun configuration);

    List<Element3D> toElements3D(HWConfigurationRun configuration);

    Domain getBoundingDomain(HWConfigurationRun configuration, float margin);
}
