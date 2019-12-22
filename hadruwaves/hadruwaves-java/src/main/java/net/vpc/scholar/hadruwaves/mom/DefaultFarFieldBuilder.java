package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TMatrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.builders.AbstractFarFieldBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultFarFieldBuilder extends AbstractFarFieldBuilder {

    public DefaultFarFieldBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public TVector<TMatrix<Complex>> computeFarFieldThetaPhiImpl(double[] theta, double[] phi, final double r, ProgressMonitor monitor) {
        final double[] theta0 = theta == null ? new double[]{0} : theta;
        final double[] phi0 = phi == null ? new double[]{0} : phi;
        Dumper p = new Dumper("computeFarFieldThetaPhi").add("theta", theta0).add("phi", phi0).add("r", r);
        return new StrSubCacheSupport<TVector<TMatrix<Complex>>>(getStructure(), "far-field", p.toString(),monitor) {

            @Override
            public TVector<TMatrix<Complex>> compute(ObjectCache momCache) {
                double progressValue = getMonitor().getProgressValue();
                MomStructure momStructure = getStructure();
                return momStructure.createFarFieldEvaluator().evaluate(getStructure(), theta0, phi0, r, getMonitor());
            }
        }.computeCached();
    }
}
