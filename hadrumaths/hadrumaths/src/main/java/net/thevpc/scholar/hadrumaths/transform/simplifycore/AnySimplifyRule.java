/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprRef;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class AnySimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new AnySimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{ExprRef.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        ExprRef f = (ExprRef) e;
        RewriteResult r = ruleset.rewrite(f.getReference(), targetExprType);
        if (r.isUnmodified()) {
            return RewriteResult.bestEffort(f.getReference());
        }
        return RewriteResult.bestEffort(r.getValue());
    }


}
