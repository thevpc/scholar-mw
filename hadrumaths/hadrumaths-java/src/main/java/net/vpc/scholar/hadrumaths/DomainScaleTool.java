package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.geom.*;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 4/1/16.
 */
public abstract class DomainScaleTool {

    public static void main(String[] args) {

        Domain a = Domain.forPoints(1, 1, 2, 2);
        Domain b = Domain.forPoints(3, 1, 5, 2);
        Domain c = a.expand(b);
        Domain u = Domain.forPoints(100, 100, 200, 200);
        System.out.println("a        =" + a);
        System.out.println("b        =" + b);
        System.out.println("a+b      =" + c);
        System.out.println("u        =" + u);
        DomainScaleTool t = DomainScaleTool.create(c, u);
        System.out.println("a scaled =" + t.rescale(a));
        System.out.println("b scaled =" + t.rescale(b));
    }

    public static DomainScaleTool createIdentity() {
        return new NoDomainScaleTool();
    }

    public static Geometry rescale(Geometry g, Domain to) {
        return DomainScaleTool.create(g.getDomain(), to).rescale(g);
    }

    public static Geometry rescale(Geometry g, int x, int y) {
        return DomainScaleTool.create(g.getDomain(), Domain.forBounds(0, x, 0, y)).rescale(g);
    }

    public static DomainScaleTool create(Domain from, Domain to) {
        if (from == null || to == null || from.equals(to)) {
            return createIdentity();
        } else {
            return new SimpleDomainScaleTool(from, to);
        }
    }

    private static class NoDomainScaleTool extends DomainScaleTool {
        @Override
        public double rescaleX(double x) {
            return x;
        }

        @Override
        public double rescaleW(double w) {
            return w;
        }

        @Override
        public double rescaleH(double h) {
            return h;
        }

        @Override
        public double rescaleY(double y) {
            return y;
        }

        @Override
        public Point rescale(Point point) {
            return point;
        }

        @Override
        public Domain rescale(Domain domain) {
            return domain;
        }

        @Override
        public Point[] rescale(Point[] points) {
            return points;
        }

        @Override
        public Polygon rescale(Polygon polygon) {
            return polygon;
        }

//        @Override
//        public java.awt.geom.Area rescale(java.awt.geom.Area area) {
//            return area;
//        }

        @Override
        public Geometry rescale(Geometry area) {
            return area;
        }

        @Override
        public List<Point> rescale(List<Point> points) {
            return new ArrayList<Point>(points);
        }

        @Override
        public DomainScaleTool inv() {
            return this;
        }

        @Override
        public Path2D.Double rescale(Path2D.Double path) {
            return path;
        }

        @Override
        public Path2D.Double rescale(PathIterator area) {
            return GeomUtils.pathIteratorToPath(area);
        }
    }

    private static class SimpleDomainScaleTool extends DomainScaleTool {
        private Domain from;
        private Domain to;

        public SimpleDomainScaleTool(Domain from, Domain to) {
            this.from = from;
            this.to = to;
        }

        public double rescaleX(double x) {
            return (x - from.xmin()) / from.xwidth() * to.xwidth() + to.xmin();
        }

        public double rescaleW(double w) {
            return w / from.xwidth() * to.xwidth();
        }

        public double rescaleH(double h) {
            return h / from.ywidth() * to.ywidth();
        }

        public double rescaleZ(double z) {
            return z;
        }

        public double rescaleY(double y) {
            if (from.ywidth() == 0) {
                return (y - from.ymin()) * to.ywidth() + to.ymin();
            }
            return (y - from.ymin()) / from.ywidth() * to.ywidth() + to.ymin();
        }

        public Geometry rescale(Geometry area) {
            return new Surface(
                    rescale(area.getPath())
            );
        }

        @Override
        public Path2D.Double rescale(Path2D.Double pi) {
            return rescale(pi.getPathIterator(null));
        }

        @Override
        public Path2D.Double rescale(PathIterator pi) {
            Path2D.Double d = new Path2D.Double();
            d.setWindingRule(pi.getWindingRule());
            double coords[] = new double[6];
            while (!pi.isDone()) {
                switch (pi.currentSegment(coords)) {
                    case PathIterator.SEG_MOVETO:
                        d.moveTo(rescaleX(coords[0]), rescaleY(coords[1]));
                        break;
                    case PathIterator.SEG_LINETO:
                        d.lineTo(rescaleX(coords[0]), rescaleY(coords[1]));
                        break;
                    case PathIterator.SEG_QUADTO:
                        d.quadTo(rescaleX(coords[0]), rescaleY(coords[1]), rescaleX(coords[2]), rescaleY(coords[3]));
                        break;
                    case PathIterator.SEG_CUBICTO:
                        d.curveTo(rescaleX(coords[0]), rescaleY(coords[1]), rescaleX(coords[2]), rescaleY(coords[3]), rescaleX(coords[4]), rescaleY(coords[5]));
                        break;
                    case PathIterator.SEG_CLOSE:
                        d.closePath();
                        break;
                    default: {
                        throw new RuntimeException("Error");
                    }
                }
                pi.next();
            }
            return d;
        }

        public Point rescale(Point point) {

            double x = rescaleX(point.x);
            double y = rescaleY(point.y);
            return new Point(x, y);
        }

        public Point[] rescale(Point[] points) {
            Point[] all = new Point[points.length];
            for (int i = 0; i < all.length; i++) {
                all[i] = rescale(points[i]);
            }
            return all;
        }

        @Override
        public List<Point> rescale(List<Point> points) {
            List<Point> all = new ArrayList<Point>();

            for (int i = 0; i < points.size(); i++) {
                all.add(rescale(points.get(i)));
            }
            return all;
        }

        public Domain rescale(Domain domain) {
            switch (domain.getDimension()) {
                case 1: {
                    return Domain.forBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax())
                    );

                }
                case 2: {
                    return Domain.forBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax()),
                            rescaleY(domain.ymin()),
                            rescaleY(domain.ymax())
                    );

                }
                case 3: {
                    return Domain.forBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax()),
                            rescaleY(domain.ymin()),
                            rescaleY(domain.ymax()),
                            rescaleZ(domain.zmin()),
                            rescaleZ(domain.zmax())
                    );

                }
            }
            return Domain.forBounds(
                    rescaleX(domain.xmin()),
                    rescaleX(domain.xmax()),
                    rescaleY(domain.ymin()),
                    rescaleY(domain.ymax()),
                    rescaleZ(domain.zmin()),
                    rescaleZ(domain.zmax())
            );
        }

        public Polygon rescale(Polygon polygon) {
            return new Polygon(
                    rescale(polygon.getPoints())
            );
        }

        @Override
        public DomainScaleTool inv() {
            return new SimpleDomainScaleTool(to, from);
        }
    }

    public abstract double rescaleX(double x);

    public abstract double rescaleW(double w);

    public abstract double rescaleH(double h);

    public abstract double rescaleY(double y);

//    public abstract java.awt.geom.Area rescale(java.awt.geom.Area area);

    public abstract Geometry rescale(Geometry area);

    public abstract DomainScaleTool inv();

    public abstract Path2D.Double rescale(Path2D.Double path);

    public abstract Path2D.Double rescale(PathIterator area);

    public abstract Point rescale(Point point);

    public abstract Domain rescale(Domain domain);

    public abstract Point[] rescale(Point[] points);

    public abstract List<Point> rescale(List<Point> points);

    public abstract Polygon rescale(Polygon polygon);

}
