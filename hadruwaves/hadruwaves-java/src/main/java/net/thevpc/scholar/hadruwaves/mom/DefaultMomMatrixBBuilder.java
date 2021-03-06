package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.plot.convergence.ObjectEvaluator;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixBBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixBBuilder extends AbstractMomMatrixBBuilder {


    public DefaultMomMatrixBBuilder(MomStructure momStructure) {
        super(momStructure);
    }

    private ComplexMatrix evalMatrixImpl() {
        MomStructure momStructure = getStructure();
        momStructure.build();
        return new MatrixBMatrixStrCacheSupport(this, getMonitor()).get();
    }

    @Override
    public ComplexMatrix evalMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalMatrixImpl();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalMatrixImpl();
                }
            }, getMonitor()));
        }
    }

    @Override
    public ComplexVector evalVector() {
        return evalMatrix().toVector();
    }
}
