package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class ExprRewriteLogger implements ExprRewriteListener {
    private long invocationCount;

    @Override
    public void onRewriteExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
        invocationCount++;
//        System.out.println(rewriter+" [#"+invocationCount+"]:: "+oldValue.getClass().getSimpleName()+"->"+newValue.getClass().getSimpleName()+"  :: "+oldValue+"\n\t\t"+newValue+" :: "+oldValue.equals(newValue));
    }

    public long getInvocationCount() {
        return invocationCount;
    }
}
