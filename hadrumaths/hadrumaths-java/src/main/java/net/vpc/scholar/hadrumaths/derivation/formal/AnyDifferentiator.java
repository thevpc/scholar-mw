package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.Any;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class AnyDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Any c = (Any) f;
        return d.derive(c.getObject(),varIndex);
    }
}
