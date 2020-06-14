package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.HashSet;
import java.util.List;

public class DoubleToMatrixDefaults {

    private DoubleToMatrixDefaults() {
    }

    public static ComplexMatrix Zero(DoubleToMatrix f) {
        return Maths.zerosMatrix(f.getComponentDimension());
    }

    public static ComplexMatrix Zero(ComponentDimension f) {
        return Maths.zerosMatrix(f);
    }

    public static ComplexMatrix[][][] evalMatrix(DoubleToMatrix f, double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> rangesxy = new Out<Range>();
        ComplexMatrix[][] r = f.evalMatrix(x, y, d0, rangesxy);
//        ExpressionsDebug.debug_check(r, rangesxy);
        Range zrange = f.getDomain().intersect(d0).range(x, y, z);
        ComplexMatrix ZERO = Maths.zerosMatrix(f.getComponentDimension());
        Range rangeOut = rangesxy.get();
        if (rangeOut != null) {
            ComplexMatrix[][][] m = new ComplexMatrix[z.length][][];
            if (zrange.xmin > 0) {
                for (int i = 0; i < zrange.xmin; i++) {
                    m[i] = ArrayUtils.fillArray2Matrix(x.length, y.length, ZERO);
                }
            }
            for (int i = zrange.xmin; i <= zrange.xmax; i++) {
                m[i] = ArrayUtils.copy(r);
            }
            if (zrange.xmax < z.length - 1) {
                for (int i = zrange.xmax + 1; i < z.length; i++) {
                    m[i] = ArrayUtils.fill(new ComplexMatrix[y.length][x.length], ZERO);
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
            return ArrayUtils.fillArray3Matrix(x.length, y.length, z.length, ZERO);
        }
    }

    public static ComplexMatrix[][] evalMatrix(DoubleToMatrix f, double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, y);
        ComplexMatrix ZERO = Maths.zerosMatrix(f.getComponentDimension());
        if (abcd != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            abcd.setDefined(def0);
            ComplexMatrix[][] c = new ComplexMatrix[y.length][x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                    defined.unset();
                    c[j][k] = f.evalMatrix(x[k], y[j], defined);
                    if (defined.get()) {
                        def0.set(j, k);
                    }
                }
            }
            ArrayUtils.fillArray2ZeroMatrix(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexMatrix[][] c = ArrayUtils.fillArray2Matrix(x.length, y.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static ComplexMatrix[] evalMatrix(DoubleToMatrix f, double[] x, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x);
        ComplexMatrix ZERO = Maths.zerosMatrix(f.getComponentDimension());
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            ComplexMatrix[] c = new ComplexMatrix[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalMatrix(x[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroMatrix(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexMatrix[] c = ArrayUtils.fillArray1Matrix(x.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }


    public static ComplexMatrix[] evalMatrix(DoubleToMatrix f, double[] x, double y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(x, new double[]{y});
        ComplexMatrix ZERO = Maths.zerosMatrix(f.getComponentDimension());
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(x.length);
            abcd.setDefined(def0);
            ComplexMatrix[] c = new ComplexMatrix[x.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                defined.unset();
                c[k] = f.evalMatrix(x[k], y, defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroMatrix(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexMatrix[] c = ArrayUtils.fillArray1Matrix(x.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static ComplexMatrix[] evalMatrix(DoubleToMatrix f, double x, double[] y, Domain d0, Out<Range> ranges) {
        Range abcd = (d0 == null ? Domain.FULLX : d0).range(new double[]{x}, y);
        ComplexMatrix ZERO = Maths.zerosMatrix(f.getComponentDimension());
        if (abcd != null) {
            BooleanArray1 def0 = BooleanArrays.newArray(y.length);
            abcd.setDefined(def0);
            ComplexMatrix[] c = new ComplexMatrix[y.length];
            BooleanRef defined = BooleanMarker.ref();
            for (int k = abcd.ymin; k <= abcd.ymax; k++) {
                defined.unset();
                c[k] = f.evalMatrix(x, y[k], defined);
                if (defined.get()) {
                    def0.set(k);
                }
            }
            ArrayUtils.fillArray1ZeroMatrix(c, abcd, ZERO);
            if (ranges != null) {
                ranges.set(abcd);
            }
            return c;
        } else {
            ComplexMatrix[] c = ArrayUtils.fillArray1Matrix(y.length, ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public static String getMatrixExpressionTitleByChildren(Expr e, int row, int col) {
        HashSet<String> titles = new HashSet<String>();
        for (Expr expression : e.getChildren()) {
            switch (expression.getType()) {
                case DOUBLE_CMATRIX: {
                    titles.add(expression.toDM().getComponentTitle(row, col));
                }
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

    public static DoubleToComplex toDC(DoubleToMatrix me) {
        ComponentDimension c = me.getComponentDimension();
        if (c.is(1, 1)) {
            return me.getComponent(0, 0).toDC();
        }
        return (DoubleToComplex) ExprDefaults.narrow(me, ExprType.DOUBLE_COMPLEX);
    }

    public static Expr getComponentByChildren(DoubleToMatrix me, int row, int col) {
        ComponentDimension dim = me.getComponentDimension();
        if (dim.is(1, 1) && (row != col || col != 0)) {
            return Maths.DZEROXY;
        }
        List<Expr> expressions = me.getChildren();
        Expr[] inner = new Expr[expressions.size()];
        for (int i = 0; i < inner.length; i++) {
            inner[i] = expressions.get(i).toDM().getComponent(row, col);
        }
        return me.newInstance(inner);
    }

    public static ExprType getNarrowType(DoubleToMatrix me) {
        if (me.getComponentDimension().is(1, 1)) {
            return me.getComponent(0, 0).getNarrowType();
        }
        return me.getType();
    }

    public interface DoubleToMatrixAggregator1 extends DoubleToMatrix {
        ComplexMatrix aggregateMatrix(ComplexMatrix m);

    }

    public interface DoubleToMatrixUnaryDM extends DoubleToMatrixAggregator1 {
        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateMatrix(a);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateMatrix(a);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.aggregateMatrix(a);
            }
            return zero;
        }

        default Expr getComponent(int row, int col) {
            return DoubleToMatrixDefaults.getComponentByChildren(this, row, col);
        }
    }

    public interface DoubleToMatrixDMDC extends DoubleToMatrix {
        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        ComplexMatrix evalMatrixSimple(ComplexMatrix dv, Complex dc);

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, z, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        @Override
        default Expr getComponent(int row, int col) {
            List<Expr> operands = this.getChildren();
            return newInstance(operands.get(0).toDM().getComponent(row, col), operands.get(1));
        }
    }

    public interface DoubleToMatrixDMDV extends DoubleToMatrix {

        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        ComplexMatrix evalMatrixSimple(ComplexMatrix dv, ComplexVector dc);

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, z, defined2);
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
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default Expr getComponent(int row, int col) {
            return DoubleToMatrixDefaults.getComponentByChildren(this, row, col);
        }
    }

    public interface DoubleToMatrixDVDM extends DoubleToMatrix {
        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        ComplexMatrix evalMatrixSimple(ComplexVector dv, ComplexMatrix dc);

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexVector a = operands.get(0).toDV().evalVector(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default Expr getComponent(int row, int col) {
            return DoubleToMatrixDefaults.getComponentByChildren(this, row, col);
        }
    }

    public interface DoubleToMatrixDMDM extends DoubleToMatrix {
        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        ComplexMatrix evalMatrixSimple(ComplexMatrix dv, ComplexMatrix dc);

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                ComplexMatrix a = operands.get(0).toDM().evalMatrix(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default Expr getComponent(int row, int col) {
            return DoubleToMatrixDefaults.getComponentByChildren(this, row, col);
        }
    }

    public interface DoubleToMatrixDCDM extends DoubleToMatrix {
        default ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        ComplexMatrix evalMatrixSimple(Complex dc, ComplexMatrix dv);

        default ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
            ComplexMatrix zero = Zero(this);
            if (this.contains(x, y, z)) {
                List<Expr> operands = this.getChildren();
                BooleanRef defined2 = BooleanMarker.ref();
                Complex a = operands.get(0).toDC().evalComplex(x, y, z, defined2);
                if (!defined2.get()) {
                    return zero;
                }
                defined2.reset();
                ComplexMatrix b = operands.get(1).toDM().evalMatrix(x, y, z, defined2);
                defined2.set();
                if (!defined2.get()) {
                    return zero;
                }
                defined.set();
                return this.evalMatrixSimple(a, b);
            }
            return zero;
        }

        default Expr getComponent(int row, int col) {
            return DoubleToMatrixDefaults.getComponentByChildren(this, row, col);
        }
    }
}
