/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public abstract class Ref extends AbstractPolymorphExpr implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Ref.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Ref e = (Ref) expression;
                return (ExpressionTransformFactory.transform(e.object, transform));
            }
        });
    }

    private Expr object;

    protected Ref() {
    }

    @Override
    public int getComponentSize() {
        return object.getComponentDimension().rows;
    }

    @Override
    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDV().computeVector(x, y, z,d0, ranges);
    }

    @Override
    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDV().computeVector(x, y, d0, ranges);
    }

    @Override
    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDV().computeVector(x, d0, ranges);
    }

    @Override
    public boolean isDoubleTyped() {
        return object.isDoubleTyped();
    }


    public String getTitle() {
        return object.getTitle();
    }


    public static Expr unwrap(Expr e) {
        return !(e instanceof Ref) ? e : (((Ref) e).object);
    }

    public boolean isZeroImpl() {
        return object.isZero();
    }

    public boolean isNaNImpl() {
        return object.isNaN();
    }

    public boolean isInfiniteImpl() {
        return object.isInfinite();
    }

    public boolean isInvariantImpl(Axis axis) {
        return object.isInvariant(axis);
    }

    public Expr getObject() {
        return object;
    }

    @Override
    public boolean isDCImpl() {
        return object.isDC();
    }

    @Override
    public boolean isDDImpl() {
        return object.isDD();
    }

//    @Override
//    public boolean isDDx() {
//        return object.isDDx();
//    }

    @Override
    public boolean isDMImpl() {
        return object.isDM();
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
    public DoubleToMatrix toDM() {
        return object.toDM();
    }

//    @Override
//    public IDDx toDDx() {
//        return object.toDDx();
//    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y) {
        return object.toDC().computeComplex(x, y);
    }

    @Override
    public Domain getDomainImpl() {
        return object.getDomain();
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return object.toDD().computeDouble(x, d0, range);
    }

    @Override
    public double computeDouble(double x) {
        return object.toDD().computeDouble(x);
    }

//    @Override
//    public Domain getDomain() {
//        return object.toDDx().getDomain();
//    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double computeDouble(double x, double y) {
        return object.toDD().computeDouble(x, y);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y) {
        return object.toDM().computeMatrix(x, y);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return object.getComponentDimension();
    }

    @Override
    public Expr getComponent(int row, int col) {
        return (object.toDM().getComponent(row, col));
    }

    public DoubleToDouble getRealDD() {
        return (toDC().getRealDD());
    }

    public DoubleToDouble getImagDD() {
        return (toDC().getImagDD());
    }

    public Expr clone() {
        Ref cloned = (Ref) super.clone();
        cloned.object = object.clone();
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return object.getSubExpressions();
    }

    @Override
    public boolean isDoubleImpl() {
        return object.isDouble();
    }

    @Override
    public boolean isComplexImpl() {
        return object.isComplex();
    }

    public boolean isDoubleExprImpl() {
        return object.isDoubleExpr();
    }

    @Override
    public boolean isMatrixImpl() {
        return object.isMatrix();
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
    public ComplexMatrix toMatrix() {
        return object.toMatrix();
    }

    @Override
    public boolean hasParamsImpl() {
        return object.hasParams();
    }

    @Override
    public Expr setParam(String name, double value) {
        return (object.setParam(name, value));
    }

    @Override
    public Expr setParam(String name, Expr value) {
        //TODO
        return (object.setParam(name, value));
    }

//    @Override
//    public String toString() {
//        return String.valueOf(object);
//    }

    @Override
    public Map<String, Object> getProperties() {
        return getObject().getProperties();
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
    public Object getProperty(String name) {
        return getObject().getProperty(name);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        return getObject().setProperty(name, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Ref ref = (Ref) o;

        return object != null ? object.equals(ref.object) : ref.object == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public Expr compose(Axis axis,Expr xreplacement) {
        return (object.compose(axis,xreplacement));
    }

    @Override
    public boolean isScalarExprImpl() {
        return object.isScalarExpr();
    }

    @Override
    public boolean isDVImpl() {
        return object.isDV();
    }

    @Override
    public DoubleToVector toDV() {
        return object.toDV();
    }

    protected void init(Expr object) {
        this.object = object;
    }

    @Override
    public int getDomainDimension() {
        return object.getDomainDimension();
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, z, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, z, d0, ranges);
    }

    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, z, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y, double z) {
        return object.toDC().computeComplex(x, y, z);
    }

    @Override
    public Complex computeComplex(double x) {
        return object.toDC().computeComplex(x);
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        return object.toDC().computeComplex(x);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, d0, ranges);
    }

    //    public Ref forObject(Expression obj){
//        return new Ref(obj);
//    }


    @Override
    public double computeDouble(double x, double y, double z) {
        return object.toDD().computeDouble(x, y, z);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y, double z) {
        return object.toDM().computeMatrix(x, y, z);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, d0, ranges);
    }

    @Override
    public ComplexMatrix computeMatrix(double x) {
        return object.toDM().computeMatrix(x);
    }

    @Override
    public Expr getComponent(Axis a) {
        return object.toDV().getComponent(a);
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        return object.toDC().computeComplex(x, y, defined);
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        return object.toDC().computeComplex(x, y, z, defined);
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        return object.toDD().computeDouble(x, defined);
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        return object.toDD().computeDouble(x, y, defined);
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        return object.toDD().computeDouble(x, y, z, defined);
    }
}
