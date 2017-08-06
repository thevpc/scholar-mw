package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Acotan extends TrigoFunctionX implements Cloneable {
    public Acotan(Expr arg) {
        super("acotan", arg);
    }

    @Override
    public String getFunctionName() {
        return "acotan";
    }

    public Complex evalComplex(Complex c) {
        return c.acotan();
    }

    protected double evalDouble(double c) {
        return c==0?(Math.PI/2) : Math.atan(1/c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acotan(argument);
    }

}
