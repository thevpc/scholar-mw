/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.IConstantValue;
import net.vpc.scholar.hadrumaths.symbolic.Neg;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class NegSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new NegSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Neg.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Neg ee = (Neg) e;
        RewriteResult sub = ruleset.rewrite(ee.getExpression());
        IConstantValue ac0 = Expressions.toComplexValue(sub.getValue());
        if (ac0 != null) {
            return RewriteResult.bestEffort(new ComplexValue(ac0.getComplexConstant().neg(), ac0.getDomain()));
        }
        if (sub.getValue() instanceof Neg) {
            return RewriteResult.newVal(((Neg)sub.getValue()).getExpression());
        }
        return RewriteResult.newVal(Maths.mul(sub.getValue(), Complex.MINUS_ONE));
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
