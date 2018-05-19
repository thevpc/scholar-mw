/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.symbolic.XX;
import net.vpc.scholar.hadrumaths.symbolic.YY;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ToDDxyLinearRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ToDDxyLinearRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{XX.class, YY.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (e instanceof XX) {
            return RewriteResult.bestEffort(new Linear(1, 0, 0, ((XX) e).getDomain()));
        } else if (e instanceof YY) {
            return RewriteResult.bestEffort(new Linear(0, 1, 0, ((YY) e).getDomain()));
        }
        return RewriteResult.unmodified(e);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

}
