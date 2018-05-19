package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Db extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Db(Expr arg) {
        super("db", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "db";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.db();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.db(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Db(argument);
    }

}
