package net.vpc.scholar.hadrumaths.symbolic.old.symexprerssions;

import net.vpc.scholar.hadrumaths.symbolic.old.*;
import net.vpc.scholar.hadrumaths.symbolic.old.symop.SymOpSub;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymRot extends SymFunction {
    private SymExpression value;
    private String x;
    private String y;
    private String z;

    public SymRot(SymExpression vector) {
        this(vector, "x", "y", "z");
    }

    public SymRot(SymExpression value, String x, String y, String z) {
        super("rot");
        this.value=value;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SymExpression diff(String varName) {
        SymExpression e = simplify();
        return e.diff(varName);
    }

    public SymExpression eval(SymContext context) {
        SymExpression l = getValue().eval(context);
        if (l instanceof SymMatrix) {
            SymMatrix m = (SymMatrix) l;
            if (m.getRows() == 3 && m.getColumns() == 1) {
                SymExpression[][] v = m.getValue();
                SymExpression fx = v[0][0];
                SymExpression fy = v[1][0];
                SymExpression fz = v[2][0];
                return new SymMatrix(
                        new SymExpression[][]{
                                {new SymOpSub(fz.diff(y), fy.diff(z)).eval(context)},
                                {new SymOpSub(fx.diff(z), fz.diff(x)).eval(context)},
                                {new SymOpSub(fy.diff(x), fx.diff(y)).eval(context)}
                        }
                ).eval(context);
            }
        }
        return new SymRot(l);
    }

    public SymExpression getValue() {
        return value;
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.NO_PRECEDENCE);
        return "Rot("+value.toString(c)+")";
    }
}