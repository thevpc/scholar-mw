package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class DoubleToVectorDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis axis, FunctionDifferentiatorManager d) {
        DoubleToVector g = (DoubleToVector) f;
        return Maths.vector(
                d.derive(g.getComponent(Axis.X), axis),
                d.derive(g.getComponent(Axis.Y), axis));
    }
}
