package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SrcGpScalarProductCacheStrCacheSupport extends StrCacheSupport<ScalarProductCache> {

    private MomStructure momStructure;

    public SrcGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ComputationMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_SRCGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public ScalarProductCache compute(ObjectCache momCache) {
        Sources ss = momStructure.getSources();
        if (ss == null || !(ss instanceof PlanarSources)) {
            throw new IllegalArgumentException();
        }
        DoubleToVector[] _g = ((PlanarSources) ss).getSourceFunctions();
        return Maths.scalarProductCache(momStructure.getTestFunctions().arr(), _g,true, getMonitor());
    }
}
