package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class LinearVsCstScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return primi_linearcst(domain, (Linear) f1, (DoubleValue) f2);
//        double b1 = domain.xmin;
//        double b2 = domain.xmax;
//        double b3 = domain.ymin;
//        double b4 = domain.ymax;
//        DLinearFunction f1ok = (DLinearFunction) f1;
//        DCstFunction f2ok = (DCstFunction) f2;
//        double x;
//        double y;
//        double value;
//        x=b1;y=b3;value =g.cst * x * y * (0.5 * f.a * x + f.b);
//        x=b1;y=b3;value =g.cst * x * y * (0.5 * f.a * x + f.b);
//        x=b1;y=b3;value =g.cst * x * y * (0.5 * f.a * x + f.b);
//        x=b1;y=b3;value =g.cst * x * y * (0.5 * f.a * x + f.b);
//        return _ps_ls_x(b1, b3, f1ok, f2ok)
//                + _ps_ls_x(b2, b4, f1ok, f2ok)
//                - _ps_ls_x(b1, b4, f1ok, f2ok)
//                - _ps_ls_x(b2, b3, f1ok, f2ok);
    }

//    private static double _ps_ls_x(double x, double y, DLinearFunction f1, DCstFunction f2) {
//        return f2.cst * x * y * (0.5 * f1.a * x + f1.b);
//    }

    //STARTING---------------------------------------
// THIS FILE WAS GENERATED AUTOMATICALLY.
// DO NOT EDIT MANUALLY.
// INTEGRATION FOR (f_a*x+f_b*y+f_c)*g_cst
    public static double primi_linearcst(Domain domain, Linear f, DoubleValue g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();

        double gvalue=g.toDouble();
        if (fa != 0 && fb != 0) {
//       t0 = g.cst*x*y*(f.a*x+f.b*y+2.0*f.c)/2.0;
            double t0;
            t0 = gvalue * b1 * b3 * (fa * b1 + fb * b3 + 2.0 * fc) / 2.0;
            value = t0;
            t0 = gvalue * b2 * b4 * (fa * b2 + fb * b4 + 2.0 * fc) / 2.0;
            value += t0;
            t0 = gvalue * b1 * b4 * (fa * b1 + fb * b4 + 2.0 * fc) / 2.0;
            value -= t0;
            t0 = gvalue * b2 * b3 * (fa * b2 + fb * b3 + 2.0 * fc) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fb != 0) {
//       t0 = g.cst*x*y*(f.b*y+2.0*f.c)/2.0;
            double t0;
            t0 = gvalue * b1 * b3 * (fb * b3 + 2.0 * fc) / 2.0;
            value = t0;
            t0 = gvalue * b2 * b4 * (fb * b4 + 2.0 * fc) / 2.0;
            value += t0;
            t0 = gvalue * b1 * b4 * (fb * b4 + 2.0 * fc) / 2.0;
            value -= t0;
            t0 = gvalue * b2 * b3 * (fb * b3 + 2.0 * fc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb == 0) {
//       t0 = g.cst*x*(f.a*x+2.0*f.c)*y/2.0;
            double t0;
            t0 = gvalue * b1 * (fa * b1 + 2.0 * fc) * b3 / 2.0;
            value = t0;
            t0 = gvalue * b2 * (fa * b2 + 2.0 * fc) * b4 / 2.0;
            value += t0;
            t0 = gvalue * b1 * (fa * b1 + 2.0 * fc) * b4 / 2.0;
            value -= t0;
            t0 = gvalue * b2 * (fa * b2 + 2.0 * fc) * b3 / 2.0;
            value -= t0;
            return value;

        } else { //all are nulls
//       t0 = f.c*g.cst*x*y;
            double t0;
            t0 = fc * gvalue * b1 * b3;
            value = t0;
            t0 = fc * gvalue * b2 * b4;
            value += t0;
            t0 = fc * gvalue * b1 * b4;
            value -= t0;
            t0 = fc * gvalue * b2 * b3;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}
