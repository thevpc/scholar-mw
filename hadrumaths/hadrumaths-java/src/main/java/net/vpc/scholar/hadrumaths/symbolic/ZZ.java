package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

/**
 * Created by vpc on 4/30/14.
 */
public class ZZ extends AxisFunction implements Cloneable {
    private static final long serialVersionUID = 1L;

    public ZZ(Domain domain) {
        super(domain.toDomain(3), Axis.Z);
    }

//    @Override
//    public String getFunctionName() {
//        return "Z";
//    }


//    @Override
//    public Expr newInstance(Expr... arguments) {
//        Expr xx = new ZZ(getDomain());
//        xx = Any.copyProperties(this, xx);
//        return xx;
//    }


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
            BooleanArray3 def = BooleanArrays.newArray(z.length, y.length, x.length);
            nonNullRanges.setDefined(def);
            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                Complex v = Complex.valueOf(z[t]);
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                        cc[t][j][k] = v;
                        def.set(t, j, k);
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
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            double[][][] cc = new double[z.length][y.length][x.length];
            BooleanArray3 def = BooleanArrays.newArray(z.length, y.length, x.length);
            nonNullRanges.setDefined(def);
            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
                double v = z[t];
                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                        cc[t][j][k] = v;
                        def.set(t, j, k);
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

//    @Override
//    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new ClassCastException("Missing Z");
//    }
//
//    @Override
//    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
//        throw new ClassCastException("Missing Z");
//    }
//
//
//    @Override
//    public Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new ClassCastException("Missing Z");
//    }
//
//    @Override
//    public Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
//        throw new ClassCastException("Missing Z");
//    }

//    @Override
//    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
//        if (nonNullRanges != null) {
//            Matrix[][][] cc = new Matrix[z.length][y.length][x.length];
//            ArrayUtils.fillArray3ZeroMatrix(cc, nonNullRanges, 1, 1);
//            BooleanArray3 def = BooleanArrays.newArray(z.length, y.length, x.length);
//            nonNullRanges.setDefined(def);
//            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
//                Matrix v = Complex.valueOf(z[t]).toMatrix();
//                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
//                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
//                        cc[t][j][k] = v;
//                        def.set(t, j, k);
//                    }
//                }
//            }
//            if (ranges != null) {
//                ranges.set(nonNullRanges);
//            }
//            return cc;
//        } else {
//            Matrix[][][] cc = ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, MathsBase.zerosMatrix(1));
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return cc;
//        }
//    }
//    @Override
//    public Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
//        if (nonNullRanges != null) {
//            Vector[][][] cc = new Vector[z.length][y.length][x.length];
//            ArrayUtils.fillArray3ZeroVector(cc, nonNullRanges, 1 );
//            BooleanArray3 def = BooleanArrays.newArray(z.length, y.length, x.length);
//            nonNullRanges.setDefined(def);
//            for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
//                Vector v = Complex.valueOf(z[t]).toVector();
//                for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
//                    for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
//                        cc[t][j][k] = v;
//                        def.set(t, j, k);
//                    }
//                }
//            }
//            if (ranges != null) {
//                ranges.set(nonNullRanges);
//            }
//            return cc;
//        } else {
//            Vector[][][] cc = ArrayUtils.fillArray3Vector(x.length, y.length, z.length, MathsBase.zerosVector(1));
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return cc;
//        }
//    }

    @Override
    public int getDomainDimension() {
        return 3;
    }

//    @Override
//    public int getComponentSize() {
//        return 1;
//    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (domain.contains(x, y, z)) {
            defined.set();
            return Complex.valueOf(z);
        }
        return Complex.ZERO;
    }


//    @Override
//    public Matrix computeMatrix(double x, double y, double z) {
//        return computeMatrix(x, y, z, BooleanMarker.none());
//    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return z;
        }
        return 0;
    }

    //@Override
    public ComplexMatrix computeMatrix(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return Complex.valueOf(z).toMatrix();
        }
        return MathsBase.identityMatrix(1);
    }


    @Override
    public Expr mul(Domain domain) {
        return new Mul(new DoubleValue(1, Domain.FULLX), this);
    }

    @Override
    public Expr mul(double other) {
        return new Mul(new DoubleValue(other, Domain.FULLX), this);
    }

    @Override
    public Expr mul(Complex other) {
        return new Mul(other, this);
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing Z");
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing Z");
    }


    @Override
    public boolean isInvariant(Axis axis) {
        if (axis == Axis.Z) {
            return false;
        }
        return true;
    }


}
