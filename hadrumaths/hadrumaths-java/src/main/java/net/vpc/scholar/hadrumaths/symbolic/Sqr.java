package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqr extends GenericFunctionX implements Cloneable{
    public Sqr(Expr arg) {
        super("sqr",arg);
    }


    public Complex evalComplex(Complex c){
        return c.sqr();
    }

    protected double evalDouble(double c){
        return c*c;
    }

    @Override
    public DoubleToDouble getReal() {
        Expr a = getArgument();
        if(a.isDC() && a.toDC().getImag().isZero()){
            return this;
        }
        return super.getReal();
    }
    @Override
    public DoubleToDouble getImag() {
        Expr a = getArgument();
        if(a.isDC() && a.toDC().getImag().isZero()){
            return FunctionFactory.DZEROXY;
        }
        return super.getImag();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqr(argument);
    }

}
