package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.*;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MeshConsDesAlgo implements MeshAlgo, Cloneable {
    private static final long serialVersionUID = 1L;
    private MeshOptionsConsDes option = new MeshOptionsConsDes();

    public MeshConsDesAlgo(int maxTriangles) {
        this(new MeshOptionsConsDes().setMaxTriangles(maxTriangles));
    }

    public MeshConsDesAlgo(MeshOptionsConsDes options) {
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

    public Collection<MeshZone> meshPolygon(Geometry polygon) {
        return meshPolygon(new Geometry[]{polygon});
    }

    public Collection<MeshZone> meshPolygon(Geometry[] polygons) {
        AlgoInfo info = new AlgoInfo();
        for (Geometry polygon : polygons) {
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
        for (Triangle triangle : info.triangles) {

            MeshZone z = new MeshZone(triangle, MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);

//            MeshZone z = new MeshZone(triangle.toPolygon().toArea(), MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);

            ret.add(z);
        }
        return ret;
    }

    private void initPolygon(Geometry geom, AlgoInfo info) {
        Polygon pg = geom.toPolygon();
        ArrayList<Triangle> trianglesList = new ArrayList<Triangle>();
        ArrayList<Point> points = new ArrayList<Point>();
        List<Point> gpoints = pg.getPoints();
        points.addAll(gpoints);
        points.add(gpoints.get(0));

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point pi = points.get(i);
                Point pj = points.get(j);
                if (!pi.equals(pj)) {
                    for (Point point : points) {
                        if (!pi.equals(point) && !pj.equals(point)) {

                            int inter = 0;
                            Point w = pi;
                            Point x = pj;
                            Point q = point;
                            if (!GeomUtils.getDomain(w, x, q).isEmpty()) {
                                Triangle t = (new Triangle(w, x, q));
                                if (!trianglesList.isEmpty()) {
                                    for (Triangle aListeTriangle1 : trianglesList) {
                                        if (t.intersection(aListeTriangle1)) {
                                            inter = 1;
                                        }
                                    }
                                }
                                if ((inter == 0) && (trianglesList.size() < gpoints.size() - 2)) {
                                    trianglesList.add(t);
                                    for (int b = 0; b < trianglesList.size(); b++) {
                                        Triangle v = trianglesList.get(b);
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
        for (Triangle aListeTriangle1 : trianglesList) {
            info.triangles.add(aListeTriangle1);
        }

    }

    private void destructTriangles(AlgoInfo info, int iteration) {
        Triangle t = option.selectMeshTriangle(info.triangles, iteration);
        if (t != null) {
            Point p = t.getBarycenter();
            //DPoint o = new DPoint();
            if (!(info.triangles.isEmpty())) {
                for (int i = 0; i < info.triangles.size(); i++) {
                    Point o = (info.triangles.get(i)).getCenter();
                    if (t.isNeighberhood(info.triangles.get(i)) && (info.triangles.get(i).getRayonCercle() >= o.distance(p))) {
                        info.listeDetruite.add(info.triangles.get(i));
                        info.triangles.remove(i);
                        i = i - 1;
                    }

                }
            } else {
                System.out.println("la liste des triangles est vide");
            }
        } else {
            System.out.println("Le maillage est termine");

        }
        info.selectedTriangle = t;
    }

    private void constructTriangles(AlgoInfo info, int iteration) {
        Triangle selectedTriangle;

        Point barycenter;
//        DPoint h = new DPoint();
        if (!info.listeDetruite.isEmpty()) {
            selectedTriangle = info.selectedTriangle;
            barycenter = selectedTriangle.getBarycenter();
            //si on a un seul Triangle dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 1) {
                Triangle t1 = info.listeDetruite.get(0);
                info.triangles.add(new Triangle(t1.p1, t1.p3, barycenter));
                info.triangles.add(new Triangle(t1.p1, t1.p2, barycenter));
                info.triangles.add(new Triangle(t1.p2, t1.p3, barycenter));
            }
            //si on a 4 triangles dans la liste des triangles detruites:
            if (info.listeDetruite.size() == 4) {
                ArrayList<Point> n = new ArrayList<Point>();
                n.add(0, selectedTriangle.p1);
                n.add(1, selectedTriangle.p2);
                n.add(2, selectedTriangle.p3);
                for (Triangle aListeDetruite : info.listeDetruite) {
                    if (!(selectedTriangle.equals(aListeDetruite))) {
                        ArrayList<Point> pt = new ArrayList<Point>();
                        ArrayList<Point> m = new ArrayList<Point>();
                        m.add(0, aListeDetruite.p1);
                        m.add(1, aListeDetruite.p2);
                        m.add(2, aListeDetruite.p3);
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
                        info.triangles.add(new Triangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new Triangle(m.get(0), pt.get(1), barycenter));
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
                ArrayList<Point> n = new ArrayList<Point>();

                n.add(0, selectedTriangle.p1);
                n.add(1, selectedTriangle.p2);
                n.add(2, selectedTriangle.p3);
                for (Triangle aListeDetruite : info.listeDetruite) {
//                    int k = 0;
                    if (!selectedTriangle.equals(aListeDetruite)) {
                        ArrayList<Point> pt = new ArrayList<Point>();
                        ArrayList<Point> m = new ArrayList<Point>();
                        m.add(0, aListeDetruite.p1);
                        m.add(1, aListeDetruite.p2);
                        m.add(2, aListeDetruite.p3);

                        for (int u = 0; u < m.size(); u++) {
                            int j = 0;
                            while ((j < n.size())) {
                                Point mu_point = m.get(u);
                                Point nj_point = n.get(j);
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
                        info.triangles.add(new Triangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new Triangle(m.get(0), pt.get(1), barycenter));
                        info.triangles.add(new Triangle(n.get(0), pt.get(0), barycenter));
                        info.triangles.add(new Triangle(n.get(0), pt.get(1), barycenter));
                    }

                }

            }

            //si on a 3 triangles dans la liste des triangles detruites:

            if (info.listeDetruite.size() == 3) {
//                int k = 0;
                ArrayList<Point> res = new ArrayList<Point>();
                for (Triangle tr : info.listeDetruite) {
                    if (!selectedTriangle.equals(tr) && selectedTriangle.isNeighberhood(tr)) {
                        ArrayList<Point> n = new ArrayList<Point>(selectedTriangle.getPoints());
                        ArrayList<Point> m = new ArrayList<Point>(tr.getPoints());
                        ArrayList<Point> pt = new ArrayList<Point>();
                        GeomUtils.dispatch(selectedTriangle.getPoints(), tr.getPoints(), n, m, pt);
                        // System.out.println(pt.size());
                        info.triangles.add(new Triangle(m.get(0), pt.get(0), barycenter));
                        info.triangles.add(new Triangle(m.get(0), pt.get(1), barycenter));
                        res.add(n.get(0));
                    }
                }
                info.triangles.add(new Triangle(res.get(0), res.get(1), barycenter));
            }
        } else {
            System.out.println("la liste des triangles detruits est vide");
        }
        for (int i = 0; i < info.listeDetruite.size(); i++) {
            info.listeDetruite.remove(i);
            i = i - 1;
        }
        for (int i = 0; i < info.triangles.size(); i++) {
            Triangle v = info.triangles.get(i);
            for (int j = i + 1; j < info.triangles.size(); j++) {
                if (v.equals(info.triangles.get(j))) {
                    info.triangles.remove(j);
                    j = j - 1;
                }
            }
        }
    }

    public MeshOptionsConsDes getOption() {
        return option;
    }

    public void setOption(MeshOptions op) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setOption(MeshOptionsConsDes op) {
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
        private final List<Triangle> triangles = new ArrayList<Triangle>();
        private final List<Triangle> listeDetruite = new ArrayList<Triangle>();
        private Triangle selectedTriangle;

        public AlgoInfo() {
        }
    }


}
