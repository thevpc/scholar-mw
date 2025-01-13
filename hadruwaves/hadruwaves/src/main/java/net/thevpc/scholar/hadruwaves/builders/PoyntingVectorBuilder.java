package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

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
