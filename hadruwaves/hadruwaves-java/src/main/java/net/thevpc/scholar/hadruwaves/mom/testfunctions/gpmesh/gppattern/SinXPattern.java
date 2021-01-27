package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

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

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 *
 */
public final class SinXPattern extends RectMeshAttachGpPattern {
    private int max;


    public SinXPattern(int complexity) {
        this.max = complexity;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("max", context.elem(max));
        return h.build();
    }


    public DoubleToVector createFunction(int p0, Domain globalDomain, MeshZone zone, MomStructure str) {
        int p;
        int p_i;
        int p_d = p0 / 3;
        int p_r = p0 % 3;
        if (p_r == 0) {
            p = p_d * 2;
            p_i = 1;
        } else {
            p = p_d * 2 + 1;
            p_i = p_r == 1 ? 1 : -1;
        }
        Domain d = zone.getDomain();
        double pPIbyH = p * PI / d.xwidth() / 2;
        Expr f = Maths.vector(
                //a.sin(qPI(x-x0)/h)
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        p_i * pPIbyH,
                        -p_i * pPIbyH * (p_i < 0 ? d.xmax (): d.xmin()),
                        d
                ),
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        p_i * pPIbyH,
                        -p_i * pPIbyH * (p_i < 0 ? d.xmax (): d.xmin()),
                        d
                )
        );
        return f.setTitle("0,sinX" + p_i + "(" + p + "x)")
        .setProperty("Type", "SinX")
        .setProperty("p", p0).toDV();
    }

    public int getCount() {
        return max;
    }

}