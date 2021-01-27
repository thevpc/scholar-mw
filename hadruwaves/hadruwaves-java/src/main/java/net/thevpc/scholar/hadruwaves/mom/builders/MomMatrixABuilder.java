package net.thevpc.scholar.hadruwaves.mom.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixABuilder extends ValueBuilder {
    MomMatrixABuilder monitor(ProgressMonitorFactory monitor);

    MomMatrixABuilder monitor(ProgressMonitor monitor);

    MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix();

}
