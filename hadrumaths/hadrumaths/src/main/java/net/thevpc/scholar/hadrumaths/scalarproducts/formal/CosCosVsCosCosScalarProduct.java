package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;

import static net.thevpc.scholar.hadrumaths.Maths.cos2;
import static net.thevpc.scholar.hadrumaths.Maths.sin2;

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
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        CosXCosY f1ok = (CosXCosY) f1;
        CosXCosY f2ok = (CosXCosY) f2;
        double d = evalTolerant(domain, f1ok, f2ok, 1E-15);
//        double d = compute(domain, f1ok, f2ok);
//        if (Double.isNaN(d)) {
////            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            //System.out.println("--**");
//            d = computeTolerant(domain, f1ok, f2ok, 1E-15);
//        }
        if (Double.isNaN(d)) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            double d0 = eval(domain, f1ok, f2ok);
            d = d0;
        }
        return d;
    }
//    public double compute(Domain domain, DFunction f1, DFunction f2) {
//        return new CosCosVsCosCosScalarProductOld2().compute(domain, f1, f2);
//    }

    private static double evalTolerant(Domain domain, CosXCosY f, CosXCosY g, double tolerance) {
        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();
        if (tolerantEqual(fa, ga, tolerance) && tolerantEqual(fc, gc, tolerance)) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (tolerantEqual(fa, ga, tolerance) && tolerantEqual(fc, -gc, tolerance)) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (tolerantEqual(fa, -ga, tolerance) && tolerantEqual(fc, gc, tolerance)) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (tolerantEqual(fa, -ga, tolerance) && tolerantEqual(fc, -gc, tolerance)) {
            return primi_cos4_af_cf(domain, f, g);
        } else if (tolerantEqual(fa, ga, tolerance)) {
            return primi_cos4_fa_fgc(domain, f, g);
        } else if (tolerantEqual(fa, -ga, tolerance)) {
            return primi_cos4_af_fgc(domain, f, g);
        } else if (tolerantEqual(fc, gc, tolerance)) {
            return primi_cos4_fga_fc(domain, f, g);
        } else if (tolerantEqual(fc, -gc, tolerance)) {
            return primi_cos4_fga_cf(domain, f, g);
        } else {
            return primi_cos4_fga_fgc(domain, f, g);
        }
    }

    static double eval(Domain domain, CosXCosY f, CosXCosY g) {
        double fa = f.getA();
        double ga = g.getA();
        double fc = f.getC();
        double gc = g.getC();
        if (fa == ga && fc == gc) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (fa == ga && fc == -gc) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (fa == -ga && fc == gc) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (fa == -ga && fc == -gc) {
            return primi_cos4_af_cf(domain, f, g);
        } else if (fa == ga) {
            return primi_cos4_fa_fgc(domain, f, g);
        } else if (fa == -ga) {
            return primi_cos4_af_fgc(domain, f, g);
        } else if (fc == gc) {
            return primi_cos4_fga_fc(domain, f, g);
        } else if (fc == -gc) {
            return primi_cos4_fga_cf(domain, f, g);
        } else {
            return primi_cos4_fga_fgc(domain, f, g);
        }
    }

    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        if (diff == 0) return true;
        if (diff < 0) {
            diff = -diff;
        }
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        double c = a < b ? b : a;

        if (c == 0) {
            return diff < tolerance;
        }
        return diff / c < tolerance;
