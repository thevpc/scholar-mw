package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadruwaves.builders.AbstractMagneticFieldCartesianBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMagneticFieldCartesianBuilder extends AbstractMagneticFieldCartesianBuilder {

    public DefaultMagneticFieldCartesianBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public VDiscrete evalVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        final double[] z0 = z == null ? new double[]{0} : z;
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "magnetic-field", CacheKey.obj("computeMagneticField","x",x,"y",y,"z",z), monitor) {

            public VDiscrete eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                MomStructure momStructure = getStructure();
                return momStructure.evaluator().createMagneticFieldEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
            }
        }.evalCached();
    }



}
