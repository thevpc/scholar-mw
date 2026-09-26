package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneShape;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.*;

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
        super(HintAxisType.XY);
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

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str, HintAxisType preferredAxisType) {
        HPolygon tri1 = (HPolygon) zone.getProperty("tri1");
        HPolygon tri2 = (HPolygon) zone.getProperty("tri2");
        if (tri1 != null && tri2 != null) {
            switch (xy) {
                case X_ONLY:
                    return _xf(index, tri1, tri2);
                case Y_ONLY:
                    return _yf(index, tri1, tri2);
                case XY:
                    return _xyf(index, tri1, tri2);
                case XY_SEPARATED:
                    return (index == 0) ? _xf(index, tri1, tri2) : _yf(index, tri1, tri2);
            }
        }
        HPolygon p = zone.getPolygon();
        switch (xy) {
            case X_ONLY: {
                return _xf(index, p);
            }
            case Y_ONLY: {
                return _yf(index, p);
            }
            case XY: {
                return _xyf(index, p);
            }
            case XY_SEPARATED: {
                switch (index) {
                    case 0: {
                        return _xf(index, p);
                    }
                    case 1: {
                        return _yf(index, p);
                    }
                }
            }
        }
        throw new IllegalArgumentException("xy=" + xy);
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
                        (new RWG(Axis.X, 1, p)),
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

    private static DoubleToVector _yf(int index, HPolygon tri1, HPolygon tri2) {
        return Maths.vector(
                        (Maths.DZEROXY),
                        (new RWG(Axis.Y, 1, tri1, tri2))
                )
                .setProperty("Type", "PolyedreY")
                .setProperty("p", index).toDV();
    }

    private static DoubleToVector _xf(int index, HPolygon tri1, HPolygon tri2) {
        return Maths.vector(
                        (new RWG(Axis.X, 1, tri1, tri2)),
                        (Maths.DZEROXY)
                )
                .setProperty("Type", "PolyedreX")
                .setProperty("p", index).toDV();
    }

    private static DoubleToVector _xyf(int index, HPolygon tri1, HPolygon tri2) {
        return Maths.vector(
                        new RWG(Axis.X, 1, tri1, tri2),
                        new RWG(Axis.Y, 1, tri1, tri2)
                )
                .setProperty("Type", "Polyedre")
                .setProperty("p", index).toDV();
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        List<MeshZone> newZones = new ArrayList<>();
        List<MeshZone> triangles = new ArrayList<>();
        for (MeshZone zone : zones) {
            if (zone.getGeometry().isTriangular()) {
                triangles.add(zone);
            }
        }
        if (triangles.isEmpty()) {
            return newZones;
        }

        double minEdge = Double.POSITIVE_INFINITY;
        for (MeshZone tz : triangles) {
            HTriangle t = tz.getGeometry().toTriangle();
            minEdge = Math.min(minEdge, t.p1().distance(t.p2()));
            minEdge = Math.min(minEdge, t.p2().distance(t.p3()));
            minEdge = Math.min(minEdge, t.p1().distance(t.p3()));
        }
        double eps = Math.min(minEdge / 10.0, 1e-6);

        CanonicalVertexPool pool = new CanonicalVertexPool(eps);
        Map<Long, List<HalfEdge>> edgeMap = new LinkedHashMap<>();

        for (MeshZone tz : triangles) {
            HTriangle t = tz.getGeometry().toTriangle();
            HPoint p1 = pool.canonicalize(t.p1());
            HPoint p2 = pool.canonicalize(t.p2());
            HPoint p3 = pool.canonicalize(t.p3());

            addEdge(edgeMap, pool, p1, p2, p3);
            addEdge(edgeMap, pool, p2, p3, p1);
            addEdge(edgeMap, pool, p3, p1, p2);
        }

        for (List<HalfEdge> edges : edgeMap.values()) {
            if (edges.size() == 2) {
                HalfEdge h1 = edges.get(0);
                HalfEdge h2 = edges.get(1);

                HPolygon tri1 = GeometryFactory.createPolygon(h1.opposite, h1.a, h1.b);
                HPolygon tri2 = GeometryFactory.createPolygon(h2.opposite, h2.a, h2.b);

                double a1 = tri1.toTriangle().area();
                double a2 = tri2.toTriangle().area();
                if (a1 > 1e-14 && a2 > 1e-14) {
                    HGeometry unionGeom = tri1.addGeometry(tri2);
                    MeshZone mz = new MeshZone(unionGeom, MeshZoneShape.POLYGON, MeshZoneType.MAIN);
                    mz.setProperty("tri1", tri1);
                    mz.setProperty("tri2", tri2);
                    newZones.add(mz);
                }
            }
        }
        return newZones;
    }

    private static void addEdge(Map<Long, List<HalfEdge>> edgeMap, CanonicalVertexPool pool, HPoint a, HPoint b, HPoint opposite) {
        int idA = pool.getId(a);
        int idB = pool.getId(b);
        int minId = Math.min(idA, idB);
        int maxId = Math.max(idA, idB);
        long key = (((long) minId) << 32) | ((long) maxId & 0xFFFFFFFFL);
        edgeMap.computeIfAbsent(key, k -> new ArrayList<>(2)).add(new HalfEdge(a, b, opposite));
    }

    private static class HalfEdge {
        final HPoint a;
        final HPoint b;
        final HPoint opposite;

        HalfEdge(HPoint a, HPoint b, HPoint opposite) {
            this.a = a;
            this.b = b;
            this.opposite = opposite;
        }
    }

    private static class CanonicalVertexPool {
        private final double eps;
        private final Map<HPoint, Integer> ids = new HashMap<>();
        private final Map<Long, List<HPoint>> grid = new HashMap<>();
        private int nextId = 1;

        CanonicalVertexPool(double eps) {
            this.eps = eps;
        }

        HPoint canonicalize(HPoint p) {
            long cx = (long) Math.floor(p.x / eps);
            long cy = (long) Math.floor(p.y / eps);
            for (long dx = -1; dx <= 1; dx++) {
                for (long dy = -1; dy <= 1; dy++) {
                    long key = ((cx + dx) << 32) ^ ((cy + dy) & 0xFFFFFFFFL);
                    List<HPoint> bucket = grid.get(key);
                    if (bucket != null) {
                        for (HPoint existing : bucket) {
                            if (p.distance(existing) <= eps) {
                                return existing;
                            }
                        }
                    }
                }
            }
            long selfKey = (cx << 32) ^ (cy & 0xFFFFFFFFL);
            grid.computeIfAbsent(selfKey, k -> new ArrayList<>()).add(p);
            ids.put(p, nextId++);
            return p;
        }

        int getId(HPoint p) {
            Integer id = ids.get(p);
            if (id == null) {
                p = canonicalize(p);
                return ids.get(p);
            }
            return id;
        }
    }

}
