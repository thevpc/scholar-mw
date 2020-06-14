/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.double2double;

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
public abstract class RefDoubleToDouble extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(RefDoubleToDouble.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                RefDoubleToDouble e = (RefDoubleToDouble) expression;
                return (ExpressionTransformFactory.transform(e.getReference(), transform));
            }
        });
    }

    protected RefDoubleToDouble() {

    }

    public static Expr unwrap(Expr e) {
        return !(e instanceof RefDoubleToDouble) ? e : (((RefDoubleToDouble) e).getReference());
    }

    public boolean isInvariant(Axis axis) {
        return getReference().isInvariant(axis);
    }

    @Override
    public double toDouble() {
        return getReference().toDouble();
    }

    @Override
    public DoubleToComplex toDC() {
        return getReference().toDC();
    }

    @Override
    public DoubleToVector toDV() {
        return getReference().toDV();
    }

    @Override
    public DoubleToMatrix toDM() {
        return getReference().toDM();
    }

    public boolean isZero() {
        return getReference().isZero();
    }

    public boolean isNaN() {
        return getReference().isNaN();
    }

    @Override
    public boolean hasParams() {
        return getReference().hasParams();
    }

    @Override
    public Expr setParam(String name, double value) {
        return (getReference().setParam(name, value));
    }
    @Override
    public Expr setParams(ParamValues params) {
        return getReference().setParams(params);
    }

//    @Override
//    public IDDx toDDx() {
//        return object.toDDx();
//    }

    @Override
    public Expr setParam(String name, Expr value) {
        //TODO
        return (getReference().setParam(name, value));
    }

    public boolean isInfinite() {
        return getReference().isInfinite();
    }

    @Override
    public Object getProperty(String name) {
        return getReference().getProperty(name);
    }

    @Override
    public Map<String, Object> getProperties() {
        return getReference().getProperties();
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return /*wrap*/(getReference().setProperties(map));
    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        return getReference().setProperties(map, merge);
    }

//    @Override
//    public Domain getDomain() {
//        return object.toDDx().getDomain();
//    }

    @Override
    public Expr setProperty(String name, Object value) {
        return getReference().setProperty(name, value);
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return (getReference().compose(xreplacement, yreplacement, zreplacement));
    }

    public String getTitle() {
        return getReference().getTitle();
    }

    public abstract DoubleToDouble getReference();

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        return getReference().evalDouble(x, defined);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        return getReference().evalDouble(x, y, defined);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return getReference().evalDouble(x, y, z, defined);
    }

    @Override
    public double evalDouble(double x) {
        return getReference().evalDouble(x);
    }

    @Override
    public double evalDouble(double x, double y) {
        return getReference().evalDouble(x, y);
    }

    @Override
    public double evalDouble(double x, double y, double z) {
        return getReference().evalDouble(x, y, z);
    }

    @Override
    public Domain getDomain() {
        return getReference().getDomain();
    }

        @Override
    public String toString() {
            return ExprDefaults.toString(this);
    }

    public List<Expr> getChildren() {
        return getReference().getChildren();
    }

    @Override
    public Complex toComplex() {
        return getReference().toComplex();
    }

    public DoubleToDouble getRealDD() {
        return (toDC().getRealDD());
    }

    public DoubleToDouble getImagDD() {
        return (toDC().getImagDD());
    }

    @Override
    public double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return getReference().evalDouble(x, y, z, d0, ranges);
    }

    @Override
    public double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return getReference().evalDouble(x, y, d0, ranges);
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        return getReference().evalDouble(x, d0, range);
    }

    @Override
    public double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return getReference().evalDouble(x, y, d0, ranges);
    }

    @Override
    public double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return getReference().evalDouble(x, y, d0, ranges);
    }


}
