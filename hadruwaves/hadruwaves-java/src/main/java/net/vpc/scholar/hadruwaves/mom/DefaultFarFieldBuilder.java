package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadruwaves.builders.AbstractFarFieldBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultFarFieldBuilder extends AbstractFarFieldBuilder {

    public DefaultFarFieldBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public Vector<ComplexMatrix> evalFarFieldThetaPhiImpl(double[] theta, double[] phi, final double r, ProgressMonitor monitor) {
        final double[] theta0 = theta == null ? new double[]{0} : theta;
        final double[] phi0 = phi == null ? new double[]{0} : phi;
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "far-field",
                CacheKey.obj("computeFarFieldThetaPhi","theta",theta0,"phi",phi0,"r",r)
                ,monitor) {

            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                double progressValue = getMonitor().getProgress();
                MomStructure momStructure = getStructure();
                return momStructure.evaluator().createFarFieldEvaluator().evaluate(getStructure(), theta0, phi0, r, getMonitor());
            }
        }.evalCached();
    }
}
