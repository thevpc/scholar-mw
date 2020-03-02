package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractDirectivityBuilder extends AbstractValueBuilder implements DirectivityBuilder {
    public AbstractDirectivityBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public DirectivityBuilder monitor(ProgressMonitor monitor) {
        return (DirectivityBuilder) super.monitor(monitor);
    }

    @Override
    public DirectivityBuilder monitor(TaskMonitorManager monitor) {
        return (DirectivityBuilder) super.monitor(monitor);
    }

    @Override
    public DirectivityBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (DirectivityBuilder) super.converge(convergenceEvaluator);
    }


}
