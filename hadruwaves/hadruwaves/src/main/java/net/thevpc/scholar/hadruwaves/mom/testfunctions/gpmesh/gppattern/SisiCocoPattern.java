package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:42:36
 */
public final class SisiCocoPattern extends AbstractGpPatternPQ {

    public SisiCocoPattern(int complexity) {
        super(complexity);
    }

    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str) {
        DoubleToVector f = Maths.vector(
                (
                        FunctionFactory.sinXsinY(
                                1 / Math.sqrt(d.xwidth() * d.ywidth()),
                                p * Math.PI / d.xwidth(),
                                -p * Math.PI * d.xmin() / d.xwidth(),
                                q * Math.PI / d.ywidth(),
                                -q * Math.PI * d.ymin() / d.ywidth(),
                                d
                        )),
                (FunctionFactory.cosXcosY(
                        1 / Math.sqrt(d.xwidth() * d.ywidth()),
                        p * Math.PI / d.xwidth(),
                        -p * Math.PI * d.xmin() / d.xwidth(),
                        q * Math.PI / d.ywidth(),
                        -q * Math.PI * d.ymin() / d.ywidth(),
                        d
                ))
        )
                .setTitle("sisi(" + p + "x," + q + "y),coco(" + p + "x," + q + "y)")
                .setProperty("Type", "SisiCoco")
//            properties.put("i", index);
                .setProperty("p", p)
                .setProperty("q", q).toDV();
//        f.setProperties(properties);
        return f;
    }
}
