/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class PowSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new PowSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Pow.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Pow ee = (Pow) e;
        RewriteResult a=ruleset.rewrite(ee.getFirst());
        RewriteResult b=ruleset.rewrite(ee.getSecond());

        IConstantValue ac0= Expressions.toComplexValue(a.getValue());
        IConstantValue bc0=Expressions.toComplexValue(b.getValue());
        if(ac0!=null && bc0!=null){
            return RewriteResult.bestEffort(new ComplexValue(ac0.getComplexConstant().pow(bc0.getComplexConstant()), ac0.getDomain().intersect(bc0.getDomain())));
        }
        if(bc0!=null && bc0.getComplexConstant().isReal()){
            double value = bc0.getComplexConstant().toDouble();
            if(value ==1){
                return RewriteResult.newVal(Maths.mul(a.getValue(), DoubleValue.valueOf(1, bc0.getDomain())));
            }else if(value==0){
                if(a.getValue().isScalarExpr() || a.getValue().isDD() || a.getValue().isDC()){
                    return RewriteResult.newVal(DoubleValue.valueOf(1,a.getValue().toDD().getDomain().intersect(bc0.getDomain())));
                }
            }
        }
        if(a.isUnmodified() && b.isUnmodified()){
            return RewriteResult.unmodified(e);
        }
        Expr e2=new Pow(a.getValue(),b.getValue());
        e2= AbstractExprPropertyAware.copyProperties(e,e2);
        if(a.isBestEffort() && b.isBestEffort()){
            return RewriteResult.bestEffort(e2);
        }
        return RewriteResult.newVal(e2);
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
