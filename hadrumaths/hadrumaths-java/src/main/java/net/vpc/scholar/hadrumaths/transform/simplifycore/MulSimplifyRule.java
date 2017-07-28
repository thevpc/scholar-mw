/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
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

        Mul ee = (Mul) e;
        Complex complexeVal = Complex.ONE;
        List<Expr> all = new ArrayList<Expr>();
        Domain fullDomain = Domain.FULL(ee.getDomainDimension());
        boolean updated=false;
        List<Expr> linearized = new ArrayList<Expr>();
        for (Expr expression : ee.getSubExpressions()) {
            RewriteResult rewrite = ruleset.rewrite(expression);
            expression = rewrite.getValue();
            if(rewrite.isRewritten()){
                updated=true;
            }
            if (expression instanceof Mul) {
                updated=true;
                linearized.addAll(expression.getSubExpressions());
            } else {
                linearized.add(expression);
            }
        }
        for (Expr expression : linearized) {
            if(expression.isZero()){
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
        if (all.size() == 0) {
            return RewriteResult.bestEffort(Complex.ZERO);
        }
        if (all.size() == 1) {
            return RewriteResult.newVal(all.get(0));
        }
        Expr m2 = Maths.mul(all.toArray(new Expr[all.size()]));
        if(!updated && m2.equals(e)){
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
        Expr[] arr = all.toArray(new Expr[all.size()]);
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
        //process cross
        if (a instanceof DoubleValue) {
            DoubleValue cst = (DoubleValue) a;
            //b.
        }

        if (a instanceof DoubleValue) {
            DoubleValue cst = (DoubleValue) a;
            if (b instanceof DoubleValue) {
                DoubleValue b2 = (DoubleValue) b;
                return DoubleValue.valueOf(cst.value * b2.value, fullDomain);
            } else if (b instanceof ComplexValue) {
                ComplexValue f = (ComplexValue) b;
                return new ComplexValue(f.getValue().mul(cst.value), fullDomain);
            } else if (b instanceof Complex) {
                Complex f = (Complex) b;
                if (f.isReal()) {
                    return DoubleValue.valueOf(f.getReal() * cst.value, fullDomain);
                } else {
                    return new ComplexValue(f.mul(cst.value), fullDomain);
                }
            } else if (b instanceof Linear) {
                Linear f = (Linear) b;
                return new Linear(cst.value * f.a, cst.value * f.b, cst.value * f.c, fullDomain);
            } else if (b instanceof Shape) {
                Shape f = (Shape) b;
                return new Shape(cst.value * f.value, f.getGeometry());
            } else if (b instanceof RWG) {
                RWG f = (RWG) b;
                return new RWG(cst.value * f.max, f.polygon1, f.polygon2);
            } else if (b instanceof Plus) {
                Plus f = (Plus) b;
                ArrayList<Expr> next = new ArrayList<Expr>();
                DoubleValue newCst = DoubleValue.valueOf(cst.value, fullDomain);
                for (Expr expression : f.getSubExpressions()) {
                    next.add(Maths.mul(expression, newCst));
                }
                return Maths.sum(next.toArray(new Expr[next.size()]));
            } else if (b instanceof Sub) {
                Sub f = (Sub) b;
                DoubleValue newCst = DoubleValue.valueOf(cst.value, fullDomain);
                return new Sub(Maths.mul(f.getFirst(), newCst), Maths.mul(f.getSecond(), newCst));
            } else if (b instanceof Div) {
                Div f = (Div) b;
                DoubleValue newCst = DoubleValue.valueOf(cst.value, fullDomain);
                return new Div(Maths.mul(f.getFirst(), newCst), Maths.mul(f.getSecond(), newCst));
//            } else if (b instanceof Pow) {
//                Pow f = (Pow) b;
//                DoubleXY newCst = new DoubleXY(cst.value, fullDomain);
//                return new Div(new Mul(f.getFirst(), newCst), f.getSecond());
            } else if (b instanceof Neg) {
                Neg f = (Neg) b;
                DoubleValue newCst = DoubleValue.valueOf(cst.value, fullDomain);
                return new Neg(Maths.mul(f.getExpression(), newCst));
                //removed because Imag is expanded to mul, if added mul to image will have stackoverflow exception
//            } else if (b instanceof Imag) {
//                Imag f = (Imag) b;
//                DoubleValue newCst = new DoubleValue(cst.value, fullDomain);
//                return new Imag(Maths.mul(f.getArg(), newCst).toDC());
//            } else if (b instanceof Real) {
//                Real f = (Real) b;
//                DoubleValue newCst = new DoubleValue(cst.value, fullDomain);
//                return (Maths.mul(f.getArg().getReal(), newCst));
            } else if (b instanceof CosXCosY) {
                CosXCosY f = (CosXCosY) b;
                return new CosXCosY(cst.value * f.amp, f.a, f.b, f.c, f.d, fullDomain);
            } else if (b instanceof CosXPlusY) {
                CosXPlusY f = (CosXPlusY) b;
                return new CosXPlusY(cst.value * f.amp, f.a, f.b, f.c, fullDomain);
            } else if (b instanceof UFunction) {
                UFunction f = (UFunction) b;
                return new UFunction(fullDomain, cst.value * f.getAmp(), f.getA(), f.getB(), f.getC(), f.getD(), f.getE());
            } else if (b instanceof DDiscrete) {
                DDiscrete f = (DDiscrete) b;
                Domain theDomain = fullDomain.intersect(f.getDomain());
                if (theDomain.equals(f.getDomain())) {
                    double factor = cst.value;
                    if (factor == 1) {
                        return b;
                    }
                }
                DoubleValue newCst = DoubleValue.valueOf(cst.value, fullDomain);
                return f.mul(newCst);
//            } else if (b instanceof DDxyToDDx) {
//                DDxyToDDx f = (DDxyToDDx) b;
//                return new DDxyToDDx(Maths.mul(a, f.getBase()).toDD(), f.getDefaultY());
            }
        } else if (a instanceof ComplexValue) {
            ComplexValue ac = (ComplexValue) a;
            if (b instanceof ComplexValue) {
                ComplexValue f = (ComplexValue) b;
                return new ComplexValue(f.getValue().mul(ac.getValue()), fullDomain);
            } else if (b instanceof Complex) {
                Complex f = (Complex) b;
                return new ComplexValue(f.mul(ac.getValue()), fullDomain);
            }
        } else if (a instanceof Complex) {
            Complex ac = (Complex) a;
            if (b instanceof Complex) {
                Complex f = (Complex) b;
                return ac.mul(f);
            }
            if (b instanceof ComplexValue) {
                ComplexValue f = (ComplexValue) b;
                return new ComplexValue(f.getValue().mul(ac), fullDomain);
            }
            if (ac.isReal()) {
                return simplify(ac.toDD(), b, fullDomain);
            } else {
                return null;
            }
        } else if (a instanceof XX) {
            XX ac = (XX) a;
            if (b instanceof XX) {
                XX f = (XX) b;
                return Maths.pow(new XX(ac.getDomain().intersect(f.getDomain())), Maths.expr(2));
            } else if (b instanceof Pow && ((Pow) b).getFirst() instanceof XX) {
                XX f = (XX) ((Pow) b).getFirst();
                return Maths.pow(new XX(ac.getDomain().intersect(f.getDomain()).intersect(((Pow) b).getDomain())), Maths.sum(((Pow) b).getSecond(), Maths.expr(1)));
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
                return Maths.pow(new YY(ac.getDomain().intersect(f.getDomain()).intersect(((Pow) b).getDomain())), Maths.sum(((Pow) b).getSecond(), Maths.expr(1)));
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
        if (b instanceof DoubleValue || b instanceof Complex || b instanceof ComplexValue || b instanceof XX || b instanceof YY) {
            return simplify(b, a, fullDomain);
        }
        return null;
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
