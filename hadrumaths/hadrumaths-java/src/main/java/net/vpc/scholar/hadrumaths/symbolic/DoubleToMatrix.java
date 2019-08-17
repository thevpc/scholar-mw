/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * function double -> CMatrix
 *
 * @author vpc
 */
public interface DoubleToMatrix extends DoubleDomainExpr {

    ComponentDimension getComponentDimension();

    Expr getComponent(int row, int col);

    String getComponentTitle(int row, int col);

    Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges);

    Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges);

    default Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    default Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }
    default Matrix computeMatrix(double x) {
        return computeMatrix(new double[]{x}, (Domain) null, null)[0];
    }

    default Matrix computeMatrix(double x, double y) {
        return Expressions.computeMatrix(this, x, y);
    }

    default Matrix computeMatrix(double x, double y, double z){
        return Expressions.computeMatrix(this, x, y);
    }

    default Matrix[] computeMatrix(double[] x) {
        return computeMatrix(x, (Domain) null, null);
    }

    default Matrix[][] computeMatrix(double[] x, double[] y) {
        return computeMatrix(x, y, (Domain) null, null);
    }

    default Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        return computeMatrix(x, y, z, (Domain) null, null);
    }

}
