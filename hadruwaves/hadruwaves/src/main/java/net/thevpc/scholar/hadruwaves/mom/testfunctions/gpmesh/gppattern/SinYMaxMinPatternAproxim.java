package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SinYMaxMinPatternAproxim extends RectMeshAttachGpPattern {

    private int max;

    public SinYMaxMinPatternAproxim(int complexity) {
        this.max = complexity;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("max", context.elem(max));
        return h.build();
    }

    public DoubleToVector createFunction(int q0, Domain globalDomain, MeshZone zone, MomStructure str) {
        int q = q0;
        int q_i = -1;
        Domain d = zone.getDomain();
        double qPIbyH = (2 * q + 1) * PI / d.ywidth() / 2;

        DoubleToVector f = Maths.vector(
                DDiscrete.of(
                        FunctionFactory.sinY(
                                1 / sqrt(d.xwidth() * d.ywidth()),
                                q_i * qPIbyH,
                                -q_i * qPIbyH * (q_i < 0 ? d.ymax() : d.ymin()),
                                d
                        ), d, 100, 100),
                DDiscrete.of(FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax() : d.ymin()),
                        d
                ), d, 100, 100)
        )
                .setTitle("0,sinY" + q_i + "(" + q + "y)")
                .setProperty("Type", "SinY")
                .setProperty("q", q0).toDV();
//        f.setProperties(properties);
        return f;
    }

    public int getCount() {
        return max;
    }

}
