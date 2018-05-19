package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class Exp extends GenericFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;

    public Exp(Expr arg) {
        super("exp", arg);
    }

    @Override
    public String getFunctionName() {
        return "exp";
    }

    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.exp();
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.exp(c);
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
