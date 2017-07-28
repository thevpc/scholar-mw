package net.vpc.scholar.hadrumaths.scalarproducts.formal;

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
final class CosCosVsCosCosScalarProductOld1 implements FormalScalarProductHelper {
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
        double d=compute(domain, f1ok, f2ok);
        if(Double.isNaN(d)){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return d;
    }


    public static double compute(Domain domain, CosXCosY f, CosXCosY g) {
        if (f.a == g.a && f.c == f.c) {
            return primi_cos4_fa_fc(domain, f, g);
        } else if (f.a == g.a && f.c == -f.c) {
            return primi_cos4_fa_cf(domain, f, g);
        } else if (f.a == -g.a && f.c == f.c) {
            return primi_cos4_af_fc(domain, f, g);
        } else if (f.a == -g.a && f.c == -f.c) {
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * x + g.b) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * x + g.b) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * x + g.b) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp / g.a * sin2(g.a * x + g.b) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) / f.a * sin2(f.a * x + f.b) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.b) / f.a * sin2(f.a * x + f.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.d) / g.a * sin2(g.a * x + g.b) * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp / g.a * sin2(g.a * x + g.b) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 / (f.a - g.a) * sin2((f.a - g.a) * x + f.b - g.b) + 0.5 / (f.a + g.a) * sin2((f.a + g.a) * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b - g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b + g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * cos2(g.d) * x / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / f.c * sin2(f.c * y + f.d));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * x / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) / g.c * sin2(g.c * y + g.d));
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 / (f.c - g.c) * sin2((f.c - g.c) * y + f.d - g.d) + 0.5 / (f.c + g.c) * sin2((f.c + g.c) * y + f.d + g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d - g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d + g.d)));
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

        if (f.a == 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a != 0 && g.c == 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a != 0 && f.c != 0 && g.a == 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else if (f.a == 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * cos2(f.d) * g.amp * cos2(g.b) * cos2(g.d) * x * y);
            return value;

        } else if (f.a != 0 && f.c == 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.d) * g.amp * cos2(g.d) * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * y);
            return value;

        } else if (f.a == 0 && f.c != 0 && g.a != 0 && g.c != 0) {
            x = b1;
            y = b3;
            value = (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * cos2(f.b) * g.amp * cos2(g.b) * x * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b4;
            value += (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b1;
            y = b4;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            x = b2;
            y = b3;
            value -= (f.amp * g.amp * (0.5 * cos2(f.b + g.b) * x + 0.25 / f.a * sin2(2.0 * f.a * x + f.b - g.b)) * (0.5 * cos2(f.d + g.d) * y + 0.25 / f.c * sin2(2.0 * f.c * y + f.d - g.d)));
            return value;
        }
    }


 // THIS FILE WAS GENERATED AUTOMATICALLY.
 // DO NOT EDIT MANUALLY.
 // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_amp*cos2(g_a*x+g_b)*cos2(f_c*y-g_d)
private static double primi_cos4_fga_cf(Domain domain,CosXCosY f,CosXCosY g){
  double x;
  double y;
  double value;
  double b1 = domain.xmin();
  double b2 = domain.xmax();
  double b3 = domain.ymin();
  double b4 = domain.ymax();

  if (f.a == 0 && f.c == 0 && g.a == 0){
    x=b1;y=b3;value =(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y);
    x=b2;y=b4;value+=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y);
    x=b1;y=b4;value-=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y);
    x=b2;y=b3;value-=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)*x*y);
    return value;

  }else if (f.a != 0 && f.c == 0 && g.a == 0){
    x=b1;y=b3;value =(f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y);
    x=b2;y=b4;value+=(f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y);
    x=b1;y=b4;value-=(f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y);
    x=b2;y=b3;value-=(f.amp*cos2(f.d)*g.amp*cos2(g.b)*cos2(g.d)/f.a*sin2(f.a*x+f.b)*y);
    return value;

  }else if (f.a == 0 && f.c != 0 && g.a == 0){
    x=b1;y=b3;value =(f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b4;value+=(f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b1;y=b4;value-=(f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b3;value-=(f.amp*cos2(f.b)*g.amp*cos2(g.b)*x*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    return value;

  }else if (f.a != 0 && f.c != 0 && g.a == 0){
    x=b1;y=b3;value =(f.amp*g.amp*cos2(g.b)/f.a*sin2(f.a*x+f.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b4;value+=(f.amp*g.amp*cos2(g.b)/f.a*sin2(f.a*x+f.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b1;y=b4;value-=(f.amp*g.amp*cos2(g.b)/f.a*sin2(f.a*x+f.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b3;value-=(f.amp*g.amp*cos2(g.b)/f.a*sin2(f.a*x+f.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    return value;

  }else if (f.a == 0 && f.c == 0 && g.a != 0){
    x=b1;y=b3;value =(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y);
    x=b2;y=b4;value+=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y);
    x=b1;y=b4;value-=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y);
    x=b2;y=b3;value-=(f.amp*cos2(f.b)*cos2(f.d)*g.amp*cos2(g.d)/g.a*sin2(g.a*x+g.b)*y);
    return value;

  }else if (f.a != 0 && f.c == 0 && g.a != 0){
    x=b1;y=b3;value =(f.amp*cos2(f.d)*g.amp*cos2(g.d)*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*y);
    x=b2;y=b4;value+=(f.amp*cos2(f.d)*g.amp*cos2(g.d)*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*y);
    x=b1;y=b4;value-=(f.amp*cos2(f.d)*g.amp*cos2(g.d)*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*y);
    x=b2;y=b3;value-=(f.amp*cos2(f.d)*g.amp*cos2(g.d)*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*y);
    return value;

  }else if (f.a == 0 && f.c != 0 && g.a != 0){
    x=b1;y=b3;value =(f.amp*cos2(f.b)*g.amp/g.a*sin2(g.a*x+g.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b4;value+=(f.amp*cos2(f.b)*g.amp/g.a*sin2(g.a*x+g.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b1;y=b4;value-=(f.amp*cos2(f.b)*g.amp/g.a*sin2(g.a*x+g.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b3;value-=(f.amp*cos2(f.b)*g.amp/g.a*sin2(g.a*x+g.b)*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    return value;

  }else{ //none is null
    x=b1;y=b3;value =(f.amp*g.amp*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b4;value+=(f.amp*g.amp*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b1;y=b4;value-=(f.amp*g.amp*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    x=b2;y=b3;value-=(f.amp*g.amp*(0.5/(-f.a+g.a)*sin2((-f.a+g.a)*x-f.b+g.b)+0.5/(f.a+g.a)*sin2((f.a+g.a)*x+f.b+g.b))*(0.5*cos2(f.d+g.d)*y+0.25/f.c*sin2(2*f.c*y+f.d-g.d)));
    return value;
  }
}//ENDING---------------------------------------
}
