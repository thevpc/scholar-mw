package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vpc on 4/29/14.
 */
public abstract class ParamExpr extends AbstractExprPropertyAware implements /*IDDx,*/TParam, DoubleToDouble, DoubleToComplex, DoubleToMatrix, DoubleToVector {
    private static final long serialVersionUID = 1L;
    private String paramName;

    protected ParamExpr(String name) {
        paramName = name;
    }

    public String getParamName() {
        return paramName;
    }

    @Override
    public boolean hasParamsImpl() {
        return true;
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        return false;
    }

    @Override
    public boolean isZeroImpl() {
        return false;
    }

    @Override
    public boolean isNaNImpl() {
        return false;
    }

    @Override
    public boolean isInfiniteImpl() {
        return false;
    }

    @Override
    public Expr clone() {
        return (Expr) super.clone();
    }

    @Override
    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
//        throw new IllegalArgumentException("Param "+getName()+" could not be evaluated");
    }

    @Override
    public DoubleToDouble toDD() {
        return this;
//        throw new IllegalArgumentException("Param "+getName()+" could not be evaluated");
    }

    //    @Override
//    public IDDx toDDx() {
//        return this;
////        throw new IllegalArgumentException("Param "+getName()+" could not be evaluated");
//    }
    @Override
    public DoubleToMatrix toDM() {
        return this;
        //throw new IllegalArgumentException("Param "+getName()+" could not be evaluated");
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

//    @Override
//    public double computeDouble(double x) {
//        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
//    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

//    @Override
//    public Complex computeComplex(double x, double y, double z) {
//        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
//    }

//    @Override
//    public double computeDouble(double x, double y, double z) {
//        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
//    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }


    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double x, double[] y) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

//    @Override
//    public String toString() {
//        return String.valueOf(paramName);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ParamExpr paramExpr = (ParamExpr) o;

        return paramName != null ? paramName.equals(paramExpr.paramName) : paramExpr.paramName == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (paramName != null ? paramName.hashCode() : 0);
        return result;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }

    @Override
    public int getDomainDimension() {
        return 1;
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0) {
        return computeComplex(x, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        return computeComplex(x, y, z, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Expr getX() {
        return getComponent(Axis.X);
    }

    @Override
    public Expr getY() {
        return getComponent(Axis.Y);
    }

    @Override
    public Expr getZ() {
        return getComponent(Axis.Z);
    }

    @Override
    public boolean isDoubleTyped() {
        return true;
    }

    @Override
    public Set<ParamExpr> getParams() {
        HashSet<ParamExpr> paramExprs = new HashSet<>(1);
        paramExprs.add(this);
        return paramExprs;
    }
}
