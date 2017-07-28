/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.AbstractExprPropertyAware;
import net.vpc.scholar.hadrumaths.symbolic.DCxy;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DCxyNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DCxyNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DCxy.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DCxy ee = (DCxy) e;
        RewriteResult rr = ruleset.rewrite(ee.getReal());
        RewriteResult ii = ruleset.rewrite(ee.getImag());
        if (!rr.isRewritten() && !ii.isRewritten()) {
            return RewriteResult.unmodified(e);
        }
        DoubleToDouble real = rr.getValue().toDD();
        DoubleToDouble imag = ii.getValue().toDD();
//        if (!real.equals(ee.getReal()) || !(imag.equals(ee.getImag()))) {
        Expr e2 = Maths.complex(real, imag);
        e2 = AbstractExprPropertyAware.copyProperties(e, e2);
        if (rr.isBestEffort() && ii.isBestEffort()) {
            return RewriteResult.bestEffort(e2);
        }
        return RewriteResult.newVal(e2);
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
