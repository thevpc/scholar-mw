/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.ExpressionTransformFactory;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public abstract class Ref extends AbstractVerboseExpr implements Cloneable {

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

    public String getName() {
        return object.getName();
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
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
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

    public DoubleToDouble getReal() {
        return (toDC().getReal());
    }

    public DoubleToDouble getImag() {
        return (toDC().getImag());
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
    public Matrix toMatrix() {
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

    @Override
    public String toString() {
        return String.valueOf(object);
    }

    @Override
    public Map<String, Object> getProperties() {
        return getObject().getProperties();
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return /*wrap*/(getObject().setProperties(map));
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
    public Expr composeX(Expr xreplacement) {
        return (object.composeX(xreplacement));
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return (object.composeY(yreplacement));
    }

//    @Override
//    public Expr setTitle(String title) {
//        getProperties().put("title", title);
//        return this;
//    }

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
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
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
    public Matrix computeMatrix(double x, double y, double z) {
        return object.toDM().computeMatrix(x, y, z);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x) {
        return object.toDM().computeMatrix(x);
    }

    @Override
    public Expr getComponent(Axis a) {
        return object.toDV().getComponent(a);
    }
}
