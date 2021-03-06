package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.builders.AbstractPoyntingVectorCartesianBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class DefaultPoyntingVectorCartesianBuilder extends AbstractPoyntingVectorCartesianBuilder {


    public DefaultPoyntingVectorCartesianBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public VDiscrete evalVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        final double[] z0 = z == null ? new double[]{0} : z;
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "poynting-vector", CacheKey.obj("computePoyntingVector","x",x,"y",y),monitor) {

            @Override
            public VDiscrete eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                MomStructure momStructure = getStructure();
                return momStructure.evaluator().createPoyntingVectorEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
            }
        }.evalCached();
    }


}
