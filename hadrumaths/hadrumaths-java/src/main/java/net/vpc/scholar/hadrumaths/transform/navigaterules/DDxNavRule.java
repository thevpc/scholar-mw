/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DDx;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DDxNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DDxNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DDx.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DDx x = (DDx) e;
        DoubleToDouble a = x.getArg();
        RewriteResult newArg = ruleset.rewrite(a);
        if (newArg.isUnmodified()) {
            return RewriteResult.unmodified(e);
        } else {
            Expr x2 = new DDx(newArg.getValue().toDD(), x.getDefaultY(), x.getDefaultY());
            if (newArg.isBestEffort()) {
                return RewriteResult.bestEffort(x2);
            }
            return RewriteResult.newVal(x2);
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
