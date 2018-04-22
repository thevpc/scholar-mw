/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
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
public class ExpSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ExpSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Exp.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof CosXCosY)) {
//            return null;
//        }

        Exp ee = (Exp) e;
        Expr aa = ee.getArgument();
        RewriteResult rewriteResult = ruleset.rewrite(aa);
        Expr simplifiedArg = rewriteResult.getValue();
        if(simplifiedArg.isDoubleExpr()) {
            return RewriteResult.newVal(
                    new DoubleValue(Maths.exp(simplifiedArg.toDouble()),simplifiedArg.getDomain())
            );
        }else if(simplifiedArg.isComplex()){
            Complex c=simplifiedArg.toComplex();
            if(c.isReal()) {
                return RewriteResult.newVal(
                        DoubleValue.valueOf(Maths.exp(c.realdbl()))
                );
            }else if(c.isImag()){
                return RewriteResult.newVal(
                            Complex.valueOf(Math.cos(c.imagdbl()),Math.sin(c.imagdbl()))
                );
            }else {
                return RewriteResult.newVal(
                            Complex.valueOf(Math.cos(c.imagdbl()),Math.sin(c.imagdbl())).mul(Math.exp(c.realdbl()))
                );
            }
        }else if(simplifiedArg.isComplexExpr()){
            ComplexValue cv=(ComplexValue) simplifiedArg;
            Complex c=cv.getValue();
            if(c.isReal()) {
                return RewriteResult.newVal(
                        new DoubleValue(Maths.exp(c.realdbl()), simplifiedArg.getDomain())
                );
            }else if(c.isImag()){
                return RewriteResult.newVal(
                        new ComplexValue(Complex.valueOf(Math.cos(c.imagdbl()),Math.sin(c.imagdbl())),simplifiedArg.getDomain())
                );
            }else {
                return RewriteResult.newVal(
                        new ComplexValue(Complex.valueOf(Math.cos(c.imagdbl()),Math.sin(c.imagdbl())).mul(Math.exp(c.realdbl())),simplifiedArg.getDomain())
                );
            }
        }else if(simplifiedArg.isDD()){
            //nothing to do
        }else if(simplifiedArg.isDC()){
            DoubleToComplex doubleToComplex = simplifiedArg.toDC();
            Expr a= ruleset.rewriteOrSame(doubleToComplex.getRealDD()).toDD();
            Expr b= ruleset.rewriteOrSame(doubleToComplex.getImagDD()).toDD();
            return RewriteResult.newVal(Maths.mul(new Exp(a),Maths.sum(Maths.cos(b),Maths.mul(Maths.I,Maths.sin(b)))));
        }
        if(rewriteResult.isUnmodified()) {
            return RewriteResult.unmodified(ee);
        }else if(rewriteResult.isBestEffort()){
            return RewriteResult.bestEffort(new Exp(rewriteResult.getValue()));
        }else {
            return RewriteResult.newVal(new Exp(rewriteResult.getValue()));
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
