package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;

import static net.thevpc.scholar.hadrumaths.Maths.minus;
import static net.thevpc.scholar.hadrumaths.Maths.minus;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class SubDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Minus c = (Minus) f;
        return minus(d.derive(c.getChild(0), varIndex), d.derive(c.getChild(1), varIndex));
    }
}
