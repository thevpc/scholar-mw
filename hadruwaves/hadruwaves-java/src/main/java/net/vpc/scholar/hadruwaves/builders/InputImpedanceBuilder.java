package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface InputImpedanceBuilder extends ValueBuilder {
    public InputImpedanceBuilder monitor(ComputationMonitor monitor);
    public InputImpedanceBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    Matrix computeMatrix();

    Complex computeComplex();

}
