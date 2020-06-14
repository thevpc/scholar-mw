package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorCartesianBuilder extends CartesianFieldBuilder {
    PoyntingVectorCartesianBuilder monitor(ProgressMonitorFactory monitor);
    PoyntingVectorCartesianBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator);

}
