package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * Created by vpc on 4/30/14.
 */
public class ZZ extends AxisFunction {
    private static final long serialVersionUID = 1L;

    public ZZ(Domain domain) {
        super(domain.toDomain(3), Axis.Z);
    }

    @Override
    public double evalDoubleSimple(double x, double y, double z) {
        return z;
    }

    @Override
    public double evalDoubleSimple(double x, double y) {
        throw new MissingAxisException(Axis.Z);
    }

    @Override
    public double evalDoubleSimple(double x) {
        throw new MissingAxisException(Axis.Z);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return axis != Axis.Z;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        if (zreplacement != null) {
            return zreplacement;
        }
        return this;
    }

    @Override
    public Expr mul(Domain domain) {
        return new ZZ(domain.intersect(getDomain()));
    }

    @Override
    public Expr mul(double other) {
        return Mul.of(Maths.expr(other, Domain.FULLX), this);
    }

    @Override
    public Expr mul(Complex other) {
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
        return "z";
    }

}
