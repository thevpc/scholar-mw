package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CExp;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;

import static net.thevpc.scholar.hadrumaths.Maths.CZERO;
import static net.thevpc.scholar.hadrumaths.Maths.HALF_PI;

/**
 * Class Description
 *
 * @author Taha BEN SALAH (taha@ant-inter.com) User: taha Date: 25-aout-2004
 * Time: 20:04:38
 */
public final class FunctionFactory extends AbstractFactory {

    /**
     * ## <-- # # # # # # # # # # a b ^ ^ | | <p/>
     * a=domain.xmin b=domain.xmax
     */
    public static CosXCosY cosXcentredCrest(double k, Domain domain) {
        double w = domain.xmax() - domain.xmin();
        double amp = k == 0 ? (1 / w) : 1 / Maths.sqrt(k * Maths.PI * w);
        return new CosXCosY(amp, -k, k * (w + domain.xmin()) - (HALF_PI), 0, 0, domain);
    }

    //TODO a revoir ?

    /**
     * <pre>
     * #  <--       #         #  <--
     * #          #           #
     * #        #             #
     * #        #             #
     * #      #               #
     * #      #               #
     * #      #      or       #
     * #      #               #
     * #    #                 #
     * #    #                 #
     * #  #                   #
     * ##                     #
     * a     b impair b(k module 2)
     * ^     ^        ^
     * |     |        |
     *
     * a=domain.xmin
     * b=domain.xmax
     * </pre>
     */
    public static DoubleToDouble cosXcentredWell(double ampCoeff, double k, Domain domain) {
        double w = domain.xmax() - domain.xmin();
        double amp = 1;//ampCoeff*k==0?sqrt(1/w):(1 / Maths.sqrt(PI * k * w));
        return new CosXCosY(amp, Maths.PI * k / w, -Maths.PI * k * domain.xmin() / w, 0, 0, domain);
    }

    //TODO
    public static DoubleToDouble cosXcentredWellInv(double ampCoeff, double k, Domain domain) {
        double w = domain.xmax() - domain.xmin();
        double amp = 1;//ampCoeff*k==0?sqrt(1/w):(1 / Maths.sqrt(PI * k * w));
        return new CosXCosY(amp, -Maths.PI * k / w, +Maths.PI * k * domain.xmax() / w, 0, 0, domain);
    }

    //TODO a revoir ?

    /**
     * # <-- # # # # # # # # # # # # # # # # # # # # # ## a b impair b(k module
     * 2) ^ ^ ^ | | | <p/>
     * a=domain.xmin b=domain.xmax
     */
    public static DoubleToDouble cosXcentredWell2(double n, Domain d) {
        double w = d.xmax() - d.xmin();

        return n == 0
                ? Maths.expr(Maths.sqrt(1 / w), d)
                : cosX(Maths.sqrt(2 / w),
                2 * n * Maths.PI / w,
                0,
                d);
    }

    public static CosXCosY cosX(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, a, b, 0, 0, domain);
    }

    public static CosXCosY cosXcosY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXcosY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Maths.PI / 2, c, d, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXcosY0(double a, double b, double c, double d, Domain domain) {
        CosXCosY s = new CosXCosY(1, a, b - Maths.PI / 2, c, d, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
        return new CosXCosY(1.0 / Maths.sqrt(Maths.scalarProduct(s, s)), a, b - Maths.PI / 2, c, d, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXsinY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Maths.PI / 2, c, d - Maths.PI / 2, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXsinY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d - Maths.PI / 2, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXsinY0(double a, double b, double c, double d, Domain domain) {
        CosXCosY s = new CosXCosY(1, a, b, c, d - Maths.PI / 2, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
        return new CosXCosY(1.0 / Maths.sqrt(Maths.scalarProduct(s, s)), a, b, c, d - Maths.PI / 2, Domain.ofBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXcosY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d, domain);
    }

    public static CosXCosY cosXsinY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d - Maths.PI / 2, domain);
    }

    public static CosXPlusY cosXPlusY(double amp, double a, double b, double c, Domain domain) {
        return new CosXPlusY(amp, a, b, c, domain);
    }

    public static CosXCosY sinXcosY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Maths.PI / 2, c, d, domain);
    }

    public static CosXCosY sinXsinY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Maths.PI / 2, c, d - Maths.PI / 2, domain);
    }

    public static CosXCosY sinX(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, a, b - Maths.PI / 2, 0, 0, domain);
    }

    public static CosXCosY cosY(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, 0, 0, a, b, domain);
    }

    public static CosXCosY sinY(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, 0, 0, a, b - Maths.PI / 2, domain);
    }

    public static Linear segment(double a, double b, double c, Domain domain) {
        return new Linear(a, b, c, domain);
    }


    /**
     * @param p the p
     * @param d the d
     * @return cos(2 ( p - 1)x/w) / sqrt(w2/4 - x2)
     */
