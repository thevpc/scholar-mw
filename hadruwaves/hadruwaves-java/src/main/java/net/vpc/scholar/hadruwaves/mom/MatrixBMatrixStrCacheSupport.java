package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixBMatrixStrCacheSupport extends StrCacheSupport<ComplexMatrix> {

    private DefaultMomMatrixBBuilder defaultMomMatrixBBuilder;
    private final ProgressMonitor[] mon;

    public MatrixBMatrixStrCacheSupport(DefaultMomMatrixBBuilder defaultMomMatrixBBuilder, ProgressMonitor mon) {
        super(defaultMomMatrixBBuilder.getStructure(), MomStructure.CACHE_MATRIX_B,mon);
        this.defaultMomMatrixBBuilder = defaultMomMatrixBBuilder;
        this.mon = getMonitor().split(2,8);
    }

    protected void init(ProgressMonitor cacheMonitor) {
        MomStructure momStructure = defaultMomMatrixBBuilder.getStructure();
        momStructure.getTestModeScalarProducts(mon[0]);
    }

    public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
        MomStructure momStructure = defaultMomMatrixBBuilder.getStructure();
        ComplexMatrix matrix = momStructure.evaluator().createMatrixBEvaluator().evaluate(momStructure, mon[1]);
        Number ceil = momStructure.getHintsManager().getHintBMatrixSparsify();
        if (ceil != null) {
            matrix = matrix.sparsify(ceil.doubleValue());
        }
        if (matrix.norm1() == 0) {
            throw new IllegalArgumentException("B matrix is Null (norm=0)");
        }
        return matrix;
    }
}
