package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.str.MagneticFieldEvaluator;

import static net.thevpc.scholar.hadrumaths.Complex.I;
import static net.thevpc.scholar.hadruwaves.Physics.omega;

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
        return Tson.ofFunction(getClass().getSimpleName()).build();
    }
}
