package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Tan extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Tan(Expr arg) {
        super("tan", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "tan";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.tan();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Math.tan(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Tan(argument);
    }

}
