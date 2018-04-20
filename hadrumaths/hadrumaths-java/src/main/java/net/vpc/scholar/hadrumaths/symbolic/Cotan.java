package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/30/14.
 */
public class Cotan extends TrigoFunctionX implements Cloneable{
    public Cotan(Expr arg) {
        super("cotan",arg);
    }

    @Override
    public String getFunctionName() {
        return "cotan";
    }

    public Complex evalComplex(Complex c){
        return c.cotan();
    }

    protected double evalDouble(double c){
        return 1.0/Math.tan(c);
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
        return new Cotan(argument);
    }

}
