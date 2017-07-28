package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Acos extends TrigoFunctionX implements Cloneable {
    public Acos(Expr arg) {
        super("acos", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.acos();
    }

    protected double evalDouble(double c) {
        return Math.acos(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acos(argument);
    }

}
