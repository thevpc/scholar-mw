package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWSolverTemplate {

    WritablePValue<String> name();

    WritablePValue<String> description();

    HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager);

}
