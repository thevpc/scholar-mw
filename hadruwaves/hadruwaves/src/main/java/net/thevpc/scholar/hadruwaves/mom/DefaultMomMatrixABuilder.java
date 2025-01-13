package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.plot.convergence.ObjectEvaluator;
import net.thevpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixABuilder;

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
