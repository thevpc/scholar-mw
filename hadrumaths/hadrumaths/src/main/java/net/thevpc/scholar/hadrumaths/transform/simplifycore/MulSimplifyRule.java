/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.common.util.ClassMap;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.ExpressionsDebug;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Div;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Inv;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Cos;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Sin;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.*;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class MulSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new MulSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Mul.class};
    private final ClassMap<MulAccumulator> accumulators = new ClassMap<>(Expr.class, MulAccumulator.class);

    public MulSimplifyRule() {
        regAccumulator(Complex.class, (MulAccumulator<Complex>) (a, context) -> context.mul(a));
        regAccumulator(DefaultComplexValue.class, (MulAccumulator<DefaultComplexValue>) (a, context) -> context.mul(a.toComplex()));
        regAccumulator(DoubleValue.class, (MulAccumulator<DoubleValue>) (a, context) -> context.mul(a.toComplex()));
        regAccumulator(Inv.class, (MulAccumulator<Inv>) (a, context) -> {
            Expr inved = a.getChild(0);
            context.dec(inved);
        });
        regAccumulator(Div.class, (MulAccumulator<Div>) (a, context) -> {
            Expr first = a.getFirst();
            if (first.isNarrow(ExprType.COMPLEX_EXPR)) {
                context.mul(first.toComplex());
                context.dec(a.getSecond());
            } else {
                accumulateDefault(a, context);
            }
        });
        regAccumulator(CosXCosY.class, (MulAccumulator<CosXCosY>) (a, context) -> {
            accumulateCosXCosY(new Trigo(a), context);
        });
        regAccumulator(Domain.class, (MulAccumulator<Domain>) (a, context) -> {
        });

        regAccumulator(Cos.class, (MulAccumulator<Cos>) (a, context) -> {
            Trigo t = new Trigo(a);
            if (t.cosXCosY != null) {
                accumulateCosXCosY(t, context);
            } else {
                accumulateDefault(a, context);
            }
        });

        regAccumulator(Sin.class, (MulAccumulator<Sin>) (a, context) -> {
            Trigo t = new Trigo(a);
            if (t.cosXCosY != null) {
                accumulateCosXCosY(t, context);
            } else {
                accumulateDefault(a, context);
            }
        });

        regAccumulator(Expr.class, (MulAccumulator<Expr>) (a, context) -> accumulateDefault(a, context));
    }

    private void regAccumulator(Class cls, MulAccumulator acc) {
        accumulators.put(cls, acc);
    }

    private void accumulateDefault(Expr a, DomainAccumulateContext context) {
        context.inc(a);
    }

    private void accumulateCosXCosY(Trigo a, DomainAccumulateContext context) {
        for (Iterator<Trigo> iterator = context.partialTrigos.iterator(); iterator.hasNext(); ) {
            Trigo cosXcosY = iterator.next();
            CosXCosY old = cosXcosY.cosXCosY;
            if (old.getA() == 0 && a.cosXCosY.getC() == 0) {
                context.mul(old.getAmp() * a.cosXCosY.getAmp() * cos2(old.getB()) * cos2(a.cosXCosY.getD()));
                cosXcosY = new Trigo(new CosXCosY(
                        1,
                        a.cosXCosY.getA(),
                        a.cosXCosY.getB(),
                        old.getC(),
                        old.getD(),
                        context.domain
                ));
                context.fullTrigos.add(cosXcosY);
                iterator.remove();
                return;
            } else if (old.getC() == 0 && a.cosXCosY.getA() == 0) {
                context.mul(old.getAmp() * a.cosXCosY.getAmp() * cos2(old.getD()) * cos2(a.cosXCosY.getB()));
                cosXcosY = new Trigo(new CosXCosY(1, old.getA(), old.getB(), a.cosXCosY.getC(), a.cosXCosY.getD(), context.domain));
                context.fullTrigos.add(cosXcosY);
                iterator.remove();
                return;
            }
        }
        if (a.cosXCosY.getAmp() == 1) {
            context.partialTrigos.add(a);
        } else {
            context.mul(a.cosXCosY.getAmp());
            context.partialTrigos.add(new Trigo(new CosXCosY(
                    1,
                    a.cosXCosY.getA(),
                    a.cosXCosY.getB(),
                    a.cosXCosY.getC(),
                    a.cosXCosY.getD(),
                    context.domain
            )));
        }
    }

    public static CosXCosY toCosXCosY(Sin c) {
        Expr t = c.getChild(0);
        Linear r = Linear.castOrConvert(t);
        if (r != null) {
            if (r.getA() == 0) {
                return (new CosXCosY(1, 0, 0, r.getB(), r.getC() - PI / 2, c.getDomain().intersect((t).getDomain())));
            } else if (r.getB() == 0) {
                return (new CosXCosY(1, r.getA(), r.getC() - PI / 2, 0, 0, c.getDomain().intersect((t).getDomain())));
            }
        }
        return null;
    }

    public static CosXPlusY toCosXPlusCosY(Sin c) {
        Expr t = c.getChild(0);
        Linear r = Linear.castOrConvert(t);
        if (r != null) {
            if (r.getA() != 0 && r.getB() != 0) {
                return (new CosXPlusY(1, r.getA(), r.getA(), r.getC() - PI / 2, c.getDomain().intersect((t).getDomain())));
            }
        }
        return null;
    }

    private static CosXCosY toCosXCosY(Cos c) {
        Expr t = c.getChild(0);
        Linear r = Linear.castOrConvert(t);
        if (r != null) {
            if (r.getA() == 0) {
                return (new CosXCosY(1, 0, 0, r.getB(), r.getC(), t.getDomain().intersect((t).getDomain())));
            } else if (r.getB() == 0) {
                return (new CosXCosY(1, r.getA(), r.getC(), 0, 0, t.getDomain().intersect((t).getDomain())));
            }
        }
        return null;
    }

    private static CosXPlusY toCosXPlusCosY(Cos c) {
        Expr t = c.getChild(0);
        Linear r = Linear.castOrConvert(t);
        if (r != null) {
            if (r.getA() != 0 && r.getB() != 0) {
                return (new CosXPlusY(1, r.getA(), r.getB(), r.getC(), t.getDomain().intersect((t).getDomain())));
            }
        }
        return null;
    }

    protected int exprMulOrder(Expr expr) {
        switch (expr.getClass().getName()) {
            case "net.thevpc.scholar.hadrumaths.DoubleExpr":
            case "net.thevpc.scholar.hadrumaths.Domain":
            case "net.thevpc.scholar.hadrumaths.DomainX":
            case "net.thevpc.scholar.hadrumaths.DomainXY":
            case "net.thevpc.scholar.hadrumaths.DomainXYZ":
            case "net.thevpc.scholar.hadrumaths.Complex":
            case "net.thevpc.scholar.hadrumaths.DefaultComplexValue":
            case "net.thevpc.scholar.hadrumaths.DefaultDoubleValue":
                return 1;

            case "net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Cos":
            case "net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Sin":
            case "net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY":
                return 2;
        }
        return 10;
    }

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
//        if(e.getSubExpressions().get(0) instanceof DoubleValue && e.getSubExpressions().get(1) instanceof Complex){
//            System.out.println("Why");
//        }
        RewriteResult e2 = rewrite0(e, ruleset);
        return e2;
    }

    public RewriteResult rewrite0(Expr e, ExpressionRewriter ruleset) {
//        RewriteResult rewriteResult = rewrite00(e, ruleset);
//        return rewriteResult;
//    }
//    public RewriteResult rewrite00(Expr e, ExpressionRewriter ruleset) {

        Mul ee = (Mul) e;
        ExprType nt = ee.getNarrowType();
        DomainAccumulateContext context = new DomainAccumulateContext(ruleset);
        context.domain = Domain.FULL(ee.getDomain().getDimension());
        RewriteResultType someModification = RewriteResultType.UNMODIFIED;
        List<Expr> eeChildren = ee.getChildren();
        int lastOrder=0;
        for (Expr expression : eeChildren) {
            RewriteResult rewrite = ruleset.rewrite(expression, null/*nt*/);
            expression = rewrite.isUnmodified() ? expression : rewrite.getValue();
            if (rewrite.isRewritten()) {
                someModification = someModification.max(rewrite.getType());
            }
            context.domain = context.domain.intersect(expression.getDomain());
            if (expression instanceof Mul) {
                someModification = someModification.max(RewriteResultType.NEW_VAL);
                for (Expr subExpression : expression.getChildren()) {
                    context.accumulate(subExpression);
                }
            } else {
                int o=exprMulOrder(expression);
                if(o<lastOrder){
                    context.modified=true;
                }
                lastOrder=o;
                context.accumulate(expression);
            }
        }
        if (context.specialEnd) {
            if (context.complex.isZero()) {
                return RewriteResult.bestEffort(Complex.ZERO);
            } else if (context.complex.isNaN()) {
                return RewriteResult.bestEffort(context.complex.toComplex());
            } else {
                throw new IllegalArgumentException("Unexpected");
            }
        } else {
            List<Expr> mlist = new ArrayList<>();
            boolean ampProcessed = false;
            boolean domainProcessed = false;
            if (context.complex.isOne()) {
                ampProcessed = true;
            }
            if (context.domain.isUnbounded1()) {
                domainProcessed = true;
            }

            for (Iterator<Trigo> iterator = context.fullTrigos.iterator(); iterator.hasNext(); ) {
                Trigo cosXcosY = iterator.next();
                if (!ampProcessed) {
                    if (context.complex.isReal()) {
                        cosXcosY = new Trigo((CosXCosY) cosXcosY.cosXCosY.mul(context.complex.getReal()));
                        context.complex.setOne();
                        ampProcessed = true;
                        context.modified = true;
                    }
                }
                if (!domainProcessed) {
                    cosXcosY = new Trigo((CosXCosY) cosXcosY.cosXCosY.mul(context.domain));
                    domainProcessed = true;
                }
                accumulateDefault(cosXcosY.base, context);
                iterator.remove();
            }
            for (Iterator<Trigo> iterator = context.partialTrigos.iterator(); iterator.hasNext(); ) {
                Trigo cosXcosY = iterator.next();
                if (!ampProcessed) {
                    if (context.complex.isReal()) {
                        cosXcosY = new Trigo((CosXCosY) cosXcosY.cosXCosY.mul(context.complex.getReal()));
                        context.complex.setOne();
                        ampProcessed = true;
                    }
                }
                if (!domainProcessed) {
                    cosXcosY = new Trigo((CosXCosY) cosXcosY.cosXCosY.mul(context.domain));
                    domainProcessed = true;
                }
                accumulateDefault(cosXcosY.base, context);
                iterator.remove();
            }

            for (Map.Entry<Expr, Integer> eentry : context.exprCount.entrySet()) {
                Expr mex = eentry.getKey();
                int value = eentry.getValue();
                switch (value) {
                    case 0: {
                        //ignore
                        break;
                    }
                    case 1: {
                        if (!ampProcessed) {
                            if (context.complex.isReal()) {
                                if (mex.isSmartMulDouble()) {
                                    mex = mex.mul(context.complex.getReal());
                                    context.complex.setOne();
                                    ampProcessed = true;
                                }
                            } else if (mex.isSmartMulComplex()) {
                                mex = mex.mul(context.complex.toComplex());
                                context.complex.setOne();
                                ampProcessed = true;
                            }
                        }
                        if (!domainProcessed) {
                            if (mex.isSmartMulDomain()) {
                                mex = mex.mul(context.domain);
                                domainProcessed = true;
                            }
                        }
                        mlist.add(mex);
                        break;
                    }
                    case -1: {
                        if (!ampProcessed) {
                            if (context.complex.isReal()) {
                                if (mex.isSmartMulDouble()) {
                                    mex = Inv.of(mex.mul(1.0 / context.complex.getReal()));
                                    context.complex.setOne();
                                    ampProcessed = true;
                                }
                            } else if (mex.isSmartMulComplex()) {
                                context.complex.inv();
                                mex = Inv.of(mex.mul(context.complex.toComplex()));
                                context.complex.setOne();
                                ampProcessed = true;
                            }
                        }
                        if (!domainProcessed) {
                            if (mex.isSmartMulDomain()) {
                                mex = mex.mul(context.domain);
                                domainProcessed = true;
                            }
                        }
                        mlist.add(mex);
                        break;
                    }
                    default: {
                        mlist.add(pow(mex, expr((value))));
                    }
                }
            }
            if (!ampProcessed) {
                //multiplier always first
                if (!domainProcessed) {
                    if (context.domain.isUnbounded1()) {
                        mlist.add(0, context.complex.toComplex());
                    } else if (context.complex.isReal()) {
                        mlist.add(0, expr(context.complex.getReal(), context.domain));
                    } else {
                        mlist.add(0, expr(context.complex.toComplex(), context.domain));
                    }
                    domainProcessed = true;
                } else {
                    if (context.complex.isReal()) {
                        mlist.add(0, expr(context.complex.getReal(), context.domain));
                    } else {
                        mlist.add(0, context.complex.toComplex());
                    }
                }
            } else {
                if (!domainProcessed) {
                    if (context.domain.isUnbounded1()) {
                        mlist.add(0, Maths.ONE);
                    } else {
                        mlist.add(0, context.domain);
                    }
                    domainProcessed = true;
                } else {
                    if (mlist.isEmpty()) {
                        mlist.add(Maths.ONE);
                    }
                }
            }
            if (mlist.isEmpty()) {
                mlist.add(Maths.ONE);
            }
            if (context.modified || mlist.size() != eeChildren.size()) {
                someModification = RewriteResultType.BEST_EFFORT;
            }
            if (mlist.size() == 1) {
                return RewriteResult.bestEffort(mlist.get(0));
            } else {
                Mul newVal = Mul.of(mlist.toArray(new Expr[0]));
                switch (someModification) {
                    case UNMODIFIED: {
                        if(ExpressionsDebug.DEBUG) {
                            if (newVal.equals(e)) {
                                return RewriteResult.unmodified();
                            }
                            return RewriteResult.bestEffort(newVal);
                        }
                        return RewriteResult.unmodified();
                    }
                    case BEST_EFFORT:
                    case NEW_VAL: {
                        return RewriteResult.bestEffort(newVal);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            }
        }
    }

    private MulAccumulator getAccumulator(Expr e) {
        MulAccumulator t = accumulators.get(e.getClass());
        if (t == null) {
            throw new IllegalArgumentException("No Accumulator for " + e.getClass() + " :: " + e);
        }
        return t;
    }

    protected interface MulAccumulator<T extends Expr> {
        void accumulate(T a, DomainAccumulateContext context);
    }

    private static class Trigo {
        private final Expr base;
        private final CosXCosY cosXCosY;
        private Cos cos;
        private Sin sin;

        public Trigo(CosXCosY base) {
            this.base = base;
            this.cosXCosY = base;
        }

        public Trigo(Cos base) {
            this.base = base;
            this.cos = base;
            this.cosXCosY = toCosXCosY(base);
        }

        public Trigo(Sin base) {
            this.base = base;
            this.sin = base;
            this.cosXCosY = toCosXCosY(base);
        }
    }

    private class DomainAccumulateContext {
        ExpressionRewriter ruleset;
        Domain domain = Domain.EMPTYX;
        MutableComplex complex = MutableComplex.One();
        boolean specialEnd;
        List<Trigo> fullTrigos = new ArrayList<>();
        List<Trigo> partialTrigos = new ArrayList<>();
        Map<Expr, Integer> exprCount = new LinkedHashMap<>();
        int complexMuls = 0;
        boolean modified = false;

        public DomainAccumulateContext(ExpressionRewriter ruleset) {
            this.ruleset = ruleset;
        }

        private void mul(double c) {
            mul(Complex.of(c));
        }

        private void mul(Complex c) {
            complex.mul(c);
            complexMuls++;
            if (complexMuls > 1) {
                modified = true;
            }
        }

        private void inc(Expr a) {
            Integer t = exprCount.get(a);
            if (t == null) {
                exprCount.put(a, 1);
            } else {
                modified = true;
                exprCount.put(a, t + 1);
            }
        }

        private void dec(Expr a) {
            modified = true;
            Integer t = exprCount.get(a);
            if (t == null) {
                exprCount.put(a, -1);
            } else {
                exprCount.put(a, t - 1);
            }
        }

        public void accumulate(Expr e) {
            if (specialEnd) {
                modified = true;
                //ignore
            } else {
                domain = domain.intersect(e.getDomain());
                if (e.isZero() || domain.isEmpty()) {
                    specialEnd = true;
                    complex.setZero();
                } else if (e.isNaN() || domain.isNaN()) {
                    specialEnd = true;
                    complex.setNaN();
                } else {
                    getAccumulator(e).accumulate(e, this);
                }
            }
        }
    }
}
