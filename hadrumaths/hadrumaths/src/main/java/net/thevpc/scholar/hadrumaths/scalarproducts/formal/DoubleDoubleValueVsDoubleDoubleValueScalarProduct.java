package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class DoubleDoubleValueVsDoubleDoubleValueScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        switch (domain.getDimension()) {
            case 1: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                return ((DoubleValue) f1).toDouble() * ((DoubleValue) f2).toDouble() * (b2 - b1);
            }
            case 2: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                double b3 = domain.ymin();
                double b4 = domain.ymax();
                return ((DoubleValue) f1).toDouble() * ((DoubleValue) f2).toDouble() * (b2 - b1) * (b4 - b3);
            }
            case 3: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                double b3 = domain.ymin();
                double b4 = domain.ymax();
                double b5 = domain.zmin();
                double b6 = domain.zmax();
                return ((DoubleValue) f1).toDouble() * ((DoubleValue) f2).toDouble() * (b2 - b1) * (b4 - b3) * (b6 - b5);
            }
        }
        throw new IllegalArgumentException("Unsupported domain");
    }
}
