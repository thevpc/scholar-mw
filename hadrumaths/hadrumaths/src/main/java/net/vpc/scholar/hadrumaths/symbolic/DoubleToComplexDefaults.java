package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.vpc.scholar.hadrumaths.symbolic.conv.Real;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.List;

public class DoubleToComplexDefaults {

    private DoubleToComplexDefaults() {
    }

    public static Complex[][][] evalComplex(DoubleToComplex f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range zrange = f.getDomain().intersect(d0).range(x,y,z);
        if (zrange != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            zrange.setDefined(def0);
            Complex[][][] c = new Complex[z.length][y.length][x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int i = zrange.zmin; i <= zrange.zmax; i++) {
                for (int j = zrange.ymin; j <= zrange.ymax; j++) {
                    for (int k = zrange.xmin; k <= zrange.xmax; k++) {
                        defined.unset();
                        c[i][j][k] = f.evalComplex(x[k], y[j], z[i], defined);
                        if (defined.get()) {
                            def0.set(i, j, k);
                        }
                    }
                }
            }
            ArrayUtils.fillArray3ZeroComplex(c, zrange);
            if (ranges != null) {
                ranges.set(zrange);
            }
            return c;
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
        }
    }


    public static Complex[][][] evalComplexSimple(DoubleToComplexSimple f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Range zrange = f.getDomain().intersect(d0).range(x,y,z);
        if (zrange != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            zrange.setDefined(def0);
            Complex[][][] c = new Complex[z.length][y.length][x.length];
            for (int i = zrange.zmin; i <= zrange.zmax; i++) {
                for (int j = zrange.ymin; j <= zrange.ymax; j++) {
                    for (int k = zrange.xmin; k <= zrange.xmax; k++) {
                        c[i][j][k] = f.evalComplexSimple(x[k], y[j], z[i]);
                    }
                }
            }
            ArrayUtils.fillArray3ZeroComplex(c, zrange);
            if (ranges != null) {
                ranges.set(zrange);
            }
            return c;
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
        }
    }

