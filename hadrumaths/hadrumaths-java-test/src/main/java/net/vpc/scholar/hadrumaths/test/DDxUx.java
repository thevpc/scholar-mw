//package net.vpc.scholar.math.functions.dfx;
//
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.Out;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
// * change this template use File | Settings | File Templates.
// */
//public class DDxUx extends DDx {
//
//    private static final long serialVersionUID = -1010101010101001015L;
//    private double amp;
//    private double a;
//    private double b;
//    private double c;
//    private double d;
//    private double e;
//
//    public DDxUx(Domain domain, double amp, double a, double b, double c, double d, double e) {
//        super(domain);
//        this.amp = amp;
//        this.a = a;
//        this.b = b;
//        this.c = c;
//        this.d = d;
//        this.e = e;
//    }
//
//    public boolean isZero() {
//        return false;
//    }
//
//    public boolean isNaN() {
//        return false;
//    }
//
//    public boolean isInfinite() {
//        return false;
//    }
//
//    public double computeDouble(double x) {
//        if (contains(x)) {
//            return amp * Math.cos(a * x + b) / Math.sqrt(c * x * x + d * x + e);
//        }
//        return 0;
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        //todo taha
//
//        double[] y = new double[x.length];
//        Range ab = (d0 == null ? domain : domain.intersect(d0)).range(x);
//
//        if (ab != null) {
//            int a = ab.xmin;
//            int b = ab.xmax;
//            for (int i = a; i <= b; i++) {
//                double xx = x[i];
//                y[i] = amp * Math.cos(this.a * xx + this.b) / Math.sqrt(this.c * xx * xx + this.d * xx + this.e);
//            }
//            if (range != null) {
//                range.set(ab);
//            }
//        }
//        return y;
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                return amp == 0 || (a == 0 && c == 0 && d == 0);
//            }
//        }
//        return true;
//    }
//
//    public DDx mul(double factor, Domain newDomain) {
//        return new DDxUx(newDomain == null ? domain : domain.intersect(newDomain), amp * factor, a, b, c, d, e);
//    }
//
//    public DDx toXOpposite() {
//        return new DDxUx(domain, amp, -a, b, c, -d, e);
//    }
//
////    public DDx toSymmetric() {
////        return transformLinear(-1, domain.xmax);
//////        throw new UnsupportedOperationException();
////    }
//
////    public DDx translate(double xDelta, double yDelta) {
////        return transformLinear(1, -xDelta);
////    }
//
//    public DDx transformLinear(double a0x, double b0) {
//        DDxCos cos = (DDxCos) new DDxCos(domain, amp, a, b).transformLinear(a0x, b0);
//        double a = c;
//        double b = d;
//        double c = e;
//        double aa = a * a0x * a0x;
//        double bb = 2 * aa * a0x * b0;
//        double cc = aa * b0 * b0 + bb * b0 + c;
//        return new DDxUx(domain.transformLinear(a0x, b0),
//                cos.getAmp(),
//                cos.getA(),
//                cos.getB(),
//                aa,
//                bb,
//                cc
//        );
//    }
//
//    public double getAmp() {
//        return amp;
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
//    public double getC() {
//        return c;
//    }
//
//    public double getD() {
//        return d;
//    }
//
//    public double getE() {
//        return e;
//    }
//
//    public String getExpression() {
//        return amp + " * cos(" + a + " * x+" + this.b + ") / sqrt(" + c + " * x² +" + this.d + " * x + " + this.e + ")";
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
//        if (!(o instanceof DDxUx)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxUx dDxUx = (DDxUx) o;
//
//        if (Double.compare(dDxUx.a, a) != 0) return false;
//        if (Double.compare(dDxUx.amp, amp) != 0) return false;
//        if (Double.compare(dDxUx.b, b) != 0) return false;
//        if (Double.compare(dDxUx.c, c) != 0) return false;
//        if (Double.compare(dDxUx.d, d) != 0) return false;
//        if (Double.compare(dDxUx.e, e) != 0) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        temp = Double.doubleToLongBits(amp);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(a);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(b);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(c);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(d);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(e);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
//}
