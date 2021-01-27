package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMagneticFieldCartesianBuilder extends AbstractVDiscreteBuilder implements MagneticFieldCartesianBuilder {

    public AbstractMagneticFieldCartesianBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public MagneticFieldCartesianBuilder monitor(ProgressMonitor monitor) {
        return (MagneticFieldCartesianBuilder) super.monitor(monitor);
    }

    @Override
    public MagneticFieldCartesianBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (MagneticFieldCartesianBuilder) super.monitor(monitor);
    }


    @Override
    public MagneticFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MagneticFieldCartesianBuilder) super.converge(convergenceEvaluator);
    }
}
