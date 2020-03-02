package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
    public MomMatrixBBuilder monitor(TaskMonitorManager monitor) {
        return (MomMatrixBBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixBBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixBBuilder) super.converge(convergenceEvaluator);
    }

}
