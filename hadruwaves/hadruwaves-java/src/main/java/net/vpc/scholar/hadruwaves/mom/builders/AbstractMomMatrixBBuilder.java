package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixBBuilder extends AbstractValueBuilder implements MomMatrixBBuilder{

    @Override
    public MomMatrixBBuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixBBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixBBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixBBuilder) super.converge(convergenceEvaluator);
    }

}
