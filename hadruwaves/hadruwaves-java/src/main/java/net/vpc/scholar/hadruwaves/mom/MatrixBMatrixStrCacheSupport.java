package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixBMatrixStrCacheSupport extends StrCacheSupport<Matrix> {

    private DefaultMomMatrixBBuilder defaultMomMatrixBBuilder;
    private final ProgressMonitor[] mon;

    public MatrixBMatrixStrCacheSupport(DefaultMomMatrixBBuilder defaultMomMatrixBBuilder, ProgressMonitor mon) {
        super(defaultMomMatrixBBuilder.momStructure, MomStructure.CACHE_MATRIX_B,mon);
        this.defaultMomMatrixBBuilder = defaultMomMatrixBBuilder;
        this.mon = getMonitor().split(new double[]{2,8});
    }

    protected void init() {
        defaultMomMatrixBBuilder.momStructure.getTestModeScalarProducts(mon[0]);
    }

    public Matrix compute(ObjectCache momCache) {
        Matrix matrix = defaultMomMatrixBBuilder.momStructure.createMatrixBEvaluator().evaluate(defaultMomMatrixBBuilder.momStructure, mon[1]);
        Number ceil = defaultMomMatrixBBuilder.momStructure.getHintsManager().getHintBMatrixSparsify();
        if (ceil != null) {
            matrix = matrix.sparsify(ceil.doubleValue());
        }
        if (matrix.norm1() == 0) {
            throw new IllegalArgumentException("B matrix is Null (norm=0)");
        }
        return matrix;
    }
}
