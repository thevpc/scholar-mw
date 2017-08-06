package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SicoCosiPattern extends AbstractGpPatternPQ {


    public SicoCosiPattern(int complexity) {
        super(complexity);
    }

    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str) {
        DoubleToVector f = Maths.vector(
                (
                        FunctionFactory.sinXcosY(
                                1 / Math.sqrt(d.xwidth() * d.ywidth()),
                                p * Math.PI / d.xwidth(),
                                -p * Math.PI * d.xmin() / d.xwidth(),
                                q * Math.PI / d.ywidth(),
                                -q * Math.PI * d.ymin() / d.ywidth(),
                                d
                        )),
                (FunctionFactory.cosXsinY(
                        1 / Math.sqrt(d.xwidth() * d.ywidth()),
                        p * Math.PI / d.xwidth(),
                        -p * Math.PI * d.xmin() / d.xwidth(),
                        q * Math.PI / d.ywidth(),
                        -q * Math.PI * d.ymin() / d.ywidth(),
                        d
                ))
        );
        return f.setTitle("sico(" + p + "x," + q + "y),cosi(" + p + "x," + q + "y)")
        .setProperty("Type", "SicoCosi")
        .setProperty("p", p)
        .setProperty("q", q).toDV();
    }

}
