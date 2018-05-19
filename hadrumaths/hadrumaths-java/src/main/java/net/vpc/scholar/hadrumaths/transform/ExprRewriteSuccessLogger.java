package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class ExprRewriteSuccessLogger implements ExprRewriteSuccessListener {
    private long invocationCount;

    @Override
    public void onRewriteSuccessExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
        invocationCount++;
    }

    public long getInvocationCount() {
        return invocationCount;
    }
}
