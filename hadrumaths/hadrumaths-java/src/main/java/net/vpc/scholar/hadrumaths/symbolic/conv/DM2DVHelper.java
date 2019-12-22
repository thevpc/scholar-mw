package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.Range;

public class DM2DVHelper {
    private DoubleToMatrix baseExpr;

    public DM2DVHelper(DoubleToMatrix baseExpr) {
        this.baseExpr = baseExpr;
    }

    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,z,d0,ranges));
    }

    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,d0,ranges));
    }

    public ComplexVector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public ComplexVector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public ComplexVector computeVector(double x) {
        return m2v(baseExpr.computeMatrix(x));
    }

    public ComplexVector computeVector(double x, double y) {
        return m2v(baseExpr.computeMatrix(x,y));
    }

    public ComplexVector computeVector(double x, double y, double z) {
        return m2v(baseExpr.computeMatrix(x,y,z));
    }

    public Expr getComponent(int row, int col) {
        if(row==0 && col==0) {
            return baseExpr;
        }
        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
    }

    public String getComponentTitle(int row, int col) {
        if(row==0 && col==0) {
            return baseExpr.toString();
        }
        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
    }

    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z) {
        return m2v(baseExpr.computeMatrix(x,y,z));
    }

    public ComplexVector[][] computeVector(double[] x, double[] y) {
        return m2v(baseExpr.computeMatrix(x,y));
    }

    public ComplexVector[] computeVector(double[] x) {
        return m2v(baseExpr.computeMatrix(x));
    }

    private ComplexVector m2v(ComplexMatrix m){
        return m.toVector();
    }

    private ComplexVector[] m2v(ComplexMatrix[] m){
        ComplexVector[] a=new ComplexVector[m.length];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
    private ComplexVector[][] m2v(ComplexMatrix[][] m){
        ComplexVector[][] a=new ComplexVector[m.length][];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
    private ComplexVector[][][] m2v(ComplexMatrix[][][] m){
        ComplexVector[][][] a=new ComplexVector[m.length][][];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
}
