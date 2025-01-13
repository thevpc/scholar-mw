package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 */
public final class SicoCocoPattern extends AbstractGpPatternPQ {


    public SicoCocoPattern(int complexity) {
        super(complexity);
    }

    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str) {
        CosXCosY fx = FunctionFactory.sinXcosY(
                1,// / Math.sqrt(d.width * d.height),
                (p + 1) * Math.PI / d.xwidth(),
                -(p + 1) * Math.PI * d.xmin() / d.xwidth(),
                q * Math.PI / d.ywidth(),
                -q * Math.PI * d.ymin() / d.ywidth(),
                d
        );
        CosXCosY fy = FunctionFactory.cosXcosY(
                1,// / Math.sqrt(d.width * d.height),
                p * Math.PI / d.xwidth(),
                -p * Math.PI * d.xmin() / d.xwidth(),
                q * Math.PI / d.ywidth(),
                -q * Math.PI * d.ymin() / d.ywidth(),
                d
        );
        double ax = Maths.scalarProduct(fx, fx);
        double ay = Maths.scalarProduct(fy, fy);
        fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
        fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
        return Maths.vector(fx,fy).setTitle("sico(" + (p+1) + "x," + q + "y),coco(" + p + "x," + q + "y)")
        .setProperty("Type", "SicoCoco")
        .setProperty("p", p)
        .setProperty("q", q).toDV();
    }

}
