/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class MulSimplifyRuleOld extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new MulSimplifyRuleOld();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Mul.class};

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
        Complex complexeVal = Complex.ONE;
        List<Expr> all = new ArrayList<Expr>();
        Domain fullDomain = Domain.FULL(ee.getDomain().getDimension());
        RewriteResultType someModification = RewriteResultType.UNMODIFIED;
        List<Expr> linearized = new ArrayList<Expr>();
        for (Expr expression : ee.getChildren()) {
            RewriteResult rewrite = ruleset.rewrite(expression, nt);
            expression = rewrite.isUnmodified() ? expression : rewrite.getValue();
            if (rewrite.isRewritten()) {
                someModification = someModification.max(rewrite.getType());
            }
            fullDomain = fullDomain.intersect(expression.getDomain());
            if (expression instanceof Mul) {
                someModification = someModification.max(RewriteResultType.NEW_VAL);
                linearized.addAll(expression.getChildren());
            } else {
                linearized.add(expression);
            }
        }
        switch (linearized.size()) {
            case 0:
                return RewriteResult.bestEffort(Complex.ZERO);
            case 1:
                return RewriteResult.bestEffort(linearized.get(0));
        }
        for (Expr expression : linearized) {
            if (expression.isZero()) {
                return RewriteResult.bestEffort(Complex.ZERO);
            }
            if (expression instanceof Complex) {
                complexeVal = complexeVal.mul((Complex) expression);
//            } else if (expression.isDDx()) {
//                IDDx v = expression.toDDx();
//                all.add(v);
//                DomainX d = v.getDomain();
//                fullDomain = fullDomain.intersect(new DomainXY(d.xmin, d.xmax));
            } else {
                switch (expression.getType()) {
                    case DOUBLE_DOUBLE: {
                        DoubleToDouble v = expression.toDD();
                        all.add(v);
                        fullDomain = fullDomain.intersect(v.getDomain());
                        break;
                    }
                    case DOUBLE_COMPLEX: {
                        DoubleToComplex v = expression.toDC();
                        all.add(v);
                        fullDomain = fullDomain.intersect(v.getDomain());
                        break;
                    }
                    case DOUBLE_CVECTOR:
                    case DOUBLE_CMATRIX: {
                        DoubleToMatrix v = expression.toDM();
                        all.add(v);
                        fullDomain = fullDomain.intersect(v.getDomain());
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported Expression Type " + expression);
                    }
                }
            }
        }
        if (fullDomain.isEmpty()) {
            return RewriteResult.bestEffort(Complex.ZERO);
        } else if (fullDomain.isNaN()) {
            return RewriteResult.bestEffort(Complex.NaN);
        }
        if (!complexeVal.equals(Complex.ONE)) {
            all.add(complexeVal);
        }
        BooleanMarker modified = BooleanMarker.create();
        all = simplifyXY(all, fullDomain, modified);
        //no primitiveElement3D in the multiplication, all are ones, so discarded!
        //we return one in that case
        if (all.size() == 0) {
            return RewriteResult.bestEffort(Complex.ONE);
        }
        if (all.size() == 1) {
            Expr value = all.get(0);
            if (e.equals(value)) {
                return RewriteResult.unmodified();
            }
            return RewriteResult.newVal(value);
        }
        Expr m2 = Maths.prod(all.toArray(new Expr[0]));
        if (someModification == RewriteResultType.UNMODIFIED && m2.equals(e)) {
            return RewriteResult.unmodified();
        }
        //TODO should we return bestEffort? test me please
        return RewriteResult.newVal(m2);
    }

    protected List<Expr> simplifyXY(List<Expr> all, Domain fullDomain, BooleanMarker modified) {
        if (all.size() < 2) {
            return all;
        }
        Expr[] arr = all.toArray(new Expr[0]);
        boolean[] processed = new boolean[arr.length];
        List<Expr> ok = new ArrayList<Expr>();
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
                            ok.add(r);
                            optimized = true;
                            break;
                        }
                    }
                }
                if (!optimized) {
                    ok.add(a);
                }
            }
        }
        return ok;
    }

    private List<Expr> simplifyXY2(List<Expr> all, Domain fullDomain, BooleanMarker modified) {
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

    protected Expr simplifyXY(Expr a, Expr b, Domain fullDomain) {
        if (a.isNarrow(ExprType.COMPLEX_EXPR)) {
            return b.mul(a.toComplex()).mul(a.getDomain());
        }
        //process cross
//        if (a.isDouble()) {
//            double cst = a.toDouble();
//            //b.
//        }

        if (a.isNarrow(ExprType.DOUBLE_EXPR)) {
            double cst = a.toDouble();
            //UPDATED
            Domain theDomain = fullDomain.intersect(b.getDomain());
            boolean domainChanged = !theDomain.equals(b.getDomain());
            if (!domainChanged) {
                if (cst == 1) {
                    return b;
                }
            }
            boolean expSimplifiableIfMulDomain = isExpSimplifiableIfMulDomain(b);
            boolean expSimplifiableIfMulDouble = isExpSimplifiableIfMulDouble(b);
            if (domainChanged) {
                if (expSimplifiableIfMulDomain && expSimplifiableIfMulDouble) {
                    return b.mul(fullDomain).mul(cst);
                }
            } else {
                if (expSimplifiableIfMulDouble) {
                    return b.mul(cst);
                }
            }
            return null;
        } else if (a.isNarrow(ExprType.COMPLEX_EXPR)) {
            Complex cst = a.toComplex();
            //UPDATED
            Domain theDomain = fullDomain.intersect(b.getDomain());
            boolean domainChanged = !theDomain.equals(b.getDomain());
            if (!domainChanged) {
                if (cst.equals(Maths.CONE)) {
                    return b;
                }
            }
            boolean expSimplifiableIfMulDomain = isExpSimplifiableIfMulDomain(b);
            boolean expSimplifiableIfMulDouble = isExpSimplifiableIfMulComplex(b);
            if (domainChanged) {
                if (expSimplifiableIfMulDomain && expSimplifiableIfMulDouble) {
                    return b.mul(fullDomain).mul(cst);
                }
            } else {
                if (expSimplifiableIfMulDouble) {
                    return b.mul(cst);
                }
            }
            return null;
        } else if (a instanceof XX) {
            XX ac = (XX) a;
            if (b instanceof XX) {
                XX f = (XX) b;
                return Maths.pow(new XX(ac.getDomain().intersect(f.getDomain())), Maths.expr(2));
            } else if (b instanceof Pow && ((Pow) b).getFirst() instanceof XX) {
                XX f = (XX) ((Pow) b).getFirst();
                return Maths.pow(new XX(ac.getDomain().intersect(f.getDomain()).intersect(b.getDomain())), Maths.sum(((Pow) b).getSecond(), Maths.expr(1)));
            } else {
                return null;
            }
        } else if (a instanceof YY) {
            YY ac = (YY) a;
            if (b instanceof YY) {
                YY f = (YY) b;
                return Maths.pow(new YY(ac.getDomain().intersect(f.getDomain())), Maths.expr(2));
            } else if (b instanceof Pow && ((Pow) b).getFirst() instanceof YY) {
                YY f = (YY) ((Pow) b).getFirst();
                return Maths.pow(new YY(ac.getDomain().intersect(f.getDomain()).intersect(b.getDomain())), Maths.sum(((Pow) b).getSecond(), Maths.expr(1)));
            } else {
                return null;
            }

        } else if (a instanceof CosXCosY && b instanceof CosXCosY) {
            CosXCosY aa = (CosXCosY) a;
            CosXCosY bb = (CosXCosY) b;
            if (aa.getA() == 0 && bb.getC() == 0) {
                return new CosXCosY(
                        aa.getAmp() * bb.getAmp() * Maths.cos2(aa.getB()) * Maths.cos2(bb.getD()),
                        bb.getA(),
                        bb.getB(),
                        aa.getC(),
                        aa.getD(),
                        fullDomain
                );
            } else if (aa.getC() == 0 && bb.getA() == 0) {
                return new CosXCosY(
                        aa.getAmp() * bb.getAmp() * Maths.cos2(aa.getD()) * Maths.cos2(bb.getB()),
                        aa.getA(),
                        aa.getB(),
                        bb.getC(),
                        bb.getD(),
                        fullDomain
                );
            }
            return null;
        }
        if (
                a.isNarrow(ExprType.COMPLEX_EXPR) || b instanceof AxisFunction
        ) {
            return simplifyXY(b, a, fullDomain);
        }
        return null;
    }

    private boolean isExpSimplifiableIfMulDouble(Expr b) {
        return b instanceof Domain || b instanceof Complex || b instanceof DefaultDoubleValue
                || b instanceof DefaultComplexValue || b instanceof CDiscrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Minus || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
    }

    private boolean isExpSimplifiableIfMulComplex(Expr b) {
        if (true) return true;
        return b instanceof Domain || b instanceof Complex || b instanceof DefaultDoubleValue
                || b instanceof DefaultComplexValue || b instanceof CDiscrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Minus || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
    }

    private boolean isExpSimplifiableIfMulDomain(Expr b) {
        if (true) return true;
        return b instanceof Domain || b instanceof Complex || b instanceof DefaultDoubleValue
                || b instanceof DefaultComplexValue || b instanceof CDiscrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Minus || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
    }

}
