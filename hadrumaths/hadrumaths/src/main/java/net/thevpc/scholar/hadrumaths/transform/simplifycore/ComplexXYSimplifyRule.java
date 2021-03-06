/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ComplexXYSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ComplexXYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DefaultComplexValue.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        DefaultComplexValue ee = (DefaultComplexValue) e;
        if (ee.getDomain().isUnbounded()) {
            return RewriteResult.bestEffort(ee.getValue());
        }
        return RewriteResult.unmodified();
    }

}
