package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.Range;

public abstract class DoubleToDoubleOneToManyHelper {
    private DoubleToDouble baseExpr;

    public DoubleToDoubleOneToManyHelper(DoubleToDouble baseExpr) {
        this.baseExpr = baseExpr;
    }
    public double[] computeDouble(double x, double[] y, Domain d0) {
        double[] r = new double[y.length];
        Domain domain = baseExpr.getDomain();
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(new double[]{x}, y);
        if (abcd != null) {
            int cy = abcd.ymin;
            int dy = abcd.ymax;
            for (int yIndex = cy; yIndex <= dy; yIndex++) {
                //computeDouble(x, y, NoneOutBoolean.INSTANCE)
                r[yIndex] = computeDouble(x, y[yIndex],NoneOutBoolean.INSTANCE);
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, double y, Domain d0) {
        double[] r = new double[x.length];
        Domain domain = baseExpr.getDomain();
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, new double[]{y});
        if (abcd != null) {
            int cy = abcd.ymin;
            int dy = abcd.ymax;
            for (int xIndex = cy; xIndex <= dy; xIndex++) {
                r[xIndex] = computeDouble(x[xIndex], y,NoneOutBoolean.INSTANCE);
            }
        }
        return r;
    }
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] r = new double[z.length][y.length][x.length];
        Domain domain = baseExpr.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            int ez = currRange.zmin;
            int fz = currRange.zmax;
            BooleanArray3 d = currRange.setDefined3(x.length, y.length, z.length);
            BooleanRef defined = BooleanMarker.ref();
            for (int i = ez; i <= fz; i++) {
                for (int j = cy; j <= dy; j++) {
                    for (int k = ax; k <= bx; k++) {
                        if (baseExpr.contains(x[k], y[j], z[i])) {
                            defined.reset();
                            double v = computeDouble0(x[k], y[j], z[i], defined);
                            r[i][j][k] = v;
                            if (defined.get()) {
                                d.set(i, j, k);
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        Domain domain = baseExpr.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray2 d = currRange.setDefined2(x.length, y.length);
            BooleanRef defined = BooleanMarker.ref();
            for (int k = ax; k <= bx; k++) {
                for (int j = cy; j <= dy; j++) {
                    if (baseExpr.contains(x[k], y[j])) {
                        defined.reset();
                        double v = computeDouble0(x[k], y[j], defined);
                        r[j][k] = v;
                        if (defined.get()) {
                            d.set(j, k);
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Domain domain = baseExpr.getDomain();
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            BooleanRef defined = BooleanMarker.ref();
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                if (baseExpr.contains(x[xIndex])) {
                    defined.reset();
                    r[xIndex] = computeDouble0(x[xIndex], defined);
                    if (defined.get()) {
                        d.set(xIndex);
                    }
                } else {
                    d.clear(xIndex);
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    //    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] d = computeDouble(x, y, z, d0, ranges);
        Complex[][][] m = new Complex[d.length][d[0].length][d[0][0].length];
        for (int zi = 0; zi < m.length; zi++) {
            for (int yi = 0; yi < m[zi].length; zi++) {
                for (int xi = 0; xi < m[zi][yi].length; xi++) {
                    m[zi][yi][xi] = Complex.valueOf(d[zi][yi][xi]);
                }
            }
        }
        return m;
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] d = computeDouble(x, y, z, d0, ranges);
        ComplexMatrix[][][] m = new ComplexMatrix[d.length][d[0].length][d[0][0].length];
        for (int zi = 0; zi < m.length; zi++) {
            for (int yi = 0; yi < m[zi].length; zi++) {
                for (int xi = 0; xi < m[zi][yi].length; xi++) {
                    m[zi][yi][xi] = MathsBase.constantMatrix(1, Complex.valueOf(d[zi][yi][xi]));
                }
            }
        }
        return m;
    }

//    @Override
//    public final double computeDouble(double x) {
//        return computeDouble(x, new BooleanMarker());
//    }

    public final double computeDouble(double x, BooleanMarker defined) {
        if (baseExpr.contains(x)) {
            return computeDouble0(x, defined);
        }
        return 0;
//        switch (getDomainDimension()) {
//            case 1: {
//                if (contains(x)) {
//                    return computeDouble0(x, defined);
//                }
//                return 0;
//            }
//        }
//        throw new IllegalArgumentException("Missing y");
    }

//    @Override
//    public final double computeDouble(double x, double y, double z) {
//        return computeDouble(x, y, z, new BooleanMarker());
//    }

    public final double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (baseExpr.contains(x, y, z)) {
            return computeDouble0(x, y, z, defined);
        }
//        switch (getDomainDimension()) {
//            case 1: {
//                if (contains(x)) {
//                    return computeDouble0(x, defined);
//                }
//                return 0;
//            }
//            case 2: {
//                if (contains(x, y)) {
//                    return computeDouble0(x, y, defined);
//                }
//                return 0;
//            }
//            case 3: {
//                if (contains(x, y, z)) {
//                    return computeDouble0(x, y, z, defined);
//                }
//                return 0;
//            }
//        }
//        throw new IllegalArgumentException("Invalid domain " + getDomainDimension());
        return 0;
    }

//    @Override
//    public double computeDouble(double x, double y) {
//        return computeDouble(x,y,new BooleanMarker());
//    }

    public double computeDouble(double x, double y, BooleanMarker defined) {
        if (baseExpr.contains(x, y)) {
            return computeDouble0(x, y, defined);
        }
        return 0;
//        switch (getDomainDimension()) {
//            case 1: {
//                if (contains(x)) {
//                    return computeDouble0(x, defined);
//                }
//                return 0;
//            }
//            case 2: {
//                if (contains(x, y)) {
//                    return computeDouble0(x, y, defined);
//                }
//                return 0;
//            }
//        }
//        if (contains(x, y)) {
//            return computeDouble0(x, y, defined);
//        }
//        return 0;
    }

    public double[] computeDouble(double[] x) {
        return computeDouble(x, (Domain) null, null);
    }

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(baseExpr, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(baseExpr, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }

    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
        return computeDouble(x, y, z, (Domain) null, null);
    }

    public double[][] computeDouble(double[] x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }


    protected abstract double computeDouble0(double x, BooleanMarker defined);

    protected abstract double computeDouble0(double x, double y, BooleanMarker defined);

    protected abstract double computeDouble0(double x, double y, double z, BooleanMarker defined);
}
