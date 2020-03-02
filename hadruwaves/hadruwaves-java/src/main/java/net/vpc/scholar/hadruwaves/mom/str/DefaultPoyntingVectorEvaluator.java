package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.common.mon.ProgressMonitor;
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
        VDiscrete E = str.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        VDiscrete H = str.magneticField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        return E.crossprod(H).mul(Complex.of(0.5));
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }
}
