package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;

import java.awt.geom.Path2D;
import java.util.*;

/**
 * @author : vpc
 * @creationtime 17 janv. 2006 00:46:37
 */
public class DefaultPolygon extends AbstractGeometry implements Cloneable, Polygon {

    private static final long serialVersionUID = 1L;
    private final Domain domain;
    public double[] xpoints;
    public double[] ypoints;
    public int npoints;
    private boolean rect = false;
    private int color = 1;

    public DefaultPolygon(Domain d) {
        this(new double[]{d.xmin(), d.xmax(), d.xmax(), d.xmin()}, new double[]{d.ymin(), d.ymin(), d.ymax(), d.ymax()}, 1);
        rect = true;
    }

    public DefaultPolygon(double[] x, double[] y, int color) {
        this.xpoints = x;
        this.ypoints = y;
        this.npoints = x.length;
        this.color = color;

        double minx = Double.NaN;
        double maxx = Double.NaN;
        double miny = Double.NaN;
        double maxy = Double.NaN;
        for (int i = 0; i < x.length; i++) {
            if (Double.isNaN(minx) || minx > x[i]) {
                minx = x[i];
            }
            if (Double.isNaN(miny) || miny > y[i]) {
                miny = y[i];
            }
            if (Double.isNaN(maxx) || maxx < x[i]) {
                maxx = x[i];
            }
            if (Double.isNaN(maxy) || maxy < y[i]) {
                maxy = y[i];
            }
        }
        domain = Domain.ofBounds(minx, maxx, miny, maxy);
        rect=toSurface().isRectangular();
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Polygon");
    }

//    public Polygon(Area a) {
//        PathIterator pi = a.getPathIterator(null);
//        ArrayList<Point> points = new ArrayList<Point>();
//        ArrayList<Double> xs = new ArrayList<Double>();
//        ArrayList<Double> ys = new ArrayList<Double>();
//        double coords[] = new double[23];
//        boolean first = true;
//        while (!pi.isDone()) {
//            switch (pi.currentSegment(coords)) {
//                case PathIterator.SEG_MOVETO:
//                    if (first) {
//                        Point p = Point.create(coords[0], coords[1]);
//                        if (!GeomUtils.found(p, points)) {
//                            xs.add(coords[0]);
//                            ys.add(coords[1]);
//                            points.add(p);
//                        }
//                        first = false;
//                    } else {
////                        throw new IllegalArgumentException("Not supported");
//                    }
//                    break;
//                case PathIterator.SEG_LINETO:
//                    Point p = Point.create(coords[0], coords[1]);
//                    if (!GeomUtils.found(p, points)) {
//                        xs.add(coords[0]);
//                        ys.add(coords[1]);
//                        points.add(p);
//                    }
//                    break;
//                case PathIterator.SEG_QUADTO:
//                    throw new IllegalArgumentException("Not Supported");
//                case PathIterator.SEG_CUBICTO:
//                    throw new IllegalArgumentException("Not Supported");
//                case PathIterator.SEG_CLOSE:
//                    break;
//            }
//            pi.next();
//        }
//        npoints = xs.size();
//        xpoints = new double[npoints];
//        ypoints = new double[npoints];
//        MinMax xm = new MinMax();
//        MinMax ym = new MinMax();
//        for (int i = 0; i < xs.size(); i++) {
//            xpoints[i] = xs.get(i);
//            ypoints[i] = ys.get(i);
//            xm.registerValue(xpoints[i]);
//            ym.registerValue(ypoints[i]);
//        }
//        domain = Domain.forBounds(xm.getMin(), xm.getMax(), ym.getMax(), ym.getMax());
//
//    }

    public DefaultPolygon(double[] x, double[] y) {
        this(x, y, 1);
    }


    public DefaultPolygon(List<Point> p) {
        this(p.toArray(new Point[0]));
    }

    public DefaultPolygon(Point... p) {
        this.xpoints = new double[p.length];
        this.ypoints = new double[p.length];
        this.npoints = p.length;
        for (int i = 0; i < p.length; i++) {
            xpoints[i] = p[i].x;
            ypoints[i] = p[i].y;
        }

        double minx = Double.NaN;
        double maxx = Double.NaN;
        double miny = Double.NaN;
        double maxy = Double.NaN;
        for (int i = 0; i < xpoints.length; i++) {
            if (Double.isNaN(minx) || minx > xpoints[i]) {
                minx = xpoints[i];
            }
            if (Double.isNaN(miny) || miny > ypoints[i]) {
                miny = ypoints[i];
            }
            if (Double.isNaN(maxx) || maxx < xpoints[i]) {
                maxx = xpoints[i];
            }
            if (Double.isNaN(maxy) || maxy < ypoints[i]) {
                maxy = ypoints[i];
            }
        }
        domain = Domain.ofBounds(minx, maxx, miny, maxy);
        rect=toSurface().isRectangular();
    }

