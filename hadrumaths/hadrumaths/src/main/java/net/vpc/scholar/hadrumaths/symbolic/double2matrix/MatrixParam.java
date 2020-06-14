package net.vpc.scholar.hadrumaths.symbolic.double2matrix;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Param;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DMComponent;

import java.util.Collections;
import java.util.Set;

/**
 * Created by vpc on 4/29/14.
 */
public class MatrixParam extends AbstractDoubleToMatrix implements /*IDDx,*/Param {
    private static final long serialVersionUID = 1L;
    private final String paramName;

    public MatrixParam(String name) {
        super();
        paramName = name;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public Complex toComplex() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    @Override
    public double toDouble() {
        throw new IllegalArgumentException("Cannot process param " + getName() + " as Complex");
    }

    @Override
    public boolean hasParams() {
        return true;
    }

    @Override
    public Expr setParams(ParamValues params) {
        Expr n = params.getValue(getName());
        if (n != null) {
            return n.toDM();
        }
        return this;
    }

    @Override
    public Expr setParam(String name, double value) {
        if (getName().equals(name)) {
            return Maths.expr(value).toDM();
        }
        return this;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        if (getName().equals(name)) {
            return value.toDM();
        }
        return this;
    }

    @Override
    public Set<Param> getParams() {
        return Collections.singleton(this);
    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        if (getName().equals(paramExpr.getName())) {
            return Maths.expr(value).toDM();
        }
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        if (getName().equals(paramExpr.getName())) {
            return value.toDM();
        }
        return this;
    }

    @Override
    public boolean isEvaluatable() {
        return false;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Domain getDomain() {
        return Domain.FULLX;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public String getName() {
        return paramName;
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
    public boolean isInfinite() {
        return false;
    }

    @Override
    public ComplexMatrix[][][] evalMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public ComplexMatrix[][] evalMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public ComplexMatrix[] evalMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("CParam " + getName() + " could not be evaluated");
    }

    @Override
    public Expr getComponent(int row, int col) {
        return DMComponent.of(this, row, col);
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

        MatrixParam paramExpr = (MatrixParam) o;

        return paramName != null ? paramName.equals(paramExpr.paramName) : paramExpr.paramName == null;
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
