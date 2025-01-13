package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
    public DirectivityBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (DirectivityBuilder) super.monitor(monitor);
    }

    @Override
    public DirectivityBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (DirectivityBuilder) super.converge(convergenceEvaluator);
    }


}
