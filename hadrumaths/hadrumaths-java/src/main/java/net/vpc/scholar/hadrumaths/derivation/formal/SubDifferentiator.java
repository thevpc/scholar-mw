package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.Sub;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class SubDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Sub c = (Sub) f;
        return new Sub(d.derive(c.getFirst(),varIndex),d.derive(c.getSecond(),varIndex));
    }
}
