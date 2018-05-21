package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixXBuilder extends AbstractValueBuilder implements MomMatrixXBuilder{

    @Override
    public MomMatrixXBuilder monitor(ProgressMonitor monitor) {
        return (MomMatrixXBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixXBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixXBuilder) super.converge(convergenceEvaluator);
    }

}
