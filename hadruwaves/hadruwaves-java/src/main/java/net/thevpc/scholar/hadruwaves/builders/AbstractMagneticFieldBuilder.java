package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMagneticFieldBuilder extends AbstractValueBuilder implements MagneticFieldBuilder {
    public AbstractMagneticFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public MagneticFieldBuilder monitor(ProgressMonitor monitor) {
        return (MagneticFieldBuilder) super.monitor(monitor);
    }

    @Override
    public MagneticFieldBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (MagneticFieldBuilder) super.monitor(monitor);
    }

    @Override
    public MagneticFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MagneticFieldBuilder) super.converge(convergenceEvaluator);
    }
}
