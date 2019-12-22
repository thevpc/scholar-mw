package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class DC2DVHelper {
    private DoubleToComplex baseExpr;

    public DC2DVHelper(DoubleToComplex baseExpr) {
        this.baseExpr = baseExpr;
    }


    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,z,d0,ranges));
    }

    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,d0,ranges));
    }

    public ComplexVector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public ComplexVector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,d0,ranges));
    }

    public ComplexVector computeVector(double x) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x));
    }

    public ComplexVector computeVector(double x, double y) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y));
    }

    public ComplexVector computeVector(double x, double y, double z) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,z));
    }

    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,z, defined));
    }

    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y, defined));
    }

    public ComplexVector computeVector(double x, BooleanMarker defined) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x, defined));
    }

//    public Expr getComponent(int row, int col) {
//        if(row==0 && col==0) {
//            return baseExpr;
//        }
//        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
//    }
//
//    public String getComponentTitle(int row, int col) {
//        if(row==0 && col==0) {
//            return baseExpr.toString();
//        }
//        throw new IllegalArgumentException("Invalid row,column "+row+","+col);
//    }

    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y,z));
    }

    public ComplexVector[][] computeVector(double[] x, double[] y) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x,y));
    }

    public ComplexVector[] computeVector(double[] x) {
        return ArrayUtils.c2v(baseExpr.computeComplex(x));
    }

    public Expr getComponent(Axis a) {
        switch (a){
            case X:return baseExpr;
        }
        throw new IllegalArgumentException("Invalid axis "+a);
    }

    public int getComponentSize() {
        return 1;
    }

    public Expr getX() {
        return baseExpr;
    }

    public Expr getY() {
        return MathsBase.CZERO;
    }

    public Expr getZ() {
        return MathsBase.CZERO;
    }
}
