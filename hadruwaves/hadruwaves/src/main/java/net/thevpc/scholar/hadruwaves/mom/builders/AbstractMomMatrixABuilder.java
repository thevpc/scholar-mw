package net.thevpc.scholar.hadruwaves.mom.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.builders.AbstractValueBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixABuilder extends AbstractValueBuilder implements MomMatrixABuilder{

    public AbstractMomMatrixABuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public MomMatrixABuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixABuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixABuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (MomMatrixABuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixABuilder) super.converge(convergenceEvaluator);
    }
}
