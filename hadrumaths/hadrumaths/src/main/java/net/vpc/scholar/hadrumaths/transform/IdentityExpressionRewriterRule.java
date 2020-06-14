package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

public class IdentityExpressionRewriterRule extends AbstractExpressionRewriterRule {
    public static final IdentityExpressionRewriterRule INSTANCE = new IdentityExpressionRewriterRule();
    private static final Class[] ALL = {Expr.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return ALL;
    }

    @Override
    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        return RewriteResult.unmodified();
    }
}
