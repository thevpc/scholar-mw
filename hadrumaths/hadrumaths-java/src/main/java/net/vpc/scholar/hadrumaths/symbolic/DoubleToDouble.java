/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Out;

/**
 * @author vpc
 */
public interface DoubleToDouble extends Expr {

    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges);

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges);

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges);

    public double computeDouble(double x, double y);

    public double computeDouble(double x, double y, double z);

    public double[] computeDouble(double[] x, Domain d0, Out<Range> range);

    public double computeDouble(double x);

    public double[] computeDouble(double[] x);

    public double[][] computeDouble(double[] x, double[] y);

    public double[] computeDouble(double x, double[] y);

    public double[][][] computeDouble(double[] x, double[] y, double[] z);

}
