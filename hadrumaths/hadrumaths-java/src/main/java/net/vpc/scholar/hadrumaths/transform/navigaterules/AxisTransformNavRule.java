/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.AxisTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class AxisTransformNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new AxisTransformNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{AxisTransform.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        AxisTransform f = (AxisTransform) e;
        RewriteResult ee = ruleset.rewrite(f.getExpression());
        if(ee.isUnmodified()){
            return RewriteResult.unmodified(e);
        }
        return RewriteResult.bestEffort(new AxisTransform(ee.getValue(),f.getAxis(),f.getDomainDimension()));
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
