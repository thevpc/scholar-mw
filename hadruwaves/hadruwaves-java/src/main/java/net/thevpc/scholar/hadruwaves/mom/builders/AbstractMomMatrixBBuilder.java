package net.thevpc.scholar.hadruwaves.mom.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.builders.AbstractValueBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixBBuilder extends AbstractValueBuilder implements MomMatrixBBuilder{

    public AbstractMomMatrixBBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public MomMatrixBBuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixBBuilder) super.monitor(monitor);
    }
    @Override
    public MomMatrixBBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (MomMatrixBBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixBBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixBBuilder) super.converge(convergenceEvaluator);
    }

}
