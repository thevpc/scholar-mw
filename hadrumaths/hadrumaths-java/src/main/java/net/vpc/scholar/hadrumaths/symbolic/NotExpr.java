package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by vpc on 4/30/14.
 */
public class NotExpr extends GenericFunctionX implements Cloneable{
    public NotExpr(Expr arg) {
        super("not",arg);
    }


    public Complex evalComplex(Complex c){
        return c.isZero()?Complex.ONE:Complex.ZERO;
    }

    protected double evalDouble(double c){
        return c==0?0:1;
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
        return new NotExpr(argument);
    }

}
