//package net.thevpc.scholar.hadrumaths.symbolic;
//
//import net.thevpc.scholar.hadrumaths.*;
//import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;
//import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;
//import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
//
//import java.util.List;
//
//public class ComplexToComplexDefaults {
//
//    private ComplexToComplexDefaults() {
//    }
//
//    public static Complex[][][] computeComplex(ComplexToComplex f, Complex[] x, Complex[] y, Complex[] z, Domain d0, Out<Range> ranges) {
//        Range zrange = f.getDomain().intersect(d0).getDomainZ().range(z);
//        if (zrange != null) {
//            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
//            zrange.setDefined(def0);
//            Complex[][][] c = new Complex[z.length][y.length][x.length];
//            BooleanRef defined = BooleanMarker.ref();
//            for (int i = zrange.zmin; i <= zrange.zmax; i++) {
//                for (int j = zrange.ymin; j <= zrange.ymax; j++) {
//                    for (int k = zrange.xmin; k <= zrange.xmax; k++) {
//                        defined.unset();
//                        c[i][j][k] = f.computeComplex(x[k], y[j], z[i], defined);
//                        if (defined.get()) {
//                            def0.set(i, j, k);
//                        }
//                    }
//                }
//            }
//            ArrayUtils.fillArray3ZeroComplex(c, zrange);
//            if (ranges != null) {
//                ranges.set(zrange);
//            }
//            return c;
//        } else {
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
//        }
//    }
//
//
//    public static Complex[][][] computeComplexSimple(ComplexToComplexSimple f, Complex[] x, Complex[] y, Complex[] z, Domain d0, Out<Range> ranges) {
//        Range zrange = f.getDomain().intersect(d0).getDomainZ().range(z);
//        if (zrange != null) {
//            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
//            zrange.setDefined(def0);
//            Complex[][][] c = new Complex[z.length][y.length][x.length];
//            for (int i = zrange.zmin; i <= zrange.zmax; i++) {
//                for (int j = zrange.ymin; j <= zrange.ymax; j++) {
//                    for (int k = zrange.xmin; k <= zrange.xmax; k++) {
//                        c[i][j][k] = f.computeComplexSimple(x[k], y[j], z[i]);
//                    }
//                }
//            }
//            ArrayUtils.fillArray3ZeroComplex(c, zrange);
//            if (ranges != null) {
//                ranges.set(zrange);
//            }
//            return c;
//        } else {
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
//        }
//    }
//
//    public static Complex[][] computeComplex(ComplexToComplex f, Complex[] x, Complex[] y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
//        if (abcd != null) {
//            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
//            abcd.setDefined(def0);
//            Complex[][] c = new Complex[y.length][x.length];
//            BooleanRef defined = BooleanMarker.ref();
//            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
//                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                    defined.unset();
//                    c[j][k] = f.computeComplex(x[k], y[j], defined);
//                    if (defined.get()) {
//                        def0.set(j, k);
//                    }
//                }
//            }
//            ArrayUtils.fillArray2ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[][] computeComplexSimple(ComplexToComplexSimple f, Complex[] x, Complex[] y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
//        if (abcd != null) {
//            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
//            abcd.setDefined(def0);
//            Complex[][] c = new Complex[y.length][x.length];
//            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
//                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                    c[j][k] = f.computeComplexSimple(x[k], y[j]);
//                }
//                def0.setRange(j, abcd.xmin, abcd.xmax, true);
//            }
//            ArrayUtils.fillArray2ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[] computeComplex(ComplexToComplex f, Complex[] x, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[x.length];
//            BooleanRef defined = BooleanMarker.ref();
//            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                defined.unset();
//                c[k] = f.computeComplex(x[k], defined);
//                if (defined.get()) {
//                    def0.set(k);
//                }
//            }
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[] computeComplexSimple(ComplexToComplexSimple f, Complex[] x, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[x.length];
//            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                c[k] = f.computeComplexSimple(x[k]);
//            }
//            def0.setRange(abcd.xmin, abcd.xmax + 1);
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//
//    public static Complex[] computeComplex(ComplexToComplex f, Complex[] x, Complex y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new Complex[]{y});
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[x.length];
//            BooleanRef defined = BooleanMarker.ref();
//            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                defined.unset();
//                c[k] = f.computeComplex(x[k], y, defined);
//                if (defined.get()) {
//                    def0.set(k);
//                }
//            }
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[] computeComplexSimple(ComplexToComplexSimple f, Complex[] x, Complex y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new Complex[]{y});
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[x.length];
//            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
//                c[k] = f.computeComplexSimple(x[k], y);
//            }
//            def0.setRange(abcd.xmin, abcd.xmax + 1);
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[] computeComplex(ComplexToComplex f, Complex x, Complex[] y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new Complex[]{x}, y);
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[y.length];
//            BooleanRef defined = BooleanMarker.ref();
//            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
//                defined.unset();
//                c[k] = f.computeComplex(x, y[k], defined);
//                if (defined.get()) {
//                    def0.set(k);
//                }
//            }
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(y.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public static Complex[] computeComplexSimple(ComplexToComplexSimple f, Complex x, Complex[] y, Domain d0, Out<Range> ranges) {
//        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new Complex[]{x}, y);
//        if (abcd != null) {
//            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
//            abcd.setDefined(def0);
//            Complex[] c = new Complex[y.length];
//            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
//                c[k] = f.computeComplexSimple(x, y[k]);
//            }
//            def0.setRange(abcd.ymin, abcd.ymax + 1);
//            ArrayUtils.fillArray1ZeroComplex(c, abcd);
//            if (ranges != null) {
//                ranges.set(abcd);
//            }
//            return c;
//        } else {
//            Complex[] c = ArrayUtils.fillArray1Complex(y.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//    }
//
//    public interface ComplexToComplexAggregator1 extends ComplexToComplex {
//        Complex aggregateComplex(Complex a);
//    }
//
//    public interface ComplexToComplexAggregator2 extends ComplexToComplex {
//        Complex aggregateComplex(Complex a, Complex b);
//    }
//
//    public interface ComplexToComplexNormal extends ComplexToComplex {
//        default Complex[] computeComplex(Complex[] x, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplex(this, x, d0, ranges);
//        }
//
//        default Complex[] computeComplex(Complex[] x, Complex y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//        }
//
//        default Complex[] computeComplex(Complex x, Complex[] y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//        }
//
//        default Complex[][][] computeComplex(Complex[] x, Complex[] y, Complex[] z, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplex(this, x, y, z, d0, ranges);
//        }
//
//        default Complex[][] computeComplex(Complex[] x, Complex[] y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//        }
//    }
//
//    public interface ComplexToComplexSimple extends ComplexToComplex {
//        default Complex[] computeComplex(Complex[] x, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplexSimple(this, x, d0, ranges);
//        }
//
//        default Complex[] computeComplex(Complex[] x, Complex y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplexSimple(this, x, y, d0, ranges);
//        }
//
//        default Complex[] computeComplex(Complex x, Complex[] y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplexSimple(this, x, y, d0, ranges);
//        }
//
//        default Complex[][][] computeComplex(Complex[] x, Complex[] y, Complex[] z, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplexSimple(this, x, y, z, d0, ranges);
//        }
//
//        default Complex[][] computeComplex(Complex[] x, Complex[] y, Domain d0, Out<Range> ranges) {
//            return ComplexToComplexDefaults.computeComplexSimple(this, x, y, d0, ranges);
//        }
//
//        @Override
//        default Complex computeComplex(Complex x, BooleanMarker defined) {
//            if (contains(x)) {
//                defined.set();
//                return computeComplexSimple(x);
//            }
//            return Complex.ZERO;
//        }
//
//        Complex computeComplexSimple(Complex x);
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, BooleanMarker defined) {
//            if (contains(x, y)) {
//                defined.set();
//                return computeComplexSimple(x, y);
//            }
//            return Complex.ZERO;
//        }
//
//        Complex computeComplexSimple(Complex x, Complex y);
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, Complex z, BooleanMarker defined) {
//            if (contains(x, y, z)) {
//                defined.set();
//                return computeComplexSimple(x, y, z);
//            }
//            return Complex.ZERO;
//        }
//
//        Complex computeComplexSimple(Complex x, Complex y, Complex z);
//
//    }
//
//    public interface ComplexToComplexUnaryDC extends ComplexToComplexAggregator1 {
//
//        @Override
//        default Complex computeComplex(Complex x, BooleanMarker defined) {
//            if (contains(x)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a);
//            }
//            return Complex.ZERO;
//        }
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, BooleanMarker defined) {
//            if (contains(x, y)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, y, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a);
//            }
//            return Complex.ZERO;
//        }
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, Complex z, BooleanMarker defined) {
//            if (contains(x, y, z)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, y, z, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a);
//            }
//            return Complex.ZERO;
//        }
//    }
//
//    public interface ComplexToComplexDCDC extends ComplexToComplexAggregator2 {
//
//        @Override
//        default Complex computeComplex(Complex x, BooleanMarker defined) {
//            if (contains(x)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined2.reset();
//                Complex b = operands.get(1).toDC().computeComplex(x, defined2);
//                defined2.set();
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a, b);
//            }
//            return Complex.ZERO;
//        }
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, BooleanMarker defined) {
//            if (contains(x, y)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, y, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined2.reset();
//                Complex b = operands.get(1).toDC().computeComplex(x, y, defined2);
//                defined2.set();
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a, b);
//            }
//            return Complex.ZERO;
//        }
//
//        @Override
//        default Complex computeComplex(Complex x, Complex y, Complex z, BooleanMarker defined) {
//            if (contains(x, y, z)) {
//                List<Expr> operands = this.getChildren();
//                BooleanRef defined2 = BooleanMarker.ref();
//                Complex a = operands.get(0).toDC().computeComplex(x, y, z, defined2);
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined2.reset();
//                Complex b = operands.get(1).toDC().computeComplex(x, y, z, defined2);
//                defined2.set();
//                if (!defined2.get()) {
//                    return Complex.ZERO;
//                }
//                defined.set();
//                return aggregateComplex(a, b);
//            }
//            return Complex.ZERO;
//        }
//    }
//
//    public interface ComplexToComplexSimple3 extends ComplexToComplex {
//        Complex computeComplexSimple(double x, double y, double z);
//    }
//
////    public interface ComplexToComplexAggregator2 extends ComplexToComplex {
////        Complex computeComplexSimple(double x, double y);
////    }
////
////    public interface ComplexToComplexAggregator1 extends ComplexToComplex {
////        Complex computeComplexSimple(double x);
////    }
//}
