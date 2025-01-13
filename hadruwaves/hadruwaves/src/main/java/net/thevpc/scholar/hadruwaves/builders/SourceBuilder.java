package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SourceBuilder extends ValueBuilder{
    SourceBuilder monitor(ProgressMonitorFactory monitor);
    SourceBuilder monitor(ProgressMonitor monitor);
    SourceBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    ComplexMatrix evalMatrix(Axis axis, double[] x, double[] y, double z);

//    VDiscrete evalVDiscrete(Samples samples);
//
//    VDiscrete evalVDiscrete(double[] x, double[] y);
//
//    public ConvergenceResult<VDiscrete> computeConvergenceByFnMax(int[] fnMax, final double[] x, final double[] y, ConvergenceConfig convPars) ;

}
