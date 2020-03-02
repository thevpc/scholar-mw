/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.ExprDim;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class PowSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new PowSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Pow.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Pow ee = (Pow) e;
        RewriteResult a = ruleset.rewrite(ee.getFirst(), targetExprType);
        RewriteResult b = ruleset.rewrite(ee.getSecond(), targetExprType);

        Expr ae = a.isUnmodified() ? ee.getFirst() : a.getValue();
        Expr be = b.isUnmodified() ? ee.getSecond() : b.getValue();

        Expr ac0 = Expressions.toConstantExprOrNull(ae);
        Expr bc0 = Expressions.toConstantExprOrNull(be);
        if (ac0 != null && bc0 != null) {
            return RewriteResult.bestEffort(new DefaultComplexValue(ac0.toComplex().pow(bc0.toComplex()), ac0.getDomain().intersect(bc0.getDomain())));
        }
        if (bc0 != null && bc0.toComplex().isReal()) {
            double value = bc0.toComplex().toDouble();
            if (value == 1) {
                return RewriteResult.newVal(Maths.mul(ae, Maths.expr(1, bc0.getDomain())));
            } else if (value == 0) {
                ExprType ad = ae.getType();
                if (ae.is(ExprDim.SCALAR) || ad == ExprType.DOUBLE_DOUBLE || ad == ExprType.DOUBLE_COMPLEX) {
                    return RewriteResult.newVal(Maths.expr(1, ae.toDD().getDomain().intersect(bc0.getDomain())));
                }
            }
        }
        if (a.isUnmodified() && b.isUnmodified()) {
            return RewriteResult.unmodified();
        }
        Expr e2 = Pow.of(ae, be);
        e2 = ExprDefaults.copyProperties(e, e2);
        if (a.isBestEffort() && b.isBestEffort()) {
            return RewriteResult.bestEffort(e2);
        }
        return RewriteResult.newVal(e2);
    }

}
