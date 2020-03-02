//package net.vpc.scholar.math.functions.dfxy;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import net.vpc.scholar.math.*;
//import net.vpc.scholar.math.functions.Domain;
//
//import net.vpc.scholar.math.functions.FunctionFactory;
//
///**
// * User: taha Date: 2 juil. 2003 Time: 11:51:13
// */
//public final class DDxyDiscrete extends DDxy {
//
//    private static final long serialVersionUID = -1010101010101001033L;
//    //    public static final int HASHCODE = 1;
//
//    private double[][] values;
//    private double[] x;
//    private double[] y;
//    private double dx;
//    private double dy;
//    private int gridX;
//    private int gridY;
////    private Plus cachedDFunctionSumXY = null;
//
//    public DDxyDiscrete(double[][] values, Domain domain) {
//        super(domain);
//        setName("numeric");
//        dsteps(values,
//                Maths.dtimes(getDomain().xmin, getDomain().xmax - dx, values[0].length),
//                Maths.dtimes(getDomain().ymin, getDomain().ymax - dy, values.length)
//        );
//    }
//
//    private void dsteps(double[][] values, double[] x, double[] y) {
//        this.values = values;
//        dx = domain.xwidth / values[0].length;
//        dy = domain.ywidth / values.length;
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public boolean isZero() {
//        return false;//amp==0;
//    }
//
//    @Override
//    public boolean isInvariant(Axis axis) {
//        return false;
//    }
//
//    public DDxyDiscrete translate(double deltaX, double deltaY) {
//        return new DDxyDiscrete(values, domain.translate(deltaX, deltaY));
//    }
//
//    public double compute(double x, double y) {
//        if (contains(x, y)) {
//            int i = (int) ((x - domain.xmin) / dx);
//            int j = (int) ((y - domain.ymin) / dy);
//            return values[j < values.length ? j : (values.length - 1)][i < values[0].length ? i : (values[0].length - 1)];
//        }
//        return 0;
//    }
//
//    public IDDxy mul(double factor, Domain newDomain) {
//        Domain theDomain = newDomain == null ? domain : domain.intersect(newDomain);
//        if (theDomain.equals(domain)) {
//            if (factor == 1) {
//                return this;
//            }
//            double[][] n = new double[values.length][];
//            for (int i = 0; i < n.length; i++) {
//                n[i] = new double[values[i].length];
//                for (int j = 0; j < n[i].length; j++) {
//                    n[i][j] = values[i][j] * factor;
//                }
//            }
//            return new DDxyDiscrete(n, domain);
//        } else {
//            return Maths.mul(FunctionFactory.cst(factor, theDomain), this).toDD();
//        }
//    }
//
//    public DDxy toXOpposite() {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public DDxy toYOpposite() {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
////    public DDxy getSymmetricX() {
////        check();
////        double[][] n = new double[values.length][];
////        for (int i = 0; i < n.length; i++) {
////            n[i] = new double[values[i].length];
////            for (int j = 0; j < n[i].length; j++) {
////                n[i][j] = values[i][n[i].length - 1 - j];
////            }
////        }
////        return new DDxyDiscrete(n, domain);
////    }
//
////    public DDxy getSymmetricY() {
////        check();
////        double[][] n = new double[values.length][];
////        for (int i = 0; i < n.length; i++) {
////            n[i] = new double[values[i].length];
////            System.arraycopy(values[n.length - 1 - i], 0, n[i], 0, n[i].length);
////        }
////        return new DDxyDiscrete(n, domain);
////    }
//
////    public Plus discretize() {
////        if (cachedDFunctionSumXY == null) {
////            ArrayList<IDDxy> sum = new ArrayList<IDDxy>();
////            Domain dom;
////            for (int yi = 0; yi < y.length; yi++) {
////                for (int xi = 0; xi < x.length; xi++) {
////                    if (values[yi][xi] != 0) {
////                        dom = Domain.forBounds(x[xi], x[xi] + dx, y[yi], y[yi] + dy);
////                        sum.add(new DoubleXY(values[yi][xi], dom));
////                    }
////                }
////            }
////            cachedDFunctionSumXY = Maths.sum(sum.toArray(new IDDxy[sum.size()]));
////        }
////        return cachedDFunctionSumXY;
////    }
//
//    public double[][] getValues() {
//        return values;
//    }
//
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        return this;
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxyDiscrete)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxyDiscrete that = (DDxyDiscrete) o;
//
//        if (Double.compare(that.dx, dx) != 0) return false;
//        if (Double.compare(that.dy, dy) != 0) return false;
//        if (gridX != that.gridX) return false;
//        if (gridY != that.gridY) return false;
////        if (cachedDFunctionSumXY != null ? !cachedDFunctionSumXY.equals(that.cachedDFunctionSumXY) : that.cachedDFunctionSumXY != null)
////            return false;
//        if (!Arrays.equals(x, that.x)) return false;
//        if (!Arrays.equals(y, that.y)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        result = 31 * result + (x != null ? Arrays.hashCode(x) : 0);
//        result = 31 * result + (y != null ? Arrays.hashCode(y) : 0);
//        temp = Double.doubleToLongBits(dx);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(dy);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + gridX;
//        result = 31 * result + gridY;
////        result = 31 * result + (cachedDFunctionSumXY != null ? cachedDFunctionSumXY.hashCode() : 0);
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
//    public double[] getX() {
//        return x;
//    }
//
//    public double[] getY() {
//        return y;
//    }
//
//    public double getDx() {
//        return dx;
//    }
//
//    public double getDy() {
//        return dy;
//    }
//
//    @Override
//    public int getDomainDimension() {
//        return 2;
//    }
//
//}
