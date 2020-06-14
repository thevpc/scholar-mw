/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Inv;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

//import net.vpc.scholar.math.functions.dfxy.DDxyDiscrete;

/**
 * @author vpc
 */
public class InvSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new InvSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Inv.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Inv)) {
            return null;
        }

        Inv ee = (Inv) e;
        RewriteResult invExpr = ruleset.rewrite(ee.getChild(0), targetExprType);

        Expr value = invExpr.isUnmodified() ? ee.getChild(0) : invExpr.getValue();
        Expr ac0 = Expressions.toConstantExprOrNull(value);
        if (ac0 != null) {
            return RewriteResult.bestEffort(new DefaultComplexValue(ac0.toComplex().inv(), ac0.getDomain()));
        }
        if (value instanceof Inv) {
            return RewriteResult.bestEffort(value.getChild(0));
        } else if (value.isNarrow(ExprType.DOUBLE_COMPLEX) && value.toDC() instanceof DefaultComplexValue) {
            DefaultComplexValue v = (DefaultComplexValue) value.toDC();
            return RewriteResult.bestEffort(new DefaultComplexValue(v.getValue().inv(), v.getDomain()));
        } else if (value instanceof DDiscrete) {
            DDiscrete f = (DDiscrete) value;
            return RewriteResult.bestEffort(f.inv());
        }
        if (invExpr.isUnmodified()) {
            return RewriteResult.unmodified();
        }
        return RewriteResult.bestEffort(Neg.of(value));
    }


}
