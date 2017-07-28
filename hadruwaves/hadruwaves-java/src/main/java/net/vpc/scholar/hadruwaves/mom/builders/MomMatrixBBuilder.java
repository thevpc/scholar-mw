package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixBBuilder extends ValueBuilder {
    MomMatrixBBuilder monitor(ComputationMonitor monitor);
    MomMatrixBBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;
    Matrix computeMatrix();

}
