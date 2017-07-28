package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixABuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixABuilder extends AbstractMomMatrixABuilder {

    private MomStructure momStructure;
    private ComputationMonitor[] mon;

    public DefaultMomMatrixABuilder(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

    public Matrix computeMatrixImpl() {
        return Maths.invokeMonitoredAction(getMonitor(), "A Builder", new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                momStructure.build();
                return new MatrixAMatrixStrCacheSupport(momStructure, getMonitor()).get();
            }
        });
    }


    public Matrix computeMatrixImplLog() {
        return computeMatrixImpl();
    }

    @Override
    public Matrix computeMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeMatrixImplLog();
        } else {
            return storeConvergenceResult(conv.evaluate(momStructure, new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ComputationMonitor monitor) {
                    return computeMatrixImplLog();
                }
            }, getMonitor()));
        }
    }

}
