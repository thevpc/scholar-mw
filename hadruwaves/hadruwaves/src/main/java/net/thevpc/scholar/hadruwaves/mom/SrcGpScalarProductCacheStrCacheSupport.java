package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;

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
        PlanarSources ps = (PlanarSources) ss;
        // Delta-gap path: when source is CstPlanarSource, delegate to RWGDeltaGapBMatrix.
        // It checks each basis function: RWG edges inside source domain use delta-gap excitation;
        // all others fall back transparently to the standard spatial scalar product.
        // This handles pure GpRWG, mixed ListTestFunctions, and pure sinusoid sets correctly.
        if (ps.getPlanarSources() != null && ps.getPlanarSources().length == 1
                && ps.getPlanarSources()[0] instanceof CstPlanarSource) {
            return RWGDeltaGapBMatrix.buildB(momStructure, (CstPlanarSource) ps.getPlanarSources()[0], getMonitor());
        }
        // Legacy path: full spatial integral for all basis functions
        DoubleToVector[] _g = ps.getSourceFunctions();
        return (ComplexMatrix) Maths.scalarProductCache(momStructure.testFunctions().toArray(), _g, getMonitor()).to(Maths.$COMPLEX);
    }
}
