package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class Cotanh extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Cotanh(Expr arg) {
        super("cotanh", arg, FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "cotanh";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.cotanh();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.cotanh(c);
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
        return new Cotanh(argument);
    }
}
