package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Exp extends GenericFunctionX implements Cloneable{
    public Exp(Expr arg) {
        super("exp",arg);
    }

    @Override
    public String getFunctionName() {
        return "exp";
    }

    public Complex evalComplex(Complex c){
        return c.exp();
    }

    protected double evalDouble(double c){
        return Math.exp(c);
    }

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
        return super.getImagDD();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Exp(argument);
    }

    @Override
    public DoubleToDouble toDD() {
        return super.toDD();
    }

    @Override
    public boolean isDDImpl() {
        return super.isDDImpl();
    }
}
