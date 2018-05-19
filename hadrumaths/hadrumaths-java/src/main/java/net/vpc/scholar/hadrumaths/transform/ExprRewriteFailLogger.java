package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class ExprRewriteFailLogger implements ExprRewriteFailListener {
    private long invocationCount;

    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        invocationCount++;
//        System.out.println(rewriter+" [#"+invocationCount+"]:: "+oldValue.getClass().getSimpleName()+"->UNMODIFIED"+"  :: "+oldValue);
    }

    public long getInvocationCount() {
        return invocationCount;
    }
}
