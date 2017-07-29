package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Db extends TrigoFunctionX implements Cloneable {
    public Db(Expr arg) {
        super("db", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.db();
    }

    protected double evalDouble(double c) {
        return Maths.db(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Db(argument);
    }

}
