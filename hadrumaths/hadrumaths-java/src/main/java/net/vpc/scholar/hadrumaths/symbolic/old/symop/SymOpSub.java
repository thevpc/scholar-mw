package net.vpc.scholar.hadrumaths.symbolic.old.symop;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.old.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpSub extends SymOperator {
    private SymExpression left;
    private SymExpression right;

    public SymOpSub(SymExpression left, SymExpression right) {
        this.left = left;
        this.right = right;
    }

    public SymExpression diff(String varName) {
        return new SymOpSub(left.diff(varName), right.diff(varName));
    }

    public SymExpression eval(SymContext context) {
        SymExpression l = left.eval(context);
        SymExpression r = right.eval(context);
        if (l instanceof SymComplex) {
            SymComplex ll = (SymComplex) l;
            if (ll.getValue().equals(Complex.ZERO)) {
                return new SymOpNeg(r).eval(context);
            }
        }
        if (r instanceof SymComplex) {
            SymComplex rr = (SymComplex) r;
            if (rr.getValue().equals(Complex.ZERO)) {
                return r.eval(context);
            }
        }
        if (l instanceof SymComplex && r instanceof SymComplex) {
            return new SymComplex(((SymComplex) l).getValue().sub(((SymComplex) r).getValue()));
        } else if (l instanceof SymMatrix && r instanceof SymMatrix) {
            SymExpression[][] le = ((SymMatrix) l).getValue();
            SymExpression[][] re = ((SymMatrix) r).getValue();
            int maxr = Math.max(((SymMatrix) l).getRows(), ((SymMatrix) r).getRows());
            int maxc = Math.max(((SymMatrix) l).getColumns(), ((SymMatrix) r).getColumns());
            SymExpression[][] ee = new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j] = new SymOpSub(le[i][j], re[i][j]).eval(context);
                }
            }
            return new SymMatrix(ee);
        }
        return new SymOpSub(l, r);
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.ADD_PRECEDENCE);
        String s = SymUtils.formatRow(null, left.toString(c), "-", right.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.ADD_PRECEDENCE);
        return par ? SymUtils.formatRow(null, "(", s, ")") : s;
    }

}