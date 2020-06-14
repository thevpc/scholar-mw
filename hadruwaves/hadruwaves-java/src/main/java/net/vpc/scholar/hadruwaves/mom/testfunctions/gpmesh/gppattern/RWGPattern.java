package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.*;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneShape;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:08
 */
public final class RWGPattern extends AbstractGpPattern implements TriangularGpPattern {
    boolean x;
    boolean y;
    private int gridx = 100;
    private int gridy = 100;

    public RWGPattern(boolean x, boolean y, int gridx, int gridy) {
        this.x = x;
        this.y = y;
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public RWGPattern(boolean x, boolean y) {
        this(x, y, 100, 100);
    }

    public RWGPattern(boolean x, boolean y, int grid) {
        this(x, y, grid, grid);
    }

    public RWGPattern(int gridx, int gridy) {
        this(true, true, gridx, gridy);
    }

    public RWGPattern(int gridx) {
        this(true, true, gridx, gridx);
    }

    public RWGPattern() {
        this(true, true, 100, 100);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("x", context.elem(x));
        h.add("y", context.elem(y));
        h.add("gridx", context.elem(gridx));
        h.add("gridy", context.elem(gridy));
        return h.build();
    }

    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        Polygon p = zone.getPolygon();
        DoubleToVector f = Maths.vector(
                (
                        x ? DDiscrete.of(new RWG(1, p), gridx, gridy) : Maths.DZEROXY
                ),
                (
                        y ? DDiscrete.of(new RWG(1, p), gridx, gridy) : Maths.DZEROXY
                )
        )
                .setProperty("Type", (x & y) ? "Polyedre" : x ? "PolyedreX" : y ? "PolyedreY" : "0")
                .setProperty("p", index).toDV();
//                f.setProperties(properties);
        return f;

//
////        AreaComponent.showDialog("1",area);
//        switch (zone.getShape()) {
//            default: {
//                Polygon p = zone.getPolygon();
////                PList<Point> ps = p.getPoints();
////                Point p1 = ps.get(1);
////                Point p2 = ps.get(3);
//                try {
//                    if (p1 != null && p2 != null) {
//                        p1 = zone.toEffectivePoint(p1);
//                        p2 = zone.toEffectivePoint(p2);
//
//                        Point s1 = null;
//                        Point s2 = null;
//                        for (Point dPoint : ps) {
//                            if (!dPoint.equals(p1) && !dPoint.equals(p2)) {
//                                if (s1 == null) {
//                                    s1 = dPoint;
//                                } else if (s2 == null) {
//                                    s2 = dPoint;
//                                } else {
//                                    throw new IllegalArgumentException("How come?");
//                                }
//                            }
//                        }
////                        if (s1 != null && s2 != null) {
////                            throw new IllegalArgumentException("How come?");
////
////                        }
//                        p = new Polygon(s1, p1, s2, p2);
//                        DoubleToVector f = Maths.vector(
//                                (
//                                        x ? DDiscrete.discretize(new RWG(1, p), gridx, gridy) : FunctionFactory.DZEROXY
//                                ),
//                                (
//                                        y ? DDiscrete.discretize(new RWG(1, p), gridx, gridy) : FunctionFactory.DZEROXY
//                                )
//                        );
//                        Map<String, Object> properties = f.getProperties();
//                        properties.put("Type", (x & y) ? "Polyedre" : x ? "PolyedreX" : y ? "PolyedreY" : "0");
//                        properties.put("p", index);
////                f.setProperties(properties);
//                        return f;
//                    }
//                } catch (RuntimeException ex) {
//                    //
//                    ex.printStackTrace();
//                }
//                p = new Polygon(ps);
//                DoubleToVector f = Maths.vector(
//                        (
//                                x ? DDiscrete.discretize(new RWG(1, p), gridx, gridy) : FunctionFactory.DZEROXY
//                        ),
//                        (
//                                y ? DDiscrete.discretize(new RWG(1, p), gridx, gridy) : FunctionFactory.DZEROXY
//                        )
//                );
//                Map<String, Object> properties = f.getProperties();
//                properties.put("Type", (x & y) ? "Cst" : x ? "CstX" : y ? "CstY" : "0");
//                properties.put("p", index);
//                return f;
//            }
//        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public boolean isAttachX() {
        return false;
    }

