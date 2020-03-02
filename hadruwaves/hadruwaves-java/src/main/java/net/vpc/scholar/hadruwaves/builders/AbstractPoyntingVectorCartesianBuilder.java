package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractPoyntingVectorCartesianBuilder extends AbstractVDiscreteBuilder implements PoyntingVectorCartesianBuilder {
    public AbstractPoyntingVectorCartesianBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public PoyntingVectorCartesianBuilder monitor(ProgressMonitor monitor) {
        return (PoyntingVectorCartesianBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorCartesianBuilder monitor(TaskMonitorManager monitor) {
        return (PoyntingVectorCartesianBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (PoyntingVectorCartesianBuilder) super.converge(convergenceEvaluator);
    }
}
