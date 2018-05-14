//package net.vpc.scholar.math;
//
//import net.vpc.scholar.math.geom.*;
//import net.vpc.scholar.math.symbolic.Range;
//import net.vpc.scholar.math.util.ArrayUtils;
//import net.vpc.scholar.math.util.dump.Dumpable;
//import net.vpc.scholar.math.util.dump.Dumper;
//
//import java.awt.geom.Path2D;
//import java.awt.geom.Rectangle2D;
//import java.io.Serializable;
//
///**
// * User: taha Date: 2 juil. 2003 Time: 14:31:19
// */
//public final class DomainDefault extends Domain implements Cloneable {
//
//    public final int dimension;
//    public final double xmin;
//    public final double xmax;
//    public final double ymin;
//    public final double ymax;
//    public final double zmin;
//    public final double zmax;
////    public final double xwidth;
////    public final double ywidth;
////    public final double zwidth;
////    public Double cachedCenterX = null;
////    public Double cachedCenterY = null;
////    public Double cachedCenterZ = null;
////    public int hashCode = 0;
//
//    protected DomainDefault(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, int dimension) {
////        if (Double.isNaN(xmin) || Double.isNaN(ymin) || Double.isNaN(xmax) || Double.isNaN(ymax) || Double.isNaN(zmin) || Double.isNaN(zmax)) {
////            System.err.println("DomainXY NaN");
////        }
////        if (ymax==1) {
////            System.err.println("DomainXY NaN");
////        }
//        this.dimension = dimension;
//        this.xmin = xmin;
//        this.xmax = xmax;
//        switch (dimension) {
//            case 1: {
//                this.ymin = Double.NEGATIVE_INFINITY;
//                this.ymax = Double.POSITIVE_INFINITY;
//                this.zmin = Double.NEGATIVE_INFINITY;
//                this.zmax = Double.POSITIVE_INFINITY;
////                this.xwidth = xmax - xmin;
////                this.ywidth = Double.POSITIVE_INFINITY;
////                this.zwidth = Double.POSITIVE_INFINITY;
//                if (xwidth() < 0) {
//                    throw new IllegalArgumentException("xwidth<0");
//                }
//                break;
//            }
//            case 2: {
//                this.ymin = ymin;
//                this.ymax = ymax;
//                this.zmin = Double.NEGATIVE_INFINITY;
//                this.zmax = Double.POSITIVE_INFINITY;
////                this.xwidth = xmax - xmin;
////                this.ywidth = ymax - ymin;
////                this.zwidth = Double.POSITIVE_INFINITY;
//                if (xwidth() < 0 || ywidth() < 0) {
//                    throw new IllegalArgumentException("xwidth<0 || ywidth<0");
//                }
//                break;
//            }
//            case 3: {
//                this.ymin = ymin;
//                this.ymax = ymax;
//                this.zmin = zmin;
//                this.zmax = zmax;
////                this.xwidth = xmax - xmin;
////                this.ywidth = ymax - ymin;
////                this.zwidth = zmax - zmin;
//                if (xwidth() < 0 || ywidth() < 0 || zwidth() < 0) {
//                    throw new IllegalArgumentException("xwidth<0 || ywidth<0 || zwidth<0");
//                }
//                break;
//            }
//            default: {
//                throw new IllegalArgumentException("Invalid dimension");
//            }
//
//        }
//        if (xwidth() < 0 || ywidth() < 0 || zwidth() < 0) {
//            throw new IllegalArgumentException("xwidth<0 || ywidth<0 || zwidth<0");
//        }
//    }
//
//
//
//    public Range range(double[] x) {
//        if (isEmpty()) {
//            return null;
//        }
//        int[] lx = Maths.rangeCO(x, xmin(), xmax());
//        if (lx == null) {
//            return null;
//        }
//        return Range.forBounds(lx[0], lx[1]);
////        int[] ints = rangeArray(x);
////        return ints == null ? null : Range.forBounds(ints[0], ints[1]);
//    }
//
//    public Range range(double[] x, double[] y) {
//        if (isEmpty()) {
//            return null;
//        }
//        switch (dimension) {
//            case 1: {
//                int[] lx = Maths.rangeCO(x, xmin(), xmax());
//                if (lx == null) {
//                    return null;
//                }
//                return Range.forBounds(lx[0], lx[1], 0, y.length - 1);
//            }
//        }
//
//        int[] lx = Maths.rangeCO(x, xmin(), xmax());
//        if (lx == null) {
//            return null;
//        }
//        int[] ly = Maths.rangeCO(y, ymin(), ymax());
//        if (ly == null) {
//            return null;
//        }
//        return Range.forBounds(lx[0], lx[1], ly[0], ly[1]);
//    }
//
//    public Range range(double[] x, double[] y, double[] z) {
//        if (isEmpty()) {
//            return null;
//        }
//        switch (dimension) {
//            case 1: {
//                int[] lx = Maths.rangeCO(x, xmin, xmax);
//                if (lx == null) {
//                    return null;
//                }
//                return Range.forBounds(lx[0], lx[1], 0, y.length - 1, 0, z.length - 1);
//            }
//            case 2: {
//                int[] lx = Maths.rangeCO(x, xmin, xmax);
//                if (lx == null) {
//                    return null;
//                }
//                int[] ly = Maths.rangeCO(y, ymin, ymax);
//                if (ly == null) {
//                    return null;
//                }
//                return Range.forBounds(lx[0], lx[1], ly[0], ly[1], 0, z.length - 1);
//            }
//        }
//        int[] lx = Maths.rangeCO(x, xmin, xmax);
//        if (lx == null) {
//            return null;
//        }
//        int[] ly = Maths.rangeCO(y, ymin, ymax);
//        if (ly == null) {
//            return null;
//        }
//        int[] lz = Maths.rangeCO(z, zmin, zmax);
//        if (lz == null) {
//            return null;
//        }
//        return Range.forBounds(lx[0], lx[1], ly[0], ly[1], lz[0], lz[1]);
//    }
//
//    public boolean contains(double x) {
//        return x >= xmin && x < xmax;
//    }
//
//    public boolean contains(double x, double y) {
//        return x >= xmin && x < xmax && y >= ymin && y < ymax;
//    }
//
//    public boolean contains(double x, double y, double z) {
//        return x >= xmin
//                && x < xmax
//                && y >= ymin
//                && y < ymax
//                && z >= zmin
//                && z < zmax;
//    }
//
//    public boolean includes(DomainDefault other) {
//        if (other.isEmpty() || other.isNaN()) {
//            return true;
//        }
//        int d = Maths.max(dimension, other.dimension);
//        switch (d) {
//            case 1: {
//                return (other.xmin >= xmin
//                        && other.xmax <= xmax
//                        && other.ymin >= ymin);
//            }
//            case 2: {
//                return (other.xmin >= xmin
//                        && other.xmax <= xmax
//                        && other.ymin >= ymin
//                        && other.ymax <= ymax);
//            }
//        }
//        return (other.xmin >= xmin
//                && other.xmax <= xmax
//                && other.ymin >= ymin
//                && other.ymax <= ymax
//                && other.zmin >= zmin
//                && other.zmax <= zmax);
//    }
//
//    public DomainDefault intersect(DomainDefault other) {
//        if (other == null) {
//            return this;
//        }
////        double x1= Maths.max(xmin,other.xmin);
////        double x2=Maths.min(xmax,other.xmax);
////        double y1=Maths.max(ymin,other.ymin);
////        double y2=Maths.min(ymax,other.ymax);
//        int dim = Maths.max(this.dimension, other.dimension);
//        double x1 = max(xmin, other.xmin);
//        double x2 = min(xmax, other.xmax);
//        double y1 = max(ymin, other.ymin);
//        double y2 = min(ymax, other.ymax);
//        double z1 = max(zmin, other.zmin);
//        double z2 = min(zmax, other.zmax);
//        x2 = max(x2, x1);
//        y2 = max(y2, y1);
//        z2 = max(z2, z1);
//        // some workaround
//        double delta = x1 - x2;
//        if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//            x1 = x2 = 0.0D;
//        }
//        delta = y1 - y2;
//        if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//            y1 = y2 = 0.0D;
//        }
//        delta = z1 - z2;
//        if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//            z1 = z2 = 0.0D;
//        }
//        return new DomainDefault(x1, x2, y1, y2, z1, z2, dim);
//    }
//
////    private static class Domain2 {
////
////        private double min;
////        private double max;
////
////        public Domain2(double min, double max) {
////            this.min = min;
////            this.max = max;
////        }
////
////        public boolean isNaN() {
////            return Double.isNaN(min) || Double.isNaN(max);
////        }
////
////        public boolean isEmpty() {
////            return max <= min;
////        }
////
////        public Domain2 union(Domain2 o) {
////            if (isEmpty()) {
////                return o;
////            } else if (o.isEmpty()) {
////                return this;
////            } else {
////                return new Domain2(min(min, o.min), max(max, o.max));
////            }
////        }
////    }
//
//    /**
//     * expand the domain to help including the given domain
//     *
//     * @param a
//     * @param b
//     * @return
//     */
//    private double[] expand(double[] a, double[] b) {
//        if (a[1] <= a[0]) {
//            return b;
//        } else if (b[1] <= b[0]) {
//            return a;
//        } else {
//            return new double[]{min(a[0], b[0]), max(a[1], b[1])};
//        }
//    }
//
//    /**
//     * expand the domain to help including the given domain
//     *
//     * @param other domain to be included in the formed doamin
//     * @return
//     */
//    public Domain expand(Domain other) {
//        if (other.equals(this)) {
//            return this;
//        }
////        if(isNaN() || other.isNaN()){
////            System.out.println("Okkay");
////        }
//
//        Domain d1 = null;
//        Domain d2 = null;
//        if (this.dimension() < other.dimension()) {
//            d1 = this;
//            d2 = other;
//        } else {
//            d2 = this;
//            d1 = other;
//        }
//        switch (d1.dimension()) {
//            case 1: {
//                switch (d2.dimension()) {
//                    case 1: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        return Domain.forBounds(x[0], x[1]);
//                    }
//                    case 2: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        double y1 = d2.ymin();
//                        double y2 = d2.ymax();
//                        return Domain.forBounds(x[0], x[1], y1, y2);
//                    }
//                    case 3: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        double y1 = d2.ymin;
//                        double y2 = d2.ymax;
//                        double z1 = d2.zmin;
//                        double z2 = d2.zmax;
//                        return Domain.forBounds(x[0], x[1], y1, y2, z1, z2);
//                    }
//                    default: {
//                        throw new IllegalArgumentException("Unsupported");
//                    }
//                }
//            }
//            case 2: {
//                switch (d2.dimension) {
//                    case 2: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        double[] y = expand(new double[]{d1.ymin, d1.ymax}, new double[]{d2.ymin, d2.ymax});
//                        return DomainDefault.forBounds(x[0], x[1], y[0], y[1]);
//                    }
//                    case 3: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        double[] y = expand(new double[]{d1.ymin, d1.ymax}, new double[]{d2.ymin, d2.ymax});
//                        double z1 = d2.zmin;
//                        double z2 = d2.zmax;
//                        return DomainDefault.forBounds(x[0], x[1], y[0], y[1], z1, z2);
//                    }
//                    default: {
//                        throw new IllegalArgumentException("Unsupported");
//                    }
//                }
//
//            }
//            case 3: {
//                switch (d2.dimension) {
//                    case 3: {
//                        double[] x = expand(new double[]{d1.xmin, d1.xmax}, new double[]{d2.xmin, d2.xmax});
//                        double[] y = expand(new double[]{d1.ymin, d1.ymax}, new double[]{d2.ymin, d2.ymax});
//                        double[] z = expand(new double[]{d1.zmin, d1.zmax}, new double[]{d2.zmin, d2.zmax});
//                        return DomainDefault.forBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
//                    }
//                    default: {
//                        throw new IllegalArgumentException("Unsupported");
//                    }
//                }
//            }
//            default: {
//                throw new IllegalArgumentException("Unsupported");
//            }
//        }
//
//    }
//
//    public boolean isValid() {
//        return !isEmpty() && !isInfinite() && !isNaN();
//    }
//
//    public boolean isEmpty() {
//        switch (dimension) {
//            case 1: {
//                return xmin >= xmax;
//            }
//            case 2: {
//                return xmin >= xmax || ymin >= ymax;
//            }
//        }
//        return xmin >= xmax || ymin >= ymax || zmin >= zmax;
//
//    }
//
//    public boolean isInfinite() {
//        switch (dimension) {
//            case 1: {
//                return Double.isInfinite(xmin) || Double.isInfinite(xmax);
//            }
//            case 2: {
//                return Double.isInfinite(xmin) || Double.isInfinite(xmax) || Double.isInfinite(ymin) || Double.isInfinite(ymax);
//            }
//        }
//        return Double.isInfinite(xmin) || Double.isInfinite(xmax) || Double.isInfinite(ymin) || Double.isInfinite(ymax) || Double.isInfinite(zmin) || Double.isInfinite(zmax);
//    }
//
//    public boolean isInfiniteX() {
//        return Double.isInfinite(xmin) || Double.isInfinite(xmax);
//    }
//
//    public boolean isInfiniteY() {
//        return Double.isInfinite(ymin) || Double.isInfinite(ymax);
//    }
//
//    public boolean isInfiniteZ() {
//        return Double.isInfinite(zmin) || Double.isInfinite(zmax);
//    }
//
//    public boolean isNaN() {
//        switch (dimension) {
//            case 1: {
//                return Double.isNaN(xmin) || Double.isNaN(xmax);
//            }
//            case 2: {
//                return Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax);
//            }
//        }
//        return Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax) || Double.isNaN(zmin) || Double.isNaN(zmax);
//    }
//
//    public DomainDefault translate(double x, double y) {
//        return DomainDefault.forBounds(
//                xmin + x, xmax + x, ymin + y,
//                ymax + y
//        );
//    }
//
//    public DomainDefault getSymmetricX(double x0) {
//        double x1 = xmin + 2 * (x0 - xmin);
//        double x2 = xmax + 2 * (x0 - xmax);
//        return DomainDefault.forBounds(
//                Maths.min(x1, x2), Maths.max(x1, x2), ymin,
//                ymax
//        );
//    }
//
//    public DomainDefault getSymmetricY(double y0) {
//        double y1 = ymin + 2 * (y0 - ymin);
//        double y2 = ymax + 2 * (y0 - ymax);
//        return DomainDefault.forBounds(
//                xmin, xmax, Maths.min(y1, y2),
//                Maths.max(y1, y2)
//        );
//    }
//
//    public double getCenterX() {
//        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY) {
//            return 0.0;
//        } else {
//            return (xmin + xmax) / 2;
//        }
//    }
//
//    public double getCenterY() {
//        if (ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY) {
//            return 0.0;
//        } else {
//            return (ymin + ymax) / 2;
//        }
//    }
//
//    public double getCenterZ() {
//        if (zmin == Double.NEGATIVE_INFINITY && zmax == Double.POSITIVE_INFINITY) {
//            return 0.0;
//        } else {
//            return (zmin + zmax) / 2;
//        }
//    }
//
//    public double getXMin() {
//        return xmin;
//    }
//
//    public double getXMax() {
//        return xmax;
//    }
//
//    public double getZMin() {
//        return zmin;
//    }
//
//    public double getZMax() {
//        return zmax;
//    }
//
//    public double getYMin() {
//        return ymin;
//    }
//
//    public double getYMax() {
//        return ymax;
//    }
//
//    public double getXwidth() {
//        return xwidth();
//    }
//
//    public double getYwidth() {
//        return ywidth();
//    }
//
//    //    public StructureContext getStructureContext() {
////        return structureContext;
////    }
////
////    public void setStructureContext(StructureContext structureContext) {
////        this.structureContext = structureContext;
////    }
////  def /(step: Int) = times(step);
////
////  def /(step: (Int, Int)) = times(step._1, step._1);
////
////  def /!(step: Double) = steps(step);
////
////  def /!(step: (Double, Double)) = steps(step._1, step._1);
//    public double getZwidth() {
//        return zwidth();
//    }
//
//    public DomainDefault x() {
//        return forBounds(xmin, xmax);
//    }
//
//    public DomainDefault y() {
//        return forBounds(ymin, ymax);
//    }
//
//    public DomainDefault z() {
//        return forBounds(zmin, zmax);
//    }
//
//    public DomainDefault getDomainX() {
//        return forBounds(xmin, xmax);
//    }
//
//    public DomainDefault getDomainY() {
//        return forBounds(ymin, ymax);
//    }
//
//    public DomainDefault getDomainZ() {
//        return DomainDefault.forBounds(zmin, zmax);
//    }
//
//    //    public static class DomainXY extends Domain{
////
////    }
////
////    public static class DomainXYZ extends DomainXY{
////
////    }
//    public String dump() {
//        switch (getDimension()) {
//            case 1: {
//                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//                h.add("x", xmin + "->" + xmax);
//                return h.toString();
//            }
//            case 2: {
//                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//                h.add("x", xmin + "->" + xmax);
//                h.add("y", ymin + "->" + ymax);
//                return h.toString();
//            }
//        }
//        Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//        h.add("x", xmin + "->" + xmax);
//        h.add("y", ymin + "->" + ymax);
//        h.add("z", zmin + "->" + zmax);
//        return h.toString();
//    }
//
//    @Override
//    public String toString() {
//        return dump();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final DomainDefault other = (DomainDefault) obj;
//        if (this.xmin != other.xmin) {
//            return false;
//        }
//        if (this.xmax != other.xmax) {
//            return false;
//        }
//        if (this.ymin != other.ymin) {
//            return false;
//        }
//        if (this.ymax != other.ymax) {
//            return false;
//        }
//        if (this.zmin != other.zmin) {
//            return false;
//        }
//        if (this.zmax != other.zmax) {
//            return false;
//        }
//        return true;
//    }
//
//    private int hashCode0() {
//        int hash = 7;
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmin) ^ (Double.doubleToLongBits(this.xmin) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmax) ^ (Double.doubleToLongBits(this.xmax) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymin) ^ (Double.doubleToLongBits(this.ymin) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymax) ^ (Double.doubleToLongBits(this.ymax) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmin) ^ (Double.doubleToLongBits(this.zmin) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmax) ^ (Double.doubleToLongBits(this.zmax) >>> 32));
//        return hash;
//    }
//
//    @Override
//    public int hashCode() {
//        return hashCode0();
////        return hashCode;
//    }
//
//    public Samples steps(double xstep) {
//        return steps(xstep, xstep, xstep);
//    }
//
//    public Samples steps(double xstep, double ystep) {
//        return steps(xstep, ystep, xstep);
//    }
//
//    public Samples steps(double xstep, double ystep, double zstep) {
//        switch (dimension) {
//            case 1: {
//                if (isInfiniteX()) {
//                    throw new IllegalArgumentException("Infinite X Domain");
//                }
//                return Samples.absolute(Maths.dsteps(xmin, xmax, xstep));
//            }
//            case 2: {
//                if (isInfiniteX()) {
//                    throw new IllegalArgumentException("Infinite X Domain");
//                }
//                if (isInfiniteY()) {
//                    throw new IllegalArgumentException("Infinite Y Domain");
//                }
//                return Samples.absolute(
//                        Maths.dsteps(xmin, xmax, xstep),
//                        Maths.dsteps(ymin, ymax, (ystep <= 0) ? xstep : ystep));
//            }
//        }
//        if (isInfiniteX()) {
//            throw new IllegalArgumentException("Infinite X Domain");
//        }
//        if (isInfiniteY()) {
//            throw new IllegalArgumentException("Infinite Y Domain");
//        }
//        if (isInfiniteZ()) {
//            throw new IllegalArgumentException("Infinite Z Domain");
//        }
//        return Samples.absolute(
//                Maths.dsteps(xmin, xmax, xstep),
//                Maths.dsteps(ymin, ymax, (ystep <= 0) ? xstep : ystep),
//                Maths.dsteps(zmin, zmax, (zstep <= 0) ? xstep : zstep)
//        );
//    }
//
//    public Samples times(int xtimes) {
//        switch (dimension) {
//            case 1: {
//                return times(xtimes, 1, 1);
//            }
//            case 2: {
//                return times(xtimes, xtimes, 1);
//            }
//        }
//        return times(xtimes, xtimes, xtimes);
//    }
//
//    public Samples times(int xtimes, int ytimes) {
//        return times(xtimes, ytimes, xtimes);
//    }
//
//    public Samples toAbsolute(Samples s) {
//        if (s.isAbsolute()) {
//            return s;
//        }
//        return new Samples(true, s.getDimension()
//                , toAbsolute(s.getX(), getXMin(), getXMax())
//                , toAbsolute(s.getY(), getYMin(), getYMax())
//                , toAbsolute(s.getZ(), getZMin(), getZMax())
//        );
//    }
//
//    private static double[] toAbsolute(double[] base, double min, double max) {
//        if (base == null) {
//            return null;
//        }
//        double[] r = new double[base.length];
//        if (Double.isInfinite(min) || Double.isInfinite(max)) {
//            if (Double.isInfinite(min) && Double.isInfinite(max)) {
//                //all zero
////                for (int i = 0; i < r.length; i++) {
////                    r[i] = 0;
////                }
//            } else {
//                for (int i = 0; i < r.length; i++) {
//                    r[i] = min;
//                }
//            }
//            return r;
//        } else {
//            double w = max - min;
//            for (int i = 0; i < r.length; i++) {
//                r[i] = min + w * base[i];
//            }
//            return r;
//        }
//    }
//
//    public Samples times(int xtimes, int ytimes, int ztimes) {
//        switch (dimension) {
//            case 1: {
//                if (isInfiniteX()) {
//                    throw new IllegalArgumentException("Infinite X Domain");
//                }
//                if (ytimes == 1 && ztimes == 1) {
//                    return Samples.absolute(Maths.dtimes(xmin, xmax, xtimes));
//                }
//                return Samples.absolute(
//                        Maths.dtimes(xmin, xmax, xtimes),
//                        ArrayUtils.fill(new double[ytimes], Double.NaN),
//                        ArrayUtils.fill(new double[ztimes], Double.NaN)
//                );
//            }
//            case 2: {
//                if (isInfiniteX()) {
//                    throw new IllegalArgumentException("Infinite X Domain");
//                }
//                if (isInfiniteY()) {
//                    throw new IllegalArgumentException("Infinite Y Domain");
//                }
//                if (ztimes == 1) {
//                    return Samples.absolute(
//                            Maths.dtimes(xmin, xmax, xtimes),
//                            Maths.dtimes(ymin, ymax, (ytimes <= 0) ? xtimes : ytimes)
//                    );
//                }
//                return Samples.absolute(
//                        Maths.dtimes(xmin, xmax, xtimes),
//                        Maths.dtimes(ymin, ymax, (ytimes <= 0) ? xtimes : ytimes),
//                        ArrayUtils.fill(new double[ztimes], Double.NaN)
//                );
//            }
//        }
//        if (isInfiniteX()) {
//            throw new IllegalArgumentException("Infinite X Domain");
//        }
//        if (isInfiniteY()) {
//            throw new IllegalArgumentException("Infinite Y Domain");
//        }
//        if (isInfiniteZ()) {
//            throw new IllegalArgumentException("Infinite Z Domain");
//        }
//        return Samples.absolute(
//                Maths.dtimes(xmin, xmax, xtimes),
//                Maths.dtimes(ymin, ymax, (ytimes <= 0) ? xtimes : ytimes),
//                Maths.dtimes(zmin, zmax, (ztimes <= 0) ? xtimes : ztimes)
//        );
//    }
//
//    //
////  def xtimes(times: Int) = Maths.dtimes(xmin, xmax, times);
////
////  def ysteps(step: Double) = Maths.dsteps(ymin, ymax, step);
////
////  def ytimes(times: Int) = Maths.dtimes(ymin, ymax, times);
//    //
////
//    public double[] xsteps(double step) {
//        return Maths.dsteps(xmin, xmax, step);
//    }
//
//    public double[] ysteps(double step) {
//        return Maths.dsteps(ymin, ymax, step);
//    }
//
//    public double[] zsteps(double step) {
//        return Maths.dsteps(xmin, xmax, step);
//    }
//
//    public double[] xtimes(int count) {
//        return Maths.dtimes(xmin, xmax, count);
//    }
//
//    public double[] ytimes(int count) {
//        return Maths.dtimes(ymin, ymax, count);
//    }
//
//    public double[] ztimes(int count) {
//        return Maths.dtimes(zmin, zmax, count);
//    }
//
//    @Override
//    public DomainDefault clone() {
//        return (DomainDefault) super.clone();
//    }
//
//    public DomainDefault scale(double x) {
//        return scale(x, x, x);
//    }
//
//    public DomainDefault scale(double x, double y) {
//        return scale(x, y, x);
//    }
//
//    public DomainDefault scale(double x, double y, double z) {
//        switch (dimension) {
//            case 1: {
//                return forWidth(xmin, xwidth() * x);
//            }
//            case 2: {
//                return forWidth(xmin, xwidth() * x, ymin, ywidth() * y);
//            }
//        }
//        return forWidth(xmin, xwidth() * x, ymin, ywidth() * y, zmin, zwidth() * z);
//    }
//
//    @Override
//    public Polygon toPolygon() {
//        return new Polygon(this);
//    }
//
//    @Override
//    public DomainDefault getDomain() {
//        return this;
//    }
//
//    @Override
//    public boolean isRectangular() {
//        return true;
//    }
//
//    public void checkDimension(int dimension) {
//        if (dimension != this.dimension) {
//            throw new IllegalArgumentException("Invalid dimension " + dimension + ". Expected " + dimension);
//        }
//    }
//
//    public DomainDefault expandDimension(int dimension) {
//        if (this.dimension == dimension) {
//            return this;
//        } else if (this.dimension < dimension) {
//            return toDomain(dimension);
//        } else {
//            throw new IllegalArgumentException("Unable to expand Domain to " + dimension);
//        }
//    }
//
//    public DomainDefault narrowDimension(int dimension) {
//        if (this.dimension == dimension) {
//            return this;
//        } else if (this.dimension > dimension) {
//            return toDomain(dimension);
//        } else {
//            throw new IllegalArgumentException("Unable to narrow Domain to " + dimension);
//        }
//    }
//
//    public DomainDefault toDomain(int dimension) {
//        if (dimension == this.dimension) {
//            return this;
//        }
//        switch (dimension) {
//            case 1:
//                return toDomainX();
//            case 2:
//                return toDomainXY();
//            case 3:
//                return toDomainXYZ();
//        }
//        throw new IllegalArgumentException("invalid dimension " + dimension);
//    }
//
//    //    public enum Type {
////
////        LENGTH, MAX, OLD_STYLE
////    }
//    public DomainDefault toDomainX() {
//        return forBounds(this.xmin, this.xmax, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//    }
//
//    public DomainDefault toDomainXY() {
//        return forBounds(this.xmin, this.xmax, this.ymin, this.ymax);
//    }
//
//    public DomainDefault toDomainXYZ() {
//        return forBounds(this.xmin, this.xmax, this.ymin, this.ymax, this.zmin, this.zmax);
//    }
//
//    public int getDimension() {
//        return dimension;
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
//    public DomainDefault transformLinear(double a0x, double b0) {
//        return forBounds(a0x * xmin + b0, a0x * xmax + b0);
//    }
//
//    public boolean isFull() {
//        switch (dimension) {
//            case 1: {
//                return isInfiniteX();
//            }
//            case 2: {
//                return isInfiniteX() && isInfiniteY();
//            }
//        }
//        return isInfiniteX() && isInfiniteY() && isInfiniteZ();
//    }
//
//    public boolean isUnconstrainedX() {
//        return (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY)
//                || (Double.isNaN(xmin) && Double.isNaN(xmax));
//    }
//
//    public boolean isUnconstrainedY() {
//        return (ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY)
//                || (Double.isNaN(ymin) && Double.isNaN(ymax));
//    }
//
//    public boolean isUnconstrainedZ() {
//        return (zmin == Double.NEGATIVE_INFINITY && zmax == Double.POSITIVE_INFINITY)
//                || (Double.isNaN(zmin) && Double.isNaN(zmax));
//    }
//
//    public boolean isUnconstrained() {
//        return isUnconstrainedX() && isUnconstrainedY() && isUnconstrainedZ();
//    }
//
//    public DomainDefault eval(Axis a1, Axis a2, Axis a3, int dim) {
//        double xmin = Double.NaN;
//        double xmax = Double.NaN;
//        double ymin = Double.NaN;
//        double ymax = Double.NaN;
//        double zmin = Double.NaN;
//        double zmax = Double.NaN;
//        if (a1 == a2 || a2 == a3 || a1 == a3 || a1 == null || a2 == null || a3 == null) {
//            throw new IllegalArgumentException("Invalid Axis suite");
//        }
//        switch (a1) {
//            case X: {
//                xmin = this.xmin;
//                xmax = this.xmax;
//                break;
//            }
//            case Y: {
//                xmin = this.ymin;
//                xmax = this.ymax;
//                break;
//            }
//            case Z: {
//                xmin = this.zmin;
//                xmax = this.zmax;
//                break;
//            }
//        }
//        switch (a2) {
//            case X: {
//                ymin = this.xmin;
//                ymax = this.xmax;
//                break;
//            }
//            case Y: {
//                ymin = this.ymin;
//                ymax = this.ymax;
//                break;
//            }
//            case Z: {
//                ymin = this.zmin;
//                ymax = this.zmax;
//                break;
//            }
//        }
//        switch (a3) {
//            case X: {
//                zmin = this.xmin;
//                zmax = this.xmax;
//                break;
//            }
//            case Y: {
//                zmin = this.ymin;
//                zmax = this.ymax;
//                break;
//            }
//            case Z: {
//                zmin = this.zmin;
//                zmax = this.zmax;
//                break;
//            }
//        }
//        return DomainDefault.forBounds(xmin, xmax, ymin, ymax, zmin, zmax).toDomain(dim);
//    }
//
//    public DomainDefault cross(DomainDefault other) {
//        int a = getDimension();
//        int b = other.getDimension();
//        if (b + a >= 4) {
//            throw new IllegalArgumentException("Unsupported Domain crossing");
//        }
//        switch (a) {
//            case 1: {
//                switch (b) {
//                    case 1: {
//                        return forBounds(this.xmin, this.xmax, other.xmin, other.xmax);
//                    }
//                    case 2: {
//                        return forBounds(this.xmin, this.xmax, other.xmin, other.xmax, other.ymin, other.ymax);
//                    }
//                }
//                break;
//            }
//            case 2: {
//                switch (b) {
//                    case 1: {
//                        return forBounds(this.xmin, this.xmax, this.ymin, this.ymax, other.xmin, other.xmax);
//                    }
//                }
//                break;
//            }
//        }
//        throw new IllegalArgumentException("Unsupported Domain crossing");
//    }
//
//    @Override
//    public Geometry translateGeometry(double x, double y) {
//        switch (dimension) {
//            case 1: {
//                if (y == 0) {
//                    return DomainDefault.forWidth(xmin + x, ywidth());
//
//                } else {
//                    throw new IllegalArgumentException("Unsupported");
//                }
//            }
//            case 2: {
//                return DomainDefault.forWidth(xmin + x, ymax + y, xwidth(), ywidth());
//            }
//            case 3: {
//                return DomainDefault.forWidth(xmin + x, ymax + y, xwidth(), ywidth(), zmin, zwidth());
//            }
//        }
//        throw new IllegalArgumentException("Unsupported Domain dimension");
//    }
//
//    @Override
//    public boolean isPolygonal() {
//        return getDimension() == 2;
//    }
//
//    @Override
//    public boolean isTriangular() {
//        return false;
//    }
//
//    @Override
//    public boolean isSingular() {
//        return isEmpty();
//    }
//
//    @Override
//    public Triangle toTriangle() {
//        throw new IllegalArgumentException("Not Triangular");
//    }
//
//    @Override
//    public Path2D.Double getPath() {
//        Path2D.Double p = new Path2D.Double();
//        p.moveTo(xmin, ymin);
//        p.lineTo(xmax, ymin);
//        p.lineTo(xmax, ymax);
//        p.lineTo(xmin, ymax);
//        p.closePath();
//        return p;
//    }
//
//    public double xmin() {
//        return xmin;
//    }
//
//    public double xmax() {
//        return xmax;
//    }
//
//    public double ymin() {
//        return ymin;
//    }
//
//    public double ymax() {
//        return ymax;
//    }
//
//    public double zmin() {
//        return zmin;
//    }
//
//    public double zmax() {
//        return zmax;
//    }
//
//    public double xwidth() {
//        return xmax - xmin;
//    }
//
//    public double ywidth() {
//        return ymax - ymin;
//    }
//
//    public double zwidth() {
//        return zmax - zmin;
//    }
//    public int dimension() {
//        return dimension;
//    }
//}
