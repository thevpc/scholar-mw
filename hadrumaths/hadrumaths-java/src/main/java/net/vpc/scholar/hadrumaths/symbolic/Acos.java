package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.BooleanMarker;

/**
 * Created by vpc on 4/30/14.
 */
public class Acos extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Acos(Expr arg) {
        super("acos", arg,FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "acos";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.acos();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.acos(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Acos(argument);
    }

}
