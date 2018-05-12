package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class EnhancedMeshPolygons implements Serializable, Dumpable {
    private static final long serialVersionUID = 1L;
    private double surface;
    private Polygon[] polygon;

    public EnhancedMeshPolygons(Polygon[] p, double a) {
        polygon = p;
        surface = a;
    }

    public String dump() {
        Dumper h=new Dumper(getClass().getSimpleName());
        h.add("surface",surface);
        h.add("polygons",polygon);
        return h.toString();
    }

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
        if (k == 1) {
            return true;
        } else {
            return false;
        }
    }
}
