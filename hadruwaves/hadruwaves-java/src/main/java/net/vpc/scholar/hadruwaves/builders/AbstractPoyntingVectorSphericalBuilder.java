package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractPoyntingVectorSphericalBuilder extends AbstractValueBuilder implements PoyntingVectorSphericalBuilder {
    public AbstractPoyntingVectorSphericalBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public PoyntingVectorSphericalBuilder monitor(ProgressMonitor monitor) {
        return (PoyntingVectorSphericalBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorSphericalBuilder monitor(TaskMonitorManager monitor) {
        return (PoyntingVectorSphericalBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorSphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (PoyntingVectorSphericalBuilder) super.converge(convergenceEvaluator);
    }


}
