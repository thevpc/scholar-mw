package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.Plus;

import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class PlusDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Plus c = (Plus) f;
        List<Expr> subExpressions = c.getSubExpressions();
        Expr[] e2=new Expr[subExpressions.size()];
        for (int i = 0; i < e2.length; i++) {
            e2[i]=d.derive(subExpressions.get(i),varIndex);
        }
        return new Plus(e2);
    }
}
