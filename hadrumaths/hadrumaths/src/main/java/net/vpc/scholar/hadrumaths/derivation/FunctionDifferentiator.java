package net.vpc.scholar.hadrumaths.derivation;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 09:27:01
 */
public interface FunctionDifferentiator {
    Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d);
}
