/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.common.util.ClassPairMapList;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public class PlusSimplifyRule implements ExpressionRewriterRule {

    public static final Class<? extends Expr>[] TYPES = new Class[]{Plus.class};
    private static final ReverseSimplifier REVERSE = new ReverseSimplifier();

    private ClassPairMapList<PlusPairSimplifier> simplifiers = new ClassPairMapList<PlusPairSimplifier>(Expr.class, Expr.class, PlusPairSimplifier.class, false);

    public static final ExpressionRewriterRule INSTANCE = new PlusSimplifyRule();

    public PlusSimplifyRule() {
        simplifiers.add(Expr.class, Expr.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                if (a.equals(b)) {
                    return MathsBase.mul(Complex.TWO, a);
                }
                return null;
            }
        });
        simplifiers.add(DoubleValue.class, DoubleValue.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                DoubleValue aa = (DoubleValue) a;
                DoubleValue bb = (DoubleValue) b;
                return DoubleValue.valueOf(aa.value + bb.value, aa.getDomain());
            }
        });
        simplifiers.add(DoubleValue.class, ComplexValue.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                DoubleValue aa = (DoubleValue) a;
                ComplexValue bb = (ComplexValue) b;
                if (bb.getValue().isReal()) {
                    return DoubleValue.valueOf(aa.value + bb.getValue().getReal(), aa.getDomain());
                }
                return new ComplexValue(bb.getValue().add(aa.value), aa.getDomain());
            }
        });
        simplifiers.add(DoubleValue.class, Complex.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                DoubleValue aa = (DoubleValue) a;
                Complex bb = (Complex) b;
                if (bb.isReal()) {
                    return DoubleValue.valueOf(aa.value + bb.getReal(), aa.getDomain());
                }
                return new ComplexValue(bb.add(aa.value), aa.getDomain());
            }
        });
        simplifiers.add(Complex.class, Complex.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Complex aa = (Complex) a;
                Complex bb = (Complex) b;
                return aa.add(bb);
            }
        });
        simplifiers.add(Complex.class, ComplexValue.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Complex aa = (Complex) a;
                ComplexValue bb = (ComplexValue) b;
                //same domain!
                return aa.add(bb.getValue());
            }
        });
        simplifiers.add(ComplexValue.class, ComplexValue.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                ComplexValue aa = (ComplexValue) a;
                ComplexValue bb = (ComplexValue) b;
                //same domain!
                return new ComplexValue(aa.getValue().add(bb.getValue()), aa.getDomain());
            }
        });
        simplifiers.add(DoubleValue.class, Linear.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                DoubleValue aa = (DoubleValue) a;
                Linear bb = (Linear) b;
                return new Linear(bb.a, bb.b, aa.value + bb.c, aa.getDomain());
            }
        });
        simplifiers.add(Complex.class, Linear.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Complex aa = (Complex) a;
                Linear bb = (Linear) b;
                if (aa.isReal()) {
                    return new Linear(bb.a, bb.b, aa.getReal() + bb.c, bb.getDomain());
                }
                return null;
            }
        });
        simplifiers.add(ComplexValue.class, Linear.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                ComplexValue aa = (ComplexValue) a;
                Linear bb = (Linear) b;
                if (aa.getValue().isReal()) {
                    return new Linear(bb.a, bb.b, aa.getValue().getReal() + bb.c, bb.getDomain());
                }
                return null;
            }
        });
        simplifiers.add(Linear.class, Linear.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Linear aa = (Linear) a;
                Linear bb = (Linear) b;
                return new Linear(aa.a + bb.a, aa.b + bb.b, aa.c + bb.c, bb.getDomain());
            }
        });
