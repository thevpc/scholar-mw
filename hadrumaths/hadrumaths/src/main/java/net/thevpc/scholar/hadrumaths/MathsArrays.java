package net.thevpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class MathsArrays {

    public static RealDoubleConverter REAL = new RealDoubleConverter();

    /**
     * sqrt(a^2 + b^2) without under/overflow.
     *
     * @param a
     * @param b
     * @return
     */
    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }

    private static class RealDoubleConverter implements ToDoubleFunction<Object> {

        @Override
        public double applyAsDouble(Object value) {
            if (value == null) {
                return Double.NaN;
            }
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            if (value instanceof Expr) {
                return ((Expr) value).toDouble();
            }
            return Double.NaN;
        }

        @Override
        public String toString() {
            return "REAL";
        }
    }

    public static double[][] toDouble(Complex[][] c, ToDoubleFunction<Object> toDoubleConverter) {
        if (c == null) {
            return null;
        }
        if (toDoubleConverter == null) {
            toDoubleConverter = MathsArrays.REAL;
        }
        double[][] z = new double[c.length][];
        for (int i = 0; i < z.length; i++) {
            z[i] = toDouble(c[i], REAL);
        }
        return z;
    }

    public static double[] toDouble(Complex[] c, ToDoubleFunction<Object> toDoubleConverter) {
        if (c == null) {
            return null;
        }
        if (toDoubleConverter == null) {
            toDoubleConverter = REAL;
        }
        double[] z = new double[c.length];
        for (int i = 0; i < z.length; i++) {
            z[i] = toDoubleConverter.applyAsDouble(c[i]);
        }
        return z;
    }

    /**
     * range closed min (inclusive) and closed max (inclusive)
     *
     * @param orderedValues array to look for range into. must be
     * <strong>ordered</strong>
     * @param min min value accepted in range (inclusive)
     * @param max max value accepted in range (inclusive)
     * @return array of two integers defining first and last indices (all
     * inclusive) accepted in range
     */
    public static int[] rangeCC(double[] orderedValues, double min, double max) {
        //System.out.printf("[%f..%f] from [%f,%f]=",range[0],range[range.length-1],min,max);
        int len = orderedValues.length;
        int a = Arrays.binarySearch(orderedValues, min);
        if (a < 0) {
            a = -a - 1;
            if (a == len) {
                //System.out.printf("0%n");
                return null;
            }
        }
        int b = Arrays.binarySearch(orderedValues, max);
        if (b < 0) {
            b = -b - 2;
            if (b < 0) {
                //System.out.printf("0%n");
                return null;
            } else if (b == len) {
                b = len - 1;
            }
        }
        //System.out.printf("[%f,%f] as [%d,%d]%n",range[a],range[b],a,b);
        return new int[]{a, b};
    }

    /**
     * range closed min (inclusive) and open max (exclusive)
     *
     * @param orderedValues array to look for range into. must be
     * <strong>ordered</strong>
     * @param min min value accepted in range (inclusive)
     * @param max max value accepted in range (exclusive)
     * @return array of two integers defining first and last indices (all
     * inclusive) accepted in range
     */
    public static int[] rangeCO(double[] orderedValues, double min, double max) {
        //System.out.printf("[%f..%f] from [%f,%f]=",range[0],range[range.length-1],min,max);
        if (orderedValues == null) {
            return null;
        }
        int len = orderedValues.length;
        int a = Arrays.binarySearch(orderedValues, min);
        if (a < 0) {
            a = -a - 1;
            if (a == len) {
                //System.out.printf("0%n");
                return null;
            }
        }
        int b = Arrays.binarySearch(orderedValues, max);
        if (b < 0) {
            b = -b - 2;
            if (b < 0) {
                //System.out.printf("0%n");
                return null;
            } else if (b == len) {
                b = len - 1;
            }
        } else if (b == 0) {
            return null;
        } else {
            b = b - 1;
        }
        if (b < a) {
            return null;
        }
        //System.out.printf("[%f,%f] as [%d,%d]%n",range[a],range[b],a,b);
        return new int[]{a, b};
    }

    public static double[][] cross(double[] x, double[] y) {
        double[][] r = new double[x.length * y.length][2];
        int p = 0;
        for (double aX : x) {
            for (double aY : y) {
                double[] v = r[p];
                v[0] = aX;
                v[1] = aY;
                p++;
            }
        }
        return r;
    }

    public static double[][] cross(double[] x, double[] y, Double2Filter filter) {
        double[][] r = new double[x.length * y.length][2];
        int p = 0;
        for (double aX : x) {
            for (double aY : y) {
                if (filter == null || filter.accept(aX, aY)) {
                    double[] v = r[p];
                    v[0] = aX;
                    v[1] = aY;
                    p++;
                }
            }
        }
        return Arrays.copyOf(r, p);
    }

    public static double[][] cross(double[] x, double[] y, double[] z) {
        double[][] r = new double[x.length * y.length * z.length][3];
        int p = 0;
        for (double aX : x) {
            for (double aY : y) {
                for (double aZ : z) {
                    double[] v = r[p];
                    v[0] = aX;
                    v[1] = aY;
                    v[2] = aZ;
                    p++;
                }
            }
        }
        return Arrays.copyOf(r, p);
    }

    public static double[][] cross(double[] x, double[] y, double[] z, Double3Filter filter) {
        List<double[]> r = new ArrayList<>(x.length * y.length * z.length);
        for (double aX : x) {
            for (double aY : y) {
                for (double aZ : z) {
                    if (filter == null || filter.accept(aX, aY, aZ)) {
                        double[] v = new double[3];
                        v[0] = aX;
                        v[1] = aY;
                        v[2] = aZ;
                        r.add(v);
                    }
                }
            }
        }
        return r.toArray(new double[0][]);
    }

    public static int[][] cross(int[] x, int[] y) {
        int[][] r = new int[x.length * y.length][2];
        int p = 0;
        for (int aX : x) {
            for (int aY : y) {
                int[] v = r[p];
                v[0] = aX;
                v[1] = aY;
                p++;
            }
        }
        return r;
    }

    public static int[][] cross(int[] x, int[] y, int[] z) {
        int[][] r = new int[x.length * y.length * z.length][3];
        int p = 0;
        for (int aX : x) {
            for (int aY : y) {
                for (int aZ : z) {
                    int[] v = r[p];
                    v[0] = aX;
                    v[1] = aY;
                    v[2] = aZ;
                    p++;
                }
            }
        }
        return r;
    }

    //a koa ca sert??????? j'ai déja range
    @Deprecated
    public static int[] range2(double[] x, double min, double max) {
        //new
        int a = 0;

        int low = 0;
        int high = x.length - 1;

        while (low <= high) {
            int mid = (low + high) >> 1;
            double midVal = x[mid];

            int cmp;
            if (midVal < min) {
                cmp = -1;   // Neither val is NaN, thisVal is smaller
            } else if (midVal > min) {
                cmp = 1;    // Neither val is NaN, thisVal is larger
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(min);
                cmp = (midBits == keyBits ? 0
                        : // Values are equal
                        (midBits < keyBits ? -1
                                : // (-0.0, 0.0) or (!NaN, NaN)
                                1));                     // (0.0, -0.0) or (NaN, !NaN)
            }

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                low = mid;
                break;
            }
        }
        a = low;

        int b = a;

        low = a;
        high = x.length - 1;

        while (low <= high) {
            int mid = (low + high) >> 1;
            double midVal = x[mid];

            int cmp;
            if (midVal < max) {
                cmp = -1;   // Neither val is NaN, thisVal is smaller
            } else if (midVal > max) {
                cmp = 1;    // Neither val is NaN, thisVal is larger
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(max);
                cmp = (midBits == keyBits ? 0
                        : // Values are equal
                        (midBits < keyBits ? -1
                                : // (-0.0, 0.0) or (!NaN, NaN)
                                1));                     // (0.0, -0.0) or (NaN, !NaN)
            }

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                high = mid;
                break;
            }
        }
        b = high;

        return (a < 0 || a >= x.length || b < a) ? null : new int[]{a, b};
    }

    public static double min(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double min = Double.NaN;
        for (int i = 0; i < arr.length; i++) {
            if (Double.isNaN(min) || (!Double.isNaN(arr[i]) && arr[i] < min)) {
                min = arr[i];
            }
        }
        return min;
    }

    public static double max(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double max = Double.NaN;
        for (int i = 0; i < arr.length; i++) {
            if (Double.isNaN(max) || (!Double.isNaN(arr[i]) && arr[i] > max)) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double avg(double[] arr) {
        if (arr.length == 0) {
            return Double.NaN;
        }
        double max = 0;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((!Double.isNaN(arr[i]))) {
                max += arr[i];
                count++;
            }
        }
        if (count == 0) {
            return Double.NaN;
        }
        return max / count;
    }

    public static double[] minMax(double[] a) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;
        for (double value : a) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        return new double[]{min, max};
    }

    public static double[] minMaxAbs(double[] a) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double anA : a) {
            double abs = Math.abs(anA);
            min = Math.min(min, abs);
            max = Math.max(max, abs);
        }
        return new double[]{min, max};
    }

    public static double[] minMaxAbsNonInfinite(double[] a) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        boolean b = false;
        for (double anA : a) {
            if (!Double.isNaN(anA) && !Double.isInfinite(anA)) {
                double abs = Math.abs(anA);
                min = Math.min(min, abs);
                max = Math.max(max, abs);
                b = true;
            }
        }
        if (b) {
            return new double[]{min, max};
        } else {
            return new double[]{0, 0};
        }
    }

    public static double avgAbs(double[] arr) {
        double avg = 0;
        for (double anArr : arr) {
            avg += Math.abs(anArr);
        }
        return avg / arr.length;
    }

    public static double[] distances(double[] arr) {
        double[] distances = new double[arr.length - 1];
        for (int i = 1; i < arr.length; i++) {
            distances[i - 1] = arr[i] - arr[i - 1];
        }
        return distances;
    }

    public static double[] resizePickFirst(double[] array, int newSize) {
        int oldSize = array.length;
        if (newSize == oldSize) {
            double[] array2 = new double[newSize];
            System.arraycopy(array, 0, array2, 0, newSize);
            return array2;
        } else if (newSize > oldSize) {
            throw new IllegalArgumentException("Unsupported yet");
        } else {
            int windowSize = oldSize / newSize;
            double[] array2 = new double[newSize];
            for (int i = 0; i < newSize; i++) {
                array2[i] = array[i * windowSize];
            }
            return array2;
        }
    }

    public static double[] resizePickAverage(double[] array, int newSize) {
        int oldSize = array.length;
        if (newSize == oldSize) {
            double[] array2 = new double[newSize];
            System.arraycopy(array, 0, array2, 0, newSize);
            return array2;
        } else if (newSize > oldSize) {
            throw new IllegalArgumentException("Unsupported yet");
        } else {
            int windowSize = oldSize / newSize;
            double[] array2 = new double[newSize];
            for (int i = 0; i < newSize; i++) {
                int m = 0;
                double a = 0;
                for (int j = 0; j < windowSize && i * windowSize + j < oldSize; j++) {
                    m++;
                    a += array[i * windowSize + j];
                }
                array2[i] = a / m;
            }
            return array2;
        }
    }

    public static double rerr(double a, double b) {
        if (a == b) {
            return 0;
        }
        if (Double.isNaN(a) && Double.isNaN(b)) {
            return 0;
        }
        if (Double.isNaN(a) || Double.isNaN(b)) {
            return Double.NaN;
        }
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Math.abs(a) > Math.abs(b)) {
            double c = a;
            a = b;
            b = c;
        }
        return Math.abs(b - a) / Math.abs(a);
    }

    public static double rerr(Complex a, Complex b) {
        if (a.equals(b)) {
            return 0;
        }
        if (a.isNaN() && b.isNaN()) {
            return 0;
        }
        if (a.isNaN() || b.isNaN()) {
            return Double.NaN;
        }
        if (a.isInfinite() || b.isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        if (a.norm() > b.norm()) {
            Complex c = a;
            a = b;
            b = c;
        }
        return (b.minus(a)).absdbl() / a.absdbl();
    }

    public static double[] getColumn(double[][] a, int index) {
        int maxi = a.length;
        double[] ret = new double[maxi];
        for (int i = 0; i < maxi; i++) {
            ret[i] = a[i][index];
        }
        return ret;
    }
}
