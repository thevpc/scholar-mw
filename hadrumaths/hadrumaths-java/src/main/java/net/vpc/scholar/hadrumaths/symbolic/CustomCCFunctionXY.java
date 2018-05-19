package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;

public interface CustomCCFunctionXY extends CustomFunction {
    Complex evalComplex(Complex x, Complex y);
}
