package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;
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
    public CurrentBuilder monitor(ProgressTaskMonitor monitor) {
        return (CurrentBuilder) super.monitor(monitor);
    }
    @Override
    public CurrentBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CurrentBuilder) super.converge(convergenceEvaluator);
    }
}
