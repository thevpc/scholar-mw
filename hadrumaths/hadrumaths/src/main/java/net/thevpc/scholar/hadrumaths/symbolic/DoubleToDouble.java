/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.*;

/**
 * @author vpc
 */
public interface DoubleToDouble extends DoubleDomainExpr {

    double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges);

    default double evalDouble(double x, double y, double z) {
        return evalDouble(x, y, z, NoneOutBoolean.INSTANCE);
    }

    double evalDouble(double x, double y, double z, BooleanMarker defined);

    default double[] evalDouble(double[] x) {
        return evalDouble(x, (Domain) null, null);
    }

    double[] evalDouble(double[] x, Domain d0, Out<Range> range);

    default double[] evalDouble(double x, double[] y) {
        return evalDouble(x, y, null, null);
    }

    double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges);

    default double[][][] evalDouble(double[] x, double[] y, double[] z) {
        return evalDouble(x, y, z, null, null);
    }

    double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    default double[][] evalDouble(double[] x, double[] y) {
        return evalDouble(x, y, null, null);
    }

    double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges);

    default double apply(double x) {
        return evalDouble(x);
    }

    default double evalDouble(double x) {
        return evalDouble(x, NoneOutBoolean.INSTANCE);
    }

    /**
     * @param x
     * @param defined by ref, will return false if the expression is not defined for this value
     * @return
     */
    double evalDouble(double x, BooleanMarker defined);

    default double apply(double x, double y) {
        return evalDouble(x, y);
    }

    default double evalDouble(double x, double y) {
        return evalDouble(x, y, NoneOutBoolean.INSTANCE);
    }

    double evalDouble(double x, double y, BooleanMarker defined);

    default double apply(double x, double y, double z) {
        return evalDouble(x, y);
    }

    default ExprType getNarrowType() {
        return getType();
    }

    default DoubleToComplex toDC() {
        return (DoubleToComplex) narrow(ExprType.DOUBLE_COMPLEX);
    }

    default DoubleToDouble toDD() {
        return this;
    }

    default DoubleToVector toDV() {
        return (DoubleToVector) narrow(ExprType.DOUBLE_CVECTOR);
    }

    default DoubleToMatrix toDM() {
        return (DoubleToMatrix) narrow(ExprType.DOUBLE_CMATRIX);
    }

    default ExprType getType() {
        return ExprType.DOUBLE_DOUBLE;
    }

    @Override
    default ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

}
