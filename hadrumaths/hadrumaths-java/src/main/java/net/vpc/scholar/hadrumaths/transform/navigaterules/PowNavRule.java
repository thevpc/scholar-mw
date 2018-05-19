/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.Pow;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class PowNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new PowNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Pow.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Pow ee = (Pow) e;
        RewriteResult a = ruleset.rewrite(ee.getFirst());
        RewriteResult b = ruleset.rewrite(ee.getSecond());
        if (a.isUnmodified() && b.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }
        Expr eee = Maths.pow(a.getValue(), b.getValue());
        eee = Any.copyProperties(e, eee);
        return RewriteResult.bestEffort(eee);
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
