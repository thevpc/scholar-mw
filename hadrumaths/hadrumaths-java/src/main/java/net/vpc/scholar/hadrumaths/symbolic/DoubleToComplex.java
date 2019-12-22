/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadruplot.Samples;

/**
 * @author vpc
 */
public interface DoubleToComplex extends DoubleDomainExpr {


    Complex computeComplex(double x, BooleanMarker defined);

    Complex computeComplex(double x, double y, BooleanMarker defined);

    Complex computeComplex(double x, double y, double z, BooleanMarker defined);

    Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges);

    Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges);

    Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    DoubleToDouble getRealDD();

    DoubleToDouble getImagDD();

    default Complex[] computeComplex(double[] x, Domain d0) {
        return computeComplex(x, d0, null);
    }

    default Complex[] computeComplex(double[] x, double y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    default Complex[] computeComplex(double x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    default Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        return computeComplex(x, y, z, d0, null);
    }

    default Complex[] computeComplex(double x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    default Complex[] computeComplex(double[] x, double y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    default Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    default Complex computeComplex(double x) {
        return computeComplex(x, NoneOutBoolean.INSTANCE);
    }

    default Complex computeComplex(double x, double y) {
        return computeComplex(x, y, NoneOutBoolean.INSTANCE);
    }

    default Complex computeComplex(double x, double y, double z) {
        return computeComplex(x, y, z, NoneOutBoolean.INSTANCE);
    }

    default Complex[] computeComplex(double[] x) {
        return computeComplex(x, (Domain) null, null);
    }

    default Complex[][] computeComplex(double[] x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    default Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        return computeComplex(x, y, z, (Domain) null, null);
    }

    default Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    default Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }


//    default Complex apply(double x) {
//        return computeComplex(x);
//    }
//
//    default Complex[] apply(double[] x) {
//        return computeComplex(x);
//    }
//
//    default Complex apply(double x, double y) {
//        return computeComplex(x, y);
//    }
//
//    default Complex[][] apply(double[] x, double[] y) {
//        return computeComplex(x, y);
//    }

}
