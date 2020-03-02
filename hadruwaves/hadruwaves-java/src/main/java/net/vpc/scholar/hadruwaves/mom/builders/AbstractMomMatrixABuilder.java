package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
    public MomMatrixABuilder monitor(TaskMonitorManager monitor) {
        return (MomMatrixABuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixABuilder) super.converge(convergenceEvaluator);
    }
}
