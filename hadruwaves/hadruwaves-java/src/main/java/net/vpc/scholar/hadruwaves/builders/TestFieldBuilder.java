package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface TestFieldBuilder extends CartesianFieldBuilder {
    TestFieldBuilder monitor(TaskMonitorManager monitor);
    TestFieldBuilder monitor(ProgressMonitor monitor);

    TestFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

}
