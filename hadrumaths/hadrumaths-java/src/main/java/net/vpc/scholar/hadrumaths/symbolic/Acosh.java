package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Acosh extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Acosh(Expr arg) {
        super("acosh", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "acosh";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.acosh();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Complex.valueOf(c).acosh().toReal();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acosh(argument);
    }

    @Override
    public boolean isDDImpl() {
        return false;
    }
}
