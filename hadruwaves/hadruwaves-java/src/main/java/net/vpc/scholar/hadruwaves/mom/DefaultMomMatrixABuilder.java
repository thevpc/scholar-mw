package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixABuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixABuilder extends AbstractMomMatrixABuilder {

    private MomStructure momStructure;
    private ProgressMonitor[] mon;

    public DefaultMomMatrixABuilder(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

    public Matrix computeMatrixImpl() {
        return Maths.invokeMonitoredAction(getMonitor(), "A Builder", new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
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
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeMatrixImplLog();
                }
            }, getMonitor()));
        }
    }

}
