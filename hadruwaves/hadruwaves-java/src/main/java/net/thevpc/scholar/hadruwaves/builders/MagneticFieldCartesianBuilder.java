package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MagneticFieldCartesianBuilder extends CartesianFieldBuilder {
    MagneticFieldCartesianBuilder monitor(ProgressMonitorFactory monitor);

    MagneticFieldCartesianBuilder monitor(ProgressMonitor monitor);

    MagneticFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator);


}
