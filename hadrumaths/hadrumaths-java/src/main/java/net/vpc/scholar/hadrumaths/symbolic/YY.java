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
public class YY extends AxisFunction implements Cloneable {
    private static final long serialVersionUID = 1L;

    public YY(Domain domain) {
        super(domain, "Y");
    }

    @Override
    public String getFunctionName() {
        return "Y";
    }


    @Override
    public Axis getAxis() {
        return Axis.Y;
    }

    @Override
    public Expr newInstance(Expr... arguments) {
        Expr xx = new YY(getDomain());
        xx= Any.copyProperties(this, xx);
        return xx;
    }

    @Override
    public Complex computeComplex(double x, OutBoolean defined) {
        throw new IllegalArgumentException("Missing y");
    }

    //    @Override
//    public Domain getDomain() {
//        return getDomain().getDomainY();
//    }





    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (nonNullRanges != null) {
            Complex[][] cc = new Complex[y.length][x.length];
            BooleanArray2 def = BooleanArrays.newArray(y.length,x.length);
            nonNullRanges.setDefined(def);
            ArrayUtils.fillArray2ZeroComplex(cc, nonNullRanges);
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                Complex v = Complex.valueOf(y[j]);
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (nonNullRanges != null) {
            Complex[][][] cc = new Complex[z.length][y.length][x.length];
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);
            nonNullRanges.setDefined(def);
            ArrayUtils.fillArray3ZeroComplex(cc, nonNullRanges);
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                Complex v = Complex.valueOf(y[j]);
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                    for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
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
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                double v = y[j];
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                double v = y[j];
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                    for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
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
        throw new ClassCastException("Missing Y");
    }

    @Override
    public Expr clone() {
        YY cloned = (YY) super.clone();
        return cloned;
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range nonNullRanges = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (nonNullRanges != null) {
            Matrix[][] cc = new Matrix[y.length][x.length];
            ArrayUtils.fillArray2ZeroMatrix(cc, nonNullRanges, 1, 1);
            BooleanArray2 def = BooleanArrays.newArray(y.length,x.length);
            nonNullRanges.setDefined(def);
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                Matrix v = Complex.valueOf(y[j]).toMatrix();
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
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
            BooleanArray3 def = BooleanArrays.newArray(z.length,y.length,x.length);;
            nonNullRanges.setDefined(def);
            for (int j = nonNullRanges.ymin; j <= nonNullRanges.ymax; j++) {
                Matrix v = Complex.valueOf(y[j]).toMatrix();
                for (int k = nonNullRanges.xmin; k <= nonNullRanges.xmax; k++) {
                    for (int t = nonNullRanges.zmin; t <= nonNullRanges.zmax; t++) {
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
        return 2;
    }

    @Override
    public Complex computeComplex(double x, double y, OutBoolean defined) {
        if (contains(x, y)) {
            defined.set();
            return Complex.valueOf(y);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, double y, OutBoolean defined) {
        if (contains(x, y)) {
            defined.set();
            return y;
        }
        return 0;
    }
    @Override
    public Complex computeComplex(double x, double y, double z,OutBoolean defined) {
        if (contains(x, y, z)) {
            defined.set();
            return Complex.valueOf(y);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, double y, double z,OutBoolean defined) {
        if (contains(x, y, z)) {
            defined.set();
            return y;
        }
        return 0;
    }

    @Override
    public Expr mul(Domain domain) {
        return new Linear(0,1,0,domain);
    }

    @Override
    public Expr mul(double other) {
        return new Linear(0,other,0,Domain.FULLXY);
    }

    @Override
    public Expr mul(Complex other) {
        if(other.isReal()){
            return mul(other.toDouble());
        }
        return new Mul(other,this);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Y");
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("Missing Y");
    }

    @Override
    public double computeDouble(double x, OutBoolean defined) {
        throw new IllegalArgumentException("Missing Y");
    }

}
