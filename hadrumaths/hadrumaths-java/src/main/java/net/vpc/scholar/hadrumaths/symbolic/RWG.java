package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 14:29:58
 */
public final class RWG extends AbstractDoubleToDouble implements Cloneable{
    private static final long serialVersionUID = 1L;
//    public static final DCstFunctionXY ZERO = new DCstFunctionXY(0, DomainXY.EMPTY);

    //    public static final int CODE = 1;


    public final double max;
    public final Polygon polygon1;
    public final Polygon polygon2;
    public final Triangle tr1;
    public final Triangle tr2;

    public RWG(double max, Polygon polygon) {
        super(null);
//        AreaComponent.showDialog("2",polygon.toArea(0.002,0,1E5));
        if (!GeomUtils.is4Edges(polygon)) {

            System.out.println(GeomUtils.toString(polygon.getPath()));
            List<Point> points = polygon.getPoints();
            GeomUtils.is4Edges(polygon);
            AreaComponent.showDialog("Not an RWG polygon", DomainScaleTool.rescale(polygon,400,400));
            throw new IllegalArgumentException("Not an RWG polygon");
        }
        FinalInfo i=init(max,
                new Polygon(
                        new double[]{polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[3]},
                        new double[]{polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[3]}
                ),
                new Polygon(
                        new double[]{polygon.xpoints[2], polygon.xpoints[1], polygon.xpoints[3]},
                        new double[]{polygon.ypoints[2], polygon.ypoints[1], polygon.ypoints[3]}
                ));
        this.max=i.max;
        this.tr1=i.tr1;
        this.tr2=i.tr2;
        this.polygon1=i.polygon1;
        this.polygon2=i.polygon2;
        this.domain=i.domain;
    }

    public RWG(double max, Polygon triange1, Polygon triange2) {
        super(null);
        FinalInfo i=init(max, triange1, triange2);
        this.domain=i.domain;
        this.max=i.max;
        this.tr1=i.tr1;
        this.tr2=i.tr2;
        this.polygon1=i.polygon1;
        this.polygon2=i.polygon2;
    }

    private RWG(double max,Domain domain,
            Polygon polygon1,
            Polygon polygon2,
            Triangle tr1,
            Triangle tr2) {
        super(null);
        this.domain=domain;
        this.max=max;
        this.tr1=tr1;
        this.tr2=tr2;
        this.polygon1=polygon1;
        this.polygon2=polygon2;
    }
    
    private class FinalInfo{
        Triangle tr1;
        Triangle tr2;
        Polygon polygon1;
        Polygon polygon2;
        Domain domain;
        double max;
    }

    private FinalInfo init(double max, Polygon triangle1, Polygon triangle2) {
        FinalInfo i=new FinalInfo();
        i.max = max;
        i.domain = max == 0 ? Domain.EMPTYXY : triangle1.getDomain().expand(triangle2.getDomain());
//        name=("RWGFunctionXY");
        if (triangle1.isTriangular() && triangle2.isTriangular()) {
            i.polygon1=triangle1;
            i.polygon2=triangle2;
            Triangle t1 = triangle1.toTriangle();
            Triangle t2 = triangle1.toTriangle();
            int x, y;
            if ((x = t2.indexOfPoint(t1.p1)) > 0 && (y = t2.indexOfPoint(t1.p2)) > 0) {
                i.tr1 = new Triangle(t1.p3, t1.p1, t1.p2);
                i.tr2 = new Triangle(t2.getPoint((x != 1 && y != 1) ? 1 : (x != 2 && y != 2) ? 2 : 3), t2.getPoint(x), t2.getPoint(y));
            } else if ((x = t2.indexOfPoint(t1.p2)) > 0 && (y = t2.indexOfPoint(t1.p3)) > 0) {
                i.tr1 = new Triangle(t1.p1, t1.p2, t1.p3);
                i.tr2 = new Triangle(t2.getPoint((x != 1 && y != 1) ? 1 : (x != 2 && y != 2) ? 2 : 3), t2.getPoint(x), t2.getPoint(y));
            } else if ((x = t2.indexOfPoint(t1.p3)) > 0 && (y = t2.indexOfPoint(t1.p1)) > 0) {
                i.tr1 = new Triangle(t1.p2, t1.p3, t1.p1);
                i.tr2 = new Triangle(t2.getPoint((x != 1 && y != 1) ? 1 : (x != 2 && y != 2) ? 2 : 3), t2.getPoint(x), t2.getPoint(y));
            } else {
                throw new IllegalArgumentException("RWG need neiberhood triangles");
            }
        } else {
            throw new IllegalArgumentException("RWG need triangles");
        }
        return i;
//        AreaComponent.showDialog("1", triange1.toArea(0.002, 0, 1E5), triange2.toArea(0.002, 0, 1E5));
    }

