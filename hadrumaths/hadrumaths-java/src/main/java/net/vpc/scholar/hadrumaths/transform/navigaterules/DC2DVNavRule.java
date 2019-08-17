/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DefaultDoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DV;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DC2DVNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DC2DVNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DC2DV.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DC2DV r = (DC2DV) e;
        DoubleToComplex s = null;
        boolean newVal = false;
        boolean bestVal = false;
        DoubleToComplex i = (DoubleToComplex) r.getBaseExpr();
        RewriteResult s2 = ruleset.rewrite(i);
        if (s2.isUnmodified()) {
            s = i;
        } else if (s2.isBestEffort()) {
            s = s2.getValue().toDC();
            bestVal = true;
        } else {
            s = s2.getValue().toDC();
            newVal = true;
        }

        if (newVal) {
            return RewriteResult.newVal(new DC2DV(s));
        } else if (bestVal) {
            return RewriteResult.bestEffort(new DC2DV(s));
        } else {
            return RewriteResult.unmodified(r);
        }
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
