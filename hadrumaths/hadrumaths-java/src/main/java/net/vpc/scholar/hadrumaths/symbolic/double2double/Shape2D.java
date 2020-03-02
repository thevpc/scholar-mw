package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Polygon;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 */
public final class Shape2D extends AbstractDoubleToDouble {

    public static final Shape2D ZERO = new Shape2D(0, Domain.EMPTYXY);
    private static final long serialVersionUID = 1L;
    //    public static final int CODE = 1;
    public double value;
    private final Geometry geometry;

    public Shape2D(double cst, Domain domain) {
        this(cst, new Polygon(domain));
    }

    public Shape2D(double cst, Geometry geometry) {
        this.value = cst;
        this.geometry = geometry;
    }

    public Shape2D(Domain domain) {
        this(Maths.sqrt(domain.xwidth() * domain.ywidth()), new Polygon(domain));
    }

    @Override
    public Domain getDomain() {
        return value == 0 ? Domain.EMPTYXY : getGeometry().getDomain();
    }

    @Override
    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new Shape2D(factor * value,
                newDomain == null ? getGeometry() : getGeometry().intersectGeometry(newDomain.toGeometry())
        );
    }

    @Override
    public AbstractDoubleToDouble toXOpposite() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble toYOpposite() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble getSymmetricX() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble getSymmetricY() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble translate(double deltaX, double deltaY) {
        return new Shape2D(value, getGeometry().translateGeometry(deltaX, deltaY));
    }

    public boolean isSymmetric(AxisXY axis) {
        return true;
    }

    @Override
    public Complex toComplex() {
        return Complex.of(value);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return axis == Axis.Z;
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isNaN() {
        return Double.isNaN(value);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    @Override
    public Expr mul(Domain domain) {
        return new Shape2D(value, getGeometry().intersectGeometry(domain.toGeometry()));
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        return toComplex().toMatrix();
//    }

    @Override
    public Expr mul(Geometry domain) {
        return new Shape2D(value, getGeometry().intersectGeometry(domain));
    }

    @Override
    public Expr mul(double other) {
        return new Shape2D(value * other, getGeometry());
    }

//    /**
//     * redefined for performance issues
//     *
//     * @param x  all x values
//     * @param y  all y values
//     * @param d0 sub-domain
//     * @return values for x,y
//     */
//    @Override
//    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        double[][] r = new double[y.length][x.length];
//        if (value != 0) {
//            Range abcd = (d0 == null ? getDomain() : getDomain().intersect(d0)).range(x, y);
//            if (abcd != null) {
//                BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
//                abcd.setDefined(def0);
//                int ax = abcd.xmin;
//                int bx = abcd.xmax;
//                int cy = abcd.ymin;
//                int dy = abcd.ymax;
//                if (geometry.isRectangular()) {
//                    for (int xIndex = ax; xIndex <= bx; xIndex++) {
//                        for (int yIndex = cy; yIndex <= dy; yIndex++) {
//                            def0.set(yIndex, xIndex);//
//                            r[yIndex][xIndex] = value;//
//                        }
//                    }
//                } else {
//                    for (int xIndex = ax; xIndex <= bx; xIndex++) {
//                        for (int yIndex = cy; yIndex <= dy; yIndex++) {
//                            if (geometry.contains(x[xIndex], y[yIndex])) {
//                                def0.set(yIndex, xIndex);//
//                                r[yIndex][xIndex] = value;//
//                            }
//                        }
//                    }
//                }
//                if (ranges != null) {
//                    ranges.set(abcd);
//                }
//            }
//        }
//        return r;
//    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.getReal());
        }
        return super.mul(other);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return false;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public boolean contains(double x) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public boolean contains(double x, double y) {
        return geometry.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return geometry.contains(x, y);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return value;
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return value;
        }
        return 0;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + Double.hashCode(value);
        result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape2D)) return false;

        Shape2D that = (Shape2D) o;

        if (Double.compare(that.value, value) != 0) return false;
        return geometry != null ? geometry.equals(that.geometry) : that.geometry == null;
    }
}
