package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class FnGpScalarProductCacheStrCacheSupport extends StrCacheSupport<ComplexMatrix> {

    private MomStructure momStructure;

    public FnGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_FNGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public ComplexMatrix eval(ObjectCache momCache) {
        ProgressMonitor monitor = getMonitor();
        ProgressMonitor[] mon = monitor.split(new double[]{.1, .9});
        momStructure.initComputation(mon[0]);
        return momStructure.createScalarProductCache(mon[1]);
    }
}
