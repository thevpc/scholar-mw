package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;

import static net.vpc.scholar.hadrumaths.Maths.*;

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
