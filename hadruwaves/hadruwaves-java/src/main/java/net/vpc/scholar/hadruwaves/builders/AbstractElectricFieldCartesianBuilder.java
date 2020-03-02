package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractElectricFieldCartesianBuilder extends AbstractVDiscreteBuilder implements ElectricFieldCartesianBuilder {

    public AbstractElectricFieldCartesianBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public ElectricFieldCartesianBuilder monitor(ProgressMonitor monitor) {
        return (ElectricFieldCartesianBuilder) super.monitor(monitor);
    }

    @Override
    public ElectricFieldCartesianBuilder monitor(TaskMonitorManager monitor) {
        return (ElectricFieldCartesianBuilder) super.monitor(monitor);
    }


    @Override
    public ElectricFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (ElectricFieldCartesianBuilder) super.converge(convergenceEvaluator);
    }
}
