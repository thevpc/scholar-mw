package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.AxisFunction;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class AxisFunctionDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        AxisFunction c = (AxisFunction) f;
        Axis functionName = c.getAxis();
        if (functionName == varIndex) {
            if (c.getDomain().isFull()) {
                return Complex.ONE;
            }
            return c.getDomain();//MathsBase.expr(c.getDomain());
        }
        return Complex.ZERO;
    }
}
