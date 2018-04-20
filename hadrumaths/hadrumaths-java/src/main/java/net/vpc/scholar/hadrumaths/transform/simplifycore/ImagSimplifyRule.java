/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class ImagSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ImagSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Imag.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Imag ee = (Imag) e;
        RewriteResult rbase = ruleset.rewrite(ee.getArg());
//        if(rbase instanceof Imag){
//            return rbase;
//        }
        IConstantValue ac0= Expressions.toComplexValue(rbase.getValue());
        if(ac0!=null){
            return RewriteResult.bestEffort(new ComplexValue(Complex.valueOf(ac0.getComplexConstant().getImag()), ac0.getDomain()));
        }
        if(rbase.getValue().isDD()){
            return RewriteResult.bestEffort(FunctionFactory.DZERO(rbase.getValue().getDomainDimension()));
        }
        if(Maths.isReal(rbase.getValue())){
            return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
        }
        if(rbase.getValue() instanceof Mul){
            return RewriteResult.newVal(RealSimplifyRule.getMulRealImag((Mul) rbase.getValue())[1]);
        }
        if(rbase.getValue() instanceof Plus){
            List<Expr> e1 = rbase.getValue().getSubExpressions();
            List<Expr> e2 = new ArrayList<Expr>();
            for (Expr expr : e1) {
                e2.add(Maths.imag(expr));
            }
            return RewriteResult.newVal(Maths.sum(e2.toArray(new Expr[e2.size()])));
        }
        if(rbase.getValue() instanceof Sub){
            return RewriteResult.newVal(new Sub(Maths.imag(((Sub) rbase.getValue()).getFirst()),Maths.imag(((Sub) rbase.getValue()).getSecond())));
        }
        if(rbase.getValue() instanceof Div){
            Expr first = ((Div) rbase.getValue()).getFirst();
            Expr second = ((Div) rbase.getValue()).getSecond();
            if(first.isDC() && second.isDC()){
                DoubleToComplex a = first.toDC();
                DoubleToComplex b = second.toDC();
                boolean aReal = a.getImagDD().isZero();
                boolean aImag = a.getRealDD().isZero();
                boolean bReal = b.getImagDD().isZero();
                boolean bImag = b.getRealDD().isZero();
                if((aReal && bReal) || (aImag && bImag)) {
                    return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
                }else if((aReal && bImag) || (bReal && aImag)){
                    return rbase;
                }
            }
        }
        DoubleToDouble r = rbase.getValue().toDC().getImagDD();
        if(!(r instanceof Imag)){
            return RewriteResult.newVal(r);
        }
//        if(rbase instanceof Inv){
//            Expr a = ((Inv) rbase).getExpression();
//            if(Maths.isReal(a)){
//                return rbase;
//            }
//        }
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
