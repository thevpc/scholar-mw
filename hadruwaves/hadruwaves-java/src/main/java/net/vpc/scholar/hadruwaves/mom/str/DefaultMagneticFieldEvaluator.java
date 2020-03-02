package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
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
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        MomStructure str=(MomStructure)structure;
        VDiscrete E = str.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        return E.rot().mul(I.div(omega(str.getFrequency())));
    }

    @Override
    public String toString() {
        return getClass().getName();
    }


    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }
}
