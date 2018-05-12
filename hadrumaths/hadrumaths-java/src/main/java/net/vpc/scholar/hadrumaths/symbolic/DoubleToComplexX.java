package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.OutBoolean;

public interface DoubleToComplexX extends DoubleToComplex {
    default Complex computeComplex(double x, double y, double z, OutBoolean defined) {
        if (contains(x, y, z)) {
            return computeComplex(x, defined);
        }
        return Complex.ZERO;
    }

    default Complex computeComplex(double x, double y, OutBoolean defined) {
        if (contains(x, y)) {
            return computeComplex(x, defined);
        }
        return Complex.ZERO;
    }

}
