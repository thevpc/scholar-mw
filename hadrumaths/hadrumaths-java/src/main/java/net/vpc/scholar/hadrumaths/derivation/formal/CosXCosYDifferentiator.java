package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class CosXCosYDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        CosXCosY c = (CosXCosY) f;
        switch (varIndex) {
            case X: {
                return new CosXCosY(c.amp * c.a, c.a, c.b - MathsBase.PI / 2, c.c, c.d, c.getDomain());
            }
            case Y: {
                return new CosXCosY(c.amp * c.c, c.a, c.b, c.c, c.d - MathsBase.PI / 2, c.getDomain());
            }
        }
        return FunctionFactory.DZEROXY;
    }
}
