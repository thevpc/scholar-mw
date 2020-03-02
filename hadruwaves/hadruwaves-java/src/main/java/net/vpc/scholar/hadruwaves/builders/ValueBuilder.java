package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceResult;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(TaskMonitorManager monitor);

    ValueBuilder monitor(ProgressMonitor monitor);

    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ConvergenceResult getConvergenceResult();

}
