package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Tan extends TrigoFunctionX implements Cloneable {
    public Tan(Expr arg) {
        super("tan", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.tan();
    }

    protected double evalDouble(double c) {
        return Math.tan(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Tan(argument);
    }

}
