package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class FnGpScalarProductCacheStrCacheSupport extends StrCacheSupport<ScalarProductCache> {

    private MomStructure momStructure;

    public FnGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ComputationMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_FNGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public ScalarProductCache compute(ObjectCache momCache) {
        EnhancedComputationMonitor monitor = getMonitor();
        EnhancedComputationMonitor[] mon = monitor.split(new double[]{.1, .9});
        momStructure.initComputation(mon[0]);
        return momStructure.createScalarProductCache(momStructure.getModeFunctions(), momStructure.getTestFunctions(), mon[1]);
    }
}
