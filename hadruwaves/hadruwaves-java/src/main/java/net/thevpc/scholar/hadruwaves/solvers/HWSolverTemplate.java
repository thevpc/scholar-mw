package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.props.WritableString;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface HWSolverTemplate {

    WritableString name();

    WritableString description();

    HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager);

}
