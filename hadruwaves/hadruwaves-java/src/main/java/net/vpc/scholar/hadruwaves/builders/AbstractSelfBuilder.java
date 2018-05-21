package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractSelfBuilder extends AbstractComplexBuilder implements SelfBuilder {

    public AbstractSelfBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public SelfBuilder monitor(ProgressMonitor monitor) {
        return (SelfBuilder) super.monitor(monitor);
    }
    @Override
    public SelfBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (SelfBuilder) super.converge(convergenceEvaluator);
    }

}
