package net.vpc.scholar.hadrumaths.test.removeme;

//package net.vpc.scholar.math.functions.dfx;
//
//import java.util.Collections;
//import java.util.List;
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.Out;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
// * change this template use File | Settings | File Templates.
// */
//public class DDxLinear extends DDx {
//
//    private static final long serialVersionUID = -1010101010101001004L;
//    private double a;
//    private double b;
//
//    public DDxLinear(Domain domain, double a, double b) {
//        super(domain);
//        this.a = a;
//        this.b = b;
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                return a == 0;
//            }
//        }
//        return true;
//    }
//
//    public boolean isZero() {
//        return a == 0 && b == 0;
//    }
//
//    public boolean isNaN() {
//        return Double.isNaN(a) || Double.isNaN(b);
//    }
//
//    public boolean isInfinite() {
//        return Double.isInfinite(a) || Double.isInfinite(b);
//    }
//
//    public double computeDouble(double x) {
//        if (contains(x)) {
//            return (a * x + b);
//        }
//        return 0;
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        double[] y = new double[x.length];
//        int[] ab = (d0 == null ? domain : domain.intersect(d0)).rangeArray(x);
//
//        if (ab != null) {
//            int a = ab[0];
//            int b = ab[1];
//            for (int i = a; i <= b; i++) {
//                double xx = x[i];
//                y[i] = (this.a * xx + this.b);
//            }
//            if (range != null) {
//                range.set(Range.forBounds(a, b));
//            }
//        }
//        return y;
//    }
//
//    public DDx mul(double factor, Domain newDomain) {
//        return new DDxLinear(
//                newDomain == null ? domain : domain.intersect(newDomain),
//                a * factor, b * factor
//        );
//    }
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
//    public DDx transformLinear(double a0x, double b0) {
//        return new DDxLinear(
//                domain.transformLinear(a0x, b0),
//                this.a * a0x,
//                this.a * b0 + this.b
//        );
//    }
//
//    public double getA() {
//        return a;
//    }
//
//    public double getB() {
//        return b;
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
//        if (!(o instanceof DDxLinear)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxLinear dDxLinear = (DDxLinear) o;
//
//        if (Double.compare(dDxLinear.a, a) != 0) return false;
//        if (Double.compare(dDxLinear.b, b) != 0) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        temp = Double.doubleToLongBits(a);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(b);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
//}
