package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

public class IfThenElse extends GenericFunctionXYZ implements Cloneable {
    private static final long serialVersionUID = 1L;
    public IfThenElse(Expr xarg, Expr yarg, Expr zarg) {
        super("if", xarg, yarg, zarg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "If";
    }

    protected Complex evalComplex(Complex x, Complex y, Complex z) {
        if (!x.isZero()) {
            return y;
        }
        return z;
    }

    @Override
    public Complex computeComplex(double x, OutBoolean defined) {
        if (contains(x)) {
            OutBoolean defined1 = new OutBoolean();
            Complex v = getXArgument().toDC().computeComplex(x, defined1);
            if (!v.isZero()) {
                return getYArgument().toDC().computeComplex(x, defined);
            } else {
                return getYArgument().toDC().computeComplex(x, defined);
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, OutBoolean defined) {
        if (contains(x, y)) {
            OutBoolean defined1 = new OutBoolean();
            Complex v = getXArgument().toDC().computeComplex(x, y, defined1);
            if (!v.isZero()) {
                return getYArgument().toDC().computeComplex(x, y, defined);
            } else {
                return getYArgument().toDC().computeComplex(x, y, defined);
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, OutBoolean defined) {
        if (contains(x, y, z)) {
            OutBoolean defined1 = new OutBoolean();
            Complex v = getXArgument().toDC().computeComplex(x, y, z, defined1);
            if (!v.isZero()) {
                return getYArgument().toDC().computeComplex(x, y, z, defined);
            } else {
                return getYArgument().toDC().computeComplex(x, y, z, defined);
            }
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, OutBoolean defined) {
        if (contains(x)) {
            OutBoolean defined1 = new OutBoolean();
            double v = getXArgument().toDD().computeDouble(x, defined1);
            if (v!=0) {
                return getYArgument().toDD().computeDouble(x, defined);
            } else {
                return getYArgument().toDD().computeDouble(x, defined);
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, OutBoolean defined) {
        if (contains(x, y)) {
            OutBoolean defined1 = new OutBoolean();
            double v = getXArgument().toDD().computeDouble(x, y, defined1);
            if (v!=0) {
                return getYArgument().toDD().computeDouble(x, y, defined);
            } else {
                return getYArgument().toDD().computeDouble(x, y, defined);
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, double z, OutBoolean defined) {
        if (contains(x, y, z)) {
            OutBoolean defined1 = new OutBoolean();
            double v = getXArgument().toDD().computeDouble(x, y, z, defined1);
            if (v!=0) {
                return getYArgument().toDD().computeDouble(x, y, z, defined);
            } else {
                return getYArgument().toDD().computeDouble(x, y, z, defined);
            }
        }
        return 0;
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
        return getYArgument().isDD() && getZArgument().isDD();
    }

    public Domain getDomainImpl() {
        return getXArgument().getDomain().union(getYArgument().getDomain()).union(getZArgument().getDomain());
    }
}
