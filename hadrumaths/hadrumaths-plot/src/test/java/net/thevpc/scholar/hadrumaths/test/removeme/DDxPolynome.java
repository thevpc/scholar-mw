package net.thevpc.scholar.hadrumaths.test.removeme;

//package net.thevpc.scholar.math.functions.dfx;
//
//import net.thevpc.scholar.math.Axis;
//import net.thevpc.scholar.math.Expr;
//import net.thevpc.scholar.math.Out;
//import net.thevpc.scholar.math.functions.Domain;
//import net.thevpc.scholar.math.functions.Range;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
// * change this template use File | Settings | File Templates.
// */
//public class DDxPolynome extends DDx {
//
//    private static final long serialVersionUID = -1010101010101001013L;
//    private double[] a;
//
//    public DDxPolynome(Domain domain, double... a) {
//        super(domain);
//        this.a = a;
//    }
//
//    public int getLevel() {
//        return a.length - 1;
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                for (int i = 1; i < a.length; i++) {
//                    if (a[i] != 0) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//        }
//        return true;
//    }
//
//    public boolean isZero() {
//        for (double anA : a) {
//            if (anA != 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean isNaN() {
//        for (double anA : a) {
//            if (Double.isNaN(anA)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isInfinite() {
//        for (double anA : a) {
//            if (Double.isInfinite(anA)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public double computeDouble(double x) {
//        if (contains(x)) {
//            double v = 0;
//            // 0
//            // a[n]*x
//            // (a[n]*x+a[n-1])*x
//            // ((a[n]*x+a[n-1])*x+a[n-2])*x
//            //
//
//            //(a * x + b) * x + c
//            for (int i = a.length - 1; i > 0; i--) {
//                v = ((v + a[i]) * x);
//            }
//            v += a[0];
//            return v;
//        }
//        return 0;
//    }
//
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        double[] y = new double[x.length];
//        int[] ab = (d0 == null ? domain : domain.intersect(d0)).rangeArray(x);
//
//        if (ab != null) {
//            int a = ab[0];
//            int b = ab[1];
//            for (int i = a; i <= b; i++) {
//                double xx = x[i];
//
//                double v = 0;
//                // 0
//                // a[n]*x
//                // (a[n]*x+a[n-1])*x
//                // ((a[n]*x+a[n-1])*x+a[n-2])*x
//                //
//
//                //(a * x + b) * x + c
//                for (int k = this.a.length - 1; k > 0; k--) {
//                    v = ((v + this.a[k]) * xx);
//                }
//                y[i] = v + this.a[0];
//            }
//            if (range != null) {
//                range.set(Range.forBounds(a, b));
//            }
//        }
//        return y;
//    }
//
////    public DDx multiply(double factor, DomainX newDomain) {
////        return new DDxPolynome2(
////                newDomain == null ? domain : domain.intersect(newDomain),
////                a * factor, b * factor, c * factor
////        );
////    }
//
//    public DDx toXOpposite() {
//        throw new UnsupportedOperationException();
//    }
//
//    public DDx toSymmetric() {
//        throw new UnsupportedOperationException();
//    }
//
//    public DDx translate(double xDelta, double yDelta) {
//        throw new UnsupportedOperationException();
//    }
//
////    public DDx transformLinear(double a0x, double b0) {
////        return new DDxPolynome2(
////                domain.transformLinear(a0x, b0),
////                this.a * a0x * a0x,
////                2 * this.a * a0x * b0,
////                this.a * b0 * b0 + this.b * b0 + c
////        );
////    }
//
//    public double[] getARef() {
//        return a;
//    }
//
//    public double[] getA() {
//        double[] c = new double[a.length];
//        System.arraycopy(a, 0, c, 0, a.length);
//        return c;
//    }
//
//    public List<Expr> getSubExpressions() {
//        return Collections.EMPTY_LIST;
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxPolynome)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxPolynome that = (DDxPolynome) o;
//
//        if (!Arrays.equals(a, that.a)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (a != null ? Arrays.hashCode(a) : 0);
//        return result;
//    }
//}
