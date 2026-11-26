package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.DoubleExpr;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * d/dx (a*x + b*y + c) = a, d/dy = b. The simplifier rewrites real polynomials as
 * {@code Linear}, so without this every derivative of a linear expression failed with
 * "FunctionDifferentiator for type Linear".
 */
public class LinearDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Linear c = (Linear) f;
        double v = varIndex == Axis.X ? c.getA() : (varIndex == Axis.Y ? c.getB() : 0);
        if (v == 0) {
            return DoubleExpr.ZERO;
        }
        if (c.getDomain().isUnbounded()) {
            return Maths.expr(v);
        }
        return Maths.expr(v, c.getDomain());
    }
}
