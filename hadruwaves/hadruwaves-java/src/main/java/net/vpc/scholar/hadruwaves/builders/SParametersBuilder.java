package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SParametersBuilder extends ValueBuilder{
    public SParametersBuilder monitor(ComputationMonitor monitor);
    public SParametersBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    Matrix computeMatrix();

    Complex computeComplex();

}
