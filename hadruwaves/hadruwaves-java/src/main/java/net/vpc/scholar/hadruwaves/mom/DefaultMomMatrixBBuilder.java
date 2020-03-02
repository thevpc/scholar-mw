package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixBBuilder;

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
