package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import static net.vpc.scholar.hadrumaths.Maths.cos2;
import static net.vpc.scholar.hadrumaths.Maths.sin2;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class CosCosVsDoubleXYScalarProduct implements FormalScalarProductHelper {
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
        CosXCosY f = (CosXCosY) f1;
        DoubleValue g = (DoubleValue) f2;
//        return primi_coscst(domain, f, g);
//    }


//STARTING---------------------------------------
    // THIS FILE WAS GENERATED AUTOMATICALLY.
    // DO NOT EDIT MANUALLY.
    // INTEGRATION FOR f_amp*cos2(f_a*x+f_b)*cos2(f_c*y+f_d)*g_cst
//    public static double primi_coscst(DomainXY domain, DCosCosFunctionXY f, DCstFunctionXY g) {
        double x;
        double y;
        double value;
        double b1 = domain.xmin();
        double b2 = domain.xmax();
        double b3 = domain.ymin();
        double b4 = domain.ymax();
        double fa = f.a;
        double fc = f.c;
        double famp = f.amp;
        double fb = f.b;
        double fd = f.d;
        double gvalue = g.value;

        if (fa == 0 && fc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * cos2(fd) * gvalue * x * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * cos2(fd) * gvalue * x * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * cos2(fd) * gvalue * x * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * cos2(fd) * gvalue * x * y);
            return value;

        } else if (fa != 0 && fc == 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fd) * gvalue / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b4;
            value += (famp * cos2(fd) * gvalue / fa * sin2(fa * x + fb) * y);
            x = b1;
            y = b4;
            value -= (famp * cos2(fd) * gvalue / fa * sin2(fa * x + fb) * y);
            x = b2;
            y = b3;
            value -= (famp * cos2(fd) * gvalue / fa * sin2(fa * x + fb) * y);
            return value;

        } else if (fa == 0 && fc != 0) {
            x = b1;
            y = b3;
            value = (famp * cos2(fb) * gvalue * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * cos2(fb) * gvalue * x / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * cos2(fb) * gvalue * x / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * cos2(fb) * gvalue * x / fc * sin2(fc * y + fd));
            return value;

        } else { //none is null
            x = b1;
            y = b3;
            value = (famp * gvalue / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b4;
            value += (famp * gvalue / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b1;
            y = b4;
            value -= (famp * gvalue / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            x = b2;
            y = b3;
            value -= (famp * gvalue / fa * sin2(fa * x + fb) / fc * sin2(fc * y + fd));
            return value;
        }
    }
//ENDING---------------------------------------

}
