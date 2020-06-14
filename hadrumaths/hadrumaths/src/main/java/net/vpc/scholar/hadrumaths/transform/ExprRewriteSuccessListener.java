package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public interface ExprRewriteSuccessListener {
    void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort);
}
