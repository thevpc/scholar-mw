/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.symbolic.AbstractExprPropertyAware;
import net.vpc.scholar.hadrumaths.symbolic.Div;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class DivNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DivNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Div.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Div ee = (Div) e;
        RewriteResult a = ruleset.rewrite(ee.getFirst());
        RewriteResult b = ruleset.rewrite(ee.getSecond());
        if(a.isUnmodified() && b.isUnmodified()){
            return RewriteResult.unmodified(e);
        }

        Expr e2 = Maths.div(a.getValue(), b.getValue());
        e2= AbstractExprPropertyAware.copyProperties(e, e2);
        return (a.isBestEffort()&& b.isBestEffort())?RewriteResult.bestEffort(e2) : RewriteResult.newVal(e2);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }

}
