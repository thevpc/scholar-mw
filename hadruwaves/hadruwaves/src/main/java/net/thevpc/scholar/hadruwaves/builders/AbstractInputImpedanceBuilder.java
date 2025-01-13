package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractInputImpedanceBuilder extends AbstractComplexBuilder implements InputImpedanceBuilder{
    public AbstractInputImpedanceBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public InputImpedanceBuilder monitor(ProgressMonitor monitor) {
        return (InputImpedanceBuilder) super.monitor(monitor);
    }

    @Override
    public InputImpedanceBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (InputImpedanceBuilder) super.monitor(monitor);
    }

    @Override
    public InputImpedanceBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (InputImpedanceBuilder) super.converge(convergenceEvaluator);
    }
}
