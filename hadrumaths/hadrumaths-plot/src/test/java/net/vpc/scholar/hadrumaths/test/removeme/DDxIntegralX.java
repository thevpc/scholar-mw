package net.vpc.scholar.hadrumaths.test.removeme;

//package net.vpc.scholar.math.functions.dfx;
//
//import net.vpc.scholar.math.*;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//import net.vpc.scholar.math.integration.DIntegralX;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
// * change this template use File | Settings | File Templates.
// */
//public class DDxIntegralX extends DDx {
//
//    private static final long serialVersionUID = -1010101010101001061L;
//    double x0;
//    private IDDx base;
//    private DIntegralX integral;
//
//    /**
//     * f(x)=integral([x0,x], base(x).dx)
//     *
//     * @param base
//     * @param integral
//     * @param x0
//     */
//    public DDxIntegralX(IDDx base, DIntegralX integral, double x0) {
//        super(base.getDomain());
//        this.base = base;
//        this.integral = integral;
//        this.x0 = x0;
//    }
//
//    public double getX0() {
//        return x0;
//    }
//
//
//    public double computeDouble(double x) {
//        return integral.integrateX(base, x0, x);
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean isZero() {
//        return base.isZero();
//    }
//
//    public boolean isNaN() {
//        return base.isNaN();
//    }
//
//    public boolean isInfinite() {
//        return base.isInfinite();
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        //todo taha
//        boolean optimized = false;
//        // simple
//        double[] y = new double[x.length];
////        double[] y0 = new double[x.length];
//        Range ab = (d0 == null ? domain : domain.intersect(d0)).range(x);
//
//        if (ab != null) {
//            int a = ab.xmin;
//            int b = ab.xmax;
//            if (optimized) {
//                y[a] = integral.integrateX(base, x0, a);
//                for (int i = a + 1; i <= b; i++) {
//                    y[i] = y[i - 1] + integral.integrateX(base, x[i - 1], x[i]);
//                    double r = integral.integrateX(base, x0, x[i]);
//                    r = ((r - y[i]) / y[i]) * 100;
//
//                }
//            } else {
//                for (int i = a; i <= b; i++) {
//                    y[i] = integral.integrateX(base, x0, x[i]);
//                }
//            }
//            if (range != null) {
//                range.set(ab);
//            }
//        }
//        return y;
//    }
//
////    public DDx multiply(double factor, DomainX newDomain) {
////        return new DDxIntegralX(base.multiply(factor, newDomain), integral, x0);
////    }
////
////    public DDx toXOpposite() {
////        return new DDxIntegralX(base.toXOpposite(), integral, x0);
////    }
////
////    public DDx toSymmetric() {
////        return base;
////    }
//
////    public DDx translate(double xDelta, double yDelta) {
////        return new DDxIntegralX(base.translate(xDelta, yDelta), integral, x0);
////    }
////
////    public DDx simplify() {
////        return new DDxIntegralX(base.simplify(), integral, x0);
////    }
//
//    public IDDx getBase() {
//        return base;
//    }
//
//    public List<Expr> getSubExpressions() {
//        return Collections.EMPTY_LIST;
//    }
//
//    public DIntegralX getIntegral() {
//        return integral;
//    }
//
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        IDDx base = (IDDx) this.base.setParam(name, value);
//        if (base != this.base) {
//            DDxIntegralX e = new DDxIntegralX(base, integral, x0);
//            AbstractExprPropertyAware.copyProperties(this, e);
//            return e;
//        }
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxIntegralX)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxIntegralX that = (DDxIntegralX) o;
//
//        if (Double.compare(that.x0, x0) != 0) return false;
//        if (base != null ? !base.equals(that.base) : that.base != null) return false;
//        if (integral != null ? !integral.equals(that.integral) : that.integral != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        result = 31 * result + (base != null ? base.hashCode() : 0);
//        result = 31 * result + (integral != null ? integral.hashCode() : 0);
//        temp = Double.doubleToLongBits(x0);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
//}
