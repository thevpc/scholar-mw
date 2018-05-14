package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class Abs extends GenericFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public Abs(Expr arg) {
        super("abs",arg);
    }

    @Override
    public String getFunctionName() {
        return "abs";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined){
        defined.set();
        return c.abs();
    }

    public double computeDoubleArg(double c, OutBoolean defined){
        defined.set();
        return Maths.abs(c);
    }

    @Override
    public DoubleToDouble getRealDD() {
        return this;
    }
    @Override
    public DoubleToDouble getImagDD() {
        return FunctionFactory.DZEROXY;
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Abs(argument);
    }

}
