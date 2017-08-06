package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Acosh extends TrigoFunctionX implements Cloneable {
    public Acosh(Expr arg) {
        super("acosh", arg);
    }

    @Override
    public String getFunctionName() {
        return "acosh";
    }

    public Complex evalComplex(Complex c) {
        return c.acosh();
    }

    protected double evalDouble(double c) {
        return Complex.valueOf(c).acosh().toReal();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acosh(argument);
    }

}
