package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.Div;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.symbolic.Sub;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class DivDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Div c = (Div) f;
        Expr a = c.getFirst();
        Expr b = c.getSecond();
        Expr ad = d.derive(a, varIndex);
        Expr bd = d.derive(b, varIndex);

        // a'/b - b'a / b²


        return new Sub(
                new Div(ad, b),
                new Div(new Mul(a, bd), new Mul(b, b))
        );
    }
}
