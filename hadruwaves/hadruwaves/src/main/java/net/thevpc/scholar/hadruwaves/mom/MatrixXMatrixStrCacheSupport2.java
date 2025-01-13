package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class MatrixXMatrixStrCacheSupport2 extends StrCacheSupport<ComplexMatrix> {

    private MomStructure momStructure;

    public MatrixXMatrixStrCacheSupport2(MomStructure momStructure,ProgressMonitor monitor) {
        super(momStructure, MomStructure.CACHE_MATRIX_UNKNOWN,monitor);
        this.momStructure = momStructure;
    }

    @Override
    public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
        return null;
    }
}
