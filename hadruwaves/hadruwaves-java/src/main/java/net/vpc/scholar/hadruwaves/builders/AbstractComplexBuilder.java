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

    protected abstract Matrix computeMatrixImpl();

    protected final Matrix computeMatrixImplLog(){
        return Maths.invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                return computeMatrixImpl();
            }
        });
    }

    public Complex computeComplexImpl() {
        return computeMatrixImplLog().toComplex();
    }

    public Matrix computeMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeMatrixImplLog();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeMatrixImplLog();
                }
            }, getMonitor()));
        }
    }


    public Complex computeComplex() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeComplexImpl();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Complex evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeComplexImpl();
                }
            }, getMonitor()));
        }
    }
}
