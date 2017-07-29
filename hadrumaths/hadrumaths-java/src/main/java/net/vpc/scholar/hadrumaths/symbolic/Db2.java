package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Db2 extends TrigoFunctionX implements Cloneable {
    public Db2(Expr arg) {
        super("db2", arg);
    }


    public Complex evalComplex(Complex c) {
        return c.db2();
    }

    protected double evalDouble(double c) {
        return Maths.db2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Db2(argument);
    }

}
