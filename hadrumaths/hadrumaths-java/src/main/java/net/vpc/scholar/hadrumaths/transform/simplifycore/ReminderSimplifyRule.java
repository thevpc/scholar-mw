/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Reminder;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ReminderSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ReminderSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Reminder.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Reminder)) {
            return null;
        }

        Reminder ee = (Reminder) e;
        Expr first = ee.getFirst();
        Expr second = ee.getSecond();
        RewriteResult ar = ruleset.rewrite(first, targetExprType);
        RewriteResult br = ruleset.rewrite(second, targetExprType);

        Expr arv = ar.isUnmodified() ? first : ar.getValue();
        Expr brv = br.isUnmodified() ? second : br.getValue();

        Expr ac0 = Expressions.toConstantExprOrNull(arv);
        Expr bc0 = Expressions.toConstantExprOrNull(brv);
        if (ac0 != null && bc0 != null) {
            return RewriteResult.newVal(new DefaultComplexValue(ac0.toComplex().rem(bc0.toComplex()), ac0.getDomain().intersect(bc0.getDomain())));
        }
        if (!ar.isRewritten() && !br.isRewritten()) {
            return RewriteResult.unmodified();
        }

        Expr eee = Maths.rem(arv, brv);
        eee = ExprDefaults.copyProperties(e, eee);
        return (ar.isBestEffort() && br.isBestEffort()) ? RewriteResult.bestEffort(eee) : RewriteResult.newVal(eee);
    }


}
