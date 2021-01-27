package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractPoyntingVectorBuilder extends AbstractValueBuilder implements PoyntingVectorBuilder {
    public AbstractPoyntingVectorBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public PoyntingVectorBuilder monitor(ProgressMonitor monitor) {
        return (PoyntingVectorBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (PoyntingVectorBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (PoyntingVectorBuilder) super.converge(convergenceEvaluator);
    }


}
