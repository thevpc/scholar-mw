package net.thevpc.scholar.hadruwaves.mom.builders;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixBBuilder extends ValueBuilder {
    MomMatrixBBuilder monitor(ProgressMonitorFactory monitor);
    MomMatrixBBuilder monitor(ProgressMonitor monitor);
    MomMatrixBBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;
    ComplexMatrix evalMatrix();
    ComplexVector evalVector();

}
