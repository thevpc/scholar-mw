package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;


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
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        switch (domain.getDimension()) {
            case 1: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                return ((DoubleValue) f1).value * ((DoubleValue) f2).value * (b2 - b1);
            }
            case 2: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                double b3 = domain.ymin();
                double b4 = domain.ymax();
                return ((DoubleValue) f1).value * ((DoubleValue) f2).value * (b2 - b1) * (b4 - b3);
            }
            case 3: {
                double b1 = domain.xmin();
                double b2 = domain.xmax();
                double b3 = domain.ymin();
                double b4 = domain.ymax();
                double b5 = domain.zmin();
                double b6 = domain.zmax();
                return ((DoubleValue) f1).value * ((DoubleValue) f2).value * (b2 - b1) * (b4 - b3) * (b6 - b5);
            }
        }
        throw new IllegalArgumentException("Unsupported domain");
    }
}
