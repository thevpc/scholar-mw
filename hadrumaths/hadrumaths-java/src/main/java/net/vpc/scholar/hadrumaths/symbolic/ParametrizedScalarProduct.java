package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

public class ParametrizedScalarProduct extends GenericFunctionXY {
    private boolean hermitian;
    public ParametrizedScalarProduct(Expr xargument, Expr yargument,boolean hermitian) {
        super(hermitian?"**":"***",xargument, yargument);
        this.hermitian=hermitian;
    }
    @Override
    public String getFunctionName() {
        return hermitian?"**":"***";
    }


    public ParametrizedScalarProduct(String functionName, Expr xargument, Expr yargument, FunctionType lowerFunctionType,boolean hermitian) {
        super(functionName,xargument, yargument, lowerFunctionType);
        this.hermitian=hermitian;
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
        return new ParametrizedScalarProduct(xargument,yargument,hermitian);
    }

    @Override
    public String toString() {
        return "("+getXArgument()+getFunctionName()+getYArgument()+")";
    }

    public boolean isHermitian() {
        return hermitian;
    }
}
