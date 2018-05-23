package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixXBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixXBuilder extends AbstractMomMatrixXBuilder {
    private MomStructure momStructure;

    public DefaultMomMatrixXBuilder(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

    private Matrix computeMatrixImpl() {
        momStructure.build();
        return new MatrixXMatrixStrCacheSupport(momStructure, getMonitor()).get();
    }


    @Override
    public Matrix computeMatrix() {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeMatrixImpl();
        } else {
            return storeConvergenceResult(conv.evaluate(momStructure, new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeMatrixImpl();
                }
            }, getMonitor()));
        }
    }

    @Override
    public Vector computeVector() {
        return computeMatrix().toVector();
    }
}
