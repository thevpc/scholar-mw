package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.builders.AbstractMomMatrixBBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMomMatrixBBuilder extends AbstractMomMatrixBBuilder {

    MomStructure momStructure;

    public DefaultMomMatrixBBuilder(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

    private Matrix computeMatrixImpl() {
        momStructure.build();
        return new MatrixBMatrixStrCacheSupport(this, getMonitor()).get();
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
