package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SinYMaxMinPattern extends RectMeshAttachGpPattern {

    private int max;

    public SinYMaxMinPattern(int complexity) {
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

        Expr f = Maths.vector(
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax() : d.ymin()),
                        d
                ),
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax() : d.ymin()),
                        d
                )
        );
        return f.setTitle("0,sinY" + q_i + "(" + q + "y)")
                .setProperty("Type", "SinY")
                .setProperty("q", q0).toDV();
    }

    public int getCount() {
        return max;
    }

}
