package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

public class ParametrizedScalarProduct extends GenericFunctionXY {
    public ParametrizedScalarProduct(Expr xargument, Expr yargument) {
        super("**",xargument, yargument);
    }

    public ParametrizedScalarProduct(String functionName, Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        super(functionName,xargument, yargument, lowerFunctionType);
    }

    @Override
    public Complex evalComplex(Complex x, Complex y) {
        throw new IllegalArgumentException("Could not evaluate");
    }

    @Override
    public Complex evalComplex(double x, double y) {
        throw new IllegalArgumentException("Could not evaluate");
    }

    @Override
    public double evalDouble(double x, double y) {
        throw new IllegalArgumentException("Could not evaluate");
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new ParametrizedScalarProduct(xargument,yargument);
    }

    @Override
    public String toString() {
        return "("+getXArgument()+getFunctionName()+getYArgument()+")";
    }
}