//        simplifiers.add(DoubleXY.class,DDxyDiscrete.class,new PlusPairSimplifier() {
//            @Override
//            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
//                DoubleXY aa = (DoubleXY) a;
//                DDxyDiscrete bb = (DDxyDiscrete) b;
//                double factor = aa.value;
//                double[][] values = bb.getValues();
//                double[][] newValues = new double[values.length][];
//                for (int i = 0; i < newValues.length; i++) {
//                    newValues[i] = new double[values[i].length];
//                    for (int j = 0; j < newValues[i].length; j++) {
//                        newValues[i][j] = values[i][j] + factor;
//                    }
//                }
//                return new DDxyDiscrete(newValues, aa.getDomain());
//            }
//        });
        simplifiers.add(DoubleValue.class, DDiscrete.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                DoubleValue aa = (DoubleValue) a;
                DDiscrete bb = (DDiscrete) b;
                DoubleValue newCst = DoubleValue.valueOf(aa.value, aa.getDomain());
                return bb.add(newCst);
            }
        });

        simplifiers.add(Expr.class, Mul.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Mul bb = (Mul) b;
                List<Expr> bbs = bb.getSubExpressions();
                if (bbs.size() == 2) {
                    IConstantValue b1 = Expressions.toComplexValue(bbs.get(0));
                    IConstantValue b2 = Expressions.toComplexValue(bbs.get(1));
                    if (b1 != null && a.equals(bbs.get(1))) {
                        return MathsBase.mul(simplifyXY(Complex.ONE, b1, domain), a);
                    }
                    if (b2 != null && a.equals(bbs.get(0))) {
                        return MathsBase.mul(simplifyXY(Complex.ONE, b2, domain), a);
                    }
                }
                return null;
            }
        });
        simplifiers.add(Mul.class, Expr.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Mul aa = (Mul) a;
                Expr bb = b;
                List<Expr> aas = aa.getSubExpressions();
                if (aas.size() == 2) {
                    IConstantValue a1 = Expressions.toComplexValue(aas.get(0));
                    if (a1 != null && b.equals(aas.get(1))) {
                        return MathsBase.mul(simplifyXY(Complex.ONE, a1, domain), b);
                    }
                    IConstantValue a2 = Expressions.toComplexValue(aas.get(1));
                    if (a2 != null && a.equals(aas.get(0))) {
                        return MathsBase.mul(simplifyXY(Complex.ONE, a2, domain), b);
                    }
                }
                return null;
            }
        });
        simplifiers.add(Mul.class, Mul.class, new PlusPairSimplifier() {
            @Override
            public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
                Mul aa = (Mul) a;
                Mul bb = (Mul) b;
                List<Expr> aas = aa.getSubExpressions();
                List<Expr> bbs = bb.getSubExpressions();
                if (aas.size() == 2 && bbs.size() == 2) {
                    IConstantValue a1 = Expressions.toComplexValue(aas.get(0));
                    IConstantValue a2 = Expressions.toComplexValue(aas.get(1));
                    IConstantValue b1 = Expressions.toComplexValue(bbs.get(0));
                    IConstantValue b2 = Expressions.toComplexValue(bbs.get(1));

                    Expr an = null;
                    Expr ax = null;
                    if (a1 != null) {
                        an = a1;
                        ax = aas.get(1);
                    } else if (a2 != null) {
                        an = a2;
                        ax = aas.get(0);
                    }

                    Expr bn = null;
                    Expr bx = null;
                    if (b1 != null) {
                        bn = b1;
                        bx = bbs.get(1);
                    } else if (b2 != null) {
                        bn = b2;
                        bx = bbs.get(0);
                    }

                    if (an != null && bn != null && ax.equals(bx)) {
                        return MathsBase.mul(simplifyXY(an, bn, domain), ax);
                    }
                }
                return null;
            }
        });
        simplifiers.add(Linear.class, DoubleValue.class, REVERSE);
