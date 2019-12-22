package net.vpc.scholar.hadrumaths.geom;


import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.MathsBase;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.List;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 mai 2007 19:46:10
 */
public final class GeomUtils {
    public static Domain UNIFORM_DOMAIN = Domain.forBounds(0, 100000, 0, 100000);

    private GeomUtils() {
    }

    public static Set<Point> roundSet(List<Point> a, double epsilon) {
        HashSet<Integer> ignored = new HashSet<Integer>();
        Set<Point> p = new HashSet<Point>();
        for (int i = 0; i < a.size(); i++) {
            if (!ignored.contains(i)) {
                Point x = a.get(i);
                for (int j = i + 1; j < a.size(); j++) {
                    Point r = a.get(j);
                    if (r.roundEquals(x, epsilon)) {
                        ignored.add(j);
                    }
                }
                p.add(x);
            }
        }
        return p;
    }

    public static List<Point> roundIntersect(List<Point> a, List<Point> b, double epsilon) {
        List<Point> a2 = new ArrayList<Point>(a);
        List<Point> b2 = new ArrayList<Point>();
        for (Point p1 : b) {
            Point p2 = null;
            for (Point r : a) {
                if (r.roundEquals(p1, epsilon)) {
                    p2 = r;
                    break;
                }
            }
            if (p2 != null) {
                b2.add(p2);
            } else {
                b2.add(p1);
            }
        }
        a2.retainAll(b2);
        return a2;
    }

    public static Point closest(Point a, List<Point> all) {
        double bestDistance = -1;
        Point bestPoint = null;
        for (Point tt : all) {
            double currDistance = tt.distance(a);
            if (bestDistance < 0 || currDistance < bestDistance) {
                bestDistance = currDistance;
                bestPoint = tt;
            }
        }
        return bestPoint;
    }

