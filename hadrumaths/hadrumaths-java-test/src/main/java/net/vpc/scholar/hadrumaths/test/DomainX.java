//package net.vpc.scholar.math.functions;
//
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.util.dump.DumpHelper;
//
//import java.io.Serializable;
//
///**
// * User: taha Date: 2 juil. 2003 Time: 14:31:19
// */
//public final class DomainX implements Serializable {
//
//    public static final DomainX NaN = new DomainX(Double.NaN, Double.NaN);
//    public static final DomainX FULL = new DomainX(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//    public static final DomainX EMPTY = new DomainX(0, 0);
//    private static final long serialVersionUID = -1010101010101001025L;
//    public double xmin;
//    public double xmax;
//    public double width;
//
//    public DomainX(double xmin, double xmax) {
//        this.xmin = xmin;
//        this.xmax = xmax;
//        this.width = xmax - xmin;
//    }
//
//    public static DomainXi range(DomainX a, DomainX b, double[] x) {
//        return (b == null ? a : a.intersect(b)).range(x);
//    }
//
//    public static DomainX intersect(DomainX d1, DomainX d2, DomainX d3) {
//        return d1.intersect(d2).intersect(d3);
//    }
//
//    public boolean[] coeff(double[] xVector) {
//        //new
//        int xl = xVector.length;
//        boolean[] r = new boolean[xl];
//        double xi = 0;
//        for (int xIndex = 0; xIndex < xl; xIndex++) {
//            xi = xVector[xIndex];
//            r[xIndex] = (xi >= xmin && xi <= xmax);
//        }
//        return r;
//    }
//
//    public DomainXi range(double[] x) {
//        int[] ints = rangeArray(x);
//        return ints == null ? null : new DomainXi(ints[0], ints[1]);
//    }
//
//    public int[] rangeArray(double[] x) {
//        if (isEmpty()) {
//            return null;//new int[]{-1,-1};
//        }
//        double min = xmin;
//        double max = xmax;
//
//        //new
//        int a = 0;
//
//        int low = 0;
//        int high = x.length - 1;
//
//        while (low <= high) {
//            int mid = (low + high) >> 1;
//            double midVal = x[mid];
//
//            int cmp;
//            if (midVal < min) {
//                cmp = -1;   // Neither val is NaN, thisVal is smaller
//            } else if (midVal > min) {
//                cmp = 1;    // Neither val is NaN, thisVal is larger
//            } else {
//                long midBits = Double.doubleToLongBits(midVal);
//                long keyBits = Double.doubleToLongBits(min);
//                cmp = (midBits == keyBits ? 0 : // Values are equal
//                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
//                                1));                     // (0.0, -0.0) or (NaN, !NaN)
//            }
//
//            if (cmp < 0) {
//                low = mid + 1;
//            } else if (cmp > 0) {
//                high = mid - 1;
//            } else {
//                low = mid;
//                break;
//            }
//        }
//        a = low;
//
//        int b = a;
//
//        low = a;
//        high = x.length - 1;
//
//        while (low <= high) {
//            int mid = (low + high) >> 1;
//            double midVal = x[mid];
//
//            int cmp;
//            if (midVal < max) {
//                cmp = -1;   // Neither val is NaN, thisVal is smaller
//            } else if (midVal > max) {
//                cmp = 1;    // Neither val is NaN, thisVal is larger
//            } else {
//                long midBits = Double.doubleToLongBits(midVal);
//                long keyBits = Double.doubleToLongBits(max);
//                cmp = (midBits == keyBits ? 0 : // Values are equal
//                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
//                                1));                     // (0.0, -0.0) or (NaN, !NaN)
//            }
//
//            if (cmp < 0) {
//                low = mid + 1;
//            } else if (cmp > 0) {
//                high = mid - 1;
//            } else {
//                high = mid;
//                break;
//            }
//        }
//        b = high;
//
//        return (a < 0 || a >= x.length || b < a) ? null : new int[]{a, b};
//    }
//
//    //    public int[] rangesArray(double[] x) {
////        if(isEmpty()){
////            return new int[]{-1,-1};
////        }
////        //new
////        int a = 0;
////
////        int low = 0;
////        int high = x.length - 1;
////
////
////        while (low <= high) {
////            int mid = (low + high) >> 1;
////            double midVal = x[mid];
////
////            int cmp;
////            if (midVal < xmin) {
////                cmp = -1;   // Neither val is NaN, thisVal is smaller
////            } else if (midVal > xmin) {
////                cmp = 1;    // Neither val is NaN, thisVal is larger
////            } else {
////                long midBits = Double.doubleToLongBits(midVal);
////                long keyBits = Double.doubleToLongBits(a);
////                cmp = (midBits == keyBits ? 0 : // Values are equal
////                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
////                        1));                     // (0.0, -0.0) or (NaN, !NaN)
////            }
////
////            if (cmp < 0)
////                low = mid + 1;
////            else if (cmp > 0) {
////                high = mid - 1;
////            } else {
////                a = mid;
////                break;
////            }
////        }
////        a = low;
////
////
////        int b = a;
////
////        low = a;
////        high = x.length - 1;
////
////
////        while (low <= high) {
////            int mid = (low + high) >> 1;
////            double midVal = x[mid];
////
////            int cmp;
////            if (midVal < xmax) {
////                cmp = -1;   // Neither val is NaN, thisVal is smaller
////            } else if (midVal > xmax) {
////                cmp = 1;    // Neither val is NaN, thisVal is larger
////            } else {
////                long midBits = Double.doubleToLongBits(midVal);
////                long keyBits = Double.doubleToLongBits(a);
////                cmp = (midBits == keyBits ? 0 : // Values are equal
////                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
////                        1));                     // (0.0, -0.0) or (NaN, !NaN)
////            }
////
////            if (cmp < 0)
////                low = mid + 1;
////            else if (cmp > 0) {
////                high = mid - 1;
////            } else {
////                a = mid;
////                break;
////            }
////        }
////        b = high;
////
////
////        return new int[]{a, b};
////    }
//    public int[] limitsOrdredOld(double[] xVector) {
//        //new
//        int a = 0;
//        int b = xVector.length - 1;
//
//        double xa;
//        double xb;
//        boolean left = false;
//        while (true) {
//            xa = xVector[a];
//            if (xa >= xmin && xa <= xmax) {
//                left = true;
//                break;
//            } else {
//                a++;
//            }
//            xb = xVector[b];
//            if (xb >= xmin && xb <= xmax) {
//                left = false;
//                break;
//            } else {
//                b--;
//            }
//            xb = xVector[a];
//        }
//        int low = a + 1;
//        int high = b;
//        if (left) {
//            while (low <= high) {
//                int mid = (low + high) >> 1;
//                double midVal = xVector[mid];
//                if (/*midVal >= xmin && */midVal <= xmax) {
//                    low = mid + 1;
//                } else {
//                    high = mid - 1;
//                }
//            }
//            b = -(low + 1);
//        } else {
//            while (low <= high) {
//                int mid = (low + high) >> 1;
//                double midVal = xVector[mid];
//                if (midVal >= xmin) {
//                    low = mid + 1;
//                } else {
//                    high = mid - 1;
//                }
//            }
//            a = -(low + 1);
//        }
//
//        return new int[]{a, b};
//    }
//
//    public boolean contains(double x) {
//        return x >= xmin && x < xmax;
//    }
//
//    public DomainX intersect(DomainX other) {
////        double x1= Maths.max(xmin,other.xmin);
////        double x2=Maths.min(xmax,other.xmax);
////        double y1=Maths.max(ymin,other.ymin);
////        double y2=Maths.min(ymax,other.ymax);
//        if (other.isEmpty()) {
//            return this;
//        }
//        double x1 = other.xmin;
//        double x2 = other.xmax;
//
//        if (xmin > x1) {
//            x1 = xmin;
//        }
//        if (xmax < x2) {
//            x2 = xmax;
//        }
//
//        // some workaround
//        final double epsilon = 1E-12;
//        if ((x1 - x2) != 0 && Math.abs(x1 - x2) < epsilon) {
//            x1 = x2 = 0;
//        }
//
//        return new DomainX(x1, x2);
//    }
//
//    public DomainX expand(DomainX other) {
//        if (other.isEmpty()) {
//            return this;
//        }
//        double x1 = Maths.min(xmin, other.xmin);
//        double x2 = Maths.max(xmax, other.xmax);
//        return new DomainX(x1, x2);
//    }
//
//    public boolean isEmpty() {
//        return xmin >= xmax;
//    }
//
//    public DomainX transformLinear(double a0x, double b0) {
//        return new DomainX(a0x * xmin + b0, a0x * xmax + b0);
//    }
//
//    public DomainX translate(double x) {
//        return new DomainX(xmin + x, xmax + x);
//    }
//
//    public DomainX getSymmetricX(double x0) {
//        double x1 = xmin + 2 * (x0 - xmin);
//        double x2 = xmax + 2 * (x0 - xmax);
//        return new DomainX(Maths.min(x1, x2), Maths.max(x1, x2));
//    }
//
//    public double getCenterX() {
//        return (xmin + xmax) / 2;
//    }
//
//    public String getDumpString() {
//        DumpHelper h = new DumpHelper("DomainX", true);
//        h.add("xmin", xmin);
//        h.add("xmax", xmax);
//        return h.toString();
//    }
//
//    public String toString() {
//        return getDumpString();
//    }
//
//
//    public double[] steps(double step) {
//        return Maths.dsteps(xmin, xmax, step);
//    }
//
//    public double[] times(int count) {
//        return Maths.dtimes(xmin, xmax, count);
//    }
//}
