package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Cos extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Cos(Expr arg) {
        super("cos", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "cos";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.cos();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Maths.cos2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Cos(argument);
    }

}
