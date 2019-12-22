/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.Plus;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.List;

/**
 * @author vpc
 */
public class PlusNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new PlusNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Plus.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Plus ee = (Plus) e;
        List<Expr> expressions = ee.getSubExpressions();
        int size = expressions.size();
        Expr[] updated = new Expr[size];
        boolean changed = false;
        int bestEfforts = 0;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = expressions.get(i);
            RewriteResult s2 = ruleset.rewrite(s1);
            if (s2.isRewritten()) {//!s1.equals(s2)
                changed = true;
                updated[i] = s2.getValue();
                if (s2.isBestEffort()) {
                    bestEfforts++;
                }
            } else {
                bestEfforts++;
                updated[i] = s1;
            }
        }
        if (changed) {
            Expr e2 = MathsBase.sum(updated);
            e2 = Any.copyProperties(e, e2);
            return bestEfforts == size ? RewriteResult.bestEffort(e2) : RewriteResult.newVal(e2);
        }
        return RewriteResult.unmodified(e);
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
