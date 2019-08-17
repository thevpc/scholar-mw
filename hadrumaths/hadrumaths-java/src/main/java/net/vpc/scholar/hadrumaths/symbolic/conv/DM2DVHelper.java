package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class DM2DVHelper {
    private DoubleToMatrix baseExpr;

    public DM2DVHelper(DoubleToMatrix baseExpr) {
        this.baseExpr = baseExpr;
    }

    public Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,z,d0,ranges));
    }

    public Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,d0,ranges));
    }

    public Vector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public Vector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return m2v(baseExpr.computeMatrix(x,y,d0,ranges));
    }

    public Vector computeVector(double x) {
        return m2v(baseExpr.computeMatrix(x));
    }

    public Vector computeVector(double x, double y) {
        return m2v(baseExpr.computeMatrix(x,y));
    }

    public Vector computeVector(double x, double y, double z) {
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

    public Vector[][][] computeVector(double[] x, double[] y, double[] z) {
        return m2v(baseExpr.computeMatrix(x,y,z));
    }

    public Vector[][] computeVector(double[] x, double[] y) {
        return m2v(baseExpr.computeMatrix(x,y));
    }

    public Vector[] computeVector(double[] x) {
        return m2v(baseExpr.computeMatrix(x));
    }

    private Vector m2v(Matrix m){
        return m.toVector();
    }

    private Vector[] m2v(Matrix[] m){
        Vector[] a=new Vector[m.length];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
    private Vector[][] m2v(Matrix[][] m){
        Vector[][] a=new Vector[m.length][];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
    private Vector[][][] m2v(Matrix[][][] m){
        Vector[][][] a=new Vector[m.length][][];
        for (int i = 0; i < m.length; i++) {
            a[i]=m2v(m[i]);
        }
        return a;
    }
}
