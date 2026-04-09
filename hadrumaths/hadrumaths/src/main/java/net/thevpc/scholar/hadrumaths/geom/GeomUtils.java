package net.thevpc.scholar.hadrumaths.geom;


import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;

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
    public static Domain UNIFORM_DOMAIN = Domain.ofBounds(0, 100000, 0, 100000);

    private GeomUtils() {
    }

    public static boolean isValidTriangle(HPoint p1, HPoint p2, HPoint p3) {
        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
            return false;
        }
        Domain domain = GeomUtils.getDomain(p1, p2, p3);
        if (domain.isEmpty()) {
            return false;
        }
        double a = new DefaultHTriangle(p1, p2, p3).area();
        if(a==0){
            return false;
        }
        return true;
    }

    public static Set<HPoint> roundSet(List<HPoint> a, double epsilon) {
        HashSet<Integer> ignored = new HashSet<Integer>();
        Set<HPoint> p = new HashSet<HPoint>();
        for (int i = 0; i < a.size(); i++) {
            if (!ignored.contains(i)) {
                HPoint x = a.get(i);
                for (int j = i + 1; j < a.size(); j++) {
                    HPoint r = a.get(j);
                    if (r.roundEquals(x, epsilon)) {
                        ignored.add(j);
                    }
                }
                p.add(x);
            }
        }
        return p;
    }

    public static List<HPoint> roundIntersect(List<HPoint> a, List<HPoint> b, double epsilon) {
        List<HPoint> a2 = new ArrayList<HPoint>(a);
        List<HPoint> b2 = new ArrayList<HPoint>();
        for (HPoint p1 : b) {
            HPoint p2 = null;
            for (HPoint r : a) {
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

    public static HPoint closest(HPoint a, List<HPoint> all) {
        double bestDistance = -1;
        HPoint bestPoint = null;
        for (HPoint tt : all) {
            double currDistance = tt.distance(a);
            if (bestDistance < 0 || currDistance < bestDistance) {
                bestDistance = currDistance;
                bestPoint = tt;
            }
        }
        return bestPoint;
    }

    public static HTriangle longestEdge(List<HTriangle> all) {
        double d = -1;
        HTriangle t = null;
        for (HTriangle triangle : all) {
            double dd = triangle.longestEdge();
            if (dd > d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static HTriangle biggestArea(List<HTriangle> all) {
        double d = -1;
        HTriangle t = null;
        for (HTriangle triangle : all) {
            double dd = triangle.area();
            if (dd > d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static HTriangle smallestArea(List<HTriangle> all) {
        double d = -1;
        HTriangle t = null;
        for (HTriangle triangle : all) {
            double dd = triangle.area();
            if (d == -1 || dd < d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static HTriangle biggestArea(HTriangle[] all) {
        double d = -1;
        HTriangle t = null;
        for (HTriangle triangle : all) {
            double dd = triangle.area();
            if (dd > d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

    public static HTriangle smallestArea(HTriangle[] all) {
        double d = -1;
        HTriangle t = null;
        for (HTriangle triangle : all) {
            double dd = triangle.area();
            if (d == -1 || dd < d) {
                d = dd;
                t = triangle;
            }
        }
        return t;
    }

//    public static boolean isTriangle(DefaultPolygon triange1) {
//        return triange1.npoints == 3 || (triange1.npoints == 4 && triange1.xpoints[0] == triange1.xpoints[3] && triange1.ypoints[0] == triange1.ypoints[3]);
//    }

//    public static boolean isTriangle(Area area) {
//        Polygon triange1 = new Polygon(area);
//        return isTriangle(triange1);
//    }

    public static boolean is4Edges(HPolygon polygon) {
        if (!polygon.isSingular()) {
            return false;
        }
        List<HPoint> points = polygon.getPoints();
        return points.size() == 4 || (points.size() == 5 &&
                points.get(0).equals(points.get(4))
        );
    }

    public static boolean isRectangular(Area a) {
        return a.isSingular() && (a.isRectangular() || a.contains(a.getBounds2D()));
    }

//    public static boolean isRectangular(Polygon a) {
//        return a.toArea().isRectangular();
//    }

    public static boolean approxEqualAreaPoints(HPoint p1, HPoint p2) {
        return p1.distance(p2) < 1E-4;
    }


    public static String toString(Shape pi) {
        return toString(pi.getPathIterator(null));
    }

    public static String toString(PathIterator pi) {
        StringBuilder s = new StringBuilder();
        double[] coords = new double[6];
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
        double[] coords = new double[6];
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
        double[] coords = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    d.moveTo(Maths.round(coords[0], xprecision), Maths.round(coords[1], yprecision));
                    break;
                case PathIterator.SEG_LINETO:
                    d.lineTo(Maths.round(coords[0], xprecision), Maths.round(coords[1], yprecision));
                    break;
                case PathIterator.SEG_QUADTO:
                    d.quadTo(Maths.round(coords[0], xprecision), Maths.round(coords[1], yprecision), Maths.round(coords[2], xprecision), Maths.round(coords[3], yprecision));
                    break;
                case PathIterator.SEG_CUBICTO:
                    d.curveTo(Maths.round(coords[0], xprecision), Maths.round(coords[1], yprecision), Maths.round(coords[2], xprecision), Maths.round(coords[3], yprecision), Maths.round(coords[4], xprecision), Maths.round(coords[5], yprecision));
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
        double[] coords = new double[6];
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
        double[] coords = new double[6];
        List<HPoint> visited = new ArrayList<HPoint>();
        class Curve {
            final int type;
            final double[] values;

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
                    HPoint p = HPoint.create((coords[0]), (coords[1]));
                    visited.add(p);
                    curves.add(new Curve(type, (coords[0]), (coords[1])));
                    break;
                }
                case PathIterator.SEG_LINETO: {
                    HPoint p = HPoint.create((coords[0]), (coords[1]));
                    if (visited.get(visited.size() - 1).equals(p)) {
                    } else if (visited.size() > 1 && visited.get(visited.size() - 2).equals(p)) {
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

    public static List<HPoint> toPoints(Area a) {
        PathIterator pi = a.getPathIterator(null);
        ArrayList<HPoint> points = new ArrayList<HPoint>();
        double[] coords = new double[23];
        boolean first = true;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (first) {
                        HPoint p = HPoint.create(coords[0], coords[1]);
                        if (!found(p, points)) {
                            points.add(p);
                        }
                        first = false;
                    } else {
//                        throw new IllegalArgumentException("Not supported");
                    }
                    break;
                case PathIterator.SEG_LINETO:
                    HPoint p = HPoint.create(coords[0], coords[1]);
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

    public static boolean found(HPoint p, Collection<HPoint> coll) {
        return found(p, coll, 1E-4);
    }

    public static boolean found(HPoint p, Collection<HPoint> coll, double precision) {
        for (HPoint dPoint : coll) {
            if (p.distance(dPoint) <= precision) {
                return true;
            }
        }
        return false;
    }

    public static void dispatch(List<HPoint> points1, List<HPoint> points2, List<HPoint> left, List<HPoint> right, List<HPoint> intersection) {
        Set<HPoint> sleft = new HashSet<HPoint>();
        Set<HPoint> sright = new HashSet<HPoint>();
        Set<HPoint> sintersection = new HashSet<HPoint>();
        dispatch(points1, points2, sleft, sright, sintersection);

        left.clear();
        left.addAll(sleft);

        right.clear();
        right.addAll(sright);

        intersection.clear();
        intersection.addAll(sintersection);
    }

    /**
     * @param points1
     * @param points2
     * @param left         items in points1 not found in points2
     * @param right        items in points2 not found in points1
     * @param intersection items in points1not found in points2
     * @return
     */
    public static void dispatch(List<HPoint> points1, List<HPoint> points2, Set<HPoint> left, Set<HPoint> right, Set<HPoint> intersection) {
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

    public static Domain getDomain(HPoint... points) {
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
        return Domain.ofBounds(minx, maxx, miny, maxy);
    }

}