//        simplifiers.add(DDxyDiscrete.class,DoubleXY.class, REVERSE);
        simplifiers.add(DDiscrete.class, DoubleValue.class, REVERSE);
        simplifiers.add(Complex.class, DoubleValue.class, REVERSE);
        simplifiers.add(ComplexValue.class, DoubleValue.class, REVERSE);
        simplifiers.add(ComplexValue.class, Complex.class, REVERSE);
        simplifiers.add(Linear.class, Complex.class, REVERSE);
        simplifiers.add(Linear.class, ComplexValue.class, REVERSE);
    }

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Plus ee = (Plus) e;
        DomainGroupedList grouped = new DomainGroupedList();
        boolean someModification = false;
        for (Expr expression : ee.getSubExpressions()) {
            RewriteResult rewrite = ruleset.rewrite(expression);
            if (!rewrite.isUnmodified()) {
                someModification = true;
            }
            expression = rewrite.getValue();
            if (expression instanceof Plus) {
                for (Expr se : expression.getSubExpressions()) {
                    grouped.addxy(se);
                }
            } else if (!expression.isZero()) {
                grouped.addxy(expression);
            }
        }
//        for (Domain domain : grouped.domainx.keySet().toArray(new Domain[grouped.domainx.size()])) {
//            grouped.domainx.put(domain, simplifyX((List) grouped.get(domain), grouped.fullx));
//        }
        for (Domain domain : grouped.domainxy.keySet().toArray(new Domain[0])) {
            List<RewriteResult> rewriteResults = simplifyXY(grouped.get(domain), grouped.fullxy);
            List<Expr> rewriteResults2 = new ArrayList<>();
            for (RewriteResult rewriteResult : rewriteResults) {
                rewriteResults2.add(rewriteResult.getValue());
                if (rewriteResult.isRewritten()) {
                    someModification = true;
                }
            }
            grouped.domainxy.put(domain, rewriteResults2);
        }

        if (grouped.domainxy.size() > 0 || !grouped.complex.isReal()) {
            //eval x to xy no more needed!
//            for (DomainX domain : grouped.domainx.keySet().toArray(new DomainX[grouped.domainx.size()])) {
//                for (IDDx expression : grouped.get(domain)) {
//                    DomainX dx = expression.getDomain();
//                    DomainXY dxy = new DomainXY(dx.xmin, grouped.fullxy.ymin, dx.xmax, grouped.fullxy.ymax);
//                    if (expression instanceof DDxCos) {
//                        DDxCos c = (DDxCos) expression;
//                        grouped.addxy(new CosXCosY(c.getAmp(), c.getA(), c.getB(), 0, 0, dxy));
//                    } else if (expression instanceof DDxLinear) {
//                        DDxLinear c = (DDxLinear) expression;
//                        grouped.addxy(new DDxyLinear(c.getA(), c.getB(), 0, dxy));
//                    } else if (expression instanceof DDxUx) {
//                        DDxUx c = (DDxUx) expression;
//                        grouped.addxy(new DDxyU(c, grouped.fullxy.ymin, grouped.fullxy.ymax));
//                    } else {
//                        grouped.addxy(new DDxToDDxy(expression, grouped.fullxy.ymin, grouped.fullxy.ymax));
//                    }
//                }
//            }
//            grouped.domainx.clear();
            if (!grouped.complex.isZero()) {
                grouped.addxy(MathsBase.expr(grouped.complex, Domain.FULL(ee.getDomainDimension())));
            }
            List<RewriteResult> all = new ArrayList<RewriteResult>();
            for (Domain domain : grouped.domainxy.keySet().toArray(new Domain[0])) {
                all.addAll(simplifyXY(grouped.get(domain), grouped.fullxy));
            }
            if (all.isEmpty()) {
                return RewriteResult.bestEffort(Complex.ZERO);
            }
            if (all.size() == 1) {
                //return RewriteResult.bestEffort(all.get(0));
                RewriteResult rewriteResult = all.get(0);
                return (rewriteResult.isUnmodified() && someModification) ? RewriteResult.newVal(rewriteResult.getValue()) : rewriteResult;
            }
            int r = someModification ? RewriteResult.NEW_VAL : RewriteResult.UNMODIFIED;
            List<Expr> exprs = new ArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                if (r == -1 || r > all.get(i).getType()) {
                    r = all.get(i).getType();
                }
                exprs.add(all.get(i).getValue());
            }
            Expr newVal = MathsBase.sum(exprs.toArray(new Expr[0]));
            if (newVal.equals(e)) {
                return RewriteResult.unmodified(e);
            }
            if (r == RewriteResult.BEST_EFFORT) {
                return RewriteResult.bestEffort(newVal);
            } else {
                return RewriteResult.newVal(newVal);
            }
