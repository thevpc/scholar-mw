package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixABuilder extends AbstractValueBuilder implements MomMatrixABuilder{

    @Override
    public MomMatrixABuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixABuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixABuilder) super.converge(convergenceEvaluator);
    }
}
