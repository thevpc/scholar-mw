package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Log10 extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Log10(Expr arg) {
        super("log10", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "log10";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.log10();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Maths.log10(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Log10(argument);
    }

}
