/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.common.collections.ClassMap;
import net.thevpc.common.collections.ClassPairMapList;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public class PlusSimplifyRuleOld extends AbstractExpressionRewriterRule {

    public static final Class<? extends Expr>[] TYPES = new Class[]{Plus.class};
    public static final ExpressionRewriterRule INSTANCE = new PlusSimplifyRuleOld();
    private static final ReverseSimplifier REVERSE = new ReverseSimplifier();
    private final ClassPairMapList<PlusPairSimplifier> pairSimplifiers = new ClassPairMapList<>(Expr.class, Expr.class, PlusPairSimplifier.class, false);
    private final ClassMap<PlusSingleSimplifier> singleSimplifiers = new ClassMap<>(Expr.class, PlusSingleSimplifier.class);

    public PlusSimplifyRuleOld() {
        singleSimplifiers.put(DefaultDoubleValue.class, a -> {
            DefaultDoubleValue d = (DefaultDoubleValue) a;
            if (d.getDomain().isUnbounded1()) {
                return Complex.of(d.getValue());
            }
            return a;
        });
        singleSimplifiers.put(DefaultComplexValue.class, a -> {
            DefaultComplexValue d = (DefaultComplexValue) a;
            if (d.getDomain().isUnbounded1()) {
                return d.getValue();
            }
            if (d.getValue().isReal()) {
                return new DefaultDoubleValue(d.getValue().getReal(), d.getDomain());
            }
            return a;
        });
        pairSimplifiers.add(Expr.class, Expr.class, (a, b, domain, plusSimplifyRule) -> {
            if (a.equals(b)) {
                return Maths.mul(Complex.TWO, a);
            }
            return null;
        });
        pairSimplifiers.add(DefaultDoubleValue.class, DefaultDoubleValue.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultDoubleValue aa = (DefaultDoubleValue) a;
            DefaultDoubleValue bb = (DefaultDoubleValue) b;
            if (aa.isInfinite() && a.getDomain().getDimension() == 1 && bb.isInfinite() && bb.getDomain().getDimension() == 1) {
                return Complex.of(bb.value + aa.value);
            }
            return Maths.expr(aa.value + bb.value, aa.getDomain());
        });
        pairSimplifiers.add(DefaultDoubleValue.class, DefaultComplexValue.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultDoubleValue aa = (DefaultDoubleValue) a;
            DefaultComplexValue bb = (DefaultComplexValue) b;
            if (aa.isInfinite() && a.getDomain().getDimension() == 1 && bb.isInfinite() && bb.getDomain().getDimension() == 1) {
                return bb.getValue().plus(aa.value);
            }
            Domain dom = aa.getDomain();
            if (bb.getValue().isReal()) {
                return Maths.expr(aa.value + bb.getValue().getReal(), dom);
            }
            return new DefaultComplexValue(bb.getValue().plus(aa.value), dom);
        });
        pairSimplifiers.add(DefaultDoubleValue.class, Complex.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultDoubleValue aa = (DefaultDoubleValue) a;
            Complex bb = (Complex) b;
            if (aa.isInfinite() && a.getDomain().getDimension() == 1) {
                return bb.plus(aa.value);
            }
            if (bb.isReal()) {
                return Maths.expr(aa.value + bb.getReal(), aa.getDomain());
            }
            return new DefaultComplexValue(bb.plus(aa.value), aa.getDomain());
        });
        pairSimplifiers.add(Complex.class, Complex.class, (a, b, domain, plusSimplifyRule) -> {
            Complex aa = (Complex) a;
            Complex bb = (Complex) b;
            return aa.plus(bb);
        });
        pairSimplifiers.add(Complex.class, DefaultComplexValue.class, (a, b, domain, plusSimplifyRule) -> {
            Complex aa = (Complex) a;
            DefaultComplexValue bb = (DefaultComplexValue) b;
            //same domain!
            return aa.plus(bb.getValue());
        });
        pairSimplifiers.add(DefaultComplexValue.class, DefaultComplexValue.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultComplexValue aa = (DefaultComplexValue) a;
            DefaultComplexValue bb = (DefaultComplexValue) b;
            //same domain!
            Domain dom = aa.getDomain();
            return new DefaultComplexValue(aa.getValue().plus(bb.getValue()), dom);
        });
        pairSimplifiers.add(DefaultDoubleValue.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultDoubleValue aa = (DefaultDoubleValue) a;
            Linear bb = (Linear) b;
            return new Linear(bb.getA(), bb.getB(), aa.getValue() + bb.getC(), aa.getDomain());
        });
        pairSimplifiers.add(Complex.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            Complex aa = (Complex) a;
            Linear bb = (Linear) b;
            if (aa.isReal()) {
                return new Linear(bb.getA(), bb.getB(), aa.getReal(), aa.getDomain());
            }
            if (aa.isImag()) {
                return null;
            }
            return Plus.of(new Linear(bb.getA(), bb.getB(), aa.getReal(), aa.getDomain()), Complex.I(aa.getImag()));
        });
        pairSimplifiers.add(DefaultComplexValue.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultComplexValue aa = (DefaultComplexValue) a;
            Linear bb = (Linear) b;
            Domain dom = aa.getDomain();
            if (aa.getValue().isReal()) {
                return new Linear(bb.getA(), bb.getB(), aa.getValue().getReal(), dom);
            }
            if (aa.isNarrow(ExprType.COMPLEX_EXPR)) {
                return null;
            }
            return Plus.of(new Linear(bb.getA(), bb.getB(), aa.getValue().getReal(), dom), new DefaultComplexValue(Complex.I(aa.getValue().getImag()), dom));
        });
        pairSimplifiers.add(Complex.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            Complex aa = (Complex) a;
            Linear bb = (Linear) b;
            if (aa.isReal()) {
                return new Linear(bb.getA(), bb.getB(), aa.getReal() + bb.getC(), bb.getDomain());
            }
            return null;
        });
        pairSimplifiers.add(DefaultComplexValue.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultComplexValue aa = (DefaultComplexValue) a;
            Linear bb = (Linear) b;
            if (aa.getValue().isReal()) {
                return new Linear(bb.getA(), bb.getB(), aa.getValue().getReal() + bb.getC(), bb.getDomain());
            }
            return null;
        });
        pairSimplifiers.add(Linear.class, Linear.class, (a, b, domain, plusSimplifyRule) -> {
            Linear aa = (Linear) a;
            Linear bb = (Linear) b;
            return new Linear(aa.getA() + bb.getA(), aa.getB() + bb.getB(), aa.getC() + bb.getC(), bb.getDomain());
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
        pairSimplifiers.add(DefaultDoubleValue.class, DDiscrete.class, (a, b, domain, plusSimplifyRule) -> {
            DefaultDoubleValue aa = (DefaultDoubleValue) a;
            DDiscrete bb = (DDiscrete) b;
            Expr newCst = Maths.expr(aa.value, aa.getDomain());
            return bb.plus(newCst);
        });

        pairSimplifiers.add(Expr.class, Mul.class, (a, b, domain, plusSimplifyRule) -> {
            Mul bb = (Mul) b;
            List<Expr> bbs = bb.getChildren();
            if (bbs.size() == 2) {
                Expr b1 = Expressions.toConstantExprOrNull(bbs.get(0));
                Expr b2 = Expressions.toConstantExprOrNull(bbs.get(1));
                if (b1 != null && a.equals(bbs.get(1))) {
                    return Maths.mul(simplifyXY(Complex.ONE, b1, domain), a);
                }
                if (b2 != null && a.equals(bbs.get(0))) {
                    return Maths.mul(simplifyXY(Complex.ONE, b2, domain), a);
                }
            }
            return null;
        });
        pairSimplifiers.add(Mul.class, Expr.class, (a, b, domain, plusSimplifyRule) -> {
            Mul aa = (Mul) a;
            List<Expr> aas = aa.getChildren();
            if (aas.size() == 2) {
                Expr a1 = Expressions.toConstantExprOrNull(aas.get(0));
                if (a1 != null && b.equals(aas.get(1))) {
                    return Maths.mul(simplifyXY(Complex.ONE, a1, domain), b);
                }
                Expr a2 = Expressions.toConstantExprOrNull(aas.get(1));
                if (a2 != null && a.equals(aas.get(0))) {
                    return Maths.mul(simplifyXY(Complex.ONE, a2, domain), b);
                }
            }
            return null;
        });
        pairSimplifiers.add(Mul.class, Mul.class, (a, b, domain, plusSimplifyRule) -> {
            Mul aa = (Mul) a;
            Mul bb = (Mul) b;
            List<Expr> aas = aa.getChildren();
            List<Expr> bbs = bb.getChildren();
            if (aas.size() == 2 && bbs.size() == 2) {
                Expr aaas0 = aas.get(0);
                Expr aas1 = aas.get(1);
                Expr bbs0 = bbs.get(0);
                Expr bbs1 = bbs.get(1);
                Expr a1 = Expressions.toConstantExprOrNull(aaas0);
                Expr a2 = Expressions.toConstantExprOrNull(aas1);
                Expr b1 = Expressions.toConstantExprOrNull(bbs0);
                Expr b2 = Expressions.toConstantExprOrNull(bbs1);

                Expr an = null;
                Expr ax = null;
                if (a1 != null) {
                    an = a1;
                    ax = aas1;
                } else if (a2 != null) {
                    an = a2;
                    ax = aaas0;
                }

                Expr bn = null;
                Expr bx = null;
                if (b1 != null) {
                    bn = b1;
                    bx = bbs1;
                } else if (b2 != null) {
                    bn = b2;
                    bx = bbs0;
                }

                if (an != null && bn != null && ax.equals(bx)) {
                    return Maths.mul(simplifyXY(an, bn, domain), ax);
                }
            }
            return null;
        });
//        pairSimplifiers.add(Linear.class, DoubleValue.class, REVERSE);
////        simplifiers.add(DDxyDiscrete.class,DoubleXY.class, REVERSE);
//        pairSimplifiers.add(DDiscrete.class, DoubleValue.class, REVERSE);
//        pairSimplifiers.add(Complex.class, DoubleValue.class, REVERSE);
//        pairSimplifiers.add(ComplexValue.class, DoubleValue.class, REVERSE);
//        pairSimplifiers.add(ComplexValue.class, Complex.class, REVERSE);
//        pairSimplifiers.add(Linear.class, Complex.class, REVERSE);
//        pairSimplifiers.add(Linear.class, ComplexValue.class, REVERSE);
    }

    private Expr simplifyXY(Expr a, Expr b, Domain fullDomain) {
        Class<? extends Expr> cls1 = a.getClass();
        Class<? extends Expr> cls2 = b.getClass();
        List<PlusPairSimplifier> found = pairSimplifiers.getAll(cls1, cls2);

        for (PlusPairSimplifier s : found) {
            Expr r = s.simplify(a, b, fullDomain, this);
            if (r != null) {
                return r;
            }
        }
        if (!cls1.equals(cls2)) {
            found = pairSimplifiers.getAll(cls2, cls1);
            if (!found.isEmpty()) {
                for (PlusPairSimplifier s : found) {
                    Expr r = s.simplify(b, a, fullDomain, this);
                    if (r != null) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
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

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Plus ee = (Plus) e;
        DomainGroupedList grouped = new DomainGroupedList();
        RewriteResultType someModification = RewriteResultType.UNMODIFIED;
        for (Expr expression : ee.getChildren()) {
            RewriteResult rewrite = ruleset.rewrite(expression, targetExprType);
            if (!rewrite.isUnmodified()) {
                someModification = someModification.max(rewrite.getType());
                expression = rewrite.getValue();
            }
            PlusSingleSimplifier li = singleSimplifiers.get(expression.getClass());
            if (li != null) {
                expression = li.simplify(expression);
            }
            if (expression instanceof Plus) {
                someModification = someModification.max(RewriteResultType.NEW_VAL);
                for (Expr se : expression.getChildren()) {
                    grouped.addxy(se);
                }
            } else {
                grouped.addxy(expression);
            }
        }
//        for (Domain domain : grouped.domainx.keySet().toArray(new Domain[grouped.domainx.size()])) {
//            grouped.domainx.put(domain, simplifyX((List) grouped.get(domain), grouped.fullx));
//        }
        for (Domain domain : grouped.domainxy.keySet().toArray(new Domain[0])) {
            List<Expr> all = grouped.get(domain);
            BooleanRef modified = BooleanMarker.ref();
            grouped.domainxy.put(domain, simplifyXY(all, grouped.fullxy, modified));
            if (modified.get()) {
                someModification = someModification.max(modified.get() ? RewriteResultType.BEST_EFFORT : RewriteResultType.NEW_VAL);
            }
        }

        if (grouped.domainxy.size() > 0 || !grouped.complex.isReal()) {
            if (!grouped.complex.isZero()) {
                grouped.addxy(Maths.expr(grouped.complex, Domain.FULL(ee.getDomain().getDimension())));
            }
            List<Expr> all = new ArrayList<>();
            for (Map.Entry<Domain, List<Expr>> dl : grouped.domainxy.entrySet()) {
                all.addAll(dl.getValue());
            }
            if (all.isEmpty()) {
                return RewriteResult.bestEffort(Complex.ZERO);
            }
            switch (someModification) {
                case NEW_VAL: {
                    if (all.size() == 1) {
                        return RewriteResult.bestEffort(all.get(0));
                    } else {
                        return RewriteResult.newVal(Maths.sum(all.toArray(new Expr[0])));
                    }
                }
                case BEST_EFFORT: {
                    if (all.size() == 1) {
                        return RewriteResult.bestEffort(all.get(0));
                    } else {
                        return RewriteResult.bestEffort(Maths.sum(all.toArray(new Expr[0])));
                    }
                }
            }
            return RewriteResult.unmodified();
        } else {
            return RewriteResult.bestEffort(grouped.complex);
        }
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
//                    return new DDxIntegralX(Maths.sum(fa.getBase(), fb.getBase()).toDDx(), fa.getIntegral(), fa.getX0());
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

    private List<Expr> simplifyXY(List<Expr> all, Domain fullDomain, BooleanMarker modified) {
        int allSize = all.size();
        if (allSize < 2) {
            return all;
        }
        Expr[] arr = all.toArray(new Expr[0]);

        while (true) {
            boolean[] processed = new boolean[arr.length];
            List<Expr> ok = new ArrayList<Expr>();
            boolean optimized0 = false;
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
                                modified.set();
                                ok.add(r);
                                optimized = true;
                                break;
                            }
                        }
                    }
                    if (!optimized) {
                        ok.add(a);
                    } else {
                        optimized0 = true;
                    }
                }
            }
            if (!optimized0 || ok.size() == 1) {
                return ok;
            }
            arr = ok.toArray(new Expr[0]);
        }
    }

    protected interface PlusPairSimplifier {
        Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRuleOld plusSimplifyRule);
    }

    protected interface PlusSingleSimplifier {
        Expr simplify(Expr a);
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
            switch (e.getType()) {
                case DOUBLE_DOUBLE: {
                    Domain d = e.toDD().getDomain();
                    get(d).add(e);
                    fullxy = fullxy.expand(d);
                    break;
                }
                case DOUBLE_COMPLEX: {
                    Domain d = e.toDC().getDomain();
                    get(d).add(e);
                    fullxy = fullxy.expand(d);
                    break;
                }
                case DOUBLE_CVECTOR:
                case DOUBLE_CMATRIX: {
                    Domain d = e.toDM().getDomain();
                    get(d).add(e);
                    fullxy = fullxy.expand(d);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported");
                }
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

    protected static class ReverseSimplifier implements PlusPairSimplifier {
        public Expr simplify(Expr a, Expr b, Domain domain, PlusSimplifyRuleOld plusSimplifyRule) {
            return plusSimplifyRule.simplifyXY(b, a, domain);
        }
    }


}
