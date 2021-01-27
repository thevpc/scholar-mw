package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.List;

import static net.thevpc.scholar.hadrumaths.Maths.add;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class PlusDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Plus c = (Plus) f;
        List<Expr> subExpressions = c.getChildren();
        Expr[] e2 = new Expr[subExpressions.size()];
        for (int i = 0; i < e2.length; i++) {
            e2[i] = d.derive(subExpressions.get(i), varIndex);
        }
        return add(e2);
    }
}
