package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.plot.convergence.ObjectEvaluator;
import net.thevpc.common.mon.ProgressMonitor;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractComplexBuilder extends AbstractValueBuilder {

    public AbstractComplexBuilder(MWStructure structure) {
        super(structure);
    }

    protected abstract ComplexMatrix evalMatrixImpl(ProgressMonitor evalMonitor);

    protected final ComplexMatrix evalMatrixImplLog(ProgressMonitor evalMonitor){
        return Maths.invokeMonitoredAction(evalMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                return evalMatrixImpl(monitor);
            }
        });
    }

    public Complex evalComplexImpl(ProgressMonitor evalMonitor) {
        return evalMatrixImplLog(evalMonitor).toComplex();
    }

    public ComplexMatrix evalMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalMatrixImplLog(getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalMatrixImplLog(monitor);
                }
            }, getMonitor()));
        }
    }


    public Complex evalComplex() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalComplexImpl(getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Complex evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalComplexImpl(monitor);
                }
            }, getMonitor()));
        }
    }
}
