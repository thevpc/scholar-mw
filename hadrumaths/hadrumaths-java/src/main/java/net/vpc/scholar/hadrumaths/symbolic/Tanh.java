package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Tanh extends TrigoFunctionX implements Cloneable{
    public Tanh(Expr arg) {
        super("tanh",arg);
    }


    public Complex evalComplex(Complex c){
        return c.tanh();
    }

    protected double evalDouble(double c){
        return Math.tanh(c);
    }


    @Override
    public Expr newInstance(Expr argument) {
        return new Tanh(argument);
    }

}
