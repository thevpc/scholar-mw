package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class ZinMatrixStrCacheSupport extends StrCacheSupport<ComplexMatrix> {

    private MomStructure momStructure;
    private ProgressMonitor[] mon;

    public ZinMatrixStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_ZIN,monitor0);
        this.momStructure = momStructure;
    }

    protected void init(ProgressMonitor cacheMonitor) {
        mon = getMonitor().split(.2,.8);
        momStructure.getTestModeScalarProducts(mon[0]);
    }

    public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
        return momStructure.evaluator().createZinEvaluator().evaluate(momStructure, mon[1]);
    }
}