    public double computeDouble(Triangle t, double x, double y) {
        Point c = Point.create((t.p2.x + t.p3.x) / 2, (t.p2.y + t.p3.y) / 2);
        Point p = Point.create(x, y);
        if (p.equals(c)) {
            return max;
        }
        Point i = null;
        double d1 = 0;
        double d2 = 0;
        i = new DLineEQ(c, p).intersect(new DLineEQ(t.p1, t.p2));
        if (i != null) {
            if (p.equals(i)) {
                return 0;
            }
            d1 = p.distance(i) / c.distance(i) * max;
        }
        i = new DLineEQ(c, p).intersect(new DLineEQ(t.p1, t.p3));
        if (i != null) {
            if (p.equals(i)) {
                return 0;
            }
            d2 = p.distance(i) / c.distance(i) * max;
        }
        return Maths.min(d1, d2) * max;

    }

    @Override
    public boolean contains(double x) {
       throw new IllegalArgumentException("Missing Y");
    }

    @Override
    public boolean contains(double x, double y) {
        if (tr1.contains(x, y)) {
            return true;
        } else if (tr2.contains(x, y)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return super.contains(x, y, z);
    }

    @Override
    protected double computeDouble0(double x, OutBoolean defined) {
        throw new IllegalArgumentException("Missing y");
    }

    @Override
    protected double computeDouble0(double x, double y, OutBoolean defined) {
        if (tr1.contains(x, y)) {
            defined.set();
            return computeDouble(tr1, x, y);
        } else if (tr2.contains(x, y)) {
            defined.set();
            return computeDouble(tr2, x, y);
        }
        return 0;
    }

    @Override
    protected double computeDouble0(double x, double y, double z, OutBoolean defined) {
        if (tr1.contains(x, y)) {
            return computeDouble(tr1, x, y);
        } else if (tr2.contains(x, y)) {
            return computeDouble(tr2, x, y);
        }
        return 0;
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
    public boolean isInvariantImpl(Axis axis) {
        return max==0;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWG)) return false;
        if (!super.equals(o)) return false;

        RWG rwg = (RWG) o;

        if (Double.compare(rwg.max, max) != 0) return false;
        if (polygon1 != null ? !polygon1.equals(rwg.polygon1) : rwg.polygon1 != null) return false;
        if (polygon2 != null ? !polygon2.equals(rwg.polygon2) : rwg.polygon2 != null) return false;
        if (tr1 != null ? !tr1.equals(rwg.tr1) : rwg.tr1 != null) return false;
        if (tr2 != null ? !tr2.equals(rwg.tr2) : rwg.tr2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (polygon1 != null ? polygon1.hashCode() : 0);
        result = 31 * result + (polygon2 != null ? polygon2.hashCode() : 0);
        result = 31 * result + (tr1 != null ? tr1.hashCode() : 0);
        result = 31 * result + (tr2 != null ? tr2.hashCode() : 0);
        return result;
    }

    @Override
    public int getDomainDimension() {
        return 2;
    }


    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("Missing y");
    }


//    @Override
//    public Expr mul(Domain domain) {
//        return super.mul(domain);
//    }

    @Override
    public Expr mul(double other) {
        return new RWG(max*other,domain,polygon1,polygon2,tr1,tr2);
    }

//    @Override
//    public Expr mul(Complex other) {
//        return super.mul(other);
//    }
}