    public static Complex[][] evalComplex(DoubleToComplex f, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            Complex[][] c = new Complex[y.length][x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    defined.unset();
                    c[j][k] = f.evalComplex(x[k], y[j], defined);
                    if (defined.get()) {
                        def0.set(j, k);
                    }
                }
            }
            ArrayUtils.fillArray2ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[][] evalComplexSimple(DoubleToComplexSimple f, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            Complex[][] c = new Complex[y.length][x.length];
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    c[j][k] = f.evalComplexSimple(x[k], y[j]);
                }
                def0.setRange(j, abcd.xmin, abcd.xmax, true);
            }
            ArrayUtils.fillArray2ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[] evalComplex(DoubleToComplex f, double[] x, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalComplex(x[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[] evalComplexSimple(DoubleToComplexSimple f, double[] x, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                c[k] = f.evalComplexSimple(x[k]);
            }
            def0.setRange(abcd.xmin, abcd.xmax + 1);
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }


    public static Complex[] evalComplex(DoubleToComplex f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new double[]{y});
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalComplex(x[k], y, defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[] evalComplexSimple(DoubleToComplexSimple f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new double[]{y});
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[x.length];
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                c[k] = f.evalComplexSimple(x[k], y);
            }
            def0.setRange(abcd.xmin, abcd.xmax + 1);
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[] evalComplex(DoubleToComplex f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new double[]{x}, y);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[y.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
                defined.unset();
                c[k] = f.evalComplex(x, y[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static Complex[] evalComplexSimple(DoubleToComplexSimple f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new double[]{x}, y);
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
            abcd.setDefined(def0);
            Complex[] c = new Complex[y.length];
            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
                c[k] = f.evalComplexSimple(x, y[k]);
            }
            def0.setRange(abcd.ymin, abcd.ymax + 1);
            ArrayUtils.fillArray1ZeroComplex(c, abcd);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public interface DoubleToComplexAggregator1 extends DoubleToComplex {
        Complex aggregateComplex(Complex a);
    }

    public interface DoubleToComplexAggregator2 extends DoubleToComplex {
        Complex aggregateComplex(Complex a, Complex b);
    }

    public interface DoubleToComplexNormal extends DoubleToComplex {
        default Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplex(this, x, d0, ranges);
        }

        default Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
        }

        default Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
        }

        default Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplex(this, x, y, z, d0, ranges);
        }

        default Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
        }
    }

    public interface DoubleToComplexSimple extends DoubleToComplex {
        default Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplexSimple(this, x, d0, ranges);
        }

        default Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplexSimple(this, x, y, d0, ranges);
        }

        default Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplexSimple(this, x, y, d0, ranges);
        }

        default Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplexSimple(this, x, y, z, d0, ranges);
        }

        default Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
            return DoubleToComplexDefaults.evalComplexSimple(this, x, y, d0, ranges);
        }

        @Override
        default Complex evalComplex(double x, BooleanMarker defined) {
            if (contains(x)) {
                defined.set();
                return evalComplexSimple(x);
            }
            return Complex.ZERO;
        }

        Complex evalComplexSimple(double x);

        @Override
        default Complex evalComplex(double x, double y, BooleanMarker defined) {
            if (contains(x, y)) {
                defined.set();
                return evalComplexSimple(x, y);
            }
            return Complex.ZERO;
        }

        Complex evalComplexSimple(double x, double y);

        @Override
        default Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
            if (contains(x, y, z)) {
                defined.set();
                return evalComplexSimple(x, y, z);
            }
            return Complex.ZERO;
        }

        Complex evalComplexSimple(double x, double y, double z);

    }

    public interface DoubleToComplexUnaryDC extends DoubleToComplexAggregator1 {

        default DoubleToDouble getRealDD() {
            List<Expr> operands = this.getChildren();
            if (operands.get(0).isZero()) {
                return Maths.DZEROX;
            }
            return new Real(this);
        }

        default DoubleToDouble getImagDD() {
            List<Expr> operands = this.getChildren();
            if (operands.get(0).isZero()) {
                return Maths.DZEROX;
            }
            return new Imag(this);
        }

        @Override
        default Complex evalComplex(double x, BooleanMarker defined) {
            if (contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a);
            }
            return Complex.ZERO;
        }

        @Override
        default Complex evalComplex(double x, double y, BooleanMarker defined) {
            if (contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a);
            }
            return Complex.ZERO;
        }

        @Override
        default Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
            if (contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, z, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a);
            }
            return Complex.ZERO;
        }
    }

    public interface DoubleToComplexDCDC extends DoubleToComplexAggregator2 {

        default DoubleToDouble getRealDD() {
            List<Expr> operands = this.getChildren();
            if (ExprDefaults.is(ExprType.DOUBLE_DOUBLE, operands)) {
                return new Real(this);
            }
            if (operands.get(0).isNarrow(ExprType.DOUBLE_COMPLEX) && operands.get(1).isNarrow(ExprType.DOUBLE_COMPLEX)) {
                DoubleToComplex a = operands.get(0).toDC();
                DoubleToComplex b = operands.get(1).toDC();
                boolean aReal = a.getImagDD().isZero();
                boolean aImag = a.getRealDD().isZero();
                boolean bReal = b.getImagDD().isZero();
                boolean bImag = b.getRealDD().isZero();
                if (aReal && bReal) {
                    return new Real(this);
                } else if (aImag && bImag) {
                    return new Real(this);
                } else if ((aReal && bImag) || (bReal && aImag)) {
                    return Maths.DZEROXY;
                }
            }
            return new Real(toDC());
        }

        default DoubleToDouble getImagDD() {
            List<Expr> operands = this.getChildren();
            if (ExprDefaults.is(ExprType.DOUBLE_DOUBLE, operands)) {
                return Maths.DZERO(getDomain().getDimension());
            }
            if (operands.get(0).isNarrow(ExprType.DOUBLE_COMPLEX) && operands.get(1).isNarrow(ExprType.DOUBLE_COMPLEX)) {
                DoubleToComplex a = operands.get(0).toDC();
                DoubleToComplex b = operands.get(1).toDC();
                boolean aReal = a.getImagDD().isZero();
                boolean aImag = a.getRealDD().isZero();
                boolean bReal = b.getImagDD().isZero();
                boolean bImag = b.getRealDD().isZero();
                if ((aReal && bReal) || (aImag && bImag)) {
                    return Maths.DZEROXY;
                } else if ((aReal && bImag) || (bReal && aImag)) {
                    return new Real(this);
                }
            }
            return new Imag(toDC());
        }

        @Override
        default Complex evalComplex(double x, BooleanMarker defined) {
            if (contains(x)) {
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = getChild(0).toDC().evalComplex(x, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined2.reset();
                Complex b = getChild(1).toDC().evalComplex(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a, b);
            }
            return Complex.ZERO;
        }

        @Override
        default Complex evalComplex(double x, double y, BooleanMarker defined) {
            if (contains(x, y)) {
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = getChild(0).toDC().evalComplex(x, y, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined2.reset();
                Complex b = getChild(1).toDC().evalComplex(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a, b);
            }
            return Complex.ZERO;
        }

        @Override
        default Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
            if (contains(x, y, z)) {
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = getChild(0).toDC().evalComplex(x, y, z, defined2);
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined2.reset();
                Complex b = getChild(1).toDC().evalComplex(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return Complex.ZERO;
                }
                defined.set();
                return aggregateComplex(a, b);
            }
            return Complex.ZERO;
        }
    }

    public interface DoubleToComplexSimple3 extends DoubleToComplex {
        Complex evalComplexSimple(double x, double y, double z);
    }

//    public interface DoubleToComplexAggregator2 extends DoubleToComplex {
//        Complex evalComplexSimple(double x, double y);
//    }
//
//    public interface DoubleToComplexAggregator1 extends DoubleToComplex {
//        Complex evalComplexSimple(double x);
//    }
}
