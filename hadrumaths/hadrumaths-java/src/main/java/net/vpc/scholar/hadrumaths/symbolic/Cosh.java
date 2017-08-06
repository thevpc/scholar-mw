package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Cosh extends TrigoFunctionX implements Cloneable{
    public Cosh(Expr arg) {
        super("cosh",arg);
    }

    @Override
    public String getFunctionName() {
        return "cosh";
    }


    public Complex evalComplex(Complex c){
        return c.cosh();
    }

    protected double evalDouble(double c){
        return Math.cosh(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Cosh(argument);
    }

}
