package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractPoyntingVectorBuilder extends AbstractVDiscreteBuilder implements PoyntingVectorBuilder{
    public AbstractPoyntingVectorBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public PoyntingVectorBuilder monitor(ProgressMonitor monitor) {
        return (PoyntingVectorBuilder) super.monitor(monitor);
    }

    @Override
    public PoyntingVectorBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (PoyntingVectorBuilder) super.converge(convergenceEvaluator);
    }
}
