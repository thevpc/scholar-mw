package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class Atan extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Atan(Expr arg) {
        super("atan", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "atan";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.atan();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.atan(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Atan(argument);
    }

//    @Override
//    public boolean isDDImpl() {
//        return false;
//    }

}
