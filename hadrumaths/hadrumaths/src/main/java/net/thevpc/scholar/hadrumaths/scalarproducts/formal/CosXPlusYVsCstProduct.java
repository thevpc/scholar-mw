package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:13:30
 */
final class CosXPlusYVsCstProduct implements FormalScalarProductHelper {

    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        return
                diff == 0 ||
//                (diff != 0 ? (absdbl((diff) / a) < tolerance) : (absdbl((diff) / b) < tolerance))
                        (diff < 0 ? (Math.abs((diff) / a) < tolerance) : (Math.abs((diff) / b) < tolerance))
                ;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }
//    public double compute(Domain domain, DFunction f1, DFunction f2) {
//        return new CosCosVsCosCosScalarProductOld2().compute(domain, f1, f2);
//    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        CosXPlusY f1ok = (CosXPlusY) f1;
        DoubleValue f2ok = (DoubleValue) f2;
        double d = evalTolerant(domain, f1ok, f2ok, 1E-15);
        if (Double.isNaN(d)) {
            double d0 = eval(domain, f1ok, f2ok);
            d = d0;
        }
        return d;
    }

    private static double evalTolerant(Domain domain, CosXPlusY f, DoubleValue g, double tolerance) {
        return eval(domain, f, g);
//        if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_fa_fc(domain, f, g);
//        } else if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_fa_cf(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_af_fc(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_af_cf(domain, f, g);
//        } else if (tolerantEqual(f.a, g.a, tolerance)) {
//            return primi_cos4_fa_fgc(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance)) {
//            return primi_cos4_af_fgc(domain, f, g);
//        } else if (tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_fga_fc(domain, f, g);
//        } else if (tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_fga_cf(domain, f, g);
//        } else {
//            return primi_cos4_fga_fgc(domain, f, g);
//        }
    }

    static double eval(Domain domain, CosXPlusY f, DoubleValue g) {
        double a1 = f.getA();
        double b1 = f.getB();
        double c1 = f.getC();
        double d = g.toDouble();
        double xmin = domain.xmin();
        double xmax = domain.xmax();
        double ymin = domain.ymin();
        double ymax = domain.ymax();

        double v = 0.0;
        if (a1 == 0 && b1 == 0) {
            v = d * cos(c1) * (ymax - ymin) * (xmax - xmin);

        } else if (a1 == 0) {
            v = (d * (xmax - xmin) / b1) * (sin(b1 * ymax + c1) - sin(b1 * ymin + c1));
        } else if (b1 == 0) {
            v = (d * (ymax - ymin) / a1) * (sin(a1 * xmax + c1) - sin(a1 * xmin + c1));
        } else {
            v = ((-d / (a1 * b1)) * (cos(a1 * xmax + b1 * ymax + c1) - cos(a1 * xmax + b1 * ymin + c1))) +
                    ((d / (a1 * b1)) * (cos(a1 * xmin + b1 * ymax + c1) - cos(a1 * xmin + b1 * ymin + c1)));
        }

        return v * f.getAmp();
    }

}