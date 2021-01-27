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
final class CosCosVsCosCosScalarProductOld1 implements FormalScalarProductHelper {
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
        double d = eval(domain, f1ok, f2ok);
        if (Double.isNaN(d)) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return d;
    }


    public static double eval(Domain domain, CosXCosY f, CosXCosY g) {

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

        if (fa == ga && fc == fc) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (fa == ga && fc == -fc) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (fa == -ga && fc == fc) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (fa == -ga && fc == -fc) {
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

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(f_c*y+g_d)
    private static double primi_cos4_fa_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(f_c*y-g_d)
    private static double primi_cos4_fa_cf(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(f_c*y+g_d)
    private static double primi_cos4_af_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(f_c*y-g_d)
    private static double primi_cos4_af_cf(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd - gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x+g_b)*cos2(g_c*y+g_d)
    private static double primi_cos4_fa_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb - gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(f_a*x-g_b)*cos2(g_c*y+g_d)
    private static double primi_cos4_af_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gd) * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 * cos2(fb + gb) * x + 0.25 / fa * sin2(2.0 * fa * x + fb - gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(f_c*y+g_d)
    private static double primi_cos4_fga_fc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd - gd) * y + 0.25 / fc * sin2(2.0 * fc * y + fd + gd)));
            return value;
        }
    }

    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(f_c*y-g_d)
    private static double primi_cos4_fga_cf(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 / (-fa + ga) * sin2((-fa + ga) * x - fb + gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 * cos2(fd + gd) * y + 0.25 / fc * sin2(2 * fc * y + fd - gd)));
            return value;
        }
    }//ENDING---------------------------------------

    //STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(g_c*y+g_d)
    private static double primi_cos4_fga_fgc(Domain domain, CosXCosY f, CosXCosY g) {
        double x;
        double y;
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

        if (fa == 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * cos2(gd) * x * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * cos2(gd) * x / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gb) * cos2(gd) / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gd) / ga * sin2(ga * x + gb) * y);
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * y);
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * x + gb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * x + gb) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * x + gb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gd) / ga * sin2(ga * x + gb) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa != 0 && fc != 0 && ga != 0 && gc == 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gd) * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / fc * sin2(fc * y + fd));
            return value;

        } else if (fa == 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp * cos2(gb) * x / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * x + fb) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * x + fb) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * x + fb) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * cos2(gb) / fa * sin2(fa * x + fb) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp * cos2(gb) * x * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa != 0 && fc != 0 && ga == 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * cos2(gb) / fa * sin2(fa * x + fb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else if (fa == 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * x + gb) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * x + gb) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * x + gb) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gamp / ga * sin2(ga * x + gb) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa != 0 && fc == 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / gc * sin2(gc * y + gd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) / gc * sin2(gc * y + gd));
            return value;

        } else if (fa == 0 && fc != 0 && ga != 0 && gc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gamp / ga * sin2(ga * x + gb) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b4;
            value += (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b1;
            y = b4;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            x = b2;
            y = b3;
            value -= (famp * gamp * (0.5 / (fa - ga) * sin2((fa - ga) * x + fb - gb) + 0.5 / (fa + ga) * sin2((fa + ga) * x + fb + gb)) * (0.5 / (fc - gc) * sin2((fc - gc) * y + fd - gd) + 0.5 / (fc + gc) * sin2((fc + gc) * y + fd + gd)));
            return value;
        }
    }
}
