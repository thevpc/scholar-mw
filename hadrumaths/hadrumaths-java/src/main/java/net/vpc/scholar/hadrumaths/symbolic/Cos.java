package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Cos extends TrigoFunctionX implements Cloneable {
    public Cos(Expr arg) {
        super("cos", arg);
    }

    @Override
    public String getFunctionName() {
        return "cos";
    }


    public Complex evalComplex(Complex c) {
        return c.cos();
    }

    protected double evalDouble(double c) {
        return Maths.cos2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Cos(argument);
    }

}
