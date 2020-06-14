package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractElectricFieldCartesianBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class DefaultElectricFieldCartesianBuilder extends AbstractElectricFieldCartesianBuilder {

    private final ElectricFieldPart part;

    public DefaultElectricFieldCartesianBuilder(MWStructure momStructure, ElectricFieldPart part) {
        super(momStructure);
        this.part = part;
    }

    public VDiscrete evalVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        final double[] z0 = z == null ? new double[]{0} : z;
        switch (part) {
            case FULL: {
                return new StrSubCacheSupport<VDiscrete>(getStructure(), "electric-field", CacheKey.obj("computeElectricField","x",x,"y",y,"z",z),monitor) {

                    @Override
                    public VDiscrete eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                        MomStructure momStructure = getStructure();
                        return momStructure.evaluator().createElectricFieldEvaluator().evaluate(getStructure(), x0, y0, z0, cacheMonitor);
                    }
                }.evalCached();
            }
            case FUNDAMENTAL: {
                return new StrSubCacheSupport<VDiscrete>(getStructure(), "electric-field-0",
                        CacheKey.obj("computeElectricFieldFundamental","x",x,"y",y,"z",z),monitor) {

                    public VDiscrete eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                        MomStructure momStructure = getStructure();
                        return momStructure.evaluator().createElectricFieldFundamentalEvaluator().evaluate(getStructure(), x0, y0, z0, cacheMonitor);
                    }
                }.evalCached();
            }
            case EVANESCENT: {
                MomStructure momStructure = getStructure();
                ProgressMonitor[] mon = ProgressMonitors.nonnull(monitor).split(2);
                VDiscrete Eall = momStructure.evaluator().createElectricFieldEvaluator().evaluate(getStructure(), x0, y0, z0, mon[0]);
                VDiscrete E0 = momStructure.evaluator().createElectricFieldFundamentalEvaluator().evaluate(getStructure(), x0, y0, z0, mon[1]);
                return Eall.sub(E0);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

//    @Override
//    public Matrix computeMatrix(Samples samples, Axis axis) {
//        ConvergenceEvaluator<MomStructure> conv = getConvergenceEvaluator();
//        if (conv == null) {
//            return computeXYMatrixImpl(samples, axis);
//        } else {
//            return storeConvergenceResult(conv.evaluate(momStructure, new ObjectEvaluator<MomStructure, Matrix>() {
//                @Override
//                public Matrix evaluate(MomStructure momStructure, ProgressMonitor monitor) {
//                    return computeXYMatrixImpl(samples, axis);
//                }
//            }, getMonitor()));
//        }
//    }


}
