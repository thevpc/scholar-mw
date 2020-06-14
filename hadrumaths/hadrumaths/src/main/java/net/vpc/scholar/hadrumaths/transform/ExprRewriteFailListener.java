package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public interface ExprRewriteFailListener {
    void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue);
}
