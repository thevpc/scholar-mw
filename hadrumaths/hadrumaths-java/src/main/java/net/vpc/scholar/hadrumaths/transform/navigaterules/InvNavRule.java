/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.symbolic.AbstractExprPropertyAware;
import net.vpc.scholar.hadrumaths.symbolic.Inv;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class InvNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new InvNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Inv.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Inv ee = (Inv) e;
        RewriteResult a = ruleset.rewrite(ee.getExpression());
        if(a.isRewritten()){
            Expr eee = Maths.inv(a.getValue());
            eee= AbstractExprPropertyAware.copyProperties(e, eee);
            if(a.isBestEffort()){
                return RewriteResult.bestEffort(eee);
            }
            return RewriteResult.newVal(eee);
        }
        return RewriteResult.unmodified(e);
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
