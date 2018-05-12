package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Sincard extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Sincard(Expr arg) {
        super("sincard",arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "sincard";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        return c.sincard();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        return Maths.sincard(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sincard(argument);
    }

}
