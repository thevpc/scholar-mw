package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWSolverTemplate {

    WritableValue<String> name();

    WritableValue<String> description();

    HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager);

}
