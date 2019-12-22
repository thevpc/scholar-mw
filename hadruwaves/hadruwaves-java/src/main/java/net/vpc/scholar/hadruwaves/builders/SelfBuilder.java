package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SelfBuilder extends ValueBuilder{
    public SelfBuilder monitor(ProgressTaskMonitor monitor);
    public SelfBuilder monitor(ProgressMonitor monitor);
    public SelfBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    ComplexMatrix computeMatrix();

    Complex computeComplex();

}
