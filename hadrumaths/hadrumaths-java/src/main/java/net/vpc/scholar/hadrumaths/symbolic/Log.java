package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Log extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Log(Expr arg) {
        super("log", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "log";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        return c.log();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        return Maths.log(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Log(argument);
    }

}
