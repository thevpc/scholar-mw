package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.AxisFunction;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class AxisFunctionDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        AxisFunction c = (AxisFunction) f;
        Axis functionName = c.getAxis();
        if (functionName == varIndex) {
            if (c.getDomain().isUnbounded()) {
                return Complex.ONE;
            }
            return c.getDomain();//Maths.expr(c.getDomain());
        }
        return Complex.ZERO;
    }
}
