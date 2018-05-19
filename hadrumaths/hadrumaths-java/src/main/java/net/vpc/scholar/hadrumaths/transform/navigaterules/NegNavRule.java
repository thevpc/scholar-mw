/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.Neg;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class NegNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new NegNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Neg.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Neg ee = (Neg) e;
        RewriteResult a = ruleset.rewrite(ee.getExpression());
        if (a.isRewritten()) {
            Expr eee = Maths.neg(a.getValue());
            eee = Any.copyProperties(e, eee);
            return a.isBestEffort() ? RewriteResult.bestEffort(eee) : RewriteResult.newVal(eee);
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
