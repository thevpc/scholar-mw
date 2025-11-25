package net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by vpc on 4/30/14.
 */
public class LteExpr extends AbstractComparatorExpr {
    private static final long serialVersionUID = 1L;

    private LteExpr(DoubleToDouble xarg, DoubleToDouble yarg) {
        super(xarg, yarg, xarg.getDomain().intersect(yarg.getDomain()));
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("LteExpr");
    }


    @Override
    public String getOperatorName() {
        return "<=";
    }

    @Override
    public Expr newInstance(Expr[] xarguments) {
        return LteExpr.of(xarguments[0].toDD(), xarguments[1].toDD());
    }

    public static LteExpr of(DoubleToDouble a, DoubleToDouble b) {
        return new LteExpr(a.toDD(), b.toDD());
    }

    public int compare(double a, double b) {
        return a <= b ? 1 : 0;
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(getChild(0).toLatex()).append("}");
        sb.append("\\leq");
        sb.append("{").append(getChild(1).toLatex()).append("}");
        return sb.toString();
    }


}
