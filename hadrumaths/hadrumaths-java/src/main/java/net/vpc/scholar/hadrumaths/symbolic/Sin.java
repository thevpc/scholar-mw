package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class Sin extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Sin(Expr arg) {
        super("sin", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "sin";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.sin();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return MathsBase.sin2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sin(argument);
    }

}
