package net.vpc.scholar.hadrumaths.geom;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.DomainScaleTool;
import net.vpc.scholar.hadrumaths.GeometryFactory;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vpc on 2/27/17.
 */
public class Surface extends AbstractGeometry implements Cloneable {
    private final Domain domain;
    private final double precision = 1.0 / 100000;
    private final Domain uniformDomain = Domain.ofBounds(0, 100000, 0, 100000);
    private final List<Point> points;
    private final Path2D.Double path;

//    public static void main(String[] args) {
////        Point ap1 = Point.create(0.050283499999999995,-0.0075);
////        Point ap2 = Point.create(0.0,-0.0075);
////        Point ap3 = Point.create(0.03352233333333333,-0.0024999999999999996);
////
////        Point bp1 = Point.create(0.050283499999999995,-0.0075);
////        Point bp2 = Point.create(0.050283499999999995,0.0075);
////        Point bp3 = Point.create(0.03352233333333333,-0.0024999999999999996);
//
////        Point ap1 = Point.create(0.050283499999999995,-0.0075);
////        Point ap2 = Point.create(0.0,-0.0075);
////        Point ap3 = Point.create(0.03352233333333333,-0.0024999999999999996);
////
////        Point bp1 = ap1;
////        Point bp2 = Point.create(0.050283499999999995,0.0075);
////        Point bp3 = ap3;
//
//        Point ap1 = Point.create(1,1);
//        Point ap2 = Point.create(3,1);
//        Point ap3 = Point.create(2,5);
//
//        Point bp1 = ap1;
//        Point bp2 = Point.create(0,5);
//        Point bp3 = ap3;
//
//
//
//        Triangle a = new Triangle(ap1, ap2, ap3);
//        Triangle b = new Triangle(bp1, bp2, bp3);
//        Geometry r = a.add(b);
//        AreaComponent.showDialog(r.scale(400,400));
//    }
//
//    public static void main(String[] args) {
//        Triangle a = new Triangle(Point.create(0, 0), Point.create(0, 0 + 200), Point.create(0 + 200, 0));
//        Triangle b = new Triangle(Point.create(0, 0), Point.create(0 + 200, 0 + 200), Point.create(0 + 200, 0));
////        Triangle a = new Triangle(Point.create(100, 100), Point.create(100, 100 + 200), Point.create(100 + 200, 100));
////        Triangle b = new Triangle(Point.create(100, 100), Point.create(100 + 200, 100 + 200), Point.create(100 + 200, 100));
//
//        DomainScaleTool t1 = DomainScaleTool.create(a.getDomain(), Domain.forBounds(0, 300, 0, 300));
//        System.out.println(t1.rescale(new Point(100, 100)));
//        System.out.println(t1.rescale(a).toSurface().getPoints());
//        AreaComponent.showDialog(a, t1.rescale(a));
//
////        AreaComponent.showDialog(new Area(a.getPath()),new Area(b.getPath()));
//        for (Point point : a.intersect(b).toSurface().getPoints()) {
//            System.out.println(point);
//        }
////        AreaComponent.showDialog(a,b);
//        AreaComponent.showDialog(a,b,a.add(b));
//        AreaComponent.showDialog(a.add(b));
//
//    }

    public Surface(Geometry other) {
        this(other.getPath());
    }

