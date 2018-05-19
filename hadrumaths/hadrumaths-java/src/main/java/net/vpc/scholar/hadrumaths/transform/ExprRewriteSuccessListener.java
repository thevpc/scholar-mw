package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public interface ExprRewriteSuccessListener {
    void onRewriteSuccessExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue);
}
