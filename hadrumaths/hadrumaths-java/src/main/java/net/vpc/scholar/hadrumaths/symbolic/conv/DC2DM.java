package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.AdaptedExpr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.Range;

public class DC2DM extends AdaptedExpr implements DoubleToMatrix {
    private DC2DMHelper dc2dm;

    public DC2DM(DoubleToComplex base) {
        super(base);
        this.dc2dm = new DC2DMHelper(base);
    }
    @Override
    protected Expr newInstance(Expr base) {
        return new DC2DM((DoubleToComplex)base);
    }

    //DC2DM START
    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return dc2dm.computeMatrix(x,y,z,d0,ranges);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return dc2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return dc2dm.computeMatrix(x,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return dc2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return dc2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix computeMatrix(double x) {
        return dc2dm.computeMatrix(x);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y) {
        return dc2dm.computeMatrix(x,y);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y, double z) {
        return dc2dm.computeMatrix(x,y,z);
    }

    @Override
    public Expr getComponent(int row, int col) {
        return dc2dm.getComponent(row,col);
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return dc2dm.getComponentTitle(row,col);
    }

    //DC2DM END

}