//        return
//                diff == 0 ||
////                (diff != 0 ? (absdbl((diff) / a) < tolerance) : (absdbl((diff) / b) < tolerance))
//                (diff <0 ? (abs((diff) / a) < tolerance) : (abs((diff) / b) < tolerance))
//                ;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb-gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb-gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb+gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb+gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb + gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb - gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb + gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd-gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd-gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd+gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd - gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd - gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd - gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd - gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*(4.0*cos2(fb+gb)*x*fa*cos2(fd+gd)*y*fc+2.0*cos2(fb+gb)*x*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(2.0*fa*x+fb-gb)*cos2(fd+gd)*y*fc+sin2(2.0*fa*x+fb-gb)*sin2(2.0*fc*y+fd-gd))/fa/fc/16.0;
            double t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value = t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value += t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b1 * fa * cos2(fd + gd) * b4 * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(2.0 * fa * b1 + fb - gb) * cos2(fd + gd) * b4 * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            t0 = famp * gamp * (4.0 * cos2(fb + gb) * b2 * fa * cos2(fd + gd) * b3 * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(2.0 * fa * b2 + fb - gb) * cos2(fd + gd) * b3 * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 16.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = gamp*famp*(2.0*cos2(fb-gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*fc+2.0*cos2(fb-gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*gc+2.0*cos2(fb-gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*fc-2.0*cos2(fb-gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*gc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y-gc*y+fd-gd)*fc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y-gc*y+fd-gd)*gc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y+gc*y+fd+gd)*fc-sin2(2.0*fa*x+fb+gb)*sin2(fc*y+gc*y+fd+gd)*gc)/fa/(fc*fc-gc*gc)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*sin2(gc*y+gd)/fa/gc/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*x/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = gamp*famp*(2.0*cos2(fb-gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*fc+2.0*cos2(fb-gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*gc+2.0*cos2(fb-gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*fc-2.0*cos2(fb-gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*gc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y-gc*y+fd-gd)*fc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y-gc*y+fd-gd)*gc+sin2(2.0*fa*x+fb+gb)*sin2(fc*y+gc*y+fd+gd)*fc-sin2(2.0*fa*x+fb+gb)*sin2(fc*y+gc*y+fd+gd)*gc)/fa/(fc*fc-gc*gc)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb + gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb - gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb + gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*sin2(gc*y+gd)/fa/gc/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*x/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*sin2(fc*y+fd)/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*cos2(gd)*x/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*sin2(fc*y+fd)/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*cos2(gd)*x/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb-gb)*x*fa+sin2(2.0*fa*x+fb+gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b1 * fa + sin2(2.0 * fa * b1 + fb + gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb - gb) * b2 * fa + sin2(2.0 * fa * b2 + fb + gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = gamp*famp*(2.0*cos2(fb+gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*fc+2.0*cos2(fb+gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*gc+2.0*cos2(fb+gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*fc-2.0*cos2(fb+gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*gc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y-gc*y+fd-gd)*fc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y-gc*y+fd-gd)*gc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y+gc*y+fd+gd)*fc-sin2(2.0*fa*x+fb-gb)*sin2(fc*y+gc*y+fd+gd)*gc)/fa/(fc*fc-gc*gc)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*sin2(gc*y+gd)/fa/gc/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*x/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = gamp*famp*(2.0*cos2(fb+gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*fc+2.0*cos2(fb+gb)*x*fa*sin2(fc*y-gc*y+fd-gd)*gc+2.0*cos2(fb+gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*fc-2.0*cos2(fb+gb)*x*fa*sin2(fc*y+gc*y+fd+gd)*gc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y-gc*y+fd-gd)*fc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y-gc*y+fd-gd)*gc+sin2(2.0*fa*x+fb-gb)*sin2(fc*y+gc*y+fd+gd)*fc-sin2(2.0*fa*x+fb-gb)*sin2(fc*y+gc*y+fd+gd)*gc)/fa/(fc*fc-gc*gc)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b1 * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(2.0 * fa * b1 + fb - gb) * sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - 2.0 * cos2(fb + gb) * b2 * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(2.0 * fa * b2 + fb - gb) * sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*sin2(gc*y+gd)/fa/gc/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(gc * b4 + gd) / fa / gc / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(gc * b3 + gd) / fa / gc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*x/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*sin2(fc*y+fd)/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*cos2(gd)*x/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*sin2(fc*y+fd)/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * sin2(fc * b4 + fd) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * sin2(fc * b3 + fd) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*cos2(gd)*x/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(2.0*cos2(fb+gb)*x*fa+sin2(2.0*fa*x+fb-gb))*y/fa/4.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b3 / fa / 4.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b4 / fa / 4.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b1 * fa + sin2(2.0 * fa * b1 + fb - gb)) * b4 / fa / 4.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (2.0 * cos2(fb + gb) * b2 * fa + sin2(2.0 * fa * b2 + fb - gb)) * b3 / fa / 4.0;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = gamp*famp*(2.0*sin2(fa*x-ga*x+fb-gb)*fa*cos2(fd-gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(fa*x-ga*x+fb-gb)*ga*cos2(fd-gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(2.0*fc*y+fd+gd)+2.0*sin2(fa*x+ga*x+fb+gb)*fa*cos2(fd-gd)*y*fc+sin2(fa*x+ga*x+fb+gb)*fa*sin2(2.0*fc*y+fd+gd)-2.0*sin2(fa*x+ga*x+fb+gb)*ga*cos2(fd-gd)*y*fc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(2.0*fc*y+fd+gd))/fc/(fa*fa-ga*ga)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd - gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd + gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd - gd) * b3 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd - gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd + gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd - gd) * b4 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd - gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd + gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd - gd) * b4 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd - gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd + gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd - gd) * b3 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*sin2(ga*x+gb)*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/ga/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / ga / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / ga / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / ga / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / ga / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*y/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gd)/ga*sin2(ga*x+gb)*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*cos2(gb)*sin2(fa*x+fb)*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gb)*cos2(gd)/fa*sin2(fa*x+fb)*y;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b3;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b4;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b4;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b3;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = gamp*famp*(2.0*sin2(fa*x-ga*x+fb-gb)*fa*cos2(fd-gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*fa*sin2(2.0*fc*y+fd+gd)+2.0*sin2(fa*x-ga*x+fb-gb)*ga*cos2(fd-gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(2.0*fc*y+fd+gd)+2.0*sin2(fa*x+ga*x+fb+gb)*fa*cos2(fd-gd)*y*fc+sin2(fa*x+ga*x+fb+gb)*fa*sin2(2.0*fc*y+fd+gd)-2.0*sin2(fa*x+ga*x+fb+gb)*ga*cos2(fd-gd)*y*fc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(2.0*fc*y+fd+gd))/fc/(fa*fa-ga*ga)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd - gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd + gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd - gd) * b3 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd - gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd + gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd - gd) * b4 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd - gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd + gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd - gd) * b4 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd + gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd - gd) * b4 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd - gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd + gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd - gd) * b3 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd + gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd - gd) * b3 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd + gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*sin2(ga*x+gb)*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/ga/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / ga / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / ga / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / ga / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / ga / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*y/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gd)/ga*sin2(ga*x+gb)*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gb)*sin2(fa*x+fb)*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd-gd)*y*fc+sin2(2.0*fc*y+fd+gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd - gd) * b4 * fc + sin2(2.0 * fc * b4 + fd + gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd - gd) * b3 * fc + sin2(2.0 * fc * b3 + fd + gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gb)*cos2(gd)/fa*sin2(fa*x+fb)*y;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b3;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b4;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b4;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0) {
            //       t0 = gamp*famp*(2.0*sin2(fa*x-ga*x+fb-gb)*fa*cos2(fd+gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*fa*sin2(2.0*fc*y+fd-gd)+2.0*sin2(fa*x-ga*x+fb-gb)*ga*cos2(fd+gd)*y*fc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(2.0*fc*y+fd-gd)+2.0*sin2(fa*x+ga*x+fb+gb)*fa*cos2(fd+gd)*y*fc+sin2(fa*x+ga*x+fb+gb)*fa*sin2(2.0*fc*y+fd-gd)-2.0*sin2(fa*x+ga*x+fb+gb)*ga*cos2(fd+gd)*y*fc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(2.0*fc*y+fd-gd))/fc/(fa*fa-ga*ga)/8.0;
            double t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd + gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd + gd) * b3 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd + gd) * b3 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd - gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd + gd) * b3 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd - gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value = t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd + gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd + gd) * b4 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd + gd) * b4 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd - gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd + gd) * b4 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd - gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value += t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * fa * cos2(fd + gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(fa * b1 - ga * b1 + fb - gb) * ga * cos2(fd + gd) * b4 * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(2.0 * fc * b4 + fd - gd) + 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * fa * cos2(fd + gd) * b4 * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(2.0 * fc * b4 + fd - gd) - 2.0 * sin2(fa * b1 + ga * b1 + fb + gb) * ga * cos2(fd + gd) * b4 * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(2.0 * fc * b4 + fd - gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            t0 = gamp * famp * (2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * fa * cos2(fd + gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(fa * b2 - ga * b2 + fb - gb) * ga * cos2(fd + gd) * b3 * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(2.0 * fc * b3 + fd - gd) + 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * fa * cos2(fd + gd) * b3 * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(2.0 * fc * b3 + fd - gd) - 2.0 * sin2(fa * b2 + ga * b2 + fb + gb) * ga * cos2(fd + gd) * b3 * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(2.0 * fc * b3 + fd - gd)) / fc / (fa * fa - ga * ga) / 8.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0) {
            //       t0 = famp*cos2(fb)*gamp*sin2(ga*x+gb)*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/ga/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / ga / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / ga / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / ga / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / ga / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*y/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gd)/ga*sin2(ga*x+gb)*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0) {
            //       t0 = famp*gamp*cos2(gb)*sin2(fa*x+fb)*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fa/fc/4.0;
            double t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 4.0;
            value = t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 4.0;
            value += t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fa / fc / 4.0;
            value -= t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fa / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(2.0*cos2(fd+gd)*y*fc+sin2(2.0*fc*y+fd-gd))/fc/4.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (2.0 * cos2(fd + gd) * b4 * fc + sin2(2.0 * fc * b4 + fd - gd)) / fc / 4.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (2.0 * cos2(fd + gd) * b3 * fc + sin2(2.0 * fc * b3 + fd - gd)) / fc / 4.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gb)*cos2(gd)/fa*sin2(fa*x+fb)*y;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b3;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b4;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b4;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;
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

        double fa = f.getA();
        double fb = f.getB();
        double fc = f.getC();
        double fd = f.getD();
        double famp = f.getAmp();

        double ga = g.getA();
        double gb = g.getB();
        double gc = g.getC();
        double gd = g.getD();
        double gamp = g.getAmp();

        if (fa != 0 && fc != 0 && ga != 0 && gc != 0) {
            //       s1 = gamp/4.0;      s3 = famp;      s6 = sin2(fa*x-ga*x+fb-gb)*fa*sin2(fc*y-gc*y+fd-gd)*fc+sin2(fa*x-ga*x+fb-gb)*fa*sin2(fc*y-gc*y+fd-gd)*gc+sin2(fa*x-ga*x+fb-gb)*fa*sin2(fc*y+gc*y+fd+gd)*fc-sin2(fa*x-ga*x+fb-gb)*fa*sin2(fc*y+gc*y+fd+gd)*gc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(fc*y-gc*y+fd-gd)*fc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(fc*y-gc*y+fd-gd)*gc+sin2(fa*x-ga*x+fb-gb)*ga*sin2(fc*y+gc*y+fd+gd)*fc-sin2(fa*x-ga*x+fb-gb)*ga*sin2(fc*y+gc*y+fd+gd)*gc;      s5 = s6+sin2(fa*x+ga*x+fb+gb)*fa*sin2(fc*y-gc*y+fd-gd)*fc+sin2(fa*x+ga*x+fb+gb)*fa*sin2(fc*y-gc*y+fd-gd)*gc+sin2(fa*x+ga*x+fb+gb)*fa*sin2(fc*y+gc*y+fd+gd)*fc-sin2(fa*x+ga*x+fb+gb)*fa*sin2(fc*y+gc*y+fd+gd)*gc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(fc*y-gc*y+fd-gd)*fc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(fc*y-gc*y+fd-gd)*gc-sin2(fa*x+ga*x+fb+gb)*ga*sin2(fc*y+gc*y+fd+gd)*fc+sin2(fa*x+ga*x+fb+gb)*ga*sin2(fc*y+gc*y+fd+gd)*gc;      s6 = 1/(fa*fa*fc*fc-fa*fa*gc*gc-ga*ga*fc*fc+ga*ga*gc*gc);      s4 = s5*s6;      s2 = s3*s4;      t0 = s1*s2;
            double t0;
            double s6;
            double s5;
            double s4;
            double s3;
            double s2;
            double s1;
            s1 = gamp / 4.0;
            s3 = famp;
            s6 = sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * gc;
            s5 = s6 + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * gc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * fc + sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * gc;
            s6 = 1 / (fa * fa * fc * fc - fa * fa * gc * gc - ga * ga * fc * fc + ga * ga * gc * gc);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value = t0;
            s1 = gamp / 4.0;
            s3 = famp;
            s6 = sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * gc;
            s5 = s6 + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * gc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * fc + sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * gc;
            s6 = 1 / (fa * fa * fc * fc - fa * fa * gc * gc - ga * ga * fc * fc + ga * ga * gc * gc);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value += t0;
            s1 = gamp / 4.0;
            s3 = famp;
            s6 = sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b1 - ga * b1 + fb - gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b1 - ga * b1 + fb - gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * gc;
            s5 = s6 + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fa * b1 + ga * b1 + fb + gb) * fa * sin2(fc * b4 + gc * b4 + fd + gd) * gc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * fc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b4 - gc * b4 + fd - gd) * gc - sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * fc + sin2(fa * b1 + ga * b1 + fb + gb) * ga * sin2(fc * b4 + gc * b4 + fd + gd) * gc;
            s6 = 1 / (fa * fa * fc * fc - fa * fa * gc * gc - ga * ga * fc * fc + ga * ga * gc * gc);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            s1 = gamp / 4.0;
            s3 = famp;
            s6 = sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b2 - ga * b2 + fb - gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b2 - ga * b2 + fb - gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * gc;
            s5 = s6 + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fa * b2 + ga * b2 + fb + gb) * fa * sin2(fc * b3 + gc * b3 + fd + gd) * gc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * fc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b3 - gc * b3 + fd - gd) * gc - sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * fc + sin2(fa * b2 + ga * b2 + fb + gb) * ga * sin2(fc * b3 + gc * b3 + fd + gd) * gc;
            s6 = 1 / (fa * fa * fc * fc - fa * fa * gc * gc - ga * ga * fc * fc + ga * ga * gc * gc);
            s4 = s5 * s6;
            s2 = s3 * s4;
            t0 = s1 * s2;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*sin2(ga*x+gb)*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/ga/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / ga / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / ga / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b1 + gb) * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / ga / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * sin2(ga * b2 + gb) * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / ga / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*sin2(gc*y+gd)/gc/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * sin2(gc * b3 + gd) / gc / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * sin2(gc * b4 + gd) / gc / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * sin2(gc * b4 + gd) / gc / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * sin2(gc * b3 + gd) / gc / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp/ga*sin2(ga*x+gb)/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * b1 + gb) / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * b2 + gb) / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * b1 + gb) / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * b2 + gb) / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*gamp*cos2(gb)*sin2(fa*x+fb)*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/fa/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b1 + fb) * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * gamp * cos2(gb) * sin2(fa * b2 + fb) * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / fa / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*x*(sin2(fc*y-gc*y+fd-gd)*fc+sin2(fc*y-gc*y+fd-gd)*gc+sin2(fc*y+gc*y+fd+gd)*fc-sin2(fc*y+gc*y+fd+gd)*gc)/(fc*fc-gc*gc)/2.0;
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b1 * (sin2(fc * b4 - gc * b4 + fd - gd) * fc + sin2(fc * b4 - gc * b4 + fd - gd) * gc + sin2(fc * b4 + gc * b4 + fd + gd) * fc - sin2(fc * b4 + gc * b4 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * b2 * (sin2(fc * b3 - gc * b3 + fd - gd) * fc + sin2(fc * b3 - gc * b3 + fd - gd) * gc + sin2(fc * b3 + gc * b3 + fd + gd) * fc - sin2(fc * b3 + gc * b3 + fd + gd) * gc) / (fc * fc - gc * gc) / 2.0;
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gb)/fa*sin2(fa*x+fb)/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * b1 + fb) / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * b2 + fb) / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * b1 + fb) / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * b2 + fb) / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*x/gc*sin2(gc*y+gd);
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b3 + gd);
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b4 + gd);
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b1 / gc * sin2(gc * b4 + gd);
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * b2 / gc * sin2(gc * b3 + gd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gd)*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*sin2(fc*y+fd)/fc/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * sin2(fc * b3 + fd) / fc / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * sin2(fc * b4 + fd) / fc / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * sin2(fc * b4 + fd) / fc / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * sin2(fc * b3 + fd) / fc / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gd)/ga*sin2(ga*x+gb)/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gd)*(sin2(fa*x-ga*x+fb-gb)*fa+sin2(fa*x-ga*x+fb-gb)*ga+sin2(fa*x+ga*x+fb+gb)*fa-sin2(fa*x+ga*x+fb+gb)*ga)*y/(fa*fa-ga*ga)/2.0;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b1 - ga * b1 + fb - gb) * fa + sin2(fa * b1 - ga * b1 + fb - gb) * ga + sin2(fa * b1 + ga * b1 + fb + gb) * fa - sin2(fa * b1 + ga * b1 + fb + gb) * ga) * b4 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gd) * (sin2(fa * b2 - ga * b2 + fb - gb) * fa + sin2(fa * b2 - ga * b2 + fb - gb) * ga + sin2(fa * b2 + ga * b2 + fb + gb) * fa - sin2(fa * b2 + ga * b2 + fb + gb) * ga) * b3 / (fa * fa - ga * ga) / 2.0;
            value -= t0;
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gd)/ga*sin2(ga*x+gb)*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b1 + gb) * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * b2 + gb) * b3;
            value -= t0;
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*gamp*cos2(gb)*cos2(gd)/fa*sin2(fa*x+fb)/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fb)*gamp*cos2(gb)*cos2(gd)*x/fc*sin2(fc*y+fd);
            double t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b3 + fd);
            value = t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b4 + fd);
            value += t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b1 / fc * sin2(fc * b4 + fd);
            value -= t0;
            t0 = famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * b2 / fc * sin2(fc * b3 + fd);
            value -= t0;
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            //       t0 = famp*cos2(fd)*gamp*cos2(gb)*cos2(gd)/fa*sin2(fa*x+fb)*y;
            double t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b3;
            value = t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b4;
            value += t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b1 + fb) * b4;
            value -= t0;
            t0 = famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * b2 + fb) * b3;
            value -= t0;
            return value;

        } else { //all are nulls
            //       t0 = famp*cos2(fb)*cos2(fd)*gamp*cos2(gb)*cos2(gd)*x*y;
            double t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b3;
            value = t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b4;
            value += t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b1 * b4;
            value -= t0;
            t0 = famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * b2 * b3;
            value -= t0;
            return value;
        }
    }
//ENDING---------------------------------------
}