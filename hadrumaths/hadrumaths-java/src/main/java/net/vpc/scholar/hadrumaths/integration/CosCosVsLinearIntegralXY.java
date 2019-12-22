package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

import static net.vpc.scholar.hadrumaths.MathsBase.cos2;
import static net.vpc.scholar.hadrumaths.MathsBase.sin2;

/**
 * User: taha Date: 2 juil. 2003 Time: 15:15:16
 */
final class CosCosVsLinearIntegralXY {
    private static final long serialVersionUID = 1L;

    public double compute(Domain domain, double famp, double fa, double fb, double fc, double fd, double ga, double gb, double gc) {
        return primi_coslinear(domain, famp, fa, fb, fc, fd, ga, gb, gc);
    }

    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*(g_a*x+g_b*y+g_c)
    public static double primi_coslinear(Domain domain, double famp, double fa, double fb, double fc, double fd, double ga, double gb, double gc) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (fa != 0 && fc != 0 && ga != 0 && gb != 0) {
            //       t0 = f.amp*(g.a*cos2(f.a*x+f.b)*sin(f.c*y+f.d)*f.c+g.a*sin(f.a*x+f.b)*x*sin(f.c*y+f.d)*f.a*f.c+g.b*sin(f.a*x+f.b)*f.a*cos2(f.c*y+f.d)+g.b*sin(f.a*x+f.b)*f.a*sin(f.c*y+f.d)*f.c*y+gc*sin(f.a*x+f.b)*sin(f.c*y+f.d)*f.a*f.c)/(f.a*f.a)/(f.c*f.c);
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
            //       t0 = f.amp*cos2(f.b)*x*(g.a*x*sin(f.c*y+f.d)*f.c+2.0*g.b*cos2(f.c*y+f.d)+2.0*g.b*sin(f.c*y+f.d)*f.c*y+2.0*gc*sin(f.c*y+f.d)*f.c)/(f.c*f.c)/2.0;
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
            //       t0 = f.amp*cos2(f.d)*y*(2.0*g.a*cos2(f.a*x+f.b)+2.0*g.a*sin(f.a*x+f.b)*f.a*x+g.b*y*sin(f.a*x+f.b)*f.a+2.0*gc*sin(f.a*x+f.b)*f.a)/(f.a*f.a)/2.0;
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
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*x*y*g.a/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*y*g.b/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*gc;
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
            //       t0 = f.amp*sin(f.a*x+f.b)*(g.b*cos2(f.c*y+f.d)+g.b*sin(f.c*y+f.d)*f.c*y+gc*sin(f.c*y+f.d)*f.c)/f.a/(f.c*f.c);
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
            //       t0 = f.amp*cos2(f.b)*x*(g.b*cos2(f.c*y+f.d)+g.b*sin(f.c*y+f.d)*f.c*y+gc*sin(f.c*y+f.d)*f.c)/(f.c*f.c);
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
            //       t0 = f.amp*cos2(f.d)*sin(f.a*x+f.b)*y*(g.b*y+2.0*gc)/f.a/2.0;
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
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*y*y*g.b/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*gc;
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
            //       t0 = f.amp*(g.a*cos2(f.a*x+f.b)+g.a*sin(f.a*x+f.b)*f.a*x+gc*sin(f.a*x+f.b)*f.a)*sin(f.c*y+f.d)/(f.a*f.a)/f.c;
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
            //       t0 = f.amp*cos2(f.b)*x*(g.a*x+2.0*gc)*sin(f.c*y+f.d)/f.c/2.0;
            double t0;
            t0 = famp * cos2(fb) * b1 * (ga * b1 + 2.0 * gc) * sin2(fc * b3 + fd) / fc / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * b2 * (ga * b2 + 2.0 * gc) * sin2(fc * b4 + fd) / fc / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * b1 * (ga * b1 + 2.0 * gc) * sin2(fc * b4 + fd) / fc / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * b2 * (ga * b2 + 2.0 * gc) * sin2(fc * b3 + fd) / fc / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*cos2(f.d)*(g.a*cos2(f.a*x+f.b)+g.a*sin(f.a*x+f.b)*f.a*x+gc*sin(f.a*x+f.b)*f.a)*y/(f.a*f.a);
            double t0;
            t0 = famp * cos2(fd) * (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * b3 / (fa * fa);
            value = t0;
            t0 = famp * cos2(fd) * (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * b4 / (fa * fa);
            value += t0;
            t0 = famp * cos2(fd) * (ga * cos2(fa * b1 + fb) + ga * sin2(fa * b1 + fb) * fa * b1 + gc * sin2(fa * b1 + fb) * fa) * b4 / (fa * fa);
            value -= t0;
            t0 = famp * cos2(fd) * (ga * cos2(fa * b2 + fb) + ga * sin2(fa * b2 + fb) * fa * b2 + gc * sin2(fa * b2 + fb) * fa) * b3 / (fa * fa);
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gb == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*x*y*g.a/2.0+f.amp*cos2(f.b)*cos2(f.d)*x*y*gc;
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
            //       t0 = f.amp*gc/f.a*sin(f.a*x+f.b)/f.c*sin(f.c*y+f.d);
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
            //       t0 = f.amp*cos2(f.b)*gc*x/f.c*sin(f.c*y+f.d);
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
            //       t0 = f.amp*cos2(f.d)*gc/f.a*sin(f.a*x+f.b)*y;
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
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*x*y*gc;
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
        double fa = f.a;
        double fc = f.c;
        double ga = g.a;
        double fb = f.b;
        double fd = f.d;
        double famp = f.amp;
        double gb = g.b;

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
//ENDING---------------------------------------
}
