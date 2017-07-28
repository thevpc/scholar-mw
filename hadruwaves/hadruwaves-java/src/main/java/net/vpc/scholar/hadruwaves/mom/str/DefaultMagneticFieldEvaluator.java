package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.str.MagneticFieldEvaluator;

import static net.vpc.scholar.hadrumaths.Complex.I;
import static net.vpc.scholar.hadruwaves.Physics.omega;

/**
 * Created by vpc on 5/30/14.
 */
public class DefaultMagneticFieldEvaluator implements MagneticFieldEvaluator {
    public static final DefaultMagneticFieldEvaluator INSTANCE=new DefaultMagneticFieldEvaluator();
    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ComputationMonitor monitor) {
        MomStructure str=(MomStructure)structure;
        VDiscrete E = str.electricField().monitor(monitor).computeVDiscrete(x, y, z);
        return E.rot().mul(I.div(omega(str.getFrequency())));
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
