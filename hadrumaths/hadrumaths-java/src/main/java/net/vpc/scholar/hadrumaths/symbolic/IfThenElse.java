package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;

public class IfThenElse extends GenericFunctionXYZ implements Cloneable {
    public IfThenElse(Expr xarg, Expr yarg, Expr zarg) {
        super("if", xarg, yarg, zarg, FunctionType.DOUBLE);
    }

    protected Complex evalComplex(Complex x, Complex y, Complex z) {
        if (!x.isZero()) {
            return y;
        }
        return z;
    }

    protected Complex evalComplex(double x, double y, double z) {
        if (x != 0) {
            return Complex.valueOf(y);
        }
        return Complex.valueOf(z);
    }

    protected double evalDouble(double x, double y, double z) {
        if (x != 0) {
            return y;
        } else {
            return z;
        }
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument, Expr zargument) {
        return new IfThenElse(xargument, yargument, zargument);
    }

    @Override
    public boolean isDDImpl() {
        return super.isDDImpl();
    }

    public Domain getDomainImpl() {
        return getXArgument().getDomain().union(getYArgument().getDomain()).union(getZArgument().getDomain());
    }
}
