//package net.vpc.scholar.math.functions.dfxy;
//
//import net.vpc.scholar.math.*;
//import net.vpc.scholar.math.FormatFactory;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//import net.vpc.scholar.math.functions.cfxy.ComplexXY;
//import net.vpc.scholar.math.functions.cfxy.IDoubleToVector;
//import net.vpc.scholar.math.functions.dfx.DDx;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * User: taha Date: 2 juil. 2003 Time: 14:29:58
// */
//public final class DoubleX extends DDx implements IDDx, Cloneable {
//
//    public static final DoubleX ZERO = new DoubleX(0, Domain.EMPTYX);
//    private static final long serialVersionUID = -1010101010101001006L;
//    //    public static final int CODE = 1;
//    public double value;
//
//    public DoubleX(Domain domain) {
//        this(1.0 / domain.xwidth, domain);
//    }
//
//    public DoubleX(double cst, Domain domain) {
//        super(cst == 0 ? Domain.EMPTYX : domain);
//        this.value = cst;
//    }
//
//    @Override
//    public boolean isInvariant(Axis axis) {
//        return true;
//    }
//
//    @Override
//    public boolean isZero() {
//        return value == 0;
//    }
//
//    @Override
//    public boolean isNaN() {
//        return Double.isNaN(value);
//    }
//
//    @Override
//    public boolean isInfinite() {
//        return Double.isInfinite(value);
//    }
//
//
//    public double getValue() {
//        return value;
//    }
//
//    public boolean isSymmetric(AxisXY axis) {
//        return true;
//    }
//
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        double[] y = new double[x.length];
//        Range ab = (d0 == null ? domain : domain.intersect(d0)).range(x);
//
//        if (ab != null) {
//            int a = ab.xmin;
//            int b = ab.xmax;
//            for (int i = a; i <= b; i++) {
//                y[i] = value;
//            }
//            if (range != null) {
//                range.set(ab);
//            }
//        }
//        return y;
//    }
//
//    public double computeDouble(double x) {
//        if (contains(x)) {
//            return value;
//        }
//        return 0;
//    }
//
//
////    public DDxy getSymmetricX(DomainXY newDomain) {
////        return getSymmetricX(((newDomain.xmin + newDomain.xmax) / 2));
////    }
////
////    public DDxy getSymmetricY(DomainXY newDomain) {
////        return getSymmetricY(((newDomain.ymin + newDomain.ymax) / 2));
////    }
////    public DDxy getSymmetricX(double x0) {
////        return (getSymmetricX()).translate(2 * (x0 - ((domain.xmin + domain.xmax) / 2)), 0);
////    }
////
////    public DDxy getSymmetricY(double y0) {
////        return (getSymmetricY()).translate(0, 2 * (y0 - ((domain.ymin + domain.ymax) / 2)));
////    }
//
//
//    @Override
//    public String toString() {
//        return FormatFactory.toString(this);
//    }
//
//    public boolean isDC() {
//        return true;
//    }
//
//    public IDoubleToComplex toDC() {
//        return new ComplexXY(Domain.toDomainXY(domain), new Complex(value));
//    }
//
//    public boolean isDD() {
//        return true;
//    }
//
//    public IDoubleToDouble toDD() {
//        return new DoubleValue(value, Domain.toDomainXY(domain));
//    }
//
//    public boolean isDM() {
//        return true;
//    }
//
//    public IDoubleToMatrix toDM() {
//        return toDC().toDM();
//    }
//
////    public boolean isDDx() {
////        return true;
////    }
//
////    public IDDx toDDx() {
////        return this;
////    }
//
//    public List<Expr> getSubExpressions() {
//        return Collections.EMPTY_LIST;
//    }
//
//    @Override
//    public boolean isDouble() {
//        return getDomain().equals(Domain.FULLX);
//    }
//
//    @Override
//    public boolean isComplex() {
//        return isDouble();
//    }
//
//    @Override
//    public boolean isMatrix() {
//        return isDouble();
//    }
//
//    @Override
//    public Complex toComplex() {
//        return new Complex(toDouble());
//    }
//
//    @Override
//    public double toDouble() {
//        if (!isDouble()) {
//            throw new ClassCastException("Constrained double");
//        }
//        return value;
//    }
//
//    @Override
//    public CMatrix toMatrix() {
//        return toComplex().toMatrix();
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        return this;
//    }
//
//    @Override
//    public Expr composeX(Expr xreplacement) {
//        return this;
//    }
//
//    @Override
//    public Expr composeY(Expr yreplacement) {
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DoubleX)) return false;
//        if (!super.equals(o)) {
//            return false;
//        }
//        DoubleX doubleX = (DoubleX) o;
//
//        if (Double.compare(doubleX.value, value) != 0) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        result = super.hashCode();
//        temp = Double.doubleToLongBits(value);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
//
//    @Override
//    public boolean isScalarExpr() {
//        return true;
//    }
//
//    @Override
//    public boolean isDoubleExpr() {
//        return true;
//    }
//
//
//    @Override
//    public boolean isDV() {
//        return true;
//    }
//
//    @Override
//    public IDoubleToVector toDV() {
//        return toDC().toDV();
//    }
//
//    @Override
//    public int getDomainDimension() {
//        return 1;
//    }
//
//}