    public Polygon shift(double x, double y) {
        if (xpoints.length == 0) {
            throw new IllegalArgumentException("Missing at least one point");
        }
        double x2 = xpoints[xpoints.length - 1] + x;
        double y2 = ypoints[ypoints.length - 1] + y;
        return add(x2, y2);
    }

    public Polygon add(double x, double y) {
        return add(Point.create(x, y));
    }

    public Polygon add(Point p) {
        ArrayList<Point> points = new ArrayList<Point>(getPoints());
        points.add(p);
        return new DefaultPolygon(points.toArray(new Point[0]));
    }

    @Override
    public List<Point> getPoints() {
        Point[] p = new Point[xpoints.length];
        for (int i = 0; i < p.length; i++) {
            p[i] = Point.create(xpoints[i], ypoints[i]);
        }
        return Arrays.asList(p);
    }

    public Path2D.Double getPath(double dx, double dy, double multiplier) {
        Path2D.Double p = new Path2D.Double();
        float xx = (float) ((xpoints[0] + dx) * multiplier);
        float yy = (float) ((ypoints[0] + dy) * multiplier);
        p.moveTo(xx, yy);
        for (int i = 1; i < xpoints.length; i++) {
            xx = (float) ((xpoints[i] + dx) * multiplier);
            yy = (float) ((ypoints[i] + dy) * multiplier);
            p.lineTo(xx, yy);
        }
        p.closePath();
        return p;
    }

    public Point getPoint(int index) {
        return Point.create(xpoints[index], ypoints[index]);
    }

    @Override
    public Geometry clone() {
        return super.clone();
    }

    public Surface toSurface() {
        return new Surface(getPath());
    }

    public Path2D.Double getPath() {
        Path2D.Double p = new Path2D.Double();
        double xx = (xpoints[0]);
        double yy = (ypoints[0]);
        p.moveTo(xx, yy);
        for (int i = 1; i < xpoints.length; i++) {
            xx = (xpoints[i]);
            yy = (ypoints[i]);
            p.lineTo(xx, yy);
        }
        p.closePath();
        return p;
    }

    public Domain getDomain() {
        return domain;
    }

    public boolean isRectangular() {
        return rect;
    }

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        if (npoints == 3) {
            return true;
        }
        return npoints == 4 && getPoint(0).equals(getPoint(npoints - 1));
    }

    @Override
    public boolean isSingular() {
        return toSurface().isSingular();
    }

    @Override
    public boolean isEmpty() {
        return toSurface().isEmpty();
    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        return translate(Point.create(x, y));
    }

    public boolean contains(double x, double y) {
        if (npoints <= 2 || !domain.contains(x, y)) {
            return false;
        }
        if (rect) {
            return true;
        }
        int hits = 0;

        double lastx = xpoints[npoints - 1];
        double lasty = ypoints[npoints - 1];
        double curx, cury;

        // Walk the edges of the polygon
        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
            curx = xpoints[i];
            cury = ypoints[i];

            if (cury == lasty) {
                continue;
            }

            double leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    @Override
    public Polygon[] toPolygons() {
        return new Polygon[]{this};
    }

    @Override
    public Polygon toPolygon() {
        return this;
    }

    @Override
    public Triangle toTriangle() {
        if (isTriangular()) {
            return new Triangle(getPoint(0), getPoint(1), getPoint(2));
        }
        throw new IllegalArgumentException("Not a Triangle");
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultPolygon that = (DefaultPolygon) o;
        return npoints == that.npoints &&
                rect == that.rect &&
                color == that.color &&
                Objects.equals(domain, that.domain) &&
                Arrays.equals(xpoints, that.xpoints) &&
                Arrays.equals(ypoints, that.ypoints);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), domain, npoints, rect, color);
        result = 31 * result + Arrays.hashCode(xpoints);
        result = 31 * result + Arrays.hashCode(ypoints);
        return result;
    }

    @Override
    public String toString() {
        return FormatFactory.format(this);
    }

    public boolean is4Edges() {
        if (!isSingular()) {
            return false;
        }
        return npoints == 4 || (npoints == 5 && xpoints[0] == xpoints[4] && ypoints[0] == ypoints[4]);
    }

    public Polygon translate(Point v) {
        List<Point> dPoints = getPoints();
        for (int i = 0; i < dPoints.size(); i++) {
            Point p = dPoints.get(i);
            dPoints.set(i, Point.create(p.x + v.x, p.y + v.y));
        }
        return (new DefaultPolygon(dPoints));
    }

}
