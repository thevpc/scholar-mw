/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * @author vpc
 */
public interface DoubleToDouble extends DoubleDomainExpr {

    double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges);

    double[] computeDouble(double[] x, Domain d0, Out<Range> range);

    /**
     * @param x
     * @param defined by ref, will return false if the expression is not defined for this value
     * @return
     */
    double computeDouble(double x, BooleanMarker defined);

    double computeDouble(double x, double y, BooleanMarker defined);

    double computeDouble(double x, double y, double z, BooleanMarker defined);

    default double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this,x,y,d0,ranges);
    }

    default double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this,x,y,d0,ranges);
    }

    default double computeDouble(double x) {
        return computeDouble(x, NoneOutBoolean.INSTANCE);
    }

    default double computeDouble(double x, double y) {
        return computeDouble(x, y, NoneOutBoolean.INSTANCE);
    }

    default double computeDouble(double x, double y, double z) {
        return computeDouble(x, y, z, NoneOutBoolean.INSTANCE);
    }

//    double[] computeDouble(double[] x);
//
//    double[][] computeDouble(double[] x, double[] y);
//
//    double[] computeDouble(double x, double[] y);
//
//    double[][][] computeDouble(double[] x, double[] y, double[] z);


    default double[] computeDouble(double[] x) {
        return computeDouble(x, (Domain) null, null);
    }

    default double[] computeDouble(double x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }

    default double[][][] computeDouble(double[] x, double[] y, double[] z) {
        return computeDouble(x, y, z, (Domain) null, null);
    }

    default double[][] computeDouble(double[] x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }


//    default double apply(double x) {
//        return computeDouble(x);
//    }
//
//    default double[] apply(double[] x) {
//        return computeDouble(x);
//    }
//
//    default double apply(double x, double y) {
//        return computeDouble(x, y);
//    }
//
//    default double[][] apply(double[] x, double[] y) {
//        return computeDouble(x, y);
//    }
}
