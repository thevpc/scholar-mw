package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.MissingAxisException;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class Domain2To1ScalarProduct implements FormalScalarProductHelper {
    public static final Domain2To1ScalarProduct INSTANCE = new Domain2To1ScalarProduct();

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        if (domain.getDimension() != 1 || !f1.isInvariant(Axis.Y) || !f1.isInvariant(Axis.Z) || !f2.isInvariant(Axis.Y) || !f2.isInvariant(Axis.Z)) {
//            throw new IllegalArgumentException("Unable to integrate over x with missing y definition");
            throw new MissingAxisException(Axis.Y);
        }
        Domain d2 = Domain.ofBounds(domain.xmin(), domain.xmax(), 0, 1);
        return sp.evalDD(d2, f1, f2);
    }
}
