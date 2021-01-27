package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Complex;

public interface CustomCCFunctionXY extends CustomFunction {
    Complex eval(Complex x, Complex y);
}
