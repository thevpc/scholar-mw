package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixABuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixABuilder extends AbstractMomMatrixABuilder {

    private ProgressMonitor[] mon;

    public DefaultMomMatrixABuilder(MomStructure momStructure) {
        super(momStructure);
    }

    public Matrix computeMatrixImpl() {
        return Maths.invokeMonitoredAction(getMonitor(), "A Builder", new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                MomStructure momStructure = getStructure();
                momStructure.build();
                return new MatrixAMatrixStrCacheSupport(momStructure, monitor).get();
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
            MomStructure momStructure = getStructure();
            return storeConvergenceResult(conv.evaluate(momStructure, new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeMatrixImplLog();
                }
            }, getMonitor()));
        }
    }

}
