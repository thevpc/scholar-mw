package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Sincard extends TrigoFunctionX implements Cloneable {
    public Sincard(Expr arg) {
        super("sincard",arg);
    }

    @Override
    public String getFunctionName() {
        return "sincard";
    }

    public Complex evalComplex(Complex c) {
        return c.sincard();
    }

    protected double evalDouble(double c) {
        return Maths.sincard(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sincard(argument);
    }

}
