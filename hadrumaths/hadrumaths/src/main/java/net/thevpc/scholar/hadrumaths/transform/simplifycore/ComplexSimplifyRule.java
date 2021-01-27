/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.DoubleExpr;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ComplexSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ComplexSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Complex.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Complex ee = (Complex) e;
        if (ee.isReal()) {
            if (targetExprType != null) {
                switch (targetExprType) {
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return RewriteResult.unmodified();
                    }
                }
            }
            return RewriteResult.bestEffort(DoubleExpr.of(ee.getReal()));
        }
        return RewriteResult.unmodified();
    }

}
