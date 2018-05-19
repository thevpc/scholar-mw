package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqr extends GenericFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Sqr(Expr arg) {
        super("sqr", arg);
    }

    @Override
    public String getFunctionName() {
        return "sqr";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.sqr();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return c * c;
    }

    @Override
    public DoubleToDouble getRealDD() {
        Expr a = getArgument();
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return this;
        }
        return super.getRealDD();
    }

    @Override
    public DoubleToDouble getImagDD() {
        Expr a = getArgument();
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return FunctionFactory.DZEROXY;
        }
        return super.getImagDD();
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqr(argument);
    }

}
