package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.symbolic.Range;

import java.util.List;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 14:29:58
 */
public class Polyhedron extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;
//    public static final DCstFunctionXY ZERO = new DCstFunctionXY(0, DomainXY.EMPTY);

//    public static final int CODE = 1;

    public double max;
    private final Polygon polygon;
    private Triangle triangle;
    private Point baricenter;
    private boolean isTriangle;
    private boolean isRectangle;
    private final Domain domain;

    public Polyhedron(double max, Domain domain) {
        this(max, GeometryFactory.createPolygon(domain));
    }

    public Polyhedron(double max, Polygon polygon) {
        this.max = max;
        this.polygon = polygon;
        this.domain = max == 0 ? Domain.EMPTYXY : polygon.getDomain();
//        name=("Polyedre");
        List<Point> points = polygon.getPoints();
        if (polygon.isRectangular()) {
            isRectangle = true;
        } else if (points.size() == 3 || (points.size() == 4 &&
                points.get(0).equals(points.get(3))
        )) {
            isTriangle = true;
            triangle = new Triangle(polygon);
            baricenter = triangle.getBarycenter();
        }
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new Polyhedron(factor * max,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    public AbstractDoubleToDouble toXOpposite() {
        return this;
    }

    public AbstractDoubleToDouble toYOpposite() {
        return this;
    }

    public AbstractDoubleToDouble getSymmetricX() {
        return this;
    }

    public AbstractDoubleToDouble getSymmetricY() {
        return this;
    }

    public AbstractDoubleToDouble translate(double deltaX, double deltaY) {
        return new Polyhedron(max, domain.translate(deltaX, deltaY));
    }

    @Override
    public boolean contains(double x) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public boolean contains(double x, double y) {
        return polygon.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return polygon.contains(x, y);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        throw new MissingAxisException(Axis.Y);
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
//        AreaComponent.showDialog(polygon.toArea(1E8));
        if (polygon.contains(x, y)) {
            if (isTriangle) {
                defined.set();
                Triangle t2;
                Triangle t3;
                double v;
                t2 = new Triangle(triangle.p1, triangle.p2, baricenter);
                if (t2.toPolygon().contains(x, y)) {
                    t3 = new Triangle(triangle.p1, triangle.p2, Point.create(x, y));
                    double h2 = t2.getHeight(3);
                    v = h2 == 0 ? 0 : t3.getHeight(3) / h2 * max;
                    if (Double.isNaN(v)) {
                        v = 0;
                    }
//                    System.out.printf("%s : x=%10.4f,y%10.4f,v%10.4f = \n",polygon,x,y,v);
                    return v;
                }
                t2 = new Triangle(triangle.p2, triangle.p3, baricenter);
                if (t2.toPolygon().contains(x, y)) {
                    t3 = new Triangle(triangle.p2, triangle.p3, Point.create(x, y));
                    double h2 = t2.getHeight(3);
                    v = h2 == 0 ? 0 : t3.getHeight(3) / h2 * max;
                    if (Double.isNaN(v)) {
                        v = 0;
                    }
//                    System.out.printf("%s : x=%10.4f,y%10.4f,v%10.4f = \n",polygon,x,y,v);
                    return v;
                }

                t2 = new Triangle(triangle.p3, triangle.p1, baricenter);
                if (t2.toPolygon().contains(x, y)) {
                    t3 = new Triangle(triangle.p3, triangle.p1, Point.create(x, y));
                    double h2 = t2.getHeight(3);
                    v = h2 == 0 ? 0 : t3.getHeight(3) / h2 * max;
                    if (Double.isNaN(v)) {
                        v = 0;
                    }
//                    System.out.printf("%s : x=%10.4f,y%10.4f,v%10.4f = \n",polygon,x,y,v);
                    return v;
                }
//                System.out.printf("%s : x=%10.4f,y%10.4f,v%s = \n",polygon,x,y,"0000");
                return 0;
            }
            throw new IllegalArgumentException("Unable to compute Polyedre here : npoints=" + polygon.getPoints().size());
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return evalDouble(x, y, defined);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }

    public boolean isZero() {
        return max == 0;
    }

    public boolean isNaN() {
        return Double.isNaN(max);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    public boolean isInfinite() {
        return Double.isInfinite(max);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + Double.hashCode(max);
        result = 31 * result + (polygon != null ? polygon.hashCode() : 0);
        result = 31 * result + (triangle != null ? triangle.hashCode() : 0);
        result = 31 * result + (baricenter != null ? baricenter.hashCode() : 0);
        result = 31 * result + (isTriangle ? 1 : 0);
        result = 31 * result + (isRectangle ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Polyhedron)) return false;

        Polyhedron that = (Polyhedron) o;

        if (isRectangle != that.isRectangle) return false;
        if (isTriangle != that.isTriangle) return false;
        if (Double.compare(that.max, max) != 0) return false;
        if (baricenter != null ? !baricenter.equals(that.baricenter) : that.baricenter != null) return false;
        if (polygon != null ? !polygon.equals(that.polygon) : that.polygon != null) return false;
        return triangle != null ? triangle.equals(that.triangle) : that.triangle == null;
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new MissingAxisException(Axis.Y);
    }

    public double getMax() {
        return max;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}