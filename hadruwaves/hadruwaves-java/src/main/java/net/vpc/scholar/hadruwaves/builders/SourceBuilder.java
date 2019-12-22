package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SourceBuilder extends ValueBuilder{
    SourceBuilder monitor(ProgressTaskMonitor monitor);
    SourceBuilder monitor(ProgressMonitor monitor);
    SourceBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    ComplexMatrix computeMatrix(Axis axis, double[] x, double[] y, double z);

//    VDiscrete computeVDiscrete(Samples samples);
//
//    VDiscrete computeVDiscrete(double[] x, double[] y);
//
//    public ConvergenceResult<VDiscrete> computeConvergenceByFnMax(int[] fnMax, final double[] x, final double[] y, ConvergenceConfig convPars) ;

}
