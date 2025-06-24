package net.thevpc.scholar.hadrumaths;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19 NO_NARROW
 */
public abstract class Domain implements DoubleValue, DoubleToDouble, DoubleToDoubleDefaults.DoubleToDoubleSimple {

//    public static final Domain NaNX = new DomainX(Double.NaN, Double.NaN);
//    public static final Domain NaNXY = new DomainXY(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
//    public static final Domain NaNXYZ = new DomainXYZ(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
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
    protected final int eagerHashCode;

    protected Domain(int eagerHashCode) {
        this.eagerHashCode = eagerHashCode;
    }

    public static Domain ZERO(int dim) {
        return EMPTY(dim);
//        return FULL(dim);
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

    public static Range range(Domain a, Domain b, double[] x) {
        return (b == null ? a : a.intersect(b)).range(x);
    }

    public Range range(double[] x) {
        if (isEmpty()) {
            return null;
        }
        int[] lx = Maths.rangeCO(x, xmin(), xmax());
        if (lx == null) {
            return null;
        }
        return Range.ofBounds(lx[0], lx[1]);
//        int[] ints = rangeArray(x);
//        return ints == null ? null : Range.forBounds(ints[0], ints[1]);
    }

    public Domain intersect(Domain other) {
        if (other == null || other == this) {
            return this;
        }
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
                return ofBounds(x1, x2);
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

                return ofBounds(x1, x2, y1, y2);
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

                if (d_t == 3 && x1 == x1_t && x2 == x2_t && y1 == y1_t && y2 == y2_t && z1 == z1_t && z2 == z2_t) {
                    return this;
                }
                if (d_o == 3 && x1 == x1_o && x2 == x2_o && y1 == y1_o && y2 == y2_o && z1 == z1_o && z2 == z2_o) {
                    return other;
                }

                return ofBounds(x1, x2, y1, y2, z1, z2);
            }
        }
        throw new IllegalArgumentException("Unsupported domain " + dim);
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

    public abstract double xmin();

    public abstract double xmax();

    public abstract int dimension();

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

    public static Domain ofBounds(double xmin, double xmax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY) {
            return FULLX;
        } else if ((/*xmin == 0 && */xmax <= xmin)) {
            return EMPTYX;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax)) {
            throw new IllegalArgumentException("Invalid Domain bounds : NaN");
//            return NaNX;
        }
        return new DomainX(xmin, xmax);
    }

    public abstract double ymin();

    public abstract double ymax();

    public static Domain ofBounds(double xmin, double xmax, double ymin, double ymax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY && ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY) {
            return FULLXY;
        } else if ((xmin == 0 && xmax <= xmin) && (ymin == 0 && ymax <= ymin)) {
            return EMPTYXY;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax)) {
            throw new IllegalArgumentException("Invalid Domain bounds : NaN");
//            return NaNXY;
        }
        //domain(4.800000000000001->5, FULL)
        return new DomainXY(xmin, xmax, ymin, ymax);
    }

    public abstract double zmin();

    public abstract double zmax();

    public static Domain ofBounds(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        if (xmin == Double.NEGATIVE_INFINITY && xmax == Double.POSITIVE_INFINITY && ymin == Double.NEGATIVE_INFINITY && ymax == Double.POSITIVE_INFINITY && zmin == Double.NEGATIVE_INFINITY && zmax == Double.POSITIVE_INFINITY) {
            return FULLXYZ;
        } else if ((xmin == 0 && xmax <= xmin) && (ymin == 0 && ymax <= ymin) && (zmin == 0 && zmax <= ymin)) {
            return EMPTYXYZ;
        } else if (Double.isNaN(xmin) || Double.isNaN(xmax) || Double.isNaN(ymin) || Double.isNaN(ymax) || Double.isNaN(zmin) || Double.isNaN(zmax)) {
            throw new IllegalArgumentException("Invalid Domain bounds : NaN");
//            return NaNXYZ;
        }
        return new DomainXYZ(xmin, xmax, ymin, ymax, zmin, zmax);
    }

