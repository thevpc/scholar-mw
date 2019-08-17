package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Cosh extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Cosh(Expr arg) {
        super("cosh", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "cosh";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.cosh();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Math.cosh(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Cosh(argument);
    }

}
