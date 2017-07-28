package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractCapacityBuilder extends AbstractComplexBuilder implements CapacityBuilder{
    public AbstractCapacityBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public CapacityBuilder monitor(ComputationMonitor monitor) {
        return (CapacityBuilder) super.monitor(monitor);
    }

    @Override
    public CapacityBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CapacityBuilder) super.converge(convergenceEvaluator);
    }
}
