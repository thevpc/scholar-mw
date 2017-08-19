package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.TMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SrcGpScalarProductCacheStrCacheSupport extends StrCacheSupport<TMatrix<Complex>> {

    private MomStructure momStructure;

    public SrcGpScalarProductCacheStrCacheSupport(MomStructure momStructure, ProgressMonitor monitor0) {
        super(momStructure, MomStructure.CACHE_SRCGP,monitor0);
        this.momStructure = momStructure;
    }

    @Override
    public TMatrix<Complex> compute(ObjectCache momCache) {
        Sources ss = momStructure.getSources();
        if (ss == null || !(ss instanceof PlanarSources)) {
            throw new IllegalArgumentException();
        }
        DoubleToVector[] _g = ((PlanarSources) ss).getSourceFunctions();
        return Maths.scalarProductCache(true, momStructure.getTestFunctions().arr(), _g, getMonitor());
    }
}
