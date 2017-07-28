package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceResult;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(ComputationMonitor monitor);
    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;
    ConvergenceResult getConvergenceResult();

}
