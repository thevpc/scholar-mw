package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqrt extends GenericFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Sqrt(Expr arg) {
        super("sqrt", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "sqrt";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.sqrt();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return MathsBase.sqrt(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqrt(argument);
    }

}
