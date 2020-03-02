package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractSParametersBuilder extends AbstractComplexBuilder implements SParametersBuilder{
    public AbstractSParametersBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public SParametersBuilder monitor(ProgressMonitor monitor) {
        return (SParametersBuilder) super.monitor(monitor);
    }

    @Override
    public SParametersBuilder monitor(TaskMonitorManager monitor) {
        return (SParametersBuilder) super.monitor(monitor);
    }

    @Override
    public SParametersBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (SParametersBuilder) super.converge(convergenceEvaluator);
    }
}
