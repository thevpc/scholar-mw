package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

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


    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.tan();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Maths.tan(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Tan(argument);
    }

}
