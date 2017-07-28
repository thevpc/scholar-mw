/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.DCxy;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class DCxySimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DCxySimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DCxy.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DCxy ee = (DCxy) e;
        DoubleToDouble real = ee.getReal();
        DoubleToDouble imag = ee.getImag();
        RewriteResult exreal=ruleset.rewrite(real);
        RewriteResult eximag=ruleset.rewrite(imag);
        if(eximag.getValue().isZero()){
            return RewriteResult.bestEffort(exreal.getValue());
        }
        if(exreal.getValue().getDomain().equals(eximag.getValue().getDomain())){
            if(exreal.getValue() instanceof DoubleValue && eximag.getValue() instanceof DoubleValue){
                DoubleValue a=(DoubleValue) exreal.getValue();
                DoubleValue b=(DoubleValue) eximag.getValue();
                return RewriteResult.bestEffort(new ComplexValue(Complex.valueOf(a.value,b.value), exreal.getValue().getDomain()));
            }
        }
        if(exreal.isUnmodified() && eximag.isUnmodified()){
            return RewriteResult.unmodified(e);
        }
        return RewriteResult.bestEffort(Maths.complex(real, imag));
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
