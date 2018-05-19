/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.IConstantValue;
import net.vpc.scholar.hadrumaths.symbolic.Inv;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

//import net.vpc.scholar.math.functions.dfxy.DDxyDiscrete;

/**
 * @author vpc
 */
public class InvSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new InvSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Inv.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (!(e instanceof Inv)) {
            return null;
        }

        Inv ee = (Inv) e;
        RewriteResult invExpr = ruleset.rewrite(ee.getExpression());

        IConstantValue ac0 = Expressions.toComplexValue(invExpr.getValue());
        if (ac0 != null) {
            return RewriteResult.bestEffort(new ComplexValue(ac0.getComplexConstant().inv(), ac0.getDomain()));
        }
        if (invExpr.getValue() instanceof Inv) {
            return RewriteResult.bestEffort(((Inv) invExpr.getValue()).getExpression());
        } else if (invExpr.getValue().isDC() && invExpr.getValue().toDC() instanceof ComplexValue) {
            ComplexValue v = (ComplexValue) invExpr.getValue().toDC();
            return RewriteResult.bestEffort(new ComplexValue(v.getValue().inv(), v.getDomain()));
        } else if (invExpr.getValue() instanceof DDiscrete) {
            DDiscrete f = (DDiscrete) invExpr.getValue();
            return RewriteResult.bestEffort(f.inv());
        }
        if (invExpr.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }
        return RewriteResult.bestEffort(new Inv(invExpr.getValue()));
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
