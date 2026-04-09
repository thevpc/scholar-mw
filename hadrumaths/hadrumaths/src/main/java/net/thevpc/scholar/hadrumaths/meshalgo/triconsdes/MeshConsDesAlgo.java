package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.*;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeshConsDesAlgo implements MeshAlgo, Cloneable {

    private static final long serialVersionUID = 1L;
    private MeshTriangulationOptions option = new MeshTriangulationOptions();

    public MeshConsDesAlgo(int maxTriangles) {
        this(new MeshTriangulationOptions().setMaxCount(maxTriangles));
    }

    public MeshConsDesAlgo(MeshTriangulationOptions options) {
        this();
        this.option = options;
    }

    public MeshConsDesAlgo() {
    }

    //    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("options", option);
//        return h.toString();
//    }
    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("options", NElementHelper.elem(option));
        return sb.build();
    }

    public List<MeshZone> meshPolygon(HGeometry polygon) {
        return meshPolygon(new HGeometry[]{polygon});
    }

    public List<MeshZone> meshPolygon(HGeometry[] polygons) {
        AlgoInfo info = new AlgoInfo();
        for (HGeometry polygon : polygons) {
            initPolygon(polygon, info);
        }
        int iteration = 1;
        while (option.isMeshAllowed(info.triangles, iteration)) {
            destructTriangles(info, iteration);

//            iteration++;
//            if(!option.isMeshAllowed(info.triangles, iteration)){
//                break;
//            }
            constructTriangles(info, iteration);
            iteration++;
        }
        ArrayList<MeshZone> ret = new ArrayList<MeshZone>();
        for (HTriangle triangle : info.triangles) {

            MeshZone z = new MeshZone(triangle, MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);

//            MeshZone z = new MeshZone(triangle.toPolygon().toArea(), MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);
            ret.add(z);
        }
        return ret;
    }

    private void initPolygon_old(HGeometry geom, AlgoInfo info) {
        HPolygon pg = geom.toPolygon();
        ArrayList<HTriangle> trianglesList = new ArrayList<HTriangle>();
        ArrayList<HPoint> points = new ArrayList<HPoint>();
        List<HPoint> gpoints = pg.getPoints();
        points.addAll(gpoints);
        points.add(gpoints.get(0));

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                HPoint pi = points.get(i);
                HPoint pj = points.get(j);
                if (!pi.equals(pj)) {
                    for (HPoint point : points) {
                        if (!pi.equals(point) && !pj.equals(point)) {

                            int inter = 0;
                            HPoint w = pi;
                            HPoint x = pj;
                            HPoint q = point;
                            if (!GeomUtils.getDomain(w, x, q).isEmpty()) {
                                HTriangle t = (new DefaultHTriangle(w, x, q));
                                HPoint centroid = t.getBarycenter();
                                if (!pg.contains(centroid.x, centroid.y)) {
                                    continue;
                                }
                                if (!trianglesList.isEmpty()) {
                                    for (HTriangle aListeTriangle1 : trianglesList) {
                                        if (t.intersection(aListeTriangle1)) {
                                            inter = 1;
                                        }
                                    }
                                }
                                if ((inter == 0) && (trianglesList.size() < gpoints.size() - 2)) {
                                    trianglesList.add(t);
                                    for (int b = 0; b < trianglesList.size(); b++) {
                                        HTriangle v = trianglesList.get(b);
                                        for (int z = b + 1; z < trianglesList.size(); z++) {
                                            if (v.equals(trianglesList.get(z))) {
                                                trianglesList.remove(z);
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
        }
        for (HTriangle aListeTriangle1 : trianglesList) {
            info.triangles.add(aListeTriangle1);
        }

    }

    private void initPolygon(HGeometry geom, AlgoInfo info) {
        HPolygon pg = geom.toPolygon();
        List<HPoint> ring = new ArrayList<>(pg.getPoints());

        // Remove closing duplicate
        if (ring.size() > 1 && ring.get(0).equals(ring.get(ring.size() - 1))) {
            ring.remove(ring.size() - 1);
        }

        // Compute signed area to determine winding (positive = CCW)
        double signedArea = 0;
        int n0 = ring.size();
        for (int i = 0; i < n0; i++) {
            HPoint a = ring.get(i);
            HPoint b = ring.get((i + 1) % n0);
            signedArea += (a.x * b.y - b.x * a.y);
        }
        // Ensure CCW for ear-clipping
        if (signedArea < 0) {
            Collections.reverse(ring);
        }

        while (ring.size() >= 3) {
            boolean earFound = false;
            int n = ring.size();
            for (int i = 0; i < n; i++) {
                HPoint prev = ring.get((i - 1 + n) % n);
                HPoint curr = ring.get(i);
                HPoint next = ring.get((i + 1) % n);

                // Convex and non-degenerate check BEFORE constructing Triangle
                double cross = (curr.x - prev.x) * (next.y - prev.y)
                        - (curr.y - prev.y) * (next.x - prev.x);
                if (cross <= 0) {
                    continue;  // concave or collinear
                }
                // Extra collinearity guard - skip zero-area ears
                if (Math.abs(cross) < 1e-20) {
                    continue;
                }

                HTriangle ear = new DefaultHTriangle(prev, curr, next);

                // Centroid inside polygon
                HPoint centroid = ear.getBarycenter();
                if (!pg.contains(centroid.x, centroid.y)) {
                    continue;
                }

                // No other vertex inside ear
                boolean valid = true;
                for (int j = 0; j < n; j++) {
                    if (j == (i - 1 + n) % n || j == i || j == (i + 1) % n) {
                        continue;
                    }
                    HPoint test = ring.get(j);
                    if (ear.contains(test.x, test.y)) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    continue;
                }

                info.triangles.add(ear);
                ring.remove(i);
                earFound = true;
                break;
            }
            if (!earFound) {
                break;
            }
        }
    }

    private void destructTriangles(AlgoInfo info, int iteration) {
        HTriangle t = option.selectMeshTriangle(info.triangles, iteration);
        if (t != null) {
            HPoint p = t.getBarycenter();
            //DPoint o = new DPoint();
            if (!(info.triangles.isEmpty())) {
                for (int i = 0; i < info.triangles.size(); i++) {
                    HPoint o = (info.triangles.get(i)).getCenter();
                    if (t.isNeighborhood(info.triangles.get(i)) && (info.triangles.get(i).getCircleRadius() >= o.distance(p))) {
                        info.listeDetruite.add(info.triangles.get(i));
                        info.triangles.remove(i);
                        i = i - 1;
                    }

                }
            }
        }
        info.selectedTriangle = t;
    }

    private void constructTriangles(AlgoInfo info, int iteration) {
        HTriangle selectedTriangle;

        HPoint barycenter;
//        DPoint h = new DPoint();
        if (!info.listeDetruite.isEmpty()) {
            selectedTriangle = info.selectedTriangle;
            barycenter = selectedTriangle.getBarycenter();
            //si on a un seul Triangle dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 1) {
                HTriangle t1 = info.listeDetruite.get(0);
                info.triangles.add(new DefaultHTriangle(t1.p1(), t1.p3(), barycenter));
                info.triangles.add(new DefaultHTriangle(t1.p1(), t1.p2(), barycenter));
                info.triangles.add(new DefaultHTriangle(t1.p2(), t1.p3(), barycenter));
            }
            //si on a 4 triangles dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 4) {
                ArrayList<HPoint> n = new ArrayList<HPoint>();
                n.add(0, selectedTriangle.p1());
                n.add(1, selectedTriangle.p2());
                n.add(2, selectedTriangle.p3());
                for (HTriangle aListeDetruite : info.listeDetruite) {
                    if (!(selectedTriangle.equals(aListeDetruite))) {
                        ArrayList<HPoint> pt = new ArrayList<HPoint>();
                        ArrayList<HPoint> m = new ArrayList<HPoint>();
                        m.add(0, aListeDetruite.p1());
                        m.add(1, aListeDetruite.p2());
                        m.add(2, aListeDetruite.p3());
                        for (int u = 0; u < m.size(); u++) {
                            int j = 0;
                            while ((j < n.size())) {
                                if (!(((m.get(u).x) == (n.get(j).x)) && ((m.get(u).y) == (n.get(j).y)))) {
                                    j = j + 1;
                                } else {
                                    j = 3;
                                    pt.add(m.get(u));
                                    m.remove(u);
                                    u = u - 1;

                                }

                            }
                        }
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(1), barycenter));
                        for (int r = 0; r < m.size(); r++) {
                            m.remove(r);
                            r = r - 1;
                        }
                        for (int r = 0; r < pt.size(); r++) {
                            pt.remove(r);
                            r = r - 1;
                        }
                    }
                }
            }
            //si on a deux triangles dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 2) {
                ArrayList<HPoint> n = new ArrayList<HPoint>();

                n.add(0, selectedTriangle.p1());
                n.add(1, selectedTriangle.p2());
                n.add(2, selectedTriangle.p3());
                for (HTriangle aListeDetruite : info.listeDetruite) {
//                    int k = 0;
                    if (!selectedTriangle.equals(aListeDetruite)) {
                        ArrayList<HPoint> pt = new ArrayList<HPoint>();
                        ArrayList<HPoint> m = new ArrayList<HPoint>();
                        m.add(0, aListeDetruite.p1());
                        m.add(1, aListeDetruite.p2());
                        m.add(2, aListeDetruite.p3());

                        for (int u = 0; u < m.size(); u++) {
                            int j = 0;
                            while ((j < n.size())) {
                                HPoint mu_point = m.get(u);
                                HPoint nj_point = n.get(j);
                                if (!mu_point.equals(nj_point)) {
                                    j = j + 1;
                                } else {
                                    n.remove(j);
                                    j = 3;
                                    pt.add(mu_point);
                                    m.remove(u);
                                    u = u - 1;
                                }
                            }
                        }
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(1), barycenter));
                        info.triangles.add(new DefaultHTriangle(n.get(0), pt.get(0), barycenter));
                        info.triangles.add(new DefaultHTriangle(n.get(0), pt.get(1), barycenter));
                    }

                }

            }

            //si on a 3 triangles dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 3) {
//                int k = 0;
                ArrayList<HPoint> res = new ArrayList<>();
                for (HTriangle tr : info.listeDetruite) {
                    if (!selectedTriangle.equals(tr) && selectedTriangle.isNeighborhood(tr)) {
                        ArrayList<HPoint> n = new ArrayList<HPoint>(selectedTriangle.getPoints());
                        ArrayList<HPoint> m = new ArrayList<HPoint>(tr.getPoints());
                        ArrayList<HPoint> pt = new ArrayList<HPoint>();
                        GeomUtils.dispatch(selectedTriangle.getPoints(), tr.getPoints(), n, m, pt);
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new DefaultHTriangle(m.get(0), pt.get(1), barycenter));
                        res.add(n.get(0));
                    }
                }
                info.triangles.add(new DefaultHTriangle(res.get(0), res.get(1), barycenter));
            }
        }
        for (int i = 0; i < info.listeDetruite.size(); i++) {
            info.listeDetruite.remove(i);
            i = i - 1;
        }
        for (int i = 0; i < info.triangles.size(); i++) {
            HTriangle v = info.triangles.get(i);
            for (int j = i + 1; j < info.triangles.size(); j++) {
                if (v.equals(info.triangles.get(j))) {
                    info.triangles.remove(j);
                    j = j - 1;
                }
            }
        }
    }

    public MeshTriangulationOptions getOption() {
        return option;
    }

    public void setOption(MeshOptions op) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setOption(MeshTriangulationOptions op) {
        this.option = op;
    }

    public MeshConsDesAlgo clone() {
        try {
            return (MeshConsDesAlgo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Never");
        }
    }

    private static class AlgoInfo {

        private final List<HTriangle> triangles = new ArrayList<HTriangle>();
        private final List<HTriangle> listeDetruite = new ArrayList<HTriangle>();
        private HTriangle selectedTriangle;

        public AlgoInfo() {
        }
    }

}
