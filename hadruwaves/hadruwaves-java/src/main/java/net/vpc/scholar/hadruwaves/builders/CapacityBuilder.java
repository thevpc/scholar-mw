package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface CapacityBuilder extends ValueBuilder{
    public CapacityBuilder monitor(ComputationMonitor monitor);
    public CapacityBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    Matrix computeMatrix();

    Complex computeComplex();
}
