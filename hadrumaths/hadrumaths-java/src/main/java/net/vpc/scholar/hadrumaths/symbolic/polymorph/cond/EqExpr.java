package net.vpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/30/14.
 */
public class EqExpr extends AbstractComparatorExpr {
    private static final long serialVersionUID = 1L;

    private EqExpr(DoubleToDouble xarg, DoubleToDouble yarg) {
        super(xarg, yarg, xarg.getDomain().intersect(yarg.getDomain()));
    }

    @Override
    public String getOperatorName() {
        return "==";
    }

    @Override
    public Expr newInstance(Expr... xargument) {
        return EqExpr.of(xargument[0].toDD(), xargument[1].toDD());
    }

    public static EqExpr of(DoubleToDouble a, DoubleToDouble b) {
        return new EqExpr(a.toDD(), b.toDD());
    }

    public int compare(double a, double b) {
        return a == b ? 1 : 0;
    }

}
