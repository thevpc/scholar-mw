package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.geom.Triangle;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 14:29:58
 */
public class Polyhedron extends AbstractDoubleToDouble implements Cloneable{
    private static final long serialVersionUID = 1L;
//    public static final DCstFunctionXY ZERO = new DCstFunctionXY(0, DomainXY.EMPTY);

//    public static final int CODE = 1;

    public double max;
    private Polygon polygon;
    Triangle triangle;
    Point baricenter;
    boolean isTriangle;
    boolean isRectangle;

    public Polyhedron(double max, Domain domain) {
        this(max, new Polygon(domain));
    }

    public Polyhedron(double max, Polygon polygon) {
        super(max == 0 ? Domain.EMPTYXY : polygon.getDomain());
        this.max = max;
        this.polygon = polygon;
//        name=("Polyedre");
        if (polygon.isRectangular()) {
            isRectangle = true;
        } else
        if (polygon.npoints == 3 || (polygon.npoints == 4 && polygon.xpoints[0] == polygon.xpoints[3] && polygon.ypoints[0] == polygon.ypoints[3])) {
            isTriangle = true;
            triangle = new Triangle(polygon);
            baricenter = triangle.getBarycenter();
        }
    }

    @Override
    public boolean contains(double x) {
        throw new IllegalArgumentException("Missing Y");
    }

    @Override
    public boolean contains(double x, double y) {
        return polygon.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return polygon.contains(x, y);
    }

    public double computeDouble0(double x, double y, OutBoolean defined) {
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
            throw new IllegalArgumentException("Unable to compute Polyedre here : npoints=" + polygon.npoints);
        }
        return 0;
    }

    @Override
    protected double computeDouble0(double x, OutBoolean defined) {
        throw new IllegalArgumentException("Missing y");
    }

    @Override
    protected double computeDouble0(double x, double y, double z, OutBoolean defined) {
        return computeDouble0(x, y, defined);
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        return false;
    }

    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new Polyhedron(factor * max,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
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

    public AbstractDoubleToDouble toXOpposite() {
        return this;
    }

    public AbstractDoubleToDouble toYOpposite() {
        return this;
    }

    public boolean isZeroImpl() {
        return max == 0;
    }

    public boolean isInfiniteImpl() {
        return Double.isInfinite(max);
    }

    public boolean isNaNImpl() {
        return Double.isNaN(max);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Polyhedron)) return false;
        if (!super.equals(o)) return false;

        Polyhedron that = (Polyhedron) o;

        if (isRectangle != that.isRectangle) return false;
        if (isTriangle != that.isTriangle) return false;
        if (Double.compare(that.max, max) != 0) return false;
        if (baricenter != null ? !baricenter.equals(that.baricenter) : that.baricenter != null) return false;
        if (polygon != null ? !polygon.equals(that.polygon) : that.polygon != null) return false;
        if (triangle != null ? !triangle.equals(that.triangle) : that.triangle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (polygon != null ? polygon.hashCode() : 0);
        result = 31 * result + (triangle != null ? triangle.hashCode() : 0);
        result = 31 * result + (baricenter != null ? baricenter.hashCode() : 0);
        result = 31 * result + (isTriangle ? 1 : 0);
        result = 31 * result + (isRectangle ? 1 : 0);
        return result;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
    }

    @Override
    public int getDomainDimension() {
        return 2;
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("Missing y");
    }

    public double getMax() {
        return max;
    }

    public Polygon getPolygon() {
        return polygon;
    }
}