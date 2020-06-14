/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DoubleXYSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DoubleXYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DoubleValue.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        DoubleValue f = (DoubleValue) e;
        if (f.getDomain().isUnbounded1()) {
            return RewriteResult.unmodified();
            //return RewriteResult.newVal(Complex.valueOf(f.getValue()));
        }
        //TODO why this condition :: !f.getDomain().isEmpty()
        if (f.isZero() && !f.getDomain().isEmpty() && !f.getDomain().isUnbounded()) {
            return RewriteResult.bestEffort(Maths.DZEROXY);
        }
        return RewriteResult.unmodified();
    }

}
