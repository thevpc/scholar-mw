package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
//import net.thevpc.common.props.ObservableValue;
//import net.thevpc.common.props.WritableList;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.HWSolution;

import java.util.List;
import java.util.function.Predicate;
import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableList;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWProjectScene extends TsonSerializable{

    ObservableValue<HWProject> project();

    ObservableValue<HWSolution> solution();

    DomainTemplate domain();

    WritableList<HWProjectComponent> components();

    List<HWProjectComponent> findDeepComponents(Predicate<HWProjectComponent> filter);

    List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter);

    Domain domain(HWConfigurationRun configuration);

    List<Element3D> toElements3D(HWConfigurationRun configuration);

    Domain getBoundingDomain(HWConfigurationRun configuration, float margin);
}
