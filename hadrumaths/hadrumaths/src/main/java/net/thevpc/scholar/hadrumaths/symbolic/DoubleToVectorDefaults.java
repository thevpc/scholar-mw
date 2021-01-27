package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.List;

public class DoubleToVectorDefaults {
    private static final ComplexVector[] ZEROS = new ComplexVector[]{
            DefaultComplexVector.Column(new Complex[]{Complex.ZERO}).toReadOnly(),
            DefaultComplexVector.Column(new Complex[]{Complex.ZERO, Complex.ZERO}).toReadOnly(),
            DefaultComplexVector.Column(new Complex[]{Complex.ZERO, Complex.ZERO, Complex.ZERO}).toReadOnly()};

    private DoubleToVectorDefaults() {
    }

    public static ComplexVector Zero(int index) {
        return ZEROS[index];
    }

    public static ComplexVector Zero(DoubleToVector f) {
        return ZEROS[f.getComponentSize()];
    }

    public static ComplexVector[][][] evalVector(DoubleToVector f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> rangesxy = new Out<Range>();
        ComplexVector[][] r = f.evalVector(x, y, d0, rangesxy);
//        ExpressionsDebug.debug_check(r, rangesxy);
        Range zrange = f.getDomain().intersect(d0).range(z,y,z);
        ComplexVector ZERO = ZEROS[f.getComponentSize()];
        Range rangeOut = rangesxy.get();
        if (rangeOut != null) {
            ComplexVector[][][] m = new ComplexVector[z.length][][];
            if (zrange.xmin > 0) {
                for (int i = 0; i < zrange.xmin; i++) {
                    m[i] = ArrayUtils.fillArray2Vector(x.length, y.length, ZERO);
                }
            }
            for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                m[i] = ArrayUtils.copy(r);
            }
            if (zrange.xmax < z.length - 1) {
                for (int i = zrange.xmax + 1; i < z.length; i++) {
                    m[i] = ArrayUtils.fill(new ComplexVector[y.length][x.length], ZERO);
                }
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
            return ArrayUtils.fillArray3Vector(x.length, y.length, z.length, ZERO);
        }
    }

    public static ComplexVector[][] evalVector(DoubleToVector f, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
        ComplexVector ZERO = ZEROS[f.getComponentSize()];
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            ComplexVector[][] c = new ComplexVector[y.length][x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    defined.unset();
                    c[j][k] = f.evalVector(x[k], y[j], defined);
                    if (defined.get()) {
                        def0.set(j, k);
                    }
                }
            }
            ArrayUtils.fillArray2ZeroVector(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexVector[][] c = ArrayUtils.fillArray2Vector(x.length, y.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static ComplexVector[] evalVector(DoubleToVector f, double[] x, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
        ComplexVector ZERO = ZEROS[f.getComponentSize()];
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            ComplexVector[] c = new ComplexVector[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalVector(x[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroVector(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexVector[] c = ArrayUtils.fillArray1Vector(x.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }


    public static ComplexVector[] evalVector(DoubleToVector f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new double[]{y});
        ComplexVector ZERO = ZEROS[f.getComponentSize()];
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            ComplexVector[] c = new ComplexVector[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalVector(x[k], y, defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroVector(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexVector[] c = ArrayUtils.fillArray1Vector(x.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static ComplexVector[] evalVector(DoubleToVector f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new double[]{x}, y);
        ComplexVector ZERO = ZEROS[f.getComponentSize()];
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
            abcd.setDefined(def0);
            ComplexVector[] c = new ComplexVector[y.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
                defined.unset();
                c[k] = f.evalVector(x, y[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroVector(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexVector[] c = ArrayUtils.fillArray1Vector(y.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static int widestComponentSize(List<Expr> all) {
        int s = 1;
        for (Expr subExpression : all) {
            int s2 = subExpression.toDV().getComponentSize();
            if (s2 > s) {
                s = s2;
            }
        }
        return s;
    }

    public interface DoubleToVectorAggregator1 extends DoubleToVector {
        ComplexVector aggregateVector(ComplexVector v);
    }

    public interface DoubleToVectorUnaryDV extends DoubleToVectorAggregator1 {
        default int getComponentSize() {
            return DoubleToVectorDefaults.widestComponentSize(getChildren());
        }

        default Expr getComponent(Axis a) {
            List<Expr> operands = this.getChildren();
            return newInstance(operands.get(0).toDV().getComponent(a));
        }

        default ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateVector(a);
            }
            return zero;
        }

        default ComplexVector evalVector(double x, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateVector(a);
            }
            return zero;
        }

        default ComplexVector evalVector(double x, double y, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateVector(a);
            }
            return zero;
        }
    }

    public interface DoubleToVectorDVDC extends DoubleToVector {
        default int getComponentSize() {
            return DoubleToVectorDefaults.widestComponentSize(getChildren());
        }

        default Expr getComponent(Axis a) {
            List<Expr> operands = this.getChildren();
            return newInstance(operands.get(0).toDV().getComponent(a), operands.get(1));
        }

        default ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                Complex b = operands.get(1).toDC().evalComplex(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        default ComplexVector evalVector(double x, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                Complex b = operands.get(1).toDC().evalComplex(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        ComplexVector evalVectorSimple(ComplexVector dv, Complex dc);

        default ComplexVector evalVector(double x, double y, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                Complex b = operands.get(1).toDC().evalComplex(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }
    }

    public interface DoubleToVectorDVDV extends DoubleToVector {
        default int getComponentSize() {
            return DoubleToVectorDefaults.widestComponentSize(getChildren());
        }

        default Expr getComponent(Axis a) {
            List<Expr> operands = this.getChildren();
            return newInstance(operands.get(0).toDV().getComponent(a), operands.get(1).toDV().getComponent(a));
        }

        default ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        default ComplexVector evalVector(double x, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        ComplexVector evalVectorSimple(ComplexVector dv, ComplexVector dc);

        default ComplexVector evalVector(double x, double y, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }
    }

    public interface DoubleToVectorDCDV extends DoubleToVector {
        default int getComponentSize() {
            return DoubleToVectorDefaults.widestComponentSize(getChildren());
        }

        default Expr getComponent(Axis a) {
            List<Expr> operands = this.getChildren();
            return newInstance(operands.get(0), operands.get(1).toDV().getComponent(a));
        }

        default ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        default ComplexVector evalVector(double x, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }

        ComplexVector evalVectorSimple(Complex dc, ComplexVector dv);

        default ComplexVector evalVector(double x, double y, BooleanMarker defined) {
            ComplexVector zero = DoubleToVectorDefaults.Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexVector b = operands.get(1).toDV().evalVector(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalVectorSimple(a, b);
            }
            return zero;
        }
    }

    public static ExprType getNarrowType(DoubleToVector me) {
        if(me.getComponentSize()==1){
            return me.getComponent(Axis.X).getNarrowType();
        }
        return me.getType();
    }
}
