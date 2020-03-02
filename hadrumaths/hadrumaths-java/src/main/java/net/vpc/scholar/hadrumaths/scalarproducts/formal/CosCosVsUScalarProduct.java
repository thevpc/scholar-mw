package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.UFunction;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class CosCosVsUScalarProduct implements FormalScalarProductHelper {
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
        UFunction u = (UFunction) f2;
        // je suppose que cela ne dépend pas de y!!!!!
        double delta = u.getB() * u.getB() - 4 * u.getA() * u.getC();
//        double delta=u.getB()*u.getB() - 4 * u.getA() * u.getC();
        return 0;
//        double alpha=
//        return _ps_cc_cst(b1, b3, f1ok, f2ok)
//                + _ps_cc_cst(b2, b4, f1ok, f2ok)
//                - _ps_cc_cst(b1, b4, f1ok, f2ok)
//                - _ps_cc_cst(b2, b3, f1ok, f2ok);
    }

//    private static double _ps_cc_cst(double x, double y, DCosCosFunction f, DCstFunction g) {
//        if (f.a == 0 && f.c == 0)
//            return f.amp * Math.cos(f.b) * Math.cos(f.d) * g.cst * x * y;
//        else if (f.a != 0 && f.c == 0)
//            return f.amp * Math.cos(f.d) * g.cst / f.a * Math.sin(f.a * x + f.b) * y;
//
//        else if (f.a == 0 && f.c != 0)
//            return f.amp * Math.cos(f.b) * g.cst * x / f.c * Math.sin(f.c * y + f.d);
//
//        else //%none is null
//            return f.amp * g.cst / f.a * Math.sin(f.a * x + f.b) / f.c * Math.sin(f.c * y + f.d);
//    }

}