//        } else if (grouped.domainx.size() > 0) {
//            List<Expr> all = new ArrayList<Expr>();
//            grouped.addxy(new DoubleX(grouped.complex.getReal(), grouped.fullx));
//            for (Domain domain : grouped.domainx.keySet().toArray(new Domain[grouped.domainx.size()])) {
//                grouped.domainx.put(domain, simplifyX((List) grouped.get(domain), grouped.fullx));
//            }
//            for (Domain domain : grouped.domainx.keySet().toArray(new Domain[grouped.domainx.size()])) {
//                all.addAll(grouped.get(domain));
//            }
//            if (all.isEmpty()) {
//                return Complex.ZERO;
//            }
//            if (all.size() == 1) {
//                return all.get(0);
//            }
//            return MathsBase.sum(all.toArray(new Expr[all.size()]));
        } else {
            return RewriteResult.bestEffort(grouped.complex);
        }
    }

//    private List<IDDx> simplifyX(List<IDDx> all, Domain fullDomain) {
//        if (all.size() < 2) {
//            return new ArrayList<IDDx>(all);
//        }
//        IDDx[] arr = all.toArray(new IDDx[all.size()]);
//        boolean[] processed = new boolean[arr.length];
//        List<IDDx> ok = new ArrayList<IDDx>();
//        boolean optimized;
//        for (int i = 0; i < arr.length; i++) {
//            IDDx a = arr[i];
//            if (!processed[i]) {
//                optimized = false;
//                for (int j = i + 1; j < arr.length; j++) {
//                    Expr b = arr[j];
//                    if (!processed[i] && !processed[j]) {
//                        IDDx r = simplifyX(a, b, fullDomain).toDDx();
//                        if (r != null) {
//                            processed[i] = true;
//                            processed[j] = true;
//                            ok.add(r);
//                            optimized = true;
//                            break;
//                        }
//                    }
//                }
//                if (!optimized) {
//                    ok.add(a);
//                }
//            }
//        }
//        return ok;
//    }

    private List<RewriteResult> simplifyXY(List<Expr> all, Domain fullDomain) {
        if (all.size() < 2) {
            ArrayList<RewriteResult> exprs = new ArrayList<>();
            for (Expr expr : all) {
                exprs.add(RewriteResult.unmodified(expr));
            }
            return exprs;
        }
        Expr[] arr = all.toArray(new Expr[all.size()]);
        boolean[] processed = new boolean[arr.length];
        List<RewriteResult> ok = new ArrayList<RewriteResult>();
        boolean optimized;
        for (int i = 0; i < arr.length; i++) {
            Expr a = arr[i];
            if (!processed[i]) {
                optimized = false;
                for (int j = i + 1; j < arr.length; j++) {
                    Expr b = arr[j];
                    if (!processed[i] && !processed[j]) {
                        Expr r = simplifyXY(a, b, fullDomain);
                        if (r != null) {
                            processed[i] = true;
                            processed[j] = true;
                            ok.add(RewriteResult.newVal(r));
                            optimized = true;
                            break;
                        }
                    }
                }
                if (!optimized) {
                    ok.add(RewriteResult.unmodified(a));
                }
            }
        }
        return ok;
    }

//    private List<IDDx> simplifyX(List<IDDx> all, DomainX fullDomain) {
//        Expression[] arr = all.toArray(new Expression[all.size()]);
//        boolean[] processed = new boolean[arr.length];
//        List<IDDx> ok = new ArrayList<IDDx>();
//        for (int i = 0; i < arr.length; i++) {
//            Expression a = arr[i];
//            for (int j = i + 1; j < arr.length; j++) {
//                Expression b = arr[j];
//                if (!processed[i] && !processed[j]) {
//                    IDDx r = simplifyX(a, b, fullDomain);
//                    if (r != null) {
//                        processed[i] = true;
//                        processed[j] = true;
//                        ok.add(r);
//                        break;
//                    }
//                }
//            }
//        }
//        return ok;
//    }

