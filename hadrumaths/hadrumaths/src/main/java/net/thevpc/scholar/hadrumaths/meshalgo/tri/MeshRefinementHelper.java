package net.thevpc.scholar.hadrumaths.meshalgo.tri;

import net.thevpc.scholar.hadrumaths.geom.*;
import org.locationtech.jts.geom.Geometry;

import java.util.*;
import java.util.stream.Collectors;

public class MeshRefinementHelper {

    public static List<HTriangle> refineLocal(HGeometry local, List<HTriangle> triangles, MeshRefinement ref) {
        List<HTriangle> all = new ArrayList<>();
        for (HTriangle triangle : triangles) {
            all.addAll(refineLocal(local, triangle, ref));
        }
        return all;
    }

    public static List<HTriangle> refineLocal(HGeometry local, HTriangle triangle, MeshRefinement ref) {
        HGeometry r = triangle.subtractGeometry(local);
        HGeometry i = triangle.intersectGeometry(local);
        List<HTriangle> all = new ArrayList<>();
        if (!r.isEmpty()) {
            if (r instanceof HTriangle) {
                all.add((HTriangle) r);
            } else {
                all.addAll(refineTriangles(triangulate(r), ref));
            }
        }
        if (!i.isEmpty()) {
            if (i instanceof HTriangle) {
                all.add((HTriangle) i);
            } else {
                all.addAll(refineTriangles(triangulate(i), ref));
            }
        }
        return all;
    }

    public static List<HTriangle> triangulate(HGeometry geometry) {
        return JTSHelper.triangulate(geometry);
    }

    public static List<HTriangle> refineTriangles(List<HTriangle> triangles, MeshRefinement r) {
        if (r == null) return triangles;
        Comparator<TriangleAndIteration> triangleComparator = splitComparator(r);
        PriorityQueue<TriangleAndIteration> candidates = new PriorityQueue<>(triangleComparator);
        List<TriangleAndIteration> done = new ArrayList<>();

        for (HTriangle t : triangles) {
            TriangleAndIteration ti = new TriangleAndIteration(t, 1);
            if (isCandidate(t, r)) {
                candidates.add(ti);
            } else {
                done.add(ti);
            }
        }

        int iterations = 0;
        boolean _isSetMaxTriangles = isSet(r.maxTriangles);
        boolean isSetMaxIterations = isSet(r.maxIterations);

        while (!candidates.isEmpty()) {
            iterations++;
            int total = done.size() + candidates.size();
            if (_isSetMaxTriangles && total >= r.maxTriangles) {
                done.addAll(candidates);
                candidates.clear();
                break;
            }
            if (isSetMaxIterations && iterations >= r.maxIterations) {
                done.addAll(candidates);
                candidates.clear();
                break;
            }

            TriangleAndIteration ti = candidates.poll();
            splitAndConform(ti, candidates, done, r);
        }
        done.addAll(candidates);
//        System.out.println("MeshRefinementHelper");
//        for (TriangleAndIteration triangleAndIteration : done) {
//            System.out.println(triangleAndIteration);
//        }
//        AreaComponent.showDialog(done.stream().map(x -> x.triangle).toArray(HGeometry[]::new));
        return done.stream().map(x -> x.triangle).collect(Collectors.toList());
    }


