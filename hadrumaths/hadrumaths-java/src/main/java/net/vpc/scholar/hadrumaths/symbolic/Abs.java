package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Abs extends GenericFunctionX implements Cloneable{
    public Abs(Expr arg) {
        super("abs",arg);
    }

    @Override
    public String getFunctionName() {
        return "abs";
    }


    public Complex evalComplex(Complex c){
        return c.abs();
    }

    protected double evalDouble(double c){
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
