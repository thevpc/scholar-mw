package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.Plus;


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
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        Plus n = (Plus) f1.toDD();//just for check
        double d = 0;
        for (Expr expression : n.getSubExpressions()) {
            d += sp.evalDD(domain, expression.toDD(), f2);
        }
        return d;
    }
}