package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 *
 */
public final class SisiSisiPattern extends AbstractGpPatternPQ {

    public SisiSisiPattern(int complexity) {
        super(complexity);
    }

    public DoubleToVector createFunction(int index, int p0, int q0, Domain d, Domain globalDomain, MomStructure str) {
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
        double qPIbyH = q * PI / d.ywidth() / 2;

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
        double pPIbyH = p * PI / d.xwidth() / 2;

        DoubleToVector f = Maths.vector(
                FunctionFactory.sinXsinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        p_i * pPIbyH,
                        -p_i * pPIbyH * (p_i < 0 ? d.xmax() : d.xmin()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax (): d.ymin()),
                        d
                ),
                FunctionFactory.sinXsinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        p_i * pPIbyH,
                        -p_i * pPIbyH * (p_i < 0 ? d.xmax (): d.xmin()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax (): d.ymin()),
                        d
                )
        )
        .setName("sin" + p_i + "(" + p + "x)sin" + q_i + "(" + q + "y)")
                .setProperty("Type", "SinXY")
                .setProperty("q", q0)
                .setProperty("p", p0).toDV();
//        f.setProperties(properties);
        return f;
    }

}
