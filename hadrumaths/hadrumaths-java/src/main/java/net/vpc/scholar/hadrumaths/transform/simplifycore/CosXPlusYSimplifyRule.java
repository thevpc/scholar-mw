/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.CosXPlusY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class CosXPlusYSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXPlusYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXPlusY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof CosXCosY)) {
//            return null;
//        }

        CosXPlusY ee = (CosXPlusY) e;
        if(ee.getAmp()==0){
            return RewriteResult.bestEffort(DoubleValue.valueOf(0,e.getDomain()));
        }
        if(ee.getA()==0){
            return RewriteResult.newVal(new CosXCosY(ee.getAmp(),0,0,ee.getB(),ee.getC(),ee.getDomain()));
        }
        if(ee.getB()==0){
            return RewriteResult.newVal(new CosXCosY(ee.getAmp(),ee.getA(),ee.getC(),0,0,ee.getDomain()));
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
