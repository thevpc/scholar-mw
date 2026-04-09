package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneShape;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshRefinementHelper;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Geometry;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:08
 */
public final class RWGPattern extends AbstractGpPattern implements TriangularGpPattern, Cloneable {

    HintAxisType xy;

    public RWGPattern() {
        this(HintAxisType.XY);
    }

    public RWGPattern(HintAxisType xy) {
        this.xy = xy;
    }

    @Override
    public RWGPattern copy() {
        return clone();
    }

    @Override
    protected RWGPattern clone() {
        try {
            return (RWGPattern) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("axis", NElementHelper.elem(xy));
        return h.build();
    }

    public int getCount() {
        switch (xy){
            case X_ONLY:
            case Y_ONLY:
            case XY:
                return 1;
            case XY_SEPARATED:
                return 2;
        }
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        HPolygon p = zone.getPolygon();
        switch (xy){
            case X_ONLY:{
                return _xf(index, p);
            }
            case Y_ONLY:{
                return _yf(index, p);
            }
            case XY:{
                return _xyf(index, p);
            }
            case XY_SEPARATED:{
                switch (index){
                    case 0:{
                        return _xf(index, p);
                    }
                    case 1:{
                        return _yf(index, p);
                    }
                }
            }
        }
        throw new IllegalArgumentException("xy="+xy);
    }

    private static DoubleToVector _yf(int index, HPolygon p) {
        return Maths.vector(
                        (Maths.DZEROXY),
                        (new RWG(Axis.Y, 1, p))
                )
                .setProperty("Type", "PolyedreY")
                .setProperty("p", index).toDV();
    }

    private static DoubleToVector _xf(int index, HPolygon p) {
        return Maths.vector(
                        (new RWG(Axis.X,1, p)),
                        (Maths.DZEROXY)
                )
                .setProperty("Type", "PolyedreX")
                .setProperty("p", index).toDV();
    }
    private static DoubleToVector _xyf(int index, HPolygon p) {
        return Maths.vector(
                        new RWG(Axis.X, 1, p),
                        new RWG(Axis.Y, 1, p)
                )
                .setProperty("Type", "Polyedre")
                .setProperty("p", index).toDV();
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        ArrayList<MeshZone> newZones = new ArrayList<>();
        ArrayList<MeshZone> triangles = new ArrayList<MeshZone>();
        ArrayList<HTriangle> trianglesAll = new ArrayList<HTriangle>();
        TreeSet<Integer> remaining = new TreeSet<Integer>();
        HGeometry gg=null;
        for (int i = 0; i < zones.size(); i++) {
            MeshZone zone = zones.get(i);
            trianglesAll.add((HTriangle) zone.getGeometry());
        }
        for (int i = 0; i < zones.size(); i++) {
            MeshZone zone = zones.get(i);
            if (!zone.getGeometry().isTriangular()) {
                //zone.getGeometry().isTriangular();
            } else {
                triangles.add(zone);
            }
            if (gg == null) {
                gg = zone.getGeometry();
            } else {
                gg = gg.addGeometry(zone.getGeometry());
            }
        }
        double minEdge = Double.POSITIVE_INFINITY;
        for (int i = 0; i < triangles.size(); i++) {
            remaining.add(i);
            HGeometry geometry = triangles.get(i).getGeometry();
            HTriangle t = geometry.toTriangle();
            minEdge = Math.min(minEdge, t.p1().distance(t.p2()));
            minEdge = Math.min(minEdge, t.p2().distance(t.p3()));
            minEdge = Math.min(minEdge, t.p1().distance(t.p3()));
        }
        double err = minEdge / 10.0;
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < triangles.size(); i++) {
            MeshZone m1 = triangles.get(i);
            HTriangle t1 = m1.getGeometry().toTriangle();
            for (int j = i + 1; j < triangles.size(); j++) {
                MeshZone m2 = triangles.get(j);
                HTriangle t2 = m2.getGeometry().toTriangle();
                List<HPoint> t1points = t1.getPoints();
                List<HPoint> t2points = t2.getPoints();
                List<HPoint> inter = GeomUtils.roundIntersect(t1points, t2points, err);
                if (inter.size() == 2) {
                    HPoint tip1 = null, tip2 = null;
                    for (HPoint p : t1points) {
                        if (!GeomUtils.found(p, inter, err)) {
                            tip1 = p;
                            break;
                        }
                    }
                    for (HPoint p : t2points) {
                        if (!GeomUtils.found(p, inter, err)) {
                            tip2 = p;
                            break;
                        }
                    }
                    if (tip1 == null || tip2 == null) {
                        throw new IllegalArgumentException("Problem");
                    }
                    List<HPoint> quad = new ArrayList<>(Arrays.asList(tip1, inter.get(0), tip2, inter.get(1)));
                    final double cx = quad.stream().mapToDouble(p -> p.x).average().getAsDouble();
                    final double cy = quad.stream().mapToDouble(p -> p.y).average().getAsDouble();

                    quad.sort((a, b) -> Double.compare(
                            Math.atan2(a.y - cy, a.x - cx),
                            Math.atan2(b.y - cy, b.x - cx)
                    ));
                    HPolygon area = GeometryFactory.createPolygon(
                            quad.get(0), quad.get(1), quad.get(2), quad.get(3)
                    );
                    if (!GeomUtils.is4Edges(area)) continue;
                    double qarea = Math.abs(
                            (quad.get(0).x - quad.get(2).x) * (quad.get(1).y - quad.get(3).y) -
                                    (quad.get(1).x - quad.get(3).x) * (quad.get(0).y - quad.get(2).y)
                    ) * 0.5;
                    if (qarea < 1e-12) continue;

                    {
                        double ep3=1e-12;
                        HGeometry u = area.subtractGeometry(gg);
                        if(u.area()>ep3){
                            continue;
                        }
                    }
                    //final check
                    if(!GeomUtils.isValidTriangle(quad.get(0), quad.get(1), quad.get(3))){
                        continue;
                    }
                    if(!GeomUtils.isValidTriangle(quad.get(2), quad.get(1), quad.get(3))){
                        continue;
                    }
                    newZones.add(new MeshZone(area, MeshZoneShape.POLYGON, MeshZoneType.MAIN));
                    visited.add(i);
                    visited.add(j);
                }
            }
        }
        for (int i = 0; i < triangles.size(); i++) {
            if (!visited.contains(i)) {
                HTriangle t = triangles.get(i).getGeometry().toTriangle();

                // Find longest edge
                HPoint a = t.p1(), b = t.p2(), c = t.p3();
                double d12 = a.distance(b);
                double d23 = b.distance(c);
                double d13 = a.distance(c);

                HPoint tip, e1, e2;
                if (d23 >= d12 && d23 >= d13) {
                    // longest edge is b-c, tip is a
                    tip = a; e1 = b; e2 = c;
                } else if (d12 >= d23 && d12 >= d13) {
                    // longest edge is a-b, tip is c
                    tip = c; e1 = a; e2 = b;
                } else {
                    // longest edge is a-c, tip is b
                    tip = b; e1 = a; e2 = c;
                }

                // Midpoint of longest edge
                HPoint mid = HPoint.create((e1.x + e2.x) / 2.0, (e1.y + e2.y) / 2.0);

                if (!GeomUtils.isValidTriangle(tip, e1, mid)) continue;
                if (!GeomUtils.isValidTriangle(tip, e2, mid)) continue;

                // Sort quad by angle around centroid for correct winding
                List<HPoint> quad = new ArrayList<>(Arrays.asList(tip, e1, mid, e2));
                final double cx = quad.stream().mapToDouble(p -> p.x).average().getAsDouble();
                final double cy = quad.stream().mapToDouble(p -> p.y).average().getAsDouble();
                quad.sort((a2, b2) -> Double.compare(
                        Math.atan2(a2.y - cy, a2.x - cx),
                        Math.atan2(b2.y - cy, b2.x - cx)
                ));

                HPolygon area = GeometryFactory.createPolygon(
                        quad.get(0), quad.get(1), quad.get(2), quad.get(3)
                );
                if (!GeomUtils.is4Edges(area)) continue;

                newZones.add(new MeshZone(area, MeshZoneShape.POLYGON, MeshZoneType.MAIN));
            }
        }
        return newZones;
    }

}
