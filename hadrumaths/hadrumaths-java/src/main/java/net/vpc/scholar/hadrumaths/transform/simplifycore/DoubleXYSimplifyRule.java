/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class DoubleXYSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DoubleXYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DoubleValue.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DoubleValue f = (DoubleValue) e;
        //TODO why this condition :: !f.getDomain().isEmpty()
        if(f.isZero() && !f.getDomain().isEmpty() && !f.getDomain().isFull()){
            return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
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
