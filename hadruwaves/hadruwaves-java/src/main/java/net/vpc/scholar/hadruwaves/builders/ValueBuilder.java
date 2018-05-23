package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceResult;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(ProgressMonitor monitor);

    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ConvergenceResult getConvergenceResult();

}
