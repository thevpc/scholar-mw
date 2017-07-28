package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixAMatrixStrCacheSupport extends StrCacheSupport<Matrix> {

    private MomStructure momStructure;
    private EnhancedComputationMonitor[] mon;

    public MatrixAMatrixStrCacheSupport(MomStructure momStructure, EnhancedComputationMonitor monitor) {
        super(momStructure, MomStructure.CACHE_MATRIX_A,monitor);
        this.momStructure = momStructure;
        this.mon = getMonitor().split(new double[]{2,8});
    }

    protected void init() {
        momStructure.getTestModeScalarProducts(mon[0]);
    }

    public Matrix compute(ObjectCache momCache) {
        Matrix matrix = momStructure.createMatrixAEvaluator().evaluate(momStructure, mon[1]);
        Number ceil = momStructure.getHintsManager().getHintAMatrixSparsify();
        if (ceil != null && !Double.isNaN(ceil.doubleValue()) && ceil.doubleValue() > 0) {
            matrix = matrix.sparsify(ceil.doubleValue());
        }
        return matrix;
    }
}
