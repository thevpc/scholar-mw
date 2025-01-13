package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
    public SParametersBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (SParametersBuilder) super.monitor(monitor);
    }

    @Override
    public SParametersBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (SParametersBuilder) super.converge(convergenceEvaluator);
    }
}
