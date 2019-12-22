package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class Sincard extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Sincard(Expr arg) {
        super("sincard", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "sincard";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.sincard();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return MathsBase.sincard(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sincard(argument);
    }

}
