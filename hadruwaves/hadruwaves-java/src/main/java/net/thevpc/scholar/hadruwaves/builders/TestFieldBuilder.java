package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface TestFieldBuilder extends CartesianFieldBuilder {
    TestFieldBuilder monitor(ProgressMonitorFactory monitor);
    TestFieldBuilder monitor(ProgressMonitor monitor);

    TestFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

}
