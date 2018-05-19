package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class ExprRewriteCounter implements ExprRewriteListener {
    private int failedInvocationCount;
    private int succeededInvocationCount;

    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        failedInvocationCount++;
    }

    @Override
    public void onRewriteSuccessExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
        succeededInvocationCount++;
    }

    public int getFailedInvocationCount() {
        return failedInvocationCount;
    }

    public int getSucceededInvocationCount() {
        return succeededInvocationCount;
    }

    @Override
    public String toString() {
        return "ExprRewriteCounter{" +
                 " succeededInvocationCount=" + succeededInvocationCount +
                ", failedInvocationCount=" + failedInvocationCount +
                ", totalCount=" + (succeededInvocationCount+failedInvocationCount) +
                '}';
    }
}
