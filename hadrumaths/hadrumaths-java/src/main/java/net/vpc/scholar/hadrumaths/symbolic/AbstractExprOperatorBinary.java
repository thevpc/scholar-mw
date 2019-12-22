package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

public abstract class AbstractExprOperatorBinary extends AbstractExprOperator{
    protected abstract Expressions.BinaryExprHelper getBinaryExprHelper();
    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, getBinaryExprHelper(), x, d0, range);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getBinaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getBinaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getBinaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getBinaryExprHelper(), x, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getBinaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getBinaryExprHelper(), x, d0, ranges);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getBinaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getBinaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getBinaryExprHelper(), x, d0, ranges);
    }

    @Override
    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getBinaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getBinaryExprHelper(), x, y, z, d0, ranges);
    }
    @Override
    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {
        return Expressions.computeVector(this,x,y,z,defined);
    }

    @Override
    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        return Expressions.computeVector(this,x,y,defined);
    }

    @Override
    public ComplexVector computeVector(double x, BooleanMarker defined) {
        return Expressions.computeVector(this,x,defined);
    }

    @Override
    public int getComponentSize() {
        int s=1;
        for (Expr subExpression : getSubExpressions()) {
            int s2=subExpression.toDV().getComponentSize();
            if(s2>s){
                s=s2;
            }
        }
        return s;
    }
}
