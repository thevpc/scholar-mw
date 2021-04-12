/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.common.collections.ClassMap;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.NumberExpr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.*;

/**
 * @author vpc
 */
public class PlusSimplifyRule extends AbstractExpressionRewriterRule {

    public static final Class<? extends Expr>[] TYPES = new Class[]{Plus.class};
    public static final ExpressionRewriterRule INSTANCE = new PlusSimplifyRule();
    private final ClassMap<PlusAccumulator> accumulators = new ClassMap<>(Expr.class, PlusAccumulator.class);

    public PlusSimplifyRule() {
        regAccumulator(Expr.class, (PlusAccumulator<Expr>) (a, context) -> context.inc(a, Complex.ONE));
        regAccumulator(DoubleValue.class, (PlusAccumulator<DoubleValue>) (a, context) -> context.addComplex(a.toDouble()));
        regAccumulator(DefaultComplexValue.class, (PlusAccumulator<DefaultComplexValue>) (a, context) -> context.addComplex(a.getValue()));
        regAccumulator(Complex.class, (PlusAccumulator<Complex>) (a, context) -> context.addComplex(a));
        regAccumulator(Linear.class, (PlusAccumulator<Linear>) (a, context) -> {
            if (context.linear == null) {
                context.linear = new MutableLinear(a.getA(), a.getB(), a.getC(), context.domain);
            } else {
                context.env.makBestModified();
                context.linear.add(a);
            }
        });
        regAccumulator(Mul.class, (PlusAccumulator<Mul>) (a, context) -> {
            ExprWithMultiplier m = extractMultiplier(a);
            context.inc(m.value, m.multiplier);
        });
    }

