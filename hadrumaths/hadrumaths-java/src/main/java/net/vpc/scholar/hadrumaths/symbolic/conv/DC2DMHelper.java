package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class DC2DMHelper {
    private DoubleToComplex baseExpr;

    public DC2DMHelper(DoubleToComplex baseExpr) {
        this.baseExpr = baseExpr;
    }


    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,z,d0,ranges));
    }

    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,d0,ranges));
    }

    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public Matrix computeMatrix(double x) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x));
    }

    public Matrix computeMatrix(double x, double y) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y));
    }

    public Matrix computeMatrix(double x, double y, double z) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,z));
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

    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y,z));
    }

    public Matrix[][] computeMatrix(double[] x, double[] y) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x,y));
    }

    public Matrix[] computeMatrix(double[] x) {
        return ArrayUtils.c2m(baseExpr.computeComplex(x));
    }
}
