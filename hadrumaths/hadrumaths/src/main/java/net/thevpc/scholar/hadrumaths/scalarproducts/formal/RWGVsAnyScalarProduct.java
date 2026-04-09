package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductHelper;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;

import static net.thevpc.scholar.hadrumaths.Maths.cos2;
import static net.thevpc.scholar.hadrumaths.Maths.sin2;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class RWGVsAnyScalarProduct implements FormalScalarProductHelper {
    public static final RWGVsAnyScalarProduct INSTANCE = new RWGVsAnyScalarProduct();

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        RWG rwg = (RWG) f2;
        return integrateOverTriangle(rwg.tr1, f1, rwg)
                + integrateOverTriangle(rwg.tr2, f1, rwg)
                ;
    }

    private double integrateOverTriangle(HTriangle t, DoubleToDouble cos, RWG rwg) {
        Domain intersection = t.getDomain().intersect(cos.getDomain());
        if (intersection.isEmpty()) return 0;
        double y = ScalarProductHelper.integrateOverTriangleUsingGaussianQuadrature(t, cos, rwg);
        if(Double.isNaN(y)){
            y = ScalarProductHelper.integrateOverTriangleUsingGaussianQuadrature(t, cos, rwg);
        }
        return y;
    }
}
