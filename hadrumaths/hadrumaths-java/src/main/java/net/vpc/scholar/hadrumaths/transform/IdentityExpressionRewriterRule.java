package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class IdentityExpressionRewriterRule implements ExpressionRewriterRule{
    public static final IdentityExpressionRewriterRule INSTANCE=new IdentityExpressionRewriterRule();
    private static Class[] ALL = {Expr.class};
    @Override
    public Class<? extends Expr>[] getTypes() {
        return ALL;
    }

    @Override
    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        return RewriteResult.unmodified(e);
    }
}
