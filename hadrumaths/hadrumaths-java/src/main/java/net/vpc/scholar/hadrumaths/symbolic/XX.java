package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

/**
 * Created by vpc on 4/30/14.
 */
public class XX extends AxisFunction implements Cloneable {
    public XX(Domain domain) {
        super(domain, "X");
    }


    @Override
    public Axis getAxis() {
        return Axis.X;
    }

    @Override
    public Expr newInstance(Expr... arguments) {
        Expr xx = new XX(getDomain());
        xx=copyProperties(this, xx);
        return xx;
    }

//    @Override
//    public boolean isDDx() {
//        return true;
//    }


//    @Override
//    public Domain getDomain() {
//        return getDomain().getDomain();
//    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (nonNullRanges != null) {
            Complex[][] cc = new Complex[y.length][x.length];
            BooleanArray2 def = BooleanArrays.newArray(y.length,x.length);
            nonNullRanges.setDefined(def);
            ArrayUtils.fillArray2ZeroComplex(cc, nonNullRanges);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                Complex v = Complex.valueOf(x[k]);
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    cc[j][k] = v;
                    def.set(j,k);
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Complex[][] cc = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (nonNullRanges != null) {
            Complex[] cc = new Complex[x.length];
            ArrayUtils.fillArray1ZeroComplex(cc, nonNullRanges);
            BooleanArray1 def = BooleanArrays.newArray(x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                cc[k] = Complex.valueOf(x[k]);
                def.set(k);
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Complex[] cc = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            Complex[][][] cc = new Complex[z.length][y.length][x.length];
            ArrayUtils.fillArray3ZeroComplex(cc, nonNullRanges);
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                Complex v = Complex.valueOf(x[k]);
                for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                    for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                        cc[t][j][k] = v;
                        def.set(t,j,k);

                    }
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Complex[][][] cc = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }


    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (nonNullRanges != null) {
            double[][] cc = new double[y.length][x.length];
            BooleanArray2 def = BooleanArrays.newArray(y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                double v = x[k];
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    cc[j][k] = v;
                    def.set(j,k);
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            double[][] cc = new double[y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            double[][][] cc = new double[z.length][y.length][x.length];
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                double v = x[k];
                for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                    for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                        cc[t][j][k] = v;
                        def.set(t,j,k);
                    }
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            double[][][] cc = new double[z.length][y.length][x.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        Range nonNullRanges = (d0 == null ? getDomain() : getDomain().intersect(d0)).range(x);
        if (nonNullRanges != null) {
            double[] cc = new double[x.length];
            System.arraycopy(x, nonNullRanges.xmin, cc, nonNullRanges.xmin, nonNullRanges.xmax + 1 - nonNullRanges.xmin);
            BooleanArray1 def = BooleanArrays.newArray(x.length);
            nonNullRanges.setDefined(def);
            for (int i = nonNullRanges.xmin; i <= nonNullRanges.xmax; i++) {
                def.set(i);
            }
            if (range != null) {
                range.set(nonNullRanges);
            }
            return cc;
        } else {
            double[] cc = new double[x.length];
            if (range != null) {
                range.set(null);
            }
            return cc;
        }
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (nonNullRanges != null) {
            Matrix[][] cc = new Matrix[y.length][x.length];
            ArrayUtils.fillArray2ZeroMatrix(cc, nonNullRanges, 1, 1);
            BooleanArray2 def = BooleanArrays.newArray(y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                Matrix v = Complex.valueOf(x[k]).toMatrix();
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    cc[j][k] = v;
                    def.set(j,k);
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Matrix[][] cc = ArrayUtils.fillArray2Matrix(x.length, y.length, Maths.zerosMatrix(1));
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            Matrix[][][] cc = new Matrix[z.length][y.length][x.length];
            ArrayUtils.fillArray3ZeroMatrix(cc, nonNullRanges, 1, 1);
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                Matrix v = Complex.valueOf(x[k]).toMatrix();
                for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                    for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                        cc[t][j][k] = v;
                        def.set(t,j,k);
                    }
                }
            }
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Matrix[][][] cc = ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, Maths.zerosMatrix(1));
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (nonNullRanges != null) {
            Matrix[] cc = new Matrix[x.length];
            BooleanArray1 def = BooleanArrays.newArray(x.length);
            nonNullRanges.setDefined(def);
            for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                Matrix v = Complex.valueOf(x[k]).toMatrix();
                cc[k] = v;
                def.set(k);
            }
            ArrayUtils.fillArray1ZeroMatrix(cc, nonNullRanges, -1,-1);
            if (ranges != null) {
                ranges.set(nonNullRanges);
            }
            return cc;
        } else {
            Matrix[] cc = ArrayUtils.fillArray1Matrix(x.length, Maths.zerosMatrix(1));
            if (ranges != null) {
                ranges.set(null);
            }
            return cc;
        }
    }

    public Expr composeX(Expr xreplacement) {
        return xreplacement;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }


    @Override
    public Expr clone() {
        XX cloned = (XX) super.clone();
        return cloned;
    }


    @Override
    public int getDomainDimension() {
        return 1;
    }


    @Override
    public Complex computeComplex(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return Complex.valueOf(x);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return x;
        }
        return 0;
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return Complex.valueOf(x).toMatrix();
        }
        return Maths.identityMatrix(1);
    }

}
