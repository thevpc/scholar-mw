package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Sinh extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Sinh(Expr arg) {
        super("sinh", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "sinh";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.sinh();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Maths.sinh(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sinh(argument);
    }

}
