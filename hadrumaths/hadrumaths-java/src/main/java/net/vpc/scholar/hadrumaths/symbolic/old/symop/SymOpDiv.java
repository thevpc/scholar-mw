package net.vpc.scholar.hadrumaths.symbolic.old.symop;

import net.vpc.scholar.hadrumaths.symbolic.old.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpDiv extends SymOperator {
    public static final SymUtils.BoxFormat DIV_FORMAT = new SymUtils.BoxFormat().setRowSeparator("-");
    private final SymExpression left;
    private final SymExpression right;

    public SymOpDiv(SymExpression left, SymExpression right) {
        this.left = left;
        this.right = right;
    }

    public SymExpression diff(String varName) {
        return new SymOpAdd(new SymOpMul(left, new SymOpInv(right).diff(varName)), new SymOpMul(left.diff(varName), right));
    }

    public SymExpression eval(SymContext context) {
        SymExpression l = left.eval(context);
        SymExpression r = right.eval(context);
        if (l instanceof SymComplex && r instanceof SymComplex) {
            return new SymComplex(((SymComplex) l).getValue().mul(((SymComplex) r).getValue()));
        } else if (!(l instanceof SymMatrix) && r instanceof SymMatrix) {
            SymExpression[][] re = ((SymMatrix) r).getValue();
            int maxr = ((SymMatrix) r).getRows();
            int maxc = ((SymMatrix) r).getColumns();
            SymExpression[][] ee = new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j] = new SymOpMul(l, re[i][j]).eval(context);
                }
            }
            return new SymMatrix(ee);
        } else if (!(r instanceof SymMatrix) && l instanceof SymMatrix) {
            SymExpression[][] le = ((SymMatrix) l).getValue();
            int maxr = ((SymMatrix) l).getRows();
            int maxc = ((SymMatrix) l).getColumns();
            SymExpression[][] ee = new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j] = new SymOpMul(le[i][j], l).eval(context);
                }
            }
            return new SymMatrix(ee);
        }
        return new SymOpDiv(l, r);
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.MUL_PRECEDENCE);
        String s = SymUtils.formatColumn(DIV_FORMAT, left.toString(c), right.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.MUL_PRECEDENCE);
        return par ? SymUtils.formatRow(null, "(", s, ")") : s;
    }

}