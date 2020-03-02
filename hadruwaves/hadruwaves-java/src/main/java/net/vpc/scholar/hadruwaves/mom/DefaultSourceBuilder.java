package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadruwaves.builders.AbstractSourceBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultSourceBuilder extends AbstractSourceBuilder {


    public DefaultSourceBuilder(MomStructure momStructure) {
        super(momStructure);
    }

    @Override
    public ComplexMatrix evalMatrix(final Axis axis, double[] x, double[] y, double z) {
//                return computeVDiscrete(x, y).getComponent(axis).getMatrix(Axis.Z, 0);

        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        MomStructure momStructure = getStructure();
        return new StrSubCacheSupport<ComplexMatrix>(momStructure, "sources-planar", CacheKey.obj("computePlanarSources","x",x,"axis",axis),getMonitor()) {

            @Override
            public ComplexMatrix eval(ObjectCache momCache) {
                return momStructure.evaluator().createSourceEvaluator().evalPlanarSources(momStructure,x0, y0, axis, getMonitor());
            }
        }.evalCached();
    }

}
