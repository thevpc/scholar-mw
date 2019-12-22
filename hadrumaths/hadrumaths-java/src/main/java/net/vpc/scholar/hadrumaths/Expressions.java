/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.HashSet;
import java.util.NoSuchElementException;

import static net.vpc.scholar.hadrumaths.ExpressionTransformFactory.getExpressionTransformer;

/**
 * @author vpc
 */
public class Expressions {


//    public static <T extends Expr> T mul(Expr e, double value) {
//        return eval(e, ExpressionTransformFactory.doubleMul(value));
//    }

    public static <T extends Expr> T crossDomainXY(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.domainMul(value));
    }

    public static <T extends Expr> T symmetric(Expr e, Axis axis, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(axis, value));
    }

    public static <T extends Expr> T xsymmetric(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.X, value));
    }

    public static <T extends Expr> T xsymmetric(Expr e) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.X, null));
    }

    public static <T extends Expr> T ysymmetric(Expr e, Domain value) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.Y, value));
    }

    public static <T extends Expr> T ysymmetric(Expr e) {
        return transform(e, ExpressionTransformFactory.symmetric(Axis.Y, null));
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

    public static <T extends Expr> T transform(Expr e, ExpressionTransform transform) {
        return (T) getExpressionTransformer(e.getClass(), transform.getClass()).transform(e, transform);
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

    public static Complex computeComplex(DoubleToComplex f, double x, double y) {
        Out<Range> ranges = new Out<>();
        Complex[][] r = f.computeComplex(new double[]{x}, new double[]{y});
        return r[0][0];
    }

    public static Complex computeComplex(DoubleToComplex f, double x, double y, BooleanMarker defined) {
        Out<Range> ranges = new Out<>();
        Complex[][] r = f.computeComplex(new double[]{x}, new double[]{y}, null, ranges);
        ExpressionsDebug.debug_check(r,ranges);
        if (ranges.get().getDefined2().get(0, 0)) {
            defined.set();
        }
        return r[0][0];
    }

    public static Complex[] computeComplex(DoubleToComplex f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Complex[][] r = f.computeComplex(x, new double[]{y}, d0, ranges);
        ExpressionsDebug.debug_check(r,ranges);
        return r[0];
    }

    public static Complex[] computeComplex(DoubleToComplex f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Complex[][] r = f.computeComplex(new double[]{x}, y, d0, ranges);
        ExpressionsDebug.debug_check(r,ranges);
        Complex[] v = new Complex[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

    public static double computeDouble(DoubleToDouble f, double x, double y) {
        double[][] r = f.computeDouble(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

    public static double computeDouble(DoubleToDouble f, double x, double y, BooleanMarker defined) {
        Out<Range> ranges = new Out<>();
        double[][] r = f.computeDouble(new double[]{x}, new double[]{y}, null, ranges);
        if (ranges.get().getDefined2().get(0, 0)) {
            defined.set();
        }
        return r[0][0];
    }

    public static double[] computeDouble(DoubleToDouble f, double[] x, double y, Domain d0, Out<Range> ranges) {
        double[][] r = f.computeDouble(x, new double[]{y}, d0, ranges);
        return r[0];
    }

    public static double[] computeDouble(DoubleToDouble f, double x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = f.computeDouble(new double[]{x}, y, d0, ranges);
        double[] v = new double[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

//    public static Complex computeComplexArg(DoubleToComplex f, double x, double y, double z) {
//        return computeComplexFromXY(f, new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }


    public static Complex[][][] computeComplexFromXY(DoubleToComplex f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> rangesxy = new Out<Range>();
        Complex[][] r = f.computeComplex(x, y, d0, rangesxy);
        ExpressionsDebug.debug_check(r,rangesxy);
        Range zrange = f.getDomain().intersect(d0).getDomainZ().range(z);
        Range rangeOut = rangesxy.get();
        if (rangeOut != null) {
            Complex[][][] m = new Complex[z.length][][];
            if (zrange.xmin > 0) {
                for (int i = 0; i < zrange.xmin; i++) {
                    m[i] = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
                }
            }
            for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                m[i] = ArrayUtils.copy(r);
            }
            if (zrange.xmax < z.length - 1) {
                for (int i = zrange.xmax + 1; i < z.length; i++) {
                    m[i] = ArrayUtils.fill(new Complex[y.length][x.length], Complex.ZERO);
                }
            }
            if (ranges != null) {
                Range d = Range.forBounds(rangeOut.xmin, rangeOut.xmax, rangeOut.ymin, rangeOut.ymax, 0, z.length - 1);
                BooleanArray2 defined2 = rangeOut.getDefined2();
                BooleanArray3 d3 = d.setDefined3(x.length, y.length, z.length);
                for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                    d3.set(i, defined2);
                }
                ranges.set(d);
            }
            return m;
        } else {
            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
        }
    }

    public static double[][][] computeDoubleFromXY(DoubleToDouble f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> rangesxy = new Out<Range>();
        double[][] r = f.computeDouble(x, y, d0, rangesxy);
        Range zrange = f.getDomain().intersect(d0).getDomainZ().range(z);
        Range rangeOut = rangesxy.get();
        if (rangeOut != null) {
            double[][][] m = new double[z.length][][];
            for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                m[i] = ArrayUtils.copy(r);
            }
            if (ranges != null) {
                Range d = Range.forBounds(rangeOut.xmin, rangeOut.xmax, rangeOut.ymin, rangeOut.ymax, 0, z.length - 1);
                BooleanArray2 defined2 = rangeOut.getDefined2();
                BooleanArray3 d3 = d.setDefined3(x.length, y.length, z.length);
                for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                    d3.set(i, defined2);
                }
                ranges.set(d);
            }
            return m;
        } else {
            return new double[z.length][y.length][x.length];
        }
    }

    public static ComplexMatrix computeMatrix(DoubleToMatrix f, double x) {
        ComplexMatrix[] r = f.computeMatrix(new double[]{x});
        return r[0];
    }

    public static double computeDouble(DoubleToDouble f, double x) {
        double[] r = f.computeDouble(new double[]{x});
        return r[0];
    }

    public static double computeDouble(DoubleToDouble f, double x, BooleanMarker defined) {
        Out<Range> range = new Out<>();
        double[] r = f.computeDouble(new double[]{x}, null, range);
        if (range.get().getDefined1().get(0)) {
            defined.set();
        }
        return r[0];
    }

//    public static double computeDouble(IDDx f, double x) {
//        double[] r = f.computeDouble(new double[]{x}, null, null);
//        return r[0];
//    }

    public static ComplexVector computeVector(DoubleToVector f, double x) {
        ComplexVector[] r = f.computeVector(new double[]{x},(Domain) null, null);
        return r[0];
    }

    public static ComplexVector computeVector(DoubleToVector f, double x, double y) {
        ComplexVector[][] r = f.computeVector(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

    public static ComplexVector computeVector(DoubleToVector f, double x, double y, double z) {
        ComplexVector[][][] r = f.computeVector(new double[]{x}, new double[]{y},new double[]{z}, null, null);
        return r[0][0][0];
    }

    public static ComplexVector computeVector(DoubleToVector f, double x, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[] r = f.computeVector(new double[]{x},(Domain) null, o);
        if(defined!=null && !o.get().isEmpty()){
            defined.set();
        }
        return r[0];
    }

    public static ComplexVector computeVector(DoubleToVector f, double x, double y, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[][] r = f.computeVector(new double[]{x}, new double[]{y}, null, o);
        if(defined!=null && !o.get().isEmpty()){
            defined.set();
        }
        return r[0][0];
    }

    public static ComplexVector computeVector(DoubleToVector f, double x, double y, double z, BooleanMarker defined) {
        Out<Range> o = new Out<>();
        ComplexVector[][][] r = f.computeVector(new double[]{x}, new double[]{y},new double[]{z}, null, o);
        if(defined!=null && !o.get().isEmpty()){
            defined.set();
        }
        return r[0][0][0];
    }

    public static ComplexMatrix computeMatrix(DoubleToMatrix f, double x, double y) {
        ComplexMatrix[][] r = f.computeMatrix(new double[]{x}, new double[]{y}, null, null);
        return r[0][0];
    }

    public static ComplexMatrix[][] computeMatrixFromComplex(DoubleToComplex f, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Complex[][] c = f.computeComplex(x, y, d0, ranges);
        ExpressionsDebug.debug_check(c,ranges);
        ComplexMatrix[][] t = new ComplexMatrix[c.length][c.length == 0 ? 0 : c[0].length];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                t[i][j] = MathsBase.constantMatrix(1, c[i][j]);
            }
        }
        return t;
    }

    public static ComplexMatrix[] computeMatrix(DoubleToMatrix f, double[] x, double y, Domain d0, Out<Range> ranges) {
        ComplexMatrix[][] r = f.computeMatrix(x, new double[]{y}, d0, ranges);
        return r[0];
    }

    public static ComplexMatrix[] computeMatrix(DoubleToMatrix f, double x, double[] y, Domain d0, Out<Range> ranges) {
        ComplexMatrix[][] r = f.computeMatrix(new double[]{x}, y, d0, ranges);
        ComplexMatrix[] v = new ComplexMatrix[y.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = r[i][0];
        }
        return v;
    }

    //    public DD2DC getSymmetricX() {
    //        return new DD2DC(real.getSymmetricX(), imag.getSymmetricX());
    //    }
    //
    //    public DD2DC getSymmetricY() {
    //        return new DD2DC(real.getSymmetricY(), imag.getSymmetricY());
    //    }
    //
    //    public DD2DC translate(double deltaX, double deltaY) {
    //        return new DD2DC(real.translate(deltaX, deltaY), imag.translate(deltaX, deltaY));
    //    }
    public static DoubleToDouble[] getReal(DoubleToComplex[] fct) {
        DoubleToDouble[] f = new DoubleToDouble[fct.length];
        int maxi = fct.length;
        for (int i = 0; i < maxi; i++) {
            f[i] = fct[i].getRealDD();

        }
        return f;
    }

    //    public DD2DC multiply(double factor, DomainXY newDomain) {
    //        return new DD2DC(real.multiply(factor, newDomain), imag.multiply(factor, newDomain));
    //    }
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
            f[i] = MathsBase.complex(fct[i]);
        }
        return f;
    }

//    public static IDoubleToComplex[] toComplex(IDDx[] fct) {
//        DD2DC[] f = new DD2DC[fct.length];
//        int maxi = fct.length;
//        for (int i = 0; i < maxi; i++) {
//            f[i] = new DD2DC(fct[i]);
//        }
//        return f;
//    }


    public static String getMatrixExpressionTitleByChildren(Expr e, int row, int col) {
        HashSet<String> titles = new HashSet<String>();
        for (Expr expression : e.getSubExpressions()) {
            if (expression.isDC()) {
                //ignore
            } else if (expression.isDM()) {
                titles.add(expression.toDM().getComponentTitle(row, col));
            }
        }
        switch (titles.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return titles.toArray(new String[0])[0];
            }
            default: {
                return titles.toArray(new String[0])[0];
            }
        }
    }

    public static IConstantValue toComplexValue(Expr e) {
        if (e instanceof IConstantValue) {
            return ((IConstantValue) e);
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

//    public static interface GenExprHelper<T extends Expr>{
//        double computeDouble(double x,RefBoo def);
//        Complex computeComplexArg(Complex x,RefBoo def);
//        Matrix computeMatrix(Matrix x,RefBoo def);
//    }
//    public static class RefBoo{
//        boolean def;
//    }
//
//    public static double[] computeDouble(Expr base, GenExprHelper h, double[] x, Domain d0, Out<Range> range) {
//        Domain domainX = base.getDomain();
//        Range r = Domain.range(domainX, d0, x);
//        if (r != null) {
//            boolean[] def0 = new boolean[x.length];
//            r.setDefined(def0);
//            Out<Range> r2 = new Out<Range>();
//            double[] ret = h.getBaseExpr(base).toDD().computeDouble(x, d0, r2);
//            Range d2 = r2.get();
//            if (d2 != null) {
//                boolean[] def2 = d2.getDefined1();
//                for (int k = d2.xmin; k <= d2.xmax; k++) {
//                    if(def2[k]) {
//                        ret[k] = h.computeDouble(ret[k]);
//                        def0[k]=true;
//                    }else{
//                        ret[k]=0;
//                    }
//                }
//            }
//            if (range != null) {
//                range.set(r);
//            }
//            return ret;
//        } else {
//            double[] ret = new double[x.length];
//            if (range != null) {
//                range.set(null);
//            }
//            return ret;
//        }
//    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public interface UnaryExprHelper<T extends Expr> {
        Expr getBaseExpr(T expr);

        double computeDouble(double x, BooleanMarker defined);

        Complex computeComplex(Complex x, BooleanMarker defined);

        ComplexMatrix computeMatrix(ComplexMatrix x);

        ComplexVector computeVector(ComplexVector x);
    }

    public static double[] computeDouble(Expr base, UnaryExprHelper h, double[] x, Domain d0, Out<Range> range) {
        Domain domainX = base.getDomain();
        Range r = Domain.range(domainX, d0, x);
        if (r != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r.setDefined(def0);
            Out<Range> r2 = new Out<Range>();
            double[] ret = h.getBaseExpr(base).toDD().computeDouble(x, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray1 def2 = d2.getDefined1();
                for (int k = d2.xmin; k <= d2.xmax; k++) {
                    if (def2.get(k)) {
                        BooleanRef defined = BooleanMarker.ref();
                        ret[k] = h.computeDouble(ret[k], defined);
                        if (defined.get()) {
                            def0.set(k);
                        }
                    } else {
                        ret[k] = 0;
                    }
                }
            }
            if (range != null) {
                range.set(r);
            }
            return ret;
        } else {
            double[] ret = new double[x.length];
            if (range != null) {
                range.set(null);
            }
            return ret;
        }
    }

    public static double[][] computeDouble(Expr base, UnaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r.setDefined(def0);
            Out<Range> r2 = new Out<Range>();
            double[][] ret = h.getBaseExpr(base).toDD().computeDouble(x, y, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray2 def2 = d2.getDefined2();
                for (int j = d2.ymin; j <= d2.ymax; j++) {
                    for (int k = d2.xmin; k <= d2.xmax; k++) {
                        if (def2.get(j, k)) {
                            BooleanRef defined = BooleanMarker.ref();
                            ret[j][k] = h.computeDouble(ret[j][k], defined);
                            if (defined.get()) {
                                def0.set(j, k);
                            }
                        } else {
                            ret[j][k] = 0;
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            double[][] ret = new double[y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static double[][][] computeDouble(Expr base, UnaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);
            Out<Range> r2 = new Out<Range>();
            double[][][] ret = h.getBaseExpr(base).toDD().computeDouble(x, y, z, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray3 def2 = d2.getDefined3();
                for (int t = d2.zmin; t <= d2.zmax; t++) {
                    for (int j = d2.ymin; j <= d2.ymax; j++) {
                        for (int k = d2.xmin; k <= d2.xmax; k++) {
                            if (def2 != null && def2.get(t, j, k)) {
                                BooleanRef defined = BooleanMarker.ref();
                                ret[t][j][k] = h.computeDouble(ret[t][j][k], defined);
                                if (defined.get()) {
                                    def0.set(t, j, k);
                                }
                            } else {
                                ret[t][j][k] = 0;
                            }
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            double[][][] ret = new double[z.length][y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static ComplexMatrix[] computeMatrix(Expr base, UnaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[] ret = h.getBaseExpr(base).toDM().computeMatrix(x, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray1 def2 = d2.getDefined1();
                ComponentDimension d = base.getComponentDimension();
                ComplexMatrix z = MathsBase.zerosMatrix(d.rows, d.columns);
                for (int k = d2.xmin; k <= d2.xmax; k++) {
                    if (def2 != null && def2.get(k)) {
                        ret[k] = h.computeMatrix(ret[k]);
                    } else {
                        ret[k] = z;
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexMatrix z = MathsBase.zerosMatrix(d.rows, d.columns);
            ComplexMatrix[] ret = ArrayUtils.fillArray1Matrix(x.length, z);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[] computeVector(Expr base, UnaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            ComplexVector[] ret = h.getBaseExpr(base).toDV().computeVector(x, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray1 def2 = d2.getDefined1();
                ComponentDimension d = base.getComponentDimension();
                ComplexVector z = MathsBase.zerosColumnVector(d.rows);
                for (int k = d2.xmin; k <= d2.xmax; k++) {
                    if (def2 != null && def2.get(k)) {
                        ret[k] = h.computeVector(ret[k]);
                    } else {
                        ret[k] = z;
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexVector z = MathsBase.zerosColumnVector(d.rows);
            ComplexVector[] ret = ArrayUtils.fillArray1Vector(x.length, z);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexMatrix[][] computeMatrix(Expr base, UnaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r.setDefined(def0);
            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[][] ret = h.getBaseExpr(base).toDM().computeMatrix(x, y, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                ComponentDimension d = base.getComponentDimension();
                ComplexMatrix z = MathsBase.zerosMatrix(d.rows, d.columns);
                BooleanArray2 def2 = d2.getDefined2();
                for (int j = d2.ymin; j <= d2.ymax; j++) {
                    for (int k = d2.xmin; k <= d2.xmax; k++) {
                        if (def2 != null && def2.get(j, k)) {
                            ret[j][k] = h.computeMatrix(ret[j][k]);
                            def0.set(j, k);
                        } else {
                            ret[j][k] = z;
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexMatrix z = MathsBase.zerosMatrix(d.rows, d.columns);
            ComplexMatrix[][] ret = ArrayUtils.fillArray2Matrix(x.length, y.length, z);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }
    public static ComplexVector[][] computeVector(Expr base, UnaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r.setDefined(def0);
            Out<Range> r2 = new Out<Range>();
            ComplexVector[][] ret = h.getBaseExpr(base).toDV().computeVector(x, y, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                ComponentDimension d = base.getComponentDimension();
                ComplexVector z = MathsBase.zerosColumnVector(d.rows);
                BooleanArray2 def2 = d2.getDefined2();
                for (int j = d2.ymin; j <= d2.ymax; j++) {
                    for (int k = d2.xmin; k <= d2.xmax; k++) {
                        if (def2 != null && def2.get(j, k)) {
                            ret[j][k] = h.computeVector(ret[j][k]);
                            def0.set(j, k);
                        } else {
                            ret[j][k] = z;
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexVector z = MathsBase.zerosColumnVector(d.rows);
            ComplexVector[][] ret = ArrayUtils.fillArray2Vector(x.length, y.length, z);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexMatrix[][][] computeMatrix(Expr base, UnaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[][][] ret = h.getBaseExpr(base).toDM().computeMatrix(x, y, z, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                ComponentDimension d = base.getComponentDimension();
                ComplexMatrix zeros = MathsBase.zerosMatrix(d.rows, d.columns);
                BooleanArray3 def2 = d2.getDefined3();
                for (int t = d2.zmin; t <= d2.zmax; t++) {
                    for (int j = d2.ymin; j <= d2.ymax; j++) {
                        for (int k = d2.xmin; k <= d2.xmax; k++) {
                            if (def2 != null && def2.get(t, j, k)) {
                                ret[t][j][k] = h.computeMatrix(ret[t][j][k]);
                                def0.set(t, j, k);
                            } else {
                                ret[t][j][k] = zeros;
                            }
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexMatrix zeros = MathsBase.zerosMatrix(d.rows, d.columns);
            ComplexMatrix[][][] ret = ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, zeros);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[][][] computeVector(Expr base, UnaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            ComplexVector[][][] ret = h.getBaseExpr(base).toDV().computeVector(x, y, z, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                ComponentDimension d = base.getComponentDimension();
                ComplexVector zeros = MathsBase.zerosColumnVector(d.rows);
                BooleanArray3 def2 = d2.getDefined3();
                for (int t = d2.zmin; t <= d2.zmax; t++) {
                    for (int j = d2.ymin; j <= d2.ymax; j++) {
                        for (int k = d2.xmin; k <= d2.xmax; k++) {
                            if (def2 != null && def2.get(t, j, k)) {
                                ret[t][j][k] = h.computeVector(ret[t][j][k]);
                                def0.set(t, j, k);
                            } else {
                                ret[t][j][k] = zeros;
                            }
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            ComponentDimension d = base.getComponentDimension();
            ComplexVector zeros = MathsBase.zerosColumnVector(d.rows);
            ComplexVector[][][] ret = ArrayUtils.fillArray3Vector(x.length, y.length, z.length, zeros);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[] computeComplex(Expr base, UnaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            Complex[] ret = h.getBaseExpr(base).toDC().computeComplex(x, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray1 def2 = d2.getDefined1();
                for (int k = d2.xmin; k <= d2.xmax; k++) {
                    if (def2 != null && def2.get(k)) {
                        BooleanRef defined = BooleanMarker.ref();
                        ret[k] = h.computeComplex(ret[k], defined);
                        if (defined.get()) {
                            def0.set(k);
                        }
                    } else {
                        ret[k] = Complex.ZERO;
                    }
                }
                BooleanArray1 def1 = d2.getDefined1();
                def0.copyFrom(def1, r);
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[][] computeComplex(Expr base, UnaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            BooleanArray2 def0 = r.setDefined2(x.length, y.length);
            Out<Range> r2 = new Out<Range>();
            Complex[][] ret = h.getBaseExpr(base).toDC().computeComplex(x, y, d0, r2);
            ExpressionsDebug.debug_check(ret,r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray2 def2 = d2.getDefined2();
                for (int j = d2.ymin; j <= d2.ymax; j++) {
                    for (int k = d2.xmin; k <= d2.xmax; k++) {
                        if (def2 != null && def2.get(j, k)) {
                            BooleanRef defined = BooleanMarker.ref();
                            ret[j][k] = h.computeComplex(ret[j][k], defined);
                            if (defined.get()) {
                                def0.set(j, k);
                            }
                        } else {
                            ret[j][k] = Complex.ZERO;
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[][][] computeComplex(Expr base, UnaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Domain domainXY = base.getDomain();
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);

            Out<Range> r2 = new Out<Range>();
            Complex[][][] ret = h.getBaseExpr(base).toDC().computeComplex(x, y, z, d0, r2);
            Range d2 = r2.get();
            if (d2 != null) {
                BooleanArray3 def2 = d2.getDefined3();
                for (int t = d2.zmin; t <= d2.zmax; t++) {
                    for (int j = d2.ymin; j <= d2.ymax; j++) {
                        for (int k = d2.xmin; k <= d2.xmax; k++) {
                            if (def2 != null && def2.get(t, j, k)) {
                                BooleanRef defined = BooleanMarker.ref();
                                ret[t][j][k] = h.computeComplex(ret[t][j][k], defined);
                                if (defined.get()) {
                                    def0.set(t, j, k);
                                }
                            } else {
                                ret[t][j][k] = Complex.ZERO;
                            }
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(r);
                }
            } else {
                if (ranges != null) {
                    ranges.set(null);
                }
            }
            return ret;
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public interface BinaryExprHelper<T extends Expr> {
        int getBaseExprCount(T expr);

        Expr getBaseExpr(T expr, int index);

        double computeDouble(double a, double b, BooleanMarker defined, ComputeDefOptions options);

        Complex computeComplex(Complex a, Complex b, BooleanMarker defined, ComputeDefOptions options);

        ComplexMatrix computeMatrix(ComplexMatrix a, ComplexMatrix b, ComplexMatrix zero, BooleanMarker defined, ComputeDefOptions options);

        ComplexVector computeVector(ComplexVector a, ComplexVector b, ComplexVector zero, BooleanMarker defined, ComputeDefOptions options);
    }

    public interface TernaryExprHelper<T extends Expr> {
        Expr getBaseExpr(T expr, int index);

        double computeDouble(double a, double b, double c, BooleanMarker defined, ComputeDefOptions options);

        Complex computeComplex(Complex a, Complex b, Complex c, BooleanMarker defined, ComputeDefOptions options);

        ComplexMatrix computeMatrix(ComplexMatrix a, ComplexMatrix b, ComplexMatrix c, ComplexMatrix zero, BooleanMarker defined, ComputeDefOptions options);

        ComplexVector computeVector(ComplexVector a, ComplexVector b, ComplexVector c, ComplexVector zero, BooleanMarker defined, ComputeDefOptions options);
    }

    public static final class ComputeDefOptions {
        public boolean value1Defined;
        public boolean value2Defined;
        public boolean value3Defined;
//        public boolean resultDefined;

        public boolean isDefined2() {
            return value1Defined && value2Defined;
        }

        public boolean isDefined3() {
            return value1Defined && value2Defined && value3Defined;
        }
    }


    public static double[] computeDouble(Expr base, BinaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            double[] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                double[] val = h.getBaseExpr(base, ii).toDD().computeDouble(x, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray1 def2 = d2.getDefined1();
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = def2 != null && def2.get(k);
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeDouble(ret1[k], val[k], defined, o);
                        def0.set(k, defined.get());
                    }
                } else {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = false;
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeDouble(ret1[k], 0, defined, o);
                        def0.set(k, defined.get());
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[] ret = new double[x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static double[] computeDouble(Expr base, TernaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            double[] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            double[] ret2 = h.getBaseExpr(base, 1).toDD().computeDouble(x, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            double[] ret3 = h.getBaseExpr(base, 2).toDD().computeDouble(x, d0, r3);
            Range d3 = r3.get();

            BooleanArray1 def2 = d2 == null ? null : d2.getDefined1();
            BooleanArray1 def3 = d3 == null ? null : d3.getDefined1();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                o.value1Defined = def0 != null && def0.get(k);
                o.value2Defined = def2 != null && def2.get(k);
                o.value2Defined = def3 == null && def3.get(k);
                BooleanRef defined = BooleanMarker.ref();
                ret1[k] = h.computeDouble(ret1[k], ret2 == null ? 0 : ret2[k], ret3 == null ? 0 : ret3[k], defined, o);
                def0.set(k, defined.get());
            }

            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[] ret = new double[x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static double[][] computeDouble(Expr base, BinaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            double[][] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
//                try {
//                    d1.getDefined2();
//                }catch(NullPointerException ee){
//                    h.getBaseExpr(base,0).toDD().computeDouble(x, y,d0, r1);
//                }
                BooleanArray2 def1 = d1.getDefined2();
//                for (int j = r0.ymin; j <= r0.ymax; j++) {
//                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        def0[j][k] = def1[j][k];
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                double[][] val = h.getBaseExpr(base, ii).toDD().computeDouble(x, y, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray2 def2 = d2.getDefined2();
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = def2 != null && def2.get(j, k);
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeDouble(ret1[j][k], val[j][k], defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                } else {
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = false;
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeDouble(ret1[j][k], 0, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[][] ret = new double[y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static double[][] computeDouble(Expr base, TernaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            double[][] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
//                try {
//                    d1.getDefined2();
//                }catch(NullPointerException ee){
//                    h.getBaseExpr(base,0).toDD().computeDouble(x, y,d0, r1);
//                }
                BooleanArray2 def1 = d1.getDefined2();
//                for (int j = r0.ymin; j <= r0.ymax; j++) {
//                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        def0[j][k] = def1[j][k];
//                    }
//                }
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            double[][] ret2 = h.getBaseExpr(base, 1).toDD().computeDouble(x, y, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            double[][] ret3 = h.getBaseExpr(base, 2).toDD().computeDouble(x, y, d0, r3);
            Range d3 = r3.get();

            BooleanArray2 def2 = d2 == null ? null : d2.getDefined2();
            BooleanArray2 def3 = d3 == null ? null : d3.getDefined2();
            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    o.value1Defined = def0 != null && def0.get(j, k);
                    o.value2Defined = def2 != null && def2.get(j, k);
                    o.value3Defined = def3 != null && def3.get(j, k);
                    BooleanRef defined = BooleanMarker.ref();
                    ret1[j][k] = h.computeDouble(ret1[j][k], ret2 == null ? 0 : ret2[j][k], ret3 == null ? 0 : ret3[j][k], defined, o);
                    def0.set(j, k, defined.get());
                }
            }

            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[][] ret = new double[y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static double[][][] computeDouble(Expr base, BinaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            double[][][] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
//                for (int i = r0.ymin; i <= r0.ymax; i++) {
//                    for (int j = r0.ymin; j <= r0.ymax; j++) {
//                        for (int k = r0.xmin; k <= r0.xmax; k++) {
//                            def0[i][j][k] = def1[i][j][k];
//                        }
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                double[][][] val = h.getBaseExpr(base, ii).toDD().computeDouble(x, y, z, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray3 def2 = d2.getDefined3();
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = def2 != null && def2.get(i, j, k);
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeDouble(ret1[i][j][k], val[i][j][k], defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                } else {
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = false;
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeDouble(ret1[i][j][k], 0, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[][][] ret = new double[z.length][y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static double[][][] computeDouble(Expr base, TernaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();

            double[][][] ret1 = h.getBaseExpr(base, 0).toDD().computeDouble(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }


            Out<Range> r2 = new Out<Range>();
            double[][][] ret2 = h.getBaseExpr(base, 1).toDD().computeDouble(x, y, z, d0, r2);
            Range d2 = r2.get();
            Out<Range> r3 = new Out<Range>();
            double[][][] ret3 = h.getBaseExpr(base, 2).toDD().computeDouble(x, y, z, d0, r3);
            Range d3 = r3.get();

            BooleanArray3 def2 = d2 == null ? null : d2.getDefined3();
            BooleanArray3 def3 = d3 == null ? null : d3.getDefined3();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(i, j, k);
                        o.value2Defined = def2 != null && def2.get(i, j, k);
                        o.value3Defined = def3 != null && def3.get(i, j, k);
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[i][j][k] = h.computeDouble(ret1[i][j][k], ret2 == null ? 0 : ret2[i][j][k], ret3 == null ? 0 : ret3[i][j][k], defined, o);
                        def0.set(i, j, k, defined.get());
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            double[][][] ret = new double[z.length][y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static Complex[] computeComplex(Expr base, BinaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {

            BooleanArray1 def0 = r0.setDefined1(x.length);
            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            Complex[] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                Complex[] val = h.getBaseExpr(base, ii).toDC().computeComplex(x, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray1 def2 = d2.getDefined1();
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = def2 != null && def2.get(k);
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[k] = h.computeComplex(ret1[k], val[k], defined, o);
                        def0.set(k, defined.get());
                    }
                } else {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = false;
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[k] = h.computeComplex(ret1[k], zero, defined, o);
                        def0.set(k, defined.get());
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[] computeComplex(Expr base, TernaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {

            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            Complex[] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            Complex[] ret2 = h.getBaseExpr(base, 1).toDC().computeComplex(x, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            Complex[] ret3 = h.getBaseExpr(base, 2).toDC().computeComplex(x, d0, r3);
            Range d3 = r3.get();

            BooleanArray1 def2 = d2 == null ? null : d2.getDefined1();
            BooleanArray1 def3 = d3 == null ? null : d3.getDefined1();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                o.value1Defined = def0 != null && def0.get(k);
                o.value2Defined = def2 != null && def2.get(k);
                o.value3Defined = def3 != null && def3.get(k);
                BooleanRef defined = BooleanMarker.ref();
                ret1[k] = h.computeComplex(ret1[k], ret2 == null ? Complex.ZERO : ret2[k], ret3 == null ? Complex.ZERO : ret3[k], defined, o);
                def0.set(k, defined.get());
            }

            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[][] computeComplex(Expr base, BinaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {
            BooleanArray2 def0 = r0.setDefined2(x.length, y.length);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            Complex[][] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, y, d0, r1);
            ExpressionsDebug.debug_check(ret1,r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                if (def1 != null) {
                    def0.copyFrom(def1, r0);
                }
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                Complex[][] val = h.getBaseExpr(base, ii).toDC().computeComplex(x, y, d0, r2);
                ExpressionsDebug.debug_check(val,r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray2 def2 = d2.getDefined2();
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = def2 != null && def2.get(j, k);
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeComplex(ret1[j][k], val[j][k], defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                } else {
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = false;
                            BooleanRef defined = BooleanMarker.ref();
                            ret1[j][k] = h.computeComplex(ret1[j][k], zero, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static Complex[][] computeComplex(Expr base, TernaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            Complex[][] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, y, d0, r1);
            ExpressionsDebug.debug_check(ret1,r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                def0.copyFrom(def1, r0);
            }
            Out<Range> r2 = new Out<Range>();
            Complex[][] ret2 = h.getBaseExpr(base, 1).toDC().computeComplex(x, y, d0, r2);
            ExpressionsDebug.debug_check(ret2,r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            Complex[][] ret3 = h.getBaseExpr(base, 2).toDC().computeComplex(x, y, d0, r3);
            ExpressionsDebug.debug_check(ret3,r3);
            Range d3 = r2.get();

            BooleanArray2 def2 = d2 == null ? null : d2.getDefined2();
            BooleanArray2 def3 = d3 == null ? null : d3.getDefined2();

            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    o.value1Defined = def0 != null && def0.get(j, k);
                    o.value2Defined = def2 != null && def2.get(j, k);
                    o.value3Defined = def3 != null && def3.get(j, k);
                    BooleanRef defined = BooleanMarker.ref();
                    ret1[j][k] = h.computeComplex(ret1[j][k], ret2 == null ? Complex.ZERO : ret2[j][k], ret3 == null ? Complex.ZERO : ret3[j][k], defined, o);
                    def0.set(j, k, defined.get());
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static Complex[][][] computeComplex(Expr base, BinaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            Complex[][][] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
//                for (int i = r0.ymin; i <= r0.ymax; i++) {
//                    for (int j = r0.ymin; j <= r0.ymax; j++) {
//                        for (int k = r0.xmin; k <= r0.xmax; k++) {
//                            def0[i][j][k] = def1[i][j][k];
//                        }
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                Complex[][][] val = h.getBaseExpr(base, ii).toDC().computeComplex(x, y, z, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray3 def2 = d2.getDefined3();
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = def2 != null && def2.get(i, j, k);
                                BooleanRef defined = BooleanMarker.ref();
                                ret1[i][j][k] = h.computeComplex(ret1[i][j][k], val[i][j][k], defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                } else {
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = false;
                                BooleanRef defined = BooleanMarker.ref();
                                ret1[i][j][k] = h.computeComplex(ret1[i][j][k], zero, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static Complex[][][] computeComplex(Expr base, TernaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        Complex zero = Complex.ZERO;
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            Complex[][][] ret1 = h.getBaseExpr(base, 0).toDC().computeComplex(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            Complex[][][] ret2 = h.getBaseExpr(base, 1).toDC().computeComplex(x, y, z, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            Complex[][][] ret3 = h.getBaseExpr(base, 2).toDC().computeComplex(x, y, z, d0, r3);
            Range d3 = r3.get();

            BooleanArray3 def2 = d2 == null ? null : d2.getDefined3();
            BooleanArray3 def3 = d3 == null ? null : d3.getDefined3();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(i, j, k);
                        o.value2Defined = def2 != null && def2.get(i, j, k);
                        o.value3Defined = def3 != null && def3.get(i, j, k);
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[i][j][k] = h.computeComplex(ret1[i][j][k], ret2 == null ? Complex.ZERO : ret2[i][j][k], ret3 == null ? Complex.ZERO : ret3[i][j][k], defined, o);
                        def0.set(i, j, k, defined.get());
                    }
                }
            }

            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


//------------------------------------------------------------------------------------------------------------------

    public static ComplexMatrix[] computeMatrix(Expr base, BinaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexMatrix[] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexMatrix[] val = h.getBaseExpr(base, ii).toDM().computeMatrix(x, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray1 def2 = d2.getDefined1();
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = def2 != null && def2.get(k);
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeMatrix(ret1[k], val[k], zero, defined, o);
                        def0.set(k, defined.get());
                    }
                } else {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = false;
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeMatrix(ret1[k], zero, zero, defined, o);
                        def0.set(k, defined.get());
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[] ret = ArrayUtils.fillArray1Matrix(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[] computeVector(Expr base, BinaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosColumnVector(d.rows);
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexVector[] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexVector[] val = h.getBaseExpr(base, ii).toDV().computeVector(x, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray1 def2 = d2.getDefined1();
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = def2 != null && def2.get(k);
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeVector(ret1[k], val[k], zero, defined, o);
                        def0.set(k, defined.get());
                    }
                } else {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(k);
                        o.value2Defined = false;
                        BooleanRef defined = BooleanRefs.create();
                        ret1[k] = h.computeVector(ret1[k], zero, zero, defined, o);
                        def0.set(k, defined.get());
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[] ret = ArrayUtils.fillArray1Vector(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexMatrix[] computeMatrix(Expr base, TernaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexMatrix[] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[] ret2 = h.getBaseExpr(base, 1).toDM().computeMatrix(x, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexMatrix[] ret3 = h.getBaseExpr(base, 2).toDM().computeMatrix(x, d0, r3);
            Range d3 = r3.get();

            BooleanArray1 def2 = d2 == null ? null : d2.getDefined1();
            BooleanArray1 def3 = d3 == null ? null : d3.getDefined1();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                o.value1Defined = def0 != null && def0.get(k);
                o.value2Defined = def2 != null && def2.get(k);
                o.value3Defined = def3 != null && def3.get(k);
                BooleanRef defined = BooleanMarker.ref();
                ret1[k] = h.computeMatrix(ret1[k], ret2 == null ? zero : ret2[k], ret3 == null ? zero : ret3[k], zero, defined, o);
                def0.set(k, defined.get());
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[] ret = ArrayUtils.fillArray1Matrix(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[] computeVector(Expr base, TernaryExprHelper h, double[] x, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosVector(d.rows);
        if (r0 != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexVector[] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexVector[] ret2 = h.getBaseExpr(base, 1).toDV().computeVector(x, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexVector[] ret3 = h.getBaseExpr(base, 2).toDV().computeVector(x, d0, r3);
            Range d3 = r3.get();

            BooleanArray1 def2 = d2 == null ? null : d2.getDefined1();
            BooleanArray1 def3 = d3 == null ? null : d3.getDefined1();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                o.value1Defined = def0 != null && def0.get(k);
                o.value2Defined = def2 != null && def2.get(k);
                o.value3Defined = def3 != null && def3.get(k);
                BooleanRef defined = BooleanMarker.ref();
                ret1[k] = h.computeVector(ret1[k], ret2 == null ? zero : ret2[k], ret3 == null ? zero : ret3[k], zero, defined, o);
                def0.set(k, defined.get());
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[] ret = ArrayUtils.fillArray1Vector(x.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexMatrix[][] computeMatrix(Expr base, BinaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexMatrix[][] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
//                for (int j = r0.ymin; j <= r0.ymax; j++) {
//                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        def0[j][k] = def1[j][k];
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexMatrix[][] val = h.getBaseExpr(base, ii).toDM().computeMatrix(x, y, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray2 def2 = d2.getDefined2();
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = def2 != null && def2.get(j, k);
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeMatrix(ret1[j][k], val[j][k], zero, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                } else {
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = false;
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeMatrix(ret1[j][k], zero, zero, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[][] ret = ArrayUtils.fillArray2Matrix(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[][] computeVector(Expr base, BinaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosVector(d.rows);
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexVector[][] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
//                for (int j = r0.ymin; j <= r0.ymax; j++) {
//                    for (int k = r0.xmin; k <= r0.xmax; k++) {
//                        def0[j][k] = def1[j][k];
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexVector[][] val = h.getBaseExpr(base, ii).toDV().computeVector(x, y, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray2 def2 = d2.getDefined2();
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = def2 != null && def2.get(j, k);
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeVector(ret1[j][k], val[j][k], zero, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                } else {
                    for (int j = r0.ymin; j <= r0.ymax; j++) {
                        for (int k = r0.xmin; k <= r0.xmax; k++) {
                            o.value1Defined = def0 != null && def0.get(j, k);
                            o.value2Defined = false;
                            BooleanRef defined = BooleanRefs.create();
                            ret1[j][k] = h.computeVector(ret1[j][k], zero, zero, defined, o);
                            def0.set(j, k, defined.get());
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[][] ret = ArrayUtils.fillArray2Vector(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexMatrix[][] computeMatrix(Expr base, TernaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexMatrix[][] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[][] ret2 = h.getBaseExpr(base, 1).toDM().computeMatrix(x, y, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexMatrix[][] ret3 = h.getBaseExpr(base, 2).toDM().computeMatrix(x, y, d0, r3);
            Range d3 = r2.get();


            BooleanArray2 def2 = d2 == null ? null : d2.getDefined2();
            BooleanArray2 def3 = d3 == null ? null : d3.getDefined2();
            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    o.value1Defined = def0 != null && def0.get(j, k);
                    o.value2Defined = def2 != null && def2.get(j, k);
                    o.value3Defined = def3 != null && def3.get(j, k);
                    BooleanRef defined = BooleanMarker.ref();
                    ret1[j][k] = h.computeMatrix(ret1[j][k], ret2 == null ? zero : ret2[j][k], ret3 == null ? zero : ret3[j][k], zero, defined, o);
                    def0.set(j, k, defined.get());
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[][] ret = ArrayUtils.fillArray2Matrix(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static ComplexVector[][] computeVector(Expr base, TernaryExprHelper h, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosVector(d.rows);
        if (r0 != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexVector[][] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexVector[][] ret2 = h.getBaseExpr(base, 1).toDV().computeVector(x, y, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexVector[][] ret3 = h.getBaseExpr(base, 2).toDV().computeVector(x, y, d0, r3);
            Range d3 = r2.get();


            BooleanArray2 def2 = d2 == null ? null : d2.getDefined2();
            BooleanArray2 def3 = d3 == null ? null : d3.getDefined2();
            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    o.value1Defined = def0 != null && def0.get(j, k);
                    o.value2Defined = def2 != null && def2.get(j, k);
                    o.value3Defined = def3 != null && def3.get(j, k);
                    BooleanRef defined = BooleanMarker.ref();
                    ret1[j][k] = h.computeVector(ret1[j][k], ret2 == null ? zero : ret2[j][k], ret3 == null ? zero : ret3[j][k], zero, defined, o);
                    def0.set(j, k, defined.get());
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[][] ret = ArrayUtils.fillArray2Vector(x.length, y.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static ComplexMatrix[][][] computeMatrix(Expr base, BinaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexMatrix[][][] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
//                for (int i = r0.ymin; i <= r0.ymax; i++) {
//                    for (int j = r0.ymin; j <= r0.ymax; j++) {
//                        for (int k = r0.xmin; k <= r0.xmax; k++) {
//                            def0[i][j][k] = def1[i][j][k];
//                        }
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexMatrix[][][] val = h.getBaseExpr(base, ii).toDM().computeMatrix(x, y, z, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray3 def2 = d2.getDefined3();
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = def2 != null && def2.get(i, j, k);
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeMatrix(ret1[i][j][k], val[i][j][k], zero, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                } else {
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = false;
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeMatrix(ret1[i][j][k], zero, zero, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[][][] ret = ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[][][] computeVector(Expr base, BinaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosVector(d.rows);
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            int count = h.getBaseExprCount(base);
            ComplexVector[][][] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
//                for (int i = r0.ymin; i <= r0.ymax; i++) {
//                    for (int j = r0.ymin; j <= r0.ymax; j++) {
//                        for (int k = r0.xmin; k <= r0.xmax; k++) {
//                            def0[i][j][k] = def1[i][j][k];
//                        }
//                    }
//                }
                def0.copyFrom(def1, r0);
            }
            for (int ii = 1; ii < count; ii++) {
                Out<Range> r2 = new Out<Range>();
                ComplexVector[][][] val = h.getBaseExpr(base, ii).toDV().computeVector(x, y, z, d0, r2);
                Range d2 = r2.get();
                if (d2 != null) {
                    BooleanArray3 def2 = d2.getDefined3();
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = def2 != null && def2.get(i, j, k);
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeVector(ret1[i][j][k], val[i][j][k], zero, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                } else {
                    for (int i = r0.zmin; i <= r0.zmax; i++) {
                        for (int j = r0.ymin; j <= r0.ymax; j++) {
                            for (int k = r0.xmin; k <= r0.xmax; k++) {
                                o.value1Defined = def0 != null && def0.get(i, j, k);
                                o.value2Defined = false;
                                BooleanRef defined = BooleanRefs.create();
                                ret1[i][j][k] = h.computeVector(ret1[i][j][k], zero, zero, defined, o);
                                def0.set(i, j, k, defined.get());
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[][][] ret = ArrayUtils.fillArray3Vector(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


    public static ComplexMatrix[][][] computeMatrix(Expr base, TernaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexMatrix zero = MathsBase.zerosMatrix(d.rows, d.columns);
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexMatrix[][][] ret1 = h.getBaseExpr(base, 0).toDM().computeMatrix(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexMatrix[][][] ret2 = h.getBaseExpr(base, 1).toDM().computeMatrix(x, y, z, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexMatrix[][][] ret3 = h.getBaseExpr(base, 2).toDM().computeMatrix(x, y, z, d0, r3);
            Range d3 = r3.get();

            BooleanArray3 def2 = d2.getDefined3();
            BooleanArray3 def3 = d3.getDefined3();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(i, j, k);
                        o.value2Defined = def2 != null && def2.get(i, j, k);
                        o.value3Defined = def3 != null && def3.get(i, j, k);
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[i][j][k] = h.computeMatrix(ret1[i][j][k], ret2 == null ? zero : ret2[i][j][k], ret3 == null ? zero : ret3[i][j][k], zero, defined, o);
                        def0.set(i, j, k, defined.get());
                    }
                }
            }


            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexMatrix[][][] ret = ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public static ComplexVector[][][] computeVector(Expr base, TernaryExprHelper h, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = base.getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        ComputeDefOptions o = new ComputeDefOptions();
        ComponentDimension d = base.getComponentDimension();
        ComplexVector zero = MathsBase.zerosVector(d.rows);
        if (r0 != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r0.setDefined(def0);

            Out<Range> r1 = new Out<Range>();
            ComplexVector[][][] ret1 = h.getBaseExpr(base, 0).toDV().computeVector(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }

            Out<Range> r2 = new Out<Range>();
            ComplexVector[][][] ret2 = h.getBaseExpr(base, 1).toDV().computeVector(x, y, z, d0, r2);
            Range d2 = r2.get();

            Out<Range> r3 = new Out<Range>();
            ComplexVector[][][] ret3 = h.getBaseExpr(base, 2).toDV().computeVector(x, y, z, d0, r3);
            Range d3 = r3.get();

            BooleanArray3 def2 = d2.getDefined3();
            BooleanArray3 def3 = d3.getDefined3();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        o.value1Defined = def0 != null && def0.get(i, j, k);
                        o.value2Defined = def2 != null && def2.get(i, j, k);
                        o.value3Defined = def3 != null && def3.get(i, j, k);
                        BooleanRef defined = BooleanMarker.ref();
                        ret1[i][j][k] = h.computeVector(ret1[i][j][k], ret2 == null ? zero : ret2[i][j][k], ret3 == null ? zero : ret3[i][j][k], zero, defined, o);
                        def0.set(i, j, k, defined.get());
                    }
                }
            }


            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            ComplexVector[][][] ret = ArrayUtils.fillArray3Vector(x.length, y.length, z.length, zero);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }


}
