package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * Created by vpc on 4/30/14.
 */
public class XX extends AxisFunction {
    private static final long serialVersionUID = 1L;

    public XX(Domain domain) {
        super(domain, Axis.X);
    }


    @Override
    public double evalDoubleSimple(double x, double y, double z) {
        return x;
    }

    @Override
    public double evalDoubleSimple(double x, double y) {
        return x;
    }

    @Override
    public double evalDoubleSimple(double x) {
        return x;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        if (xreplacement != null) {
            return xreplacement;
        }
        return this;
    }

    @Override
    public Expr mul(Domain domain) {
        return new XX(domain.intersect(getDomain()));
    }

    @Override
    public Expr mul(double other) {
        return new Linear(other, 0, 0, getDomain());
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return Mul.of(other, this);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public String toLatex() {
        return "x";
    }

}
