package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class Arg extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Arg(Expr arg) {
        super("arg", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "arg";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.arg();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return MathsBase.arg(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Arg(argument);
    }

}
