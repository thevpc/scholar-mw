package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
    public CurrentBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (CurrentBuilder) super.monitor(monitor);
    }
    @Override
    public CurrentBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CurrentBuilder) super.converge(convergenceEvaluator);
    }
}
