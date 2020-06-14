package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceResult;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(net.vpc.common.mon.ProgressMonitorFactory monitor);

    ValueBuilder monitor(ProgressMonitor monitor);

    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ConvergenceResult getConvergenceResult();

}
