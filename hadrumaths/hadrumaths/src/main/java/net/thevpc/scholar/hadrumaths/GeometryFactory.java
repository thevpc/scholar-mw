package net.thevpc.scholar.hadrumaths;


import net.thevpc.scholar.hadrumaths.geom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 1/23/14.
 */
public class GeometryFactory extends AbstractFactory {
    public static HPoint point(double x) {
        return HPoint.create(x);
    }

    public static HPoint point(double x, double y) {
        return HPoint.create(x, y);
    }

    public static HPoint point(double x, double y, double z) {
        return HPoint.create(x, y, z);
    }

    public static HGeometryList createPolygonList(HGeometry... list) {
        List<HGeometry> list1 = new ArrayList<HGeometry>();
        if (list != null) {
            for (HGeometry x : list) {
                if (x != null) {
                    list1.add(x);
                }
            }
        }

        if (list1.isEmpty()) {
            return new DefaultHGeometryList();
        }
        if (list1.size() == 1) {
            if (list1.get(0) instanceof HGeometryList) {
                return (HGeometryList) list1.get(0);
            }
        }
        DefaultHGeometryList list2 = new DefaultHGeometryList();
        for (HGeometry x : list1) {
            list2.add(x);
        }
        return list2;
    }

    public static HGeometryList createPolygonList() {
        return new DefaultHGeometryList();
    }

    public static HPolygon createPolygon(Domain domainXY) {
        return new DefaultHPolygon(domainXY);
    }

    public static HPolygon createPolygon(HPoint... points) {
        return new DefaultHPolygon(points);
    }

    public static HPolygon createPolygon(List<HPoint> points) {
        return new DefaultHPolygon(points);
    }


    public static RegularHPolygon createRegularPolygon() {
        return new RegularHPolygon();
    }

    public static EllipticHPolygon createEllipticPolygon() {
        return new EllipticHPolygon();
    }
}
