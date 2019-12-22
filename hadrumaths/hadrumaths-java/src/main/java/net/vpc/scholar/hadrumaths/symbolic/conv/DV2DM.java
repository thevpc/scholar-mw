package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Out;
import net.vpc.scholar.hadrumaths.symbolic.*;

public class DV2DM extends AdaptedExpr implements DoubleToMatrix {
    private DV2DMHelper dv2dm;

    public DV2DM(DoubleToVector base) {
        super(base);
        this.dv2dm = new DV2DMHelper(base);
    }
    @Override
    protected Expr newInstance(Expr base) {
        return new DV2DM(base.toDV());
    }

    //DC2DM START
    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return dv2dm.computeMatrix(x,y,z,d0,ranges);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return dv2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return dv2dm.computeMatrix(x,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return dv2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return dv2dm.computeMatrix(x,y,d0,ranges);
    }

    @Override
    public ComplexMatrix computeMatrix(double x) {
        return dv2dm.computeMatrix(x);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y) {
        return dv2dm.computeMatrix(x,y);
    }

    @Override
    public ComplexMatrix computeMatrix(double x, double y, double z) {
        return dv2dm.computeMatrix(x,y,z);
    }

    @Override
    public Expr getComponent(int row, int col) {
        return dv2dm.getComponent(row,col);
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return dv2dm.getComponentTitle(row,col);
    }
    //DC2DM END

}
