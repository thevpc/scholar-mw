package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Cotanh extends TrigoFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public Cotanh(Expr arg) {
        super("cotanh",arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "cotanh";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined){
        defined.set();
        return c.cotanh();
    }

    public double computeDoubleArg(double c, OutBoolean defined){
        defined.set();
        return 1.0/Math.tanh(c);
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
        return new Cotanh(argument);
    }
}