    private static void splitAndConform(
            TriangleAndIteration ti,
            PriorityQueue<TriangleAndIteration> candidates,
            List<TriangleAndIteration> done,
            MeshRefinement r) {

        HTriangle t = ti.triangle;
        double d12 = t.p1().distance(t.p2());
        double d23 = t.p2().distance(t.p3());
        double d13 = t.p1().distance(t.p3());
        HPoint a, b, opposite;
        if (d12 >= d23 && d12 >= d13) {
            a = t.p1();
            b = t.p2();
            opposite = t.p3();
        } else if (d23 >= d12 && d23 >= d13) {
            a = t.p2();
            b = t.p3();
            opposite = t.p1();
        } else {
            a = t.p1();
            b = t.p3();
            opposite = t.p2();
        }
        HPoint mid = HPoint.create((a.x + b.x) / 2.0, (a.y + b.y) / 2.0);

        // split ti into two children
        try {
            TriangleAndIteration c1 = new TriangleAndIteration(new DefaultHTriangle(a, mid, opposite), ti.iteration + 1);
            if (isCandidate(c1.triangle, r)) candidates.add(c1);
            else done.add(c1);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            TriangleAndIteration c2 = new TriangleAndIteration(new DefaultHTriangle(mid, b, opposite), ti.iteration + 1);
            if (isCandidate(c2.triangle, r)) candidates.add(c2);
            else done.add(c2);
        } catch (IllegalArgumentException ignored) {
        }

        // find neighbor sharing edge (a,b) and conform it
        TriangleAndIteration neighbor = null;
        for (TriangleAndIteration x : candidates) {
            if (sharesEdge(x.triangle, a, b)) {
                neighbor = x;
                break;
            }
        }
        if (neighbor == null) {
            for (TriangleAndIteration x : done) {
                if (sharesEdge(x.triangle, a, b)) {
                    neighbor = x;
                    break;
                }
            }
        }
        if (neighbor == null) return; // boundary edge, no T-junction

        // remove neighbor and split it at mid on edge (a,b)
        candidates.remove(neighbor);
        done.remove(neighbor);

        // find neighbor's opposite point
        HPoint nOpposite = null;
        for (HPoint p : neighbor.triangle.getPoints()) {
            if (!p.equals(a) && !p.equals(b)) {
                nOpposite = p;
                break;
            }
        }
        if (nOpposite == null) return;

        // check if (a,b) is neighbor's longest edge
        // if not, we must split neighbor on ITS longest edge first (Rivara)
        double nLongest = neighbor.triangle.longestEdge();
        if (Math.abs(a.distance(b) - nLongest) < 1e-12) {
            // (a,b) is neighbor's longest — split directly at mid
            try {
                TriangleAndIteration c1 = new TriangleAndIteration(new DefaultHTriangle(a, mid, nOpposite), neighbor.iteration + 1);
                if (isCandidate(c1.triangle, r)) candidates.add(c1);
                else done.add(c1);
            } catch (IllegalArgumentException ignored) {
            }
            try {
                TriangleAndIteration c2 = new TriangleAndIteration(new DefaultHTriangle(mid, b, nOpposite), neighbor.iteration + 1);
                if (isCandidate(c2.triangle, r)) candidates.add(c2);
                else done.add(c2);
            } catch (IllegalArgumentException ignored) {
            }
        } else {
            // (a,b) is not neighbor's longest — split neighbor on its longest edge
            // then recurse until the T-junction on (a,b) is resolved
            splitAndConform(neighbor, candidates, done, r);
            // now find whichever child still has edge (a,b) and split it at mid
            List<TriangleAndIteration> toCheck = new ArrayList<>(candidates);
            toCheck.addAll(done);
            for (TriangleAndIteration child : toCheck) {
                if (sharesEdge(child.triangle, a, b)) {
                    candidates.remove(child);
                    done.remove(child);
                    splitAndConform(new TriangleAndIteration(child.triangle, child.iteration), candidates, done, r);
                    break;
                }
            }
        }
    }

