package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixXMatrixStrCacheSupport extends StrCacheSupport<Matrix> {

    private MomStructure momStructure;

    public MatrixXMatrixStrCacheSupport(MomStructure momStructure,ProgressMonitor monitor) {
        super(momStructure, MomStructure.CACHE_MATRIX_UNKNOWN,monitor);
        this.momStructure = momStructure;
    }

    @Override
    public Matrix compute(ObjectCache momCache) {
        return momStructure.createMatrixUnknownEvaluator().evaluate(momStructure, getMonitor());
    }
}
