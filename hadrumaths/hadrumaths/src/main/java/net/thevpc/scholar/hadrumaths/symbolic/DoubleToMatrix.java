/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.*;

/**
 * function double -> CMatrix
 *
 * @author vpc
 */
public interface DoubleToMatrix extends DoubleDomainExpr {

    default String getComponentTitle(int row, int col) {
        return DoubleToMatrixDefaults.getMatrixExpressionTitleByChildren(this, row, col);
    }

    default ComplexMatrix[] evalMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return DoubleToMatrixDefaults.evalMatrix(this, x, y, d0, ranges);
    }

    default ComplexMatrix[] evalMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return DoubleToMatrixDefaults.evalMatrix(this, x, y, d0, ranges);
    }

    default ComplexMatrix apply(double x) {
        return evalMatrix(x);

    }

    default ComplexMatrix evalMatrix(double x) {
        return evalMatrix(x, BooleanMarker.none());
    }

    ComplexMatrix evalMatrix(double x, BooleanMarker defined);

    default ComplexMatrix apply(double x, double y) {
        return evalMatrix(x, y);
    }

    default ComplexMatrix evalMatrix(double x, double y) {
        return evalMatrix(x, y, BooleanMarker.none());
    }

    ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined);

    default ComplexMatrix apply(double x, double y, double z) {
        return evalMatrix(x, y);
    }

    default ComplexMatrix evalMatrix(double x, double y, double z) {
        return evalMatrix(x, y, z, BooleanMarker.none());
    }

    ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined);

    default ComplexMatrix[] evalMatrix(double[] x) {
        return evalMatrix(x, (Domain) null, null);
    }

    default ComplexMatrix[] evalMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return DoubleToMatrixDefaults.evalMatrix(this, x, d0, ranges);
    }

    default ComplexMatrix[][] evalMatrix(double[] x, double[] y) {
        return evalMatrix(x, y, null, null);
    }

    default ComplexMatrix[][] evalMatrix(double[] x, double[] y, Domain d0) {
        return evalMatrix(x,y,d0,null);
    }

    default ComplexMatrix[][] evalMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return DoubleToMatrixDefaults.evalMatrix(this, x, y, d0, ranges);
    }

    default ComplexMatrix[][][] evalMatrix(double[] x, double[] y, double[] z) {
        return evalMatrix(x, y, z, null, null);
    }

    default ComplexMatrix[][][] evalMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return DoubleToMatrixDefaults.evalMatrix(this, x, y, z, d0, ranges);
    }

    default ExprType getNarrowType() {
        if (getComponentDimension().is(1, 1)) {
            return getComponent(0, 0).getNarrowType();
        }
        return getType();
    }

    Expr getComponent(int row, int col);

    default DoubleToComplex toDC() {
        return DoubleToMatrixDefaults.toDC(this);
    }

    default DoubleToDouble toDD() {
        return (DoubleToDouble) narrow(ExprType.DOUBLE_DOUBLE);
    }

    default DoubleToVector toDV() {
        return (DoubleToVector) narrow(ExprType.DOUBLE_CVECTOR);
    }

    default DoubleToMatrix toDM() {
        return this;
    }

    @Override
    default ExprType getType() {
        return ExprType.DOUBLE_CMATRIX;
    }

}
