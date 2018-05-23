package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractElectricFieldBuilder extends AbstractVDiscreteBuilder implements ElectricFieldBuilder{

    public AbstractElectricFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public ElectricFieldBuilder monitor(ProgressMonitor monitor) {
        return (ElectricFieldBuilder) super.monitor(monitor);
    }


    @Override
    public ElectricFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (ElectricFieldBuilder) super.converge(convergenceEvaluator);
    }
}
