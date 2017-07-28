package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;

public interface FormalScalarProductHelper {
    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp);
}
