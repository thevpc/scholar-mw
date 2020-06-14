package net.vpc.scholar.hadruwaves.solvers;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWSolverTemplate {

    WritablePValue<String> name();

    WritablePValue<String> description();

    HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager);

}
