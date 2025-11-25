package net.thevpc.scholar.hadruwaves.mom.str;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.str.PoyntingVectorEvaluator;

/**
 * Created by vpc on 5/30/14.
 */
public class DefaultPoyntingVectorEvaluator implements PoyntingVectorEvaluator {
    public static final DefaultPoyntingVectorEvaluator INSTANCE = new DefaultPoyntingVectorEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        MomStructure str=(MomStructure) structure;
        VDiscrete E = str.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        VDiscrete H = str.magneticField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        return E.crossprod(H).mul(Complex.of(0.5));
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName());
    }
}
