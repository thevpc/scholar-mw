package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractTestFieldBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultTestFieldBuilder extends AbstractTestFieldBuilder {

    public DefaultTestFieldBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    @Override
    public VDiscrete evalVDiscrete(double[] x, double[] y) {
        return evalVDiscrete(x,y,new double[]{0});
    }

    public VDiscrete evalVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "current-test",  CacheKey.obj("computeTestField","x",x,"y",y),monitor) {

            @Override
            public VDiscrete eval(ObjectCache momCache) {
                MomStructure momStructure = getStructure();
                return momStructure.evaluator().createTestFieldEvaluator().evaluate(getStructure(), x0, y0, getMonitor());
            }
        }.evalCached();
    }

}
