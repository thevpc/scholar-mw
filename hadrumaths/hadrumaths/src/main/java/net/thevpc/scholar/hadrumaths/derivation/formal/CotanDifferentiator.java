package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Cotan;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class CotanDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Cotan c = (Cotan) f;
        Expr a = c.getChild(0);
        return
                neg(mul(
                        d.derive(a, varIndex)
                        ,
                        add(
                                Complex.ONE,
                                sqr(c)
                        )
                ))
                ;
    }
}
