package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceResult;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;


/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractValueBuilder implements ValueBuilder {
    private ProgressMonitor monitor;
    private ConvergenceResult convergenceResult;
    private ConvergenceEvaluator convergenceEvaluator;
    private MWStructure structure;

    public AbstractValueBuilder(MWStructure structure) {
        this.structure = structure;
    }

    public <T extends MWStructure> T getStructure() {
        return (T) structure;
    }

    @Override
    public ValueBuilder monitor(ProgressMonitor monitor) {
        this.monitor = monitor;
        return this;
    }

    @Override
    public ValueBuilder monitor(net.vpc.common.mon.ProgressMonitorFactory monitor) {
        this.monitor = monitor == null ? null : monitor.createMonitor(toString(), getClass().getSimpleName());
        return this;
    }

    @Override
    public ValueBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        this.convergenceEvaluator = convergenceEvaluator;
        return this;
    }

    protected <V> V storeConvergenceResult(ConvergenceResult r) {
        convergenceResult = r;
        return (V) r.getValue();
    }

    public ConvergenceResult getConvergenceResult() {
        return convergenceResult;
    }

    public ProgressMonitor getMonitor() {
        if (monitor != null) {
            return monitor;
        }
        if (structure != null) {
            ProgressMonitorFactory m = structure.monitorFactory();
            if (m != null) {
                ProgressMonitor p = m.createMonitor(toString(), getClass().getSimpleName());
                return ProgressMonitors.nonnull(p);
            }
        }
        return ProgressMonitors.none();
    }

    public ConvergenceEvaluator getConvergenceEvaluator() {
        return convergenceEvaluator;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
