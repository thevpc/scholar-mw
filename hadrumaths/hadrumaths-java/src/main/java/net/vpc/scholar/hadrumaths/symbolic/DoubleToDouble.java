/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Out;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * @author vpc
 */
public interface DoubleToDouble extends DoubleDomainExpr {

    double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges);

    double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges);

    double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges);

    double[] computeDouble(double[] x, Domain d0, Out<Range> range);

    /**
     * @param x
     * @param defined by ref, will return false if the expression is not defined for this value
     * @return
     */
    double computeDouble(double x, OutBoolean defined);

    double computeDouble(double x, double y, OutBoolean defined);

    double computeDouble(double x, double y, double z, OutBoolean defined);

    default double computeDouble(double x) {
        return computeDouble(x, new OutBoolean());
    }

    default double computeDouble(double x, double y) {
        return computeDouble(x, y, new OutBoolean());
    }

    default double computeDouble(double x, double y, double z) {
        return computeDouble(x, y, z, new OutBoolean());
    }

    double[] computeDouble(double[] x);

    double[][] computeDouble(double[] x, double[] y);

    double[] computeDouble(double x, double[] y);

    double[][][] computeDouble(double[] x, double[] y, double[] z);

}
