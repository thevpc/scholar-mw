package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Log extends TrigoFunctionX implements Cloneable {
    public Log(Expr arg) {
        super("log", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.log();
    }

    protected double evalDouble(double c) {
        return Maths.log(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Log(argument);
    }

}
