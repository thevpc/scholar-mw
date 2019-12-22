/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.Reminder;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ReminderNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ReminderNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Reminder.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Reminder ee = (Reminder) e;
        RewriteResult a = ruleset.rewrite(ee.getFirst());
        RewriteResult b = ruleset.rewrite(ee.getSecond());
        if (a.isUnmodified() && b.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }

        Expr e2 = MathsBase.rem(a.getValue(), b.getValue());
        e2 = Any.copyProperties(e, e2);
        return (a.isBestEffort() && b.isBestEffort()) ? RewriteResult.bestEffort(e2) : RewriteResult.newVal(e2);
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