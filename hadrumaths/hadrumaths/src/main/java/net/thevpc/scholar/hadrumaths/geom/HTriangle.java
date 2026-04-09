package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.scholar.hadrumaths.util.OIndex;

import java.io.Serializable;
import java.util.List;

public interface HTriangle extends HGeometry, Serializable, HPolygonBuilder, Cloneable {
    OIndex indexOfPoint(HPoint p);

    HPoint getBarycenter();

    double getCircleRadius();

    HPoint getCenter();

    boolean isNeighborhood(HTriangle t);

    boolean intersection(HTriangle t);

    List<HPoint> getPoints();

    double getHeight(int index);

    double area();

    @Override
    HGeometry clone();

    HPoint p1();
    HPoint p2();
    HPoint p3();

    double longestEdge();

    double shortestEdge();

    HPoint getPoint(OIndex index);
}
