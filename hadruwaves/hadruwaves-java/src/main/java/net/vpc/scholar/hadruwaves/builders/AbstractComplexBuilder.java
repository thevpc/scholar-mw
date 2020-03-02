package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.common.mon.ProgressMonitor;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractComplexBuilder extends AbstractValueBuilder {

    public AbstractComplexBuilder(MWStructure structure) {
        super(structure);
    }

    protected abstract ComplexMatrix evalMatrixImpl();

    protected final ComplexMatrix evalMatrixImplLog(){
        return Maths.invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                return evalMatrixImpl();
            }
        });
    }

    public Complex evalComplexImpl() {
        return evalMatrixImplLog().toComplex();
    }

    public ComplexMatrix evalMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalMatrixImplLog();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalMatrixImplLog();
                }
            }, getMonitor()));
        }
    }


    public Complex evalComplex() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalComplexImpl();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Complex evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalComplexImpl();
                }
            }, getMonitor()));
        }
    }
}
