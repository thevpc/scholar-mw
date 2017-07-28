package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixABuilder extends ValueBuilder {
    MomMatrixABuilder monitor(ComputationMonitor monitor);

    MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator);

    Matrix computeMatrix();

}
