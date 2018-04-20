package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import static java.lang.Math.abs;
import static net.vpc.scholar.hadrumaths.Maths.cos2;
import static net.vpc.scholar.hadrumaths.Maths.sin2;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:13:30
 */
final class CosCosVsCosCosScalarProduct implements FormalScalarProductHelper {

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        CosXCosY f1ok = (CosXCosY) f1;
        CosXCosY f2ok = (CosXCosY) f2;
        double d=computeTolerant(domain, f1ok, f2ok, 1E-15);
//        double d = compute(domain, f1ok, f2ok);
//        if (Double.isNaN(d)) {
////            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            //System.out.println("--**");
//            d = computeTolerant(domain, f1ok, f2ok, 1E-15);
//        }
        if (Double.isNaN(d)) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            double d0 = compute(domain, f1ok, f2ok);
            d=d0;
        }
        return d;
    }
//    public double compute(Domain domain, DFunction f1, DFunction f2) {
//        return new CosCosVsCosCosScalarProductOld2().compute(domain, f1, f2);
//    }


    static double compute(Domain domain, CosXCosY f, CosXCosY g) {
        if (f.a == g.a && f.c == g.c) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (f.a == g.a && f.c == -g.c) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (f.a == -g.a && f.c == g.c) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (f.a == -g.a && f.c == -g.c) {
            return primi_cos4_af_cf(domain, f, g);
        } else if (f.a == g.a) {
            return primi_cos4_fa_fgc(domain, f, g);
        } else if (f.a == -g.a) {
            return primi_cos4_af_fgc(domain, f, g);
        } else if (f.c == g.c) {
            return primi_cos4_fga_fc(domain, f, g);
        } else if (f.c == -g.c) {
            return primi_cos4_fga_cf(domain, f, g);
        } else {
            return primi_cos4_fga_fgc(domain, f, g);
        }
    }


    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        if(diff==0)return true;
        if(diff<0){
            diff=-diff;
        }
        if(a<0){
            a=-a;
        }
        if(b<0){
            b=-b;
        }
        double c=a<b?b:a;

        if(c==0){
            return diff < tolerance;
        }
        return diff/c < tolerance;
//        return
//                diff == 0 ||
////                (diff != 0 ? (absdbl((diff) / a) < tolerance) : (absdbl((diff) / b) < tolerance))
//                (diff <0 ? (abs((diff) / a) < tolerance) : (abs((diff) / b) < tolerance))
//                ;
    }

    private static double computeTolerant(Domain domain, CosXCosY f, CosXCosY g, double tolerance) {
        if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
            return primi_cos4_af_cf(domain, f, g);
        } else if (tolerantEqual(f.a, g.a, tolerance)) {
            return primi_cos4_fa_fgc(domain, f, g);
        } else if (tolerantEqual(f.a, -g.a, tolerance)) {
            return primi_cos4_af_fgc(domain, f, g);
        } else if (tolerantEqual(f.c, g.c, tolerance)) {
            return primi_cos4_fga_fc(domain, f, g);
        } else if (tolerantEqual(f.c, -g.c, tolerance)) {
            return primi_cos4_fga_cf(domain, f, g);
        } else {
            return primi_cos4_fga_fgc(domain, f, g);
        }
    }

//STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(g_c*y+g_d)
    public static double primi_cos4_fga_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       s1 = g.amp/4.0;      s3 = f.amp;      s6 = sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c;      s5 = s6+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c+sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c;      s6 = 1/(f.a*f.a*f.c*f.c-f.a*f.a*g.c*g.c-g.a*g.a*f.c*f.c+g.a*g.a*g.c*g.c);      s4 = s5*s6;      s2 = s3*s4;      t0 = s1*s2;
            double t0;
            double s6;
            double s5;
            double s4;
            double s3;
            double s2;
            double s1;
            s1 = g.amp / 4.0;
            s3 = f.amp;
            s6 = sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c;
            s5 = s6 + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c;
            s6 = 1 / (f.a * f.a * f.c * f.c - f.a * f.a * g.c * g.c - g.a * g.a * f.c * f.c + g.a * g.a * g.c * g.c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value = t0;
            s1 = g.amp / 4.0;
            s3 = f.amp;
            s6 = sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c;
            s5 = s6 + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c;
            s6 = 1 / (f.a * f.a * f.c * f.c - f.a * f.a * g.c * g.c - g.a * g.a * f.c * f.c + g.a * g.a * g.c * g.c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value += t0;
            s1 = g.amp / 4.0;
            s3 = f.amp;
            s6 = sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c;
            s5 = s6 + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c;
            s6 = 1 / (f.a * f.a * f.c * f.c - f.a * f.a * g.c * g.c - g.a * g.a * f.c * f.c + g.a * g.a * g.c * g.c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            s1 = g.amp / 4.0;
            s3 = f.amp;
            s6 = sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c;
            s5 = s6 + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c;
            s6 = 1 / (f.a * f.a * f.c * f.c - f.a * f.a * g.c * g.c - g.a * g.a * f.c * f.c + g.a * g.a * g.c * g.c);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*sin2(g.a*x+g.b)*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/g.a/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / g.a / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / g.a / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / g.a / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / g.a / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*sin2(g.c*y+g.d)/g.c/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * sin2(g.c * b3 + g.d) / g.c / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * sin2(g.c * b4 + g.d) / g.c / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * sin2(g.c * b4 + g.d) / g.c / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * sin2(g.c * b3 + g.d) / g.c / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp/g.a*sin2(g.a*x+g.b)/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * b1 + g.b) / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * b2 + g.b) / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * b1 + g.b) / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * b2 + g.b) / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*cos2(g.b)*sin2(f.a*x+f.b)*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/f.a/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.b)/f.a*sin2(f.a*x+f.b)/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * b1 + f.b) / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * b2 + f.b) / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * b1 + f.b) / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * b2 + f.b) / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*x/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.d)*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*sin2(f.c*y+f.d)/f.c/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * sin2(f.c * b3 + f.d) / f.c / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * sin2(f.c * b4 + f.d) / f.c / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * sin2(f.c * b4 + f.d) / f.c / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * sin2(f.c * b3 + f.d) / f.c / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*y/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*cos2(g.d)*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(g_c*y+g_d)
    public static double primi_cos4_fa_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = g.amp*f.amp*(2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/f.a/(f.c*f.c-g.c*g.c)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*sin2(g.c*y+g.d)/f.a/g.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*x/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = g.amp*f.amp*(2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-2.0*cos2(f.b-g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(2.0*f.a*x+f.b+g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/f.a/(f.c*f.c-g.c*g.c)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*sin2(g.c*y+g.d)/f.a/g.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*x/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*sin2(f.c*y+f.d)/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*cos2(g.d)*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*sin2(f.c*y+f.d)/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*cos2(g.d)*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(f_c*y+g_d)
    public static double primi_cos4_fga_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = g.amp*f.amp*(2.0*sin2(f.a*x-g.a*x+f.b-g.b)*f.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(f.a*x-g.a*x+f.b-g.b)*g.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(f.a*x+g.a*x+f.b+g.b)*f.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(2.0*f.c*y+f.d+g.d)-2.0*sin2(f.a*x+g.a*x+f.b+g.b)*g.a*cos2(f.d-g.d)*y*f.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(2.0*f.c*y+f.d+g.d))/f.c/(f.a*f.a-g.a*g.a)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d - g.d) * b3 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d - g.d) * b4 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d - g.d) * b4 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d - g.d) * b3 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*sin2(g.a*x+g.b)*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/g.a/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / g.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / g.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / g.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / g.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*y/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*cos2(g.b)*sin2(f.a*x+f.b)*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b3;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = g.amp*f.amp*(2.0*sin2(f.a*x-g.a*x+f.b-g.b)*f.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(f.a*x-g.a*x+f.b-g.b)*g.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(f.a*x+g.a*x+f.b+g.b)*f.a*cos2(f.d-g.d)*y*f.c+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(2.0*f.c*y+f.d+g.d)-2.0*sin2(f.a*x+g.a*x+f.b+g.b)*g.a*cos2(f.d-g.d)*y*f.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(2.0*f.c*y+f.d+g.d))/f.c/(f.a*f.a-g.a*g.a)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d - g.d) * b3 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d - g.d) * b4 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d - g.d) * b4 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d - g.d) * b4 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d - g.d) * b3 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d - g.d) * b3 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*sin2(g.a*x+g.b)*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/g.a/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / g.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / g.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / g.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / g.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*y/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.b)*sin2(f.a*x+f.b)*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(f_c*y+g_d)
    public static double primi_cos4_fa_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(f_c*y-g_d)
    public static double primi_cos4_fa_cf(Domain domain, CosXCosY f, CosXCosY g) {

        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b-g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b-g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b+g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b+g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b - g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b + g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b + g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b - g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b - g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b + g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b + g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b-g.b)*x*f.a+sin2(2.0*f.a*x+f.b+g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b + g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b - g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b + g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(g_c*y+g_d)
    public static double primi_cos4_af_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = g.amp*f.amp*(2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/f.a/(f.c*f.c-g.c*g.c)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*sin2(g.c*y+g.d)/f.a/g.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*x/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = g.amp*f.amp*(2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-2.0*cos2(f.b+g.b)*x*f.a*sin2(f.c*y+g.c*y+f.d+g.d)*g.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(2.0*f.a*x+f.b-g.b)*sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/f.a/(f.c*f.c-g.c*g.c)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / f.a / (f.c * f.c - g.c * g.c) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(sin2(f.c*y-g.c*y+f.d-g.d)*f.c+sin2(f.c*y-g.c*y+f.d-g.d)*g.c+sin2(f.c*y+g.c*y+f.d+g.d)*f.c-sin2(f.c*y+g.c*y+f.d+g.d)*g.c)/(f.c*f.c-g.c*g.c)/2.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (sin2(f.c * b4 - g.c * b4 + f.d - g.d) * f.c + sin2(f.c * b4 - g.c * b4 + f.d - g.d) * g.c + sin2(f.c * b4 + g.c * b4 + f.d + g.d) * f.c - sin2(f.c * b4 + g.c * b4 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (sin2(f.c * b3 - g.c * b3 + f.d - g.d) * f.c + sin2(f.c * b3 - g.c * b3 + f.d - g.d) * g.c + sin2(f.c * b3 + g.c * b3 + f.d + g.d) * f.c - sin2(f.c * b3 + g.c * b3 + f.d + g.d) * g.c) / (f.c * f.c - g.c * g.c) / 2.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*sin2(g.c*y+g.d)/f.a/g.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(g.c * b4 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(g.c * b3 + g.d) / f.a / g.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*x/g.c*sin2(g.c*y+g.d);
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b3 + g.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b4 + g.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b1 / g.c * sin2(g.c * b4 + g.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * b2 / g.c * sin2(g.c * b3 + g.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*sin2(f.c*y+f.d)/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*cos2(g.d)*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*sin2(f.c*y+f.d)/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * sin2(f.c * b4 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * sin2(f.c * b3 + f.d) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*cos2(g.d)*x/f.c*sin2(f.c*y+f.d);
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b3 + f.d);
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b4 + f.d);
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b1 / f.c * sin2(f.c * b4 + f.d);
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * b2 / f.c * sin2(f.c * b3 + f.d);
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(f_c*y+g_d)
    public static double primi_cos4_af_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d-g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d+g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d+g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d - g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d - g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d + g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d + g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d-g.d)*y*f.c+sin2(2.0*f.c*y+f.d+g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d - g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d - g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d + g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(f_c*y-g_d)
    public static double primi_cos4_af_cf(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*g.amp*(4.0*cos2(f.b+g.b)*x*f.a*cos2(f.d+g.d)*y*f.c+2.0*cos2(f.b+g.b)*x*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(2.0*f.a*x+f.b-g.b)*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.a*x+f.b-g.b)*sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/16.0;
            double t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value = t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value += t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b1 * f.a * cos2(f.d + g.d) * b4 * f.c + 2.0 * cos2(f.b + g.b) * b1 * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b1 + f.b - g.b) * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.a * b1 + f.b - g.b) * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            t0 = f.amp * g.amp * (4.0 * cos2(f.b + g.b) * b2 * f.a * cos2(f.d + g.d) * b3 * f.c + 2.0 * cos2(f.b + g.b) * b2 * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(2.0 * f.a * b2 + f.b - g.b) * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.a * b2 + f.b - g.b) * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 16.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(2.0*cos2(f.b+g.b)*x*f.a+sin2(2.0*f.a*x+f.b-g.b))*y/f.a/4.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b3 / f.a / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b4 / f.a / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b1 * f.a + sin2(2.0 * f.a * b1 + f.b - g.b)) * b4 / f.a / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (2.0 * cos2(f.b + g.b) * b2 * f.a + sin2(2.0 * f.a * b2 + f.b - g.b)) * b3 / f.a / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(f_c*y-g_d)
    public static double primi_cos4_fga_cf(Domain domain, CosXCosY f, CosXCosY g) {
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();

        if (f.a != 0 && f.c != 0 && g.a != 0) {
            //       t0 = g.amp*f.amp*(2.0*sin2(f.a*x-g.a*x+f.b-g.b)*f.a*cos2(f.d+g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*f.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(f.a*x-g.a*x+f.b-g.b)*g.a*cos2(f.d+g.d)*y*f.c+sin2(f.a*x-g.a*x+f.b-g.b)*g.a*sin2(2.0*f.c*y+f.d-g.d)+2.0*sin2(f.a*x+g.a*x+f.b+g.b)*f.a*cos2(f.d+g.d)*y*f.c+sin2(f.a*x+g.a*x+f.b+g.b)*f.a*sin2(2.0*f.c*y+f.d-g.d)-2.0*sin2(f.a*x+g.a*x+f.b+g.b)*g.a*cos2(f.d+g.d)*y*f.c-sin2(f.a*x+g.a*x+f.b+g.b)*g.a*sin2(2.0*f.c*y+f.d-g.d))/f.c/(f.a*f.a-g.a*g.a)/8.0;
            double t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d + g.d) * b3 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value = t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d + g.d) * b4 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value += t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a * sin2(2.0 * f.c * b4 + f.d - g.d) + 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * cos2(f.d + g.d) * b4 * f.c + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a * sin2(2.0 * f.c * b4 + f.d - g.d) - 2.0 * sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * cos2(f.d + g.d) * b4 * f.c - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a * sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            t0 = g.amp * f.amp * (2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a * sin2(2.0 * f.c * b3 + f.d - g.d) + 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * cos2(f.d + g.d) * b3 * f.c + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a * sin2(2.0 * f.c * b3 + f.d - g.d) - 2.0 * sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * cos2(f.d + g.d) * b3 * f.c - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a * sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / (f.a * f.a - g.a * g.a) / 8.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*sin2(g.a*x+g.b)*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/g.a/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / g.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / g.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b1 + g.b) * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / g.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * sin2(g.a * b2 + g.b) * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / g.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.d)*(sin2(f.a*x-g.a*x+f.b-g.b)*f.a+sin2(f.a*x-g.a*x+f.b-g.b)*g.a+sin2(f.a*x+g.a*x+f.b+g.b)*f.a-sin2(f.a*x+g.a*x+f.b+g.b)*g.a)*y/(f.a*f.a-g.a*g.a)/2.0;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b1 - g.a * b1 + f.b - g.b) * f.a + sin2(f.a * b1 - g.a * b1 + f.b - g.b) * g.a + sin2(f.a * b1 + g.a * b1 + f.b + g.b) * f.a - sin2(f.a * b1 + g.a * b1 + f.b + g.b) * g.a) * b4 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.d) * (sin2(f.a * b2 - g.a * b2 + f.b - g.b) * f.a + sin2(f.a * b2 - g.a * b2 + f.b - g.b) * g.a + sin2(f.a * b2 + g.a * b2 + f.b + g.b) * f.a - sin2(f.a * b2 + g.a * b2 + f.b + g.b) * g.a) * b3 / (f.a * f.a - g.a * g.a) / 2.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0) {
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b1 + g.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * b2 + g.b) * b3;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0) {
            //       t0 = f.amp*g.amp*cos2(g.b)*sin2(f.a*x+f.b)*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.a/f.c/4.0;
            double t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 4.0;
            value = t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 4.0;
            value += t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b1 + f.b) * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.a / f.c / 4.0;
            value -= t0;
            t0 = f.amp * g.amp * cos2(g.b) * sin2(f.a * b2 + f.b) * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.a / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0) {
            //       t0 = f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(2.0*cos2(f.d+g.d)*y*f.c+sin2(2.0*f.c*y+f.d-g.d))/f.c/4.0;
            double t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value = t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value += t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b1 * (2.0 * cos2(f.d + g.d) * b4 * f.c + sin2(2.0 * f.c * b4 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            t0 = f.amp * cos2(f.b) * g.amp * cos2(g.b) * b2 * (2.0 * cos2(f.d + g.d) * b3 * f.c + sin2(2.0 * f.c * b3 + f.d - g.d)) / f.c / 4.0;
            value -= t0;
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0) {
            //       t0 = f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y;
            double t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b3;
            value = t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b4;
            value += t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b1 + f.b) * b4;
            value -= t0;
            t0 = f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * b2 + f.b) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y;
            double t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b3;
            value = t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b4;
            value += t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b1 * b4;
            value -= t0;
            t0 = f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * b2 * b3;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}