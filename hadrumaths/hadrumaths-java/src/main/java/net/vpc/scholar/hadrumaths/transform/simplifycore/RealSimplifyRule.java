/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.vpc.scholar.hadrumaths.symbolic.conv.Real;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class RealSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new RealSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Real.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {

        Real ee = (Real) e;
        RewriteResult rbase = ruleset.rewrite(ee.getArg(), targetExprType);
        Expr rv = rbase.isUnmodified() ? ee.getArg() : rbase.getValue();
        if (rv instanceof Real) {
            return rbase;
        }
        ExprType rvd = rv.getType();
        if (rvd == ExprType.DOUBLE_DOUBLE) {
            return rbase.isBestEffort() ? RewriteResult.bestEffort(rv) : RewriteResult.newVal(rv);
        }
        if (rvd == ExprType.DOUBLE_COMPLEX) {
            DoubleToDouble r = rv.toDC().getRealDD();
            if (!(r instanceof Real)) {
                return RewriteResult.bestEffort(ruleset.rewriteOrSame(r, null));
            }
        }
        Expr ac0 = Expressions.toConstantExprOrNull(rv);
        if (ac0 != null) {
            return RewriteResult.bestEffort(new DefaultComplexValue(Complex.of(ac0.toComplex().getReal()), ac0.getDomain()));
        }
//        if (rv.isDD()) {
//            return rbase;
//        }
        if (rv instanceof Mul) {
            Mul m = (Mul) rv;
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
        if (rv instanceof Plus) {
            List<Expr> e1 = rv.getChildren();
            List<Expr> e2 = new ArrayList<Expr>();
            for (Expr expr : e1) {
                e2.add(Maths.real(expr));
            }
            return RewriteResult.newVal(Maths.sum(e2.toArray(new Expr[0])));
        }
        if (rv instanceof Sub) {
            return RewriteResult.newVal(Sub.of(Maths.real(rv.getChild(0)), Maths.real(rv.getChild(1))));
        }
        if (rv instanceof Div) {
            Expr first = ((Div) rv).getFirst();
            Expr second = ((Div) rv).getSecond();
            if (first.isNarrow(ExprType.DOUBLE_COMPLEX) && second.isNarrow(ExprType.DOUBLE_COMPLEX)) {
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
                    return RewriteResult.bestEffort(Maths.DZEROXY);
                }
            }
        }
        if (rv instanceof Inv) {
            Expr a = rv.getChild(0);
            if (Maths.isReal(a)) {
                return rbase;
            }
        }
        if (rbase.isUnmodified()) {
            return RewriteResult.unmodified();
        }
        if (rbase.isBestEffort()) {
            return RewriteResult.bestEffort(new Real(rv.toDC()));
        }
        return RewriteResult.newVal(new Real(rv.toDC()));
    }

    public static DoubleToDouble[] getMulRealImag(Mul m) {
        List<Expr> expressions = m.getChildren();
        Expr[] real = new Expr[expressions.size()];
        Expr[] imag = new Expr[expressions.size()];
        List<Expr> realOnly = new ArrayList<>();
        List<Expr> others = new ArrayList<>();
        int someReal = 0;
        int someImag = 0;
        for (int i = 0; i < expressions.size(); i++) {
            DoubleToComplex v = expressions.get(i).toDC();
            if (v.isZero()) {
                real[i] = Maths.DZEROXY;
                imag[i] = Maths.DZEROXY;
                someReal += 1;
                return new DoubleToDouble[]{Maths.DZEROXY, Maths.DZEROXY};
            } else {
                real[i] = v.getRealDD();
                imag[i] = v.getImagDD();
                boolean br = !real[i].isZero();
                boolean bi = !imag[i].isZero();
                someReal += br ? 1 : 0;
                someImag += bi ? 1 : 0;
                if (br && !bi) {
                    realOnly.add(v);
                } else {
                    others.add(v);
                }
            }
        }
        //zeros
        if (someReal == 0 && someImag == 0) {
            return new DoubleToDouble[]{Maths.DZEROXY, Maths.DZEROXY};
            //pure real
        } else if (someReal != 0 && someImag == 0) {
            return new DoubleToDouble[]{Maths.mul(real).toDD(), Maths.DZEROXY};
        } else if (someReal == 0 && someImag != 0) {
            switch (someImag % 4) {
                case 0: {
                    return new DoubleToDouble[]{Maths.mul(imag).toDD(), Maths.DZEROXY};
                }
                case 1: {
                    return new DoubleToDouble[]{Maths.DZEROXY, Maths.mul(imag).toDD()};
                }
                case 2: {
                    return new DoubleToDouble[]{Neg.of(Maths.mul(imag).toDD()).toDD(), Maths.DZEROXY};
                }
                case 3: {
                    return new DoubleToDouble[]{Maths.DZEROXY, Neg.of(Maths.mul(imag).toDD()).toDD()};
                }
            }
            throw new IllegalArgumentException("Unsupported");
        } else {

            DoubleToComplex e = others.get(0).toDC();
            if (others.size() == 1) {
                if (realOnly.isEmpty()) {
                    return new DoubleToDouble[]{
                            e.getRealDD(),
                            e.getImagDD()
                    };
                } else {
                    Expr[] r = ArrayUtils.append(realOnly.toArray(new Expr[0]), e.getRealDD());
                    Expr[] i = ArrayUtils.append(realOnly.toArray(new Expr[0]), e.getImagDD());
                    return new DoubleToDouble[]{
                            r.length == 1 ? r[0].toDD() : Maths.mul(r).toDD(),
                            i.length == 1 ? i[0].toDD() : Maths.mul(i).toDD()
                    };
                }
            } else {
                DoubleToComplex a = e;
                DoubleToComplex b = null;
                if (others.size() == 2) {
                    b = others.get(1).toDC();
                } else {
                    List<Expr> e2 = new ArrayList<Expr>(m.getChildren());
                    e2.remove(0);
                    b = Maths.mul(e2.toArray(new Expr[0])).toDC();
                }
                Sub realFull = Sub.of(Maths.mul(a.getRealDD(), b.getRealDD()), Maths.mul(a.getImagDD(), b.getImagDD()));
                DoubleToDouble imagFull = Maths.sum(Maths.mul(a.getRealDD(), b.getImagDD()), Maths.mul(a.getImagDD(), b.getRealDD())).toDD();
                if (realOnly.isEmpty()) {
                    return new DoubleToDouble[]{realFull.toDD(), imagFull};
                } else {
                    Expr[] r = ArrayUtils.append(realOnly.toArray(new Expr[0]), realFull);
                    Expr[] i = ArrayUtils.append(realOnly.toArray(new Expr[0]), imagFull);
                    return new DoubleToDouble[]{
                            r.length == 1 ? r[0].toDD() : Maths.mul(r).toDD(),
                            i.length == 1 ? i[0].toDD() : Maths.mul(i).toDD()
                    };
                }
            }
        }

    }

    private Expr[] getSimplifiedRealImag(Expr expr, ExpressionRewriter ruleset) {
        Expr r = ruleset.rewriteOrSame(new Real(expr.toDC()), null);
        if (r instanceof Real) {
            return null;
        }
        Expr i = ruleset.rewriteOrSame(new Imag(expr.toDC()), null);
        if (i instanceof Imag) {
            return null;
        }
        return new Expr[]{r, i};
    }

    private Expr rewriteMul(Mul m) {
        return getMulRealImag(m)[0];
    }
}
