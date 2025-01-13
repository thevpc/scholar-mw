package net.thevpc.scholar.hadruwaves.mom.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.builders.AbstractValueBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixXBuilder extends AbstractValueBuilder implements MomMatrixXBuilder{

    public AbstractMomMatrixXBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public MomMatrixXBuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixXBuilder) super.monitor(monitor);
    }
    @Override
    public MomMatrixXBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (MomMatrixXBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixXBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixXBuilder) super.converge(convergenceEvaluator);
    }

}
