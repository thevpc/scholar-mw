package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

import static net.thevpc.scholar.hadrumaths.Maths.cos2;
import static net.thevpc.scholar.hadrumaths.Maths.sin2;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class CosCosVsLinearScalarProduct implements FormalScalarProductHelper {
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

        double fa = f.getA();
        double fc = f.getC();
        double ga = g.getA();
        double famp = f.getAmp();
        double fb = f.getB();
        double fd = f.getD();
        double gb = g.getB();
        if (fa == 0 && fc == 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gb * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gb * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gb * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gb * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gb / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gb / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gb / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gb / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gb * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gb * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gb * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gb * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * gb / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gb / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gb / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gb / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * (0.5 * ga * x * x + gb * x) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * (0.5 * ga * x * x + gb * x) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * (0.5 * ga * x * x + gb * x) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * (0.5 * ga * x * x + gb * x) * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * (0.5 * ga * x * x + gb * x) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * (0.5 * ga * x * x + gb * x) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * (0.5 * ga * x * x + gb * x) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * (0.5 * ga * x * x + gb * x) / fc * sin2(fc * y + fd));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp / fa * (ga / fa * (cos2(fa * x + fb) + (fa * x + fb) * sin2(fa * x + fb)) - ga / fa * fb * sin2(fa * x + fb) + gb * sin2(fa * x + fb)) / fc * sin2(fc * y + fd));
            return value;
        }
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

