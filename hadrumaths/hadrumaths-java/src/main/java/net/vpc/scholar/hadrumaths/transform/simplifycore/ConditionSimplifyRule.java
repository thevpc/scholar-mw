/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class ConditionSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ConditionSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{ComparatorExpr.class,NotExpr.class,IfThenElse.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if(e instanceof ComparatorExpr){
            ComparatorExpr c=(ComparatorExpr)e;
            RewriteResult rxa=ruleset.rewrite(c.getXArgument());
            RewriteResult rya=ruleset.rewrite(c.getYArgument());
            Expr xa=rxa.getValue();
            Expr ya=rya.getValue();
            if((xa.isDouble() || xa.isComplex()) && (ya.isDouble() ||ya.isComplex())){
                Complex a=xa.toComplex();
                Complex b=ya.toComplex();
                return RewriteResult.bestEffort(c.computeComplexArg(a,b,true,true, new OutBoolean()));
            } else if(rxa.isUnmodified() && rya.isUnmodified()){
                return RewriteResult.unmodified(e);
            }else if(rxa.isBestEffort() && rya.isBestEffort()){
                return RewriteResult.bestEffort(e);
            }else{
                Expr c2 = c.newInstance(xa, ya);
                return RewriteResult.newVal(c2);
            }
        }
        if(e instanceof NotExpr){
            NotExpr c=(NotExpr)e;
            RewriteResult rxa = ruleset.rewrite(c.getArgument());
            Expr xa= rxa.getValue();
            if((xa.isDouble() || xa.isComplex())){
                Complex a=xa.toComplex();
                return RewriteResult.bestEffort(c.computeComplexArg(a, new OutBoolean()));
            } else if(rxa.isUnmodified()){
                return RewriteResult.unmodified(e);
            }else if(rxa.isBestEffort()){
                return RewriteResult.bestEffort(e);
            }else{
                Expr c2 = c.newInstance(xa);
                return RewriteResult.newVal(c2);
            }

        }
        if(e instanceof IfThenElse){
            IfThenElse c=(IfThenElse)e;
            RewriteResult rxa=ruleset.rewrite(c.getXArgument());
            RewriteResult rya=ruleset.rewrite(c.getYArgument());
            RewriteResult rza=ruleset.rewrite(c.getZArgument());
            Expr xa=rxa.getValue();
            Expr ya=rya.getValue();
            Expr za=rza.getValue();
            if((xa.isDouble() || xa.isComplex())){
                Complex a=xa.toComplex();
                if(!a.isZero()){
                    return RewriteResult.newVal(ya);
                }else{
                    return RewriteResult.newVal(za);
                }
            } else if(rxa.isUnmodified() && rya.isUnmodified()&& rza.isUnmodified()){
                return RewriteResult.unmodified(e);
            }else if(rxa.isBestEffort() && rya.isBestEffort()&& rza.isBestEffort()){
                return RewriteResult.bestEffort(e);
            }else{
                Expr c2 = c.newInstance(xa, ya,za);
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
