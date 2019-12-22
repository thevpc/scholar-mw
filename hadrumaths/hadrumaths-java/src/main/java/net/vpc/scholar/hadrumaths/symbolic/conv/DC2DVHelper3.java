package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.Range;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class DC2DVHelper3 {
    private DoubleToComplex xx;
    private DoubleToComplex yy;
    private DoubleToComplex zz;

    public DC2DVHelper3(DoubleToComplex xx, DoubleToComplex yy, DoubleToComplex zz) {
        this.xx = xx;
        this.yy = yy;
        this.zz = zz;
    }

    public Expr getComponent(Axis a) {
        switch (a){
            case X:return xx;
            case Y:return yy;
            case Z:{
                if(zz!=null){
                    return zz;
                }
            }
        }
        throw new IllegalArgumentException("Invalid component " + a);
    }

    public int getComponentSize() {
        return zz==null?2:3;
    }

    public Expr getX() {
        return xx;
    }

    public Expr getY() {
        return yy;
    }

    public Expr getZ() {
        return zz;
    }

    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, z, d0, ranges),
                yy.computeComplex(x, y, z, d0, ranges),
                zz == null ? null : zz.computeComplex(x, y, z, d0, ranges)
        );
    }

    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, d0, ranges)
                , yy.computeComplex(x, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, d0, ranges)
        );
    }

    public ComplexVector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, d0, ranges)
                , yy.computeComplex(x, y, d0, ranges)
                , zz == null ? null : zz.computeComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector computeVector(double x) {
        return ArrayUtils.c2v(
                xx.computeComplex(x)
                , yy.computeComplex(x)
                , zz == null ? null : zz.computeComplex(x)
        );
    }
    public ComplexVector computeVector(double x, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.computeComplex(x,defined)
                , yy.computeComplex(x,defined)
                , zz == null ? null : zz.computeComplex(x,defined)
        );
    }

    public ComplexVector computeVector(double x, double y) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y)
                , yy.computeComplex(x, y)
                , zz == null ? null : zz.computeComplex(x, y)
        );
    }

    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y,defined)
                , yy.computeComplex(x, y,defined)
                , zz == null ? null : zz.computeComplex(x, y,defined)
        );
    }

    public ComplexVector computeVector(double x, double y, double z) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, z)
                , yy.computeComplex(x, y, z)
                , zz == null ? null : zz.computeComplex(x, y, z)
        );
    }

    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, z,defined)
                , yy.computeComplex(x, y, z,defined)
                , zz == null ? null : zz.computeComplex(x, y, z,defined)
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

    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y, z)
                , yy.computeComplex(x, y, z)
                , zz == null ? null : zz.computeComplex(x, y, z)
        );
    }

    public ComplexVector[][] computeVector(double[] x, double[] y) {
        return ArrayUtils.c2v(
                xx.computeComplex(x, y)
                , yy.computeComplex(x, y)
                , zz == null ? null : zz.computeComplex(x, y)
        );
    }

    public ComplexVector[] computeVector(double[] x) {
        return ArrayUtils.c2v(
                xx.computeComplex(x)
                , yy.computeComplex(x)
                , zz == null ? null : zz.computeComplex(x)
        );
    }
}
