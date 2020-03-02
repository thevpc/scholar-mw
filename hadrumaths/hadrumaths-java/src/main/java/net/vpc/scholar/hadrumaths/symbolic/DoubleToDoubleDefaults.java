/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.List;

/**
 * @author vpc
 */
public final class DoubleToDoubleDefaults {
    private DoubleToDoubleDefaults() {

    }

    public static double[][][] evalDouble(DoubleToDouble base, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] r = new double[z.length][y.length][x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray3 d = currRange.setDefined3(x.length, y.length, z.length);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int i = currRange.zmin; i <= currRange.zmax; i++) {
                for (int j = cy; j <= dy; j++) {
                    for (int k = ax; k <= bx; k++) {
                        wasSet.set(false);
                        double v = base.evalDouble(x[k], y[j], z[i], wasSet);
                        if (wasSet.get()) {
                            r[i][j][k] = v;
                            d.set(i, j, k);
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[][][] evalDoubleSimple(DoubleToDoubleSimple base, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] r = new double[z.length][y.length][x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray3 d = currRange.setDefined3(x.length, y.length, z.length);
            for (int i = currRange.zmin; i <= currRange.zmax; i++) {
                for (int j = cy; j <= dy; j++) {
                    for (int k = ax; k <= bx; k++) {
                        r[i][j][k] = base.evalDoubleSimple(x[k], y[j], z[i]);
                    }
                    d.setAll(i, j, ax, bx + 1, true);
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[][] evalDouble(DoubleToDouble base, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray2 d = BooleanArrays.newArray(y.length, x.length);
            currRange.setDefined(d);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int k = ax; k <= bx; k++) {
                for (int j = cy; j <= dy; j++) {
                    wasSet.set(false);
                    double v = base.evalDouble(x[k], y[j], wasSet);
                    if (wasSet.get()) {
                        r[j][k] = v;
                        d.set(j, k);
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[][] evalDoubleSimple(DoubleToDoubleSimple base, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray2 d = BooleanArrays.newArray(y.length, x.length);
            currRange.setDefined(d);
            for (int j = cy; j <= dy; j++) {
                for (int k = ax; k <= bx; k++) {
                    r[j][k] = base.evalDoubleSimple(x[k], y[j]);
                }
                d.setRange(j, ax, bx + 1, true);
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDouble(DoubleToDouble base, double[] x, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                wasSet.set(false);
                double v = base.evalDouble(x[xIndex], wasSet);
                if (wasSet.get()) {
                    d.set(xIndex);
                    r[xIndex] = v;
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDoubleSimple(DoubleToDoubleSimple base, double[] x, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                r[xIndex] = base.evalDoubleSimple(x[xIndex]);
            }
            d.setRange(ax, bx + 1);
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDouble(DoubleToDouble base, double[] x, double y, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                wasSet.set(false);
                double v = base.evalDouble(x[xIndex], y, wasSet);
                if (wasSet.get()) {
                    d.set(xIndex);
                    r[xIndex] = v;
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDoubleSimple(DoubleToDoubleSimple base, double[] x, double y, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                r[xIndex] = base.evalDoubleSimple(x[xIndex], y);
            }
            d.setRange(ax, bx + 1);
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDouble(DoubleToDouble base, double x, double[] y, Domain d0, Out<Range> ranges) {
        double[] r = new double[y.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(y.length);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int yIndex = ax; yIndex <= bx; yIndex++) {
                wasSet.set(false);
                double v = base.evalDouble(x, y[yIndex], wasSet);
                if (wasSet.get()) {
                    d.set(yIndex);
                    r[yIndex] = v;
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDoubleSimple(DoubleToDoubleSimple base, double x, double[] y, Domain d0, Out<Range> ranges) {
        double[] r = new double[y.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(y.length);
            for (int yIndex = ax; yIndex <= bx; yIndex++) {
                r[yIndex] = base.evalDoubleSimple(x, y[yIndex]);
            }
            d.setRange(ax, bx + 1);
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDouble(DoubleToDouble base, double[] x, double y, double z, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            BooleanRef wasSet = BooleanMarker.ref();
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                wasSet.set(false);
                double v = base.evalDouble(x[xIndex], y, z, wasSet);
                if (wasSet.get()) {
                    d.set(xIndex);
                    r[xIndex] = v;
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public static double[] evalDoubleSimple(DoubleToDoubleSimple base, double[] x, double y, double z, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = base.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                r[xIndex] = base.evalDoubleSimple(x[xIndex], y, z);
            }
            d.setRange(ax, bx + 1);
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
        }
        return r;
    }

    public interface DoubleToDoubleUnaryDD extends DoubleToDoubleSimple1, DoubleToDoubleNormal {

        @Override
        default double evalDouble(double x, double y, double z, BooleanMarker defined) {
            if (contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = operands.get(0).toDD().evalDouble(x, y, z, defined2);
                if (defined2.get()) {
                    defined.set();
                    return evalDoubleSimple(a);
                }
            }
            return 0;
        }

        @Override
        default double evalDouble(double x, BooleanMarker defined) {
            if (contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = operands.get(0).toDD().evalDouble(x, defined2);
                if (!defined2.get()) {
                    return 0;
                }
                defined.set();
                return evalDoubleSimple(a);
            }
            return 0;
        }

        @Override
        default double evalDouble(double x, double y, BooleanMarker defined) {
            if (contains(x, y)) {
//                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = this.getChild(0).toDD().evalDouble(x, y, defined2);
                if (!defined2.get()) {
                    return 0;
                }
                defined.set();
                return evalDoubleSimple(a);
            }
            return 0;
        }
    }

    public interface DoubleToDoubleSimple3 extends DoubleToDouble {
        double evalDoubleSimple(double x, double y, double z);
    }

    public interface DoubleToDoubleSimple2 extends DoubleToDouble {
        double evalDoubleSimple(double x, double y);
    }

    public interface DoubleToDoubleSimple1 extends DoubleToDouble {
        double evalDoubleSimple(double x);
    }

    public interface DoubleToDoubleNormal extends DoubleToDouble {
        default double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDouble(this, x, y, d0, ranges);
        }

        default double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
            return DoubleToDoubleDefaults.evalDouble(this, x, d0, range);
        }

        default double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDouble(this, x, y, d0, ranges);
        }

        default double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDouble(this, x, y, z, d0, ranges);
        }

        default double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDouble(this, x, y, d0, ranges);
        }
    }

    public interface DoubleToDoubleSimple extends DoubleToDoubleSimple1, DoubleToDoubleSimple2, DoubleToDoubleSimple3 {
        default double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDoubleSimple(this, x, y, d0, ranges);
        }

        default double evalDouble(double x, double y, double z, BooleanMarker defined) {
            if ((contains(x, y, z))) {
                defined.set();
                return this.evalDoubleSimple(x, y, z);
            }
            return 0;
        }

        default double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
            return DoubleToDoubleDefaults.evalDoubleSimple(this, x, d0, range);
        }

        default double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDoubleSimple(this, x, y, d0, ranges);
        }

        default double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDoubleSimple(this, x, y, z, d0, ranges);
        }

        default double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToDoubleDefaults.evalDoubleSimple(this, x, y, d0, ranges);
        }

        default double evalDouble(double x, BooleanMarker defined) {
            if (contains(x)) {
                defined.set();
                return this.evalDoubleSimple(x);
            }
            return 0;
        }

        default double evalDouble(double x, double y, BooleanMarker defined) {//
            if (contains(x, y)) {
                defined.set();
                return this.evalDoubleSimple(x, y);
            }
            return 0;
        }
    }

    public interface DoubleToDoubleBinaryDDDD extends DoubleToDoubleSimple2, DoubleToDoubleNormal {

        @Override
        default double evalDouble(double x, double y, double z, BooleanMarker defined) {
            if (contains(x, y, z)) {
                DoubleToDouble c1 = this.getChild(0).toDD();
                DoubleToDouble c2 = this.getChild(1).toDD();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = c1.toDD().evalDouble(x, y, z, defined2);
                if (defined2.get()) {
                    double b = c2.toDD().evalDouble(x, y, z, defined2);
                    if (defined2.get()) {
                        defined.set();
                        return evalDoubleSimple(a, b);
                    }
                }

            }
            return 0;
        }

        @Override
        default double evalDouble(double x, BooleanMarker defined) {
            if (contains(x)) {
                DoubleToDouble c1 = this.getChild(0).toDD();
                DoubleToDouble c2 = this.getChild(1).toDD();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = c1.evalDouble(x, defined2);
                if (defined2.get()) {
                    double b = c2.evalDouble(x, defined2);
                    if (defined2.get()) {
                        defined.set();
                        return evalDoubleSimple(a, b);
                    }
                }
            }
            return 0;
        }

        @Override
        default double evalDouble(double x, double y, BooleanMarker defined) {
            if (contains(x, y)) {
                DoubleToDouble c1 = this.getChild(0).toDD();
                DoubleToDouble c2 = this.getChild(1).toDD();
                BooleanRef defined2 = BooleanMarker.ref();
                double a = c1.evalDouble(x, y, defined2);
                if (defined2.get()) {
                    double b = c2.evalDouble(x, y, defined2);
                    if (defined2.get()) {
                        defined.set();
                        return evalDoubleSimple(a, b);
                    }
                }

            }
            return 0;
        }
    }

}
