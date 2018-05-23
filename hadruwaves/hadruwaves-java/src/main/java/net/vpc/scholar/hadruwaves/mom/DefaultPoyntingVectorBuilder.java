package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractPoyntingVectorBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultPoyntingVectorBuilder extends AbstractPoyntingVectorBuilder {


    public DefaultPoyntingVectorBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    @Override
    public MomStructure getStructure() {
        return (MomStructure) super.getStructure();
    }

    public VDiscrete computeVDiscreteImpl(double[] x, double[] y, double[] z,ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        final double[] z0 = z == null ? new double[]{0} : z;
        Dumper p = new Dumper("computePoyntingVector").add("x", x).add("y", y).add("z", z);
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "poynting-vector", p.toString(),monitor) {

            @Override
            public VDiscrete compute(ObjectCache momCache) {
                return getStructure().createPoyntingVectorEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
            }
        }.computeCached();
    }


}
