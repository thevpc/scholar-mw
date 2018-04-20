package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.CExp;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.symbolic.PiecewiseSineXFunctionXY;
import net.vpc.scholar.hadrumaths.symbolic.RooftopXFunctionXY;
import net.vpc.scholar.hadrumaths.symbolic.UFunction;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.vpc.scholar.hadrumaths.Maths.CZERO;
import static net.vpc.scholar.hadrumaths.Maths.HALF_PI;

/**
 * Class Description
 *
 * @author Taha BEN SALAH (taha@ant-inter.com) User: taha Date: 25-aout-2004
 *         Time: 20:04:38
 */
public final class FunctionFactory extends AbstractFactory{


    //    public static void main(String[] args) {
//        DomainXY d = new DomainXY(0, 0, 1, 1);
//        Plot.builder().title("e").domain(d).plot(
//                Maths.vector(
//                        archeSinus(Axis.Y, 1, d),
//                        archeSinus(Axis.Y, 0.8, d)
//                ), Maths.vector(
//                        archeSinus(Axis.Y, 1, d).getSymmetricY(),
//                        archeSinus(Axis.Y, 0.8, d).getSymmetricY()
//                )
//                //                ,new CFunctionVector2D(
//                //                    rooftop(Axis.Y, 1,false,d),
//                //                    rooftop(Axis.Y, 1,true,d)
//                //                )
//        );
//    }
    public static final DoubleToDouble DZEROX = DoubleValue.valueOf(0, Domain.ZEROX).setTitle("0").toDD();
    public static final DoubleToDouble DZEROXY = DoubleValue.valueOf(0, Domain.ZEROXY).setTitle("0").toDD();
    public static final DoubleToDouble DZEROXYZ = DoubleValue.valueOf(0, Domain.ZEROXYZ).setTitle("0").toDD();


    public static final DoubleToComplex CZEROX = new ComplexValue(CZERO, Domain.ZEROX).setTitle("0").toDC();
    public static final DoubleToComplex CZEROXY = new ComplexValue(CZERO, Domain.ZEROXY).setTitle("0").toDC();
    public static final DoubleToComplex CZEROXYZ = new ComplexValue(CZERO, Domain.ZEROXYZ).setTitle("0").toDC();

//    //    public static final DFunctionVector2D DZERO_XY2D = new DFunctionVector2D(DZEROXY, DZEROXY);
//    public static final DoubleToVector VZEROXY2 = (DoubleToVector) Maths.vector(CZEROXY, CZEROXY).setName("0");
//    public static final DoubleToVector V3ZERO = (DoubleToVector) Maths.vector(CZEROXY, CZEROXY, CZEROXY).setName("0");


    public static DoubleToDouble DZERO(int dim) {
        switch (dim) {
            case 1: {
                return DZEROX;
            }
            case 2: {
                return DZEROXY;
            }
            case 3: {
                return DZEROXYZ;
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + dim);
    }

    public static DoubleToComplex CZERO(int dim) {
        switch (dim) {
            case 1: {
                return CZEROX;
            }
            case 2: {
                return CZEROXY;
            }
            case 3: {
                return CZEROXYZ;
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + dim);
    }

    //TODO a revoir ?

    /**
     * ## <-- # # # # # # # # # # a b ^ ^ | | <p/>
     * a=domain.xmin b=domain.xmax
     */
    public static CosXCosY cosXcentredCrest(double k, Domain domain) {
        double w = domain.xmax() - domain.xmin();
        double amp = k == 0 ? (1 / w) : 1 / Math.sqrt(k * PI * w);
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
        double amp = 1;//ampCoeff*k==0?sqrt(1/w):(1 / Math.sqrt(PI * k * w));
        return new CosXCosY(amp, PI * k / w, -PI * k * domain.xmin() / w, 0, 0, domain);
    }

    //TODO
    public static DoubleToDouble cosXcentredWellInv(double ampCoeff, double k, Domain domain) {
        double w = domain.xmax() - domain.xmin();
        double amp = 1;//ampCoeff*k==0?sqrt(1/w):(1 / Math.sqrt(PI * k * w));
        return new CosXCosY(amp, -PI * k / w, +PI * k * domain.xmax() / w, 0, 0, domain);
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
                ? Maths.expr(sqrt(1 / w), d)
                : cosX(sqrt(2 / w),
                2 * n * PI / w,
                0,
                d);
    }

    public static CosXCosY cosXcosY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXcosY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Math.PI / 2, c, d, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXcosY0(double a, double b, double c, double d, Domain domain) {
        CosXCosY s = new CosXCosY(1, a, b - Math.PI / 2, c, d, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
        return new CosXCosY(1.0 / Math.sqrt(Maths.scalarProduct(s, s)), a, b - Math.PI / 2, c, d, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY sinXsinY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Math.PI / 2, c, d - Math.PI / 2, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXsinY0(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d - Math.PI / 2, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXsinY0(double a, double b, double c, double d, Domain domain) {
        CosXCosY s = new CosXCosY(1, a, b, c, d - Math.PI / 2, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
        return new CosXCosY(1.0 / Math.sqrt(Maths.scalarProduct(s, s)), a, b, c, d - Math.PI / 2, Domain.forBounds(0, domain.xwidth(), 0, domain.ywidth())).translate(domain.xmin(), domain.ymin());
    }

    public static CosXCosY cosXcosY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d, domain);
    }

    public static CosXCosY cosXsinY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b, c, d - Math.PI / 2, domain);
    }

    public static CosXCosY sinXcosY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Math.PI / 2, c, d, domain);
    }

    public static CosXCosY sinXsinY(double amp, double a, double b, double c, double d, Domain domain) {
        return new CosXCosY(amp, a, b - Math.PI / 2, c, d - Math.PI / 2, domain);
    }

    public static CosXCosY cosX(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, a, b, 0, 0, domain);
    }


