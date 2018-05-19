/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.Real;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class RealNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new RealNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Real.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Real ee = (Real) e;
        DoubleToComplex base = ee.getArg();
        RewriteResult a = ruleset.rewrite(base);
        if (a.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }
//        if(!a.equals(base)){
        Expr eee = new Real(a.getValue().toDC());
        eee = Any.copyProperties(e, eee);
        return a.isBestEffort() ? RewriteResult.bestEffort(eee) : RewriteResult.newVal(eee);
//        }
//        return e;
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
