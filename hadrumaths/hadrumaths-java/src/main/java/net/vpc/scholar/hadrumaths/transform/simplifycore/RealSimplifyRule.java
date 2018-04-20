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
public class RealSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new RealSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Real.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {

        Real ee = (Real) e;
        RewriteResult rbase = ruleset.rewrite(ee.getArg());
        if(rbase.getValue() instanceof Real){
            return rbase;
        }
        DoubleToDouble r = rbase.getValue().toDC().getRealDD();
        if(!(r instanceof Real)){
            return RewriteResult.bestEffort(r);
        }
        if(Maths.isImag(rbase.getValue())){
            return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
        }
        IConstantValue ac0= Expressions.toComplexValue(rbase.getValue());
        if(ac0!=null){
            return RewriteResult.bestEffort(new ComplexValue(Complex.valueOf(ac0.getComplexConstant().getReal()), ac0.getDomain()));
        }
        if(rbase.getValue().isDD()){
            return rbase;
        }
        if(rbase.getValue() instanceof Mul){
            return RewriteResult.newVal(getMulRealImag((Mul) rbase.getValue())[0]);
        }
        if(rbase.getValue() instanceof Plus){
            List<Expr> e1 = rbase.getValue().getSubExpressions();
            List<Expr> e2 = new ArrayList<Expr>();
            for (Expr expr : e1) {
                e2.add(Maths.real(expr));
            }
            return RewriteResult.newVal(Maths.sum(e2.toArray(new Expr[e2.size()])));
        }
        if(rbase.getValue() instanceof Sub){
            return RewriteResult.newVal(new Sub(Maths.real(((Sub) rbase.getValue()).getFirst()),Maths.real(((Sub) rbase.getValue()).getSecond())));
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
                if(aReal && bReal) {
                    return rbase;
                }else if(aImag && bImag){
                    return rbase;
                }else if((aReal && bImag) || (bReal && aImag)){
                    return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
                }
            }
        }
        if(rbase.getValue() instanceof Inv){
            Expr a = ((Inv) rbase.getValue()).getExpression();
            if(Maths.isReal(a)){
                return rbase;
            }
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

    private Expr rewriteMul(Mul m){
        return getMulRealImag(m)[0];
    }



    public static DoubleToDouble[] getMulRealImag(Mul m) {
        List<Expr> expressions = m.getSubExpressions();
        Expr[] real=new Expr[expressions.size()];
        Expr[] imag=new Expr[expressions.size()];
        boolean someReal=false;
        boolean someImag=false;
        for (int i = 0; i < expressions.size(); i++) {
            DoubleToComplex v = expressions.get(i).toDC();
            real[i]= v.getRealDD();
            imag[i]= v.getImagDD();
            someReal=someReal|!real[i].isZero();
            someImag=someImag|!imag[i].isZero();
        }
        //zeros
        if(!someReal && !someImag){
            return new DoubleToDouble[]{FunctionFactory.DZEROXY,FunctionFactory.DZEROXY};
        //pure real
        }else  if(someReal && !someImag){
            return new DoubleToDouble[]{Maths.mul(real).toDD(),FunctionFactory.DZEROXY};
        }else  if(!someReal && someImag){
            return new DoubleToDouble[]{FunctionFactory.DZEROXY,Maths.mul(imag).toDD()};
        }else{
            DoubleToComplex e = expressions.get(0).toDC();
            if(expressions.size()==1){
                return new DoubleToDouble[]{
                        e.getRealDD(),
                        e.getImagDD()
                };
            }else {
                DoubleToComplex a= e;
                DoubleToComplex b=null;
                if(expressions.size()==2){
                    b=expressions.get(1).toDC();
                }else{
                    List<Expr> e2 = new ArrayList<Expr>(m.getSubExpressions());
                    e2.remove(0);
                    b=Maths.mul(e2.toArray(new Expr[e2.size()])).toDC();
                }
                Sub realFull = new Sub(Maths.mul(a.getRealDD(), b.getRealDD()), Maths.mul(a.getImagDD(), b.getImagDD()));
                DoubleToDouble imagFull = Maths.sum(Maths.mul(a.getRealDD(), b.getImagDD()), Maths.mul(a.getImagDD(), b.getRealDD())).toDD();
                return new DoubleToDouble[]{realFull,imagFull};
            }
        }

    }
}
