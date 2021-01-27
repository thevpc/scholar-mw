package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractSourceBuilder extends AbstractValueBuilder implements SourceBuilder{
    public AbstractSourceBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public SourceBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
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
