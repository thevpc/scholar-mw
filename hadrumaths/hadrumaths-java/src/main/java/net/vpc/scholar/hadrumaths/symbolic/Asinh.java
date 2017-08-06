package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Asinh extends TrigoFunctionX implements Cloneable {
    public Asinh(Expr arg) {
        super("asinh", arg);
    }

    @Override
    public String getFunctionName() {
        return "asinh";
    }


    public Complex evalComplex(Complex c) {
        return c.asinh();
    }

    protected double evalDouble(double c) {
        return Complex.valueOf(c).asinh().toReal();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Asinh(argument);
    }

}
