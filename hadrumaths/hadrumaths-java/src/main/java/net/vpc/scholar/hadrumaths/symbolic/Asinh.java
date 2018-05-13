package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Asinh extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Asinh(Expr arg) {
        super("asinh", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "asinh";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.asinh();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Complex.valueOf(c).asinh().toReal();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Asinh(argument);
    }

    @Override
    public boolean isDDImpl() {
        return false;
    }

}
