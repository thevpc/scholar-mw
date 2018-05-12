package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Triangle extends AbstractGeometry implements Serializable, PolygonBuilder, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final Comparator<Triangle> SURFACE_COMPARATOR = new Comparator<Triangle>() {
        public int compare(Triangle o1, Triangle o2) {
            double s = o1.getSurface() - o2.getSurface();
            return s > 0 ? 1 : s < 0 ? -1 : 0;
        }
    };

//    public static void main(String[] args) {
////        p1 = {Point@2401} "(0.011174111111111109,-8.333333333333332E-4)"
////        p2 = {Point@2402} "(0.0,0.0075)"
////        p3 = {Point@2403} "(0.027935277777777774,0.004166666666666667)"
//        int c = 1;
//        Triangle t = new Triangle(
//                Point.create(c * 0.011174111111111109, c * -8.333333333333332E-4),
//                Point.create(c * 0.0, c * 0.0075),
//                Point.create(c * 0.027935277777777774, c * 0.004166666666666667)
//        );
//        System.out.println(t.getPoints().size()+" :: "+t.getPoints());
//        System.out.println(t.toPolygon().getPoints().size()+" :: "+t.toPolygon().getPoints());
//        System.out.println(t.toSurface().getPoints().size()+" :: "+t.toSurface().getPoints());
//    }

    public static void main(String[] args) {
        Polygon p = new Polygon(
                Point.create(0, 0),
                Point.create(2, 2),
                Point.create(2, 0),
                Point.create(0, 2)
        );
        System.out.println(GeomUtils.toString(p.toSurface().getPath()));
        System.out.println(GeomUtils.toString(new Area(p.toSurface().getPath()).getPathIterator(null)));
        System.out.println(p.toSurface().isSingular());
        AreaComponent.showDialog(p.scale(400, 400));
    }

    public Point p1;
    public Point p2;
    public Point p3;
    private Polygon cachedPolygon;
    private Domain domain;

    public Triangle(List<Point> points) {
        this(
                points.get(0),
                points.get(1),
                points.get(2)
        );
        if (points.size() > 3) {
            throw new IllegalArgumentException("Its not a polygon");
        }
    }

    public Triangle(Polygon polygon) {
        this(
                Point.create(polygon.xpoints[0], polygon.ypoints[0]),
                Point.create(polygon.xpoints[1], polygon.ypoints[1]),
                Point.create(polygon.xpoints[2], polygon.ypoints[2])
        );
        if (polygon.npoints > 3) {
            throw new IllegalArgumentException("Its not a polygon");
        }
    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        return new Triangle(
                getPoint(0).translate(x, y),
                getPoint(1).translate(x, y),
                getPoint(2).translate(x, y)
        );
    }

    public int indexOfPoint(Point p) {
        if (p1.equals(p)) {
            return 1;
        }
        if (p2.equals(p)) {
            return 2;
        }
        if (p3.equals(p)) {
            return 3;
        }
        return -1;
    }

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        if(p1.equals(p2) || p1.equals(p3) || p2.equals(p3)){
            throw new IllegalArgumentException("Invalid Triangle");
        }
        domain=GeomUtils.getDomain(p1,p2,p3);
        if(domain.isEmpty()){
            throw new IllegalArgumentException("Invalid Triangle");
        }
    }

    public Polygon toPolygon() {
        if (cachedPolygon == null) {
            cachedPolygon = new Polygon(
                    new double[]{p1.x, p2.x, p3.x},
                    new double[]{p1.y, p2.y, p3.y}
            );
        }
        return cachedPolygon;
    }

    public boolean contains(double x, double y) {
        return toPolygon().contains(x, y);
    }

    public double getSurface() {
        double dp1p2, dp1p3, dp2p3, s;
        dp1p2 = Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
        dp1p3 = Math.sqrt((p3.x - p1.x) * (p3.x - p1.x) + (p3.y - p1.y) * (p3.y - p1.y));
        dp2p3 = Math.sqrt((p3.x - p2.x) * (p3.x - p2.x) + (p3.y - p2.y) * (p3.y - p2.y));
        s = (1.0 / 2.0) * (dp1p2 + dp1p3 + dp2p3);
        return (Math.sqrt(s * (s - dp1p2) * (s - dp1p3) * (s - dp2p3)));
    }

    public Point getBarycenter() {
        return Point.create(
                (1.0 / 3.0) * (p1.x + p2.x + p3.x),
                (1.0 / 3.0) * (p1.y + p2.y + p3.y)
        );
    }

    public Point getCenter() {
        double a1, a2, b1, b2;
        double ox = 0;
        double oy = 0;
        if ((p1.x == p2.x) || (p1.x == p3.x) || (p2.x == p3.x) || (p1.y == p2.y) || (p1.y == p3.y) || (p2.y == p3.y)) {
            if (p1.x == p2.x) {
                oy = (p1.y + p2.y) / 2.0;
                if ((p1.y == p3.y) || (p2.y == p3.y)) {
                    ox = (p1.x + p3.x) / 2.0;
                } else {
                    ox = (1.0 / 2.0) * (((oy - p3.y) * (oy - p3.y) - (oy - p1.y) * (oy - p1.y)) / (p3.x - p1.x) + p1.x + p3.x);
                }

            }
            if (p1.x == p3.x) {
                oy = (p1.y + p3.y) / 2.0;
                if ((p1.y == p2.y) || (p2.y == p3.y)) {
                    ox = (p1.x + p2.x) / 2.0;
                } else
                    ox = (1.0 / 2.0) * (((oy - p2.y) * (oy - p2.y) - (oy - p1.y) * (oy - p1.y)) / (p2.x - p1.x) + p1.x + p2.x);

            }
            if (p2.x == p3.x) {
                oy = (p2.y + p3.y) / 2.0;
                if ((p1.y == p2.y) || (p1.y == p3.y)) {
                    ox = (p1.x + p2.x) / 2.0;
                } else
                    ox = (1.0 / 2.0) * (((oy - p1.y) * (oy - p1.y) - (oy - p2.y) * (oy - p2.y)) / (p1.x - p2.x) + p1.x + p2.x);

            }
            //condition sur y
            if (p1.y == p2.y) {
                ox = (p1.x + p2.x) / 2.0;
                oy = (1.0 / 2.0) * (((ox - p3.x) * (ox - p3.x) - (ox - p1.x) * (ox - p1.x)) / (p3.y - p1.y) + p1.y + p3.y);

            }
            if (p1.y == p3.y) {
                ox = (p1.x + p3.x) / 2.0;
                oy = (1.0 / 2.0) * (((ox - p2.x) * (ox - p2.x) - (ox - p1.x) * (ox - p1.x)) / (p2.y - p1.y) + p1.y + p2.y);

            }
            if (p2.y == p3.y) {
                ox = (p2.x + p3.x) / 2.0;
                oy = (1.0 / 2.0) * (((ox - p1.x) * (ox - p1.x) - (ox - p2.x) * (ox - p2.x)) / (p1.y - p2.y) + p1.y + p2.y);

            }
        } else {
            a1 = (p1.y - p2.y) / (p1.x - p2.x);
            a2 = (p1.y - p3.y) / (p1.x - p3.x);
            a1 = -1.0 / a1;
            a2 = -1.0 / a2;
            b1 = (p1.y + p2.y) / 2.0 - a1 * (p1.x + p2.x) / 2.0;
            b2 = (p1.y + p3.y) / 2.0 - a2 * (p1.x + p3.x) / 2.0;
            ox = (b1 - b2) / (a2 - a1);
            oy = a1 * ox + b1;
        }

        return Point.create(ox, oy);
    }

    public double getRayonCercle() {
        Point o;
        o = getCenter();
        return (o.distance(p1));

    }

    public boolean isNeighberhood(Triangle t) {
        ArrayList<Point> l = new ArrayList<Point>();
        ArrayList<Point> lt = new ArrayList<Point>();
        int k = 0;
        l.add(0, p1);
        l.add(1, p2);
        l.add(2, p3);
        lt.add(0, t.p1);
        lt.add(1, t.p2);
        lt.add(2, t.p3);
        for (int i = 0; i < 3; i++) {
            int j = 0;
            while ((j < 3) && (k < 2)) {
                if (((((l.get(i))).x) != ((lt.get(j)).x)) || ((((l.get(i))).y) != ((lt.get(j)).y)))
                    j = j + 1;
                else {
                    j = 3;
                    k = k + 1;
                }

            }
        }
        if (k == 2)
            return (true);
        else
            return (false);

    }

    public boolean equals(Triangle t) {
        Point[] ps1=new Point[]{p1,p2,p3};
        Point[] ps2=new Point[]{t.p1,t.p2,t.p3};
        int eq=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(ps1[i].equals(ps2[j])){
                    eq++;
                }
            }
        }
        return eq>=3;
    }

    public boolean equals0(Triangle t) {
        ArrayList<Point> l = new ArrayList<Point>();
        ArrayList<Point> lt = new ArrayList<Point>();
        int k = 0;
        l.add(p1);
        l.add(p2);
        l.add(p3);
        lt.add(t.p1);
        lt.add(t.p2);
        lt.add(t.p3);
        for (int i = 0; i < 3; i++) {
            int j = 0;
            while ((j < 3)) {
                if (((((l.get(i))).x) != ((lt.get(j)).x)) || ((((l.get(i))).y) != ((lt.get(j)).y))) {
                    j = j + 1;
                } else {
                    j = 3;
                    k = k + 1;
                }

            }
        }
        if (k == 3) {
            return (true);
        } else {
            return (false);
        }

    }

    private boolean isSameLine(Line2D l, Line2D m) {
        return ((l.getP1().getX() == m.getP1().getX()) && (l.getP2().getX() == m.getP2().getX()) && (l.getP1().getY() == m.getP1().getY()) && (l.getP2().getY() == m.getP2().getY())) || ((l.getP1().getX() == m.getP2().getX()) && (l.getP2().getX() == m.getP1().getX()) && (l.getP1().getY() == m.getP2().getY()) && (l.getP2().getY() == m.getP1().getY()));
    }

    private boolean isValidLine(Line2D l, Line2D m) {
        return ((l.getP1().getX() == m.getP1().getX()) && (l.getP1().getY() == m.getP1().getY())) || ((l.getP2().getX() == m.getP2().getX()) && (l.getP2().getY() == m.getP2().getY())) || ((l.getP1().getX() == m.getP2().getX()) && (l.getP1().getY() == m.getP2().getY())) || ((l.getP2().getX() == m.getP1().getX()) && (l.getP2().getY() == m.getP1().getY()));
    }

    public boolean intersection(Triangle t) {
        int k = 0;
        ArrayList<Line2D> l1 = new ArrayList<Line2D>();
        ArrayList<Line2D> l2 = new ArrayList<Line2D>();
        l1.add(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        l1.add(new Line2D.Double(p1.x, p1.y, p3.x, p3.y));
        l1.add(new Line2D.Double(p3.x, p3.y, p2.x, p2.y));
        l2.add(new Line2D.Double(t.p1.x, t.p1.y, t.p2.x, t.p2.y));
        l2.add(new Line2D.Double(t.p1.x, t.p1.y, t.p3.x, t.p3.y));
        l2.add(new Line2D.Double(t.p3.x, t.p3.y, t.p2.x, t.p2.y));

        for (int i = 0; i < l1.size(); i++) {
            int j = 0;
            while ((j < l2.size())) {
                if ((!l1.get(i).intersectsLine(l2.get(j))) || (isSameLine(l1.get(i), l2.get(j))) || (isValidLine(l1.get(i), l2.get(j)))) {
                    j = j + 1;
                } else {
                    j = l2.size();
                    k = 1;
                    i = l1.size();
                }

            }
        }

        return k != 0;
    }

    public List<Point> getPoints() {
        return Arrays.asList(
                p1, p2, p3
        );
    }

    public Point getPoint(int index) {
        switch (index) {
            case 1:
                return p1;
            case 2:
                return p2;
            case 3:
                return p3;
        }
        return null;
    }

    public double getHeight(int index) {
        Point pp2;
        Point pp3;
        switch (index) {
            case 1: {
                pp2 = p2;
                pp3 = p3;
                break;
            }
            case 2: {
                pp2 = p3;
                pp3 = p1;
                break;
            }
            case 3: {
                pp2 = p1;
                pp3 = p2;
                break;
            }
            default: {
                throw new IllegalArgumentException("index 1..3");
            }
        }
        return 2 * getSurface() / pp2.distance(pp3);
    }

    @Override
    public boolean isRectangular() {
        return false;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        return true;
    }

    @Override
    public Geometry clone() {
        try {
            return (Geometry) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Surface toSurface() {
        Surface surface = toPolygon().toSurface();
        if (surface.getPoints().size() != 3) {
            throw new RuntimeException("Error baby : a triangle with " + surface.getPoints().size() + " edges");
        }
        return surface;
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
    public Triangle toTriangle() {
        return this;
    }

    @Override
    public Path2D.Double getPath() {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(p1.x, p1.y);
        p.lineTo(p2.x, p2.y);
        p.lineTo(p3.x, p3.y);
        p.closePath();
        return p;
    }
}
    
