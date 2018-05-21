package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class ZinMatrixStrCacheSupport extends StrCacheSupport<Matrix> {

    private MomStructure momStructure;
    private ProgressMonitor[] mon;

    public ZinMatrixStrCacheSupport(MomStructure momStructure, EnhancedProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_ZIN,monitor0);
        this.momStructure = momStructure;
    }

    protected void init() {
        mon = getMonitor().split(new double[]{2,8});
        momStructure.getTestModeScalarProducts(mon[0]);
    }

    public Matrix compute(ObjectCache momCache) {
        return momStructure.createZinEvaluator().evaluate(momStructure, mon[1]);
    }
}
