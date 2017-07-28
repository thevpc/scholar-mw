package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class TrigoFunctionX extends GenericFunctionX implements Cloneable{
    public TrigoFunctionX(String name, Expr arg) {
        super(name,arg);
    }


    public Complex evalComplex(Complex c){
        return c.atan();
    }

    protected double evalDouble(double c){
        return Math.atan(c);
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
        return super.getReal();
    }

    @Override
    public boolean isDDImpl() {
        if(super.isDDImpl()){
            return true;
        }
        return getArgument().isDD();
    }

}
