package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SinYPattern extends RectMeshAttachGpPattern {

    private int max;

    public SinYPattern(int complexity) {
        this.max = complexity;
    }

    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("max", max);
        return h;
    }

    public DoubleToVector createFunction(int q0, Domain globalDomain, MeshZone zone, MomStructure str) {
        int q;
        int q_i;
        int q_d = q0 / 3;
        int q_r = q0 % 3;
        if (q_r == 0) {
            q = q_d * 2;
            q_i = 1;
        } else {
            q = q_d * 2 + 1;
            q_i = q_r == 1 ? 1 : -1;
        }
        Domain d = zone.getDomain();
        double qPIbyH = q * PI / d.ywidth() / 2;
        DoubleToVector f = Maths.vector(
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
