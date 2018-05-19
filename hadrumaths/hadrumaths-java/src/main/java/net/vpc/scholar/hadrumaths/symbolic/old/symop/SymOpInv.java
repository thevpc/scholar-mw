package net.vpc.scholar.hadrumaths.symbolic.old.symop;

import net.vpc.scholar.hadrumaths.symbolic.old.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpInv extends SymOperator {
    private SymExpression value;

    public SymOpInv(SymExpression value) {
        this.value = value;
    }

    public SymExpression diff(String varName) {
        return new SymOpNeg(value.diff(varName));
    }

    public SymExpression eval(SymContext context) {
        SymExpression x = value.eval(context);
        if (x instanceof SymComplex) {
            return new SymComplex(((SymComplex) x).getValue().inv());
        }
        if (x == value) {
            return this;
        }
        return new SymOpInv(x);
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.MUL_PRECEDENCE);
        String s = SymUtils.formatColumn(SymOpDiv.DIV_FORMAT, "1", value.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.ADD_PRECEDENCE);
        return par ? SymUtils.formatRow(null, "(", s, ")") : s;
    }

}