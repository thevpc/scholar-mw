package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.PoyntingVectorEvaluator;

/**
 * Created by vpc on 5/30/14.
 */
public class DefaultPoyntingVectorEvaluator implements PoyntingVectorEvaluator {
    public static final DefaultPoyntingVectorEvaluator INSTANCE = new DefaultPoyntingVectorEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        MomStructure str=(MomStructure) structure;
        VDiscrete E = str.electricField().monitor(monitor).computeVDiscrete(x, y, z);
        VDiscrete H = str.magneticField().monitor(monitor).computeVDiscrete(x, y, z);
        return E.crossprod(H).mul(Complex.valueOf(0.5));
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public String dump() {
        return getClass().getName();
    }
}
