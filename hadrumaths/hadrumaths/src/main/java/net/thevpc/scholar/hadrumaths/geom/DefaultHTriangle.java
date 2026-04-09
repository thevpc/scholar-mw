package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NArrayElementBuilder;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadrumaths.util.OIndex;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DefaultHTriangle extends AbstractHGeometry implements HTriangle {
    public static final Comparator<HTriangle> SURFACE_COMPARATOR = new Comparator<HTriangle>() {
        public int compare(HTriangle o1, HTriangle o2) {
            double s = o1.area() - o2.area();
            return s > 0 ? 1 : s < 0 ? -1 : 0;
        }
    };
    private static final long serialVersionUID = 1L;

    public HPoint p1;
    public HPoint p2;
    public HPoint p3;
    private HPolygon cachedPolygon;
    private final Domain domain;


    public DefaultHTriangle(List<HPoint> points) {
        this(
                _checkTriangleList(points).get(0),
                points.get(1),
                points.get(2)
        );
        if (points.size() > 3) {
            throw new IllegalArgumentException("Its not a polygon");
        }
    }

    private static List<HPoint> _checkTriangleList(List<HPoint> points) {
        if (points == null || points.size() != 3) {
            throw new IllegalArgumentException("Its not a triangle");
        }
        return points;
    }

    public DefaultHTriangle(HPoint p1, HPoint p2, HPoint p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
            throw new IllegalArgumentException("Invalid Triangle");
        }
        domain = GeomUtils.getDomain(p1, p2, p3);
        if (domain.isEmpty()) {
            throw new IllegalArgumentException("Invalid Triangle");
        }
    }

    public DefaultHTriangle(HPolygon polygon) {
        List<HPoint> p = polygon.getPoints();
        if (p.size() != 3) {
            throw new IllegalArgumentException("Its not a polygon");
        }
        this.p1 = p.get(0);
        this.p2 = p.get(1);
        this.p3 = p.get(2);
        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
            throw new IllegalArgumentException("Invalid Triangle");
        }
        domain = GeomUtils.getDomain(p1, p2, p3);
        if (domain.isEmpty()) {
            throw new IllegalArgumentException("Invalid Triangle");
        }
    }

    @Override
    public OIndex indexOfPoint(HPoint p) {
        if (p1.equals(p)) {
            return OIndex._1;
        }
        if (p2.equals(p)) {
            return OIndex._2;
        }
        if (p3.equals(p)) {
            return OIndex._3;
        }
        return OIndex.of(-1);
    }

    @Override
    public HPoint getBarycenter() {
        return HPoint.create(
                (1.0 / 3.0) * (p1.x + p2.x + p3.x),
                (1.0 / 3.0) * (p1.y + p2.y + p3.y)
        );
    }

    @Override
    public double getCircleRadius() {
        HPoint o;
        o = getCenter();
        return (o.distance(p1));

    }

    @Override
    public HPoint getCenter() {
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

        return HPoint.create(ox, oy);
    }

    @Override
    public boolean isNeighborhood(HTriangle t) {
        ArrayList<HPoint> l = new ArrayList<HPoint>();
        ArrayList<HPoint> lt = new ArrayList<HPoint>();
        int k = 0;
        l.add(0, p1);
        l.add(1, p2);
        l.add(2, p3);
        List<HPoint> t2 = t.getPoints();
        lt.add(0, t2.get(0));
        lt.add(1, t2.get(1));
        lt.add(2, t2.get(2));
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
        return k == 2;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultHTriangle t = (DefaultHTriangle) o;
        HPoint[] ps1 = new HPoint[]{p1, p2, p3};
        HPoint[] ps2 = new HPoint[]{t.p1, t.p2, t.p3};
        int eq = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (ps1[i].equals(ps2[j])) {
                    eq++;
                }
            }
        }
        return eq >= 3;
    }

    @Override
    public int hashCode() {
        int r = super.hashCode();
        r = r * 31 + p1.hashCode();
        r = r * 31 + p2.hashCode();
        r = r * 31 + p3.hashCode();
        return r;
    }

    //    public boolean equals0(Triangle t) {