    public boolean isAttachY() {
        return false;
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        ArrayList<MeshZone> newZones = new ArrayList<MeshZone>();
        ArrayList<MeshZone> triangles = new ArrayList<MeshZone>();
        TreeSet<Integer> remaining = new TreeSet<Integer>();
        for (MeshZone zone : zones) {
            if (!zone.getGeometry().isTriangular()) {
                zone.getGeometry().isTriangular();
                //newZones.add(zone);
            } else {
                triangles.add(zone);
            }
        }
        double err = Double.POSITIVE_INFINITY;
        for (int i = 0; i < triangles.size(); i++) {
            remaining.add(i);
            Geometry geometry = triangles.get(i).getGeometry();
            Triangle t1 = geometry.toTriangle();
            Domain dd = t1.getDomain();
            if (err > dd.xwidth()) {
                err = dd.xwidth();
            }
            if (err > dd.ywidth()) {
                err = dd.ywidth();
            }
        }
        err = err / 10.0;
        for (int i = 0; i < triangles.size(); i++) {
            MeshZone m1 = triangles.get(i);
            Triangle t1 = m1.getGeometry().toTriangle();
//            boolean ok=false;
            for (int j = i + 1; j < triangles.size(); j++) {
                MeshZone m2 = triangles.get(j);
                Triangle t2 = m2.getGeometry().toTriangle();


                List<Point> t1points = t1.getPoints();
                List<Point> t2points = t2.getPoints();

                List<Point> initialPoints = new ArrayList<Point>();
                initialPoints.addAll(t1points);
                initialPoints.addAll(t2points);
                Set<Point> allNonDuplicatePoints = GeomUtils.roundSet(initialPoints, err);
                List<Point> inter = GeomUtils.roundIntersect(t1points, t2points, err);
                if (inter.size() == 2) {
                    HashSet<Point> nonBase = new HashSet<Point>(allNonDuplicatePoints);
                    nonBase.removeAll(inter);
                    if (nonBase.size() != 2) {
                        throw new IllegalArgumentException("Problem");
                    }
                    Point[] nonBasePoints = nonBase.toArray(new Point[nonBase.size()]);
                    Polygon area = GeometryFactory.createPolygon(
                            nonBasePoints[0],
                            inter.get(0),
                            nonBasePoints[1],
                            inter.get(1)
                    );
                    MeshZone meshZone = new MeshZone(area, MeshZoneShape.POLYGON, MeshZoneType.MAIN);
//                        Point p1ok = GeomUtils.closest(p1, polyPoints);
//                        Point p2ok = GeomUtils.closest(p2, polyPoints);
//                        meshZone.setProperty("P1", p1ok);
//                        meshZone.setProperty("P2", p2ok);
                    newZones.add(meshZone);
//                    AreaComponent.showDialog("area",area);
                }
//                if (pp.size() == 2) {
//                    Point[] points = pp.toArray(new Point[pp.size()]);
//                    p1 = points[0];
//                    p2 = points[1];
//                    remaining.remove(i);
//                    remaining.remove(j);
//
//
//                    Geometry area =  m1.getGeometry().add(m2.getGeometry());
//                    if(area.isPolygonal() && area.toPolygon().is4Edges()) {
//                        PList<Point> polyPoints = area.toPolygon().getPoints();
//                        MeshZone meshZone = new MeshZone(area, MeshZoneShape.POLYGON, MeshZoneType.MAIN);
//                        Point p1ok = GeomUtils.closest(p1, polyPoints);
//                        Point p2ok = GeomUtils.closest(p2, polyPoints);
//                        meshZone.setProperty("P1", p1ok);
//                        meshZone.setProperty("P2", p2ok);
//                        newZones.add(meshZone);
//                    }else{
//                        AreaComponent.showDialog("m1.getGeometry(),m2.getGeometry()",m1.getGeometry(),m2.getGeometry());
//                        AreaComponent.showDialog("m1.getGeometry(),m2.getGeometry(),area",m1.getGeometry(),m2.getGeometry(),area);
//                        AreaComponent.showDialog("m2.getGeometry(),area",m2.getGeometry(),area);
//                        AreaComponent.showDialog("area",area);
//                        area =  m1.getGeometry().add(m2.getGeometry());
//                        throw new RuntimeException("Not a 4 edges Polygon !!! : "+(area.isPolygonal()?"Poly":"NonPoly")+" , "+area.toSurface().getPoints().size());
//                    }
//                }
            }
        }
//        if (remaining.size() > 0) {
//            //evaluate error
//            MinMax m = new MinMax();
//
//        }
//        for (Integer integer : remaining) {
//            System.err.println(">> remaining " + integer);
//        }
        return newZones;
    }

}