/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class ParametrizedScalarProductSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ParametrizedScalarProductSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{ParametrizedScalarProduct.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if(e instanceof ParametrizedScalarProduct){
            ParametrizedScalarProduct c=(ParametrizedScalarProduct)e;
            RewriteResult rxa=ruleset.rewrite(c.getXArgument());
            RewriteResult rya=ruleset.rewrite(c.getYArgument());
            if(!rxa.getValue().hasParams() && !rya.getValue().hasParams()){
                return RewriteResult.bestEffort(Maths.scalarProduct(c.isHermitian(), rxa.getValue(),rya.getValue()));
            }
            Expr xa=rxa.getValue();
            Expr ya=rya.getValue();
            if(rxa.isUnmodified() && rya.isUnmodified()){
                return RewriteResult.unmodified(e);
            }else if(rxa.isBestEffort() && rya.isBestEffort()){
                return RewriteResult.bestEffort(e);
            }else{
                Expr c2 = c.newInstance(xa, ya);
                return RewriteResult.newVal(c2);
            }
        }
        throw new IllegalArgumentException("Unsupported");
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
