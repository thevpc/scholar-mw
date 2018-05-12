package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class TrigoFunctionX extends GenericFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public TrigoFunctionX(String name, Expr arg,FunctionType lowerFunctionType) {
        super(name,arg,lowerFunctionType);
    }


//    public Complex evalComplex(Complex c){
//        return c.atan();
//    }
//
//    protected double computeDoubleArg(double c){
//        return Math.atan(c);
//    }

    @Override
    public DoubleToDouble getRealDD() {
        Expr a = getArgument();
        if(a.isDC() && a.toDC().getImagDD().isZero()){
            return this;
        }
        return super.getRealDD();
    }
    @Override
    public DoubleToDouble getImagDD() {
        Expr a = getArgument();
        if(a.isDC() && a.toDC().getImagDD().isZero()){
            return FunctionFactory.DZEROXY;
        }
        return super.getRealDD();
    }

    @Override
    public boolean isDDImpl() {
        if(super.isDDImpl()){
            return true;
        }
        return getArgument().isDD();
    }

}
