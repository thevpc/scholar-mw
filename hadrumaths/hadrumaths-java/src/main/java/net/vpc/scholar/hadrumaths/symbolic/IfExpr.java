package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class IfExpr extends GenericFunctionXYZ implements Cloneable {
    private static final long serialVersionUID = 1L;
    private boolean hasThen;
    private boolean hasElse;

    public IfExpr(Expr xarg) {
        this(xarg, null, null);
    }

    public IfExpr(Expr xarg, Expr yarg) {
        this(xarg, yarg, null);
    }

    public IfExpr(Expr xarg, Expr yarg, Expr zarg) {
        super("if", xarg==null?Complex.ONE:xarg,
                yarg==null?Complex.ONE:yarg,
                zarg==null?Complex.ZERO:zarg,
                FunctionType.DOUBLE
        );
        hasThen=yarg!=null;
        hasElse=zarg!=null;
    }

    public IfExpr Then(Expr yarg){
        if(hasThen){
            throw new IllegalArgumentException("Multiple Then Expressions");
        }
        return new IfExpr(getXArgument(),yarg,hasElse?getZArgument():null);
    }

    public IfExpr Else(Expr zarg){
        if(!hasThen){
            throw new IllegalArgumentException("Multiple Missing Then expression");
        }
        if(hasElse){
            throw new IllegalArgumentException("Multiple Else Expressions");
        }
        return new IfExpr(getXArgument(),getYArgument(),zarg);
    }

    @Override
    public String getFunctionName() {
        return "If";
    }

    protected Complex evalComplex(Complex x, Complex y, Complex z) {
        if (!x.isZero()) {
            return y;
        }
        return z;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            Complex v = getXArgument().toDC().computeComplex(x, defined1);
            if (defined1.get()) {
                if (!v.isZero()) {
                    return getYArgument().toDC().computeComplex(x, defined);
                } else {
                    return getZArgument().toDC().computeComplex(x, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            Complex v = getXArgument().toDC().computeComplex(x, y, defined1);
            if (defined1.get()) {
                if (!v.isZero()) {
                    return getYArgument().toDC().computeComplex(x, y, defined);
                } else {
                    return getZArgument().toDC().computeComplex(x, y, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            Complex v = getXArgument().toDC().computeComplex(x, y, z, defined1);
            if (defined1.get()) {
                if (!v.isZero()) {
                    return getYArgument().toDC().computeComplex(x, y, z, defined);
                } else {
                    return getZArgument().toDC().computeComplex(x, y, z, defined);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getXArgument().toDD().computeDouble(x, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getYArgument().toDD().computeDouble(x, defined);
                } else {
                    return getZArgument().toDD().computeDouble(x, defined);
                }
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getXArgument().toDD().computeDouble(x, y, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getYArgument().toDD().computeDouble(x, y, defined);
                } else {
                    return getZArgument().toDD().computeDouble(x, y, defined);
                }
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef defined1 = BooleanMarker.ref();
            double v = getXArgument().toDD().computeDouble(x, y, z, defined1);
            if (defined1.get()) {
                if (v != 0) {
                    return getYArgument().toDD().computeDouble(x, y, z, defined);
                } else {
                    return getZArgument().toDD().computeDouble(x, y, z, defined);
                }
            }
        }
        return 0;
    }

    protected Complex evalComplex(double x, double y, double z) {
        if (x != 0) {
            return Complex.valueOf(y);
        }
        return Complex.valueOf(z);
    }

    protected double evalDouble(double x, double y, double z) {
        if (x != 0) {
            return y;
        } else {
            return z;
        }
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument, Expr zargument) {
        return new IfExpr(xargument, yargument, zargument);
    }

    @Override
    public boolean isDDImpl() {
        return getXArgument().isDD() && getYArgument().isDD() && getZArgument().isDD();
    }

    @Override
    public boolean isDCImpl() {
        return getXArgument().isDC() && getYArgument().isDC() && getZArgument().isDC();
    }

    public Domain getDomainImpl() {
        return getXArgument().getDomain().union(getYArgument().getDomain()).union(getZArgument().getDomain());
    }


    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        if (r0 != null) {
            BooleanArray3 def0 = r0.setDefined3(x.length, y.length, z.length);

            Out<Range> r1 = new Out<Range>();

            double[][][] ret1 = getXArgument().toDD().computeDouble(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }
            DoubleToDouble ydd = getYArgument().toDD();
            DoubleToDouble zdd = getZArgument().toDD();
            BooleanRef readable = BooleanMarker.ref();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        if (def0.get(i, j, k)) {
                            if (ret1[i][j][k] != 0) {
                                ret1[i][j][k] = ydd.computeDouble(x[k], y[j], z[i], readable);
                            } else {
                                ret1[i][j][k] = zdd.computeDouble(x[k], y[j], z[i], readable);
                            }
                            def0.set(i, j, k, readable.get());
                            readable.reset();
                        } else {
                            ret1[i][j][k] = 0;
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

    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x, y, z);
        if (r0 != null) {
            BooleanArray3 def0 = r0.setDefined3(x.length, y.length, z.length);

            Out<Range> r1 = new Out<Range>();

            Complex[][][] ret1 = getXArgument().toDC().computeComplex(x, y, z, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray3 def1 = d1.getDefined3();
                def0.copyFrom(def1, r0);
            }
            DoubleToComplex ydd = getYArgument().toDC();
            DoubleToComplex zdd = getZArgument().toDC();
            BooleanRef readable = BooleanMarker.ref();
            for (int i = r0.zmin; i <= r0.zmax; i++) {
                for (int j = r0.ymin; j <= r0.ymax; j++) {
                    for (int k = r0.xmin; k <= r0.xmax; k++) {
                        if (def0.get(i, j, k)) {
                            if (!ret1[i][j][k].isZero()) {
                                ret1[i][j][k] = ydd.computeComplex(x[k], y[j], z[i], readable);
                            } else {
                                ret1[i][j][k] = zdd.computeComplex(x[k], y[j], z[i], readable);
                            }
                            def0.set(i, j, k, readable.get());
                            readable.reset();
                        } else {
                            ret1[i][j][k] = Complex.ZERO;
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        if (r0 != null) {
            BooleanArray2 def0 = r0.setDefined2(x.length, y.length);

            Out<Range> r1 = new Out<Range>();

            double[][] ret1 = getXArgument().toDD().computeDouble(x, y, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                def0.copyFrom(def1, r0);
            }
            DoubleToDouble ydd = getYArgument().toDD();
            DoubleToDouble zdd = getZArgument().toDD();
            BooleanRef readable = BooleanMarker.ref();
            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    if (def0.get(j, k)) {
                        if (ret1[j][k] != 0) {
                            ret1[j][k] = ydd.computeDouble(x[k], y[j], readable);
                        } else {
                            ret1[j][k] = zdd.computeDouble(x[k], y[j], readable);
                        }
                        def0.set(j, k, readable.get());
                        readable.reset();
                    } else {
                        ret1[j][k] = 0;
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

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x, y);
        if (r0 != null) {
            BooleanArray2 def0 = r0.setDefined2(x.length, y.length);

            Out<Range> r1 = new Out<Range>();

            Complex[][] ret1 = getXArgument().toDC().computeComplex(x, y, d0, r1);
            ExpressionsDebug.debug_check(ret1,r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray2 def1 = d1.getDefined2();
                def0.copyFrom(def1, r0);
            }
            DoubleToComplex ydd = getYArgument().toDC();
            DoubleToComplex zdd = getZArgument().toDC();
            BooleanRef readable = BooleanMarker.ref();
            for (int j = r0.ymin; j <= r0.ymax; j++) {
                for (int k = r0.xmin; k <= r0.xmax; k++) {
                    if (def0.get(j, k)) {
                        if (!ret1[j][k].isZero()) {
                            ret1[j][k] = ydd.computeComplex(x[k], y[j], readable);
                        } else {
                            ret1[j][k] = zdd.computeComplex(x[k], y[j], readable);
                        }
                        def0.set(j, k, readable.get());
                        readable.reset();
                    } else {
                        ret1[j][k] = Complex.ZERO;
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x);
        if (r0 != null) {
            BooleanArray1 def0 = r0.setDefined1(x.length);

            Out<Range> r1 = new Out<Range>();

            double[] ret1 = getXArgument().toDD().computeDouble(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            DoubleToDouble ydd = getYArgument().toDD();
            DoubleToDouble zdd = getZArgument().toDD();
            BooleanRef readable = BooleanMarker.ref();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                if (def0.get(k)) {
                    if (ret1[k] != 0) {
                        ret1[k] = ydd.computeDouble(x[k], readable);
                    } else {
                        ret1[k] = zdd.computeDouble(x[k], readable);
                    }
                    def0.set(k, readable.get());
                    readable.reset();
                } else {
                    ret1[k] = 0;
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

    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        d0 = getDomain().intersect(d0);
        Range r0 = d0.range(x);
        if (r0 != null) {
            BooleanArray1 def0 = r0.setDefined1(x.length);

            Out<Range> r1 = new Out<Range>();

            Complex[] ret1 = getXArgument().toDC().computeComplex(x, d0, r1);
            Range d1 = r1.get();
            if (d1 != null) {
                BooleanArray1 def1 = d1.getDefined1();
                def0.copyFrom(def1, r0);
            }
            DoubleToComplex ydd = getYArgument().toDC();
            DoubleToComplex zdd = getZArgument().toDC();
            BooleanRef readable = BooleanMarker.ref();
            for (int k = r0.xmin; k <= r0.xmax; k++) {
                if (def0.get(k)) {
                    if (!ret1[k].isZero()) {
                        ret1[k] = ydd.computeComplex(x[k], readable);
                    } else {
                        ret1[k] = zdd.computeComplex(x[k], readable);
                    }
                    def0.set(k, readable.get());
                    readable.reset();
                } else {
                    ret1[k] = Complex.ZERO;
                }
            }
            if (ranges != null) {
                ranges.set(r0);
            }
            return ret1;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

}