    public static CosXCosY sinX(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, a, b - Math.PI / 2, 0, 0, domain);
    }

    public static CosXCosY cosY(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, 0, 0, a, b, domain);
    }

    public static CosXCosY sinY(double amp, double a, double b, Domain domain) {
        return new CosXCosY(amp, 0, 0, a, b - Math.PI / 2, domain);
    }

    public static Linear segment(double a, double b, double c, Domain domain) {
        return new Linear(a, b, c, domain);
    }


    /**
     * @param p the p
     * @param d the d
     * @return cos(2(p-1)x/w) / sqrt(w2/4 - x2)
     */
//    public static DDxUx uX(int p, Domain d) {
//        Domain d0 = Domain.forBounds(-d.xwidth / 2, d.xwidth / 2);
//        return (DDxUx) (new DDxUx(d0,
//                1, 2 * (p - 1) / d.xwidth, 0, -1, 0, d.xwidth * d.xwidth / 4
//        ).transformLinear(1, -(d0.xmin - d.xmin)));
//    }
    public static DoubleToDouble uXY(int p, Domain d) {
        Domain d0 =
                Domain.forBounds(-d.xwidth() / 2
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
        return new RooftopXFunctionXY(domain, axis, 1 / sqrt(d), count, crestOnEdge);
//        // w=domain.width;
//        //seg=sx+t avec s=pente=v/(w/(2*count))
//        //|seg|=integrale((sx+t)*(sx+t))=integrale((s2x2+2stx+t2))
//        double amp2seg=1.0/(2*segWidth
//        return new RooftopXFunctionXY(domain,axis,
//                count * 2 / Math.sqrt(((domain.width / count) * domain.height)), count, crestOnEdge);
    }

    public static PiecewiseSineXFunctionXY archeSinus(Axis axis, double factor, Domain domain) {
        return new PiecewiseSineXFunctionXY(domain, axis, 1, factor, 1, false);
    }

    public static CExp expIXexpIY(double amp, double a, double b, Domain domain) {
        return new CExp(amp, a, b, domain);
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
        return ExpressionRewriterFactory.getComputationOptimizer().rewriteOrSame(f).toDD();
    }

//    public static IDoubleToComplex toCFunctionXY(IDoubleToDouble f) {
//        return Maths.complex(f, DZEROXY);
//    }

//    public static IDoubleToVector toCFunctionVector2D(IDoubleToComplex f) {
//        return Maths.vector(f, null);
//    }
//
//    public static IDoubleToVector toCFunctionVector2D(IDoubleToDouble f) {
//        return Maths.vector(f, DZEROXY);
//    }

//    public static IDoubleToVector[] toArrayCFunctionVector2D(IDoubleToComplex... arr) {
//        IDoubleToVector[] all = new IDoubleToVector[arr.length];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = Maths.vector(arr[i], null);
//        }
//        return all;
//    }

//    public static IDoubleToComplex[] toArrayCFunctionXY(IDoubleToDouble... arr) {
//        IDoubleToComplex[] all = new IDoubleToComplex[arr.length];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = new DCxy(arr[i], DZEROXY);
//        }
//        return all;
//    }
//
//    public static IDoubleToVector[] toArrayCFunctionVector2D(IDoubleToDouble... arr) {
//        IDoubleToVector[] all = new IDoubleToVector[arr.length];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = Maths.vector(arr[i], DZEROXY);
//        }
//        return all;
//    }

    public static PiecewiseSineXFunctionXY sinPiece(Axis axis, Domain domain) {
        return new PiecewiseSineXFunctionXY(domain, axis, 1, 1, false);
    }

}
