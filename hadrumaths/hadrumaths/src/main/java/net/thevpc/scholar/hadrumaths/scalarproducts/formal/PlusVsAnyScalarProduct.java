package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class PlusVsAnyScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        Plus n = (Plus) f1.toDD();//just for check
        double d = 0;
        for (Expr expression : n.getChildren()) {
            d += sp.evalDD(domain, expression.toDD(), f2);
        }
        return d;
    }
}