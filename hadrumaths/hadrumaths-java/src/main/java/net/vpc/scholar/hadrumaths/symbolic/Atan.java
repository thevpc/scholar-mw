package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

/**
 * Created by vpc on 4/30/14.
 */
public class Atan extends TrigoFunctionX implements Cloneable {
    public Atan(Expr arg) {
        super("atan", arg);
    }

    @Override
    public String getFunctionName() {
        return "atan";
    }

    public Complex evalComplex(Complex c) {
        return c.atan();
    }

    protected double evalDouble(double c) {
        return Math.atan(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Atan(argument);
    }

}
