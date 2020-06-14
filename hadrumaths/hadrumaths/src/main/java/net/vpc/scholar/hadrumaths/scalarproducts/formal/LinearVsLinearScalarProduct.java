package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class LinearVsLinearScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return primi_linear2(domain, (Linear) f1, (Linear) f2);
//        double b1 = domain.xmin;
//        double b2 = domain.xmax;
//        double b3 = domain.ymin;
//        double b4 = domain.ymax;
//        DLinearFunction f1ok = (DLinearFunction) f1;
//        DLinearFunction f2ok = (DLinearFunction) f2;
//        return _ps_ll_x(b1, b3, f1ok, f2ok)
//                + _ps_ll_x(b2, b4, f1ok, f2ok)
//                - _ps_ll_x(b1, b4, f1ok, f2ok)
//                - _ps_ll_x(b2, b3, f1ok, f2ok);
    }

//    private static double _ps_ll_x(double x, double y, DLinearFunction f1, DLinearFunction f2) {
//        if (f1.a == 0 && f2.a == 0) {
//            return f1.b * f2.b * x * y;
//
////        } else if (f1.a != 0 && f2.a == 0) {
//        } else if (f2.a == 0) {
//            return f2.b * x * y * (0.5 * f1.a * x + f1.b);
//
////        } else if (f1.a == 0 && f2.a != 0) {
//        } else if (f1.a == 0) {
//            return f1.b * x * y * (0.5 * f2.a * x + f2.b);
//
//        } else {//none is null
//            return x * y * (1.0 / 3.0 * f1.a * f2.a * x * x + 0.5 * (f1.b * f2.a + f1.a * f2.b) * x + f1.b * f2.b);
//        }
//    }

    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR (f_a*x+f_b*y+f_c)*(g_a*x+g_b*y+g_c)
    public static double primi_linear2(Domain domain, Linear f, Linear g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        if (fa != 0 && fb != 0 && ga != 0 && gb != 0) {
            //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*fc*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*gc*y/2.0+x*f.b*g.b*y*y*y/3.0+x*y*y*fc*g.b/2.0+x*y*y*f.b*gc/2.0+x*fc*gc*y;
            double t0;
            t0 = ga * fa * b1 * b1 * b1 * b3 / 3.0 + b1 * b1 * ga * fb * b3 * b3 / 4.0 + b1 * b1 * ga * fc * b3 / 2.0 + b1 * b1 * fa * gb * b3 * b3 / 4.0 + b1 * b1 * fa * gc * b3 / 2.0 + b1 * fb * gb * b3 * b3 * b3 / 3.0 + b1 * b3 * b3 * fc * gb / 2.0 + b1 * b3 * b3 * fb * gc / 2.0 + b1 * fc * gc * b3;
            value = t0;
            t0 = ga * fa * b2 * b2 * b2 * b4 / 3.0 + b2 * b2 * ga * fb * b4 * b4 / 4.0 + b2 * b2 * ga * fc * b4 / 2.0 + b2 * b2 * fa * gb * b4 * b4 / 4.0 + b2 * b2 * fa * gc * b4 / 2.0 + b2 * fb * gb * b4 * b4 * b4 / 3.0 + b2 * b4 * b4 * fc * gb / 2.0 + b2 * b4 * b4 * fb * gc / 2.0 + b2 * fc * gc * b4;
            value += t0;
            t0 = ga * fa * b1 * b1 * b1 * b4 / 3.0 + b1 * b1 * ga * fb * b4 * b4 / 4.0 + b1 * b1 * ga * fc * b4 / 2.0 + b1 * b1 * fa * gb * b4 * b4 / 4.0 + b1 * b1 * fa * gc * b4 / 2.0 + b1 * fb * gb * b4 * b4 * b4 / 3.0 + b1 * b4 * b4 * fc * gb / 2.0 + b1 * b4 * b4 * fb * gc / 2.0 + b1 * fc * gc * b4;
            value -= t0;
            t0 = ga * fa * b2 * b2 * b2 * b3 / 3.0 + b2 * b2 * ga * fb * b3 * b3 / 4.0 + b2 * b2 * ga * fc * b3 / 2.0 + b2 * b2 * fa * gb * b3 * b3 / 4.0 + b2 * b2 * fa * gc * b3 / 2.0 + b2 * fb * gb * b3 * b3 * b3 / 3.0 + b2 * b3 * b3 * fc * gb / 2.0 + b2 * b3 * b3 * fb * gc / 2.0 + b2 * fc * gc * b3;
            value -= t0;
            return value;

        } else if (fa == 0 && fb != 0 && ga != 0 && gb != 0) {
            //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*fc*g.b/2.0+x*x*g.a*f.b*y*y/4.0+x*y*y*f.b*gc/2.0+x*x*g.a*fc*y/2.0+x*fc*gc*y;
            double t0;
            t0 = b1 * fb * gb * b3 * b3 * b3 / 3.0 + b1 * b3 * b3 * fc * gb / 2.0 + b1 * b1 * ga * fb * b3 * b3 / 4.0 + b1 * b3 * b3 * fb * gc / 2.0 + b1 * b1 * ga * fc * b3 / 2.0 + b1 * fc * gc * b3;
            value = t0;
            t0 = b2 * fb * gb * b4 * b4 * b4 / 3.0 + b2 * b4 * b4 * fc * gb / 2.0 + b2 * b2 * ga * fb * b4 * b4 / 4.0 + b2 * b4 * b4 * fb * gc / 2.0 + b2 * b2 * ga * fc * b4 / 2.0 + b2 * fc * gc * b4;
            value += t0;
            t0 = b1 * fb * gb * b4 * b4 * b4 / 3.0 + b1 * b4 * b4 * fc * gb / 2.0 + b1 * b1 * ga * fb * b4 * b4 / 4.0 + b1 * b4 * b4 * fb * gc / 2.0 + b1 * b1 * ga * fc * b4 / 2.0 + b1 * fc * gc * b4;
            value -= t0;
            t0 = b2 * fb * gb * b3 * b3 * b3 / 3.0 + b2 * b3 * b3 * fc * gb / 2.0 + b2 * b2 * ga * fb * b3 * b3 / 4.0 + b2 * b3 * b3 * fb * gc / 2.0 + b2 * b2 * ga * fc * b3 / 2.0 + b2 * fc * gc * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fb == 0 && ga != 0 && gb != 0) {
            //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*fc*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*gc*y/2.0+x*y*y*fc*g.b/2.0+x*fc*gc*y;
            double t0;
            t0 = ga * fa * b1 * b1 * b1 * b3 / 3.0 + b1 * b1 * ga * fc * b3 / 2.0 + b1 * b1 * fa * gb * b3 * b3 / 4.0 + b1 * b1 * fa * gc * b3 / 2.0 + b1 * b3 * b3 * fc * gb / 2.0 + b1 * fc * gc * b3;
            value = t0;
            t0 = ga * fa * b2 * b2 * b2 * b4 / 3.0 + b2 * b2 * ga * fc * b4 / 2.0 + b2 * b2 * fa * gb * b4 * b4 / 4.0 + b2 * b2 * fa * gc * b4 / 2.0 + b2 * b4 * b4 * fc * gb / 2.0 + b2 * fc * gc * b4;
            value += t0;
            t0 = ga * fa * b1 * b1 * b1 * b4 / 3.0 + b1 * b1 * ga * fc * b4 / 2.0 + b1 * b1 * fa * gb * b4 * b4 / 4.0 + b1 * b1 * fa * gc * b4 / 2.0 + b1 * b4 * b4 * fc * gb / 2.0 + b1 * fc * gc * b4;
            value -= t0;
            t0 = ga * fa * b2 * b2 * b2 * b3 / 3.0 + b2 * b2 * ga * fc * b3 / 2.0 + b2 * b2 * fa * gb * b3 * b3 / 4.0 + b2 * b2 * fa * gc * b3 / 2.0 + b2 * b3 * b3 * fc * gb / 2.0 + b2 * fc * gc * b3;
            value -= t0;
            return value;

        } else if (fa == 0 && fb == 0 && ga != 0 && gb != 0) {
            //       t0 = fc*x*y*(g.a*x+g.b*y+2.0*gc)/2.0;
            double t0;
            t0 = fc * b1 * b3 * (ga * b1 + gb * b3 + 2.0 * gc) / 2.0;
            value = t0;
            t0 = fc * b2 * b4 * (ga * b2 + gb * b4 + 2.0 * gc) / 2.0;
            value += t0;
            t0 = fc * b1 * b4 * (ga * b1 + gb * b4 + 2.0 * gc) / 2.0;
            value -= t0;
            t0 = fc * b2 * b3 * (ga * b2 + gb * b3 + 2.0 * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb != 0 && ga == 0 && gb != 0) {
            //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*f.b*gc/2.0+x*x*f.a*g.b*y*y/4.0+x*y*y*fc*g.b/2.0+x*x*f.a*gc*y/2.0+x*fc*gc*y;
            double t0;
            t0 = b1 * fb * gb * b3 * b3 * b3 / 3.0 + b1 * b3 * b3 * fb * gc / 2.0 + b1 * b1 * fa * gb * b3 * b3 / 4.0 + b1 * b3 * b3 * fc * gb / 2.0 + b1 * b1 * fa * gc * b3 / 2.0 + b1 * fc * gc * b3;
            value = t0;
            t0 = b2 * fb * gb * b4 * b4 * b4 / 3.0 + b2 * b4 * b4 * fb * gc / 2.0 + b2 * b2 * fa * gb * b4 * b4 / 4.0 + b2 * b4 * b4 * fc * gb / 2.0 + b2 * b2 * fa * gc * b4 / 2.0 + b2 * fc * gc * b4;
            value += t0;
            t0 = b1 * fb * gb * b4 * b4 * b4 / 3.0 + b1 * b4 * b4 * fb * gc / 2.0 + b1 * b1 * fa * gb * b4 * b4 / 4.0 + b1 * b4 * b4 * fc * gb / 2.0 + b1 * b1 * fa * gc * b4 / 2.0 + b1 * fc * gc * b4;
            value -= t0;
            t0 = b2 * fb * gb * b3 * b3 * b3 / 3.0 + b2 * b3 * b3 * fb * gc / 2.0 + b2 * b2 * fa * gb * b3 * b3 / 4.0 + b2 * b3 * b3 * fc * gb / 2.0 + b2 * b2 * fa * gc * b3 / 2.0 + b2 * fc * gc * b3;
            value -= t0;
            return value;

        } else if (fa == 0 && fb != 0 && ga == 0 && gb != 0) {
            //       t0 = x*y*(2.0*f.b*g.b*y*y+3.0*y*fc*g.b+3.0*y*f.b*gc+6.0*fc*gc)/6.0;
            double t0;
            t0 = b1 * b3 * (2.0 * fb * gb * b3 * b3 + 3.0 * b3 * fc * gb + 3.0 * b3 * fb * gc + 6.0 * fc * gc) / 6.0;
            value = t0;
            t0 = b2 * b4 * (2.0 * fb * gb * b4 * b4 + 3.0 * b4 * fc * gb + 3.0 * b4 * fb * gc + 6.0 * fc * gc) / 6.0;
            value += t0;
            t0 = b1 * b4 * (2.0 * fb * gb * b4 * b4 + 3.0 * b4 * fc * gb + 3.0 * b4 * fb * gc + 6.0 * fc * gc) / 6.0;
            value -= t0;
            t0 = b2 * b3 * (2.0 * fb * gb * b3 * b3 + 3.0 * b3 * fc * gb + 3.0 * b3 * fb * gc + 6.0 * fc * gc) / 6.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb == 0 && ga == 0 && gb != 0) {
            //       t0 = x*(f.a*x+2.0*fc)*y*(g.b*y+2.0*gc)/4.0;
            double t0;
            t0 = b1 * (fa * b1 + 2.0 * fc) * b3 * (gb * b3 + 2.0 * gc) / 4.0;
            value = t0;
            t0 = b2 * (fa * b2 + 2.0 * fc) * b4 * (gb * b4 + 2.0 * gc) / 4.0;
            value += t0;
            t0 = b1 * (fa * b1 + 2.0 * fc) * b4 * (gb * b4 + 2.0 * gc) / 4.0;
            value -= t0;
            t0 = b2 * (fa * b2 + 2.0 * fc) * b3 * (gb * b3 + 2.0 * gc) / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fb == 0 && ga == 0 && gb != 0) {
            //       t0 = fc*x*y*(g.b*y+2.0*gc)/2.0;
            double t0;
            t0 = fc * b1 * b3 * (gb * b3 + 2.0 * gc) / 2.0;
            value = t0;
            t0 = fc * b2 * b4 * (gb * b4 + 2.0 * gc) / 2.0;
            value += t0;
            t0 = fc * b1 * b4 * (gb * b4 + 2.0 * gc) / 2.0;
            value -= t0;
            t0 = fc * b2 * b3 * (gb * b3 + 2.0 * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb != 0 && ga != 0 && gb == 0) {
            //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*fc*y/2.0+x*x*f.a*gc*y/2.0+x*y*y*f.b*gc/2.0+x*fc*gc*y;
            double t0;
            t0 = ga * fa * b1 * b1 * b1 * b3 / 3.0 + b1 * b1 * ga * fb * b3 * b3 / 4.0 + b1 * b1 * ga * fc * b3 / 2.0 + b1 * b1 * fa * gc * b3 / 2.0 + b1 * b3 * b3 * fb * gc / 2.0 + b1 * fc * gc * b3;
            value = t0;
            t0 = ga * fa * b2 * b2 * b2 * b4 / 3.0 + b2 * b2 * ga * fb * b4 * b4 / 4.0 + b2 * b2 * ga * fc * b4 / 2.0 + b2 * b2 * fa * gc * b4 / 2.0 + b2 * b4 * b4 * fb * gc / 2.0 + b2 * fc * gc * b4;
            value += t0;
            t0 = ga * fa * b1 * b1 * b1 * b4 / 3.0 + b1 * b1 * ga * fb * b4 * b4 / 4.0 + b1 * b1 * ga * fc * b4 / 2.0 + b1 * b1 * fa * gc * b4 / 2.0 + b1 * b4 * b4 * fb * gc / 2.0 + b1 * fc * gc * b4;
            value -= t0;
            t0 = ga * fa * b2 * b2 * b2 * b3 / 3.0 + b2 * b2 * ga * fb * b3 * b3 / 4.0 + b2 * b2 * ga * fc * b3 / 2.0 + b2 * b2 * fa * gc * b3 / 2.0 + b2 * b3 * b3 * fb * gc / 2.0 + b2 * fc * gc * b3;
            value -= t0;
            return value;

        } else if (fa == 0 && fb != 0 && ga != 0 && gb == 0) {
            //       t0 = x*(g.a*x+2.0*gc)*y*(f.b*y+2.0*fc)/4.0;
            double t0;
            t0 = b1 * (ga * b1 + 2.0 * gc) * b3 * (fb * b3 + 2.0 * fc) / 4.0;
            value = t0;
            t0 = b2 * (ga * b2 + 2.0 * gc) * b4 * (fb * b4 + 2.0 * fc) / 4.0;
            value += t0;
            t0 = b1 * (ga * b1 + 2.0 * gc) * b4 * (fb * b4 + 2.0 * fc) / 4.0;
            value -= t0;
            t0 = b2 * (ga * b2 + 2.0 * gc) * b3 * (fb * b3 + 2.0 * fc) / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb == 0 && ga != 0 && gb == 0) {
            //       t0 = x*(2.0*f.a*g.a*x*x+3.0*x*fc*g.a+3.0*x*f.a*gc+6.0*fc*gc)*y/6.0;
            double t0;
            t0 = b1 * (2.0 * fa * ga * b1 * b1 + 3.0 * b1 * fc * ga + 3.0 * b1 * fa * gc + 6.0 * fc * gc) * b3 / 6.0;
            value = t0;
            t0 = b2 * (2.0 * fa * ga * b2 * b2 + 3.0 * b2 * fc * ga + 3.0 * b2 * fa * gc + 6.0 * fc * gc) * b4 / 6.0;
            value += t0;
            t0 = b1 * (2.0 * fa * ga * b1 * b1 + 3.0 * b1 * fc * ga + 3.0 * b1 * fa * gc + 6.0 * fc * gc) * b4 / 6.0;
            value -= t0;
            t0 = b2 * (2.0 * fa * ga * b2 * b2 + 3.0 * b2 * fc * ga + 3.0 * b2 * fa * gc + 6.0 * fc * gc) * b3 / 6.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fb == 0 && ga != 0 && gb == 0) {
            //       t0 = fc*x*(g.a*x+2.0*gc)*y/2.0;
            double t0;
            t0 = fc * b1 * (ga * b1 + 2.0 * gc) * b3 / 2.0;
            value = t0;
            t0 = fc * b2 * (ga * b2 + 2.0 * gc) * b4 / 2.0;
            value += t0;
            t0 = fc * b1 * (ga * b1 + 2.0 * gc) * b4 / 2.0;
            value -= t0;
            t0 = fc * b2 * (ga * b2 + 2.0 * gc) * b3 / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb != 0 && ga == 0 && gb == 0) {
            //       t0 = gc*x*y*(f.a*x+f.b*y+2.0*fc)/2.0;
            double t0;
            t0 = gc * b1 * b3 * (fa * b1 + fb * b3 + 2.0 * fc) / 2.0;
            value = t0;
            t0 = gc * b2 * b4 * (fa * b2 + fb * b4 + 2.0 * fc) / 2.0;
            value += t0;
            t0 = gc * b1 * b4 * (fa * b1 + fb * b4 + 2.0 * fc) / 2.0;
            value -= t0;
            t0 = gc * b2 * b3 * (fa * b2 + fb * b3 + 2.0 * fc) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fb != 0 && ga == 0 && gb == 0) {
            //       t0 = gc*x*y*(f.b*y+2.0*fc)/2.0;
            double t0;
            t0 = gc * b1 * b3 * (fb * b3 + 2.0 * fc) / 2.0;
            value = t0;
            t0 = gc * b2 * b4 * (fb * b4 + 2.0 * fc) / 2.0;
            value += t0;
            t0 = gc * b1 * b4 * (fb * b4 + 2.0 * fc) / 2.0;
            value -= t0;
            t0 = gc * b2 * b3 * (fb * b3 + 2.0 * fc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fb == 0 && ga == 0 && gb == 0) {
            //       t0 = gc*x*(f.a*x+2.0*fc)*y/2.0;
            double t0;
            t0 = gc * b1 * (fa * b1 + 2.0 * fc) * b3 / 2.0;
            value = t0;
            t0 = gc * b2 * (fa * b2 + 2.0 * fc) * b4 / 2.0;
            value += t0;
            t0 = gc * b1 * (fa * b1 + 2.0 * fc) * b4 / 2.0;
            value -= t0;
            t0 = gc * b2 * (fa * b2 + 2.0 * fc) * b3 / 2.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = x*fc*gc*y;
            double t0;
            t0 = b1 * fc * gc * b3;
            value = t0;
            t0 = b2 * fc * gc * b4;
            value += t0;
            t0 = b1 * fc * gc * b4;
            value -= t0;
            t0 = b2 * fc * gc * b3;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}
