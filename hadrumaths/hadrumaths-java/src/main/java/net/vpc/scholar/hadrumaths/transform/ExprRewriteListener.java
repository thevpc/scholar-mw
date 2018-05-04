package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public interface ExprRewriteListener {
    void onRewriteExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue);
}
