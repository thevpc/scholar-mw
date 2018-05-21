package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractTestFieldBuilder extends AbstractVDiscreteBuilder implements TestFieldBuilder{
    public AbstractTestFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public TestFieldBuilder monitor(ProgressMonitor monitor) {
        return (TestFieldBuilder) super.monitor(monitor);
    }

    @Override
    public TestFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (TestFieldBuilder) super.converge(convergenceEvaluator);
    }
}