//    public static DDxUx uX(int p, Domain d) {
//        Domain d0 = Domain.forBounds(-d.xwidth / 2, d.xwidth / 2);
//        return (DDxUx) (new DDxUx(d0,
//                1, 2 * (p - 1) / d.xwidth, 0, -1, 0, d.xwidth * d.xwidth / 4
//        ).transformLinear(1, -(d0.xmin - d.xmin)));
//    }
    public static DoubleToDouble uXY(int p, Domain d) {
        Domain d0 =
                Domain.ofBounds(-d.xwidth() / 2
                        , d.xwidth() / 2, Double.NEGATIVE_INFINITY
                        , Double.POSITIVE_INFINITY);
        return (new UFunction(d0,
                1, 2 * (p - 1) / d.xwidth(), 0, -1, 0, d.xwidth() * d.xwidth() / 4
        ).transformLinear(1, -(d0.xmin() - d.xmin())));
    }

    public static RooftopXFunctionXY rooftop(Axis axis, double crestValue, int nbrPeriods, boolean startWithCrest, Domain domain) {
        return new RooftopXFunctionXY(domain, axis, crestValue, nbrPeriods, startWithCrest);
    }

    public static RooftopXFunctionXY rooftop(Axis axis, double crestValue, Domain domain) {
        return new RooftopXFunctionXY(domain, axis, crestValue, 1, false);
    }

    //    public static PiecewiseSineXFunctionXY sinPiece(Axis axis,DomainXY domain) {
//        return new PiecewiseSineXFunctionXY(domain, axis,1, 1, false);
//    }
    //TODO norm is incorrect ??? pleazzzzzz coorect it
    public static RooftopXFunctionXY rooftop(Axis axis, int count, boolean crestOnEdge, Domain domain) {
        RooftopXFunctionXY r0 = new RooftopXFunctionXY(domain, axis, 1, count, crestOnEdge);
        double d = Maths.scalarProduct(r0, r0);
        return new RooftopXFunctionXY(domain, axis, 1 / Maths.sqrt(d), count, crestOnEdge);
//        // w=domain.width;
//        //seg=sx+t avec s=pente=v/(w/(2*count))
//        //|seg|=integrale((sx+t)*(sx+t))=integrale((s2x2+2stx+t2))
//        double amp2seg=1.0/(2*segWidth
//        return new RooftopXFunctionXY(domain,axis,
//                count * 2 / Maths.sqrt(((domain.width / count) * domain.height)), count, crestOnEdge);
    }

    public static PiecewiseSineXFunctionXY archeSinus(Axis axis, double factor, Domain domain) {
        return new PiecewiseSineXFunctionXY(domain, axis, 1, factor, 1, false);
    }

    public static CExp expIXexpIY(double amp, double a, double b, Domain domain) {
        return new CExp(Complex.of(amp), a, b, domain);
    }

//    public static IDDxy[] getXFunctions(DFunctionVector2D[] f) {
//        IDDxy[] g = new IDDxy[f.length];
//        for (int i = 0; i < g.length; i++) {
//            g[i] = f[i].fx;
//        }
//        return g;
//    }
//
//    public static IDDxy[] getYFunctions(DFunctionVector2D[] f) {
//        IDDxy[] g = new IDDxy[f.length];
//        for (int i = 0; i < g.length; i++) {
//            g[i] = f[i].fy;
//
//        }
//        return g;
//    }

    public static DoubleToComplex[] getXFunctions(DoubleToVector[] f) {
        DoubleToComplex[] g = new DoubleToComplex[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = f[i].getComponent(Axis.X).toDC();
        }
        return g;
    }

    public static DoubleToComplex[] getYFunctions(DoubleToVector[] f) {
        DoubleToComplex[] g = new DoubleToComplex[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = f[i].getComponent(Axis.Y).toDC();

        }
        return g;
    }

    public static DoubleToDouble[] getRealFunctions(DoubleToComplex[] f) {
        DoubleToDouble[] g = new DoubleToDouble[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = f[i].getRealDD();
        }
        return g;
    }

    public static DoubleToDouble[] getImagFunctions(DoubleToComplex[] f) {
        DoubleToDouble[] g = new DoubleToDouble[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = f[i].getRealDD();
        }
        return g;
    }

    @Deprecated
    public static DoubleToDouble simplify(DoubleToDouble f) {
        return ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(f, ExprType.DOUBLE_DOUBLE).toDD();
    }

    public static PiecewiseSineXFunctionXY sinPiece(Axis axis, Domain domain) {
        return new PiecewiseSineXFunctionXY(domain, axis, 1, 1, false);
    }

}
