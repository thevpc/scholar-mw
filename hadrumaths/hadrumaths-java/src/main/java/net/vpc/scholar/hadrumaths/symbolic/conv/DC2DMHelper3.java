package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Out;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class DC2DMHelper3 {
    private DoubleToComplex xx;
    private DoubleToComplex yy;
    private DoubleToComplex zz;

    public DC2DMHelper3(DoubleToComplex xx, DoubleToComplex yy, DoubleToComplex zz) {
        this.xx = xx;
        this.yy = yy;
        this.zz = zz;
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, z, d0, ranges),
                yy.computeComplex(x, y, z, d0, ranges),
                zz == null ? null : zz.computeComplex(x, y, z, d0, ranges)
        );
    }

    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, d0, ranges)
                , yy.computeComplex(x, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, d0, ranges)
        );
    }

    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexMatrix computeMatrix(double x) {
        return ArrayUtils.c2m(
                xx.computeComplex(x)
                , yy.computeComplex(x)
                , zz == null ? null : zz.computeComplex(x)
        );
    }

    public ComplexMatrix computeMatrix(double x, double y) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y)
                , yy.computeComplex(x, y)
                , zz == null ? null : zz.computeComplex(x, y)
        );
    }

    public ComplexMatrix computeMatrix(double x, double y, double z) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, z)
                , yy.computeComplex(x, y, z)
                , zz == null ? null : zz.computeComplex(x, y, z)
        );
    }

    public Expr getComponent(int row, int col) {
        if (col == 0) {
            switch (row) {
                case 0:
                    return xx;
                case 1:
                    return yy;
                case 2: {
                    if (zz != null) {
                        return zz;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Invalid row,column " + row + "," + col);
    }

    public String getComponentTitle(int row, int col) {
        if (col == 0) {
            switch (row) {
                case 0:
                    return "X";
                case 1:
                    return "Y";
                case 2: {
                    if (zz != null) {
                        return "Z";
                    }
                }
            }
        }
        throw new IllegalArgumentException("Invalid row,column " + row + "," + col);
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y, z)
                , yy.computeComplex(x, y, z)
                , zz == null ? null : zz.computeComplex(x, y, z)
        );
    }

    public ComplexMatrix[][] computeMatrix(double[] x, double[] y) {
        return ArrayUtils.c2m(
                xx.computeComplex(x, y)
                , yy.computeComplex(x, y)
                , zz == null ? null : zz.computeComplex(x, y)
        );
    }

    public ComplexMatrix[] computeMatrix(double[] x) {
        return ArrayUtils.c2m(
                xx.computeComplex(x)
                , yy.computeComplex(x)
                , zz == null ? null : zz.computeComplex(x)
        );
    }
}
