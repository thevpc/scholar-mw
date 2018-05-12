package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Tanh extends TrigoFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public Tanh(Expr arg) {
        super("tanh",arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "tanh";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined){
        return c.tanh();
    }

    public double computeDoubleArg(double c, OutBoolean defined){
        return Math.tanh(c);
    }


    @Override
    public Expr newInstance(Expr argument) {
        return new Tanh(argument);
    }

}
