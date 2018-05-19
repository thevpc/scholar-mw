package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;


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
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        if (domain.getDimension() != 1 || !f1.isInvariant(Axis.Y) || !f1.isInvariant(Axis.Z) || !f2.isInvariant(Axis.Y) || !f2.isInvariant(Axis.Z)) {
            throw new IllegalArgumentException("Unable to integrate over x with missing y definition");
        }
        Domain d2 = Domain.forBounds(domain.xmin(), domain.xmax(), 0, 1);
        return sp.evalDD(d2, f1, f2);
    }
}
