package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.Domain;

import static net.vpc.scholar.hadrumaths.Maths.cos2;
import static net.vpc.scholar.hadrumaths.Maths.sin2;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:13:30
 */
final class CosCosCosCosIntegralXY {
    private static final long serialVersionUID = 1L;
//    public double compute(DomainXY domain, DFunctionXY f1, DFunctionXY f2, FormalScalarProduct sp) {
//        DCosCosFunctionXY f1ok = (DCosCosFunctionXY) f1;
//        DCosCosFunctionXY f2ok = (DCosCosFunctionXY) f2;
//        double d=computeTolerant(domain, f1ok, f2ok, 1E-15);
////        double d = compute(domain, f1ok, f2ok);
////        if (Double.isNaN(d)) {
//////            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
////            //System.out.println("--**");
////            d = computeTolerant(domain, f1ok, f2ok, 1E-15);
////        }
//        if (Double.isNaN(d)) {
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            double d0 = compute(domain, f1ok, f2ok);
//        }
//        return d;
//    }
//    public double compute(Domain domain, DFunction f1, DFunction f2) {
//        return new CosCosVsCosCosScalarProductOld2().compute(domain, f1, f2);
//    }

    private static double compute(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        if (f_a == g_a && f_c == g_c) {
            return primi_cos4_fa_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_a == g_a && f_c == -g_c) {
            return primi_cos4_fa_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_a == -g_a && f_c == g_c) {
            return primi_cos4_af_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_a == -g_a && f_c == -g_c) {
            return primi_cos4_af_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_a == g_a) {
            return primi_cos4_fa_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_a == -g_a) {
            return primi_cos4_af_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_c == g_c) {
            return primi_cos4_fga_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (f_c == -g_c) {
            return primi_cos4_fga_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else {
            return primi_cos4_fga_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        }
    }


    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        return
                diff == 0 ||
                        (diff != 0 ? (Math.abs((diff) / a) < tolerance) : (Math.abs((diff) / b) < tolerance))
                ;
    }

    private static double computeTolerant(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d, double tolerance) {
        if (tolerantEqual(f_a, g_a, tolerance) && tolerantEqual(f_c, g_c, tolerance)) {
            return primi_cos4_fa_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_a, g_a, tolerance) && tolerantEqual(f_c, -g_c, tolerance)) {
            return primi_cos4_fa_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_a, -g_a, tolerance) && tolerantEqual(f_c, g_c, tolerance)) {
            return primi_cos4_af_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_a, -g_a, tolerance) && tolerantEqual(f_c, -g_c, tolerance)) {
            return primi_cos4_af_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_a, g_a, tolerance)) {
            return primi_cos4_fa_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_a, -g_a, tolerance)) {
            return primi_cos4_af_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_c, g_c, tolerance)) {
            return primi_cos4_fga_fc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else if (tolerantEqual(f_c, -g_c, tolerance)) {
            return primi_cos4_fga_cf(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        } else {
            return primi_cos4_fga_fgc(domain, f_amp, f_a, f_b, f_c, f_d, g_amp, g_a, g_b, g_c, g_d);
        }
    }

    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(g_a*x+g_b)*cos(g_c*y+g_d)
    public static double primi_cos4_fga_fgc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       s1 = g_amp/4.0;      s3 = f_amp;      s6 = sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c;      s5 = s6+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c+sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c;      s6 = 1/(f_a*f_a*f_c*f_c-f_a*f_a*g_c*g_c-g_a*g_a*f_c*f_c+g_a*g_a*g_c*g_c);      s4 = s5*s6;      s2 = s3*s4;      t0 = s1*s2;
            double t0;
            double s6;
            double s5;
            double s4;
            double s3;
            double s2;
            double s1;
            s1 = g_amp / 4.0;
            s3 = f_amp;
            s6 = sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c;
            s5 = s6 + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c;
            s6 = 1 / (f_a * f_a * f_c * f_c - f_a * f_a * g_c * g_c - g_a * g_a * f_c * f_c + g_a * g_a * g_c * g_c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value = t0;
            s1 = g_amp / 4.0;
            s3 = f_amp;
            s6 = sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c;
            s5 = s6 + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c;
            s6 = 1 / (f_a * f_a * f_c * f_c - f_a * f_a * g_c * g_c - g_a * g_a * f_c * f_c + g_a * g_a * g_c * g_c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value += t0;
            s1 = g_amp / 4.0;
            s3 = f_amp;
            s6 = sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c;
            s5 = s6 + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c;
            s6 = 1 / (f_a * f_a * f_c * f_c - f_a * f_a * g_c * g_c - g_a * g_a * f_c * f_c + g_a * g_a * g_c * g_c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            s1 = g_amp / 4.0;
            s3 = f_amp;
            s6 = sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c;
            s5 = s6 + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c;
            s6 = 1 / (f_a * f_a * f_c * f_c - f_a * f_a * g_c * g_c - g_a * g_a * f_c * f_c + g_a * g_a * g_c * g_c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*sin2(g_a*x+g_b)*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/g_a/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / g_a / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / g_a / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / g_a / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / g_a / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*sin2(g_c*y+g_d)/g_c/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * sin2(g_c * b3 + g_d) / g_c / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * sin2(g_c * b4 + g_d) / g_c / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * sin2(g_c * b4 + g_d) / g_c / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * sin2(g_c * b3 + g_d) / g_c / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp/g_a*sin2(g_a*x+g_b)/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp / g_a * sin2(g_a * b1 + g_b) / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp / g_a * sin2(g_a * b2 + g_b) / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp / g_a * sin2(g_a * b1 + g_b) / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp / g_a * sin2(g_a * b2 + g_b) / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*cos(g_b)*sin2(f_a*x+f_b)*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/f_a/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_b)/f_a*sin2(f_a*x+f_b)/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) / f_a * sin2(f_a * b1 + f_b) / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) / f_a * sin2(f_a * b2 + f_b) / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) / f_a * sin2(f_a * b1 + f_b) / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) / f_a * sin2(f_a * b2 + f_b) / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*x/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_d)*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*sin2(f_c*y+f_d)/f_c/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * sin2(f_c * b3 + f_d) / f_c / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * sin2(f_c * b4 + f_d) / f_c / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * sin2(f_c * b4 + f_d) / f_c / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * sin2(f_c * b3 + f_d) / f_c / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_d)/g_a*sin2(g_a*x+g_b)/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*y/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_d)/g_a*sin2(g_a*x+g_b)*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_b)*cos(g_d)/f_a*sin2(f_a*x+f_b)/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*cos(g_d)*x/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_b)*cos(g_d)/f_a*sin2(f_a*x+f_b)*y;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x+g_b)*cos(g_c*y+g_d)
    public static double primi_cos4_fa_fgc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = g_amp*f_amp*(2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/f_a/(f_c*f_c-g_c*g_c)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*sin2(g_c*y+g_d)/f_a/g_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*x/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = g_amp*f_amp*(2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-2.0*cos(f_b-g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(2.0*f_a*x+f_b+g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/f_a/(f_c*f_c-g_c*g_c)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*sin2(g_c*y+g_d)/f_a/g_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*x/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*sin2(f_c*y+f_d)/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*cos(g_d)*x/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*sin2(f_c*y+f_d)/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*cos(g_d)*x/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(g_a*x+g_b)*cos(f_c*y+g_d)
    public static double primi_cos4_fga_fc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = g_amp*f_amp*(2.0*sin2(f_a*x-g_a*x+f_b-g_b)*f_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(f_a*x-g_a*x+f_b-g_b)*g_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(f_a*x+g_a*x+f_b+g_b)*f_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(2.0*f_c*y+f_d+g_d)-2.0*sin2(f_a*x+g_a*x+f_b+g_b)*g_a*cos(f_d-g_d)*y*f_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(2.0*f_c*y+f_d+g_d))/f_c/(f_a*f_a-g_a*g_a)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d - g_d) * b3 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d - g_d) * b4 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d - g_d) * b4 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d - g_d) * b3 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*sin2(g_a*x+g_b)*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/g_a/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / g_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / g_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / g_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / g_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*y/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_d)/g_a*sin2(g_a*x+g_b)*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*cos(g_b)*sin2(f_a*x+f_b)*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_b)*cos(g_d)/f_a*sin2(f_a*x+f_b)*y;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b3;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = g_amp*f_amp*(2.0*sin2(f_a*x-g_a*x+f_b-g_b)*f_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(f_a*x-g_a*x+f_b-g_b)*g_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(f_a*x+g_a*x+f_b+g_b)*f_a*cos(f_d-g_d)*y*f_c+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(2.0*f_c*y+f_d+g_d)-2.0*sin2(f_a*x+g_a*x+f_b+g_b)*g_a*cos(f_d-g_d)*y*f_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(2.0*f_c*y+f_d+g_d))/f_c/(f_a*f_a-g_a*g_a)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d - g_d) * b3 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d - g_d) * b4 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d - g_d) * b4 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d - g_d) * b4 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d - g_d) * b3 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d - g_d) * b3 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*sin2(g_a*x+g_b)*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/g_a/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / g_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / g_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / g_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / g_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*y/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_d)/g_a*sin2(g_a*x+g_b)*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_b)*sin2(f_a*x+f_b)*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_b)*cos(g_d)/f_a*sin2(f_a*x+f_b)*y;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x+g_b)*cos(f_c*y+g_d)
    public static double primi_cos4_fa_fc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x+g_b)*cos(f_c*y-g_d)
    public static double primi_cos4_fa_cf(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {

        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b-g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b-g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b+g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b+g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b - g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b + g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b + g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b - g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b - g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b + g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b + g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b-g_b)*x*f_a+sin2(2.0*f_a*x+f_b+g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b + g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b - g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b + g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x-g_b)*cos(g_c*y+g_d)
    public static double primi_cos4_af_fgc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = g_amp*f_amp*(2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/f_a/(f_c*f_c-g_c*g_c)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*sin2(g_c*y+g_d)/f_a/g_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*x/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = g_amp*f_amp*(2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-2.0*cos(f_b+g_b)*x*f_a*sin2(f_c*y+g_c*y+f_d+g_d)*g_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(2.0*f_a*x+f_b-g_b)*sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/f_a/(f_c*f_c-g_c*g_c)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / f_a / (f_c * f_c - g_c * g_c) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(sin2(f_c*y-g_c*y+f_d-g_d)*f_c+sin2(f_c*y-g_c*y+f_d-g_d)*g_c+sin2(f_c*y+g_c*y+f_d+g_d)*f_c-sin2(f_c*y+g_c*y+f_d+g_d)*g_c)/(f_c*f_c-g_c*g_c)/2.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (sin2(f_c * b4 - g_c * b4 + f_d - g_d) * f_c + sin2(f_c * b4 - g_c * b4 + f_d - g_d) * g_c + sin2(f_c * b4 + g_c * b4 + f_d + g_d) * f_c - sin2(f_c * b4 + g_c * b4 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (sin2(f_c * b3 - g_c * b3 + f_d - g_d) * f_c + sin2(f_c * b3 - g_c * b3 + f_d - g_d) * g_c + sin2(f_c * b3 + g_c * b3 + f_d + g_d) * f_c - sin2(f_c * b3 + g_c * b3 + f_d + g_d) * g_c) / (f_c * f_c - g_c * g_c) / 2.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*sin2(g_c*y+g_d)/f_a/g_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(g_c * b4 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(g_c * b3 + g_d) / f_a / g_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*x/g_c*sin2(g_c*y+g_d);
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b3 + g_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b4 + g_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b1 / g_c * sin2(g_c * b4 + g_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * b2 / g_c * sin2(g_c * b3 + g_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*sin2(f_c*y+f_d)/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*cos(g_d)*x/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*sin2(f_c*y+f_d)/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * sin2(f_c * b4 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * sin2(f_c * b3 + f_d) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*cos(g_d)*x/f_c*sin2(f_c*y+f_d);
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b3 + f_d);
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b4 + f_d);
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b1 / f_c * sin2(f_c * b4 + f_d);
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * cos2(g_d) * b2 / f_c * sin2(f_c * b3 + f_d);
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x-g_b)*cos(f_c*y+g_d)
    public static double primi_cos4_af_fc(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d-g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d+g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d-g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d+g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d - g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d - g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d + g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d + g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d-g_d)*y*f_c+sin2(2.0*f_c*y+f_d+g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d - g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d - g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d + g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(f_a*x-g_b)*cos(f_c*y-g_d)
    public static double primi_cos4_af_cf(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a == 0 && g_c != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*g_amp*(4.0*cos(f_b+g_b)*x*f_a*cos(f_d+g_d)*y*f_c+2.0*cos(f_b+g_b)*x*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(2.0*f_a*x+f_b-g_b)*cos(f_d+g_d)*y*f_c+sin2(2.0*f_a*x+f_b-g_b)*sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/16.0;
            double t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value = t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value += t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b1 * f_a * cos2(f_d + g_d) * b4 * f_c + 2.0 * cos2(f_b + g_b) * b1 * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b1 + f_b - g_b) * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_a * b1 + f_b - g_b) * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            t0 = f_amp * g_amp * (4.0 * cos2(f_b + g_b) * b2 * f_a * cos2(f_d + g_d) * b3 * f_c + 2.0 * cos2(f_b + g_b) * b2 * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(2.0 * f_a * b2 + f_b - g_b) * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_a * b2 + f_b - g_b) * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 16.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0 && g_c == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(2.0*cos(f_b+g_b)*x*f_a+sin2(2.0*f_a*x+f_b-g_b))*y/f_a/4.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b3 / f_a / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b4 / f_a / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b1 * f_a + sin2(2.0 * f_a * b1 + f_b - g_b)) * b4 / f_a / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (2.0 * cos2(f_b + g_b) * b2 * f_a + sin2(2.0 * f_a * b2 + f_b - g_b)) * b3 / f_a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos(f_a*x+f_b)*cos(f_c*y+f_d)*g_amp*cos(g_a*x+g_b)*cos(f_c*y-g_d)
    public static double primi_cos4_fga_cf(Domain domain, double f_amp, double f_a, double f_b, double f_c, double f_d, double g_amp, double g_a, double g_b, double g_c, double g_d) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f_a != 0 && f_c != 0 && g_a != 0) {
            //       t0 = g_amp*f_amp*(2.0*sin2(f_a*x-g_a*x+f_b-g_b)*f_a*cos(f_d+g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*f_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(f_a*x-g_a*x+f_b-g_b)*g_a*cos(f_d+g_d)*y*f_c+sin2(f_a*x-g_a*x+f_b-g_b)*g_a*sin2(2.0*f_c*y+f_d-g_d)+2.0*sin2(f_a*x+g_a*x+f_b+g_b)*f_a*cos(f_d+g_d)*y*f_c+sin2(f_a*x+g_a*x+f_b+g_b)*f_a*sin2(2.0*f_c*y+f_d-g_d)-2.0*sin2(f_a*x+g_a*x+f_b+g_b)*g_a*cos(f_d+g_d)*y*f_c-sin2(f_a*x+g_a*x+f_b+g_b)*g_a*sin2(2.0*f_c*y+f_d-g_d))/f_c/(f_a*f_a-g_a*g_a)/8.0;
            double t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d + g_d) * b3 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value = t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d + g_d) * b4 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value += t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a * sin2(2.0 * f_c * b4 + f_d - g_d) + 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * cos2(f_d + g_d) * b4 * f_c + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a * sin2(2.0 * f_c * b4 + f_d - g_d) - 2.0 * sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * cos2(f_d + g_d) * b4 * f_c - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a * sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            t0 = g_amp * f_amp * (2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a * sin2(2.0 * f_c * b3 + f_d - g_d) + 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * cos2(f_d + g_d) * b3 * f_c + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a * sin2(2.0 * f_c * b3 + f_d - g_d) - 2.0 * sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * cos2(f_d + g_d) * b3 * f_c - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a * sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / (f_a * f_a - g_a * g_a) / 8.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a != 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*sin2(g_a*x+g_b)*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/g_a/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / g_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / g_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b1 + g_b) * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / g_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * sin2(g_a * b2 + g_b) * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / g_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a != 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_d)*(sin2(f_a*x-g_a*x+f_b-g_b)*f_a+sin2(f_a*x-g_a*x+f_b-g_b)*g_a+sin2(f_a*x+g_a*x+f_b+g_b)*f_a-sin2(f_a*x+g_a*x+f_b+g_b)*g_a)*y/(f_a*f_a-g_a*g_a)/2.0;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b1 - g_a * b1 + f_b - g_b) * f_a + sin2(f_a * b1 - g_a * b1 + f_b - g_b) * g_a + sin2(f_a * b1 + g_a * b1 + f_b + g_b) * f_a - sin2(f_a * b1 + g_a * b1 + f_b + g_b) * g_a) * b4 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_d) * (sin2(f_a * b2 - g_a * b2 + f_b - g_b) * f_a + sin2(f_a * b2 - g_a * b2 + f_b - g_b) * g_a + sin2(f_a * b2 + g_a * b2 + f_b + g_b) * f_a - sin2(f_a * b2 + g_a * b2 + f_b + g_b) * g_a) * b3 / (f_a * f_a - g_a * g_a) / 2.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c == 0 && g_a != 0) {
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_d)/g_a*sin2(g_a*x+g_b)*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b1 + g_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_d) / g_a * sin2(g_a * b2 + g_b) * b3;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c != 0 && g_a == 0) {
            //       t0 = f_amp*g_amp*cos(g_b)*sin2(f_a*x+f_b)*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_a/f_c/4.0;
            double t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 4.0;
            value = t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 4.0;
            value += t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b1 + f_b) * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_a / f_c / 4.0;
            value -= t0;
            t0 = f_amp * g_amp * cos2(g_b) * sin2(f_a * b2 + f_b) * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_a / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a == 0 && f_c != 0 && g_a == 0) {
            //       t0 = f_amp*cos(f_b)*g_amp*cos(g_b)*x*(2.0*cos(f_d+g_d)*y*f_c+sin2(2.0*f_c*y+f_d-g_d))/f_c/4.0;
            double t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value = t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value += t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b1 * (2.0 * cos2(f_d + g_d) * b4 * f_c + sin2(2.0 * f_c * b4 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            t0 = f_amp * cos2(f_b) * g_amp * cos2(g_b) * b2 * (2.0 * cos2(f_d + g_d) * b3 * f_c + sin2(2.0 * f_c * b3 + f_d - g_d)) / f_c / 4.0;
            value -= t0;
            return value;

        } else if (f_a != 0 && f_c == 0 && g_a == 0) {
            //       t0 = f_amp*cos(f_d)*g_amp*cos(g_b)*cos(g_d)/f_a*sin2(f_a*x+f_b)*y;
            double t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b3;
            value = t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b4;
            value += t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b1 + f_b) * b4;
            value -= t0;
            t0 = f_amp * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) / f_a * sin2(f_a * b2 + f_b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f_amp*cos(f_b)*cos(f_d)*g_amp*cos(g_b)*cos(g_d)*x*y;
            double t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b3;
            value = t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b4;
            value += t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b1 * b4;
            value -= t0;
            t0 = f_amp * cos2(f_b) * cos2(f_d) * g_amp * cos2(g_b) * cos2(g_d) * b2 * b3;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}