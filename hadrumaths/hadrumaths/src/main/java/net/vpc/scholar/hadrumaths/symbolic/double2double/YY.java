package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * Created by vpc on 4/30/14.
 */
public class YY extends AxisFunction {
    private static final long serialVersionUID = 1L;

    public YY(Domain domain) {
        super(domain.dimension() < 2 ? domain.expandDimension(2) : domain, Axis.Y);
    }


    @Override
    public double evalDoubleSimple(double x) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public double evalDoubleSimple(double x, double y) {
        return y;
    }

    @Override
    public double evalDoubleSimple(double x, double y, double z) {
        return y;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        if (yreplacement != null) {
            return yreplacement;
        }
        return this;
    }

    @Override
    public Expr mul(Domain domain) {
        return new YY(domain.intersect(getDomain()));
    }

    @Override
    public Expr mul(double other) {
        return new Linear(0, other, 0, getDomain());
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
        return "y";
    }

}
