package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractCurrentBuilder extends AbstractVDiscreteBuilder implements CurrentBuilder{
    public AbstractCurrentBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public CurrentBuilder monitor(ProgressMonitor monitor) {
        return (CurrentBuilder) super.monitor(monitor);
    }
    @Override
    public CurrentBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CurrentBuilder) super.converge(convergenceEvaluator);
    }
}
