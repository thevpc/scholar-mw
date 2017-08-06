package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DDiscrete;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SinYMaxMinPatternAproxim extends RectMeshAttachGpPattern {

    private int max;

    public SinYMaxMinPatternAproxim(int complexity) {
        this.max = complexity;
    }

    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("max", max);
        return h;
    }

    public DoubleToVector createFunction(int q0, Domain globalDomain, MeshZone zone, MomStructure str) {
        int q = q0;
        int q_i = -1;
        Domain d = zone.getDomain();
        double qPIbyH = (2 * q + 1) * PI / d.ywidth() / 2;

        DoubleToVector f = Maths.vector(
                DDiscrete.discretize(
                        FunctionFactory.sinY(
                                1 / sqrt(d.xwidth() * d.ywidth()),
                                q_i * qPIbyH,
                                -q_i * qPIbyH * (q_i < 0 ? d.ymax() : d.ymin()),
                                d
                        ), d, 100, 100),
                DDiscrete.discretize(FunctionFactory.sinY(
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
