package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixXBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixXBuilder extends AbstractMomMatrixXBuilder {

    public DefaultMomMatrixXBuilder(MomStructure momStructure) {
        super(momStructure);
    }

    private ComplexMatrix computeMatrixImpl() {
        MomStructure momStructure = getStructure();
        momStructure.build();
        return new MatrixXMatrixStrCacheSupport(momStructure, getMonitor()).get();
    }


    @Override
    public ComplexMatrix computeMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeMatrixImpl();
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeMatrixImpl();
                }
            }, getMonitor()));
        }
    }

    @Override
    public ComplexVector computeVector() {
        return computeMatrix().toVector();
    }
}
