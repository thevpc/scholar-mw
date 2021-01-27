package net.thevpc.scholar.hadrumaths.symbolic.old.symop;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.symbolic.old.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpAdd extends SymOperator {
    private final SymExpression left;
    private final SymExpression right;

    public SymOpAdd(SymExpression left, SymExpression right) {
        this.left = left;
        this.right = right;
    }

    public SymExpression diff(String varName) {
        return new SymOpAdd(left.diff(varName).simplify(), right.diff(varName).simplify()).simplify();
    }

    public SymExpression eval(SymContext context) {
        SymExpression l = left.eval(context);
        SymExpression r = right.eval(context);
        if (l instanceof SymComplex) {
            SymComplex ll = (SymComplex) l;
            if (ll.getValue().equals(Complex.ZERO)) {
                return r;
            }
        }
        if (r instanceof SymComplex) {
            SymComplex rr = (SymComplex) r;
            if (rr.getValue().equals(Complex.ZERO)) {
                return l;
            }
        }
        if (l instanceof SymComplex && r instanceof SymComplex) {
            return new SymComplex(((SymComplex) l).getValue().plus(((SymComplex) r).getValue()));
        } else if (l instanceof SymMatrix && r instanceof SymMatrix) {
            SymExpression[][] le = ((SymMatrix) l).getValue();
            SymExpression[][] re = ((SymMatrix) r).getValue();
            int maxr = Math.max(((SymMatrix) l).getRows(), ((SymMatrix) r).getRows());
            int maxc = Math.max(((SymMatrix) l).getColumns(), ((SymMatrix) r).getColumns());
            SymExpression[][] ee = new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j] = new SymOpAdd(le[i][j], re[i][j]).eval(context);
                }
            }
            return new SymMatrix(ee);
        }
        if (l != left || r != right) {
            return new SymOpAdd(l, r);
        }
        return this;
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.ADD_PRECEDENCE);
        String s = SymUtils.formatRow(null, left.toString(c), "+", right.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.ADD_PRECEDENCE);
        return par ? SymUtils.formatRow(null, "(", s, ")") : s;
    }

}
