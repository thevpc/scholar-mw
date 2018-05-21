package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceResult;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(ProgressMonitor monitor);
    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;
    ConvergenceResult getConvergenceResult();

}
