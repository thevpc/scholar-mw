package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface CapacityBuilder extends ValueBuilder {
    CapacityBuilder monitor(ProgressMonitor monitor);

    CapacityBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    Matrix computeMatrix();

    Complex computeComplex();
}
