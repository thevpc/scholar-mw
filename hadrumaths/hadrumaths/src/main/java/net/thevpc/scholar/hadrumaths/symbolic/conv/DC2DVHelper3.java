package net.thevpc.scholar.hadrumaths.symbolic.conv;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

public class DC2DVHelper3 {
    private final DoubleToComplex xx;
    private final DoubleToComplex yy;
    private final DoubleToComplex zz;

    public DC2DVHelper3(DoubleToComplex xx, DoubleToComplex yy, DoubleToComplex zz) {
        this.xx = xx;
        this.yy = yy;
        this.zz = zz;
    }

    public Expr getComponent(Axis a) {
        switch (a) {
            case X:
                return xx;
            case Y:
                return yy;
            case Z: {
                if (zz != null) {
                    return zz;
                }
            }
        }
        return Maths.CZEROXY;
    }

    public int getComponentSize() {
        return zz == null ? 2 : 3;
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

    public ComplexVector[][][] evalVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, z, d0, ranges),
                yy.evalComplex(x, y, z, d0, ranges),
                zz == null ? null : zz.evalComplex(x, y, z, d0, ranges)
        );
    }

    public ComplexVector[][] evalVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, d0, ranges)
                , yy.evalComplex(x, y, d0, ranges)
                , zz == null ? null : zz.evalComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector[] evalVector(double[] x, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, d0, ranges)
                , yy.evalComplex(x, d0, ranges)
                , zz == null ? null : zz.evalComplex(x, d0, ranges)
        );
    }

    public ComplexVector[] evalVector(double[] x, double y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, d0, ranges)
                , yy.evalComplex(x, y, d0, ranges)
                , zz == null ? null : zz.evalComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector[] evalVector(double x, double[] y, Domain d0, Out<Range> ranges) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, d0, ranges)
                , yy.evalComplex(x, y, d0, ranges)
                , zz == null ? null : zz.evalComplex(x, y, d0, ranges)
        );
    }

    public ComplexVector evalVector(double x) {
        return ArrayUtils.c2v(
                xx.evalComplex(x)
                , yy.evalComplex(x)
                , zz == null ? null : zz.evalComplex(x)
        );
    }

    public ComplexVector evalVector(double x, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, defined)
                , yy.evalComplex(x, defined)
                , zz == null ? null : zz.evalComplex(x, defined)
        );
    }

    public ComplexVector evalVector(double x, double y) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y)
                , yy.evalComplex(x, y)
                , zz == null ? null : zz.evalComplex(x, y)
        );
    }

    public ComplexVector evalVector(double x, double y, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, defined)
                , yy.evalComplex(x, y, defined)
                , zz == null ? null : zz.evalComplex(x, y, defined)
        );
    }

    public ComplexVector evalVector(double x, double y, double z) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, z)
                , yy.evalComplex(x, y, z)
                , zz == null ? null : zz.evalComplex(x, y, z)
        );
    }

    public ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, z, defined)
                , yy.evalComplex(x, y, z, defined)
                , zz == null ? null : zz.evalComplex(x, y, z, defined)
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

    public ComplexVector[][][] evalVector(double[] x, double[] y, double[] z) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y, z)
                , yy.evalComplex(x, y, z)
                , zz == null ? null : zz.evalComplex(x, y, z)
        );
    }

    public ComplexVector[][] evalVector(double[] x, double[] y) {
        return ArrayUtils.c2v(
                xx.evalComplex(x, y)
                , yy.evalComplex(x, y)
                , zz == null ? null : zz.evalComplex(x, y)
        );
    }

    public ComplexVector[] evalVector(double[] x) {
        return ArrayUtils.c2v(
                xx.evalComplex(x)
                , yy.evalComplex(x)
                , zz == null ? null : zz.evalComplex(x)
        );
    }
}
