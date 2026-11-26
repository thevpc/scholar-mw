package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.DoubleExpr;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Sqr;

import static net.thevpc.scholar.hadrumaths.Maths.mul;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class SqrDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Sqr c = (Sqr) f;
        Expr a = c.getChild(0);
        return
                Maths.prod(
                        DoubleExpr.TWO,
                        a,
                        d.derive(a, varIndex)
                );
    }
}