//    public static Domain toDomainXY(Domain d) {
//        return forBounds(d.xmin, d.xmax, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//    }
    public static Range range(Domain a, Domain b, double[] x, double[] y) {
        return ((b == null) ? a : (a.intersect(b))).range(x, y);
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
                return Range.ofBounds(lx[0], lx[1], 0, y.length - 1);
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
        return Range.ofBounds(lx[0], lx[1], ly[0], ly[1]);
    }

    public static Range range(Domain a, Domain b, double[] x, double[] y, double[] z) {
        return ((b == null) ? a : (a.intersect(b))).range(x, y, z);
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
                return Range.ofBounds(lx[0], lx[1], 0, y.length - 1, 0, z.length - 1);
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
                return Range.ofBounds(lx[0], lx[1], ly[0], ly[1], 0, z.length - 1);
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
        return Range.ofBounds(lx[0], lx[1], ly[0], ly[1], lz[0], lz[1]);
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

    public static Domain ofWidthXY(double xmin, double ymin, double xwidth, double ywidth) {
        return ofBounds(xmin, xmin + xwidth, ymin, ymin + ywidth);
    }

    public static Domain ofWidthXYZ(double xmin, double ymin, double zmin, double xwidth, double ywidth, double zwidth) {
        return ofBounds(xmin, xmin + xwidth, ymin, ymin + ywidth, zmin, zmin + zwidth);
    }

    public static Domain toDomainXY(Rectangle2D rectangle) {
        return ofWidth(rectangle.getX(), rectangle.getWidth(), rectangle.getY(), rectangle.getHeight());
    }

    public static Domain ofWidth(double xmin, double xwidth, double ymin, double ywidth) {
        return ofBounds(xmin, xmin + xwidth, ymin, ymin + ywidth);
    }

    /**
     * 3 dimensions domain if z is not null otherwise will call forArray(x, y)
     *
     * @param x x values in the domain, ordered asc
     * @param y y values in the domain, ordered asc
     * @param z z values in the domain, ordered asc
     * @return 3 dimensions domain if z is not null otherwise will call
     * forArray(x, y)
     */
    public static Domain ofArray(double[] x, double[] y, double[] z) {
        if (z == null) {
            if (y == null) {
                return ofArray(x);
            }
            return ofArray(x, y);
        }
        Domain xd = ofArray(x);
        Domain yd = ofArray(y);
        Domain zd = ofArray(z);
        return ofBounds(xd.xmin(), xd.xmax(), yd.xmin(), yd.xmax(), zd.xmin(), zd.xmax());
    }

    /**
     * creates a one dimension domain bound by x[0] and x[x.length - 1]
     *
     * @param xyz domain (asc) ordered values
     * @return ine dimension dmain
     */
    public static Domain ofArray(double[] xyz) {
        if (xyz == null || xyz.length == 0) {
            throw new IllegalArgumentException("Invalid Domain interval");
        }
        return ofBounds(xyz[0], xyz[xyz.length - 1]);
    }

    /**
     * 3 dimensions domain if z is not null otherwise will call forArray(x, y)
     *
     * @param x x values in the domain, ordered asc
     * @param y y values in the domain, ordered asc
     * @return 2 dimensions domain if y is not null otherwise will call
     * forArray(x)
     */
    public static Domain ofArray(double[] x, double[] y) {
        if (y == null) {
            return ofArray(x);
        }
        Domain xd = ofArray(x);
        Domain yd = ofArray(y);
        return ofBounds(xd.xmin(), xd.xmax(), yd.xmin(), yd.xmax());
    }

    public double xvalue() {
        return valueBetween(xmin(), xmax());
    }

    public static double valueBetween(double a, double b) {
        if (a == b) {
            return a;
        }
        if (b < a) {
            return b;
        }
        if (a == Double.NEGATIVE_INFINITY && b == Double.POSITIVE_INFINITY) {
            return 0;
        }

        if (a == Double.NEGATIVE_INFINITY) {
            if (b > 0) {
                return 0;
            }
            return b - 1;
        }

        if (b == Double.POSITIVE_INFINITY) {
            if (a <= 0) {
                return 0;
            }
            return a + 1;
        }
        return (a + b) / 2;
    }

    public double yvalue() {
        return valueBetween(ymin(), ymax());
    }

    public double zvalue() {
        return valueBetween(zmin(), zmax());
    }

    public double[] intersect(double[] x) {
        Range r = range(x);
        return ArrayUtils.subarray(x, r);
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
    public boolean contains(double x) {
        switch (dimension()) {
            case 1:
                return x >= xmin() && x < xmax();
        }
        throw new MissingAxisException(Axis.Y);
    }

    //    @Override
    public boolean contains(double x, double y) {
        switch (dimension()) {
            case 1:
                return x >= xmin() && x < xmax();
            case 2:
                return x >= xmin() && x < xmax() && y >= ymin() && y < ymax();
        }
        throw new MissingAxisException(Axis.Z);
    }

    public boolean contains(double x, double y, double z) {
        switch (dimension()) {
            case 1:
                return x >= xmin() && x < xmax();
            case 2:
                return x >= xmin() && x < xmax() && y >= ymin() && y < ymax();
        }
        return x >= xmin()
                && x < xmax()
                && y >= ymin()
                && y < ymax()
                && z >= zmin()
                && z < zmax();
    }

    // a union of two domains is not necessarely a domain
//    public Domain union(Domain other) {
//        return expand(other);
//    }
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

    /**
     * expand the domain to help including the given domain
     *
     * @param other domain to be included in the formed doamin
     * @return
     */
    public Domain expand(Domain other) {
        if (other == null || other == this || other.equals(this)) {
            return this;
        }
        int dim = Math.max(dimension(), other.dimension());
        Domain d1 = this.expandDimension(dim);
        Domain d2 = other.expandDimension(dim);
        switch (dim) {
            case 1: {
                double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                return Domain.ofBounds(x[0], x[1]);
            }
            case 2: {
                double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                return Domain.ofBounds(x[0], x[1], y[0], y[1]);
            }
            case 3: {
                double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                double[] z = expand(new double[]{d1.zmin(), d1.zmax()}, new double[]{d2.zmin(), d2.zmax()});
                return Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    public Domain expandDimension(int dimension) {
        if (this.dimension() == dimension) {
            return this;
        } else if (this.dimension() < dimension) {
            return toDomain(dimension);
        } else {
            throw new IllegalArgumentException("Unable to expand Domain " + this.dimension() + " to " + dimension);
        }
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
        return ofBounds(this.xmin(), this.xmax());
    }

    public Domain toDomainXY() {
        return ofBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax());
    }

    public Domain toDomainXYZ() {
        return ofBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax(), this.zmin(), this.zmax());
    }

    private Domain expand_old(Domain other) {
        if (other == null || other == this || other.equals(this)) {
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
                        return Domain.ofBounds(x[0], x[1]);
                    }
                    case 2: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double y1 = d2.ymin();
                        double y2 = d2.ymax();
                        return Domain.ofBounds(x[0], x[1], y1, y2);
                    }
                    case 3: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double y1 = d2.ymin();
                        double y2 = d2.ymax();
                        double z1 = d2.zmin();
                        double z2 = d2.zmax();
                        return Domain.ofBounds(x[0], x[1], y1, y2, z1, z2);
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
                        return Domain.ofBounds(x[0], x[1], y[0], y[1]);
                    }
                    case 3: {
                        double[] x = expand(new double[]{d1.xmin(), d1.xmax()}, new double[]{d2.xmin(), d2.xmax()});
                        double[] y = expand(new double[]{d1.ymin(), d1.ymax()}, new double[]{d2.ymin(), d2.ymax()});
                        double z1 = d2.zmin();
                        double z2 = d2.zmax();
                        return Domain.ofBounds(x[0], x[1], y[0], y[1], z1, z2);
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
                        return Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
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

    public boolean isInfiniteInterval() {
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

    public void requireFull1() {
        if (!isUnbounded1()) {
            throw new IllegalArgumentException("Require One Dimension Unconstrainted but was " + this);
        }
    }

    public boolean isUnbounded1() {
        return dimension() == 1 && isUnboundedX();
    }

    public boolean isUnboundedX() {
        return (xmin() == Double.NEGATIVE_INFINITY && xmax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(xmin()) && Double.isNaN(xmax()));
    }

    public boolean isUnbounded2() {
        return dimension() == 2 && isUnboundedX() && isUnboundedY();
    }

    public boolean isUnboundedY() {
        return (ymin() == Double.NEGATIVE_INFINITY && ymax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(ymin()) && Double.isNaN(ymax()));
    }

    public boolean isUnbounded3() {
        return dimension() == 3 && isUnboundedX() && isUnboundedY() && isUnboundedZ();
    }

    public boolean isUnboundedZ() {
        return (zmin() == Double.NEGATIVE_INFINITY && zmax() == Double.POSITIVE_INFINITY)
                || (Double.isNaN(zmin()) && Double.isNaN(zmax()));
    }

    public Domain expandAll(double abs) {
        return expandAll(abs, abs);
    }

    public Domain expandPercent(float percent) {
        if(percent==0){
            return this;
        }
        return expandPercent(percent,percent,percent);
    }

    public Domain expandAll(double xmin, double xmax) {
        switch (getDomain().getDimension()) {
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

    private static double[] _expandPercent(double xmin, double xmax, float percent) {
        if (percent < 0) {
            throw new IllegalArgumentException("Invalid expand float " + percent);
        }
        if (xmin == Double.NEGATIVE_INFINITY) {
            if (xmax == Double.POSITIVE_INFINITY) {
                return new double[]{xmin, xmax};
            } else {
                return new double[]{xmin,
                    xmax + Math.abs(xmax) * percent
                };
            }
        } else {
            if (xmax == Double.POSITIVE_INFINITY) {
                return new double[]{xmin - Math.abs(xmax) * percent,
                     xmax};
            } else {
                double w = xmax - xmin;
                if (w == 0) {
                    w = 1;
                }
                double d = Math.abs(w) * percent;
                return new double[]{
                    xmin - d,
                    xmax + d
                };
            }
        }
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
        switch (getDomain().getDimension()) {
            case 1: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                return ofBounds(xmin0, xmax0);
            }
            case 2: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                double ymin0 = getYMin() - ymin;
                double ymax0 = getYMax() + ymax;
                return ofBounds(xmin0, xmax0, ymin0, ymax0);
            }
            case 3: {
                double xmin0 = getXMin() - xmin;
                double xmax0 = getXMax() + xmax;
                double ymin0 = getYMin() - ymin;
                double ymax0 = getYMax() + ymax;
                double zmin0 = getZMin() - zmin;
                double zmax0 = getZMax() + zmax;
                return ofBounds(xmin0, xmax0, ymin0, ymax0, zmin0, zmax0);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    
    public Domain expandPercent(float xExp, float yExp, float zExp) {
        if (xExp < 0 || Float.isNaN(xExp)) {
            throw new IllegalArgumentException("Invalid xmin");
        }
        switch (getDomain().getDimension()) {
            case 1: {
                double[] xx = _expandPercent(getXMin(), getXMax(), xExp);
                return ofBounds(xx[0], xx[1]);
            }
            case 2: {
                if (yExp < 0 || Float.isNaN(yExp)) {
                    throw new IllegalArgumentException("Invalid ymin");
                }
                double[] xx = _expandPercent(getXMin(), getXMax(), xExp);
                double[] yy = _expandPercent(getYMin(), getYMax(), yExp);
                return ofBounds(xx[0], xx[1], yy[0], yy[1]);
            }
            case 3: {
                if (yExp < 0 || Float.isNaN(yExp)) {
                    throw new IllegalArgumentException("Invalid ymin");
                }
                if (zExp < 0 || Float.isNaN(zExp)) {
                    throw new IllegalArgumentException("Invalid ymin");
                }
                double[] xx = _expandPercent(getXMin(), getXMax(), xExp);
                double[] yy = _expandPercent(getYMin(), getYMax(), yExp);
                double[] zz = _expandPercent(getZMin(), getZMax(), zExp);
                return ofBounds(xx[0], xx[1], yy[0], yy[1], zz[0], zz[1]);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain translate(double x, double y) {
        return Domain.ofBounds(
                xmin() + x, xmax() + x, ymin() + y,
                ymax() + y
        );
    }

    public Domain getSymmetricX(double x0) {
        double x1 = xmin() + 2 * (x0 - xmin());
        double x2 = xmax() + 2 * (x0 - xmax());
        return Domain.ofBounds(
                Math.min(x1, x2), Math.max(x1, x2), ymin(),
                ymax()
        );
    }

    public Domain getSymmetricY(double y0) {
        double y1 = ymin() + 2 * (y0 - ymin());
        double y2 = ymax() + 2 * (y0 - ymax());
        return Domain.ofBounds(
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

    public double getXwidth() {
        return xwidth();
    }

    public abstract double xwidth();

    public double getYwidth() {
        return ywidth();
    }

    public double ywidth() {
        return ymax() - ymin();
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

    public double zwidth() {
        return zmax() - zmin();
    }

    public Domain x() {
        return ofBounds(xmin(), xmax());
    }

    public Domain y() {
        return ofBounds(ymin(), ymax());
    }

    public Domain z() {
        return ofBounds(zmin(), zmax());
    }

    public Domain getDomainX() {
        return ofBounds(xmin(), xmax());
    }

    //    public static class DomainXY extends Domain{
//
//    }
//
//    public static class DomainXYZ extends DomainXY{
//
//    }
//    public String dump() {
//        switch (getDimension()) {
//            case 1: {
//                Dumper h = new Dumper("domain", Dumper.Type.SIMPLE).setElementSeparator(",");
//                h.add(xmin() + "->" + xmax());
//                return h.toString();
//            }
//            case 2: {
//                Dumper h = new Dumper("domain", Dumper.Type.SIMPLE).setElementSeparator(",");
//                h.add(xmin() + "->" + xmax());
//                h.add(ymin() + "->" + ymax());
//                return h.toString();
//            }
//        }
//        Dumper h = new Dumper("domain", Dumper.Type.SIMPLE).setElementSeparator(",");
//        h.add(xmin() + "->" + xmax());
//        h.add(ymin() + "->" + ymax());
//        h.add(zmin() + "->" + zmax());
//        return h.toString();
//    }
    public Domain getDomainY() {
        return ofBounds(ymin(), ymax());
    }

    public Domain getDomainZ() {
        return Domain.ofBounds(zmin(), zmax());
    }

//    private int hashCode0() {
//        int hash = 7;
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmin()) ^ (Double.doubleToLongBits(this.xmin()) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmax()) ^ (Double.doubleToLongBits(this.xmax()) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymin()) ^ (Double.doubleToLongBits(this.ymin()) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymax()) ^ (Double.doubleToLongBits(this.ymax()) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmin()) ^ (Double.doubleToLongBits(this.zmin()) >>> 32));
//        hash = 43 * hash + (int) (Double.doubleToLongBits(this.zmax()) ^ (Double.doubleToLongBits(this.zmax()) >>> 32));
//        return hash;
//    }
    @Override
    public int hashCode() {
        return eagerHashCode;
//        return hashCode0();
//        return hashCode;
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
        return this.zmax() == other.zmax();
    }

    @Override
    public String toString() {
        return FormatFactory.format(this);
    }

    public AbsoluteSamples dsteps(double xstep) {
        return dsteps(xstep, xstep, xstep);
    }

    public AbsoluteSamples dsteps(double xstep, double ystep, double zstep) {
        return steps(xstep, ystep, zstep);
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

    public boolean isInfiniteX() {
        return Double.isInfinite(xmin()) || Double.isInfinite(xmax());
    }

    public boolean isInfiniteY() {
        return Double.isInfinite(ymin()) || Double.isInfinite(ymax());
    }

    public boolean isInfiniteZ() {
        return Double.isInfinite(zmin()) || Double.isInfinite(zmax());
    }

    public AbsoluteSamples steps(double xstep) {
        return steps(xstep, xstep, xstep);
    }

    public AbsoluteSamples dsteps(double xstep, double ystep) {
        return steps(xstep, ystep);
    }

    public AbsoluteSamples steps(double xstep, double ystep) {
        return steps(xstep, ystep, xstep);
    }

    public AbsoluteSamples dtimes(int xtimes) {
        return times(xtimes);
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

    public AbsoluteSamples times(int xtimes, int ytimes, int ztimes) {
        if (isInfiniteX()) {
            throw new IllegalArgumentException("Infinite X Domain");
        }
        switch (dimension()) {
            case 1: {
                return Samples.absolute(Maths.dtimes(xmin(), xmax(), xtimes));
            }
            case 2: {
                if (isInfiniteY()) {
                    throw new IllegalArgumentException("Infinite Y Domain");
                }
                return Samples.absolute(
                        Maths.dtimes(xmin(), xmax(), xtimes),
                        Maths.dtimes(ymin(), ymax(), (ytimes <= 0) ? xtimes : ytimes)
                );
            }
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

    public AbsoluteSamples dtimes(int xtimes, int ytimes) {
        return times(xtimes, ytimes);
    }

    public AbsoluteSamples times(int xtimes, int ytimes) {
        return times(xtimes, ytimes, xtimes);
    }

    public AbsoluteSamples dtimes(int xtimes, int ytimes, int ztimes) {
        return times(xtimes, ytimes, ztimes);
    }

    public AbsoluteSamples toAbsolute(Samples s) {
        if (s instanceof AbsoluteSamples) {
            return (AbsoluteSamples) s;
        }
        if (s instanceof RelativeSamples) {
            RelativeSamples r = (RelativeSamples) s;
            return new AbsoluteSamples(
                    toAbsolute(r.getX(), getXMin(), getXMax()),
                     toAbsolute(r.getY(), getYMin(), getYMax()),
                     toAbsolute(r.getZ(), getZMin(), getZMax())
            );
        }
        throw new IllegalArgumentException("Unsupported yet");
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

    public double getXMin() {
        return xmin();
    }

    public double getXMax() {
        return xmax();
    }

    public double getYMin() {
        return ymin();
    }

    public double getYMax() {
        return ymax();
    }

    public double getZMin() {
        return zmin();
    }

    public double getZMax() {
        return zmax();
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

    public Domain scale(double x) {
        return scale(x, x, x);
    }

    public Domain scale(double x, double y, double z) {
        switch (dimension()) {
            case 1: {
                return ofWidth(xmin(), xwidth() * x);
            }
            case 2: {
                return ofWidth(xmin(), xwidth() * x, ymin(), ywidth() * y);
            }
        }
        return ofWidth(xmin(), xwidth() * x, ymin(), ywidth() * y, zmin(), zwidth() * z);
    }

    public static Domain ofWidth(double xmin, double width) {
        return new DomainX(xmin, xmin + width);
    }

    public static Domain ofWidth(double xmin, double xwidth, double ymin, double ywidth, double zmin, double zwidth) {
        return ofBounds(xmin, xmin + xwidth, ymin, ymin + ywidth, zmin, zmin + zwidth);
    }

    public Domain scale(double x, double y) {
        return scale(x, y, x);
    }

    public Domain scale(Align anchor, double x) {
        return scale(anchor, x, x, x);
    }

    public Domain scale(Align anchor, double x, double y, double z) {
        switch (dimension()) {
            case 1: {
                switch (anchor) {
                    case WEST:
                    case SOUTH_WEST:
                    case NORTH_WEST: {
                        return ofWidth(xmin(), xwidth() * x);
                    }
                    case EAST:
                    case SOUTH_EAST:
                    case NORTH_EAST: {
                        double w = xwidth() * x;
                        return ofWidth(xmin() - (w - xwidth()), w);
                    }
                    case CENTER:
                    case NORTH:
                    case SOUTH: {
                        double w = xwidth() * x;
                        return ofWidth(xmin() - (w - xwidth()) / 2, w);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + anchor);
                    }
                }
                //return forWidth(xmin(), xwidth() * x);
            }
            case 2: {
                double xwidth = xwidth() * x;
                double ywidth = ywidth() * y;
                switch (anchor) {
                    case NORTH_WEST: {
                        return ofWidth(xmin(), xwidth, ymin(), ywidth);
                    }
                    case CENTER: {
                        return ofWidth(xmin() - (xwidth - xwidth()) / 2, xwidth, ymin() - (ywidth - ywidth()) / 2, ywidth);
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + anchor);
                    }
//                    case WEST:{
//                        throw new IllegalArgumentException("Unsupported yet");
//                    }
//                    case SOUTH_WEST:{
//                        return forWidth(xmin(), xwidth, ymin(), ywidth() * y);
//                    }
//                    case EAST:
//                    case SOUTH_EAST:
//                    case NORTH_EAST: {
//                        double w = xwidth() * x;
//                        return forWidth(xmin() - (w - xwidth()), w);
//                    }
//                    case NORTH:
//                    case SOUTH:{
//                        double w = xwidth() * x;
//                        return forWidth(xmin() - (w - xwidth())/2, w);
//                    }
                }
                //return forWidth(xmin(), xwidth, ymin(), ywidth() * y);
            }
        }
        double xwidth = xwidth() * x;
        double ywidth = ywidth() * y;
        double zwidth = zwidth() * z;
        switch (anchor) {
            case NORTH_WEST: {
                return ofWidth(xmin(), xwidth, ymin(), ywidth, zmin(), zwidth);
            }
            case CENTER: {
                return ofWidth(xmin() - (xwidth - xwidth()) / 2, xwidth,
                         ymin() - (ywidth - ywidth()) / 2, ywidth,
                         zmin() - (zwidth - zwidth()) / 2, zwidth
                );
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + anchor);
            }
//                    case WEST:{
//                        throw new IllegalArgumentException("Unsupported yet");
//                    }
//                    case SOUTH_WEST:{
//                        return forWidth(xmin(), xwidth, ymin(), ywidth() * y);
//                    }
//                    case EAST:
//                    case SOUTH_EAST:
//                    case NORTH_EAST: {
//                        double w = xwidth() * x;
//                        return forWidth(xmin() - (w - xwidth()), w);
//                    }
//                    case NORTH:
//                    case SOUTH:{
//                        double w = xwidth() * x;
//                        return forWidth(xmin() - (w - xwidth())/2, w);
//                    }
        }
//        return forWidth(xmin(), xwidth() * x, ymin(), ywidth() * y, zmin(), zwidth() * z);
    }

    public Domain scale(Align anchor, double x, double y) {
        return scale(anchor, x, y, x);
    }

    //    @Override
    public Polygon toPolygon() {
        return GeometryFactory.createPolygon(this);
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

    public Domain narrowDimension(int dimension) {
        if (this.dimension() == dimension) {
            return this;
        } else if (this.dimension() > dimension) {
            return toDomain(dimension);
        } else {
            throw new IllegalArgumentException("Unable to narrow Domain to " + dimension);
        }
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

    public Domain transformLinear(double a0x, double b0) {
        return ofBounds(a0x * xmin() + b0, a0x * xmax() + b0);
    }

    public boolean isUnbounded() {
        switch (dimension()) {
            case 1: {
                return isUnboundedX();
            }
            case 2: {
                return isUnboundedX() && isUnboundedY();
            }
        }
        return isUnboundedX() && isUnboundedY() && isUnboundedZ();
    }

    @Override
    public boolean isCstComplex() {
        return true;
    }

    @Override
    public boolean isCstDouble() {
        return true;
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
        return Domain.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax).toDomain(dim);
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
                        return ofBounds(this.xmin(), this.xmax(), other.xmin(), other.xmax());
                    }
                    case 2: {
                        return ofBounds(this.xmin(), this.xmax(), other.xmin(), other.xmax(), other.ymin(), other.ymax());
                    }
                }
                break;
            }
            case 2: {
                switch (b) {
                    case 1: {
                        return ofBounds(this.xmin(), this.xmax(), this.ymin(), this.ymax(), other.xmin(), other.xmax());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported Domain crossing");
    }

    public int getDimension() {
        return dimension();
    }

    //    @Override
    public Geometry translateGeometry(double x, double y) {
        switch (dimension()) {
            case 1: {
                if (y == 0) {
                    return Domain.ofWidth(xmin() + x, ywidth()).toTriangle();

                } else {
                    throw new IllegalArgumentException("Unsupported");
                }
            }
            case 2: {
                return Domain.ofWidth(xmin() + x, xwidth(), ymax() + y, ywidth()).toTriangle();
            }
            case 3: {
                return Domain.ofWidth(xmin() + x, xwidth(), ymax() + y, ywidth(), zmin(), zwidth()).toTriangle();
            }
        }
        throw new IllegalArgumentException("Unsupported Domain dimension");
    }

    //    @Override
    public Triangle toTriangle() {
        throw new IllegalArgumentException("Not Triangular");
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return true;
//    }
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

    public double xmiddle() {
        return (xmin() + xmax()) / 2;
    }

    public double ymiddle() {
        return (ymin() + ymax()) / 2;
    }

    public double zmiddle() {
        return (zmin() + zmax()) / 2;
    }

    public Domain ensureFiniteBounds(double x, double y, double z) {
        if (x <= 0 || y <= 0 || z <= 0) {
            throw new IllegalArgumentException("Unsupported bounds " + x + ", " + y + ", " + z);
        }
        return ensureFiniteBounds(Domain.ofBounds(-x, x, -y, y, -z, z));
    }

    public Domain ensureFiniteBounds(Domain bounds) {
        switch (dimension()) {
            case 1: {
                return Domain.ofBounds(
                        xmin() == Double.NEGATIVE_INFINITY ? bounds.xmin() : xmin(),
                        xmax() == Double.POSITIVE_INFINITY ? bounds.xmax() : xmax()
                );
            }
            case 2: {
                return Domain.ofBounds(
                        xmin() == Double.NEGATIVE_INFINITY ? bounds.xmin() : xmin(),
                        xmax() == Double.POSITIVE_INFINITY ? bounds.xmax() : xmax(),
                         ymin() == Double.NEGATIVE_INFINITY ? bounds.ymin() : ymin(),
                        ymax() == Double.POSITIVE_INFINITY ? bounds.ymax() : ymax()
                );
            }
            case 3: {
                return Domain.ofBounds(
                        xmin() == Double.NEGATIVE_INFINITY ? bounds.xmin() : xmin(),
                        xmax() == Double.POSITIVE_INFINITY ? bounds.xmax() : xmax(),
                         ymin() == Double.NEGATIVE_INFINITY ? bounds.ymin() : ymin(),
                        ymax() == Double.POSITIVE_INFINITY ? bounds.ymax() : ymax(),
                         zmin() == Double.NEGATIVE_INFINITY ? bounds.zmin() : zmin(),
                        zmax() == Double.POSITIVE_INFINITY ? bounds.zmax() : zmax()
                );
            }
        }
        throw new IllegalArgumentException("Unsupported dimension " + dimension());
    }

    public Domain ensureBounded(Domain bounds) {
        switch (dimension()) {
            case 1: {
                double xmin = getXMin();
                double xmax = getXMax();
                if (xmin == Double.NEGATIVE_INFINITY || xmin < bounds.xmin()) {
                    xmin = bounds.xmin();
                }
                if (xmax == Double.POSITIVE_INFINITY || xmax > bounds.xmax()) {
                    xmax = bounds.xmax();
                }
                return ofPoints(xmin, xmax);
            }
            case 2: {
                double xmin = getXMin();
                double xmax = getXMax();
                if (xmin == Double.NEGATIVE_INFINITY || xmin < bounds.xmin()) {
                    xmin = bounds.xmin();
                }
                if (xmax == Double.POSITIVE_INFINITY || xmax > bounds.xmax()) {
                    xmax = bounds.xmax();
                }
                double ymin = getYMin();
                double ymax = getYMax();
                if (ymin == Double.NEGATIVE_INFINITY || ymin < bounds.ymin()) {
                    ymin = bounds.ymin();
                }
                if (ymax == Double.POSITIVE_INFINITY || ymax > bounds.ymax()) {
                    ymax = bounds.ymax();
                }
                return ofPoints(xmin, ymin, xmax, ymax);
            }
            case 3: {
                double xmin = getXMin();
                double xmax = getXMax();
                if (xmin == Double.NEGATIVE_INFINITY || xmin < bounds.xmin()) {
                    xmin = bounds.xmin();
                }
                if (xmax == Double.POSITIVE_INFINITY || xmax > bounds.xmax()) {
                    xmax = bounds.xmax();
                }
                double ymin = getYMin();
                double ymax = getYMax();
                if (ymin == Double.NEGATIVE_INFINITY || ymin < bounds.ymin()) {
                    ymin = bounds.ymin();
                }
                if (ymax == Double.POSITIVE_INFINITY || ymax > bounds.ymax()) {
                    ymax = bounds.ymax();
                }
                double zmin = getZMin();
                double zmax = getZMax();
                if (zmin == Double.NEGATIVE_INFINITY || zmin < bounds.zmin()) {
                    zmin = bounds.zmin();
                }
                if (zmax == Double.POSITIVE_INFINITY || zmax > bounds.zmax()) {
                    zmax = bounds.ymax();
                }
                return Domain.ofPoints(xmin, ymin, zmin, xmax, ymax, zmax);
            }
            default: {
                throw new IllegalArgumentException("Unsupported dimension " + dimension());
            }
        }
    }

    public static Domain ofPoints(double xmin, double xmax) {
        return ofBounds(xmin, xmax);
    }

    public static Domain ofPoints(double xmin, double ymin, double xmax, double ymax) {
        return ofBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain ofPoints(double xmin, double ymin, double zmin, double xmax, double ymax, double zmax) {
        return ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        switch(axis){
            case X:return isUnboundedX();
            case Y:return isUnboundedY();
            case Z:return isUnboundedZ();
        }
        return false;
    }

    @Override
    public Complex toComplex() {
        return Complex.ONE;
    }

    @Override
    public NumberExpr toNumber() {
        return this;
    }

    @Override
    public ExprType getType() {
        return isUnbounded1() ? ExprType.DOUBLE_NBR : ExprType.DOUBLE_EXPR;
    }

//    @Override
//    public boolean isDouble() {
//        return isUnconstrained();
//    }
//
//    @Override
//    public boolean isComplex() {
//        return isUnconstrained();
//    }
//    @Override
//    public boolean isComplexExpr() {
//        return true;
//    }
//    @Override
//    public boolean isDoubleExpr() {
//        return true;
//    }
//    @Override
//    public boolean isMatrix() {
//        return true;
//    }
    @Override
    public DoubleToComplex toDC() {
        return new DefaultComplexValue(Complex.ONE, this);
    }

    @Override
    public boolean isZero() {
        return false;
    }

    public boolean isNaN() {
        return false;
//        switch (dimension()) {
//            case 1: {
//                return Double.isNaN(xmin()) || Double.isNaN(xmax());
//            }
//            case 2: {
//                return Double.isNaN(xmin()) || Double.isNaN(xmax()) || Double.isNaN(ymin()) || Double.isNaN(ymax());
//            }
//        }
//        return Double.isNaN(xmin()) || Double.isNaN(xmax()) || Double.isNaN(ymin()) || Double.isNaN(ymax()) || Double.isNaN(zmin()) || Double.isNaN(zmax());
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        return toComplex().toMatrix();
//    }
    @Override
    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public Expr setParams(ParamValues params) {
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
    public Set<Param> getParams() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        return this;
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        return this;
    }

    public boolean isInfinite() {
        return false;
//        switch (dimension()) {
//            case 1: {
//                return Double.isInfinite(xmin()) || Double.isInfinite(xmax());
//            }
//            case 2: {
//                return Double.isInfinite(xmin()) || Double.isInfinite(xmax()) || Double.isInfinite(ymin()) || Double.isInfinite(ymax());
//            }
//        }
//        return Double.isInfinite(xmin()) || Double.isInfinite(xmax()) || Double.isInfinite(ymin()) || Double.isInfinite(ymax()) || Double.isInfinite(zmin()) || Double.isInfinite(zmax());
    }

    @Override
    public boolean isEvaluatable() {
        return true;
    }

    @Override
    public boolean hasProperties() {
        return false;
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
    public Map<String, Object> getProperties() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return this;
    }

    @Override
    public Expr normalize() {
        return ExprDefaults.normalize(this);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        if (isUnbounded1()) {
            return other;
        }
        if (other.isOne()) {
            return this;
        }
        return Maths.expr(other, this);
    }

    @Override
    public Expr plus(int other) {
        return Maths.expr(other + 1, this);
    }

    @Override
    public Expr plus(double other) {
        return Maths.expr(other + 1, this);
    }

    @Override
    public Expr plus(Expr other) {
        return ExprDefaults.add(this, other);
    }

    @Override
    public Expr rdiv(double other) {
        return Maths.expr(other).div(this);
    }

    @Override
    public Expr rmul(double other) {
        return Maths.expr(other).mul(this);
    }

//    @Override
//    public Expr simplify() {
//        return this;
//    }
//    @Override
//    public Expr simplify(SimplifyOptions options) {
//        return this;
//    }
    @Override
    public Expr radd(double other) {
        return Maths.expr(other).plus(this);
    }

    @Override
    public Expr rsub(double other) {
        return Maths.expr(other).sub(this);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        switch (getDimension()) {
            case 1: {
                return Tson.ofUplet("domain", Tson.of(xmin()), Tson.of(xmax())).build();
            }
            case 2: {
                return Tson.ofUplet("domain",
                        Tson.of(xmin()), Tson.of(xmax()),
                        Tson.of(ymin()), Tson.of(ymax())
                ).build();
            }
        }
        return Tson.ofUplet("domain",
                Tson.of(xmin()), Tson.of(xmax()),
                Tson.of(ymin()), Tson.of(ymax()),
                Tson.of(zmin()), Tson.of(zmax())
        ).build();
    }

    public Domain multiply(Domain other) {
        return mul(other);
    }

    public Domain mul(Domain other) {
        return this.intersect(other);
    }

    public Expr multiply(Geometry geometry) {
        return mul(geometry);
    }

    public Expr mul(Geometry domain) {
        return mul(Maths.expr(domain));
    }

    public Expr multiply(int c) {
        return mul(c);
    }

    @Override
    public Expr mul(int other) {
        return Maths.expr(other, this);
    }

    public Expr multiply(double c) {
        return mul(c);
    }

    @Override
    public Expr mul(double other) {
        return other == 1 ? this : other == 0 ? Maths.DZERO(getDimension()) : Maths.expr(other, this);
    }

    public Expr multiply(Expr c) {
        return mul(c);
    }

    @Override
    public Expr mul(Expr other) {
        return ExprDefaults.mul(this, other);
    }

    @Override
    public Expr divide(int other) {
        return div(other);
    }

    @Override
    public Expr div(int other) {
        return Maths.expr(1.0 / other, this);
    }

    public Expr divide(double other) {
        return div(other);
    }

    @Override
    public Expr div(double other) {
        return Maths.expr(1.0 / other, this);
    }

    public Expr divide(Expr other) {
        return div(other);
    }

    @Override
    public Expr div(Expr other) {
        return ExprDefaults.div(this, other);
    }

    public Expr subtract(int c) {
        return sub(c);
    }

    @Override
    public Expr sub(int other) {
        return Maths.expr(1.0 - other, this);
    }

    public Expr subtract(double c) {
        return sub(c);
    }

    @Override
    public Expr sub(double other) {
        return Maths.expr(1.0 - other, this);
    }

    @Override
    public Expr subtract(Expr other) {
        return sub(other);
    }

    @Override
    public Expr sub(Expr other) {
        return ExprDefaults.sub(this, other);
    }

    public Expr neg() {
        return Maths.neg(this);
    }

    @Override
    public Domain getDomain() {
        return this;
    }

    @Override
    public Object prop(String name) {
        return getProperty(name);
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public double toDouble() {
        return 1;
    }

    public Geometry scale(Domain newDomain) {
        return DomainScaleTool.create(getDomain(), newDomain).rescale(toGeometry());
    }

    public Geometry toGeometry() {
        return new DomainGeometry(this);
    }

    public Geometry scale(int width, int height) {
        return DomainScaleTool.create(getDomain(), Domain.ofBounds(0, width, 0, height)).rescale(toGeometry());
    }

    public Geometry addGeometry(Domain geometry) {
        return addGeometry(geometry.toGeometry());
    }

    public Geometry addGeometry(Geometry geometry) {
        return toSurface().addGeometry(geometry);
    }

    public Surface toSurface() {
        return new Surface(getPath());
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

    public Geometry subtractGeometry(Domain geometry) {
        return subtractGeometry(geometry.toGeometry());
    }

    public Geometry subtractGeometry(Geometry geometry) {
        return toSurface().subtractGeometry(geometry);
    }

    public Geometry intersectGeometry(Domain geometry) {
        return intersectGeometry(geometry.toGeometry());
    }

    public Geometry intersectGeometry(Geometry geometry) {
        return toSurface().intersectGeometry(geometry);
    }

    public Geometry exclusiveOrGeometry(Geometry geometry) {
        return toSurface().exclusiveOrGeometry(geometry);
    }

    public Domain replaceXmin(double newValue) {
        switch (dimension()) {
            case 1: {
                return Domain.ofBounds(newValue, xmax());
            }
            case 2: {
                return Domain.ofBounds(newValue, xmax(), ymin(), ymax());
            }
            case 3: {
                return Domain.ofBounds(newValue, xmax(), ymin(), ymax(), zmin(), zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain replaceXwidth(double newValue) {
        switch (dimension()) {
            case 1: {
                return Domain.ofBounds(xmin(), xmin() + newValue);
            }
            case 2: {
                return Domain.ofBounds(xmin(), xmin() + newValue, ymin(), ymax());
            }
            case 3: {
                return Domain.ofBounds(xmin(), xmin() + newValue, ymin(), ymax(), zmin(), zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain replaceYmin(double newValue) {
        switch (dimension()) {
            case 1: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 2: {
                return Domain.ofBounds(xmin(), xwidth(), newValue, ymax());
            }
            case 3: {
                return Domain.ofBounds(xmin(), xmax(), newValue, ymax(), zmin(), zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain replaceYwidth(double newValue) {
        switch (dimension()) {
            case 1: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 2: {
                return Domain.ofBounds(xmin(), xmax(), ymin(), ymin() + newValue);
            }
            case 3: {
                return Domain.ofBounds(xmin(), xmax(), ymin(), ymin() + newValue, zmin(), zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain replaceZmin(double newValue) {
        switch (dimension()) {
            case 1: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 2: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 3: {
                return Domain.ofBounds(xmin(), xmax(), ymin(), ymax(), newValue, zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Domain replaceZwidth(double newValue) {
        switch (dimension()) {
            case 1: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 2: {
                throw new IllegalArgumentException("Invalid dimension");
            }
            case 3: {
                return Domain.ofBounds(xmin(), xmax(), ymin(), ymax(), zmin(), zmin() + newValue);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public double evalDoubleSimple(double x, double y, double z) {
        return 1;
    }

    @Override
    public double evalDoubleSimple(double x, double y) {
        return 1;
    }

    @Override
    public double evalDoubleSimple(double x) {
        return 1;
    }

    @Override
    public String toLatex() {
        switch (dimension()){
            case 1:{
                return "\\prod\\left("+DoubleExpr.of(xmin()).toLatex()+"\\rightarrow "+DoubleExpr.of(xmax()).toLatex()+"\\right)";
            }
            case 2:{
                return "\\prod\\left("
                        +DoubleExpr.of(xmin()).toLatex()+"\\rightarrow "+DoubleExpr.of(xmax()).toLatex()
                        +","+DoubleExpr.of(ymin()).toLatex()+"\\rightarrow "+DoubleExpr.of(ymax()).toLatex()
                        +"\\right)";
            }
            case 3:{
                return "\\prod\\left("
                        +DoubleExpr.of(xmin()).toLatex()+"\\rightarrow "+DoubleExpr.of(xmax()).toLatex()
                        +","+DoubleExpr.of(ymin()).toLatex()+"\\rightarrow "+DoubleExpr.of(ymax()).toLatex()
                        +","+DoubleExpr.of(zmin()).toLatex()+"\\rightarrow "+DoubleExpr.of(zmax()).toLatex()
                        +"\\right)";
            }
        }
        return toString();
    }
}
