package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DM;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DV;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vpc on 4/29/14.
 */
public abstract class ParamExpr extends AbstractExprPropertyAware implements /*IDDx,*/TParam, DoubleToDouble, DoubleToComplex {
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
        return true;
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
        return new DC2DM(this);
    }

    @Override
    public DoubleToVector toDV() {
        return new DC2DV(this);
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
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Param " + getParamName() + " could not be evaluated");
    }


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
    public Expr compose(Axis axis, Expr xreplacement) {
        return this;
    }

    @Override
    public int getDomainDimension() {
        return 1;
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
