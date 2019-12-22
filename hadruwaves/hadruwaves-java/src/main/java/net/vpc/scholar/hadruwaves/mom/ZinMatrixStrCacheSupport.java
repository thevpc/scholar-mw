package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.common.mon.ProgressMonitor;

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

    protected void init() {
        mon = getMonitor().split(new double[]{2,8});
        momStructure.getTestModeScalarProducts(mon[0]);
    }

    public ComplexMatrix compute(ObjectCache momCache) {
        return momStructure.createZinEvaluator().evaluate(momStructure, mon[1]);
    }
}
