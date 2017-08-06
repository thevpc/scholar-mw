package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Asin extends TrigoFunctionX implements Cloneable {
    public Asin(Expr arg) {
        super("asin", arg);
    }

    @Override
    public String getFunctionName() {
        return "asin";
    }

    public Complex evalComplex(Complex c) {
        return c.asin();
    }

    protected double evalDouble(double c) {
        return Math.asin(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Asin(argument);
    }

}
