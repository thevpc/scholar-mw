package net.vpc.scholar.hadrumaths;


import java.util.ArrayList;
import java.util.List;

import net.vpc.scholar.hadrumaths.geom.*;
import net.vpc.scholar.hadrumaths.geom.GeometryList;

/**
 * Created by vpc on 1/23/14.
 */
public class GeometryFactory extends AbstractFactory{
    public static Point point(double x){
        return Point.create(x);
    }
    public static Point point(double x,double y){
        return Point.create(x,y);
    }
    public static Point point(double x,double y,double z){
        return Point.create(x,y,z);
    }

    public static GeometryList createPolygonList(Geometry... list) {
        List<Geometry> list1 = new ArrayList<Geometry>();
        if (list != null) {
            for (Geometry x : list) {
                if (x != null) {
                    list1.add(x);
                }
            }
        }

        if (list1.isEmpty()) {
            return new DefaultGeometryList();
        }
        if (list1.size() == 1) {
            if (list1.get(0) instanceof GeometryList) {
                return (GeometryList) list1.get(0);
            }
        }
        DefaultGeometryList list2 = new DefaultGeometryList();
        for (Geometry x : list1) {
            list2.add(x);
        }
        return list2;
    }

    public static GeometryList createPolygonList() {
        return new DefaultGeometryList();
    }

    public static Polygon createPolygon(Domain domainXY) {
        return new Polygon(domainXY);
    }

    public static Polygon createPolygon(Point... points) {
        return new Polygon(points);
    }

    public static Polygon createPolygon(List<Point> points) {
        return new Polygon(points);
    }


    public static RegularPolygon createRegularPolygon() {
        return new RegularPolygon();
    }

    public static EllipticPolygon createEllipticPolygon() {
        return new EllipticPolygon();
    }
}
