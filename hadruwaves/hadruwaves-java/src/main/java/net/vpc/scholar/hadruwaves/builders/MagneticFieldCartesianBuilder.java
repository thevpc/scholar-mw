package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MagneticFieldCartesianBuilder extends CartesianFieldBuilder {
    MagneticFieldCartesianBuilder monitor(ProgressMonitorFactory monitor);

    MagneticFieldCartesianBuilder monitor(ProgressMonitor monitor);

    MagneticFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator);


}
