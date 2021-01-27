package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 *
 */
public final class UserSinePattern extends AbstractGpPatternPQ {

    private CellBoundaries xboundaries = CellBoundaries.UDxUUy;
    private CellBoundaries yboundaries = CellBoundaries.UDxUUy;


    public UserSinePattern(int complexity, CellBoundaries xboundaries, CellBoundaries yboundaries) {
        super(complexity);
        this.xboundaries = xboundaries;
        this.yboundaries = yboundaries;
    }

//    public static void main(String[] args) {
//        DomainXY dom = new DomainXY(0, 0, 1, 1);
//        VDCxy v = createFunction(CellBoundaries.UUxUDy, CellBoundaries.UUxUDy, 0, 0, 1, dom, dom);
//        VDCxy s = v.getSymmetricY();
//        Plot.plotFunctionsDialog("",dom,v,s);
//
//    }
    public static DoubleToVector createFunction(CellBoundaries xboundaries, CellBoundaries yboundaries, int index, int p, int q, Domain d, Domain globalDomain) {
        CosXCosY fx = createFunction(xboundaries, p, q, d);
        CosXCosY fy = createFunction(yboundaries, p, q, d);


        double ax = Maths.scalarProduct(fx, fx);
        double ay = Maths.scalarProduct(fy, fy);
        fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
        fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
        DoubleToVector f = Maths.vector(
                fx,
                fy
        )
        .setTitle("Sine_" + xboundaries + "_" + yboundaries + "(" + p + "," + q + "))")
                .setProperty("Type", "Sine_" + xboundaries + "_" + yboundaries)
                .setProperty("p", p)
                .setProperty("q", q).toDV();
//        f.setProperties(properties);
        return f;
    }

    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str) {
        return createFunction(xboundaries, yboundaries, index, p, q, d, globalDomain);
    }

    public static CosXCosY createFunction(CellBoundaries boundaries, int p, int q, Domain d) {
        switch (boundaries) {
            //--UUy
            case UDxUUy: {
                return FunctionFactory.cosXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxUUy: {
                return FunctionFactory.sinXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxUUy: {
                return FunctionFactory.sinXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxUUy: {
                return FunctionFactory.cosXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--DDy
            case UDxDDy: {
                return FunctionFactory.cosXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxDDy: {
                return FunctionFactory.sinXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxDDy: {
                return FunctionFactory.sinXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxDDy: {
                return FunctionFactory.cosXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--UDy
            case UDxUDy: {
                return FunctionFactory.cosXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxUDy: {
                return FunctionFactory.sinXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxUDy: {
                return FunctionFactory.sinXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxUDy: {
                return FunctionFactory.cosXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--DUy
            case UDxDUy: {
                return FunctionFactory.cosXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxDUy: {
                return FunctionFactory.sinXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxDUy: {
                return FunctionFactory.sinXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxDUy: {
                return FunctionFactory.cosXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
        }
        return null;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("xboundaries", context.elem(xboundaries));
        h.add("yboundaries", context.elem(yboundaries));
        return h.build();
    }

}