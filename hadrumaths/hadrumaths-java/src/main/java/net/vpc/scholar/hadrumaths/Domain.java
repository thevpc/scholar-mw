package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.geom.Surface;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
public abstract class Domain /*extends AbstractGeometry*/ implements Serializable, Dumpable/*, PolygonBuilder*/, Cloneable, Expr {

    public static final Domain NaNX = new DomainX(Double.NaN, Double.NaN);
    public static final Domain NaNXY = new DomainXY(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    public static final Domain NaNXYZ = new DomainXYZ(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);

    public static final Domain EMPTYX = new DomainX(0, 0);
    public static final Domain FULLX = new DomainX(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Domain FULLXY = new DomainXY(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Domain EMPTYXY = new DomainXY(0, 0, 0, 0);
    public static final Domain FULLXYZ = new DomainXYZ(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Domain EMPTYXYZ = new DomainXYZ(0, 0, 0, 0, 0, 0);
    public static final Domain ZEROX = FULLX;//forBounds(0, 0);
    public static final Domain ZEROXY = FULLXY;//forBounds(0, 0, 0, 0);
    public static final Domain ZEROXYZ = FULLXYZ;//forBounds(0, 0, 0, 0, 0, 0);
    protected final static double epsilon = 1E-12;
    private static final long serialVersionUID = 1L;


    public static Domain ZERO(int dim) {
        return FULL(dim);
    }

    public static Domain EMPTY(int dim) {
        switch (dim) {
            case 1: {
                return EMPTYX;
            }
            case 2: {
                return EMPTYXY;
            }
            case 3: {
                return EMPTYXYZ;
            }
        }
        throw new IllegalArgumentException("Unsupported dimension " + dim);
    }

    public static Domain FULL(int dim) {
        switch (dim) {
            case 1: {
                return FULLX;
            }
            case 2: {
                return FULLXY;
            }
            case 3: {
                return FULLXYZ;
            }
        }
        throw new IllegalArgumentException("Unsupported dimension " + dim);
    }

    //    public Domain(Rectangle2D rectangle) {
//        this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), Domain.Type.LENGTH);
//    }
//    public Domain(Rectangle rectangle) {
//        this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), Domain.Type.LENGTH);
//    }
//    public Domain(DomainX d) {
//        this(d.xmin(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, d.xmax, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 2);
//    }
//    public Domain(double xmin, double xmax) {
//        this(xmin, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, xmax, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
//    }
//    public Domain(double xmin, double ymin, double max_x_or_width, double max_y_or_height, Type type) {
//        this(xmin, ymin,
//                Double.NEGATIVE_INFINITY,
//                type.equals(Type.LENGTH) ? (xmin + max_x_or_width) : max_x_or_width,
//                type.equals(Type.LENGTH) ? (ymin + max_y_or_height) : max_y_or_height
//                , Double.POSITIVE_INFINITY,
//                2
//        );
//    }
    //    public static DomainXY forItervallBounds(double xmin, double xmax, double ymin, double ymax) {
//        return new DomainXY(xmin, ymin, xmax, ymax);
//    }
//
//    public static DomainXY forItervallLengths(double xmin, double xlen, double ymin, double ylen) {
//        return new DomainXY(xmin, ymin, xmin + xlen, ymin + ylen);
//    }
//
//    public static DomainXY forCornerBounds(double xmin, double ymin, double xmax, double ymax) {
//        return new DomainXY(xmin, ymin, xmax, ymax);
//    }
//
//    public static DomainXY forCornerLengths(double xmin, double ymin, double xlen, double ylen) {
//        return new DomainXY(xmin, ymin, xmin + xlen, ymin + ylen);
//    }
//    public Domain(double xmin, double xmax, double ymin, double ymax) {
//        this(xmin, xmax, ymin, ymax, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 2);
//    }
    public static Domain NaN(int dim) {
        switch (dim) {
            case 1: {
                return NaNX;
            }
            case 2: {
                return NaNXY;
            }
            case 3: {
                return NaNXYZ;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static Range range(Domain a, Domain b, double[] x) {
        return (b == null ? a : a.intersect(b)).range(x);
    }

    public static Range range(Domain a, Domain b, double[] x, double[] y) {
        return ((b == null) ? a : (a.intersect(b))).range(x, y);
    }

    public static Range range(Domain a, Domain b, double[] x, double[] y, double[] z) {
        return ((b == null) ? a : (a.intersect(b))).range(x, y, z);
    }

    //    public int[][] coeff(double[] xVector, double[] yVector) {
//        int xl = xVector.length;
//        int yl = yVector.length;
//        int[][] r = new int[yl][xl];
//        double xi = 0;
//        double yj = 0;
//        for (int yIndex = 0; yIndex < yl; yIndex++) {
//            yj = yVector[yIndex];
//            for (int xIndex = 0; xIndex < xl; xIndex++) {
//                xi = xVector[xIndex];
//                if (xi >= xmin && xi <= xmax && yj >= ymin && yj <= ymax) {
//                    r[yIndex][xIndex] = 1;
//                }
//            }
//        }
//        return r;
//    }
//    public int[] coeff(double[] xVector, double y) {
//        //new
//        int xl = xVector.length;
//        int[] r = new int[xl];
//        double xi = 0;
//        double yj = y;
//        for (int xIndex = 0; xIndex < xl; xIndex++) {
//            xi = xVector[xIndex];
//            if (xi >= xmin && xi <= xmax && yj >= ymin && yj <= ymax) {
//                r[xIndex] = 1;
//            }
//        }
//        return r;
//    }
//    public int[] coeff(double x, double[] yVector) {
//        //new
//        int yl = yVector.length;
//        int[] r = new int[yl];
//        double yj = 0;
//        for (int yIndex = 0; yIndex < yl; yIndex++) {
//            yj = yVector[yIndex];
//            if (x >= xmin && x <= xmax && yj >= ymin && yj <= ymax) {
//                r[yIndex] = 1;
//            }
//        }
//        return r;
//    }
    public static Domain intersect(Domain d1, Domain d2, Domain d3) {
        return d1.intersect(d2).intersect(d3);
    }

    public static Domain forBounds(double xmin, double xmax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY) {
            return FULLX;
        } else if ((/*xmin == 0 && */xmax <= xmin)) {
            return EMPTYX;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax)) {
            return NaNX;
        }
        return new DomainX(xmin, xmax);
    }

    public static Domain forBounds(double xmin, double xmax, double ymin, double ymax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY && ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY) {
            return FULLXY;
        } else if ((xmin == 0 && xmax <= xmin) && (ymin == 0 && ymax <= ymin)) {
            return EMPTYXY;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax)) {
            return NaNXY;
        }
        //domain(4.800000000000001->5, FULL)
        return new DomainXY(xmin, xmax, ymin, ymax);
    }

    public static Domain forBounds(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY && ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY && zmin == Double.NEGATIVE_INFINITY && zmax == Double.POSITIVE_INFINITY) {
            return FULLXYZ;
        } else if ((xmin == 0 && xmax <= xmin) && (ymin == 0 && ymax <= ymin) && (zmin == 0 && zmax <= ymin)) {
            return EMPTYXYZ;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax) || Double.isNaN(zmin) || Double.isNaN(zmax)) {
            return NaNXYZ;
        }
        return new DomainXYZ(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Domain forWidth(double xmin, double width) {
        return new DomainX(xmin, xmin + width);
    }

    public static Domain forWidth(double xmin, double xwidth, double ymin, double ywidth) {
        return forBounds(xmin, xmin + xwidth, ymin, ymin + ywidth);
    }

    public static Domain forPoints(double xmin, double xmax) {
        return forBounds(xmin, xmax);
    }

    public static Domain forPoints(double xmin, double ymin, double xmax, double ymax) {
        return forBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain forPoints(double xmin, double ymin, double zmin, double xmax, double ymax, double zmax) {
        return forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Domain forWidthXY(double xmin, double ymin, double xwidth, double ywidth) {
        return forBounds(xmin, xmin + xwidth, ymin, ymin + ywidth);
    }

    public static Domain forWidthXYZ(double xmin, double ymin, double zmin, double xwidth, double ywidth, double zwidth) {
        return forBounds(xmin, xmin + xwidth, ymin, ymin + ywidth, zmin, zmin + zwidth);
    }


    public static Domain forWidth(double xmin, double xwidth, double ymin, double ywidth, double zmin, double zwidth) {
        return forBounds(xmin, xmin + xwidth, ymin, ymin + ywidth, zmin, zmin + zwidth);
    }

//    public static Domain toDomainXY(Domain d) {
//        return forBounds(d.xmin, d.xmax, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//    }

    public static Domain toDomainXY(Rectangle2D rectangle) {
        return forWidth(rectangle.getX(), rectangle.getWidth(), rectangle.getY(), rectangle.getHeight());
    }

    /**
     * 3 dimensions domain if z is not null otherwise will call forArray(x, y)
     *
     * @param x x values in the domain, ordered asc
     * @param y y values in the domain, ordered asc
     * @return 2 dimensions domain if y is not null otherwise will call forArray(x)
     */
    public static Domain forArray(double[] x, double[] y) {
        if (y == null) {
            return forArray(x);
        }
        Domain xd = forArray(x);
        Domain yd = forArray(y);
        return forBounds(xd.xmin(), xd.xmax(), yd.xmin(), yd.xmax());
    }

    /**
     * 3 dimensions domain if z is not null otherwise will call forArray(x, y)
     *
     * @param x x values in the domain, ordered asc
     * @param y y values in the domain, ordered asc
     * @param z z values in the domain, ordered asc
     * @return 3 dimensions domain if z is not null otherwise will call forArray(x, y)
     */
    public static Domain forArray(double[] x, double[] y, double[] z) {
        if (z == null) {
            return forArray(x, y);
        }
        Domain xd = forArray(x);
        Domain yd = forArray(y);
        Domain zd = forArray(z);
        return forBounds(xd.xmin(), xd.xmax(), yd.xmin(), yd.xmax(), zd.xmin(), zd.xmax());
    }

    //    public int[] rangesArray(double[] x, double[] y) {
//        if (isEmpty()) {
//            return null;
//        }
//        int[] lx = Maths.range(x, xmin, xmax);
//        if (lx == null) {
//            return null;
//        }
//        int[] ly = Maths.range(y, ymin, ymax);
//        if (ly == null) {
//            return null;
//        }
//        return new int[]{lx[0], lx[1], ly[0], ly[1]};
//    }
//    public int[] rangesArray(double[] x, double[] y, double[] z) {
//        if (isEmpty()) {
//            return null;
//        }
//        int[] lx = Maths.range(x, xmin, xmax);
//        if (lx == null) {
//            return null;
//        }
//        int[] ly = Maths.range(y, ymin, ymax);
//        if (ly == null) {
//            return null;
//        }
//        int[] lz = Maths.range(z, zmin, zmax);
//        if (lz == null) {
//            return null;
//        }
//        return new int[]{lx[0], lx[1], ly[0], ly[1], lz[0], lz[1]};
//    }

    /**
     * creates a one dimension domain bound by x[0] and x[x.length - 1]
     *
     * @param x domain (asc) ordered values
     * @return ine dimension dmain
     */
    public static Domain forArray(double[] x) {
        if (x == null || x.length == 0) {
            throw new IllegalArgumentException("Invalid Domain interval");
        }
        return forBounds(x[0], x[x.length - 1]);
    }

    protected static double min(double d1, double d2) {
//        if (Double.isNaN(d1)) {
//            return d2;
//        }
//        if (Double.isNaN(d2)) {
//            return d1;
//        }
//        return Math.min(d1, d2);
        if (d1 != d1) {
            return d2;
        }
        if (d2 != d2) {
            return d1;
        }
        return d1 <= d2 ? d1 : d2;
    }

    protected static double max(double d1, double d2) {
//        if (Double.isNaN(d1)) {
//            return d2;
//        }
//        if (Double.isNaN(d2)) {
//            return d1;
//        }
//        return Math.max(d1, d2);
        if (d1 != d1) {
            return d2;
        }
        if (d2 != d2) {
            return d1;
        }
        return d1 >= d2 ? d1 : d2;
    }

    private static double[] toAbsolute(double[] base, double min, double max) {
        if (base == null) {
            return null;
        }
        double[] r = new double[base.length];
        if (Double.isInfinite(min) || Double.isInfinite(max)) {
            if (Double.isInfinite(min) && Double.isInfinite(max)) {
                //all zero
//                for (int i = 0; i < r.length; i++) {
//                    r[i] = 0;
//                }
            } else {
                for (int i = 0; i < r.length; i++) {
                    r[i] = min;
                }
            }
            return r;
        } else {
            double w = max - min;
            for (int i = 0; i < r.length; i++) {
                r[i] = min + w * base[i];
            }
            return r;
        }
    }

    public double[] intersect(double[] x) {
        Range r = range(x);
        return ArrayUtils.subarray(x, r);
    }

    public Range range(double[] x) {
        if (isEmpty()) {
            return null;
        }
        int[] lx = Maths.rangeCO(x, xmin(), xmax());
        if (lx == null) {
            return null;
        }
        return Range.forBounds(lx[0], lx[1]);
//        int[] ints = rangeArray(x);
//        return ints == null ? null : Range.forBounds(ints[0], ints[1]);
    }

    public Range range(double[] x, double[] y) {
        if (isEmpty()) {
            return null;
        }
        switch (dimension()) {
            case 1: {
                int[] lx = Maths.rangeCO(x, xmin(), xmax());
                if (lx == null) {
                    return null;
                }
                return Range.forBounds(lx[0], lx[1], 0, y.length - 1);
            }
        }

        int[] lx = Maths.rangeCO(x, xmin(), xmax());
        if (lx == null) {
            return null;
        }
        int[] ly = Maths.rangeCO(y, ymin(), ymax());
        if (ly == null) {
            return null;
        }
        return Range.forBounds(lx[0], lx[1], ly[0], ly[1]);
    }

    public Range range(double[] x, double[] y, double[] z) {
        if (isEmpty()) {
            return null;
        }
        switch (dimension()) {
            case 1: {
                int[] lx = Maths.rangeCO(x, xmin(), xmax());
                if (lx == null) {
                    return null;
                }
                return Range.forBounds(lx[0], lx[1], 0, y.length - 1, 0, z.length - 1);
            }
            case 2: {
                int[] lx = Maths.rangeCO(x, xmin(), xmax());
                if (lx == null) {
                    return null;
                }
                int[] ly = Maths.rangeCO(y, ymin(), ymax());
                if (ly == null) {
                    return null;
                }
                return Range.forBounds(lx[0], lx[1], ly[0], ly[1], 0, z.length - 1);
            }
        }
        int[] lx = Maths.rangeCO(x, xmin(), xmax());
        if (lx == null) {
            return null;
        }
        int[] ly = Maths.rangeCO(y, ymin(), ymax());
        if (ly == null) {
            return null;
        }
        int[] lz = Maths.rangeCO(z, zmin(), zmax());
        if (lz == null) {
            return null;
        }
        return Range.forBounds(lx[0], lx[1], ly[0], ly[1], lz[0], lz[1]);
    }

    public double computeDouble(double x, double y, double z) {
        return contains(x, y, z) ? 1 : 0;
    }

    public boolean contains(double x) {
        return x >= xmin() && x < xmax();
    }

    //    @Override
    public boolean contains(double x, double y) {
        return x >= xmin() && x < xmax() && y >= ymin() && y < ymax();
    }

    public boolean contains(double x, double y, double z) {
        return x >= xmin()
                && x < xmax()
                && y >= ymin()
                && y < ymax()
                && z >= zmin()
                && z < zmax();
    }

    public boolean includes(Domain other) {
        if (other.isEmpty() || other.isNaN()) {
            return true;
        }
        int d = Math.max(dimension(), other.dimension());
        switch (d) {
            case 1: {
                return (other.xmin() >= xmin()
                        && other.xmax() <= xmax()
                        && other.ymin() >= ymin());
            }
            case 2: {
                return (other.xmin() >= xmin()
                        && other.xmax() <= xmax()
                        && other.ymin() >= ymin()
                        && other.ymax() <= ymax());
            }
        }
        return (other.xmin() >= xmin()
                && other.xmax() <= xmax()
                && other.ymin() >= ymin()
                && other.ymax() <= ymax()
                && other.zmin() >= zmin()
                && other.zmax() <= zmax());
    }

//    private static class Domain2 {
//
//        private double min;
//        private double max;
//
//        public Domain2(double min, double max) {
//            this.min = min;
//            this.max = max;
//        }
//
//        public boolean isNaN() {
//            return Double.isNaN(min) || Double.isNaN(max);
//        }
//
//        public boolean isEmpty() {
//            return max <= min;
//        }
//
//        public Domain2 union(Domain2 o) {
//            if (isEmpty()) {
//                return o;
//            } else if (o.isEmpty()) {
//                return this;
//            } else {
//                return new Domain2(min(min, o.min), max(max, o.max));
//            }
//        }
//    }

    public Domain intersect(Domain other) {
        if (other == null) {
            return this;
        }
//        double x1= Maths.max(xmin,other.xmin);
//        double x2=Maths.min(xmax,other.xmax);
//        double y1=Maths.max(ymin,other.ymin);
//        double y2=Maths.min(ymax,other.ymax);
        int d_t = this.dimension();
        int d_o = other.dimension();
        int dim = Math.max(d_t, d_o);
        double x1_t = xmin();
        double x2_t = xmax();
        double x1_o = other.xmin();
        double x2_o = other.xmax();

        double x1 = max(x1_t, x1_o);
        double x2 = min(x2_t, x2_o);
        // some workaround
        double delta = x1 - x2;
        if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
            x1 = x2 = 0.0D;
        }

        switch (dim) {
            case 1: {
                if (d_t == 1 && x1 == x1_t && x2 == x2_t) {
                    return this;
                }
                if (d_o == 1 && x1 == x1_o && x2 == x2_o) {
                    return other;
                }
                return forBounds(x1, x2);
            }
            case 2: {
                double y1_t = ymin();
                double y1_o = other.ymin();
                double y2_t = ymax();
                double y2_o = other.ymax();
                double y1 = max(y1_t, y1_o);
                double y2 = min(y2_t, y2_o);
                x2 = max(x2, x1);
                y2 = max(y2, y1);
                delta = y1 - y2;
                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
                    y1 = y2 = 0.0D;
                }

                if (d_t == 2 && x1 == x1_t && x2 == x2_t && y1 == y1_t && y2 == y2_t) {
                    return this;
                }
                if (d_o == 2 && x1 == x1_o && x2 == x2_o && y1 == y1_o && y2 == y2_o) {
                    return other;
                }

                return forBounds(x1, x2, y1, y2);
            }
            case 3: {
                double y1_t = ymin();
                double y1_o = other.ymin();
                double y2_t = ymax();
                double y2_o = other.ymax();
                double y1 = max(y1_t, y1_o);
                double y2 = min(y2_t, y2_o);


                x2 = max(x2, x1);
                y2 = max(y2, y1);
                delta = y1 - y2;
                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
                    y1 = y2 = 0.0D;
                }

                double z1_t = zmin();
                double z1_o = other.zmin();
                double z2_t = zmax();
                double z2_o = other.zmax();
                double z1 = max(z1_t, z1_o);
                double z2 = min(z2_t, z2_o);

                z2 = max(z2, z1);
                delta = z1 - z2;
                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
                    z1 = z2 = 0.0D;
                }

                if (d_t == 2 && x1 == x1_t && x2 == x2_t && y1 == y1_t && y2 == y2_t && z1 == z1_t && z2 == z2_t) {
                    return this;
                }
                if (d_o == 2 && x1 == x1_o && x2 == x2_o && y1 == y1_o && y2 == y2_o && z1 == z1_o && z2 == z2_o) {
                    return other;
                }

                return forBounds(x1, x2, y1, y2, z1, z2);
            }
        }
        throw new IllegalArgumentException("Unsupported domain " + dim);
    }

    /**
     * expand the domain to help including the given domain
     *
     * @param a
     * @param b
     * @return
     */
    private double[] expand(double[] a, double[] b) {
        if (a[1] <= a[0]) {
            return b;
        } else if (b[1] <= b[0]) {
            return a;
        } else {
            return new double[]{min(a[0], b[0]), max(a[1], b[1])};
        }
    }

    public Domain union(Domain other) {
        return expand(other);
    }

    /**
     * expand the domain to help including the given domain
     *
     * @param other domain to be included in the formed doamin
     * @return
     */
    public Domain expand(Domain other) {
        if (other.equals(this)) {
            return this;
        }
//        if(isNaN() || other.isNaN()){
//            System.out.println("Okkay");
//        }

        Domain d1 = null;
        Domain d2 = null;
        if (this.dimension() < other.dimension()) {
            d1 = this;
            d2 = other;
        } else {
            d2 = this;
            d1 = other;
        }
        switch (d1.dimension()) {
            case 1: {
                switch (d2.dimension()) {
                    case 1: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        return Domain.forBounds(x[0], x[1]);
                    }
                    case 2: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double y1 = d2.ymin();
                        double y2 = d2.ymax();
                        return Domain.forBounds(x[0], x[1], y1, y2);
                    }
                    case 3: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double y1 = d2.ymin();
                        double y2 = d2.ymax();
                        double z1 = d2.zmin();
                        double z2 = d2.zmax();
                        return Domain.forBounds(x[0], x[1], y1, y2, z1, z2);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            }
            case 2: {
                switch (d2.dimension()) {
                    case 2: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                        return Domain.forBounds(x[0], x[1], y[0], y[1]);
                    }
                    case 3: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                        double z1 = d2.zmin();
                        double z2 = d2.zmax();
                        return Domain.forBounds(x[0], x[1], y[0], y[1], z1, z2);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }

            }
            case 3: {
                switch (d2.dimension()) {
                    case 3: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                        double[] z = expand(new double[]{d1.zmin(), d1.zmax()}, new double[]{d2.zmin(), d2.zmax()});
                        return Domain.forBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }

    }

    public boolean isValid() {
        return !isEmpty() && !isInfinite() && !isNaN();
    }

    public boolean isEmpty() {
        switch (dimension()) {
            case 1: {
                return xmin() >= xmax();
            }
            case 2: {
                return xmin() >= xmax() || ymin() >= ymax();
            }
        }
        return xmin() >= xmax() || ymin() >= ymax() || zmin() >= zmax();

    }

    public boolean isInfinite() {
        switch (dimension()) {
            case 1: {
                return Double.isInfinite(xmin()) || Double.isInfinite(xmax());
            }
            case 2: {
                return Double.isInfinite(xmin()) || Double.isInfinite(xmax()) || Double.isInfinite(ymin()) || Double.isInfinite(ymax());
            }
        }
        return Double.isInfinite(xmin()) || Double.isInfinite(xmax()) || Double.isInfinite(ymin()) || Double.isInfinite(ymax()) || Double.isInfinite(zmin()) || Double.isInfinite(zmax());
    }

    public boolean isInfiniteX() {
        return Double.isInfinite(xmin()) || Double.isInfinite(xmax());
    }

    public boolean isInfiniteY() {
        return Double.isInfinite(ymin()) || Double.isInfinite(ymax());
    }

    public boolean isInfiniteZ() {
        return Double.isInfinite(zmin()) || Double.isInfinite(zmax());
    }

    public boolean isNaN() {
        switch (dimension()) {
            case 1: {
                return Double.isNaN(xmin()) || Double.isNaN(xmax());
            }
            case 2: {
                return Double.isNaN(xmin()) || Double.isNaN(xmax()) || Double.isNaN(ymin()) || Double.isNaN(ymax());
            }
        }
        return Double.isNaN(xmin()) || Double.isNaN(xmax()) || Double.isNaN(ymin()) || Double.isNaN(ymax()) || Double.isNaN(zmin()) || Double.isNaN(zmax());
    }

    public Domain expandAll(double xmin, double xmax) {
        switch (getDomainDimension()) {
            case 1:
                return expand(xmin, xmax);
            case 2:
                return expand(xmin, xmax, xmin, xmax);
            case 3:
                return expand(xmin, xmax, xmin, xmax, xmin, xmax);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain expand(double xmin, double xmax) {
        return expand(xmin, xmax, 0, 0, 0, 0);
    }

    public Domain expand(double xmin, double xmax, double ymin, double ymax) {
        return expand(xmin, xmax, ymin, xmax, 0, 0);
    }

    public Domain expand(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        if (xmin < 0 || Double.isNaN(xmin)) {
            throw new IllegalArgumentException("Invalid xmin");
        }
        if (xmax < 0 || Double.isNaN(xmax)) {
            throw new IllegalArgumentException("Invalid xmax");
        }
        if (ymin < 0 || Double.isNaN(ymin)) {
            throw new IllegalArgumentException("Invalid ymin");
        }
        if (ymax < 0 || Double.isNaN(ymax)) {
            throw new IllegalArgumentException("Invalid ymax");
        }
        if (zmin < 0 || Double.isNaN(zmin)) {
            throw new IllegalArgumentException("Invalid ymin");
        }
        if (zmax < 0 || Double.isNaN(zmax)) {
            throw new IllegalArgumentException("Invalid ymax");
        }
        switch (getDomainDimension()) {
            case 1: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                return forBounds(xmin0, xmax0);
            }
            case 2: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                double ymin0 = getYMin() - ymin;
                double ymax0 = getYMax() + ymax;
                return forBounds(xmin0, xmax0, ymin0, ymax0);
            }
            case 3: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                double ymin0 = getYMin() - ymin;
                double ymax0 = getYMax() + ymax;
                double zmin0 = getZMin() - zmin;
                double zmax0 = getZMax() + zmax;
                return forBounds(xmin0, xmax0, ymin0, ymax0, zmin0, zmax0);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain translate(double x, double y) {
        return Domain.forBounds(
                xmin() + x, xmax() + x, ymin() + y,
                ymax() + y
        );
    }

    public Domain getSymmetricX(double x0) {
        double x1 = xmin() + 2 * (x0 - xmin());
        double x2 = xmax() + 2 * (x0 - xmax());
        return Domain.forBounds(
                Math.min(x1, x2), Math.max(x1, x2), ymin(),
                ymax()
        );
    }

    public Domain getSymmetricY(double y0) {
        double y1 = ymin() + 2 * (y0 - ymin());
        double y2 = ymax() + 2 * (y0 - ymax());
        return Domain.forBounds(
                xmin(), xmax(), Math.min(y1, y2),
                Math.max(y1, y2)
        );
    }

    public double getCenterX() {
        if (xmin() == Double.NEGATIVE_INFINITY && xmax() == Double.POSITIVE_INFINITY) {
            return 0.0;
        } else {
            return (xmin() + xmax()) / 2;
        }
    }

    public double getCenterY() {
        if (ymin() == Double.NEGATIVE_INFINITY && ymax() == Double.POSITIVE_INFINITY) {
            return 0.0;
        } else {
            return (ymin() + ymax()) / 2;
        }
    }

    public double getCenterZ() {
        if (zmin() == Double.NEGATIVE_INFINITY && zmax() == Double.POSITIVE_INFINITY) {
            return 0.0;
        } else {
            return (zmin() + zmax()) / 2;
        }
    }

    public double getXMin() {
        return xmin();
    }

    public double getXMax() {
        return xmax();
    }

    public double getZMin() {
        return zmin();
    }

    public double getZMax() {
        return zmax();
    }

    public double getYMin() {
        return ymin();
    }

    public double getYMax() {
        return ymax();
    }

    public double getXwidth() {
        return xwidth();
    }

    public double getYwidth() {
        return ywidth();
    }

    //    public StructureContext getStructureContext() {
//        return structureContext;
//    }
//
//    public void setStructureContext(StructureContext structureContext) {
//        this.structureContext = structureContext;
//    }
//  def /(step: Int) = times(step);
//
//  def /(step: (Int, Int)) = times(step._1, step._1);
//
//  def /!(step: Double) = steps(step);
//
//  def /!(step: (Double, Double)) = steps(step._1, step._1);
    public double getZwidth() {
        return zwidth();
    }

    public Domain x() {
        return forBounds(xmin(), xmax());
    }

    public Domain y() {
        return forBounds(ymin(), ymax());
    }

    public Domain z() {
        return forBounds(zmin(), zmax());
    }

    public Domain getDomainX() {
        return forBounds(xmin(), xmax());
    }

    public Domain getDomainY() {
        return forBounds(ymin(), ymax());
    }

    public Domain getDomainZ() {
        return Domain.forBounds(zmin(), zmax());
    }

    //    public static class DomainXY extends Domain{
//
//    }
//
//    public static class DomainXYZ extends DomainXY{
//
//    }
    public String dump() {
        switch (getDimension()) {
            case 1: {
                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
                h.add("x", xmin() + "->" + xmax());
                return h.toString();
            }
            case 2: {
                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
                h.add("x", xmin() + "->" + xmax());
                h.add("y", ymin() + "->" + ymax());
                return h.toString();
            }
        }
        Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
        h.add("x", xmin() + "->" + xmax());
        h.add("y", ymin() + "->" + ymax());
        h.add("z", zmin() + "->" + zmax());
        return h.toString();
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Domain other = (Domain) obj;
        if (this.xmin() != other.xmin()) {
            return false;
        }
        if (this.xmax() != other.xmax()) {
            return false;
        }
        if (this.ymin() != other.ymin()) {
            return false;
        }
        if (this.ymax() != other.ymax()) {
            return false;
        }
        if (this.zmin() != other.zmin()) {
            return false;
        }
        if (this.zmax() != other.zmax()) {
            return false;
        }
        return true;
    }

    private int hashCode0() {
        int hash = 7;
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmin()) ^ (Double.doubleToLongBits(this.xmin()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmax()) ^ (Double.doubleToLongBits(this.xmax()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymin()) ^ (Double.doubleToLongBits(this.ymin()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymax()) ^ (Double.doubleToLongBits(this.ymax()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmin()) ^ (Double.doubleToLongBits(this.zmin()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmax()) ^ (Double.doubleToLongBits(this.zmax()) >>> 32));
        return hash;
    }

    @Override
    public int hashCode() {
        return hashCode0();
//        return hashCode;
    }

    public AbsoluteSamples steps(double xstep) {
        return steps(xstep, xstep, xstep);
    }

    public AbsoluteSamples steps(double xstep, double ystep) {
        return steps(xstep, ystep, xstep);
    }

    public AbsoluteSamples steps(double xstep, double ystep, double zstep) {
        switch (dimension()) {
            case 1: {
                if (isInfiniteX()) {
                    throw new IllegalArgumentException("Infinite X Domain");
                }
                return Samples.absolute(Maths.dsteps(xmin(), xmax(), xstep));
            }
            case 2: {
                if (isInfiniteX()) {
                    throw new IllegalArgumentException("Infinite X Domain");
                }
                if (isInfiniteY()) {
                    throw new IllegalArgumentException("Infinite Y Domain");
                }
                return Samples.absolute(
                        Maths.dsteps(xmin(), xmax(), xstep),
                        Maths.dsteps(ymin(), ymax(), (ystep <= 0) ? xstep : ystep));
            }
        }
        if (isInfiniteX()) {
            throw new IllegalArgumentException("Infinite X Domain");
        }
        if (isInfiniteY()) {
            throw new IllegalArgumentException("Infinite Y Domain");
        }
        if (isInfiniteZ()) {
            throw new IllegalArgumentException("Infinite Z Domain");
        }
        return Samples.absolute(
                Maths.dsteps(xmin(), xmax(), xstep),
                Maths.dsteps(ymin(), ymax(), (ystep <= 0) ? xstep : ystep),
                Maths.dsteps(zmin(), zmax(), (zstep <= 0) ? xstep : zstep)
        );
    }

    public AbsoluteSamples times(int xtimes) {
        switch (dimension()) {
            case 1: {
                return times(xtimes, 1, 1);
            }
            case 2: {
                return times(xtimes, xtimes, 1);
            }
        }
        return times(xtimes, xtimes, xtimes);
    }

    public AbsoluteSamples times(int xtimes, int ytimes) {
        return times(xtimes, ytimes, xtimes);
    }

    public AbsoluteSamples toAbsolute(Samples s) {
        if (s instanceof AbsoluteSamples) {
            return (AbsoluteSamples) s;
        }
        if (s instanceof RelativeSamples) {
            RelativeSamples r = (RelativeSamples) s;
            return new AbsoluteSamples(
                    toAbsolute(r.getX(), getXMin(), getXMax())
                    , toAbsolute(r.getY(), getYMin(), getYMax())
                    , toAbsolute(r.getZ(), getZMin(), getZMax())
            );
        }
        throw new IllegalArgumentException("Unsupported yet");
    }

    public AbsoluteSamples times(int xtimes, int ytimes, int ztimes) {
        switch (dimension()) {
            case 1: {
                if (isInfiniteX()) {
                    throw new IllegalArgumentException("Infinite X Domain");
                }
                if (ytimes == 1 && ztimes == 1) {
                    return Samples.absolute(Maths.dtimes(xmin(), xmax(), xtimes));
                }
                return Samples.absolute(
                        Maths.dtimes(xmin(), xmax(), xtimes),
                        ArrayUtils.fill(new double[ytimes], Double.NaN),
                        ArrayUtils.fill(new double[ztimes], Double.NaN)
                );
            }
            case 2: {
                if (isInfiniteX()) {
                    throw new IllegalArgumentException("Infinite X Domain");
                }
                if (isInfiniteY()) {
                    throw new IllegalArgumentException("Infinite Y Domain");
                }
                if (ztimes == 1) {
                    return Samples.absolute(
                            Maths.dtimes(xmin(), xmax(), xtimes),
                            Maths.dtimes(ymin(), ymax(), (ytimes <= 0) ? xtimes : ytimes)
                    );
                }
                return Samples.absolute(
                        Maths.dtimes(xmin(), xmax(), xtimes),
                        Maths.dtimes(ymin(), ymax(), (ytimes <= 0) ? xtimes : ytimes),
                        ArrayUtils.fill(new double[ztimes], Double.NaN)
                );
            }
        }
        if (isInfiniteX()) {
            throw new IllegalArgumentException("Infinite X Domain");
        }
        if (isInfiniteY()) {
            throw new IllegalArgumentException("Infinite Y Domain");
        }
        if (isInfiniteZ()) {
            throw new IllegalArgumentException("Infinite Z Domain");
        }
        return Samples.absolute(
                Maths.dtimes(xmin(), xmax(), xtimes),
                Maths.dtimes(ymin(), ymax(), (ytimes <= 0) ? xtimes : ytimes),
                Maths.dtimes(zmin(), zmax(), (ztimes <= 0) ? xtimes : ztimes)
        );
    }

    //
//  def xtimes(times: Int) = Maths.dtimes(xmin, xmax, times);
//
//  def ysteps(step: Double) = Maths.dsteps(ymin, ymax, step);
//
//  def ytimes(times: Int) = Maths.dtimes(ymin, ymax, times);
    //
//
    public double[] xsteps(double step) {
        return Maths.dsteps(xmin(), xmax(), step);
    }

    public double[] ysteps(double step) {
        return Maths.dsteps(ymin(), ymax(), step);
    }

    public double[] zsteps(double step) {
        return Maths.dsteps(xmin(), xmax(), step);
    }

    public double[] xtimes(int count) {
        return Maths.dtimes(xmin(), xmax(), count);
    }

    public double[] ytimes(int count) {
        return Maths.dtimes(ymin(), ymax(), count);
    }

    public double[] ztimes(int count) {
        return Maths.dtimes(zmin(), zmax(), count);
    }

    @Override
    public Domain clone() {
        try {
            return (Domain) super.clone();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Domain scale(double x) {
        return scale(x, x, x);
    }

    public Domain scale(double x, double y) {
        return scale(x, y, x);
    }

    public Domain scale(double x, double y, double z) {
        switch (dimension()) {
            case 1: {
                return forWidth(xmin(), xwidth() * x);
            }
            case 2: {
                return forWidth(xmin(), xwidth() * x, ymin(), ywidth() * y);
            }
        }
        return forWidth(xmin(), xwidth() * x, ymin(), ywidth() * y, zmin(), zwidth() * z);
    }

    //    @Override
    public Polygon toPolygon() {
        return new Polygon(this);
    }

    @Override
    public Domain getDomain() {
        return this;
    }

    //    @Override
    public boolean isRectangular() {
        return true;
    }

    public void checkDimension(int dimension) {
        if (dimension != this.dimension()) {
            throw new IllegalArgumentException("Invalid dimension " + dimension + ". Expected " + dimension);
        }
    }

    public Domain expandDimension(int dimension) {
        if (this.dimension() == dimension) {
            return this;
        } else if (this.dimension() < dimension) {
            return toDomain(dimension);
        } else {
            throw new IllegalArgumentException("Unable to expand Domain to " + dimension);
        }
    }

    public Domain narrowDimension(int dimension) {
        if (this.dimension() == dimension) {
            return this;
        } else if (this.dimension() > dimension) {
            return toDomain(dimension);
        } else {
            throw new IllegalArgumentException("Unable to narrow Domain to " + dimension);
        }
    }

    public Domain toDomain(int dimension) {
        if (dimension == this.dimension()) {
            return this;
        }
        switch (dimension) {
            case 1:
                return toDomainX();
            case 2:
                return toDomainXY();
            case 3:
                return toDomainXYZ();
        }
        throw new IllegalArgumentException("invalid dimension " + dimension);
    }

    //    public enum Type {
//
//        LENGTH, MAX, OLD_STYLE
//    }
    public Domain toDomainX() {
        return forBounds(this.xmin(), this.xmax());
    }

    public Domain toDomainXY() {
        return forBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax());
    }

    public Domain toDomainXYZ() {
        return forBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax(), this.zmin(), this.zmax());
    }

    public int getDimension() {
        return dimension();
    }

    public int[] rangeArray(double[] x) {
        if (isEmpty()) {
            return null;//new int[]{-1,-1};
        }
        double min = xmin();
        double max = xmax();

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
                cmp = (midBits == keyBits ? 0 : // Values are equal
                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
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
                cmp = (midBits == keyBits ? 0 : // Values are equal
                        (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
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

    public Domain transformLinear(double a0x, double b0) {
        return forBounds(a0x * xmin() + b0, a0x * xmax() + b0);
    }

    public boolean isFull() {
        switch (dimension()) {
            case 1: {
                return isUnconstrainedX();
            }
            case 2: {
                return isUnconstrainedX() && isUnconstrainedY();
            }
        }
        return isUnconstrainedX() && isUnconstrainedY() && isUnconstrainedZ();
    }

    public boolean isUnconstrainedX() {
        return (xmin() == Double.NEGATIVE_INFINITY && xmax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(xmin()) && Double.isNaN(xmax()));
    }

    public boolean isUnconstrainedY() {
        return (ymin() == Double.NEGATIVE_INFINITY && ymax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(ymin()) && Double.isNaN(ymax()));
    }

    public boolean isUnconstrainedZ() {
        return (zmin() == Double.NEGATIVE_INFINITY && zmax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(zmin()) && Double.isNaN(zmax()));
    }

    public boolean isUnconstrained() {
        return isUnconstrainedX() && isUnconstrainedY() && isUnconstrainedZ();
    }

    public Domain transform(Axis a1, Axis a2, Axis a3, int dim) {
        double xmin = Double.NaN;
        double xmax = Double.NaN;
        double ymin = Double.NaN;
        double ymax = Double.NaN;
        double zmin = Double.NaN;
        double zmax = Double.NaN;
        if (a1 == a2 || a2 == a3 || a1 == a3 || a1 == null || a2 == null || a3 == null) {
            throw new IllegalArgumentException("Invalid Axis suite");
        }
        switch (a1) {
            case X: {
                xmin = this.xmin();
                xmax = this.xmax();
                break;
            }
            case Y: {
                xmin = this.ymin();
                xmax = this.ymax();
                break;
            }
            case Z: {
                xmin = this.zmin();
                xmax = this.zmax();
                break;
            }
        }
        switch (a2) {
            case X: {
                ymin = this.xmin();
                ymax = this.xmax();
                break;
            }
            case Y: {
                ymin = this.ymin();
                ymax = this.ymax();
                break;
            }
            case Z: {
                ymin = this.zmin();
                ymax = this.zmax();
                break;
            }
        }
        switch (a3) {
            case X: {
                zmin = this.xmin();
                zmax = this.xmax();
                break;
            }
            case Y: {
                zmin = this.ymin();
                zmax = this.ymax();
                break;
            }
            case Z: {
                zmin = this.zmin();
                zmax = this.zmax();
                break;
            }
        }
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax).toDomain(dim);
    }

    public Domain cross(RightArrowUplet2.Double other) {
        return cross(Maths.domain(other));
    }

    public Domain cross(Domain other) {
        int a = getDimension();
        int b = other.getDimension();
        if (b + a >= 4) {
            throw new IllegalArgumentException("Unsupported Domain crossing");
        }
        switch (a) {
            case 1: {
                switch (b) {
                    case 1: {
                        return forBounds(this.xmin(), this.xmax(), other.xmin(), other.xmax());
                    }
                    case 2: {
                        return forBounds(this.xmin(), this.xmax(), other.xmin(), other.xmax(), other.ymin(), other.ymax());
                    }
                }
                break;
            }
            case 2: {
                switch (b) {
                    case 1: {
                        return forBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax(), other.xmin(), other.xmax());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported Domain crossing");
    }

    //    @Override
    public Geometry translateGeometry(double x, double y) {
        switch (dimension()) {
            case 1: {
                if (y == 0) {
                    return Domain.forWidth(xmin() + x, ywidth()).toTriangle();

                } else {
                    throw new IllegalArgumentException("Unsupported");
                }
            }
            case 2: {
                return Domain.forWidth(xmin() + x, xwidth(), ymax() + y, ywidth()).toTriangle();
            }
            case 3: {
                return Domain.forWidth(xmin() + x, xwidth(), ymax() + y, ywidth(), zmin(), zwidth()).toTriangle();
            }
        }
        throw new IllegalArgumentException("Unsupported Domain dimension");
    }

    //    @Override
    public boolean isPolygonal() {
        return getDimension() == 2;
    }

    //    @Override
    public boolean isTriangular() {
        return false;
    }

    //    @Override
    public boolean isSingular() {
        return isEmpty();
    }

    //    @Override
    public Triangle toTriangle() {
        throw new IllegalArgumentException("Not Triangular");
    }

    //    @Override
    public Path2D.Double getPath() {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(xmin(), ymin());
        p.lineTo(xmax(), ymin());
        p.lineTo(xmax(), ymax());
        p.lineTo(xmin(), ymax());
        p.closePath();
        return p;
    }

    @Override
    public boolean isDoubleTyped() {
        return true;
    }

    public double ywidth() {
        return ymax() - ymin();
    }

    public double zwidth() {
        return zmax() - zmin();
    }


    public abstract double xmin();

    public abstract double xmax();

    public abstract double ymin();

    public abstract double ymax();

    public abstract double zmin();

    public abstract double zmax();

    public abstract double xwidth();

    public abstract int dimension();


    public Domain ensureBounded(double x, double y, double z) {
        if (x <= 0 || y <= 0 || z <= 0) {
            throw new IllegalArgumentException("Unsupported bounds " + x + ", " + y + ", " + z);
        }
        if (isInfinite()) {
            return ensureBounded(Domain.forBounds(
                    -x, x, -y, y, -z, z
            ));
        }
        return this;
    }

    public Domain ensureBounded(Domain bounds) {
        if (isInfinite()) {
            double xmin = getXMin();
            double xmax = getXMax();
            if (Double.isInfinite(xmin)) {
                xmin = bounds.xmin();
            }
            if (Double.isInfinite(xmax)) {
                xmax = bounds.xmax();
            }
            double ymin = getYMin();
            double ymax = getYMax();
            if (Double.isInfinite(ymin)) {
                ymin = bounds.ymin();
            }
            if (Double.isInfinite(ymax)) {
                ymax = bounds.ymax();
            }
            double zmin = getZMin();
            double zmax = getZMax();
            if (Double.isInfinite(zmin)) {
                zmin = bounds.zmin();
            }
            if (Double.isInfinite(zmax)) {
                zmax = bounds.zmax();
            }
            switch (dimension()) {
                case 1: {
                    return forPoints(xmin, xmax);
                }
                case 2: {
                    return forPoints(xmin, ymin, xmax, ymax);
                }
                case 3: {
                    return Domain.forPoints(xmin, ymin, zmin, xmax, ymax, zmax);
                }
                default: {
                    throw new IllegalArgumentException("Unsupported dimension " + dimension());
                }
            }
        }
        return this;
    }

    public Domain multiply(Domain other) {
        return mul(other);
    }

    public Domain mul(Domain other) {
        return this.intersect(other);
    }


    @Override
    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public boolean isDouble() {
        return isUnconstrained();
    }

    @Override
    public boolean isComplex() {
        return isUnconstrained();
    }

    @Override
    public boolean isComplexExpr() {
        return true;
    }

    @Override
    public boolean isDoubleExpr() {
        return true;
    }

    @Override
    public boolean isScalarExpr() {
        return true;
    }

    @Override
    public boolean isMatrix() {
        return true;
    }

    @Override
    public Expr sub(Expr other) {
        return Maths.sub(this, other);
    }

    @Override
    public Complex toComplex() {
        return Complex.ONE;
    }

    @Override
    public double toDouble() {
        return 1;
    }

    @Override
    public Matrix toMatrix() {
        return toComplex().toMatrix();
    }

    @Override
    public boolean isDC() {
        return true;
    }

    @Override
    public boolean isDD() {
        return true;
    }

    @Override
    public boolean isDV() {
        return true;
    }

    @Override
    public boolean isDM() {
        return true;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    public Expr neg() {
        return Maths.neg(this);
    }

    public Expr mul(Geometry domain) {
        return mul(Maths.expr(domain));
    }

    public Expr multiply(Geometry domain) {
        return mul(domain);
    }

    public Expr divide(Expr other) {
        return div(other);
    }


    public Expr divide(double other) {
        return div(other);
    }

    public Expr multiply(int c) {
        return mul(c);
    }

    public Expr multiply(double c) {
        return mul(c);
    }

    public Expr multiply(Expr c) {
        return mul(c);
    }

    public Expr subtract(int c) {
        return sub(c);
    }

    public Expr subtract(double c) {
        return sub(c);
    }


    public Expr negate() {
        return neg();
    }

    @Override
    public Expr divide(int other) {
        return div(other);
    }

    @Override
    public Expr subtract(Expr other) {
        return sub(other);
    }

    @Override
    public int getDomainDimension() {
        return getDimension();
    }

    @Override
    public Expr mul(int other) {
        return DoubleValue.valueOf(other, this);
    }

    @Override
    public Expr mul(double other) {
        return DoubleValue.valueOf(other, this);
    }


    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return ComplexValue.valueOf(other, this);
    }

    @Override
    public Expr mul(Expr other) {
        return Maths.mul(this, other);
    }

    @Override
    public Expr add(int other) {
        return DoubleValue.valueOf(other + 1, this);
    }

    @Override
    public Expr add(double other) {
        return DoubleValue.valueOf(other + 1, this);
    }

    @Override
    public Expr add(Expr other) {
        return Maths.add(this, other);
    }

    @Override
    public Expr div(int other) {
        return DoubleValue.valueOf(1.0 / other, this);
    }

    @Override
    public Expr div(double other) {
        return DoubleValue.valueOf(1.0 / other, this);
    }

    @Override
    public Expr div(Expr other) {
        return Maths.div(this, other);
    }

    @Override
    public Expr sub(int other) {
        return DoubleValue.valueOf(1.0 - other, this);
    }

    @Override
    public Expr sub(double other) {
        return DoubleValue.valueOf(1.0 - other, this);
    }

    @Override
    public Domain domain() {
        return this;
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    @Override
    public Expr simplify() {
        return this;
    }

    @Override
    public Expr normalize() {
        return Maths.normalize((Expr) this);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Expr setTitle(String name) {
        if (name != null) {
            return new Any(this, name, null);
        } else {
            return this;
        }
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, double value) {
        return this;
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, Expr value) {
        return this;
    }


    public Expr setParam(String name, double value) {
        return this;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return setProperties(map, false);
    }

    @Override
    public Expr setMergedProperties(Map<String, Object> map) {
        return setProperties(map, true);
    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        if (map == null || map.isEmpty()) {
            return this;
        }
        return new Any(this, null, map);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m = new HashMap<>(1);
        m.put(name, value);
        return setProperties(m, true);
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Integer getIntProperty(String name) {
        return null;
    }

    @Override
    public Long getLongProperty(String name) {
        return null;
    }

    @Override
    public String getStringProperty(String name) {
        return null;
    }

    @Override
    public Double getDoubleProperty(String name) {
        return null;
    }

    @Override
    public DoubleToComplex toDC() {
        return new ComplexValue(Complex.ONE, this);
    }

    @Override
    public DoubleToDouble toDD() {
        return new DoubleValue(1, this);
    }

    @Override
    public DoubleToVector toDV() {
        return toDC().toDV();
    }

    @Override
    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }


    public Geometry scale(Domain newDomain) {
        return DomainScaleTool.create(getDomain(), newDomain).rescale(toGeometry());
    }

    public Geometry scale(int width, int height) {
        return DomainScaleTool.create(getDomain(), Domain.forBounds(0, width, 0, height)).rescale(toGeometry());
    }

    public Geometry intersectGeometry(Geometry geometry) {
        return toSurface().intersectGeometry(geometry);
    }

    public Geometry subtractGeometry(Geometry geometry) {
        return toSurface().subtractGeometry(geometry);
    }

    public Geometry addGeometry(Domain geometry) {
        return addGeometry(geometry.toGeometry());
    }

    public Geometry subtractGeometry(Domain geometry) {
        return subtractGeometry(geometry.toGeometry());
    }

    public Geometry intersectGeometry(Domain geometry) {
        return intersectGeometry(geometry.toGeometry());
    }

    public Geometry addGeometry(Geometry geometry) {
        return toSurface().addGeometry(geometry);
    }

    public Geometry exclusiveOrGeometry(Geometry geometry) {
        return toSurface().exclusiveOrGeometry(geometry);
    }

    public Surface toSurface() {
        return new Surface(getPath());
    }


    public Geometry toGeometry() {
        return new DomainGeometry(this);
    }

    @Override
    public Set<ParamExpr> getParams() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Expr rdiv(double other) {
        return Maths.expr(other).div(this);
    }

    @Override
    public Expr rmul(double other) {
        return Maths.expr(other).mul(this);
    }

    @Override
    public Expr radd(double other) {
        return Maths.expr(other).add(this);
    }

    @Override
    public Expr rsub(double other) {
        return Maths.expr(other).sub(this);
    }
}
