/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.NoSuchElementException;

import static net.vpc.scholar.hadrumaths.ExpressionTransformFactory.getExpressionTransformer;

/**
 * @author vpc
 */
public class Expressions {

    public static <T extends Expr> T crossDomainXY(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.domainMul(value));
    }

    public static <T extends Expr> T transform(Expr e, ExpressionTransform transform) {
        return (T) getExpressionTransformer(e.getClass(), transform.getClass()).transform(e, transform);
    }

    public static <T extends Expr> T symmetric(Expr e, Axis axis, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(axis, value));
    }

    public static <T extends Expr> T xsymmetric(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.X, value));
    }

    public static <T extends Expr> T ysymmetric(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.Y, value));
    }

    public static <T extends Expr> T opposite(Expr e, Axis axis) {
        return transform(e, ExpressionTransformFactory.opposite(axis));
    }

    public static <T extends Expr> T translated(Expr e, double valueX, double valueY) {
        return transform(e, ExpressionTransformFactory.translate(valueX, valueY));
    }

    public static <T extends Expr> T xtranslated(Expr e, double valueX) {
        return transform(e, ExpressionTransformFactory.translate(valueX, 0));
    }

    public static <T extends Expr> T ytranslated(Expr e, double valueY) {
        return transform(e, ExpressionTransformFactory.translate(0, valueY));
    }

    public static boolean isSymmetric(Expr e, AxisXY axis) {
        switch (axis) {
            case X: {
                return xsymmetric(e).equals(e);
            }
            case Y: {
                return ysymmetric(e).equals(e);
            }
            case XY: {
                return xsymmetric(e).equals(e) && ysymmetric(e).equals(e);
            }
        }
        throw new IllegalArgumentException("Not supported");
    }

    public static <T extends Expr> T xsymmetric(Expr e) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.X, null));
    }

    public static <T extends Expr> T ysymmetric(Expr e) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.Y, null));
    }

    public static boolean isXSymmetric(Expr e) {
        try {
            return xsymmetric(e).equals(e);
        } catch (NoSuchElementException ex) {
            //no transformer
            //System.err.println(ex);
            return false;
        }
    }

    public static boolean isYSymmetric(Expr e) {
        try {
            return ysymmetric(e).equals(e);
        } catch (NoSuchElementException ex) {
            //no transformer
//            System.err.println(ex);
            return false;
        }
    }

    public static Complex evalComplex(DoubleToComplex f, double x, double y) {
        Out<Range> ranges = new Out<>();
        Complex[][] r = f.evalComplex(new double[]{x}, new double[]{y});
        return r[0][0];
    }

    public static Complex evalComplex(DoubleToComplex f, double x, double y, BooleanMarker defined) {
        Out<Range> ranges = new Out<>();
        Complex[][] r = f.evalComplex(new double[]{x}, new double[]{y}, null, ranges);
        ExpressionsDebug.debug_check(r, ranges);
        if (ranges.get().getDefined2().get(0, 0)) {
            defined.set();
        }
        return r[0][0];
    }

    public static Complex[] evalComplex(DoubleToComplex f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Complex[][] r = f.evalComplex(x, new double[]{y}, d0, ranges);
        ExpressionsDebug.debug_check(r, ranges);
        return r[0];
    }

    public static Complex[] evalComplex(DoubleToComplex f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Complex[][] r = f.evalComplex(new double[]{x}, y, d0, ranges);
        ExpressionsDebug.debug_check(r, ranges);
        Complex[] v = new Complex[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

    public static double evalDouble(DoubleToDouble f, double x, double y) {
        double[][] r = f.evalDouble(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

    public static double evalDouble(DoubleToDouble f, double x, double y, BooleanMarker defined) {
        Out<Range> ranges = new Out<>();
        double[][] r = f.evalDouble(new double[]{x}, new double[]{y}, null, ranges);
        if (ranges.get().getDefined2().get(0, 0)) {
            defined.set();
        }
        return r[0][0];
    }

    public static double[] evalDouble(DoubleToDouble f, double[] x, double y, Domain d0, Out<Range> ranges) {
        double[][] r = f.evalDouble(x, new double[]{y}, d0, ranges);
        return r[0];
    }

    public static double[] evalDouble(DoubleToDouble f, double x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = f.evalDouble(new double[]{x}, y, d0, ranges);
        double[] v = new double[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

    public static double[][][] evalDoubleFromXY(DoubleToDouble f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> rangesxy = new Out<Range>();
        double[][] r = f.evalDouble(x, y, d0, rangesxy);
        Range zrange = f.getDomain().intersect(d0).range(x, y, z);
        Range rangeOut = rangesxy.get();
        if (rangeOut != null) {
            double[][][] m = new double[z.length][][];
            for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                m[i] = ArrayUtils.copy(r);
            }
            if (ranges != null) {
                Range d = Range.ofBounds(rangeOut.xmin, rangeOut.xmax, rangeOut.ymin, rangeOut.ymax, 0, z.length - 1);
                BooleanArray2 defined2 = rangeOut.getDefined2();
                BooleanArray3 d3 = d.setDefined3(x.length, y.length, z.length);
                for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                    d3.set(i, defined2);
                }
                ranges.set(d);
            }
            return m;
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
            return new double[z.length][y.length][x.length];
        }
    }

    public static ComplexMatrix evalMatrix(DoubleToMatrix f, double x) {
        ComplexMatrix[] r = f.evalMatrix(new double[]{x});
        return r[0];
    }

    public static double evalDouble(DoubleToDouble f, double x) {
        double[] r = f.evalDouble(new double[]{x});
        return r[0];
    }

    public static double evalDouble(DoubleToDouble f, double x, BooleanMarker defined) {
        Out<Range> range = new Out<>();
        double[] r = f.evalDouble(new double[]{x}, null, range);
        if (range.get().getDefined1().get(0)) {
            defined.set();
        }
        return r[0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x) {
        ComplexVector[] r = f.evalVector(new double[]{x}, (Domain) null, null);
        return r[0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x, double y) {
        ComplexVector[][] r = f.evalVector(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x, double y, double z) {
        ComplexVector[][][] r = f.evalVector(new double[]{x}, new double[]{y}, new double[]{z}, null, null);
        return r[0][0][0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[] r = f.evalVector(new double[]{x}, null, o);
        if (defined != null && !o.get().isEmpty()) {
            defined.set();
        }
        return r[0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x, double y, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[][] r = f.evalVector(new double[]{x}, new double[]{y}, null, o);
        if (defined != null && !o.get().isEmpty()) {
            defined.set();
        }
        return r[0][0];
    }

    public static ComplexVector evalVector(DoubleToVector f, double x, double y, double z, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[][][] r = f.evalVector(new double[]{x}, new double[]{y}, new double[]{z}, null, o);
        if (defined != null && !o.get().isEmpty()) {
            defined.set();
        }
        return r[0][0][0];
    }

    public static ComplexMatrix evalMatrix(DoubleToMatrix f, double x, double y) {
        ComplexMatrix[][] r = f.evalMatrix(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

//    public static Complex computeComplexArg(DoubleToComplex f, double x, double y, double z) {
//        return computeComplexFromXY(f, new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }

    public static ComplexMatrix[] evalMatrix(DoubleToMatrix f, double[] x, double y, Domain d0, Out<Range> ranges) {
        ComplexMatrix[][] r = f.evalMatrix(x, new double[]{y}, d0, ranges);
        return r[0];
    }

    public static ComplexMatrix[] evalMatrix(DoubleToMatrix f, double x, double[] y, Domain d0, Out<Range> ranges) {
        ComplexMatrix[][] r = f.evalMatrix(new double[]{x}, y, d0, ranges);
        ComplexMatrix[] v = new ComplexMatrix[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

    public static DoubleToDouble[] getReal(DoubleToComplex[] fct) {
        DoubleToDouble[] f = new DoubleToDouble[fct.length];
        int maxi = fct.length;
        for (int i = 0; i < maxi; i++) {
            f[i] = fct[i].getRealDD();

        }
        return f;
    }

    public static DoubleToDouble[] getImag(DoubleToComplex[] fct) {
        DoubleToDouble[] f = new DoubleToDouble[fct.length];
        int maxi = fct.length;
        for (int i = 0; i < maxi; i++) {
            f[i] = fct[i].getImagDD();

        }
        return f;
    }

    public static DoubleToComplex[] toComplex(DoubleToDouble[] fct) {
        DoubleToComplex[] f = new DoubleToComplex[fct.length];
        int maxi = fct.length;
        for (int i = 0; i < maxi; i++) {
            f[i] = Maths.complex(fct[i]);
        }
        return f;
    }

    public static NumberExpr toConstantExprOrNull(Expr e) {
        if (e instanceof NumberExpr) {
            return (NumberExpr) e;
        }
        return null;
    }


    public static int widestComponentSize(Expr... expressions) {
        int cs = 1;
        for (Expr expression : expressions) {
            int s = expression.toDV().getComponentSize();
            if (s > cs) {
                cs = s;
            }
        }
        return cs;
    }

    public static void domainIntersectHelper(double a, double b, double c, double d, double[] minMax) {
        int _a = Double.compare(a, c);
        int _b = Double.compare(b, d);
        switch (_a) {
            case -1: {
                switch (_b) {
                    // [a b] <c d>
                    // [a <c b] d>
                    case -1: {
                        if (b < c) {
                            // [ ] < >
                            minMax[0] = 0;
                            minMax[1] = 0;
                            minMax[2] = 0;
                        } else {
                            // [ < ] >
                            minMax[0] = c;
                            minMax[1] = b;
                            minMax[2] = 0;
                        }
                        break;
                    }
                    // [a<c d>=b]
                    case 0: {
                        minMax[0] = c;
                        minMax[1] = d;
                        minMax[2] = 2;
                        break;
                    }
                    // [a <c  d> b]
                    case 1: {
                        minMax[0] = c;
                        minMax[1] = d;
                        minMax[2] = 2;
                        break;
                    }
                }
                break;
            }
            case 0: {
                switch (_b) {
                    // [=< ]  >
                    case -1: {
                        minMax[0] = c;
                        minMax[1] = b;
                        minMax[2] = 0;
                        break;
                    }
                    // [a=<c  d>=b]
                    case 0: {
                        minMax[0] = a;
                        minMax[1] = b;
                        minMax[2] = 1;
                        break;
                    }
                    // [a=<c  d> b]
                    case 1: {
                        minMax[0] = c;
                        minMax[1] = d;
                        minMax[2] = 2;
                        break;
                    }
                }
                break;
            }
            case 1: {
                switch (_b) {
                    // <c [a b] d>
                    case -1: {
                        minMax[0] = a;
                        minMax[1] = b;
                        minMax[2] = 1;
                        break;
                    }
                    // <c [a b]=d>
                    case 0: {
                        minMax[0] = a;
                        minMax[1] = b;
                        minMax[2] = 1;
                        break;
                    }
                    // < > [ ]
                    // < >=[ ]
                    case 1: {
                        // <c d>=[a b]
                        // <c d> [a b]
                        if (a >= d) {
                            minMax[0] = 0;
                            minMax[1] = 0;
                            minMax[2] = 0;
                        } else {
                            // <c [a d> b]
                            minMax[0] = a;
                            minMax[1] = d;
                            minMax[2] = 0;
                        }
                        break;
                    }
                }
                break;
            }
        }
//        if (a == Double.NEGATIVE_INFINITY && b == Double.POSITIVE_INFINITY) {
//            minMax[0] = c;
//            minMax[1] = d;
//            minMax[2] = 2;
//        } else if (c == Double.NEGATIVE_INFINITY && d == Double.POSITIVE_INFINITY) {
//            minMax[0] = a;
//            minMax[1] = b;
//            minMax[2] = 1;
//        } else {
//            double e = a > c ? a : c;
//            double f = b < d ? b : d;
//            minMax[2] = 0;
//            if (f < e) {
//                minMax[0] = 0.0D;
//                minMax[1] = 0.0D;
//            } else {
//                double delta = f - e;
//                if ((delta < 0.0D && -delta < Domain.epsilon) || (delta > 0.0D && delta < Domain.epsilon)) {
//                    minMax[0] = 0.0D;
//                    minMax[1] = 0.0D;
//                } else {
//                    minMax[0] = e;
//                    minMax[1] = f;
//                }
//            }
//        }
    }
}
