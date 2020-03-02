package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorCartesianBuilder extends CartesianFieldBuilder {
    PoyntingVectorCartesianBuilder monitor(TaskMonitorManager monitor);
    PoyntingVectorCartesianBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator);

}
