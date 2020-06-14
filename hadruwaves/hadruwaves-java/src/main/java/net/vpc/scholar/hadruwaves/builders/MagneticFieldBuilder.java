package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MagneticFieldBuilder extends ValueBuilder {
    MagneticFieldBuilder monitor(ProgressMonitorFactory monitor);

    MagneticFieldBuilder monitor(ProgressMonitor monitor);

    MagneticFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    MagneticFieldCartesianBuilder cartesian();

    MagneticFieldSphericalBuilder spherical();
}
