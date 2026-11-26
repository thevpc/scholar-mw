package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class PowDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr e, Axis varIndex, FunctionDifferentiatorManager d) {
        Pow c = (Pow) e;
        Expr f = c.getFirst();
        Expr g = c.getSecond();
        Expr fd = d.derive(f, varIndex);
        Expr gd = d.derive(g, varIndex);

        if (g.isNarrow(net.thevpc.scholar.hadrumaths.symbolic.ExprType.DOUBLE_NBR)) {
            // constant exponent: g * f^(g-1) * f'  (the log form below is singular at f=0)
            double gv = g.toDouble();
            return prod(Maths.expr(gv), pow(f, Maths.expr(gv - 1.0)), fd);
        }

        return mul(
                pow(f, g),
                add(
                        div(
                                mul(fd, g),
                                f
                        )
                        ,
                        mul(gd, Maths.log(f))
                )

        );
    }
}
