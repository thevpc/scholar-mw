/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class MulSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new MulSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Mul.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
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
        Complex complexeVal = Complex.ONE;
        List<Expr> all = new ArrayList<Expr>();
        Domain fullDomain = Domain.FULL(ee.getDomainDimension());
        boolean updated = false;
        List<Expr> linearized = new ArrayList<Expr>();
        for (Expr expression : ee.getSubExpressions()) {
            RewriteResult rewrite = ruleset.rewrite(expression);
            expression = rewrite.getValue();
            if (rewrite.isRewritten()) {
                updated = true;
            }
            fullDomain = fullDomain.intersect(expression.getDomain());
            if (expression instanceof Mul) {
                updated = true;
                linearized.addAll(expression.getSubExpressions());
            } else {
                linearized.add(expression);
            }
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
            } else if (expression.isDD()) {
                DoubleToDouble v = expression.toDD();
                all.add(v);
                fullDomain = fullDomain.intersect(v.getDomain());
            } else if (expression.isDC()) {
                DoubleToComplex v = expression.toDC();
                all.add(v);
                fullDomain = fullDomain.intersect(v.getDomain());
            } else if (expression.isDM()) {
                DoubleToMatrix v = expression.toDM();
                all.add(v);
                fullDomain = fullDomain.intersect(v.getDomain());
            } else {
                throw new IllegalArgumentException("Unsupported Expression Type " + expression);
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
        all = simplify(all, fullDomain);
        //no element in the multiplication, all are ones, so discarded!
        //we return one in that case
        if (all.size() == 0) {
            return RewriteResult.bestEffort(Complex.ONE);
        }
        if (all.size() == 1) {
            Expr value = all.get(0);
            if (e.equals(value)) {
                return RewriteResult.unmodified(e);
            }
            return RewriteResult.newVal(value);
        }
        Expr m2 = Maths.mul(all.toArray(new Expr[0]));
        if (!updated && m2.equals(e)) {
            return RewriteResult.unmodified(e);
        }
        //TODO should we return bestEffort? test me please
        return RewriteResult.newVal(m2);
    }

    protected List<Expr> simplify(List<Expr> all, Domain fullDomain) {
        while (true) {
            List<Expr> all2 = simplify0(all, fullDomain);
            if (all2.size() == all.size()) {
                break;
            }
            all = all2;
        }
        return all;
    }

    protected List<Expr> simplify0(List<Expr> all, Domain fullDomain) {
        if (all.size() < 2) {
            return new ArrayList<Expr>(all);
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
                        Expr r = simplify(a, b, fullDomain);
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

    protected Expr simplify(Expr a, Expr b, Domain fullDomain) {
        if (a.isComplexExpr()) {
            return b.mul(a.toComplex()).mul(a.getDomain());
        }
        if (b.isComplexExpr()) {
            return a.mul(b.toComplex()).mul(b.getDomain());
        }
        //process cross
//        if (a.isDouble()) {
//            double cst = a.toDouble();
//            //b.
//        }

        if (a.isDoubleExpr()) {
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
        } else if (a.isComplexExpr()) {
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
        if (b.isDoubleExpr() || b.isComplexExpr() || b instanceof AxisFunction) {
            return simplify(b, a, fullDomain);
        }
        return null;
    }

    private boolean isExpSimplifiableIfMulDouble(Expr b) {
        return b instanceof Domain || b instanceof Complex || b instanceof DoubleValue
                || b instanceof ComplexValue || b instanceof Discrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Sub || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
    }

    private boolean isExpSimplifiableIfMulComplex(Expr b) {
        if (true) return true;
        return b instanceof Domain || b instanceof Complex || b instanceof DoubleValue
                || b instanceof ComplexValue || b instanceof Discrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Sub || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
    }

    private boolean isExpSimplifiableIfMulDomain(Expr b) {
        if (true) return true;
        return b instanceof Domain || b instanceof Complex || b instanceof DoubleValue
                || b instanceof ComplexValue || b instanceof Discrete || b instanceof VDiscrete
                || b instanceof Linear || b instanceof Shape2D || b instanceof RWG
                || b instanceof Plus || b instanceof Sub || b instanceof Neg
                || b instanceof CosXCosY || b instanceof CosXPlusY || b instanceof UFunction;
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
