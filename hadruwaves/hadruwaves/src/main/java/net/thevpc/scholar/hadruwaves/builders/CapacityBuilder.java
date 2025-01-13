package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface CapacityBuilder extends ValueBuilder {
    CapacityBuilder monitor(ProgressMonitorFactory monitor);
    CapacityBuilder monitor(ProgressMonitor monitor);

    CapacityBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix();

    Complex evalComplex();
}