    private boolean isMultiplier(Expr e) {
        if (e instanceof NumberExpr) {
            return true;
        }
        if (e instanceof DoubleParam) {
            return true;
        }
        if (e instanceof Plus) {
            Plus p = (Plus) e;
            for (Expr c : p.getChildren()) {
                if (!isMultiplier(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private ExprWithMultiplier extractMultiplier(Mul e) {
        List<Expr> multipliers = new ArrayList<>();
        List<Expr> values = new ArrayList<>();
        for (Expr se : e.getChildren()) {
            if (isMultiplier(se)) {
                multipliers.add(se);
            } else {
                values.add(se);
            }
        }
        switch (multipliers.size()) {
            case 0: {
                return new ExprWithMultiplier(Complex.ONE, e);
            }
            case 1: {
                switch (values.size()) {
                    case 0:
                        throw new IllegalArgumentException("Unexpected");
                    case 1:
                        return new ExprWithMultiplier(multipliers.get(0), values.get(0));
                    default:
                        return new ExprWithMultiplier(multipliers.get(0), Mul.of(values.toArray(new Expr[0])));
                }
            }
            default: {
                switch (values.size()) {
                    case 0: {
                        Expr val=null;
                        for (Iterator<Expr> iterator = multipliers.iterator(); iterator.hasNext(); ) {
                            Expr ee = iterator.next();
                            if (ee.hasParams()) {
                                val=ee;
                                iterator.remove();
                                break;
                            }
                        }
                        if(val==null){
                            val=multipliers.remove(0);
                        }
                        return new ExprWithMultiplier(Mul.of(multipliers.toArray(new Expr[0])), val);
                    }
                    case 1:
                        return new ExprWithMultiplier(Mul.of(multipliers.toArray(new Expr[0])), values.get(0));
                    default:
                        return new ExprWithMultiplier(Mul.of(multipliers.toArray(new Expr[0])), Mul.of(values.toArray(new Expr[0])));
                }
            }
        }
    }

    private void regAccumulator(Class cls, PlusAccumulator acc) {
        accumulators.put(cls, acc);
    }

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Plus ee = (Plus) e;
        DomainAccumulateContextList grouped = new DomainAccumulateContextList(ruleset);
        boolean initialModified = false;
        for (Expr expression : ee.getChildren()) {
            RewriteResult rewrite = ruleset.rewrite(expression, targetExprType);
            if (!rewrite.isUnmodified()) {
                initialModified = true;
                grouped.modified(rewrite.getType());
                expression = rewrite.getValue();
            }
            if (expression instanceof Plus) {
                grouped.someModification = grouped.someModification.max(RewriteResultType.NEW_VAL);
                for (Expr se : expression.getChildren()) {
                    grouped.accumulate(se);
                }
            } else {
                grouped.accumulate(expression);
            }
        }
        List<Expr> p2List = new ArrayList<>();
        for (DomainAccumulateContext c : grouped.byDomain.values()) {
            if (c.linear != null) {
                if (c.complex != null && c.complex.getReal() != 0) {
                    c.linear.addC(c.complex.getReal());
                    c.complex.setReal(0);
                }
                if (!c.linear.isZero()) {
                    p2List.add(c.linear.toImmutable());
                }
            }
            for (Map.Entry<Expr, Expr> e2m : c.exprToMultipliers.entrySet()) {
                Expr e1 = e2m.getKey();
                Expr m1 = e2m.getValue();
                RewriteResult m1r = ruleset.rewrite(m1, targetExprType);
                if (!m1r.isUnmodified()) {
                    m1 = m1r.getValue();
                }
                if (m1.isZero()) {
                    //
                } else if (m1.equals(Complex.ONE)) {
                    initialModified=true;
                    p2List.add(e1);
                } else {
                    initialModified=true;
                    p2List.add(Mul.of(m1, e1));
                }
            }

            if (c.complex != null && !c.complex.isZero()) {
                if (c.domain.isUnbounded1()) {
                    p2List.add(c.complex.toComplex());
                } else if (c.complex.isReal()) {
                    p2List.add(Maths.expr(c.complex.getReal(), c.domain));
                } else {
                    p2List.add(Maths.expr(c.complex.toComplex(), c.domain));
                }
            }
        }

        switch (p2List.size()) {
            case 0:
                return RewriteResult.bestEffort(Complex.ZERO);
            case 1:
                return RewriteResult.bestEffort(p2List.get(0));
            default: {
                switch (grouped.someModification) {
                    case NEW_VAL:
                    case BEST_EFFORT: {
                        initialModified=true;
                        break;
                    }
                }
                if(initialModified){
                    return RewriteResult.bestEffort(Plus.of(p2List.toArray(new Expr[0])));
                }
                List<Expr> oldChildren = ee.getChildren();
                if (oldChildren.equals(p2List)) {
                    return RewriteResult.unmodified();
                }
                return RewriteResult.bestEffort(Plus.of(p2List.toArray(new Expr[0])));
            }
        }
    }

    private PlusAccumulator getAccumulator(Expr e) {
        PlusAccumulator t = accumulators.get(e.getClass());
        if (t == null) {
            throw new IllegalArgumentException("No Accumulator for " + e.getClass() + " :: " + e);
        }
        return t;
    }

    protected interface PlusAccumulator<T extends Expr> {
        void accumulate(T a, DomainAccumulateContext context);
    }

    private static class DomainAccumulateContext {
        DomainAccumulateContextList env;
        ExpressionRewriter ruleset;
        int domainDimension = 1;
        MutableComplex complex;
        MutableLinear linear = null;
        Domain domain = Domain.EMPTYX;
        Map<Expr, Expr> exprToMultipliers = new LinkedHashMap<>();

        public DomainAccumulateContext(DomainAccumulateContextList env) {
            this.env = env;
        }

        private void addComplex(Complex c) {
            if (complex == null) {
                complex = MutableComplex.of(c);
            } else {
                complex.add(c);
                env.makBestModified();
            }
        }

        private void addComplex(double c) {
            if (complex == null) {
                complex = new MutableComplex(c, 0);
            } else {
                complex.add(c);
                env.makBestModified();
            }
        }

        private void inc(Expr a, Expr multiplier) {
            Expr t = exprToMultipliers.get(a);
            if (t == null) {
                exprToMultipliers.put(a, multiplier);
            } else {
                env.makBestModified();
                exprToMultipliers.put(a, multiplier.plus(t));
            }
        }
    }

    private class ExprWithMultiplier {
        Expr multiplier;
        Expr value;

        public ExprWithMultiplier(Expr multiplier, Expr value) {
            this.multiplier = multiplier;
            this.value = value;
        }
    }

    private class DomainAccumulateContextList {
        DomainAccumulateContext global;
        Map<Domain, DomainAccumulateContext> byDomain = new HashMap<Domain, DomainAccumulateContext>();
        RewriteResultType someModification = RewriteResultType.UNMODIFIED;

        public DomainAccumulateContextList(ExpressionRewriter ruleset) {
            global = new DomainAccumulateContext(this);
            global.ruleset = ruleset;
        }

        public void makBestModified() {
            modified(RewriteResultType.BEST_EFFORT);
        }

        public void modified(RewriteResultType t) {
            someModification = someModification.max(t);
        }

        public void makNewModified() {
            modified(RewriteResultType.NEW_VAL);
        }

        public void accumulate(Expr e) {
            if (e.isZero()) {
                //ignore
                return;
            }
            getAccumulator(e).accumulate(e, get(e.getDomain()));
        }

        public DomainAccumulateContext get(Domain d) {
            global.domain = global.domain.expand(d);
            if (d.dimension() < global.domainDimension) {
                d = d.expandDimension(global.domainDimension);
            } else if (d.dimension() > global.domainDimension) {
                global.domainDimension = d.dimension();
                if (byDomain.size() > 0) {
                    Map<Domain, DomainAccumulateContext> domainxy2 = new HashMap<>();
                    for (Map.Entry<Domain, DomainAccumulateContext> domainListEntry : byDomain.entrySet()) {
                        Domain dom2 = domainListEntry.getKey().expandDimension(global.domainDimension);
                        DomainAccumulateContext ctx2 = domainListEntry.getValue();
                        ctx2.domain = dom2;
                        domainxy2.put(
                                dom2,
                                ctx2
                        );
                    }
                    byDomain = domainxy2;
                }
            }
            DomainAccumulateContext list = byDomain.get(d);
            if (list == null) {
                list = new DomainAccumulateContext(this);
                list.domain = d;
                list.ruleset = global.ruleset;
                byDomain.put(d, list);
            }
            return list;
        }
    }


}
