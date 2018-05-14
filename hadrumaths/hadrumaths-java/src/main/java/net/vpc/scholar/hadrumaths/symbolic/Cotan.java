package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class Cotan extends TrigoFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public Cotan(Expr arg) {
        super("cotan",arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "cotan";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined){
        defined.set();
        return c.cotan();
    }

    public double computeDoubleArg(double c, OutBoolean defined){
        defined.set();
        return 1.0/ Maths.tan(c);
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
