package net.thevpc.scholar.hadrumaths.derivation.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class DoubleToVectorDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis axis, FunctionDifferentiatorManager d) {
        DoubleToVector g = f.toDV();
        return Maths.vector(
                d.derive(g.getComponent(Axis.X), axis),
                d.derive(g.getComponent(Axis.Y), axis));
    }
}
