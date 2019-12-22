/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.IConstantValue;
import net.vpc.scholar.hadrumaths.symbolic.Reminder;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ReminderSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ReminderSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Reminder.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (!(e instanceof Reminder)) {
            return null;
        }

        Reminder ee = (Reminder) e;
        Expr first = ee.getFirst();
        Expr second = ee.getSecond();
        RewriteResult ar = ruleset.rewrite(first);
        RewriteResult br = ruleset.rewrite(second);

        IConstantValue ac0 = Expressions.toComplexValue(ar.getValue());
        IConstantValue bc0 = Expressions.toComplexValue(br.getValue());
        if (ac0 != null && bc0 != null) {
            return RewriteResult.newVal(new ComplexValue(ac0.getComplexConstant().rem(bc0.getComplexConstant()), ac0.getDomain().intersect(bc0.getDomain())));
        }
        if (!ar.isRewritten() && !br.isRewritten()) {
            return RewriteResult.unmodified(e);
        }

        Expr eee = MathsBase.rem(ar.getValue(), br.getValue());
        eee = Any.copyProperties(e, eee);
        return (ar.isBestEffort() && br.isBestEffort()) ? RewriteResult.bestEffort(eee) : RewriteResult.newVal(eee);
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
