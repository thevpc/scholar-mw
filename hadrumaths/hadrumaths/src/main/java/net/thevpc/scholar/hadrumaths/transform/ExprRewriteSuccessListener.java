package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;

public interface ExprRewriteSuccessListener {
    void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort);
}