    private static List<TriangleAndIteration> conformingSplit(
            HPoint a, HPoint b, HPoint mid,
            PriorityQueue<TriangleAndIteration> candidates,
            List<TriangleAndIteration> done) {

        List<TriangleAndIteration> result = new ArrayList<>();

        // find neighbor sharing edge (a,b)
        TriangleAndIteration neighbor = null;
        for (TriangleAndIteration x : candidates) {
            if (sharesEdge(x.triangle, a, b)) {
                neighbor = x;
                break;
            }
        }
        if (neighbor == null) {
            for (TriangleAndIteration x : done) {
                if (sharesEdge(x.triangle, a, b)) {
                    neighbor = x;
                    break;
                }
            }
        }

        if (neighbor == null) return result; // boundary edge, no neighbor — fine

        // remove neighbor from wherever it is
        candidates.remove(neighbor);
        done.remove(neighbor);

        HTriangle n = neighbor.triangle;
        double na = n.p1().distance(n.p2());
        double nb = n.p2().distance(n.p3());
        double nc = n.p1().distance(n.p3());
        double longestN = Math.max(na, Math.max(nb, nc));
        double sharedEdgeLen = a.distance(b);

        if (Math.abs(sharedEdgeLen - longestN) < 1e-12) {
            // shared edge IS neighbor's longest edge — split it directly at mid
            result.addAll(splitTriangleAtMidpoint(neighbor, a, b, mid));
        } else {
            // shared edge is NOT neighbor's longest edge
            // split neighbor on ITS longest edge first, then recurse
            // this is Rivara's algorithm — propagate until conforming
            HPoint na2, nb2, nopp;
            if (na >= nb && na >= nc) {
                na2 = n.p1();
                nb2 = n.p2();
                nopp = n.p3();
            } else if (nb >= na && nb >= nc) {
                na2 = n.p2();
                nb2 = n.p3();
                nopp = n.p1();
            } else {
                na2 = n.p1();
                nb2 = n.p3();
                nopp = n.p2();
            }
            HPoint mid2 = HPoint.create((na2.x + nb2.x) / 2.0, (na2.y + nb2.y) / 2.0);

            // split neighbor
            List<TriangleAndIteration> neighborChildren = splitTriangleAtMidpoint(neighbor, na2, nb2, mid2);
            // recurse: fix the T-junction this new split may have created
            for (TriangleAndIteration child : neighborChildren) {
                result.add(child);
            }
            // now also fix the original T-junction by splitting one of the children
            // that still has edge (a,b)
            List<TriangleAndIteration> toReprocess = new ArrayList<>(result);
            result.clear();
            for (TriangleAndIteration child : toReprocess) {
                if (sharesEdge(child.triangle, a, b)) {
                    result.addAll(conformingSplit(a, b, mid, candidates, done));
                } else {
                    result.add(child);
                }
            }
        }
        return result;
    }

    private static TriangleAndIteration findNeighborSharingEdge(
            HPoint a, HPoint b,
            PriorityQueue<TriangleAndIteration> candidates,
            List<TriangleAndIteration> done) {
        for (TriangleAndIteration ti : candidates) {
            if (sharesEdge(ti.triangle, a, b)) return ti;
        }
        for (TriangleAndIteration ti : done) {
            if (sharesEdge(ti.triangle, a, b)) return ti;
        }
        return null;
    }

    private static boolean sharesEdge(HTriangle t, HPoint a, HPoint b) {
        List<HPoint> pts = t.getPoints();
        boolean hasA = pts.stream().anyMatch(p -> p.equals(a));
        boolean hasB = pts.stream().anyMatch(p -> p.equals(b));
        return hasA && hasB;
    }

    private static List<TriangleAndIteration> splitTriangleAtMidpoint(
            TriangleAndIteration ti, HPoint a, HPoint b, HPoint mid) {
        HTriangle t = ti.triangle;
        HPoint opposite = null;
        for (HPoint p : t.getPoints()) {
            if (!p.equals(a) && !p.equals(b)) {
                opposite = p;
                break;
            }
        }
        if (opposite == null) return Collections.emptyList();
        List<TriangleAndIteration> result = new ArrayList<>();
        try {
            result.add(new TriangleAndIteration(new DefaultHTriangle(a, mid, opposite), ti.iteration + 1));
        } catch (IllegalArgumentException ignored) {
        }
        try {
            result.add(new TriangleAndIteration(new DefaultHTriangle(mid, b, opposite), ti.iteration + 1));
        } catch (IllegalArgumentException ignored) {
        }
        return result;
    }

    private static class TriangleAndIteration {
        HTriangle triangle;
        int iteration;

