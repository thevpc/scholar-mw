/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DDxyLinearSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DDxyLinearSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Linear.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Linear f = (Linear) e;
        if (f.getA() == 0 && f.getB() == 0) {
            return RewriteResult.bestEffort(Maths.expr(f.getC(), f.getDomain()));
        }
        return RewriteResult.unmodified();
    }


}
