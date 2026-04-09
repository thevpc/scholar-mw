package net.thevpc.scholar.hadrumaths.meshalgo.triflip;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.*;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.ArrayList;
import java.util.List;

public class MeshFlipAlgo implements MeshAlgo {
    private static final long serialVersionUID = 1L;
    OptionFlip option;
    ArrayList<HTriangle> listeTriangle;

    public MeshFlipAlgo() {
        listeTriangle = new ArrayList<>();
    }

    public List<MeshZone> meshPolygon(HGeometry polygon) {
        return meshPolygon(new HGeometry[]{polygon});
    }

    public List<MeshZone> meshPolygon(HGeometry[] polygons) {
        for (HGeometry polygon : polygons) {
            initPolygon(polygon);
        }
        int iteration = 1;
        while (option.isMeshAllowed(listeTriangle, iteration)) {
            constructionTriangle(iteration);
        }
        ArrayList<MeshZone> ret = new ArrayList<MeshZone>();
        for (HTriangle triangle : listeTriangle) {
            MeshZone z = new MeshZone(triangle, MeshZoneShape.TRIANGLE, MeshZoneType.MAIN);
            ret.add(z);
        }
        return ret;
    }

//    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("options", option);
//        return h.toString();
//    }

    private void initPolygon(HGeometry geom) {
        HPolygon pg = geom.toPolygon();
        List<HPoint> gpoints = pg.getPoints();
        ArrayList<HTriangle> listeTriangle1 = new ArrayList<HTriangle>();
        ArrayList<HPoint> points = new ArrayList<HPoint>();
        for (int i = 0; i < gpoints.size(); i++) {
            HPoint p = gpoints.get(i);
            if (p.x != 0 || p.y != 0) {
                points.add(p);
            }

        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if ((points.get(i).x != points.get(j).x) || (points.get(i).y != points.get(j).y)) {
                    for (HPoint point : points) {
                        if (((point.x != points.get(j).x) || (point.y != points.get(j).y)) && ((point.x != points.get(i).x) || (point.y != points.get(i).y))) {

                            int inter = 0;
                            HPoint w = HPoint.create(points.get(i).x, points.get(i).y);
                            HPoint x = HPoint.create(points.get(j).x, points.get(j).y);
                            HPoint q = HPoint.create(point.x, point.y);
                            HTriangle t = (new DefaultHTriangle(w, x, q));
                            if (!listeTriangle1.isEmpty()) {
                                for (HTriangle aListeTriangle1 : listeTriangle1) {
                                    if (t.intersection(aListeTriangle1)) {
                                        inter = 1;
                                    }
                                }
                            }
                            if ((inter == 0) && (listeTriangle1.size() < gpoints.size() - 2)) {
                                listeTriangle1.add(t);
                                for (int b = 0; b < listeTriangle1.size(); b++) {
                                    HTriangle v = listeTriangle1.get(b);
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
        for (HTriangle aListeTriangle1 : listeTriangle1) {
            listeTriangle.add(aListeTriangle1);
        }

    }

    private void constructionTriangle(int iteration) {
        ArrayList<HTriangle> ld = new ArrayList<HTriangle>();
        ArrayList<HTriangle> newTriangle = new ArrayList<HTriangle>();
        HPoint p;
        HTriangle t = option.selectMeshTriangle(listeTriangle, iteration);
        p = t.getBarycenter();
        for (int i = 0; i < listeTriangle.size(); i++) {
            if (t.equals(listeTriangle.get(i))) {
                listeTriangle.remove(i);
                i = i - 1;
            }
        }
        ld.add(new DefaultHTriangle(p, t.p1(), t.p2()));
        ld.add(new DefaultHTriangle(p, t.p1(), t.p3()));
        ld.add(new DefaultHTriangle(p, t.p2(), t.p3()));
        for (HTriangle aLd : ld) {
            int k = 0;
            for (int j = 0; j < listeTriangle.size(); j++) {
                if (aLd.isNeighborhood(listeTriangle.get(j))) {
                    k = 1;
                    ArrayList<HPoint> m = new ArrayList<HPoint>();
                    ArrayList<HPoint> n = new ArrayList<HPoint>();
                    ArrayList<HPoint> op = new ArrayList<HPoint>();
                    ArrayList<HPoint> v = new ArrayList<HPoint>();
                    m.add(listeTriangle.get(j).p1());
                    m.add((listeTriangle.get(j)).p2());
                    m.add((listeTriangle.get(j)).p3());
                    n.add(aLd.p1());
                    n.add(aLd.p2());
                    n.add(aLd.p3());
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
                    HPoint p1 = aLd.getCenter();
                    HPoint p2 = listeTriangle.get(j).getCenter();
                    if ((p1.distance(op.get(0)) < aLd.getCircleRadius()) || (p2.distance(op.get(1)) < listeTriangle.get(j).getCircleRadius())) {
                        newTriangle.add(new DefaultHTriangle(op.get(0), op.get(1), v.get(0)));
                        newTriangle.add(new DefaultHTriangle(op.get(0), op.get(1), v.get(1)));
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
        for (HTriangle aNewTriangle : newTriangle) {
            listeTriangle.add(aNewTriangle);
        }
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("options", NElementHelper.elem(option));
        sb.add("triangles", NElementHelper.elem(listeTriangle));
        return sb.build();
    }

    public OptionFlip getOption() {
        return option;
    }

    public void setOption(MeshOptions op) {
        //
    }

    public void setOption(OptionFlip op) {
        this.option = op;
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
