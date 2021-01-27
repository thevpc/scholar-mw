package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractDirectivitySphericalBuilder extends AbstractValueBuilder implements DirectivitySphericalBuilder {
    public AbstractDirectivitySphericalBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public DirectivitySphericalBuilder monitor(ProgressMonitor monitor) {
        return (DirectivitySphericalBuilder) super.monitor(monitor);
    }

    @Override
    public DirectivitySphericalBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (DirectivitySphericalBuilder) super.monitor(monitor);
    }

    @Override
    public DirectivitySphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (DirectivitySphericalBuilder) super.converge(convergenceEvaluator);
    }


}
