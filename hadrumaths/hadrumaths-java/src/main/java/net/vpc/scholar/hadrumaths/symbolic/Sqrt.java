package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqrt extends GenericFunctionX implements Cloneable{
    public Sqrt(Expr arg) {
        super("sqrt",arg,FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "sqrt";
    }

    public Complex evalComplex(Complex c){
        return c.sqrt();
    }

    protected double evalDouble(double c){
        return Math.sqrt(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqrt(argument);
    }

}