//    private IDDx simplifyX(Expr a, Expr b, Domain fullDomain) {
//        if (a instanceof DoubleX) {
//            DoubleX fa = (DoubleX) a;
//            if (b instanceof DoubleX) {
//                DoubleX fb = (DoubleX) b;
//                return new DoubleX(fa.value + fb.value, fa.getDomain());
//            } else if (b instanceof DDxLinear) {
//                DDxLinear fb = (DDxLinear) b;
//                return new DDxLinear(fa.getDomain(), fb.getA(), fb.getB() + fa.value);
//            } else if (b instanceof DDxPolynome) {
//                DDxPolynome fb = (DDxPolynome) b;
//                double[] coeffs = fb.getA();
//                coeffs[0] = coeffs[0] + fa.value;
//                return new DDxPolynome(fa.getDomain(), coeffs);
//            }
//        } else if (a instanceof DDxLinear) {
//            DDxLinear fa = (DDxLinear) a;
//            if (b instanceof DDxLinear) {
//                DDxLinear fb = (DDxLinear) b;
//                return new DDxLinear(fa.getDomain(), fa.getA() + fb.getA(), fa.getB() + fb.getB());
//            } else if (b instanceof DDxPolynome) {
//                DDxPolynome fb = (DDxPolynome) b;
//                double[] coeffs = fb.getA();
//                if (coeffs.length < 2) {
//                    double[] coeffs2 = new double[2];
//                    System.arraycopy(coeffs, 0, coeffs2, 0, coeffs.length);
//                    coeffs = coeffs2;
//                }
//                coeffs[0] = coeffs[0] + fa.getB();
//                coeffs[1] = coeffs[1] + fa.getA();
//                return new DDxPolynome(fa.getDomain(), coeffs);
//            }
//        } else if (a instanceof DDxPolynome) {
//            DDxPolynome fa = (DDxPolynome) a;
//            if (b instanceof DDxPolynome) {
//                DDxPolynome fb = (DDxPolynome) b;
//                double[] coeffs1 = fa.getA();
//                double[] coeffs2 = fb.getA();
//                double[] coeffs3 = new double[Math.max(coeffs1.length, coeffs2.length)];
//                for (int i = 0; i < coeffs1.length; i++) {
//                    coeffs3[i] = coeffs1[i];
//                }
//                for (int i = 0; i < coeffs2.length; i++) {
//                    coeffs3[i] += coeffs2[i];
//                }
//                return new DDxPolynome(fa.getDomain(), coeffs3);
//            }
//        } else if (a instanceof DDxIntegralX) {
//            DDxIntegralX fa = (DDxIntegralX) a;
//            if (b instanceof DDxIntegralX) {
//                DDxIntegralX fb = (DDxIntegralX) b;
//                if (fb.getX0() == fa.getX0() && fa.getIntegral().equals(fb.getIntegral())) {
//                    return new DDxIntegralX(MathsBase.sum(fa.getBase(), fb.getBase()).toDDx(), fa.getIntegral(), fa.getX0());
//                }
//            }
//        } else if (b instanceof DoubleX) {
//            return simplifyX(b, a, fullDomain);
//        } else if (b instanceof DDxLinear) {
//            return simplifyX(b, a, fullDomain);
//        } else if (b instanceof DDxPolynome) {
//            return simplifyX(b, a, fullDomain);
//        }
//        return null;
//    }
//
//    private List<Expression> simplifyXY(List<Expression> all, DomainXY fullDomain) {
//        Expression[] arr = all.toArray(new Expression[all.size()]);
//        boolean[] processed = new boolean[arr.length];
//        List<Expression> ok = new ArrayList<Expression>();
//        for (int i = 0; i < arr.length; i++) {
//            Expression a = arr[i];
//            for (int j = i + 1; j < arr.length; j++) {
//                Expression b = arr[j];
//                if (!processed[i] && !processed[j]) {
//                    Expression r = simplifyXY(a, b, fullDomain);
//                    if (r != null) {
//                        processed[i] = true;
//                        processed[j] = true;
//                        ok.add(r);
//                        break;
//                    }
//                }
//            }
//        }
//        return ok;
//    }

    private Expr simplifyXY(Expr a, Expr b, Domain fullDomain) {
        for (PlusPairSimplifier s : simplifiers.getAll(a.getClass(), b.getClass())) {
            Expr r = s.simplify(a, b, fullDomain, this);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    private static class DomainGroupedList {
        int domainDimension = 1;
        Map<Domain, List<Expr>> domainxy = new HashMap<Domain, List<Expr>>();
        //        Map<Domain, List<IDDx>> domainx = new HashMap<Domain, List<IDDx>>();
        Complex complex = Complex.ZERO;
        Domain fullxy = Domain.EMPTYX;
//        Domain fullx = Domain.EMPTYX;

        public void addxy(Expr e) {
            if (e.isZero()) {
                //ignore
                return;
            }
            if (e.isDD()) {
                Domain d = e.toDD().getDomain();
                get(d).add(e);
                fullxy = fullxy.expand(d);
            } else if (e.isDC()) {
                Domain d = e.toDC().getDomain();
                get(d).add(e);
                fullxy = fullxy.expand(d);
            } else if (e.isDM()) {
                Domain d = e.toDM().getDomain();
                get(d).add(e);
                fullxy = fullxy.expand(d);
            } else {
                throw new IllegalArgumentException("Unsupported");
            }

        }

//        public void add(Expression e) {
//            if (e.isZero()) {
//                //ignore
//                return;
//            }
//            if (e instanceof Complex) {
//                complex = complex.add(((Complex) e));
//            } else if (e.isDDx()) {
//                IDDx toDDx = e.toDDx();
//                DomainX d = toDDx.getDomain();
//                get(d).add(toDDx);
//                fullx = fullx.expand(d);
//            } else if (e.isDD()) {
//                DomainXY d = e.toDD().getDomain();
//                get(d).add(e);
//                fullxy = fullxy.expand(d);
//            } else if (e.isDC()) {
//                DomainXY d = e.toDC().getDomain();
//                get(d).add(e);
//                fullxy = fullxy.expand(d);
//            } else if (e.isDM()) {
//                DomainXY d = e.toDM().getDomain();
//                get(d).add(e);
//                fullxy = fullxy.expand(d);
//            } else {
//                throw new IllegalArgumentException("Unsupported");
//            }
//        }

        //TODO remove "domainx" map
//        public List<IDDx> get(DomainX d) {
//            List<IDDx> list = domainx.get(d);
//            if (list == null) {
//                list = new ArrayList<IDDx>();
//                domainx.put(d, list);
//            }
//            return list;
//        }

        public List<Expr> get(Domain d) {
            if (d.dimension() < domainDimension) {
                d = d.expandDimension(domainDimension);
            } else if (d.dimension() > domainDimension) {
                domainDimension = d.dimension();
                if (domainxy.size() > 0) {
                    Map<Domain, List<Expr>> domainxy2 = new HashMap<Domain, List<Expr>>();
                    for (Map.Entry<Domain, List<Expr>> domainListEntry : domainxy.entrySet()) {
                        domainxy2.put(
                                domainListEntry.getKey().expandDimension(domainDimension),
                                domainListEntry.getValue()
                        );
                    }
                    domainxy = domainxy2;
                }
            }
            List<Expr> list = domainxy.get(d);
            if (list == null) {
                list = new ArrayList<Expr>();
                domainxy.put(d, list);
            }
            return list;
        }
    }

    protected interface PlusPairSimplifier {
        Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule);
    }

    protected static class ReverseSimplifier implements PlusPairSimplifier {
        public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRule plusSimplifyRule) {
            return plusSimplifyRule.simplifyXY(b, a, domain);
        }
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
}