    public static Triangle biggest(List<Triangle> all) {
        double d = -1;
        Triangle t = null;
        for (Triangle triangle : all) {
            double dd = triangle.getSurface();
            if (dd > d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static Triangle smallest(List<Triangle> all) {
        double d = -1;
        Triangle t = null;
        for (Triangle triangle : all) {
            double dd = triangle.getSurface();
            if (d == -1 || dd < d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static Triangle biggest(Triangle[] all) {
        double d = -1;
        Triangle t = null;
        for (Triangle triangle : all) {
            double dd = triangle.getSurface();
            if (dd > d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static Triangle smallest(Triangle[] all) {
        double d = -1;
        Triangle t = null;
        for (Triangle triangle : all) {
            double dd = triangle.getSurface();
            if (d == -1 || dd < d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static boolean isTriangle(Polygon triange1) {
        return triange1.npoints == 3 || (triange1.npoints == 4 && triange1.xpoints[0] == triange1.xpoints[3] && triange1.ypoints[0] == triange1.ypoints[3]);
    }

//    public static boolean isTriangle(Area area) {
//        Polygon triange1 = new Polygon(area);
//        return isTriangle(triange1);
//    }

    public static boolean is4Edges(Polygon polygon) {
        if (!polygon.isSingular()) {
            return false;
        }
        return polygon.npoints == 4 || (polygon.npoints == 5 && polygon.xpoints[0] == polygon.xpoints[4] && polygon.ypoints[0] == polygon.ypoints[4]);
    }

    public static boolean isRectangular(Area a) {
        return a.isSingular() && (a.isRectangular() || a.contains(a.getBounds2D()));
    }

//    public static boolean isRectangular(Polygon a) {
//        return a.toArea().isRectangular();
//    }

    public static boolean approxEqualAreaPoints(Point p1, Point p2) {
        return p1.distance(p2) < 1E-4;
    }


    public static String toString(Shape pi) {
        return toString(pi.getPathIterator(null));
    }

    public static String toString(PathIterator pi) {
        StringBuilder s = new StringBuilder();
        double coords[] = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (s.length() > 0) {
                        s.append(",");
                    }
                    s.append("moveTo(").append(coords[0]).append(",").append(coords[1]).append(")");
                    break;
                case PathIterator.SEG_LINETO:
                    if (s.length() > 0) {
                        s.append(",");
                    }
                    s.append("lineTo(").append(coords[0]).append(",").append(coords[1]).append(")");
                    break;
                case PathIterator.SEG_QUADTO:
                    if (s.length() > 0) {
                        s.append(",");
                    }
                    s.append("quadTo(").append(coords[0]).append(",").append(coords[1]).append(",").append(coords[2]).append(",").append(coords[3]).append(")");
                case PathIterator.SEG_CUBICTO:
                    if (s.length() > 0) {
                        s.append(",");
                    }
                    s.append("curveTo(").append(coords[0]).append(",").append(coords[1]).append(",").append(coords[2]).append(",").append(coords[3]).append(",").append(coords[4]).append(",").append(coords[5]).append(")");
                case PathIterator.SEG_CLOSE:
                    if (s.length() > 0) {
                        s.append(",");
                    }
                    s.append("closePath");
                    break;
            }
            pi.next();
        }
        return s.toString();
    }

    public static Path2D.Double pathIteratorToPath(PathIterator pi) {
        Path2D.Double d = new Path2D.Double();
        d.setWindingRule(pi.getWindingRule());
        double coords[] = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    d.moveTo((coords[0]), (coords[1]));
                    break;
                case PathIterator.SEG_LINETO:
                    d.lineTo((coords[0]), (coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    d.quadTo((coords[0]), (coords[1]), (coords[2]), (coords[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    d.curveTo((coords[0]), (coords[1]), (coords[2]), (coords[3]), (coords[4]), (coords[5]));
                    break;
                case PathIterator.SEG_CLOSE:
                    d.closePath();
                    break;
            }
            pi.next();
        }
        return d;
    }


    public static Path2D.Double round(Path2D.Double pi, double xprecision, double yprecision) {
//        return pi;
        return round(pi.getPathIterator(null), xprecision, yprecision);
    }

    public static Path2D.Double round(PathIterator pi, double xprecision, double yprecision) {
        Path2D.Double d = new Path2D.Double();
        d.setWindingRule(pi.getWindingRule());
        double coords[] = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    d.moveTo(MathsBase.round(coords[0], xprecision), MathsBase.round(coords[1], yprecision));
                    break;
                case PathIterator.SEG_LINETO:
                    d.lineTo(MathsBase.round(coords[0], xprecision), MathsBase.round(coords[1], yprecision));
                    break;
                case PathIterator.SEG_QUADTO:
                    d.quadTo(MathsBase.round(coords[0], xprecision), MathsBase.round(coords[1], yprecision), MathsBase.round(coords[2], xprecision), MathsBase.round(coords[3], yprecision));
                    break;
                case PathIterator.SEG_CUBICTO:
                    d.curveTo(MathsBase.round(coords[0], xprecision), MathsBase.round(coords[1], yprecision), MathsBase.round(coords[2], xprecision), MathsBase.round(coords[3], yprecision), MathsBase.round(coords[4], xprecision), MathsBase.round(coords[5], yprecision));
                    break;
                case PathIterator.SEG_CLOSE:
                    d.closePath();
                    break;
            }
            pi.next();
        }
        return d;
    }

    public static Path2D.Double translate(PathIterator pi, double dx, double dy) {
        Path2D.Double d = new Path2D.Double();
        d.setWindingRule(pi.getWindingRule());
        double coords[] = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    d.moveTo((coords[0] + dx), (coords[1] + dy));
                    break;
                case PathIterator.SEG_LINETO:
                    d.lineTo((coords[0] + dx), (coords[1] + dy));
                    break;
                case PathIterator.SEG_QUADTO:
                    d.quadTo((coords[0] + dx), (coords[1] + dy), (coords[2] + dx), (coords[3] + dy));
                    break;
                case PathIterator.SEG_CUBICTO:
                    d.curveTo((coords[0] + dx), (coords[1] + dy), (coords[2] + dx), (coords[3] + dy), (coords[4] + dx), (coords[5] + dy));
                    break;
                case PathIterator.SEG_CLOSE:
                    d.closePath();
                    break;
            }
            pi.next();
        }
        return d;
    }

    public static Path2D.Double simplifySingular(PathIterator pi) {
        Path2D.Double d = new Path2D.Double();
        d.setWindingRule(pi.getWindingRule());
        double coords[] = new double[6];
        List<Point> visited = new ArrayList<Point>();
        class Curve {
            int type;
            double[] values;

            public Curve(int type, double... values) {
                this.type = type;
                this.values = new double[values.length];
                System.arraycopy(values, 0, this.values, 0, this.values.length);
            }
        }
        List<Curve> curves = new ArrayList<Curve>();
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO: {
                    Point p = Point.create((coords[0]), (coords[1]));
                    visited.add(p);
                    System.out.println(p);

                    curves.add(new Curve(type, (coords[0]), (coords[1])));
                    break;
                }
                case PathIterator.SEG_LINETO: {
                    Point p = Point.create((coords[0]), (coords[1]));
                    System.out.println(p);
                    if (visited.get(visited.size() - 1).equals(p)) {
                        System.out.println("remove last");
                    } else if (visited.size() > 1 && visited.get(visited.size() - 2).equals(p)) {
                        System.out.println("remove before last");
                        visited.remove(visited.size() - 1);
                        curves.remove(curves.size() - 1);
                    } else if (!visited.get(visited.size() - 1).equals(p) && !(visited.size() > 1 && visited.get(visited.size() - 2).equals(p))) {
                        visited.add(p);
                        curves.add(new Curve(type, (coords[0]), (coords[1])));
                    }
                    break;
                }
                case PathIterator.SEG_QUADTO:
                    curves.add(new Curve(type, (coords[0]), (coords[1]), (coords[2]), (coords[3])));
                    break;
                case PathIterator.SEG_CUBICTO:
                    curves.add(new Curve(type, (coords[0]), (coords[1]), (coords[2]), (coords[3]), (coords[4]), (coords[5])));
                    break;
                case PathIterator.SEG_CLOSE:
                    curves.add(new Curve(type));
                    break;
            }
            pi.next();
        }
        for (Curve curve : curves) {
            switch (curve.type) {
                case PathIterator.SEG_MOVETO: {
                    d.moveTo(curve.values[0], curve.values[1]);
                    break;
                }
                case PathIterator.SEG_LINETO: {
                    d.lineTo(curve.values[0], curve.values[1]);
                    break;
                }
                case PathIterator.SEG_QUADTO:
                    d.quadTo((curve.values[0]), (curve.values[1]), (curve.values[2]), (curve.values[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    d.curveTo((curve.values[0]), (curve.values[1]), (curve.values[2]), (curve.values[3]), (curve.values[4]), (curve.values[5]));
                    break;
                case PathIterator.SEG_CLOSE:
                    d.closePath();
                    break;

            }
        }
        return d;
    }

    public static List<Point> toPoints(Area a) {
        PathIterator pi = a.getPathIterator(null);
        ArrayList<Point> points = new ArrayList<Point>();
        double coords[] = new double[23];
        boolean first = true;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (first) {
                        Point p = Point.create(coords[0], coords[1]);
                        if (!found(p, points)) {
                            points.add(p);
                        }
                        first = false;
                    } else {
//                        throw new IllegalArgumentException("Not supported");
                    }
                    break;
                case PathIterator.SEG_LINETO:
                    Point p = Point.create(coords[0], coords[1]);
                    if (!found(p, points)) {
                        points.add(p);
                    }
                    break;
                case PathIterator.SEG_QUADTO:
                    throw new IllegalArgumentException("Not Supported");
                case PathIterator.SEG_CUBICTO:
                    throw new IllegalArgumentException("Not Supported");
                case PathIterator.SEG_CLOSE:
                    break;
            }
            pi.next();
        }
        return points;
    }

    public static boolean found(Point p, Collection<Point> coll) {
        return found(p, coll, 1E-4);
    }

    public static boolean found(Point p, Collection<Point> coll, double precision) {
        for (Point dPoint : coll) {
            if (p.distance(dPoint) <= precision) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param points1
     * @param points2
     * @param left         items in points1 not found in points2
     * @param right        items in points2 not found in points1
     * @param intersection items in points1not found in points2
     * @return
     */
    public static void dispatch(List<Point> points1, List<Point> points2, Set<Point> left, Set<Point> right, Set<Point> intersection) {
        left.clear();
        left.addAll(points1);

        right.clear();
        right.addAll(points2);

        intersection.clear();
        intersection.addAll(left);
        intersection.retainAll(right);
        left.removeAll(intersection);
        right.removeAll(intersection);
    }

    public static void dispatch(List<Point> points1, List<Point> points2, List<Point> left, List<Point> right, List<Point> intersection) {
        Set<Point> sleft = new HashSet<Point>();
        Set<Point> sright = new HashSet<Point>();
        Set<Point> sintersection = new HashSet<Point>();
        dispatch(points1, points2, sleft, sright, sintersection);

        left.clear();
        left.addAll(sleft);

        right.clear();
        right.addAll(sright);

        intersection.clear();
        intersection.addAll(sintersection);
    }

    public static Domain getDomain(Point... points) {
        double minx = Double.NaN;
        double maxx = Double.NaN;
        double miny = Double.NaN;
        double maxy = Double.NaN;
        for (int i = 0; i < points.length; i++) {
            double xx = points[i].x;
            double yy = points[i].y;

            if (Double.isNaN(minx) || minx > xx) {
                minx = xx;
            }
            if (Double.isNaN(miny) || miny > yy) {
                miny = yy;
            }
            if (Double.isNaN(maxx) || maxx < xx) {
                maxx = xx;
            }
            if (Double.isNaN(maxy) || maxy < yy) {
                maxy = yy;
            }
        }
        return Domain.forBounds(minx, maxx, miny, maxy);
    }

//    private void _debug_autoFusion(Polygon polygon, ArrayList<Area> all) {
//        Domain domain = polygon.getDomain();
//        double maxRelativeSizeXAbsolute = maxRelativeSizeX * domain.xwidth;
//        double maxRelativeSizeYAbsolute = maxRelativeSizeY * domain.ywidth;
//        boolean changes = true;
//        while (changes) {
//            changes = false;
//            Area[] all2 = all.toArray(new Area[0]);
//            for (int i = 0; i < all2.length; i++) {
//                Area area = all2[i];
////                System.out.println(area.getBounds());
//                Area fusion = null;
//                Area expand = null;
//                if (GeomUtils.isRectangular(area)) {
//                    for (int j = i + 1; j < all2.length; j++) {
//                        Area other = all2[j];
////                        System.out.println(other.getBounds());
//                        if (GeomUtils.isRectangular(other)) {
//                            Area a2 = (Area) area.clone();
//                            a2.add(other);
////                            System.out.println("compare " + area.getBounds() + " / " + other.getBounds() + " : " + a2.getBounds() + "(singular=" + a2.isSingular() + ";rect=" + GeomUtils.isRectangular(a2) + ")");
//                            if (GeomUtils.isRectangular(a2)) {
//                                Rectangle2D d = a2.getBounds2D();
//                                double dw = d.getWidth();
//                                double dh = d.getHeight();
//                                if (dw <= maxRelativeSizeXAbsolute && dh <= maxRelativeSizeYAbsolute) {
//                                    fusion = other;
//                                    expand = a2;
////                                    System.out.println(">> accepted");
//                                    break;
//                                }
////                                System.out.println(">> rejected, too big");
//                            } else {
////                                System.out.println(">> rejected, bad shape");
//                                AreaComponent dc = new AreaComponent(a2);
//                                JOptionPane.showMessageDialog(null, dc, a2.getBounds() + "(singular=" + a2.isSingular() + ";rect=" + GeomUtils.isRectangular(a2) + ")", JOptionPane.PLAIN_MESSAGE);
//                            }
//                        }
//                    }
//                }
//                if (fusion != null) {
//                    all.add(expand);
//                    all.remove(area);
//                    all.remove(fusion);
//                    changes = true;
//                    break;
//                }
//            }
//        }
//    }

}
