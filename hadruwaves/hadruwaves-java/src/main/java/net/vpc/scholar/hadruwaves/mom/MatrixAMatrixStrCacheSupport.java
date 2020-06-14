package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixAMatrixStrCacheSupport extends StrCacheSupport<ComplexMatrix> {

    private MomStructure momStructure;

    public MatrixAMatrixStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor) {
        super(momStructure, MomStructure.CACHE_MATRIX_A,monitor);
        this.momStructure = momStructure;
    }

    protected void init(ProgressMonitor cacheMonitor) {
        momStructure.getTestModeScalarProducts(cacheMonitor);
    }

    public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
        ComplexMatrix matrix = momStructure.evaluator().createMatrixAEvaluator().evaluate(momStructure, cacheMonitor);
        Number ceil = momStructure.getHintsManager().getHintAMatrixSparsify();
        if (ceil != null && !Double.isNaN(ceil.doubleValue()) && ceil.doubleValue() > 0) {
            matrix = matrix.sparsify(ceil.doubleValue());
        }
        return matrix;
    }
}
