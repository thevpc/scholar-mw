package net.vpc.scholar.hadrumaths.symbolic.old.symop;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.old.*;

import static net.vpc.scholar.hadrumaths.Complex.ZERO;
import static net.vpc.scholar.hadrumaths.Complex.ONE;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpNeg extends SymOperator {
    private SymExpression value;

    public SymOpNeg(SymExpression left) {
        this.value = left;
    }

    public SymExpression diff(String varName) {
        return new SymOpNeg(value.diff(varName));
    }

    public SymExpression eval(SymContext context) {
        SymExpression r=value.eval(context);
        if(r instanceof SymOpNeg){
            return ((SymOpNeg)r).getValue().eval(context);
        }else if(r instanceof SymComplex){
            SymComplex rr=(SymComplex) r;
            if(rr.getValue().equals(ZERO)){
                return SymComplex.XZERO;
            }else if(rr.getValue().equals(ONE)){
                return new SymComplex(Complex.MINUS_ONE);
            }
        }
        if(r instanceof SymMatrix){
            SymExpression[][] re = ((SymMatrix) r).getValue();
            int maxr=((SymMatrix) r).getRows();
            int maxc=((SymMatrix) r).getColumns();
            SymExpression[][] ee=new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j]=new SymOpNeg(re[i][j]).eval(context);
                }
            }
            return new SymMatrix(ee);
        }
        return new SymOpNeg(r);
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.UNIT_MINUS_PRECEDENCE);

        String s = SymUtils.formatRow(null,"-", value.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.UNIT_MINUS_PRECEDENCE);
        return par ? SymUtils.formatRow(null,"(",s,")") : s;
    }

    public SymExpression getValue() {
        return value;
    }
}