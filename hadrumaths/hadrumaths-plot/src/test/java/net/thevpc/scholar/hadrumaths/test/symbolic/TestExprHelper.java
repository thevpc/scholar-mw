package net.thevpc.scholar.hadrumaths.test.symbolic;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.HashSet;

public class TestExprHelper {
    public static void checkNarrowYes(Expr e, ExprType... ts) {
        HashSet<ExprType> yes = new HashSet<>(Arrays.asList(ts));
        for (ExprType value : ExprType.values()) {
            boolean isYes = yes.contains(value);
            Assertions.assertEquals(isYes, e.isNarrow(value), "Expected " + e.getClass().getSimpleName() + ".isNarrow(" + value + ")=" + isYes);
        }
    }

    public static void checkNarrowNo(Expr e, ExprType... ts) {
        HashSet<ExprType> no = new HashSet<>(Arrays.asList(ts));
        for (ExprType value : ExprType.values()) {
            boolean yes = !no.contains(value);
            Assertions.assertEquals(yes, e.isNarrow(value), "Expected " + e.getClass().getSimpleName() + ".isNarrow(" + value + ")=" + (yes));
        }
    }

    public static void checkDDApply(Expr e, double[] xs, double[] vals) {
        double[] vals2 = e.toDD().evalDouble(xs);
        Assertions.assertArrayEquals(vals, vals2);
        for (int i = 0; i < xs.length; i++) {
            double x = xs[i];
            double v = vals[i];
            Assertions.assertEquals(v, e.toDD().apply(x));
        }
    }

    public static void checkDDApply(Expr e, double[] xs, double[] ys, double[][] vals) {
        double[][] vals2 = e.toDD().evalDouble(xs, ys);
        Assertions.assertArrayEquals(vals, vals2);
        for (int yi = 0; yi < ys.length; yi++) {
            for (int xi = 0; xi < xs.length; xi++) {
                double x = xs[xi];
                double y = ys[xi];
                double v = vals[yi][xi];
                Assertions.assertEquals(v, e.toDD().apply(x, y));
            }
        }
    }

    public static void checkDDApply(Expr e, double[] xs, double[] ys, double[] zs, double[][][] vals) {
        double[][][] vals2 = e.toDD().evalDouble(xs, ys, zs);
        Assertions.assertArrayEquals(vals, vals2);
        for (int zi = 0; zi < ys.length; zi++) {
            for (int yi = 0; yi < ys.length; yi++) {
                for (int xi = 0; xi < xs.length; xi++) {
                    double x = xs[xi];
                    double y = ys[yi];
                    double z = zs[zi];
                    double v = vals[zi][yi][xi];
                    Assertions.assertEquals(v, e.toDD().apply(x, y, z));
                }
            }
        }
    }

    public static void checkDCApply(Expr e, double[] xs, Complex[] vals) {
        Complex[] vals2 = e.toDC().evalComplex(xs);
        Assertions.assertArrayEquals(vals, vals2);
        for (int i = 0; i < xs.length; i++) {
            double x = xs[i];
            Complex v = vals[i];
            Assertions.assertEquals(v, e.toDC().apply(x));
        }
    }

    public static void checkDCApply(Expr e, double[] xs, double[] ys, Complex[][] vals) {
        Complex[][] vals2 = e.toDC().evalComplex(xs, ys);
        Assertions.assertArrayEquals(vals, vals2);
        for (int yi = 0; yi < ys.length; yi++) {
            for (int xi = 0; xi < xs.length; xi++) {
                double x = xs[xi];
                double y = ys[xi];
                Complex v = vals[yi][xi];
                Assertions.assertEquals(v, e.toDC().apply(x, y));
            }
        }
    }

    public static void checkDCApply(Expr e, double[] xs, double[] ys, double[] zs, Complex[][][] vals) {
        Complex[][][] vals2 = e.toDC().evalComplex(xs, ys, zs);
        Assertions.assertArrayEquals(vals, vals2);
        for (int zi = 0; zi < ys.length; zi++) {
            for (int yi = 0; yi < ys.length; yi++) {
                for (int xi = 0; xi < xs.length; xi++) {
                    double x = xs[xi];
                    double y = ys[yi];
                    double z = zs[zi];
                    Complex v = vals[zi][yi][xi];
                    Assertions.assertEquals(v, e.toDC().apply(x, y, z));
                }
            }
        }
    }

    public static void checkIs(Expr e, ExprType t, ExprType t2) {
        Assertions.assertEquals(t, e.getType(), "Invalid Def Type");
        Assertions.assertEquals(t2, e.getNarrowType(), "Invalid Narrow");
        Assertions.assertTrue(e.is(t));
        Assertions.assertTrue(e.is(t.out().nbr()));
        Assertions.assertTrue(e.is(t.out().dim()));
        Assertions.assertEquals(t, e.getType());
        switch (t) {
            case DOUBLE_EXPR: {
                Assertions.assertTrue(e instanceof DoubleValue, e.getClass().getSimpleName() + " :: Expression is marked as DOUBLE but does not implement DoubleValue");
//                Assertions.assertTrue(e instanceof DoubleToDouble,e.getClass().getSimpleName()+" :: Expression is marked as DOUBLE but does not implement DoubleToDouble");
                break;
            }
            case COMPLEX_EXPR: {
                Assertions.assertTrue(e instanceof ComplexValue, e.getClass().getSimpleName() + " :: Expression is marked as COMPLEX but does not implement ComplexValue");
//                Assertions.assertTrue(e instanceof DoubleToComplex,e.getClass().getSimpleName()+" :: Expression is marked as COMPLEX but does not implement DoubleToComplex");
                break;
            }
            case DOUBLE_DOUBLE: {
                Assertions.assertTrue(e instanceof DoubleToDouble, e.getClass().getSimpleName() + " :: Expression is marked as DOUBLE_DOUBLE but does not implement DoubleToDouble");
                break;
            }
            case DOUBLE_COMPLEX: {
                Assertions.assertTrue(e instanceof DoubleToComplex, e.getClass().getSimpleName() + " :: Expression is marked as DOUBLE_COMPLEX but does not implement DoubleToComplex");
                break;
            }
            case DOUBLE_CVECTOR: {
                Assertions.assertTrue(e instanceof DoubleToVector, e.getClass().getSimpleName() + " :: Expression is marked as DOUBLE_CVECTOR but does not implement DoubleToVector");
                break;
            }
            case DOUBLE_CMATRIX: {
                Assertions.assertTrue(e instanceof DoubleToMatrix, e.getClass().getSimpleName() + " :: Expression is marked as DOUBLE_CMATRIX but does not implement DoubleToMatrix");
                break;
            }
        }
    }
}
