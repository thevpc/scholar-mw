package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.*;

import java.io.Serializable;

/**
 * Created by vpc on 6/6/14.
 */
public interface DoubleToVector extends DoubleDomainExpr, Serializable {
    default Expr X() {
        return getX();
    }

    default Expr getX() {
        return getComponent(Axis.X);
    }

    Expr getComponent(Axis a);

    default Expr Y() {
        return getY();
    }

    default Expr getY() {
        return getComponent(Axis.Y);
    }

    default Expr Z() {
        return getZ();
    }

    default Expr getZ() {
        return getComponent(Axis.Z);
    }

    default ComplexVector evalVector(double x, double y, double z) {
        return evalVector(x, y, z, BooleanMarker.none());
    }

    ComplexVector evalVector(double x, double y, double z, BooleanMarker defined);

    default ComplexVector[] evalVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return evalVector(x, new double[]{y}, d0, ranges)[0];
    }

    default ComplexVector[][] evalVector(double[] x, double[] y, Domain d0) {
        return evalVector(x,y,d0,null);
    }

    default ComplexVector[][] evalVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return DoubleToVectorDefaults.evalVector(this, x, y, d0, ranges);
    }

    default ComplexVector[] evalVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return evalVector(new double[]{x}, y, d0, ranges)[0];
    }

    default ComplexVector[][][] evalVector(double[] x, double[] y, double[] z) {
        return evalVector(x, y, z, null, null);
    }

    default ComplexVector[][][] evalVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return DoubleToVectorDefaults.evalVector(this, x, y, z, d0, ranges);
    }

    default ComplexVector[][] evalVector(double[] x, double[] y) {
        return evalVector(x, y, null, null);
    }

    default ComplexVector[] evalVector(double[] x) {
        return evalVector(x, (Domain) null, null);
    }

    default ComplexVector[] evalVector(double[] x, Domain d0, Out<Range> ranges) {
        return DoubleToVectorDefaults.evalVector(this, x, d0, ranges);
    }

    default ComplexVector apply(double x) {
        return evalVector(x);

    }

    default ComplexVector evalVector(double x) {
        return evalVector(x, BooleanMarker.none());
    }

    ComplexVector evalVector(double x, BooleanMarker defined);

    default ComplexVector apply(double x, double y) {
        return evalVector(x, y);
    }

    default ComplexVector evalVector(double x, double y) {
        return evalVector(x, y, BooleanMarker.none());
    }

    ComplexVector evalVector(double x, double y, BooleanMarker defined);

    default ComplexVector apply(double x, double y, double z) {
        return evalVector(x, y);
    }

    int getComponentSize();


//    default Vector apply(double x) {
//        return computeVector(x);
//    }
//
//    default Vector[] apply(double[] x) {
//        return computeVector(x);
//    }
//
//    default Vector apply(double x, double y) {
//        return computeVector(x, y);
//    }
//
//    default Vector[][] apply(double[] x, double[] y) {
//        return computeVector(x, y);
//    }


    default ExprType getNarrowType() {
        if (getComponentSize() == 1) {
            return getComponent(Axis.X).getNarrowType();
        }
        return getType();
    }

    default DoubleToComplex toDC() {
        return (DoubleToComplex) narrow(ExprType.DOUBLE_COMPLEX);
    }

    default DoubleToDouble toDD() {
        return (DoubleToDouble) narrow(ExprType.DOUBLE_DOUBLE);
    }

    default DoubleToVector toDV() {
        return this;
    }

    default DoubleToMatrix toDM() {
        return (DoubleToMatrix) narrow(ExprType.DOUBLE_CMATRIX);
    }

    @Override
    default ExprType getType() {
        return ExprType.DOUBLE_CVECTOR;
    }
}
