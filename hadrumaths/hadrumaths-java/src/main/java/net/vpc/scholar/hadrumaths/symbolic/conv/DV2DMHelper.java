package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.Range;

public class DV2DMHelper {
    private DoubleToVector baseExpr;

    public DV2DMHelper(DoubleToVector baseExpr) {
        this.baseExpr = baseExpr;
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return v2m(baseExpr.computeVector(x,y,z,d0,ranges));
    }

    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return v2m(baseExpr.computeVector(x,y,d0,ranges));
    }

    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return v2m(baseExpr.computeVector(x,d0,ranges));
    }

    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return v2m(baseExpr.computeVector(x,y,d0,ranges));
    }

    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return v2m(baseExpr.computeVector(x,y,d0,ranges));
    }

    public ComplexMatrix computeMatrix(double x) {
        return v2m(baseExpr.computeVector(x));
    }

    public ComplexMatrix computeMatrix(double x, double y) {
        return v2m(baseExpr.computeVector(x,y));
    }

    public ComplexMatrix computeMatrix(double x, double y, double z) {
        return v2m(baseExpr.computeVector(x,y,z));
    }

    public Expr getComponent(int row, int col) {
        if(col==0) {
            return baseExpr.getComponent(Axis.values()[row]);
        }
        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
    }

    public String getComponentTitle(int row, int col) {
        if(col==0) {
            return baseExpr.getComponent(Axis.values()[row]).toString();
        }
        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        return v2m(baseExpr.computeVector(x,y,z));
    }

    public ComplexMatrix[][] computeMatrix(double[] x, double[] y) {
        return v2m(baseExpr.computeVector(x,y));
    }

    public ComplexMatrix[] computeMatrix(double[] x) {
        return v2m(baseExpr.computeVector(x));
    }

    private ComplexMatrix v2m(ComplexVector m){
        return m.toMatrix();
    }

    private ComplexMatrix[] v2m(ComplexVector[] m){
        ComplexMatrix[] a=new ComplexMatrix[m.length];
        for (int i = 0; i < m.length; i++) {
            a[i]=v2m(m[i]);
        }
        return a;
    }
    private ComplexMatrix[][] v2m(ComplexVector[][] m){
        ComplexMatrix[][] a=new ComplexMatrix[m.length][];
        for (int i = 0; i < m.length; i++) {
            a[i]=v2m(m[i]);
        }
        return a;
    }
    private ComplexMatrix[][][] v2m(ComplexVector[][][] m){
        ComplexMatrix[][][] a=new ComplexMatrix[m.length][][];
        for (int i = 0; i < m.length; i++) {
            a[i]=v2m(m[i]);
        }
        return a;
    }
}
