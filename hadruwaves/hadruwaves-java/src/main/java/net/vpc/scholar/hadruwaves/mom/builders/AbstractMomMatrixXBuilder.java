package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.builders.AbstractValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMomMatrixXBuilder extends AbstractValueBuilder implements MomMatrixXBuilder{

    @Override
    public MomMatrixXBuilder monitor(ComputationMonitor monitor) {
        return (MomMatrixXBuilder) super.monitor(monitor);
    }

    @Override
    public MomMatrixXBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MomMatrixXBuilder) super.converge(convergenceEvaluator);
    }

}
