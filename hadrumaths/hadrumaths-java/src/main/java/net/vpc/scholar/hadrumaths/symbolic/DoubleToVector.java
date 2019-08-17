package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.io.Serializable;

/**
 * Created by vpc on 6/6/14.
 */
public interface DoubleToVector extends DoubleDomainExpr, Serializable, /*DoubleToMatrix,*/ Cloneable {
    Expr getComponent(Axis a);

    int getComponentSize();

    default Expr getX() {
        return getComponent(Axis.X);
    }

    default Expr getY() {
        return getComponent(Axis.Y);
    }

    default Expr getZ() {
        return getComponent(Axis.Z);
    }


    Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges);

    Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges);

    Vector computeVector(double x, double y, double z, BooleanMarker defined);

    Vector computeVector(double x, double y, BooleanMarker defined);

    Vector computeVector(double x, BooleanMarker defined);

    default Vector computeVector(double x) {
        return computeVector(x, BooleanMarker.none());
    }

    default Vector computeVector(double x, double y) {
        return computeVector(x, y, BooleanMarker.none());
    }

    default Vector computeVector(double x, double y, double z) {
        return computeVector(x, y, z, BooleanMarker.none());
    }

    default Vector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return computeVector(x, new double[]{y}, d0, ranges)[0];
    }

    default Vector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return computeVector(new double[]{x}, y, d0, ranges)[0];
    }

    default Vector[][][] computeVector(double[] x, double[] y, double[] z) {
        return computeVector(x, y, z, null, null);
    }

    default Vector[][] computeVector(double[] x, double[] y) {
        return computeVector(x, y, null, null);
    }

    default Vector[] computeVector(double[] x) {
        return computeVector(x, (Domain) null, null);
    }

}
