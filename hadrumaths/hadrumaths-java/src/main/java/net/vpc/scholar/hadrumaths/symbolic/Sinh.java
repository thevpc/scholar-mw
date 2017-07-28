package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Sinh extends TrigoFunctionX implements Cloneable {
    public Sinh(Expr arg) {
        super("sinh", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.sinh();
    }

    protected double evalDouble(double c) {
        return Math.sinh(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sinh(argument);
    }

}
