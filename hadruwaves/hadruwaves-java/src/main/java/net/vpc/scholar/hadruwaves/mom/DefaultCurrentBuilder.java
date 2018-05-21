package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractCurrentBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultCurrentBuilder extends AbstractCurrentBuilder {

    public DefaultCurrentBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    @Override
    public MomStructure getStructure() {
        return (MomStructure) super.getStructure();
    }

    @Override
    public VDiscrete computeVDiscrete(double[] x, double[] y) {
        return computeVDiscrete(x,y,new double[]{0});
    }

    protected VDiscrete computeVDiscreteImpl(double[] x, double[] y, double[] z,ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        Dumper p = new Dumper("computeCurrent").add("x", x).add("y", y);
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "current", p.toString(),monitor) {

            @Override
            public VDiscrete compute(ObjectCache momCache) {
                return getStructure().createCurrentEvaluator().evaluate(getStructure(), x0, y0, getMonitor());
            }
        }.computeCached();
    }
}
