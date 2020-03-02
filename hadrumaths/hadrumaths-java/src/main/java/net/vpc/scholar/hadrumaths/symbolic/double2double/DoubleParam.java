package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.symbolic.Param;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by vpc on 4/29/14.
 */
public class DoubleParam extends AbstractDoubleToDouble implements /*IDDx,*/Param {
    private static final long serialVersionUID = 1L;
    private final String paramName;

    public DoubleParam(String name) {
        super();
        paramName = name;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public double toDouble() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean hasParams() {
        return true;
    }

    @Override
    public Expr setParam(String name, double value) {
        if (getName().equals(name)) {
            return DoubleExpr.of(value);
        }
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        if (getName().equals(paramExpr.getName())) {
            return DoubleExpr.of(value);
        }
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        if (getName().equals(paramExpr.getName())) {
            return value.toDD();
        }
        return this;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        if (getName().equals(name)) {
            return value.toDD();
        }
        return this;
    }

    @Override
    public Expr setParams(ParamValues params) {
        Expr n = params.getValue(getName());
        if(n!=null){
            return n.toDD();
        }
        return this;
    }
//    @Override
//    public Expr clone() {
//        return (Expr) super.clone();
//    }

    @Override
    public Set<Param> getParams() {
        return Collections.singleton(this);
    }

//    @Override
//    public DoubleToComplex toDC() {
//        return this;
////        throw new IllegalArgumentException("CParam "+getName()+" could not be evaluated");
//    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    //    @Override
//    public IDDx toDDx() {
//        return this;
////        throw new IllegalArgumentException("CParam "+getName()+" could not be evaluated");
//    }
//    @Override
//    public DoubleToMatrix toDM() {
//        return new DC2DM(this);
//    }
//
//    @Override
//    public DoubleToVector toDV() {
//        return new DC2DV(this);
//    }

//    @Override
//    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex computeComplex(double x, BooleanMarker defined) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex computeComplex(double x, double y, BooleanMarker defined) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }

    @Override
    public boolean isEvaluatable() {
        return false;
    }

//    @Override
//    public double computeDouble(double x) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public double evalDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

//    @Override
//    public double computeDouble(double x, BooleanMarker defined) {
//        throw new IllegalArgumentException("CParam " + getParamName() + " could not be evaluated");
//    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public String getName() {
        return paramName;
    }

    @Override
    public double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (paramName != null ? paramName.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleParam paramExpr = (DoubleParam) o;

        return paramName != null ? paramName.equals(paramExpr.paramName) : paramExpr.paramName == null;
    }

    @Override
    public Domain getDomain() {
        return Domain.FULLX;
    }


    @Override
    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Complex toComplex() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    public DoubleParamFromTo1 steps(double from, double to) {
        return new DoubleParamFromTo1(this, from, to, 1, null);
    }

    public DoubleParamFromTo1 in(double from, double to) {
        return new DoubleParamFromTo1(this, from, to, 1, null);
    }

    public DoubleParamFromTo1 steps(double from, double to, double by) {
        return new DoubleParamFromTo1(this, from, to, by, null);
    }

    public DoubleParamFromTo1 times(double from, double to, int times) {
        double step = (to - from) / (times - 1);
        return new DoubleParamFromTo1(this, from, to, step, null);
    }

    public DoubleParamFromTo1 to(double to) {
        return from(0).to(to);
    }

    public DoubleParamFromTo1 from(double from) {
        return new DoubleParamFromTo1(this, from, from, 1, null);
    }
}
