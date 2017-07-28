package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceResult;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractValueBuilder implements ValueBuilder{
    private ComputationMonitor monitor;
    private ConvergenceResult convergenceResult;
    private ConvergenceEvaluator convergenceEvaluator;

    @Override
    public ValueBuilder monitor(ComputationMonitor monitor) {
        this.monitor=monitor;
        return this;
    }

    @Override
    public ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        this.convergenceEvaluator=convergenceEvaluator;
        return this;
    }

    protected <V> V storeConvergenceResult(ConvergenceResult r){
        convergenceResult =r;
        return (V)r.getValue();
    }

    public ConvergenceResult getConvergenceResult() {
        return convergenceResult;
    }

    public EnhancedComputationMonitor getMonitor() {
        return ComputationMonitorFactory.enhance(monitor);
    }

    public ConvergenceEvaluator getConvergenceEvaluator() {
        return convergenceEvaluator;
    }
}