//        ArrayList<Point> l = new ArrayList<Point>();
//        ArrayList<Point> lt = new ArrayList<Point>();
//        int k = 0;
//        l.add(p1);
//        l.add(p2);
//        l.add(p3);
//        lt.add(t.p1);
//        lt.add(t.p2);
//        lt.add(t.p3);
//        for (int i = 0; i < 3; i++) {
//            int j = 0;
//            while ((j < 3)) {
//                if (((((l.get(i))).x) != ((lt.get(j)).x)) || ((((l.get(i))).y) != ((lt.get(j)).y))) {
//                    j = j + 1;
//                } else {
//                    j = 3;
//                    k = k + 1;
//                }
//
//            }
//        }
//        return k == 3;
//
//    }

    @Override
    public boolean intersection(HTriangle t) {
        int k = 0;
        List<HPoint> tpoints = t.getPoints();
        HPoint tp1 = tpoints.get(0);
        HPoint tp2 = tpoints.get(1);
        HPoint tp3 = tpoints.get(2);

        ArrayList<Line2D> l1 = new ArrayList<Line2D>();
        ArrayList<Line2D> l2 = new ArrayList<Line2D>();
        l1.add(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        l1.add(new Line2D.Double(p1.x, p1.y, p3.x, p3.y));
        l1.add(new Line2D.Double(p3.x, p3.y, p2.x, p2.y));
        l2.add(new Line2D.Double(tp1.x, tp1.y, tp2.x, tp2.y));
        l2.add(new Line2D.Double(tp1.x, tp1.y, tp3.x, tp3.y));
        l2.add(new Line2D.Double(tp3.x, tp3.y, tp2.x, tp2.y));

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

    private boolean isSameLine(Line2D l, Line2D m) {
        return ((l.getP1().getX() == m.getP1().getX()) && (l.getP2().getX() == m.getP2().getX()) && (l.getP1().getY() == m.getP1().getY()) && (l.getP2().getY() == m.getP2().getY())) || ((l.getP1().getX() == m.getP2().getX()) && (l.getP2().getX() == m.getP1().getX()) && (l.getP1().getY() == m.getP2().getY()) && (l.getP2().getY() == m.getP1().getY()));
    }

    private boolean isValidLine(Line2D l, Line2D m) {
        return ((l.getP1().getX() == m.getP1().getX()) && (l.getP1().getY() == m.getP1().getY())) || ((l.getP2().getX() == m.getP2().getX()) && (l.getP2().getY() == m.getP2().getY())) || ((l.getP1().getX() == m.getP2().getX()) && (l.getP1().getY() == m.getP2().getY())) || ((l.getP2().getX() == m.getP1().getX()) && (l.getP2().getY() == m.getP1().getY()));
    }

    @Override
    public List<HPoint> getPoints() {
        return Arrays.asList(
                p1, p2, p3
        );
    }

    @Override
    public double getHeight(int index) {
        HPoint pp2;
        HPoint pp3;
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
        return 2 * this.area() / pp2.distance(pp3);
    }

    @Override
    public double area() {
        double dp1p2, dp1p3, dp2p3, s;
        dp1p2 = Maths.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
        dp1p3 = Maths.sqrt((p3.x - p1.x) * (p3.x - p1.x) + (p3.y - p1.y) * (p3.y - p1.y));
        dp2p3 = Maths.sqrt((p3.x - p2.x) * (p3.x - p2.x) + (p3.y - p2.y) * (p3.y - p2.y));
        s = (1.0 / 2.0) * (dp1p2 + dp1p3 + dp2p3);
        return (Maths.sqrt(s * (s - dp1p2) * (s - dp1p3) * (s - dp2p3)));
    }

    @Override
    public HGeometry clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public HPoint p1() {
        return p1;
    }

    @Override
    public HPoint p2() {
        return p2;
    }

    @Override
    public HPoint p3() {
        return p3;
    }

    @Override
    public double longestEdge() {
        return Math.max(p1().distance(p2()),
                Math.max(p2().distance(p3()),
                        p1().distance(p3())));
    }

    @Override
    public double shortestEdge() {
        return Math.min(p1().distance(p2()),
                Math.min(p2().distance(p3()),
                        p1().distance(p3())));
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isRectangular() {
        return false;
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
    public boolean isSingular() {
        return toPolygon().isSingular();
    }

    @Override
    public boolean isEmpty() {
        return toPolygon().isEmpty();
    }

    @Override
    public HGeometry translate(double x, double y) {
        return new DefaultHTriangle(
                p1.translate(x, y),
                p2.translate(x, y),
                p3.translate(x, y)
        );
    }

    public boolean contains(double x, double y) {
        return toPolygon().contains(x, y);
    }

    public HPolygon toPolygon() {
        if (cachedPolygon == null) {
            cachedPolygon = GeometryFactory.createPolygon(p1, p2, p3);
        }
        return cachedPolygon;
    }

    @Override
    public HTriangle toTriangle() {
        return this;
    }

    public HPoint getPoint(OIndex index) {
        switch (index.zeroIndex()) {
            case 0:
                return p1;
            case 1:
                return p2;
            case 2:
                return p3;
        }
        return null;
    }

    @Override
    public HPolygon[] toPolygons() {
        return new HPolygon[]{toPolygon()};
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("Triangle");
        NArrayElementBuilder arr = NElement.ofArrayBuilder();
        arr.add(ue(p1));
        arr.add(ue(p2));
        arr.add(ue(p3));
        b.add("points", arr.build());
        b.addIf("properties", NElementHelper.elem(getProperties()), NElementHelper.blankPredicate());
        return b.build();
    }

    @Override
    public String toString() {
        return toElement().toString();
    }

    private NUpletElement ue(HPoint p) {
        return NElement.ofUplet(
                NElement.ofDouble(p.getX()),
                NElement.ofDouble(p.getY())
        );
    }

}
    
