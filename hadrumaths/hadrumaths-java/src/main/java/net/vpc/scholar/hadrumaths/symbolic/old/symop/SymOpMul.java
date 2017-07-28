package net.vpc.scholar.hadrumaths.symbolic.old.symop;

import net.vpc.scholar.hadrumaths.symbolic.old.*;

import static net.vpc.scholar.hadrumaths.Complex.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymOpMul extends SymOperator {
    private SymExpression left;
    private SymExpression right;

    public SymOpMul(SymExpression left, SymExpression right) {
        this.left = left;
        this.right = right;
    }

    public SymExpression diff(String varName) {
        return new SymOpAdd(new SymOpMul(left,right.diff(varName)).simplify(), new SymOpMul(left.diff(varName),right).simplify()).simplify();
    }

    public SymExpression eval(SymContext context) {
        SymExpression l=left.eval(context);
        SymExpression r=right.eval(context);
        if(l instanceof SymComplex){
            SymComplex ll=(SymComplex) l;
            if(r instanceof SymComplex){
                SymComplex rr=(SymComplex) r;
                return new SymComplex(ll.getValue().mul(rr.getValue()));
            }else{
                if(ll.getValue().equals(ZERO)){
                    return SymComplex.XZERO;
                }else if(ll.getValue().equals(ONE)){
                    return r;
                }
            }
        }
        if(r instanceof SymComplex){
            SymComplex rr=(SymComplex) r;
            if(rr.getValue().equals(ZERO)){
                return SymComplex.XZERO;
            }else if(rr.getValue().equals(ONE)){
                return l;
            }
        }
        if(!(l instanceof SymMatrix) && r instanceof SymMatrix){
            SymExpression[][] re = ((SymMatrix) r).getValue();
            int maxr=((SymMatrix) r).getRows();
            int maxc=((SymMatrix) r).getColumns();
            SymExpression[][] ee=new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j]=new SymOpMul(l,re[i][j]).eval(context);
                }
            }
            return new SymMatrix(ee);
        }else if(!(r instanceof SymMatrix) && l instanceof SymMatrix){
            SymExpression[][] le = ((SymMatrix) l).getValue();
            int maxr=((SymMatrix) l).getRows();
            int maxc=((SymMatrix) l).getColumns();
            SymExpression[][] ee=new SymExpression[maxr][maxc];
            for (int i = 0; i < ee.length; i++) {
                SymExpression[] symExpressions = ee[i];
                for (int j = 0; j < symExpressions.length; j++) {
                    symExpressions[j]=new SymOpMul(le[i][j],l).eval(context);
                }
            }
            return new SymMatrix(ee);
        }
        return new SymOpMul(l,r);
    }

    public String toString(SymStringContext context) {
        SymStringContext c = context.clone();
        c.setPreferParentheses(false);
        c.setOperatorPrecedence(SymStringContext.MUL_PRECEDENCE);
        String s = SymUtils.formatRow(null,left.toString(c), "*", right.toString(c));
        boolean par = context.isPreferParentheses(SymStringContext.MUL_PRECEDENCE);
        return par ? SymUtils.formatRow(null,"(",s,")") : s;
    }
    
}