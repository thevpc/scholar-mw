package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by vpc on 4/30/14.
 */
public class Abs extends GenericFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Abs(Expr arg) {
        super("abs", arg);
    }

    @Override
    public String getFunctionName() {
        return "abs";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.abs();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Math.abs(c);
    }

    @Override
    public DoubleToDouble getRealDD() {
        return this;
    }

    @Override
    public DoubleToDouble getImagDD() {
        return FunctionFactory.DZEROXY;
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Abs(argument);
    }

}
