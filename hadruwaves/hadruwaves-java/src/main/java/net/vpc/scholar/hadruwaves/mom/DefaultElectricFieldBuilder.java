package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractElectricFieldBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultElectricFieldBuilder extends AbstractElectricFieldBuilder {

    private final ElectricFieldPart part;

    public DefaultElectricFieldBuilder(MWStructure momStructure, ElectricFieldPart part) {
        super(momStructure);
        this.part = part;
    }

    @Override
    public MomStructure getStructure() {
        return (MomStructure) super.getStructure();
    }


    public VDiscrete computeVDiscreteImpl(double[] x, double[] y, double[] z,ProgressMonitor monitor) {
        final double[] x0 = x == null ? new double[]{0} : x;
        final double[] y0 = y == null ? new double[]{0} : y;
        final double[] z0 = z == null ? new double[]{0} : z;
        switch (part) {
            case FULL: {
                Dumper p = new Dumper("computeElectricField").add("x", x).add("y", y).add("z", z);
                return new StrSubCacheSupport<VDiscrete>(getStructure(), "electric-field", p.toString(),monitor) {

                    @Override
                    public VDiscrete compute(ObjectCache momCache) {
                        double progressValue = getMonitor().getProgressValue();
                        return getStructure().createElectricFieldEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
                    }
                }.computeCached();
            }
            case FUNDAMENTAL: {
                Dumper p = new Dumper("computeElectricFieldFundamental").add("x", x).add("y", y).add("z", z);
                return new StrSubCacheSupport<VDiscrete>(getStructure(), "electric-field-0", p.toString(),monitor) {

                    public VDiscrete compute(ObjectCache momCache) {
                        return getStructure().createElectricFieldFundamentalEvaluator().evaluate(getStructure(), x0, y0, z0, getMonitor());
                    }
                }.computeCached();
            }
            case EVANESCENT: {
                ProgressMonitor[] mon = ProgressMonitorFactory.nonnull(monitor).split(2);
                VDiscrete Eall = getStructure().electricField(ElectricFieldPart.FULL).monitor(mon[0]).computeVDiscrete(x, y, z);
                VDiscrete E0 = getStructure().electricField(ElectricFieldPart.FUNDAMENTAL).monitor(mon[1]).computeVDiscrete(x, y, z);
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
