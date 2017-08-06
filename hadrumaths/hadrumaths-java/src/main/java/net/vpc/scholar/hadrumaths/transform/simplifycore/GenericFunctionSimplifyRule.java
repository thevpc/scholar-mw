/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.symbolic.AbstractComposedFunction;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class GenericFunctionSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new GenericFunctionSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{AbstractComposedFunction.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        AbstractComposedFunction ee = (AbstractComposedFunction) e;
        Expr[] args=ee.getArguments();
        Expr[] args2=new Expr[args.length];
        boolean updated=false;
        int bestEffort=0;
        for (int i = 0; i < args.length; i++) {
            RewriteResult v = ruleset.rewrite(args[i]);
            if(v.isRewritten()){
                updated=true;
                if(v.isBestEffort()){
                    bestEffort++;
                }
            }else{
                bestEffort++;
            }
            args2[i]=v.getValue();
        }
        if(updated){
            Expr a = ee.newInstance(args2);
            a= Any.copyProperties(e, a);
            return bestEffort==args.length?RewriteResult.bestEffort(a) : RewriteResult.newVal(a);
        }else{
            return RewriteResult.unmodified(e);
        }
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
