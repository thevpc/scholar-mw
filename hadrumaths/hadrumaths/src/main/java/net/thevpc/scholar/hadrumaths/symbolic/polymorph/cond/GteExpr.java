package net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/30/14.
 */
public class GteExpr extends AbstractComparatorExpr {
    private static final long serialVersionUID = 1L;

    private GteExpr(DoubleToDouble xarg, DoubleToDouble yarg) {
        super(xarg, yarg, xarg.getDomain().intersect(yarg.getDomain()));
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Gte");
    }


    @Override
    public String getOperatorName() {
        return ">=";
    }

    @Override
    public Expr newInstance(Expr[] xarguments) {
        return GteExpr.of(xarguments[0].toDD(), xarguments[1].toDD());
    }

    public static GteExpr of(DoubleToDouble a, DoubleToDouble b) {
        return new GteExpr(a.toDD(), b.toDD());
    }

    public int compare(double a, double b) {
        return a >= b ? 1 : 0;
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(getChild(0).toLatex()).append("}");
        sb.append("\\geq");
        sb.append("{").append(getChild(1).toLatex()).append("}");
        return sb.toString();
    }

}
