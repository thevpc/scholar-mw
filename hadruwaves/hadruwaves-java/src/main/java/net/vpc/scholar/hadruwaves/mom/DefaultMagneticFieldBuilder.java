package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractMagneticFieldBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMagneticFieldBuilder extends AbstractMagneticFieldBuilder {

    public DefaultMagneticFieldBuilder(MWStructure momStructure) {
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
        Dumper p = new Dumper("computeMagneticField").add("x", x).add("y", y).add("z", z);
        return new StrSubCacheSupport<VDiscrete>(getStructure(), "magnetic-field", p.toString(),monitor) {

            public VDiscrete compute(ObjectCache momCache) {
                return getStructure().createMagneticFieldEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
            }
        }.computeCached();
    }



}
