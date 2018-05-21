package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
    public InputImpedanceBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (InputImpedanceBuilder) super.converge(convergenceEvaluator);
    }
}
