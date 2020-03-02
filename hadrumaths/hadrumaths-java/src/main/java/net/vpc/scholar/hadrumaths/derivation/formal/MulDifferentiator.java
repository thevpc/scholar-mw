package net.vpc.scholar.hadrumaths.derivation.formal;

import net.vpc.common.util.CollectionUtils;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiator;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

import java.util.List;

import static net.vpc.scholar.hadrumaths.Maths.add;
import static net.vpc.scholar.hadrumaths.Maths.mul;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 juil. 2007 10:04:00
 */
public class MulDifferentiator implements FunctionDifferentiator {
    public Expr derive(Expr f, Axis varIndex, FunctionDifferentiatorManager d) {
        Mul c = (Mul) f;
        return deriveMul(c.getChildren(), varIndex, d);
    }

    public Expr deriveMul(List<Expr> children, Axis varIndex, FunctionDifferentiatorManager d) {
        if (children.size() == 0) {
            return Complex.ZERO;
        }
        if (children.size() == 1) {
            return d.derive(children.get(0), varIndex);
        }
        if (children.size() == 2) {
            return derive(children.get(0), children.get(1), varIndex, d);
        }
        List<Expr> aL = CollectionUtils.head(children, -1);
        Expr a = mul(aL.toArray(new Expr[0]));
        Expr b = children.get(children.size() - 1);
        return derive(a, b, varIndex, d);
//
//        Expr ad = deriveMul(a, varIndex, d);
//        Expr bd = d.derive(b, varIndex);
//        //a,bd
//        List<Expr> a2 = new ArrayList<Expr>(a);
//        a2.add(bd);
//        return Plus.of(Mul.of(a2.toArray(new Expr[0])), new Mul(ad, b));
    }

    public Expr derive(Expr a, Expr b, Axis varIndex, FunctionDifferentiatorManager d) {
        Expr ad = d.derive(a, varIndex);
        Expr bd = d.derive(b, varIndex);
        return add(mul(a, bd), mul(ad, b));
    }
}
