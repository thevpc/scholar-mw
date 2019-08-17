package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.builders.AbstractSourceBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultSourceBuilder extends AbstractSourceBuilder {


    public DefaultSourceBuilder(MomStructure momStructure) {
        super(momStructure);
    }

    @Override
    public Matrix computeMatrix(final Axis axis, double[] x, double[] y, double z) {
//                return computeVDiscrete(x, y).getComponent(axis).getMatrix(Axis.Z, 0);

        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        MomStructure momStructure = getStructure();
        Dumper p = new Dumper("computePlanarSources").add("x", x).add("y", y).add("axis", axis);
        return new StrSubCacheSupport<Matrix>(momStructure, "sources-planar", p.toString(),getMonitor()) {

            @Override
            public Matrix compute(ObjectCache momCache) {
                return momStructure.createSourceEvaluator().computePlanarSources(momStructure,x0, y0, axis, getMonitor());
            }
        }.computeCached();
    }

}
