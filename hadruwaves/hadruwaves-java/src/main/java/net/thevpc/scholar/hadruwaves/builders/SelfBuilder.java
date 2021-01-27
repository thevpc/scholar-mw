package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SelfBuilder extends ValueBuilder{
    public SelfBuilder monitor(ProgressMonitorFactory monitor);
    public SelfBuilder monitor(ProgressMonitor monitor);
    public SelfBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    ComplexMatrix evalMatrix();

    Complex evalComplex();

}
