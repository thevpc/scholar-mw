package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
    public MagneticFieldCartesianBuilder monitor(TaskMonitorManager monitor) {
        return (MagneticFieldCartesianBuilder) super.monitor(monitor);
    }


    @Override
    public MagneticFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MagneticFieldCartesianBuilder) super.converge(convergenceEvaluator);
    }
}
