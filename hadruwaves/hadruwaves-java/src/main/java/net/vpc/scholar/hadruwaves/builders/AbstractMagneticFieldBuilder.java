package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
    public MagneticFieldBuilder monitor(net.vpc.common.mon.ProgressMonitorFactory monitor) {
        return (MagneticFieldBuilder) super.monitor(monitor);
    }

    @Override
    public MagneticFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MagneticFieldBuilder) super.converge(convergenceEvaluator);
    }
}
