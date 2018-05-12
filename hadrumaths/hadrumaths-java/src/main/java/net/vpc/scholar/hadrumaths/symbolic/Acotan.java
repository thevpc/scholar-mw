package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Acotan extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Acotan(Expr arg) {
        super("acotan", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "acotan";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        return c.acotan();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        return c==0?(Math.PI/2) : Math.atan(1/c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acotan(argument);
    }

    @Override
    public boolean isDDImpl() {
        return false;
    }

}
