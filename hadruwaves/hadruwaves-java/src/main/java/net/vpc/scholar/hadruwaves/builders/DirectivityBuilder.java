package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface DirectivityBuilder extends ValueBuilder {
    DirectivityBuilder monitor(ProgressMonitorFactory monitor);

    DirectivityBuilder monitor(ProgressMonitor monitor);

    DirectivityBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    DirectivitySphericalBuilder spherical();
}
