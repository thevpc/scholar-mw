/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.double2vector;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
@IgnoreRandomGeneration
public abstract class RefDoubleToVector extends AbstractDoubleToVector {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(RefDoubleToVector.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                RefDoubleToVector e = (RefDoubleToVector) expression;
                return (ExpressionTransformFactory.transform(e.object, transform));
            }
        });
    }

    private Expr object;

    protected RefDoubleToVector() {
    }

    public static Expr unwrap(Expr e) {
        return !(e instanceof RefDoubleToVector) ? e : (((RefDoubleToVector) e).object);
    }

    @Override
    public ComplexVector[][][] evalVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDV().evalVector(x, y, z, d0, ranges);
    }

    @Override
    public ComplexVector[] evalVector(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDV().evalVector(x, d0, ranges);
    }

    public boolean isNaN() {
        return object.isNaN();
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return object.isDoubleTyped();
//    }

    public boolean isInfinite() {
        return object.isInfinite();
    }

    public boolean isZero() {
        return object.isZero();
    }

    @Override
    public ComplexVector[][] evalVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDV().evalVector(x, y, d0, ranges);
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
        return object.toDC();
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
        return object.setParams(params);
    }
    @Override
    public Expr setParam(String name, Expr value) {
        //TODO
        return (object.setParam(name, value));
    }

    public List<Expr> getChildren() {
        return Arrays.asList(object);
    }

    @Override
    public Object getProperty(String name) {
        return getObject().getProperty(name);
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        return object.toMatrix();
//    }

    @Override
    public Map<String, Object> getProperties() {
        return getObject().getProperties();
    }

    public Expr getObject() {
        return object;
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return /*wrap*/(getObject().setProperties(map));
    }

//    @Override
//    public String toString() {
//        return String.valueOf(object);
//    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        return getObject().setProperties(map, merge);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        return getObject().setProperty(name, value);
    }

    public String getTitle() {
        return object.getTitle();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return object.getComponentDimension();
    }

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

        RefDoubleToVector ref = (RefDoubleToVector) o;

        return object != null ? object.equals(ref.object) : ref.object == null;
    }

    protected void init(Expr object) {
        this.object = object;
    }

    @Override
    public Expr getComponent(Axis a) {
        return object.toDV().getComponent(a);
    }

    @Override
    public int getComponentSize() {
        return object.getComponentDimension().rows;
    }


}
