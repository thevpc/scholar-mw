package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;

public interface ExprRewriteFailListener {
    void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue);
}
