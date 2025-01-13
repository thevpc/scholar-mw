package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractTestFieldBuilder extends AbstractVDiscreteBuilder implements TestFieldBuilder{
    public AbstractTestFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public TestFieldBuilder monitor(ProgressMonitor monitor) {
        return (TestFieldBuilder) super.monitor(monitor);
    }

    @Override
    public TestFieldBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (TestFieldBuilder) super.monitor(monitor);
    }

    @Override
    public TestFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (TestFieldBuilder) super.converge(convergenceEvaluator);
    }
}
