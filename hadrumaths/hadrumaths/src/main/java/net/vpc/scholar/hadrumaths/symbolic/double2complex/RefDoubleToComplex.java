/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.double2complex;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
@IgnoreRandomGeneration
public abstract class RefDoubleToComplex implements DoubleToComplex {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(RefDoubleToComplex.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                RefDoubleToComplex e = (RefDoubleToComplex) expression;
                return (ExpressionTransformFactory.transform(e.object, transform));
            }
        });
    }

    private Expr object;

    protected RefDoubleToComplex() {
    }


//    @Override
//    public boolean isDoubleTyped() {
//        return object.isDoubleTyped();
//    }

    public static Expr unwrap(Expr e) {
        return !(e instanceof RefDoubleToComplex) ? e : (((RefDoubleToComplex) e).object);
    }

    public boolean isInvariant(Axis axis) {
        return object.isInvariant(axis);
    }

    @Override
    public Complex toComplex() {
        return object.toComplex();
    }

    @Override
    public double toDouble() {
        return object.toDouble();
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    @Override
    public DoubleToDouble toDD() {
        return object.toDD();
    }

    @Override
    public DoubleToVector toDV() {
        return object.toDV();
    }

    @Override
    public DoubleToMatrix toDM() {
        return object.toDM();
    }

    public boolean isZero() {
        return object.isZero();
    }

    public boolean isNaN() {
        return object.isNaN();
    }

//    @Override
//    public IDDx toDDx() {
//        return object.toDDx();
//    }

    @Override
    public boolean hasParams() {
        return object.hasParams();
    }

    @Override
    public Expr setParam(String name, double value) {
        return (object.setParam(name, value));
    }
    @Override
    public Expr setParams(ParamValues params) {
        return (object.setParams(params));
    }
    @Override
    public Expr setParam(String name, Expr value) {
        //TODO
        return (object.setParam(name, value));
    }

    public boolean isInfinite() {
        return object.isInfinite();
    }

    public List<Expr> getChildren() {
        return object.getChildren();
    }


//    @Override
//    public Domain getDomain() {
//        return object.toDDx().getDomain();
//    }

    @Override
    public Object getProperty(String name) {
        return getReference().getProperty(name);
    }

    @Override
    public Map<String, Object> getProperties() {
        return getReference().getProperties();
    }

    public Expr getReference() {
        return object;
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return /*wrap*/(getReference().setProperties(map));
    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        return getReference().setProperties(map, merge);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        return getReference().setProperty(name, value);
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        return object.toMatrix();
//    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return (object.compose(xreplacement, yreplacement, zreplacement));
    }

    public String getTitle() {
        return object.getTitle();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return object.getComponentDimension();
    }

//    @Override
//    public String toString() {
//        return String.valueOf(object);
//    }

    @Override
    public Domain getDomain() {
        return object.getDomain();
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefDoubleToComplex ref = (RefDoubleToComplex) o;

        return object != null ? object.equals(ref.object) : ref.object == null;
    }

    protected void init(Expr object) {
        this.object = object;
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        return object.toDC().evalComplex(x);
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        return object.toDC().evalComplex(x, y, defined);
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        return object.toDC().evalComplex(x, y, z, defined);
    }

    public DoubleToDouble getRealDD() {
        return (toDC().getRealDD());
    }

    public DoubleToDouble getImagDD() {
        return (toDC().getImagDD());
    }

    @Override
    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, d0, ranges);
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, z, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex evalComplex(double x) {
        return object.toDC().evalComplex(x);
    }

    //    public Ref forObject(Expression obj){
//        return new Ref(obj);
//    }

    @Override
    public Complex evalComplex(double x, double y) {
        return object.toDC().evalComplex(x, y);
    }

    @Override
    public Complex evalComplex(double x, double y, double z) {
        return object.toDC().evalComplex(x, y, z);
    }


}
