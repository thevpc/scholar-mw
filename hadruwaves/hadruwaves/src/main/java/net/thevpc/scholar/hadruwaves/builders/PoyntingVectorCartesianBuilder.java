package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorCartesianBuilder extends CartesianFieldBuilder {
    PoyntingVectorCartesianBuilder monitor(ProgressMonitorFactory monitor);
    PoyntingVectorCartesianBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator);

}
