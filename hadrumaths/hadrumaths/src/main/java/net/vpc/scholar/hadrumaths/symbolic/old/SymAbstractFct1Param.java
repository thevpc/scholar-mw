package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.old.symop.SymOpMul;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public abstract class SymAbstractFct1Param extends SymFunction {
    private final SymExpression value;

    public SymAbstractFct1Param(String name, SymExpression value) {
        super(name);
        this.value = value;
    }

    public SymExpression diff(String varName) {
        return new SymOpMul(getValue().diff(varName).simplify(), diff()).simplify();
    }

    public SymExpression eval(SymContext context) {
        SymExpression l = getValue().eval(context);
        if (l instanceof SymComplex) {
            return new SymComplex(eval(((SymComplex) l).getValue()));
        } else if (l instanceof SymMatrix) {
            SymExpression[][] le = ((SymMatrix) l).getValue();
            int maxr = ((SymMatrix) l).getRows();
            int maxc = ((SymMatrix) l).getColumns();
            SymExpression[][] ee = new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j] = newInstance(le[i][j]);
                }
            }
            return new SymMatrix(ee).eval(context);
        }
        return newInstance(l);
    }

    protected abstract Complex eval(Complex value);

    protected SymAbstractFct1Param newInstance(SymExpression value) {
        try {
            return getClass().getConstructor(SymExpression.class).newInstance(value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.NO_PRECEDENCE);
        String s = getValue().toString(c);
        return (SymUtils.formatRow(null, getName() + "(", s, ")"));
    }

    public SymExpression getValue() {
        return value;
    }

    protected abstract SymExpression diff();

}