package net.vpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/30/14.
 */
public class OrExpr extends AbstractComparatorExpr {
    private static final long serialVersionUID = 1L;

    private OrExpr(DoubleToDouble xarg, DoubleToDouble yarg) {
        super(xarg, yarg, xarg.getDomain().expand(yarg.getDomain()));
    }

    @Override
    public String getOperatorName() {
        return "||";
    }

    @Override
    public Expr newInstance(Expr[] xarguments) {
        return OrExpr.of(xarguments[0].toDD(), xarguments[1].toDD());
    }

    public static OrExpr of(DoubleToDouble a, DoubleToDouble b) {
        return new OrExpr(a.toDD(), b.toDD());
    }

    public int compare(double a, double b) {
        return (a == 0 && b == 0) ? 0 : 1;
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(getXArgument()).append("}");
        sb.append("\\lor");
        sb.append("{").append(getYArgument()).append("}");
        return sb.toString();
    }

}
