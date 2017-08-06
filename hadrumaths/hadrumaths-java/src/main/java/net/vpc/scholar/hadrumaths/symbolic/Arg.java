package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Arg extends TrigoFunctionX implements Cloneable {
    public Arg(Expr arg) {
        super("arg", arg);
    }

    @Override
    public String getFunctionName() {
        return "arg";
    }


    public Complex evalComplex(Complex c) {
        return c.arg();
    }

    protected double evalDouble(double c) {
        return Maths.arg(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Arg(argument);
    }

}
