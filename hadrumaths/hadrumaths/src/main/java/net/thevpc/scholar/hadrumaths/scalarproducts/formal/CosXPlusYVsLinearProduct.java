package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

import static java.lang.Math.*;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:13:30
 */
final class CosXPlusYVsLinearProduct implements FormalScalarProductHelper {

    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        return
                diff == 0 ||
//                (diff != 0 ? (absdbl((diff) / a) < tolerance) : (absdbl((diff) / b) < tolerance))
                        (diff < 0 ? (abs((diff) / a) < tolerance) : (abs((diff) / b) < tolerance))
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
        Linear f2ok = (Linear) f2;
        double d = evalTolerant(domain, f1ok, f2ok, 1E-15);
        if (Double.isNaN(d)) {
            double d0 = eval(domain, f1ok, f2ok);
            d = d0;
        }
        return d;
    }

    private static double evalTolerant(Domain domain, CosXPlusY f, Linear g, double tolerance) {
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

    static double eval(Domain domain, CosXPlusY f, Linear g) {

        double a2 = g.getA();
        double b2 = g.getB();
        double c2 = g.getC();

        double a1 = f.getA();
        double b1 = f.getB();
        double c1 = f.getC();
        double xmin = domain.xmin();
        double xmax = domain.xmax();
        double ymin = domain.ymin();
        double ymax = domain.ymax();

        double v = 0.0;
        if (a1 == 0 && b1 == 0) {
            v = (cos(c1) * ((1.0 / 2.0) * a2 * ymax * (xmax * xmax) + (1.0 / 2.0) * b2 * xmax * (ymax * ymax) + (c2 * xmax * ymax))) -
                    (cos(c1) * ((1.0 / 2.0) * a2 * ymin * (xmax * xmax) + (1.0 / 2.0) * b2 * xmax * (ymin * ymin) + (c2 * xmax * ymin))) -
                    (cos(c1) * ((1.0 / 2.0) * a2 * ymax * (xmin * xmin) + (1.0 / 2.0) * b2 * xmin * (ymax * ymax) + (c2 * xmin * ymax))) +
                    (cos(c1) * ((1.0 / 2.0) * a2 * ymin * (xmin * xmin) + (1.0 / 2.0) * b2 * xmin * (ymin * ymin) + (c2 * xmin * ymin)));

        } else if (a1 == 0) {
            v = (((a2 / (b1 * b1)) * (xmax - xmin)) * (cos(b1 * ymax + c1) - cos(b1 * ymin + c1))) +
                    ((sin(b1 * ymax + c1) / b1) * (((1.0 / 2.0) * a2 * xmax * xmax + (b2 * ymax + c2) * xmax) - ((1.0 / 2.0) * a2 * xmin * xmin + (b2 * ymax + c2) * xmin))) -
                    ((sin(b1 * ymin + c1) / b1) * (((1.0 / 2.0) * a2 * xmax * xmax + (b2 * ymin + c2) * xmax) - ((1.0 / 2.0) * a2 * xmin * xmin + (b2 * ymin + c2) * xmin)));
        } else if (b1 == 0) {
            v = (((a2 / (a1 * a1)) * (ymax - ymin)) * (cos(a1 * xmax + c1) - cos(a1 * xmin + c1))) +
                    ((sin(a1 * xmax + c1) / a1) * (((1.0 / 2.0) * b2 * ymax * ymax + (a2 * xmax + c2) * ymax) - ((1.0 / 2.0) * b2 * ymin * ymin + (a2 * xmax + c2) * ymin))) -
                    ((sin(a1 * xmin + c1) / a1) * (((1.0 / 2.0) * b2 * ymax * ymax + (a2 * xmin + c2) * ymax) - ((1.0 / 2.0) * b2 * ymin * ymin + (a2 * xmin + c2) * ymin)));

        } else {
            v = ((-1.0 / (a1 * b1)) * ((a2 * xmax + b2 * ymax + c2) * cos(a1 * xmax + b1 * ymax + c1) - (a2 * xmax + b2 * ymin + c2) * cos(a1 * xmax + b1 * ymin + c1))) +
                    (((b2) / (a1 * b1 * b1)) * (sin(a1 * xmax + b1 * ymax + c1) - sin(a1 * xmax + b1 * ymin + c1))) +
                    ((1.0 / (a1 * b1)) * ((a2 * xmin + b2 * ymax + c2) * cos(a1 * xmin + b1 * ymax + c1) - (a2 * xmin + b2 * ymin + c2) * cos(a1 * xmin + b1 * ymin + c1))) -
                    (((b2) / (a1 * b1 * b1)) * (sin(a1 * xmin + b1 * ymax + c1) - sin(a1 * xmin + b1 * ymin + c1))) +
                    (((a2) / (a1 * a1 * b1)) * (sin(a1 * xmax + b1 * ymax + c1) - sin(a1 * xmax + b1 * ymin + c1))) -
                    (((a2) / (a1 * a1 * b1)) * (sin(a1 * xmin + b1 * ymax + c1) - sin(a1 * xmin + b1 * ymin + c1)));


        }

        return v * f.getAmp();
    }

}