    public Surface(Path2D.Double path) {
        this.path = path;
        PathIterator pi = path.getPathIterator(null);
        List<Point> points = new ArrayList<Point>();
        List<Double> xs = new ArrayList<Double>();
        List<Double> ys = new ArrayList<Double>();
        double[] coords = new double[6];
        double[] old = new double[]{Double.NaN, Double.NaN};
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    _add(points, xs, ys, old, coords, false, 0);
                    break;
                case PathIterator.SEG_LINETO:
                    _add(points, xs, ys, old, coords, true, 0);
                    break;
                case PathIterator.SEG_QUADTO:
                    _add(points, xs, ys, old, coords, true, 0);
                    _add(points, xs, ys, old, coords, true, 3);
                    break;

                case PathIterator.SEG_CUBICTO:
                    _add(points, xs, ys, old, coords, true, 0);
                    _add(points, xs, ys, old, coords, true, 3);
                    _add(points, xs, ys, old, coords, true, 5);
                case PathIterator.SEG_CLOSE:
                    old = new double[]{Double.NaN, Double.NaN};
                    break;
            }
            pi.next();
        }
        if (xs.isEmpty()) {
            domain = Domain.EMPTYXY;
            this.points = new ArrayList<>();
        } else {
            MinMax xm = new MinMax();
            MinMax ym = new MinMax();
            List<Point> points2 = new ArrayList<Point>();
            for (int i = 0; i < xs.size(); i++) {
                double xx = xs.get(i);
                double yy = ys.get(i);
                Point e = new Point(xx, yy);
                points2.add(e);
                xm.registerValue(xx);
                ym.registerValue(yy);
            }
            if (points2.size() > 1 && points2.get(0).equals(points2.get(points2.size() - 1))) {
                points2.remove(points2.size() - 1);
            }
            domain = Domain.ofBounds(xm.getMin(), xm.getMax(), ym.getMin(), ym.getMax());
//        UNIFORM_DOMAIN=domain;
            this.points = points2;
        }
    }

    public Surface(Shape shape) {
        this(shape.getPathIterator(null));
    }

    public Surface(PathIterator pathIterator) {
        this(GeomUtils.pathIteratorToPath(pathIterator));
    }

    private void _add(List<Point> points,
                      List<Double> xs,
                      List<Double> ys, double[] old, double[] coords, boolean noDuplicate, int pos) {
        if (noDuplicate && !Double.isNaN(coords[pos + 0])) {
            if (coords[pos + 0] == old[0] && coords[pos + 1] == old[1]) {
                return;
            }
        }
        Point p = Point.create(coords[0], coords[1]);
        xs.add(coords[pos]);
        ys.add(coords[pos + 1]);
        points.add(p);
        old[0] = coords[pos];
        old[1] = coords[pos + 1];
    }


