package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.Div;
import net.vpc.scholar.hadrumaths.symbolic.Inv;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.symbolic.Neg;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class InvDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Inv c = (Inv) f;
        Expr e = c.getExpression();
        return new Div(new Neg(e), new Mul(e, e));
    }
}
