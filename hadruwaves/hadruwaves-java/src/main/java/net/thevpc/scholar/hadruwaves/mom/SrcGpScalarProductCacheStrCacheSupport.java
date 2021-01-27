package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SrcGpScalarProductCacheStrCacheSupport extends StrCacheSupport<ComplexMatrix> {

    private MomStructure momStructure;

    public SrcGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_SRCGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
        Sources ss = momStructure.getSources();
        if (ss == null || !(ss instanceof PlanarSources)) {
            throw new IllegalArgumentException();
        }
        DoubleToVector[] _g = ((PlanarSources) ss).getSourceFunctions();
        return (ComplexMatrix) Maths.scalarProductCache(momStructure.testFunctions().arr(), _g, getMonitor()).to(Maths.$COMPLEX);
    }
}
