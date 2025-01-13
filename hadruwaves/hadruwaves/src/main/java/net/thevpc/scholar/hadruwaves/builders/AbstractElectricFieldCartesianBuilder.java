package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
    public ElectricFieldCartesianBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (ElectricFieldCartesianBuilder) super.monitor(monitor);
    }


    @Override
    public ElectricFieldCartesianBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (ElectricFieldCartesianBuilder) super.converge(convergenceEvaluator);
    }
}
