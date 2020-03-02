package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractSourceBuilder extends AbstractValueBuilder implements SourceBuilder{
    public AbstractSourceBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public SourceBuilder monitor(TaskMonitorManager monitor) {
        return (SourceBuilder) super.monitor(monitor);
    }
    @Override
    public SourceBuilder monitor(ProgressMonitor monitor) {
        return (SourceBuilder) super.monitor(monitor);
    }
    @Override
    public SourceBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (SourceBuilder) super.converge(convergenceEvaluator);
    }
}
