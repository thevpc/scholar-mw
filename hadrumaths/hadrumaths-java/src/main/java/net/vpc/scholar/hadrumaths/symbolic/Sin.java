package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Sin extends TrigoFunctionX implements Cloneable {
    public Sin(Expr arg) {
        super("sin",arg);
    }

    @Override
    public String getFunctionName() {
        return "sin";
    }


    public Complex evalComplex(Complex c) {
        return c.sin();
    }

    protected double evalDouble(double c) {
        return Maths.sin2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sin(argument);
    }

}