//    private static double _ps_cc_x(double x, double y, DCosCosFunction f, DLinearFunction g) {
//        if (f.a == 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos2(fb) * Math.cos2(fd) * g.b * x * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos2(fd) * g.b / f.a * Math.sin2(f.a * x + fb) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a == 0)
//            return f.amp * Math.cos2(fb) * g.b * x / f.c * Math.sin2(f.c * y + fd);
//
//        else if (f.a != 0 && f.c != 0 && g.a == 0)
//            return f.amp * g.b / f.a * Math.sin2(f.a * x + fb) / f.c * Math.sin2(f.c * y + fd);
//
//        else if (f.a == 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos2(fb) * Math.cos2(fd) * (0.5 * g.a * x * x + g.b * x) * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos2(fd) / f.a * (g.a / f.a * (Math.cos2(f.a * x + fb) + (f.a * x + fb) * Math.sin2(f.a * x + fb)) - g.a / f.a * fb * Math.sin2(f.a * x + fb) + g.b * Math.sin2(f.a * x + fb)) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a != 0)
//            return f.amp * Math.cos2(fb) * (0.5 * g.a * x * x + g.b * x) / f.c * Math.sin2(f.c * y + fd);
//
//        else //%none is null
//            return f.amp / f.a * (g.a / f.a * (Math.cos2(f.a * x + fb) + (f.a * x + fb) * Math.sin2(f.a * x + fb)) - g.a / f.a * fb * Math.sin2(f.a * x + fb) + g.b * Math.sin2(f.a * x + fb)) / f.c * Math.sin2(f.c * y + fd);
//    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
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
//ENDING---------------------------------------

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

        double famp = f.getAmp();
        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();

        if (fa != 0 && fc != 0 && ga != 0 && gb != 0) {
            //       t0 = f.amp*(g.a*cos2(f.a*x+fb)*sin2(f.c*y+fd)*f.c+g.a*sin2(f.a*x+fb)*x*sin2(f.c*y+fd)*f.a*f.c+g.b*sin2(f.a*x+fb)*f.a*cos2(f.c*y+fd)+g.b*sin2(f.a*x+fb)*f.a*sin2(f.c*y+fd)*f.c*y+gc*sin2(f.a*x+fb)*sin2(f.c*y+fd)*f.a*f.c)/(f.a*f.a)/(f.c*f.c);
            double t0;
            t0 = famp * (ga * cos2(fa * b1 + fb) * sin2(fc * b3 + fd) * fc + ga * sin2(fa * b1 + fb) * b1 * sin2(fc * b3 + fd) * fa * fc + gb * sin2(fa * b1 + fb) * fa * cos2(fc * b3 + fd) + gb * sin2(fa * b1 + fb) * fa * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fa * b1 + fb) * sin2(fc * b3 + fd) * fa * fc) / (fa * fa) / (fc * fc);
            value = t0;
            t0 = famp * (ga * cos2(fa * b2 + fb) * sin2(fc * b4 + fd) * fc + ga * sin2(fa * b2 + fb) * b2 * sin2(fc * b4 + fd) * fa * fc + gb * sin2(fa * b2 + fb) * fa * cos2(fc * b4 + fd) + gb * sin2(fa * b2 + fb) * fa * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fa * b2 + fb) * sin2(fc * b4 + fd) * fa * fc) / (fa * fa) / (fc * fc);
            value += t0;
            t0 = famp * (ga * cos2(fa * b1 + fb) * sin2(fc * b4 + fd) * fc + ga * sin2(fa * b1 + fb) * b1 * sin2(fc * b4 + fd) * fa * fc + gb * sin2(fa * b1 + fb) * fa * cos2(fc * b4 + fd) + gb * sin2(fa * b1 + fb) * fa * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fa * b1 + fb) * sin2(fc * b4 + fd) * fa * fc) / (fa * fa) / (fc * fc);
            value -= t0;
            t0 = famp * (ga * cos2(fa * b2 + fb) * sin2(fc * b3 + fd) * fc + ga * sin2(fa * b2 + fb) * b2 * sin2(fc * b3 + fd) * fa * fc + gb * sin2(fa * b2 + fb) * fa * cos2(fc * b3 + fd) + gb * sin2(fa * b2 + fb) * fa * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fa * b2 + fb) * sin2(fc * b3 + fd) * fa * fc) / (fa * fa) / (fc * fc);
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gb != 0) {
            //       t0 = f.amp*cos2(fb)*x*(g.a*x*sin2(f.c*y+fd)*f.c+2.0*g.b*cos2(f.c*y+fd)+2.0*g.b*sin2(f.c*y+fd)*f.c*y+2.0*gc*sin2(f.c*y+fd)*f.c)/(f.c*f.c)/2.0;
            double t0;
            t0 = famp * cos2(fb) * b1 * (ga * b1 * sin2(fc * b3 + fd) * fc + 2.0 * gb * cos2(fc * b3 + fd) + 2.0 * gb * sin2(fc * b3 + fd) * fc * b3 + 2.0 * gc * sin2(fc * b3 + fd) * fc) / (fc * fc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * b2 * (ga * b2 * sin2(fc * b4 + fd) * fc + 2.0 * gb * cos2(fc * b4 + fd) + 2.0 * gb * sin2(fc * b4 + fd) * fc * b4 + 2.0 * gc * sin2(fc * b4 + fd) * fc) / (fc * fc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * b1 * (ga * b1 * sin2(fc * b4 + fd) * fc + 2.0 * gb * cos2(fc * b4 + fd) + 2.0 * gb * sin2(fc * b4 + fd) * fc * b4 + 2.0 * gc * sin2(fc * b4 + fd) * fc) / (fc * fc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * b2 * (ga * b2 * sin2(fc * b3 + fd) * fc + 2.0 * gb * cos2(fc * b3 + fd) + 2.0 * gb * sin2(fc * b3 + fd) * fc * b3 + 2.0 * gc * sin2(fc * b3 + fd) * fc) / (fc * fc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gb != 0) {
            //       t0 = f.amp*cos2(fd)*y*(2.0*g.a*cos2(f.a*x+fb)+2.0*g.a*sin2(f.a*x+fb)*f.a*x+g.b*y*sin2(f.a*x+fb)*f.a+2.0*gc*sin2(f.a*x+fb)*f.a)/(f.a*f.a)/2.0;
            double t0;
            t0 = famp * cos2(fd) * b3 * (2.0 * ga * cos2(fa * b1 + fb) + 2.0 * ga * sin2(fa * b1 + fb) * fa * b1 + gb * b3 * sin2(fa * b1 + fb) * fa + 2.0 * gc * sin2(fa * b1 + fb) * fa) / (fa * fa) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * b4 * (2.0 * ga * cos2(fa * b2 + fb) + 2.0 * ga * sin2(fa * b2 + fb) * fa * b2 + gb * b4 * sin2(fa * b2 + fb) * fa + 2.0 * gc * sin2(fa * b2 + fb) * fa) / (fa * fa) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * b4 * (2.0 * ga * cos2(fa * b1 + fb) + 2.0 * ga * sin2(fa * b1 + fb) * fa * b1 + gb * b4 * sin2(fa * b1 + fb) * fa + 2.0 * gc * sin2(fa * b1 + fb) * fa) / (fa * fa) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * b3 * (2.0 * ga * cos2(fa * b2 + fb) + 2.0 * ga * sin2(fa * b2 + fb) * fa * b2 + gb * b3 * sin2(fa * b2 + fb) * fa + 2.0 * gc * sin2(fa * b2 + fb) * fa) / (fa * fa) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gb != 0) {
            //       t0 = f.amp*cos2(fb)*cos2(fd)*x*x*y*g.a/2.0+f.amp*cos2(fb)*cos2(fd)*x*y*y*g.b/2.0+f.amp*cos2(fb)*cos2(fd)*x*y*gc;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b1 * b3 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b3 * b3 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b3 * gc;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b2 * b4 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b4 * b4 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b4 * gc;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b1 * b4 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b4 * b4 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b4 * gc;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b2 * b3 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b3 * b3 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b3 * gc;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gb != 0) {
            //       t0 = f.amp*sin2(f.a*x+fb)*(g.b*cos2(f.c*y+fd)+g.b*sin2(f.c*y+fd)*f.c*y+gc*sin2(f.c*y+fd)*f.c)/f.a/(f.c*f.c);
            double t0;
            t0 = famp * sin2(fa * b1 + fb) * (gb * cos2(fc * b3 + fd) + gb * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fc * b3 + fd) * fc) / fa / (fc * fc);
            value = t0;
            t0 = famp * sin2(fa * b2 + fb) * (gb * cos2(fc * b4 + fd) + gb * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fc * b4 + fd) * fc) / fa / (fc * fc);
            value += t0;
            t0 = famp * sin2(fa * b1 + fb) * (gb * cos2(fc * b4 + fd) + gb * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fc * b4 + fd) * fc) / fa / (fc * fc);
            value -= t0;
            t0 = famp * sin2(fa * b2 + fb) * (gb * cos2(fc * b3 + fd) + gb * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fc * b3 + fd) * fc) / fa / (fc * fc);
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gb != 0) {
            //       t0 = f.amp*cos2(fb)*x*(g.b*cos2(f.c*y+fd)+g.b*sin2(f.c*y+fd)*f.c*y+gc*sin2(f.c*y+fd)*f.c)/(f.c*f.c);
            double t0;
            t0 = famp * cos2(fb) * b1 * (gb * cos2(fc * b3 + fd) + gb * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fc * b3 + fd) * fc) / (fc * fc);
            value = t0;
            t0 = famp * cos2(fb) * b2 * (gb * cos2(fc * b4 + fd) + gb * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fc * b4 + fd) * fc) / (fc * fc);
            value += t0;
            t0 = famp * cos2(fb) * b1 * (gb * cos2(fc * b4 + fd) + gb * sin2(fc * b4 + fd) * fc * b4 + gc * sin2(fc * b4 + fd) * fc) / (fc * fc);
            value -= t0;
            t0 = famp * cos2(fb) * b2 * (gb * cos2(fc * b3 + fd) + gb * sin2(fc * b3 + fd) * fc * b3 + gc * sin2(fc * b3 + fd) * fc) / (fc * fc);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gb != 0) {
            //       t0 = f.amp*cos2(fd)*sin2(f.a*x+fb)*y*(g.b*y+2.0*gc)/f.a/2.0;
            double t0;
            t0 = famp * cos2(fd) * sin2(fa * b1 + fb) * b3 * (gb * b3 + 2.0 * gc) / fa / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * sin2(fa * b2 + fb) * b4 * (gb * b4 + 2.0 * gc) / fa / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * sin2(fa * b1 + fb) * b4 * (gb * b4 + 2.0 * gc) / fa / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * sin2(fa * b2 + fb) * b3 * (gb * b3 + 2.0 * gc) / fa / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gb != 0) {
            //       t0 = f.amp*cos2(fb)*cos2(fd)*x*y*y*g.b/2.0+f.amp*cos2(fb)*cos2(fd)*x*y*gc;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b3 * b3 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b3 * gc;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b4 * b4 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b4 * gc;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b4 * b4 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b4 * gc;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b3 * b3 * gb / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b3 * gc;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*(g.a*cos2(f.a*x+fb)+g.a*sin2(f.a*x+fb)*f.a*x+gc*sin2(f.a*x+fb)*f.a)*sin2(f.c*y+fd)/(f.a*f.a)/f.c;
            double t0;
            t0 = famp * (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * sin2(fc * b3 + fd) / (fa * fa) / fc;
            value = t0;
            t0 = famp * (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * sin2(fc * b4 + fd) / (fa * fa) / fc;
            value += t0;
            t0 = famp * (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * sin2(fc * b4 + fd) / (fa * fa) / fc;
            value -= t0;
            t0 = famp * (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * sin2(fc * b3 + fd) / (fa * fa) / fc;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*cos2(fb)*x*(g.a*x+2.0*gc)*sin2(f.c*y+fd)/f.c/2.0;

//    double t0;
//          t0 = b1*(g.a*b1+2.0*gc)*sin2(f.c*b3+fd);
//    value  = t0;
//          t0 = b2*(g.a*b2+2.0*gc)*sin2(f.c*b4+fd);
//    value += t0;
//          t0 = b1*(g.a*b1+2.0*gc)*sin2(f.c*b4+fd);
//    value -= t0;
//          t0 = b2*(g.a*b2+2.0*gc)*sin2(f.c*b3+fd);
//    value -= t0;

            value = b1 * (ga * b1 + 2.0 * gc) * (sin2(fc * b3 + fd) - sin2(fc * b4 + fd))
                    + b2 * (ga * b2 + 2.0 * gc) * (sin2(fc * b4 + fd) - sin2(fc * b3 + fd))
            ;
            return famp * cos2(fb) * value / fc / 2.0;

        } else if (fa != 0 && fc == 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*cos2(fd)*(g.a*cos2(f.a*x+fb)+g.a*sin2(f.a*x+fb)*f.a*x+gc*sin2(f.a*x+fb)*f.a)*y/(f.a*f.a);
            double t0;
            t0 = (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * b3;
            value = t0;
            t0 = (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * b4;
            value += t0;
            t0 = (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * b4;
            value -= t0;
            t0 = (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * b3;
            value -= t0;
            return famp * cos2(fd) * value / (fa * fa);

        } else if (fa == 0 && fc == 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*cos2(fb)*cos2(fd)*x*x*y*g.a/2.0+f.amp*cos2(fb)*cos2(fd)*x*y*gc;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b1 * b3 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b3 * gc;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b2 * b4 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b4 * gc;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b1 * b4 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b1 * b4 * gc;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b2 * b3 * ga / 2.0 + famp * cos2(fb) * cos2(fd) * b2 * b3 * gc;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gb == 0) {
            //       t0 = f.amp*gc/f.a*sin2(f.a*x+fb)/f.c*sin2(f.c*y+fd);
            double t0;
            t0 = famp * gc / fa * sin2(fa * b1 + fb) / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * gc / fa * sin2(fa * b2 + fb) / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * gc / fa * sin2(fa * b1 + fb) / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * gc / fa * sin2(fa * b2 + fb) / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gb == 0) {
            //       t0 = f.amp*cos2(fb)*gc*x/f.c*sin2(f.c*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gc * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gc * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gc * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gc * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gb == 0) {
            //       t0 = f.amp*cos2(fd)*gc/f.a*sin2(f.a*x+fb)*y;
            double t0;
            t0 = famp * cos2(fd) * gc / fa * sin2(fa * b1 + fb) * b3;
            value = t0;
            t0 = famp * cos2(fd) * gc / fa * sin2(fa * b2 + fb) * b4;
            value += t0;
            t0 = famp * cos2(fd) * gc / fa * sin2(fa * b1 + fb) * b4;
            value -= t0;
            t0 = famp * cos2(fd) * gc / fa * sin2(fa * b2 + fb) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(fb)*cos2(fd)*x*y*gc;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b3 * gc;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b4 * gc;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * b1 * b4 * gc;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * b2 * b3 * gc;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}
