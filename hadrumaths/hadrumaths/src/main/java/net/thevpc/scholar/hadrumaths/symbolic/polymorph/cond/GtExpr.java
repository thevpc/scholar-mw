package net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/30/14.
 */
public class GtExpr extends AbstractComparatorExpr {
    private static final long serialVersionUID = 1L;

    private GtExpr(DoubleToDouble xarg, DoubleToDouble yarg) {
        super(xarg, yarg, xarg.getDomain().intersect(yarg.getDomain()));
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("GtExpr");
    }


    @Override
    public String getOperatorName() {
        return ">";
    }

    @Override
    public Expr newInstance(Expr[] xarguments) {
        return GtExpr.of(xarguments[0].toDD(), xarguments[1].toDD());
    }

    public static GtExpr of(DoubleToDouble a, DoubleToDouble b) {
        return new GtExpr(a.toDD(), b.toDD());
    }

    public int compare(double a, double b) {
        return a > b ? 1 : 0;
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(getChild(0).toLatex()).append("}");
        sb.append(">");
        sb.append("{").append(getChild(1).toLatex()).append("}");
        return sb.toString();
    }

}
