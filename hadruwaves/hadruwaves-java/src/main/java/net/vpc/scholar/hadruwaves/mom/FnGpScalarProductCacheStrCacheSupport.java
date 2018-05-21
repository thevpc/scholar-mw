package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.TMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class FnGpScalarProductCacheStrCacheSupport extends StrCacheSupport<TMatrix<Complex>> {

    private MomStructure momStructure;

    public FnGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_FNGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public TMatrix<Complex> compute(ObjectCache momCache) {
        EnhancedProgressMonitor monitor = getMonitor();
        EnhancedProgressMonitor[] mon = monitor.split(new double[]{.1, .9});
        momStructure.initComputation(mon[0]);
        return momStructure.createScalarProductCache(mon[1]);
    }
}
