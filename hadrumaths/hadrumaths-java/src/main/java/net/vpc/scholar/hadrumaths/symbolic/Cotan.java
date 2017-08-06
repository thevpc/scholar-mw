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
        return new Cotan(argument);
    }

}
