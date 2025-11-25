package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * Created by vpc on 4/30/14.
 */
public class XX extends AxisFunction {
    private static final long serialVersionUID = 1L;

    public XX(Domain domain) {
        super(domain, Axis.X);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("XX");
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
