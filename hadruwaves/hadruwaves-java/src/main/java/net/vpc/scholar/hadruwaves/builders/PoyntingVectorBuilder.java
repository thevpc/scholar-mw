package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorBuilder extends ValueBuilder {
    PoyntingVectorBuilder monitor(TaskMonitorManager monitor);

    PoyntingVectorBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    PoyntingVectorCartesianBuilder cartesian();

    PoyntingVectorSphericalBuilder spherical();

}
