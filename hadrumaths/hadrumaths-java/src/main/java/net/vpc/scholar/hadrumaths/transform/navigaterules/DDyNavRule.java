/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.symbolic.AbstractExprPropertyAware;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DDy;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class DDyNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DDyNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DDy.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DDy ee = (DDy) e;
        DoubleToDouble base = ee.getArg();
        RewriteResult a = ruleset.rewrite(base);
        if(a.isRewritten()){
            Expr e2 = new DDy(a.getValue().toDD(),ee.getDefaultX(),ee.getDefaultZ());
            e2= AbstractExprPropertyAware.copyProperties(e, e2);
            return RewriteResult.bestEffort(e2);
        }
        return RewriteResult.unmodified(e);
    }
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }

}
