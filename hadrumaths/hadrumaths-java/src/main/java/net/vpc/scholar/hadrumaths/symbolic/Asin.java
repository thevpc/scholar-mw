package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Asin extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Asin(Expr arg) {
        super("asin", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "asin";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.asin();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Math.asin(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Asin(argument);
    }

}
