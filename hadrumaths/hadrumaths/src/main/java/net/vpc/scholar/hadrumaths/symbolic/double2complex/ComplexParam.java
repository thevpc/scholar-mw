package net.vpc.scholar.hadrumaths.symbolic.double2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.symbolic.Param;

import java.util.Collections;
import java.util.Set;

/**
 * Created by vpc on 4/29/14.
 */
public class ComplexParam extends AbstractDoubleToComplex implements /*IDDx,*/Param {
    private static final long serialVersionUID = 1L;
    private final String paramName;

    public ComplexParam(String name) {
        super();
        paramName = name;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return true;
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
            return Complex.of(value);
        }
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        if (getName().equals(paramExpr.getName())) {
            return Complex.of(value);
        }
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        if (getName().equals(paramExpr.getName())) {
            return value.toDC();
        }
        return this;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        if (getName().equals(name)) {
            return value.toDC();
        }
        return this;
    }

    @Override
    public Expr setParams(ParamValues params) {
        Expr n = params.getValue(getName());
        if(n!=null){
            return n.toDC();
        }
        return this;
    }

    @Override
    public Set<Param> getParams() {
        return Collections.singleton(this);
    }

    @Override
    public boolean isInfinite() {
        return false;
    }


    @Override
    public boolean isEvaluatable() {
        return false;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public Complex evalComplex(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public String getName() {
        return paramName;
    }

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
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

        ComplexParam paramExpr = (ComplexParam) o;

        return paramName != null ? paramName.equals(paramExpr.paramName) : paramExpr.paramName == null;
    }

    @Override
    public Domain getDomain() {
        return Domain.FULLX;
    }


    @Override
    public double toDouble() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    @Override
    public Complex toComplex() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public String toLatex() {
        String n = getName();
        if(n.length()==1){
            return n;
        }
        StringBuilder sb=new StringBuilder();
        for (String s : n.split("_")) {
            if(sb.length()>0){
                //retain sub
                sb.append("_");
            }
            if(s.length()<2){
                sb.append(s);
            }else{
                sb.append("\\text{");
                sb.append(s);
                sb.append("}");
            }
        }
        return sb.toString();
    }
}
