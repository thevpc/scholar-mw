package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComponentDimension;
import net.vpc.scholar.hadrumaths.Expr;

import java.lang.ref.WeakReference;

public class ParametrizedScalarProduct extends GenericFunctionXY {
    private boolean hermitian;
    private transient WeakReference<Expr> meSimplified=new WeakReference<Expr>(null);
    public ParametrizedScalarProduct(Expr xargument, Expr yargument,boolean hermitian) {
        super();
        init(xargument, yargument, null,false);
        this.hermitian=hermitian;
    }
    @Override
    public String getFunctionName() {
        return hermitian?"**":"***";
    }


    public ParametrizedScalarProduct(String functionName, Expr xargument, Expr yargument, FunctionType lowerFunctionType,boolean hermitian) {
        super();
        init(xargument, yargument, lowerFunctionType,false);
        this.hermitian=hermitian;
    }

    @Override
    public Complex evalComplex(Complex x, Complex y) {
        Expr e = meSimplified.get();
        if(e==null){
            e = this.simplify();
            meSimplified=new WeakReference<Expr>(e);
        }
        return e.toComplex();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ParametrizedScalarProduct that = (ParametrizedScalarProduct) o;

        return hermitian == that.hermitian;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (hermitian ? 1 : 0);
        return result;
    }
}
