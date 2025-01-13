package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
    public SelfBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (SelfBuilder) super.monitor(monitor);
    }

    @Override
    public SelfBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (SelfBuilder) super.converge(convergenceEvaluator);
    }

}
