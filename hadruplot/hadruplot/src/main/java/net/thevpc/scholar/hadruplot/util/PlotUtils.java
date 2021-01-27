package net.thevpc.scholar.hadruplot.util;

import net.thevpc.scholar.hadruplot.extension.PlotNumbers;
import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class PlotUtils {
    public static double[] toValidOneDimArray(double[] a) {
        if (a == null) {
            return new double[0];
        }
        return a;
    }

    public static double[][] toValidTwoDimArray(double[][] a) {
        if (a == null) {
            return new double[0][];
        }
        for (int i = 0; i < a.length; i++) {
            double[] d = a[i];
            if (d == null) {
                a[i] = new double[0];
            }
        }
        return a;
    }

    public static double[] toValidOneDimArray(double[][] a) {
        return toValidOneDimArray(a, 0);
    }

    public static double[] toValidOneDimArray(double[][] a, int expectedSize) {
        if (isNullOrEmptyOrHasNullElements(a)) {
            if (expectedSize <= 0) {
                return new double[0];
            }
            return ArrayUtils.dsteps(1, expectedSize, 1.0);
        }
        return a[0];
    }

    public static double[] toValidOneDimArray(double[] a, int expectedSize) {
        if (isNullOrEmptyOrHasNullElements(a)) {
            if (expectedSize <= 0) {
                return new double[0];
            }
            return ArrayUtils.dsteps(1, expectedSize, 1.0);
        }
        if (a.length<expectedSize) {
            return ArrayUtils.dsteps(1, expectedSize, 1.0);
        }
        return a;
    }

    public static boolean isNullOrEmptyOrHasNullElements(double[][] a) {
        if (a == null) {
            return true;
        }
        if (a.length == 0) {
            return true;
        }
        for (double[] v : a) {
            if (v == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrEmptyOrHasNullElements(double[] a) {
        if (a == null) {
            return true;
        }
        if (a.length == 0) {
            return true;
        }
        return false;
    }

    public static Object[][] toValidTwoDimArray(Object[][] a) {
        if (a == null) {
            return new Object[0][];
        }
        for (int i = 0; i < a.length; i++) {
            Object[] d = a[i];
            if (d == null) {
                a[i] = new Object[0];
            }
            for (int j = 0; j < a[i].length; j++) {
                Object c = a[i][j];
                if (c == null) {
                    a[i][j] = 0.;
                }
            }
        }
        return a;
    }


    public static double[][] rotateValuesLeft(double[][] A) {
        double[][] newVals = new double[A[0].length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                newVals[j][A.length - 1 - i] = A[i][j];
            }
        }
        return newVals;
    }

    public static double[][] rotateValuesRight(double[][] A) {
        double[][] newVals = new double[A[0].length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                newVals[A[i].length - 1 - j][i] = A[i][j];
            }
        }
        return newVals;
    }

    public static double[][] flipHorizontally(double[][] A) {
        double[][] newVals = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                newVals[A.length - 1 - i][j] = A[i][j];
            }
        }
        return newVals;
    }

    public static double[][] flipVertically(double[][] A) {
        double[][] newVals = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                newVals[i][A[i].length - 1 - j] = A[i][j];
            }
        }
        return newVals;
    }

    /**
     * This is a work around if jdk 8 is not available
     *
     * @param d
     * @return
     */
    public static boolean isDoubleFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    public static double toDouble(Object obj) {
        if (obj == null) {
            return Double.NaN;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new IllegalArgumentException("Not a Double");
    }

    public static boolean isSameCouple(String a, String b, String c, String d) {
        if ((a.equals(c) && b.equals(d)) || (a.equals(d) && b.equals(c))) {
            return true;
        }
        return false;
    }

    public static Object numbersMin(Object a, Object b) {
        int x = PlotConfigManager.Numbers.compare(a, b);
        if (x < 0) {
            return a;
        } else {
            return b;
        }
    }

    public static Object numbersMax(Object a, Object b) {
        int x = PlotConfigManager.Numbers.compare(a, b);
        if (x < 0) {
            return b;
        } else {
            return a;
        }
    }

    public static ToDoubleFunction<Object> resolveDoubleConverter(Object[][][] c) {
        ToDoubleFunction<Object> real = PlotDoubleConverter.intern(PlotDoubleConverter.REAL);
        if (c == null) {
            return real;
        }

        for (Object[][] d : c) {
            ToDoubleFunction<Object> d2 = resolveDoubleConverter(d);
            if (d2 != real) {
                return d2;
            }
        }
        return real;
    }

    public static ToDoubleFunction<Object> resolveDoubleConverter(Object[][] c) {
        ToDoubleFunction<Object> real = PlotDoubleConverter.intern(PlotDoubleConverter.REAL);
        if (c == null) {
            return real;
        }

        for (Object[] d : c) {
            ToDoubleFunction<Object> d2 = resolveDoubleConverter(d);
            if (d2 != real) {
                return d2;
            }
        }
        return real;
    }

    public static ToDoubleFunction<Object> resolveDoubleConverter(Object[] c) {
        ToDoubleFunction<Object> real = PlotDoubleConverter.intern(PlotDoubleConverter.REAL);
        if (c == null) {
            return real;
        }
        for (Object d : c) {
            PlotDoubleConverter d2 = PlotConfigManager.Numbers.resolveDoubleConverter(d);
            if (d2 != null && d2 != real) {
                return d2;
            }
        }
        return real;
    }

    public static PlotHyperCube mul(PlotHyperCube c, double v) {
        List<PlotCube> t = new ArrayList<>();
        for (int i = 0; i < c.getCubesCount(); i++) {
            t.add(mul(c.getCube(i), v));
        }
        return new DefaultPlotHyperCube(t.toArray(new PlotCube[0]));
    }

    public static PlotCube mul(PlotCube c, double v) {
        Object[][][] values = c.getValues();
        Object[][][] mul0 = mul(values, v);
        return new PlotCube(
                c.getX(), c.getY(), c.getZ(),
                mul0
        );
    }

    public static Object[][][] mul(Object[][][] c, double v) {
        Object[][][] c2 = new Object[c.length][][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = mul(c[i], v);
        }
        return c2;
    }

    public static Object[][] mul(Object[][] c, double v) {
        Object[][] c2 = new Object[c.length][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = mul(c[i], v);
        }
        return c2;
    }

    public static Object[] mul(Object[] a, double b) {
        int max = a.length;
        Object[] ret = new Object[max];
        PlotNumbers numbers = PlotConfigManager.Numbers;
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] == null ? null : numbers.mul(a[i], b);
        }
        return ret;
    }

    public static PlotHyperCube plus(PlotHyperCube c, PlotHyperCube v) {
        List<PlotCube> t = new ArrayList<>();
        for (int i = 0; i < c.getCubesCount(); i++) {
            t.add(plus(c.getCube(i), v.getCube(i)));
        }
        return new DefaultPlotHyperCube(t.toArray(new PlotCube[0]));
    }

    public static PlotCube plus(PlotCube c, PlotCube v) {
        Object[][][] values1 = c.getValues();
        Object[][][] values2 = v.getValues();
        return new PlotCube(
                c.getX(), c.getY(), c.getZ(),
                plus(values1, values2)
        );
    }

    public static Object[][][] plus(Object[][][] c, Object[][][] v) {
        Object[][][] c2 = new Object[c.length][][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = plus(c[i], v[i]);
        }
        return c2;
    }

    public static Object[][] plus(Object[][] c, Object[][] v) {
        Object[][] c2 = new Object[c.length][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = plus(c[i], v[i]);
        }
        return c2;
    }

    public static Object[] plus(Object[] a, Object[] b) {
        int max = a.length;
        Object[] ret = new Object[max];
        PlotNumbers numbers = PlotConfigManager.Numbers;
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] == null ? null : numbers.plus(a[i], b[i]);
        }
        return ret;
    }

    public static PlotHyperCube relativeError(PlotHyperCube c, PlotHyperCube v) {
        List<PlotCube> t = new ArrayList<>();
        for (int i = 0; i < c.getCubesCount(); i++) {
            t.add(relativeError(c.getCube(i), v.getCube(i)));
        }
        return new DefaultPlotHyperCube(t.toArray(new PlotCube[0]));
    }

    public static PlotCube relativeError(PlotCube c, PlotCube v) {
        Object[][][] values1 = c.getValues();
        Object[][][] values2 = v.getValues();
        return new PlotCube(
                c.getX(), c.getY(), c.getZ(),
                relativeError(values1, values2)
        );
    }

    public static Object[][][] relativeError(Object[][][] c, Object[][][] v) {
        Object[][][] c2 = new Object[c.length][][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = relativeError(c[i], v[i]);
        }
        return c2;
    }

    public static Object[][] relativeError(Object[][] c, Object[][] v) {
        Object[][] c2 = new Object[c.length][];
        for (int i = 0; i < c.length; i++) {
            c2[i] = relativeError(c[i], v[i]);
        }
        return c2;
    }

    public static Object[] relativeError(Object[] a, Object[] b) {
        int max = a.length;
        Object[] ret = new Object[max];
        PlotNumbers numbers = PlotConfigManager.Numbers;
        for (int i = 0; i < max; i++) {
            ret[i] = a[i] == null ? null : numbers.relativeError(a[i], b[i]);
        }
        return ret;
    }

    public static Object[][] toColumn(Object[] obj, Class cls) {
        Object[][] vals = (Object[][]) Array.newInstance(cls, obj.length, 1);
        for (int i = 0; i < obj.length; i++) {
            vals[i][0] = obj[i];
        }
        return vals;
    }

    public static boolean isComponentType(Object obj) {
        if (obj.getClass().isArray()) {
            return true;
        } else if (obj instanceof Iterable) {
            return true;
        }
        return false;
    }

    public static Object[][] sub(Object[][] d, Object[][] m) {
        return plus(d, mul(m, -1));
    }

    public static Object[] toObjectArrayOrNull(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            int l = Array.getLength(obj);
            List<Object> initial = new ArrayList<>(l);
            for (int i = 0; i < l; i++) {
                initial.add(Array.get(obj, i));
            }
            return initial.toArray();
        } else if (obj instanceof Iterable) {
            List<Object> initial = new ArrayList<>();
            for (Object o : (Iterable) obj) {
                initial.add(o);
            }
            return initial.toArray();
        } else {
            return null;
        }
    }

    public static ImageIcon getScaledImageIcon(URL srcImg, int w, int h) {
        Image image = new ImageIcon(srcImg).getImage();
        return new ImageIcon(getScaledImage(image, w, h));
    }

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static JPopupMenu getOrCreateComponentPopupMenu(JComponent c){
        JPopupMenu p = c.getComponentPopupMenu();
        if(p==null){
            p=new JPopupMenu();
            c.setComponentPopupMenu(p);
        }
        return p;
    }
}
