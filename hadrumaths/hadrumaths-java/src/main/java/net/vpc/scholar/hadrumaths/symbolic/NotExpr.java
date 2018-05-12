package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class NotExpr extends GenericFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public NotExpr(Expr arg) {
        super("not",arg);
    }

    @Override
    public String getFunctionName() {
        return "not";
    }


    public Complex computeComplexArg(Complex c, OutBoolean defined){
        return c.isZero()?Complex.ONE:Complex.ZERO;
    }

    protected double computeDoubleArg(double c, OutBoolean defined){
        return c==0?1:0;
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
        return new NotExpr(argument);
    }

}
