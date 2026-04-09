package net.thevpc.scholar.hadrumaths.meshalgo.tri;


import net.thevpc.scholar.hadrumaths.geom.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.triangulate.polygon.ConstrainedDelaunayTriangulator;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class JTSHelper {
    private static final org.locationtech.jts.geom.GeometryFactory JTS_FACTORY =
            new org.locationtech.jts.geom.GeometryFactory();


    public static double getArea(HGeometry geo) {
        return JTSHelper.toJtsGeometry(geo).getArea();
    }

    public static HGeometry fromJtsGeometry(org.locationtech.jts.geom.Geometry g) {
        if (g == null) {
            return null;
        }
        if (g instanceof org.locationtech.jts.geom.Polygon) {
            Polygon polygon = (Polygon) g;
            LinearRing r = polygon.getExteriorRing();
            int numInteriorRing = polygon.getNumInteriorRing();
            if (numInteriorRing == 0) {
                return fromJtsGeometry(r);
            } else {
                List<HPolygon> holes = new ArrayList<>(numInteriorRing);
                for (int i = 0; i < numInteriorRing; i++) {
                    LinearRing r2 = polygon.getInteriorRingN(i);
                    int np2 = r2.getNumPoints();
                    List<HPoint> points2 = new ArrayList<>(np2);
                    for (int j = 0; j < np2; j++) {
                        org.locationtech.jts.geom.Coordinate c2 = r2.getCoordinateN(j);
                        points2.add(HPoint.create(c2.x, c2.y));
                    }
                    holes.add(new DefaultHPolygon(points2.toArray(new HPoint[0])));
                }
                return new DefaultHPolygonWithHoles(
                        (HPolygon) fromJtsGeometry(r),
                        holes
                );
            }
        }
        if (g instanceof LinearRing) {
            LinearRing r = (LinearRing) g;
            int np = r.getNumPoints();
            List<HPoint> points = new ArrayList<>(np);
            for (int i = 0; i < np; i++) {
                org.locationtech.jts.geom.Coordinate c = r.getCoordinateN(i);
                points.add(HPoint.create(c.x, c.y));
            }
            if (np == 3) {
                return new DefaultHTriangle(points);
            }
            return new DefaultHPolygon(
                    points.toArray(new HPoint[0])
            );
        }
        if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            int n = gc.getNumGeometries();
            if (n == 1) {
                return fromJtsGeometry(gc.getGeometryN(0));
            } else {
                List<HGeometry> geos = new ArrayList<>(n);
                for (int i = 0; i < n; i++) {
                    geos.add(fromJtsGeometry(gc.getGeometryN(i)));
                }
                return new DefaultHGeometryList(geos);
            }
        }
        throw new IllegalArgumentException("Unsupported geometry type: " + g.getClass());
    }

    public static boolean contains(org.locationtech.jts.geom.Geometry jtsGeometry, double x, double y) {
        Point p = JTS_FACTORY.createPoint(new Coordinate(x, y));
        return jtsGeometry.contains(p);
    }

    public static org.locationtech.jts.geom.Geometry toJtsGeometry(HGeometry g) {
        if (g instanceof HGeometryList) {
            org.locationtech.jts.geom.Geometry gg = null;
            for (HGeometry child : (HGeometryList) g) {
                Geometry n = toJtsGeometry(child);
                if (gg == null) {
                    gg = n;
                } else {
                    gg = gg.union(n);
                }
            }
            return gg;
        } else if (g instanceof HPolygon) {
            return toJtsPolygon((HPolygon) g);
        } else if (g instanceof HPolygonWithHoles) {
            return toJtsPolygonWithHoles((HPolygonWithHoles) g);
        } else {
            // Triangle, Surface, EllipticPolygon, RegularPolygon, etc.
            try {
                return toJtsPolygon(g.toPolygon());
            } catch (Exception e) {
                // not representable as polygon, skip
            }
        }
        return null;
    }

    public static List<org.locationtech.jts.geom.Geometry> toJtsList(HGeometry g) {
        List<org.locationtech.jts.geom.Geometry> result = new ArrayList<>();
        if (g instanceof HGeometryList) {
            for (HGeometry child : (HGeometryList) g) {
                result.addAll(toJtsList(child));
            }
        } else if (g instanceof HPolygon) {
            org.locationtech.jts.geom.Geometry jts = toJtsPolygon((HPolygon) g);
            if (jts != null) result.add(jts);
        } else {
            // Triangle, Surface, EllipticPolygon, RegularPolygon, etc.
            try {
                org.locationtech.jts.geom.Geometry jts = toJtsPolygon(g.toPolygon());
                if (jts != null) result.add(jts);
            } catch (Exception e) {
                // not representable as polygon, skip
            }
        }
        return result;
    }

    public static List<HTriangle> triangulate(HGeometry g) {
        List<org.locationtech.jts.geom.Geometry> jtsPolygons = toJtsList(g);
        if (jtsPolygons.isEmpty()) {
            return new ArrayList<>(); // empty result
        }
        org.locationtech.jts.geom.Geometry input = jtsPolygons.size() == 1
                ? jtsPolygons.get(0)
                : JTS_FACTORY.createGeometryCollection(
                jtsPolygons.toArray(new org.locationtech.jts.geom.Geometry[0])
        );
        // union first to merge shared edges and resolve the concave outline
        org.locationtech.jts.geom.Geometry unioned = input.union();


        // Step 1: reduce precision to snap near-coincident vertices
        org.locationtech.jts.geom.PrecisionModel pm = new org.locationtech.jts.geom.PrecisionModel(1e8); // 0.01 nanometer resolution — safe for mm antenna
        unioned = org.locationtech.jts.precision.GeometryPrecisionReducer.reduce(unioned, pm);

// Step 2: fix any topology issues introduced by precision reduction
        unioned = org.locationtech.jts.geom.util.GeometryFixer.fix(unioned);

// Step 3: remove collinear vertices (the main culprit for "no convex corner")
        unioned = org.locationtech.jts.simplify.DouglasPeuckerSimplifier.simplify(unioned, 1e-10);

        ConstrainedDelaunayTriangulator cdt = new ConstrainedDelaunayTriangulator(unioned);
        org.locationtech.jts.geom.Geometry triangles = cdt.getResult();
        // convert back to your Triangle list
        List<HTriangle> result = new ArrayList<>();
        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            org.locationtech.jts.geom.Polygon tri =
                    (org.locationtech.jts.geom.Polygon) triangles.getGeometryN(i);
            org.locationtech.jts.geom.Coordinate[] c = tri.getExteriorRing().getCoordinates();
            result.add(new DefaultHTriangle(
                    HPoint.create(c[0].x, c[0].y),
                    HPoint.create(c[1].x, c[1].y),
                    HPoint.create(c[2].x, c[2].y)
            ));
        }
        return result;
    }

    private static org.locationtech.jts.geom.Geometry toJtsPolygon(HPolygon p) {
        List<HPoint> points = p.getPoints();
        return toJtsPolygon(points);
    }

    public static org.locationtech.jts.geom.Polygon toJtsPolygonWithHoles(HPolygonWithHoles p) {
        HPolygon e = p.getExteriorRing();
        List<HPolygon> holes = p.getHoles();
        if (holes.isEmpty()) {
            return (Polygon) toJtsPolygon(e);
        }
        org.locationtech.jts.geom.LinearRing ring = toJtsLinearRing(e);

        org.locationtech.jts.geom.Geometry pp = JTS_FACTORY.createPolygon(ring,
                holes.stream().map(JTSHelper::toJtsLinearRing).toArray(org.locationtech.jts.geom.LinearRing[]::new)
        );
        if (!pp.isValid()) {
            pp = org.locationtech.jts.geom.util.GeometryFixer.fix(pp);
        }
        return (Polygon) pp;
    }

    public static org.locationtech.jts.geom.LinearRing toJtsLinearRing(HPolygon p) {
        return toJtsLinearRing(p.getPoints());
    }

    public static org.locationtech.jts.geom.LinearRing toJtsLinearRing(List<HPoint> points) {
        if (points == null || points.size() < 3) {
            return null;
        }
        // JTS requires closed ring: last coord == first coord
        org.locationtech.jts.geom.Coordinate[] coords = new org.locationtech.jts.geom.Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            coords[i] = new org.locationtech.jts.geom.Coordinate(points.get(i).x, points.get(i).y);
        }
        coords[coords.length - 1] = coords[0]; // close the ring
        org.locationtech.jts.geom.LinearRing ring = JTS_FACTORY.createLinearRing(coords);
        if (!ring.isValid()) {
            ring = (LinearRing) org.locationtech.jts.geom.util.GeometryFixer.fix(ring);
        }
        return ring;
    }

    public static org.locationtech.jts.geom.Geometry toJtsPolygon(List<HPoint> points) {

        if (points == null) {
            return null;
        }
        if (points.size() < 3) {
            return JTS_FACTORY.createPolygon(new Coordinate[0]);
        }
        // JTS requires closed ring: last coord == first coord
        org.locationtech.jts.geom.Coordinate[] coords = new org.locationtech.jts.geom.Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            coords[i] = new org.locationtech.jts.geom.Coordinate(points.get(i).x, points.get(i).y);
        }
        coords[coords.length - 1] = coords[0]; // close the ring
        org.locationtech.jts.geom.LinearRing ring = JTS_FACTORY.createLinearRing(coords);
        org.locationtech.jts.geom.Geometry pp = JTS_FACTORY.createPolygon(ring);
        if (!pp.isValid()) {
            pp = org.locationtech.jts.geom.util.GeometryFixer.fix(pp);
        }
        return pp;
    }

    public static Geometry translate(Geometry geometry, double x, double y) {
        AffineTransformation at = AffineTransformation.translationInstance(x, y);
        return at.transform(geometry);
    }


    public static Path2D.Double getPath(Geometry geometry) {
        Path2D.Double path = new Path2D.Double();
        appendGeometryToPath(geometry, path);
        return path;
    }

    public static boolean isRectangular(Geometry geom) {
        if (geom == null || geom.isEmpty()) return false;

        // Get the bounding box as a Geometry
        Geometry env = geom.getEnvelope();

        // 1. If the envelope isn't a Polygon, it's a point or a line (not a rectangle)
        if (!(env instanceof Polygon)) {
            return false;
        }

        // 2. If it is a Polygon, it must be topologically equal to the original
        // This also implicitly handles holes (if geom has a hole, it won't equal the solid env)
        return geom.equalsTopo(env);
    }

    public static boolean isTriangular(Geometry geom) {
        // 1. Basic type and emptiness check
        if (geom == null || geom.isEmpty() || !(geom instanceof Polygon)) {
            return false;
        }

        Polygon poly = (Polygon) geom;

        // 2. A triangle cannot have holes
        if (poly.getNumInteriorRing() > 0) {
            return false;
        }

        // 3. Check the vertex count of the Exterior Ring
        // JTS rings are always closed, so a triangle has points: A, B, C, A
        Coordinate[] coords = poly.getExteriorRing().getCoordinates();

        return coords.length == 4;
    }

    public static boolean is4Edges(Geometry geom) {
        if (geom == null || geom.isEmpty() || !(geom instanceof Polygon)) {
            return false;
        }

        Polygon poly = (Polygon) geom;

        // Usually, a 4-edged shape is considered a single "filled" boundary.
        // If it has holes, it's topologically more complex than a simple quad.
        if (poly.getNumInteriorRing() > 0) {
            return false;
        }

        // A quadrilateral has 4 sides, so JTS needs 5 coordinates (A-B-C-D-A)
        Coordinate[] coords = poly.getExteriorRing().getCoordinates();

        return coords.length == 5;
    }

    public static void appendGeometryToPath(Geometry geom, Path2D.Double path) {
        if (geom.isEmpty()) {
            return;
        }

        if (geom instanceof Polygon) {
            Polygon poly = (Polygon) geom;
            // 1. Exterior Ring
            appendRing(poly.getExteriorRing(), path);
            // 2. Interior Rings (Holes)
            for (int i = 0; i < poly.getNumInteriorRing(); i++) {
                appendRing(poly.getInteriorRingN(i), path);
            }
        } else if (geom instanceof LineString) {
            appendRing((LineString) geom, path);
        } else if (geom instanceof Point) {
            Point p = (Point) geom;
            // Points are usually rendered as tiny rectangles or circles,
            // but for a Path2D, a moveTo/lineTo of 0 length represents the coordinate.
            path.moveTo(p.getX(), p.getY());
            path.lineTo(p.getX(), p.getY());
        } else if (geom instanceof GeometryCollection) {
            // Recurse for MultiPolygons, MultiLineStrings, etc.
            for (int i = 0; i < geom.getNumGeometries(); i++) {
                appendGeometryToPath(geom.getGeometryN(i), path);
            }
        }
    }

    public static void appendRing(LineString line, Path2D.Double path) {
        Coordinate[] coords = line.getCoordinates();
        if (coords.length > 0) {
            path.moveTo(coords[0].x, coords[0].y);
            for (int i = 1; i < coords.length; i++) {
                path.lineTo(coords[i].x, coords[i].y);
            }
            // If it's a closed ring (like a Polygon boundary),
            // use closePath to let AWT know it's a closed loop.
            if (line instanceof LinearRing) {
                path.closePath();
            }
        }
    }

    public static Geometry intersect(Geometry a, Geometry b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) {
            // Return an empty geometry of the same factory/type if possible
            return a != null ? a.getFactory().createEmpty(2) : null;
        }
        return a.intersection(b);
    }

    public static Geometry union(Geometry a, Geometry b) {
        if (a == null) return b;
        if (b == null) return a;
        // Union handles overlapping areas and merges them into a single structure
        return a.union(b);
    }

    public static Geometry minus(Geometry a, Geometry b) {
        if (a == null) return null;
        if (b == null || b.isEmpty()) return a.copy();
        // Difference: parts of 'a' that are not covered by 'b'
        return a.difference(b);
    }

    public static Geometry exclusiveOr(Geometry a, Geometry b) {
        if (a == null || a.isEmpty()) return b != null ? b.copy() : null;
        if (b == null || b.isEmpty()) return a.copy();

        // Symmetric Difference: (A union B) minus (A intersect B)
        return a.symDifference(b);
    }

    public static boolean isSingular(Geometry geom) {
        if (geom == null || geom.isEmpty()) {
            return true;
        }

        // 1. Check if it's a single Polygon
        // MultiPolygons are NOT singular, even if they have only 1 element.
        if (geom instanceof Polygon) {
            Polygon poly = (Polygon) geom;

            // 2. Singular usually means "no holes"
            return poly.getNumInteriorRing() == 0;
        }

        // 3. Handle GeometryCollections (like MultiPolygon)
        if (geom instanceof GeometryCollection) {
            // If it has 0 or >1 distinct parts, it's not a singular entity
            if (geom.getNumGeometries() != 1) {
                return false;
            }
            // If it has exactly one part, check if THAT part is singular
            return isSingular(geom.getGeometryN(0));
        }

        // For Points or LineStrings, singular usually means it's not a "Multi" type
        return true;
    }

    public static boolean isPolygonal(Geometry geom) {
        if (geom == null || geom.isEmpty()) {
            return false;
        }

        // Returns true if it is a Polygon or MultiPolygon
        // Note: A GeometryCollection containing a Polygon is NOT
        // technically "Polygonal" in JTS unless it's strictly a MultiPolygon.
        return geom instanceof Polygonal;
    }


    public static Geometry fromPath2D(Path2D.Double path) {
        // 1. Flatten the path (convert curves to line segments)
        // The 'flatness' parameter (0.01) determines how smooth the curves stay
        PathIterator pi = path.getPathIterator(null, 0.01);

        List<Polygon> polygons = new ArrayList<>();
        List<Coordinate> currentCoords = new ArrayList<>();
        double[] coords = new double[6];

        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    // If we were already building a path, treat the MoveTo as a new island
                    processSubPath(currentCoords, polygons, JTS_FACTORY);
                    currentCoords.add(new Coordinate(coords[0], coords[1]));
                    break;

                case PathIterator.SEG_LINETO:
                    currentCoords.add(new Coordinate(coords[0], coords[1]));
                    break;

                case PathIterator.SEG_CLOSE:
                    // Ensure the last point matches the first for JTS LinearRings
                    if (!currentCoords.isEmpty()) {
                        Coordinate first = currentCoords.get(0);
                        Coordinate last = currentCoords.get(currentCoords.size() - 1);
                        if (!first.equals2D(last)) {
                            currentCoords.add(new Coordinate(first.x, first.y));
                        }
                    }
                    processSubPath(currentCoords, polygons, JTS_FACTORY);
                    break;
            }
            pi.next();
        }

        // Final check for unclosed paths
        processSubPath(currentCoords, polygons, JTS_FACTORY);

        // 2. Combine all polygons into a single Geometry
        if (polygons.isEmpty()) return JTS_FACTORY.createEmpty(2);
        if (polygons.size() == 1) return polygons.get(0);

        // MultiPolygon or GeometryCollection
        return JTS_FACTORY.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    private static void processSubPath(List<Coordinate> coords, List<Polygon> polys, GeometryFactory factory) {
        if (coords.size() < 4) { // JTS needs at least 4 points (A-B-C-A) for a ring
            coords.clear();
            return;
        }
        LinearRing ring = factory.createLinearRing(coords.toArray(new Coordinate[0]));
        // Note: This creates every sub-path as a separate Polygon shell.
        // To handle holes properly, use the JTS Polygonizer or union the results.
        polys.add(factory.createPolygon(ring, null));
        coords.clear();
    }
}
