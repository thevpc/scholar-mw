package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Log10 extends TrigoFunctionX implements Cloneable {
    public Log10(Expr arg) {
        super("log10", arg);
    }

    @Override
    public String getFunctionName() {
        return "log10";
    }

    public Complex evalComplex(Complex c) {
        return c.log10();
    }

    protected double evalDouble(double c) {
        return Maths.log10(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Log10(argument);
    }

}
