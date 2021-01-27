package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceResult;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ValueBuilder {
    ValueBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor);

    ValueBuilder monitor(ProgressMonitor monitor);

    ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ConvergenceResult getConvergenceResult();

}
