package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.*;
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
        String functionName = c.getFunctionName();
        if (functionName.equals(varIndex.toString())) {
            return Maths.expr(c.getDomain());
        }
        return Complex.ZERO;
    }
}