        public TriangleAndIteration(HTriangle triangle, int iteration) {
            this.triangle = triangle;
            this.iteration = iteration;
        }

        @Override
        public String toString() {
            return "TriangleAndIteration{" +
                    "triangle=" + triangle +
                    ", iteration=" + iteration +
                    '}';
        }
    }

    private static boolean isCandidate(HTriangle t, MeshRefinement r) {
        if (isSet(r.maxSurface) && t.area() > r.maxSurface) return true;
        if (isSet(r.maxWidth) && t.longestEdge() > r.maxWidth) return true;
        // no constraints active: everything is a candidate (maxTriangles drives it)
        if (r.predicate != null) {
            if (!r.predicate.test(t)) {
                return false;
            }
        }
        return !isSet(r.maxSurface) && !isSet(r.maxWidth);
    }


    private static Comparator<TriangleAndIteration> splitComparator(MeshRefinement r) {
        boolean surfaceSet = isSet(r.maxSurface);
        boolean widthSet = isSet(r.maxWidth);
        return new Comparator<TriangleAndIteration>() {
            @Override
            public int compare(TriangleAndIteration o1, TriangleAndIteration o2) {
                if (surfaceSet) {
                    int a = Double.compare(o2.triangle.area(), o1.triangle.area());
                    if (a != 0) return a;
                }
                if (widthSet) {
                    int a = Double.compare(o2.triangle.longestEdge(), o1.triangle.longestEdge());
                    if (a != 0) return a;
                }
                if (!surfaceSet && !widthSet) {
                    int a = Double.compare(o2.triangle.area(), o1.triangle.area());
                    if (a != 0) return a;
                    a = Double.compare(o2.triangle.longestEdge(), o1.triangle.longestEdge());
                    if (a != 0) return a;
                }
                {
                    int a = Integer.compare(o1.iteration, o2.iteration);
                    return a;
                }
                // need stable triangulation ??
            }
        };
    }

    private static boolean isSet(int v) {
        return v > 0 && v < Integer.MAX_VALUE;
    }

    private static boolean isSet(double v) {
        return v > 0 && !Double.isInfinite(v) && !Double.isNaN(v) && v < Double.MAX_VALUE;
    }

    private static List<TriangleAndIteration> splitTriangle(TriangleAndIteration ti) {
        // split on longest edge — best for quality
        HTriangle t = ti.triangle;
        HPoint p1 = t.p1(), p2 = t.p2(), p3 = t.p3();
        double d12 = p1.distance(p2);
        double d23 = p2.distance(p3);
        double d13 = p1.distance(p3);

        HPoint a, b, opposite;
        if (d12 >= d23 && d12 >= d13) {
            a = p1;
            b = p2;
            opposite = p3;
        } else if (d23 >= d12 && d23 >= d13) {
            a = p2;
            b = p3;
            opposite = p1;
        } else {
            a = p1;
            b = p3;
            opposite = p2;
        }

        HPoint mid = HPoint.create((a.x + b.x) / 2.0, (a.y + b.y) / 2.0);

        List<TriangleAndIteration> result = new ArrayList<>();
        try {
            result.add(new TriangleAndIteration(new DefaultHTriangle(a, mid, opposite), ti.iteration + 1));
        } catch (IllegalArgumentException ignored) {
            return new ArrayList<>();
        }
        try {
            result.add(new TriangleAndIteration(new DefaultHTriangle(mid, b, opposite), ti.iteration + 1));
        } catch (IllegalArgumentException ignored) {
            return new ArrayList<>();
        }
        return result;
    }

    public static boolean containsWithEps(HTriangle t, double x, double y) {
        if (t.contains(x, y)) return true;
        double bx = (t.p1().x + t.p2().x + t.p3().x) / 3.0;
        double by = (t.p1().y + t.p2().y + t.p3().y) / 3.0;
        return t.contains(x + (bx - x) * 1e-6, y + (by - y) * 1e-6);
    }
}
