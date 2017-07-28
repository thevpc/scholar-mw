package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractComplexBuilder extends AbstractValueBuilder {
    protected MWStructure structure;

    public AbstractComplexBuilder(MWStructure structure) {
        this.structure = structure;
    }

    public MWStructure getStructure() {
        return structure;
    }

    protected abstract Matrix computeMatrixImpl();

    protected final Matrix computeMatrixImplLog(){
        return Maths.invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
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
            return storeConvergenceResult(conv.evaluate(structure, new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ComputationMonitor monitor) {
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
            return storeConvergenceResult(conv.evaluate(structure, new ObjectEvaluator() {
                @Override
                public Complex evaluate(Object momStructure, ComputationMonitor monitor) {
                    return computeComplexImpl();
                }
            }, getMonitor()));
        }
    }
}
