/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.ComponentDimension;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Out;

/**
 * function double -> CMatrix
 * @author vpc
 */
public interface DoubleToMatrix extends Expr {

    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z,Domain d0, Out<Range> ranges);

    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges);

    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges);

    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges);

    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges);

    public Matrix computeMatrix(double x);

    public Matrix computeMatrix(double x, double y);

    public Matrix computeMatrix(double x, double y, double z);

    public ComponentDimension getComponentDimension();

    public Expr getComponent(int row, int col);

    public String getComponentTitle(int row, int col);

    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z);
    public Matrix[][] computeMatrix(double[] x, double[] y);
    public Matrix[] computeMatrix(double[] x);
//    public IDMxy setName(String name);
}
