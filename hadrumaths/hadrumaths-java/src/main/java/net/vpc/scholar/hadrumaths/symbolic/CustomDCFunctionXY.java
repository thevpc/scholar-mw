package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;

public interface CustomDCFunctionXY extends CustomFunction {
    Complex eval(double x, double y);
}
