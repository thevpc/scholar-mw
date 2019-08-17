/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
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
        if (rbase.getValue() instanceof Real) {
            return rbase;
        }
        if (rbase.getValue().isDD()) {
            return rbase.isBestEffort() ? RewriteResult.bestEffort(rbase.getValue()) : RewriteResult.newVal(rbase.getValue());
        }
        if (rbase.getValue().isDC()) {
            DoubleToDouble r = rbase.getValue().toDC().getRealDD();
            if (!(r instanceof Real)) {
                return RewriteResult.bestEffort(ruleset.rewriteOrSame(r));
            }
        }
        IConstantValue ac0 = Expressions.toComplexValue(rbase.getValue());
        if (ac0 != null) {
            return RewriteResult.bestEffort(new ComplexValue(Complex.valueOf(ac0.getComplexConstant().getReal()), ac0.getDomain()));
        }
        if (rbase.getValue().isDD()) {
            return rbase;
        }
        if (rbase.getValue() instanceof Mul) {
            Mul m = (Mul) rbase.getValue();
//            List<Expr> dd = new ArrayList<>();
//            List<Expr> ndd = new ArrayList<>();
//            for (Expr expr : m.getSubExpressions()) {
//                if (expr.isDD()) {
//                    dd.add(expr);
//                } else {
//                    //    Complex.valueOf(getReal() * c.getReal() - getImag() * c.getImag(), getReal() * c.getImag() + getImag() * c.getReal());
//                    getSimplifiedRealImag
//                    boolean ok = true;
//                    Expr s = ruleset.rewriteOrSame(new Real(expr.toDC()));
//                    if (s instanceof Real) {
//                        //
//                        ok = false;
//                    }
//
//                    ndd.add(expr);
//                }
//            }

            return RewriteResult.newVal(getMulRealImag(m)[0]);
        }
        if (rbase.getValue() instanceof Plus) {
            List<Expr> e1 = rbase.getValue().getSubExpressions();
            List<Expr> e2 = new ArrayList<Expr>();
            for (Expr expr : e1) {
                e2.add(Maths.real(expr));
            }
            return RewriteResult.newVal(Maths.sum(e2.toArray(new Expr[0])));
        }
        if (rbase.getValue() instanceof Sub) {
            return RewriteResult.newVal(new Sub(Maths.real(((Sub) rbase.getValue()).getFirst()), Maths.real(((Sub) rbase.getValue()).getSecond())));
        }
        if (rbase.getValue() instanceof Div) {
            Expr first = ((Div) rbase.getValue()).getFirst();
            Expr second = ((Div) rbase.getValue()).getSecond();
            if (first.isDC() && second.isDC()) {
                DoubleToComplex a = first.toDC();
                DoubleToComplex b = second.toDC();
                boolean aReal = a.getImagDD().isZero();
                boolean aImag = a.getRealDD().isZero();
                boolean bReal = b.getImagDD().isZero();
                boolean bImag = b.getRealDD().isZero();
                if (aReal && bReal) {
                    return rbase;
                } else if (aImag && bImag) {
                    return rbase;
                } else if ((aReal && bImag) || (bReal && aImag)) {
                    return RewriteResult.bestEffort(FunctionFactory.DZEROXY);
                }
            }
        }
        if (rbase.getValue() instanceof Inv) {
            Expr a = ((Inv) rbase.getValue()).getExpression();
            if (Maths.isReal(a)) {
                return rbase;
            }
        }
        if (rbase.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }
        if (rbase.isBestEffort()) {
            return RewriteResult.bestEffort(new Real(rbase.getValue().toDC()));
        }
        return RewriteResult.newVal(new Real(rbase.getValue().toDC()));
    }

    private Expr[] getSimplifiedRealImag(Expr expr, ExpressionRewriter ruleset) {
        Expr r = ruleset.rewriteOrSame(new Real(expr.toDC()));
        if (r instanceof Real) {
            return null;
        }
        Expr i = ruleset.rewriteOrSame(new Imag(expr.toDC()));
        if (i instanceof Imag) {
            return null;
        }
        return new Expr[]{r, i};
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    private Expr rewriteMul(Mul m) {
        return getMulRealImag(m)[0];
    }


    public static DoubleToDouble[] getMulRealImag(Mul m) {
        List<Expr> expressions = m.getSubExpressions();
        Expr[] real = new Expr[expressions.size()];
        Expr[] imag = new Expr[expressions.size()];
        List<Expr> realOnly=new ArrayList<>();
        List<Expr> others=new ArrayList<>();
        int someReal = 0;
        int someImag = 0;
        for (int i = 0; i < expressions.size(); i++) {
            DoubleToComplex v = expressions.get(i).toDC();
            if(v.isZero()){
                real[i] = FunctionFactory.DZEROXY;
                imag[i] = FunctionFactory.DZEROXY;
                someReal += 1;
                return new DoubleToDouble[]{FunctionFactory.DZEROXY,FunctionFactory.DZEROXY};
            }else {
                real[i] = v.getRealDD();
                imag[i] = v.getImagDD();
                boolean br = !real[i].isZero();
                boolean bi = !imag[i].isZero();
                someReal += br ? 1 : 0;
                someImag += bi ? 1 : 0;
                if(br && !bi){
                    realOnly.add(v);
                }else{
                    others.add(v);
                }
            }
        }
        //zeros
        if (someReal==0 && someImag==0) {
            return new DoubleToDouble[]{FunctionFactory.DZEROXY, FunctionFactory.DZEROXY};
            //pure real
        } else if (someReal!=0 && someImag==0) {
            return new DoubleToDouble[]{Maths.mul(real).toDD(), FunctionFactory.DZEROXY};
        } else if (someReal==0 && someImag!=0) {
            switch (someImag%4){
                case 0:{
                    return new DoubleToDouble[]{Maths.mul(imag).toDD(),FunctionFactory.DZEROXY};
                }
                case 1:{
                    return new DoubleToDouble[]{FunctionFactory.DZEROXY, Maths.mul(imag).toDD()};
                }
                case 2:{
                    return new DoubleToDouble[]{new Neg(Maths.mul(imag).toDD()),FunctionFactory.DZEROXY};
                }
                case 3:{
                    return new DoubleToDouble[]{FunctionFactory.DZEROXY,new Neg(Maths.mul(imag).toDD())};
                }
            }
            throw new IllegalArgumentException("Unsupported");
        }else{

            DoubleToComplex e = others.get(0).toDC();
            if (others.size() == 1) {
                if(realOnly.isEmpty()) {
                    return new DoubleToDouble[]{
                            e.getRealDD(),
                            e.getImagDD()
                    };
                }else{
                    Expr[] r = ArrayUtils.append(realOnly.toArray(new Expr[0]), e.getRealDD());
                    Expr[] i = ArrayUtils.append(realOnly.toArray(new Expr[0]), e.getImagDD());
                    return new DoubleToDouble[]{
                            r.length==1?r[0].toDD():Maths.mul(r).toDD(),
                            i.length==1?i[0].toDD():Maths.mul(i).toDD()
                    };
                }
            } else {
                DoubleToComplex a = e;
                DoubleToComplex b = null;
                if (others.size() == 2) {
                    b = others.get(1).toDC();
                } else {
                    List<Expr> e2 = new ArrayList<Expr>(m.getSubExpressions());
                    e2.remove(0);
                    b = Maths.mul(e2.toArray(new Expr[0])).toDC();
                }
                Sub realFull = new Sub(Maths.mul(a.getRealDD(), b.getRealDD()), Maths.mul(a.getImagDD(), b.getImagDD()));
                DoubleToDouble imagFull = Maths.sum(Maths.mul(a.getRealDD(), b.getImagDD()), Maths.mul(a.getImagDD(), b.getRealDD())).toDD();
                if(realOnly.isEmpty()) {
                    return new DoubleToDouble[]{realFull, imagFull};
                }else{
                    Expr[] r = ArrayUtils.append(realOnly.toArray(new Expr[0]), realFull);
                    Expr[] i = ArrayUtils.append(realOnly.toArray(new Expr[0]), imagFull);
                    return new DoubleToDouble[]{
                            r.length==1?r[0].toDD():Maths.mul(r).toDD(),
                            i.length==1?i[0].toDD():Maths.mul(i).toDD()
                    };
                }
            }
        }

    }
}
