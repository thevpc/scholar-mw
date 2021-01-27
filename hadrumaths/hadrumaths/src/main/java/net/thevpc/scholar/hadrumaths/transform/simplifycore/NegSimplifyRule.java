/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Expressions;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class NegSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new NegSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Neg.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Neg ee = (Neg) e;
        RewriteResult sub = ruleset.rewrite(ee.getChild(0), targetExprType);
        Expr value = sub.isUnmodified() ? ee.getChild(0) : sub.getValue();
        Expr ac0 = Expressions.toConstantExprOrNull(value);
        if (ac0 != null) {
            return RewriteResult.bestEffort(DefaultComplexValue.of(ac0.toComplex().neg(), ac0.getDomain()));
        }
        if (value instanceof Neg) {
            return RewriteResult.bestEffort(value.getChild(0));
        }
        return RewriteResult.bestEffort(ruleset.rewriteOrSame(Maths.mul(value, -1),targetExprType));
    }


}
