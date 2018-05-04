package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Out;

/**
 * Created by vpc on 4/30/14.
 */
public class ZZ extends AxisFunction implements Cloneable {

    public ZZ(Domain domain) {
        super(domain, "Z");
    }

    @Override
    public String getFunctionName() {
        return "Z";
    }

    @Override
    public Axis getAxis() {
        return Axis.Z;
    }

    @Override
    public Expr newInstance(Expr... arguments) {
        Expr xx = new ZZ(getDomain());
        xx= Any.copyProperties(this, xx);
        return xx;
    }
    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
    }


//    @Override
//    public Domain getDomain() {
//        return getDomain().getDomainY();
//    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            Complex[][][] cc = new Complex[z.length][y.length][x.length];
            ArrayUtils.fillArray3ZeroComplex(cc, nonNullRanges);
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                Complex v = Complex.valueOf(z[t]);
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
//        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).ranges(x, y);
//        if (nonNullRanges != null) {
//            Complex[][] cc = new Complex[y.length][x.length];
//            Arrays2.fillArray2ZeroComplex(cc, nonNullRanges);
//            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
//                Complex v = new Complex(y[j], 0);
//                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
//                    cc[j][k] = v;
//                }
//            }
//            if (ranges != null) {
//                ranges.set(nonNullRanges);
//            }
//            return cc;
//        } else {
//            Complex[][] cc = Arrays2.fillArray2Complex(x.length, y.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return cc;
//        }
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
//        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).ranges(x, y);
//        if (nonNullRanges != null) {
//            double[][] cc = new double[y.length][x.length];
//            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
//                double v = y[j];
//                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
//                    cc[j][k] = v;
//                }
//            }
//            if (ranges != null) {
//                ranges.set(nonNullRanges);
//            }
//            return cc;
//        } else {
//            double[][] cc = new double[y.length][x.length];
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return cc;
//        }
    }


    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            double[][][] cc = new double[z.length][y.length][x.length];
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                double v = z[t];
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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

//    @Override
//    public boolean isDDx() {
//        return false;
//    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new ClassCastException("Missing Z");
    }

    @Override
    public Expr clone() {
        ZZ cloned = (ZZ) super.clone();
        return cloned;
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new ClassCastException("Missing Z");
//        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
//        if (nonNullRanges != null) {
//            Matrix[][] cc = new Matrix[y.length][x.length];
//            ArrayUtils.fillArray2ZeroMatrix(cc, nonNullRanges, -1, -1);
//            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
//                Matrix v = new Complex(y[j], 0).toMatrix();
//                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
//                    cc[j][k] = v;
//                }
//            }
//            if (ranges != null) {
//                ranges.set(nonNullRanges);
//            }
//            return cc;
//        } else {
//            Matrix[][] cc = ArrayUtils.fillArray2Matrix(x.length, y.length, Matrix.zerosMatrix(1));
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return cc;
//        }
    }
    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        throw new ClassCastException("Missing Z");
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            Matrix[][][] cc = new Matrix[z.length][y.length][x.length];
            ArrayUtils.fillArray3ZeroMatrix(cc, nonNullRanges, 1, 1);
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                Matrix v = Complex.valueOf(z[t]).toMatrix();
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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
    public int getDomainDimension() {
        return 3;
    }


    @Override
    public Complex computeComplex(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return Complex.valueOf(z);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return z;
        }
        return 0;
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        if (domain.contains(x, y, z)) {
            return Complex.valueOf(z).toMatrix();
        }
        return Maths.identityMatrix(1);
    }


    @Override
    public Expr mul(Domain domain) {
        return new Mul(new DoubleValue(1,Domain.FULLX),this);
    }

    @Override
    public Expr mul(double other) {
        return new Mul(new DoubleValue(other,Domain.FULLX),this);
    }

    @Override
    public Expr mul(Complex other) {
        return new Mul(other,this);
    }

}
