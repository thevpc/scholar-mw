package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

import static net.vpc.scholar.hadrumaths.Maths.cos2;
import static net.vpc.scholar.hadrumaths.Maths.sin2;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class CosCosVsLinearScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
//        double b1 = domain.xmin;
//        double b2 = domain.xmax;
//        double b3 = domain.ymin;
//        double b4 = domain.ymax;
        CosXCosY f = (CosXCosY) f1;
        Linear g = (Linear) f2;
//        return _ps_cc_x(b1, b3, f, g)
//                + _ps_cc_x(b2, b4, f, g)
//                - _ps_cc_x(b1, b4, f, g)
//                - _ps_cc_x(b2, b3, f, g);
        return primi_coslinear(domain, f, g);
    }

//    private static double _ps_cc_x(double x, double y, DCosCosFunction f, DLinearFunction g) {
//        if (f.a == 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos2(f.b) * Math.cos2(f.d) * g.b * x * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos2(f.d) * g.b / f.a * Math.sin2(f.a * x + f.b) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a == 0)
//            return f.amp * Math.cos2(f.b) * g.b * x / f.c * Math.sin2(f.c * y + f.d);
//
//        else if (f.a != 0 && f.c != 0 && g.a == 0)
//            return f.amp * g.b / f.a * Math.sin2(f.a * x + f.b) / f.c * Math.sin2(f.c * y + f.d);
//
//        else if (f.a == 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos2(f.b) * Math.cos2(f.d) * (0.5 * g.a * x * x + g.b * x) * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos2(f.d) / f.a * (g.a / f.a * (Math.cos2(f.a * x + f.b) + (f.a * x + f.b) * Math.sin2(f.a * x + f.b)) - g.a / f.a * f.b * Math.sin2(f.a * x + f.b) + g.b * Math.sin2(f.a * x + f.b)) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a != 0)
//            return f.amp * Math.cos2(f.b) * (0.5 * g.a * x * x + g.b * x) / f.c * Math.sin2(f.c * y + f.d);
//
//        else //%none is null
//            return f.amp / f.a * (g.a / f.a * (Math.cos2(f.a * x + f.b) + (f.a * x + f.b) * Math.sin2(f.a * x + f.b)) - g.a / f.a * f.b * Math.sin2(f.a * x + f.b) + g.b * Math.sin2(f.a * x + f.b)) / f.c * Math.sin2(f.c * y + f.d);
//    }


    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*(g_a*x+g_b*y+g_c)
    public static double primi_coslinear(Domain domain, CosXCosY f, Linear g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.b != 0) {
            //       t0 = f.amp*(g.a*cos2(f.a*x+f.b)*sin2(f.c*y+f.d)*f.c+g.a*sin2(f.a*x+f.b)*x*sin2(f.c*y+f.d)*f.a*f.c+g.b*sin2(f.a*x+f.b)*f.a*cos2(f.c*y+f.d)+g.b*sin2(f.a*x+f.b)*f.a*sin2(f.c*y+f.d)*f.c*y+g.c*sin2(f.a*x+f.b)*sin2(f.c*y+f.d)*f.a*f.c)/(f.a*f.a)/(f.c*f.c);
            double t0;
            t0 = f.amp * (g.a * cos2(f.a * b1 + f.b) * sin2(f.c * b3 + f.d) * f.c + g.a * sin2(f.a * b1 + f.b) * b1 * sin2(f.c * b3 + f.d) * f.a * f.c + g.b * sin2(f.a * b1 + f.b) * f.a * cos2(f.c * b3 + f.d) + g.b * sin2(f.a * b1 + f.b) * f.a * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.a * b1 + f.b) * sin2(f.c * b3 + f.d) * f.a * f.c) / (f.a * f.a) / (f.c * f.c);
            value = t0;
            t0 = f.amp * (g.a * cos2(f.a * b2 + f.b) * sin2(f.c * b4 + f.d) * f.c + g.a * sin2(f.a * b2 + f.b) * b2 * sin2(f.c * b4 + f.d) * f.a * f.c + g.b * sin2(f.a * b2 + f.b) * f.a * cos2(f.c * b4 + f.d) + g.b * sin2(f.a * b2 + f.b) * f.a * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.a * b2 + f.b) * sin2(f.c * b4 + f.d) * f.a * f.c) / (f.a * f.a) / (f.c * f.c);
            value += t0;
            t0 = f.amp * (g.a * cos2(f.a * b1 + f.b) * sin2(f.c * b4 + f.d) * f.c + g.a * sin2(f.a * b1 + f.b) * b1 * sin2(f.c * b4 + f.d) * f.a * f.c + g.b * sin2(f.a * b1 + f.b) * f.a * cos2(f.c * b4 + f.d) + g.b * sin2(f.a * b1 + f.b) * f.a * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.a * b1 + f.b) * sin2(f.c * b4 + f.d) * f.a * f.c) / (f.a * f.a) / (f.c * f.c);
            value -= t0;
            t0 = f.amp * (g.a * cos2(f.a * b2 + f.b) * sin2(f.c * b3 + f.d) * f.c + g.a * sin2(f.a * b2 + f.b) * b2 * sin2(f.c * b3 + f.d) * f.a * f.c + g.b * sin2(f.a * b2 + f.b) * f.a * cos2(f.c * b3 + f.d) + g.b * sin2(f.a * b2 + f.b) * f.a * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.a * b2 + f.b) * sin2(f.c * b3 + f.d) * f.a * f.c) / (f.a * f.a) / (f.c * f.c);
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.b)*x*(g.a*x*sin2(f.c*y+f.d)*f.c+2.0*g.b*cos2(f.c*y+f.d)+2.0*g.b*sin2(f.c*y+f.d)*f.c*y+2.0*g.c*sin2(f.c*y+f.d)*f.c)/(f.c*f.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * b1 * (g.a * b1 * sin2(f.c * b3 + f.d) * f.c + 2.0 * g.b * cos2(f.c * b3 + f.d) + 2.0 * g.b * sin2(f.c * b3 + f.d) * f.c * b3 + 2.0 * g.c * sin2(f.c * b3 + f.d) * f.c) / (f.c * f.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * b2 * (g.a * b2 * sin2(f.c * b4 + f.d) * f.c + 2.0 * g.b * cos2(f.c * b4 + f.d) + 2.0 * g.b * sin2(f.c * b4 + f.d) * f.c * b4 + 2.0 * g.c * sin2(f.c * b4 + f.d) * f.c) / (f.c * f.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * b1 * (g.a * b1 * sin2(f.c * b4 + f.d) * f.c + 2.0 * g.b * cos2(f.c * b4 + f.d) + 2.0 * g.b * sin2(f.c * b4 + f.d) * f.c * b4 + 2.0 * g.c * sin2(f.c * b4 + f.d) * f.c) / (f.c * f.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * b2 * (g.a * b2 * sin2(f.c * b3 + f.d) * f.c + 2.0 * g.b * cos2(f.c * b3 + f.d) + 2.0 * g.b * sin2(f.c * b3 + f.d) * f.c * b3 + 2.0 * g.c * sin2(f.c * b3 + f.d) * f.c) / (f.c * f.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.d)*y*(2.0*g.a*cos2(f.a*x+f.b)+2.0*g.a*sin2(f.a*x+f.b)*f.a*x+g.b*y*sin2(f.a*x+f.b)*f.a+2.0*g.c*sin2(f.a*x+f.b)*f.a)/(f.a*f.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * b3 * (2.0 * g.a * cos2(f.a * b1 + f.b) + 2.0 * g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.b * b3 * sin2(f.a * b1 + f.b) * f.a + 2.0 * g.c * sin2(f.a * b1 + f.b) * f.a) / (f.a * f.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * b4 * (2.0 * g.a * cos2(f.a * b2 + f.b) + 2.0 * g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.b * b4 * sin2(f.a * b2 + f.b) * f.a + 2.0 * g.c * sin2(f.a * b2 + f.b) * f.a) / (f.a * f.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * b4 * (2.0 * g.a * cos2(f.a * b1 + f.b) + 2.0 * g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.b * b4 * sin2(f.a * b1 + f.b) * f.a + 2.0 * g.c * sin2(f.a * b1 + f.b) * f.a) / (f.a * f.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * b3 * (2.0 * g.a * cos2(f.a * b2 + f.b) + 2.0 * g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.b * b3 * sin2(f.a * b2 + f.b) * f.a + 2.0 * g.c * sin2(f.a * b2 + f.b) * f.a) / (f.a * f.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*x*y*g.a/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*y*g.b/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*g.c;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b1 * b3 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * b3 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * g.c;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b2 * b4 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * b4 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * g.c;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b1 * b4 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * b4 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * g.c;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b2 * b3 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * b3 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * g.c;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.b != 0) {
            //       t0 = f.amp*sin2(f.a*x+f.b)*(g.b*cos2(f.c*y+f.d)+g.b*sin2(f.c*y+f.d)*f.c*y+g.c*sin2(f.c*y+f.d)*f.c)/f.a/(f.c*f.c);
            double t0;
            t0 = f.amp * sin2(f.a * b1 + f.b) * (g.b * cos2(f.c * b3 + f.d) + g.b * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.c * b3 + f.d) * f.c) / f.a / (f.c * f.c);
            value = t0;
            t0 = f.amp * sin2(f.a * b2 + f.b) * (g.b * cos2(f.c * b4 + f.d) + g.b * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.c * b4 + f.d) * f.c) / f.a / (f.c * f.c);
            value += t0;
            t0 = f.amp * sin2(f.a * b1 + f.b) * (g.b * cos2(f.c * b4 + f.d) + g.b * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.c * b4 + f.d) * f.c) / f.a / (f.c * f.c);
            value -= t0;
            t0 = f.amp * sin2(f.a * b2 + f.b) * (g.b * cos2(f.c * b3 + f.d) + g.b * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.c * b3 + f.d) * f.c) / f.a / (f.c * f.c);
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.b)*x*(g.b*cos2(f.c*y+f.d)+g.b*sin2(f.c*y+f.d)*f.c*y+g.c*sin2(f.c*y+f.d)*f.c)/(f.c*f.c);
            double t0;
            t0 = f.amp * cos2(f.b) * b1 * (g.b * cos2(f.c * b3 + f.d) + g.b * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.c * b3 + f.d) * f.c) / (f.c * f.c);
            value = t0;
            t0 = f.amp * cos2(f.b) * b2 * (g.b * cos2(f.c * b4 + f.d) + g.b * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.c * b4 + f.d) * f.c) / (f.c * f.c);
            value += t0;
            t0 = f.amp * cos2(f.b) * b1 * (g.b * cos2(f.c * b4 + f.d) + g.b * sin2(f.c * b4 + f.d) * f.c * b4 + g.c * sin2(f.c * b4 + f.d) * f.c) / (f.c * f.c);
            value -= t0;
            t0 = f.amp * cos2(f.b) * b2 * (g.b * cos2(f.c * b3 + f.d) + g.b * sin2(f.c * b3 + f.d) * f.c * b3 + g.c * sin2(f.c * b3 + f.d) * f.c) / (f.c * f.c);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.d)*sin2(f.a*x+f.b)*y*(g.b*y+2.0*g.c)/f.a/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * sin2(f.a * b1 + f.b) * b3 * (g.b * b3 + 2.0 * g.c) / f.a / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * sin2(f.a * b2 + f.b) * b4 * (g.b * b4 + 2.0 * g.c) / f.a / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * sin2(f.a * b1 + f.b) * b4 * (g.b * b4 + 2.0 * g.c) / f.a / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * sin2(f.a * b2 + f.b) * b3 * (g.b * b3 + 2.0 * g.c) / f.a / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.b != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*y*y*g.b/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*g.c;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * b3 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * g.c;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * b4 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * g.c;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * b4 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * g.c;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * b3 * g.b / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * g.c;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.b == 0) {
            //       t0 = f.amp*(g.a*cos2(f.a*x+f.b)+g.a*sin2(f.a*x+f.b)*f.a*x+g.c*sin2(f.a*x+f.b)*f.a)*sin2(f.c*y+f.d)/(f.a*f.a)/f.c;
            double t0;
            t0 = f.amp * (g.a * cos2(f.a * b1 + f.b) + g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.c * sin2(f.a * b1 + f.b) * f.a) * sin2(f.c * b3 + f.d) / (f.a * f.a) / f.c;
            value = t0;
            t0 = f.amp * (g.a * cos2(f.a * b2 + f.b) + g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.c * sin2(f.a * b2 + f.b) * f.a) * sin2(f.c * b4 + f.d) / (f.a * f.a) / f.c;
            value += t0;
            t0 = f.amp * (g.a * cos2(f.a * b1 + f.b) + g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.c * sin2(f.a * b1 + f.b) * f.a) * sin2(f.c * b4 + f.d) / (f.a * f.a) / f.c;
            value -= t0;
            t0 = f.amp * (g.a * cos2(f.a * b2 + f.b) + g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.c * sin2(f.a * b2 + f.b) * f.a) * sin2(f.c * b3 + f.d) / (f.a * f.a) / f.c;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.b == 0) {
            //       t0 = f.amp*cos2(f.b)*x*(g.a*x+2.0*g.c)*sin2(f.c*y+f.d)/f.c/2.0;

//    double t0;
//          t0 = b1*(g.a*b1+2.0*g.c)*sin2(f.c*b3+f.d);
//    value  = t0;
//          t0 = b2*(g.a*b2+2.0*g.c)*sin2(f.c*b4+f.d);
//    value += t0;
//          t0 = b1*(g.a*b1+2.0*g.c)*sin2(f.c*b4+f.d);
//    value -= t0;
//          t0 = b2*(g.a*b2+2.0*g.c)*sin2(f.c*b3+f.d);
//    value -= t0;

            value = b1 * (g.a * b1 + 2.0 * g.c) * (sin2(f.c * b3 + f.d) - sin2(f.c * b4 + f.d))
                    + b2 * (g.a * b2 + 2.0 * g.c) * (sin2(f.c * b4 + f.d) - sin2(f.c * b3 + f.d))
            ;
            return f.amp * cos2(f.b) * value / f.c / 2.0;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.b == 0) {
            //       t0 = f.amp*cos2(f.d)*(g.a*cos2(f.a*x+f.b)+g.a*sin2(f.a*x+f.b)*f.a*x+g.c*sin2(f.a*x+f.b)*f.a)*y/(f.a*f.a);
            double t0;
            t0 = (g.a * cos2(f.a * b1 + f.b) + g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.c * sin2(f.a * b1 + f.b) * f.a) * b3;
            value = t0;
            t0 = (g.a * cos2(f.a * b2 + f.b) + g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.c * sin2(f.a * b2 + f.b) * f.a) * b4;
            value += t0;
            t0 = (g.a * cos2(f.a * b1 + f.b) + g.a * sin2(f.a * b1 + f.b) * f.a * b1 + g.c * sin2(f.a * b1 + f.b) * f.a) * b4;
            value -= t0;
            t0 = (g.a * cos2(f.a * b2 + f.b) + g.a * sin2(f.a * b2 + f.b) * f.a * b2 + g.c * sin2(f.a * b2 + f.b) * f.a) * b3;
            value -= t0;
            return f.amp * cos2(f.d) * value / (f.a * f.a);

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.b == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*x*y*g.a/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*g.c;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b1 * b3 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * g.c;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b2 * b4 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * g.c;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b1 * b4 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * g.c;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b2 * b3 * g.a / 2.0 + f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * g.c;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.b == 0) {
            //       t0 = f.amp*g.c/f.a*sin2(f.a*x+f.b)/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * g.c / f.a * sin2(f.a * b1 + f.b) / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * g.c / f.a * sin2(f.a * b2 + f.b) / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * g.c / f.a * sin2(f.a * b1 + f.b) / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * g.c / f.a * sin2(f.a * b2 + f.b) / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.b == 0) {
            //       t0 = f.amp*cos2(f.b)*g.c*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.c * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.c * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.c * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.c * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.b == 0) {
            //       t0 = f.amp*cos2(f.d)*g.c/f.a*sin2(f.a*x+f.b)*y;
            double t0;
            t0 = f.amp * cos2(f.d) * g.c / f.a * sin2(f.a * b1 + f.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.c / f.a * sin2(f.a * b2 + f.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.c / f.a * sin2(f.a * b1 + f.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.c / f.a * sin2(f.a * b2 + f.b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*y*g.c;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b3 * g.c;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b4 * g.c;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b1 * b4 * g.c;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * b2 * b3 * g.c;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------


    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*(g_a*x+g_b)
    public static double primi_coslinear_old(Domain domain, CosXCosY f, Linear g) {
        double x;
        double y;
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a == 0 && f.c == 0 && g.a == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.b * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.b * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.b * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.b * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.b / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.b / f.a * sin2(f.a * x + f.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.b / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.b / f.a * sin2(f.a * x + f.b) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.b * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.b * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.b * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.b * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.b / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.b / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.b / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.b / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * (0.5 * g.a * x * x + g.b * x) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * (0.5 * g.a * x * x + g.b * x) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * (0.5 * g.a * x * x + g.b * x) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * (0.5 * g.a * x * x + g.b * x) * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * (0.5 * g.a * x * x + g.b * x) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * (0.5 * g.a * x * x + g.b * x) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * (0.5 * g.a * x * x + g.b * x) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * (0.5 * g.a * x * x + g.b * x) / f.c * sin2(f.c * y + f.d));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp / f.a * (g.a / f.a * (cos2(f.a * x + f.b) + (f.a * x + f.b) * sin2(f.a * x + f.b)) - g.a / f.a * f.b * sin2(f.a * x + f.b) + g.b * sin2(f.a * x + f.b)) / f.c * sin2(f.c * y + f.d));
            return value;
        }
    }
//ENDING---------------------------------------
}
