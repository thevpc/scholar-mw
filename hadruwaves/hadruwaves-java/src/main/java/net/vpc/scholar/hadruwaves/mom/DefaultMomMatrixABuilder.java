package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.plot.convergence.ObjectEvaluator;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixABuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixABuilder extends AbstractMomMatrixABuilder {

    private ProgressMonitor[] mon;

    public DefaultMomMatrixABuilder(MomStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix evalMatrixImpl() {
        return Maths.invokeMonitoredAction(getMonitor(), "A Builder", new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                MomStructure momStructure = getStructure();
                momStructure.build();
                return new MatrixAMatrixStrCacheSupport(momStructure, monitor).get();
            }
        });
    }


    public ComplexMatrix evalMatrixImplLog() {
        return evalMatrixImpl();
    }

    @Override
    public ComplexMatrix evalMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalMatrixImplLog();
        } else {
            MomStructure momStructure = getStructure();
            return storeConvergenceResult(conv.evaluate(momStructure, new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalMatrixImplLog();
                }
            }, getMonitor()));
        }
    }

}
