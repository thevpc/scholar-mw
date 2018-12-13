package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.meshalgo.*;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.ArrayList;
import java.util.Collection;

public class MeshFlipAlgo implements MeshAlgo {
    private static final long serialVersionUID = 1L;
    OptionFlip option;
    ArrayList<Triangle> listeTriangle;

    public MeshFlipAlgo() {
        listeTriangle = new ArrayList<Triangle>();
    }

    public Collection<MeshZone> meshPolygon(Geometry polygon) {
        return meshPolygon(new Geometry[]{polygon});
    }

    public String dump() {
        Dumper h = new Dumper(getClass().getSimpleName());
        h.add("options", option);
        return h.toString();
    }

    public Collection<MeshZone> meshPolygon(Geometry[] polygons) {
        for (Geometry polygon : polygons) {
            initPolygon(polygon);
        }
        int iteration = 1;
        while (option.isMeshAllowed(listeTriangle, iteration)) {
            constructionTriangle(iteration);
        }
        ArrayList<MeshZone> ret = new ArrayList<MeshZone>();
        for (Triangle triangle : listeTriangle) {
            MeshZone z = new MeshZone(triangle, MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);
            ret.add(z);
        }
        return ret;
    }

    public void setOption(MeshOptions op) {
        //
    }

    public void setOption(OptionFlip op) {
        this.option = op;
    }

    public OptionFlip getOption() {
        return option;
    }

    private void initPolygon(Geometry geom) {
        Polygon pg = geom.toPolygon();
        ArrayList<Triangle> listeTriangle1 = new ArrayList<Triangle>();
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < pg.npoints; i++) {
            Point p = Point.create(pg.xpoints[i], pg.ypoints[i]);
            if (p.x != 0 || p.y != 0) {
                points.add(p);
            }

        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if ((points.get(i).x != points.get(j).x) || (points.get(i).y != points.get(j).y)) {
                    for (Point point : points) {
                        if (((point.x != points.get(j).x) || (point.y != points.get(j).y)) && ((point.x != points.get(i).x) || (point.y != points.get(i).y))) {

                            int inter = 0;
                            Point w = Point.create(points.get(i).x, points.get(i).y);
                            Point x = Point.create(points.get(j).x, points.get(j).y);
                            Point q = Point.create(point.x, point.y);
                            Triangle t = (new Triangle(w, x, q));
                            if (!listeTriangle1.isEmpty()) {
                                for (Triangle aListeTriangle1 : listeTriangle1) {
                                    if (t.intersection(aListeTriangle1)) {
                                        inter = 1;
                                    }
                                }
                            }
                            if ((inter == 0) && (listeTriangle1.size() < pg.npoints - 2)) {
                                listeTriangle1.add(t);
                                for (int b = 0; b < listeTriangle1.size(); b++) {
                                    Triangle v = listeTriangle1.get(b);
                                    for (int z = b + 1; z < listeTriangle1.size(); z++) {
                                        if (v.equals(listeTriangle1.get(z))) {
                                            listeTriangle1.remove(z);
                                            z = z - 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Triangle aListeTriangle1 : listeTriangle1) {
            listeTriangle.add(aListeTriangle1);
        }

    }

    private void constructionTriangle(int iteration) {
        ArrayList<Triangle> ld = new ArrayList<Triangle>();
        ArrayList<Triangle> newTriangle = new ArrayList<Triangle>();
        Point p;
        Triangle t = option.selectMeshTriangle(listeTriangle, iteration);
        p = t.getBarycenter();
        for (int i = 0; i < listeTriangle.size(); i++) {
            if (t.equals(listeTriangle.get(i))) {
                listeTriangle.remove(i);
                i = i - 1;
            }
        }
        ld.add(new Triangle(p, t.p1, t.p2));
        ld.add(new Triangle(p, t.p1, t.p3));
        ld.add(new Triangle(p, t.p2, t.p3));
        for (Triangle aLd : ld) {
            int k = 0;
            for (int j = 0; j < listeTriangle.size(); j++) {
                if (aLd.isNeighberhood(listeTriangle.get(j))) {
                    k = 1;
                    ArrayList<Point> m = new ArrayList<Point>();
                    ArrayList<Point> n = new ArrayList<Point>();
                    ArrayList<Point> op = new ArrayList<Point>();
                    ArrayList<Point> v = new ArrayList<Point>();
                    m.add(listeTriangle.get(j).p1);
                    m.add((listeTriangle.get(j)).p2);
                    m.add((listeTriangle.get(j)).p3);
                    n.add(aLd.p1);
                    n.add(aLd.p2);
                    n.add(aLd.p3);
                    for (int u = 0; u < m.size(); u++) {
                        int s = 0;
                        while ((s < n.size())) {
                            if (!(m.get(u).x == n.get(s).x && m.get(u).y == n.get(s).y)) {
                                s = s + 1;
                            } else {
                                n.remove(s);
                                s = 3;
                                v.add(m.get(u));
                                m.remove(u);
                                u = u - 1;

                            }

                        }
                    }
                    op.add(m.get(0));
                    op.add(n.get(0));
                    Point p1 = aLd.getCenter();
                    Point p2 = listeTriangle.get(j).getCenter();
                    if ((p1.distance(op.get(0)) < aLd.getRayonCercle()) || (p2.distance(op.get(1)) < listeTriangle.get(j).getRayonCercle())) {
                        newTriangle.add(new Triangle(op.get(0), op.get(1), v.get(0)));
                        newTriangle.add(new Triangle(op.get(0), op.get(1), v.get(1)));
                        listeTriangle.remove(j);
                        j = j - 1;
                    } else {
                        newTriangle.add(aLd);
                    }
                }
            }
            if (k == 0) {
                newTriangle.add(aLd);

            }
        }
        for (Triangle aNewTriangle : newTriangle) {
            listeTriangle.add(aNewTriangle);
        }
    }

    @Override
    public MeshFlipAlgo clone() {
        try {
            return (MeshFlipAlgo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Never");
        }
    }
}