//    public Surface(Area awtArea) {
//        this(awtArea.getPathIterator(null));
//    }

    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public Path2D.Double getPath() {
        return path;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isRectangular() {
        if(points.size()==0){
            return false;
        }
        Area awtArea = getUniformArea();
        return awtArea.isSingular() && (awtArea.isRectangular() || awtArea.contains(awtArea.getBounds2D()));
    }

    public boolean isPolygonal() {
        return points.size()>0 && getUniformArea().isPolygonal();
    }

    public boolean isTriangular() {
        if (!getUniformArea().isPolygonal()) {
            return false;
        }
        if (points.size() == 3) {
            return true;
        }
        return points.size() == 4 && points.get(0).equals(points.get(points.size() - 1));
    }

    @Override
    public boolean isSingular() {
        return points.isEmpty() || getUniformArea().isSingular();
    }

    @Override
    public boolean isEmpty() {
        return points.isEmpty() || getUniformArea().isEmpty();
    }

//    public Domain getUniformDomain() {
//        return UNIFORM_DOMAIN;
//    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        return new Surface(GeomUtils.translate(getPath().getPathIterator(null), x, y));
    }

    @Override
    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }

    @Override
    public Polygon[] toPolygons() {
        //should replace with multi polygons!!
        return new Polygon[]{toPolygon()};
    }

    public Polygon toPolygon() {
        if (isPolygonal()) {
            return GeometryFactory.createPolygon(points.toArray(new Point[0]));
        }
        throw new IllegalArgumentException("Not a Polygon");
    }

    public Triangle toTriangle() {
        if (isTriangular()) {
            return new Triangle(points.get(0), points.get(1), points.get(2));
        }
        throw new IllegalArgumentException("Not a Triangle");
    }

    @Override
    public Surface clone() {
        return (Surface) super.clone();
    }

    public Surface intersectGeometry(Geometry geometry) {

        Surface other = geometry.toSurface();


        Domain d1 = this.domain;
        Domain d2 = other.domain;
        Domain d = d1.expand(d2);

        DomainScaleTool t1 = DomainScaleTool.create(d, uniformDomain);
        DomainScaleTool t2 = t1.inv();
        Area a1 = new Area(t1.rescale(path));

        Area a2 = new Area(t1.rescale(other.path));
        a1.intersect(a2);
        Path2D.Double path2 = t2.rescale(a1.getPathIterator(null));

        return new Surface(path2);

    }

    public Surface subtractGeometry(Geometry geometry) {
        Surface other = geometry.toSurface();
        Domain d1 = this.domain;
        Domain d2 = other.domain;
        Domain d = d1.expand(d2);

        DomainScaleTool t1 = DomainScaleTool.create(d, uniformDomain);
        DomainScaleTool t2 = t1.inv();
        Area a1 = new Area(t1.rescale(path));

        Area a2 = new Area(t1.rescale(other.path));
        a1.subtract(a2);
        Path2D.Double path2 = t2.rescale(a1.getPathIterator(null));

        return new Surface(path2);
    }

    public Surface addGeometry(Geometry geometry) {
        Surface other = geometry.toSurface();
        Domain d1 = this.domain;
        Domain d2 = other.domain;
        Domain d = d1.expand(d2);

        DomainScaleTool t1 = DomainScaleTool.create(d, uniformDomain);
        DomainScaleTool t2 = t1.inv();
        Area a1 = new Area(t1.rescale(path));

        Area a2 = new Area(t1.rescale(other.path));
        a1.add(a2);

        Path2D.Double a3 = GeomUtils.round(a1.getPathIterator(null), 0.001, 0.001);


        Path2D.Double path2;
        PathIterator pathIterator = a3.getPathIterator(null);
        if (a1.isSingular()) {
//            if(new Surface(a3).getPoints().size()>4){
//                Geometry s1 = new Surface(a3).scale(400, 400);
//                Geometry s2 = new Surface(GeomUtils.simplifySingular(a3.getPathIterator(null))).scale(400, 400);
//                System.out.println(s1.toSurface().getPoints());
//                System.out.println(s2.toSurface().getPoints());
//                AreaComponent.showDialog("-----", s1, s2);
//            }
            path2 = (GeomUtils.simplifySingular(pathIterator));
        } else {
            path2 = GeomUtils.pathIteratorToPath(pathIterator);
        }

        path2 = t2.rescale(path2);

//        Path2D.Double path2 = t2.rescale(a3.getPathIterator(null));
        return new Surface(path2);
    }

    public Surface exclusiveOrGeometry(Geometry geometry) {
        Surface other = geometry.toSurface();
        Domain d1 = this.domain;
        Domain d2 = other.domain;
        Domain d = d1.expand(d2);

        DomainScaleTool t1 = DomainScaleTool.create(d, uniformDomain);
        DomainScaleTool t2 = t1.inv();
        Area a1 = new Area(t1.rescale(path));

        Area a2 = new Area(t1.rescale(other.path));
        a1.exclusiveOr(a2);
        Path2D.Double path2 = t2.rescale(a1.getPathIterator(null));

        return new Surface(path2);
    }

    @Override
    public Surface toSurface() {
        return this;
    }

    public Path2D.Double getUniformPath() {
        return DomainScaleTool.create(domain, uniformDomain).rescale(path);
    }

    public Area getUniformArea() {
        return new Area(getUniformPath());
    }

    public Surface addGeometry_(Geometry geometry) {
        Surface other = geometry.toSurface();
        Domain d1 = this.domain;
        Domain d2 = other.domain;
        Domain d = d1.expand(d2);

        DomainScaleTool t1 = DomainScaleTool.create(d, uniformDomain);
        DomainScaleTool t2 = t1.inv();
        Area a1 = new Area(GeomUtils.round(t1.rescale(path), precision, precision));

        Area a2 = new Area(GeomUtils.round(t1.rescale(other.path), precision, precision));
        a1.add(a2);

        Path2D.Double path2;
        if (a1.isSingular()) {
            path2 = t2.rescale(GeomUtils.simplifySingular(a1.getPathIterator(null)));
        } else {
            path2 = t2.rescale(a1.getPathIterator(null));
        }


        Surface surface = new Surface(path2);
//        Surface surface0 = add0(geometry);
        return surface;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("surface").addAll(
                Tson.pair("domain", context.elem(domain)),
                Tson.pair("points", context.elem(points))
        ).build();
    }

    @Override
    public String toString() {
        return dump();
    }

//    @Override
//    public String dump() {
//        return new Dumper(this)
//                .add("domain", domain)
//                .add("points", points)
//                .toString();
//    }


}
