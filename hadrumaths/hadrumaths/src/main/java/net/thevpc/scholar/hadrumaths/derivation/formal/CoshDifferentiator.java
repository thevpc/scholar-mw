package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Cosh;

import static net.thevpc.scholar.hadrumaths.Maths.mul;
import static net.thevpc.scholar.hadrumaths.Maths.sinh;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class CoshDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Cosh c = (Cosh) f;
        Expr a = c.getChild(0);
        return
                mul(
                        d.derive(a, varIndex),
                        sinh(a)
                );
    }
}
