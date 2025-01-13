package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadruwaves.builders.AbstractFarFieldBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

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
