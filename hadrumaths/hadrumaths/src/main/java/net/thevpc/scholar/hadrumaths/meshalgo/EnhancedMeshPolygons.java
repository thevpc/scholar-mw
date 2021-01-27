package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class EnhancedMeshPolygons implements HSerializable {
    private static final long serialVersionUID = 1L;
    private final double surface;
    private final Polygon[] polygon;

    public EnhancedMeshPolygons(Polygon[] p, double a) {
        polygon = p;
        surface = a;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("surface", context.elem(surface))
                .add("polygons", context.elem(polygon))
                .build();
    }

//    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("surface", surface);
//        h.add("polygons", polygon);
//        return h.toString();
//    }

    public Triangle firstTriangleInZoneValide(List<Triangle> t) {
        int k = -1;
        Collections.sort(t, Triangle.SURFACE_COMPARATOR);
        for (int j = 0; j < t.size(); j++) {
            for (int i = 0; i < polygon.length; i++) {
                if ((polygon[i].contains(t.get(j).p1.x, t.get(j).p1.y) || polygon[i].contains(t.get(j).p2.x, t.get(j).p2.y) || polygon[i].contains(t.get(j).p3.x, t.get(j).p3.y)) && t.get(j).getSurface() > surface) {
                    k = j;
                    j = t.size();
                    i = t.size();
                }
            }
        }
        if (k != -1) {
            return t.get(k);
        } else {
            return null;
        }
    }

    public boolean isZoneValide(java.util.List<Triangle> t) {
        int k = 0;
        for (Triangle a : t) {
            for (Polygon b : polygon) {
                if ((b.contains(a.p1.x, a.p1.y) || b.contains(a.p2.x, a.p2.y) || b.contains(a.p3.x, a.p3.y)) && a.getSurface() > surface) {
                    k = 1;
                }
            }
        }
        return k == 1;
    }
}
