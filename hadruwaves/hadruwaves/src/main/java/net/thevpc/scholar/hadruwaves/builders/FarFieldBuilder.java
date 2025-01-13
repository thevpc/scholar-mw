package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface FarFieldBuilder extends ValueBuilder {
    FarFieldBuilder monitor(ProgressMonitorFactory monitor);
    FarFieldBuilder monitor(ProgressMonitor monitor);

    FarFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix(SAxis axis, double[] theta, double[] phi, double r);

    Vector<ComplexMatrix> evalMatrices(double[] theta, double[] phi, double r);
}
