/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.double2matrix;

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
public abstract class RefDoubleToMatrix extends AbstractDoubleToMatrix {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(RefDoubleToMatrix.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                RefDoubleToMatrix e = (RefDoubleToMatrix) expression;
                return (ExpressionTransformFactory.transform(e.object, transform));
            }
        });
    }

    private Expr object;

    protected RefDoubleToMatrix() {
    }


//    @Override
//    public boolean isDoubleTyped() {
//        return object.isDoubleTyped();
//    }

    public static Expr unwrap(Expr e) {
        return !(e instanceof RefDoubleToMatrix) ? e : (((RefDoubleToMatrix) e).object);
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
//    @Override
//    public IDDx toDDx() {
//        return object.toDDx();
//    }

    @Override
    public Expr setParam(String name, Expr value) {
        return (object.setParam(name, value));
    }

    public List<Expr> getChildren() {
        return object.getChildren();
    }

    @Override
    public Object getProperty(String name) {
        return getObject().getProperty(name);
    }

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

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        return getObject().setProperties(map, merge);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        return getObject().setProperty(name, value);
    }

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

    @Override
    public Domain getDomain() {
        return object.getDomain();
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[] evalMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, d0, ranges);
    }

//    @Override
//    public String toString() {
//        return String.valueOf(object);
//    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y) {
        return object.toDM().evalMatrix(x, y);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z) {
        return object.toDM().evalMatrix(x, y, z);
    }

    @Override
    public ComplexMatrix[][][] evalMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, z, d0, ranges);
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, d0, ranges);
    }

    public boolean isNaN() {
        return object.isNaN();
    }

    public boolean isInfinite() {
        return object.isInfinite();
    }

    public boolean isZero() {
        return object.isZero();
    }

    @Override
    public Expr getComponent(int row, int col) {
        return (object.toDM().getComponent(row, col));
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return DoubleToMatrixDefaults.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public ComplexMatrix evalMatrix(double x) {
        return object.toDM().evalMatrix(x);
    }

    public DoubleToDouble getRealDD() {
        return (toDC().getRealDD());
    }

    public DoubleToDouble getImagDD() {
        return (toDC().getImagDD());
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

        RefDoubleToMatrix ref = (RefDoubleToMatrix) o;

        return object != null ? object.equals(ref.object) : ref.object == null;
    }

    protected void init(Expr object) {
        this.object = object;
    }
}
