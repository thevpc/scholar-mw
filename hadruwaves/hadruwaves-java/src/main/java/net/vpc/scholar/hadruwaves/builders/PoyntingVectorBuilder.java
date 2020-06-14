package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorBuilder extends ValueBuilder {
    PoyntingVectorBuilder monitor(ProgressMonitorFactory monitor);

    PoyntingVectorBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    PoyntingVectorCartesianBuilder cartesian();

    PoyntingVectorSphericalBuilder spherical();

}
