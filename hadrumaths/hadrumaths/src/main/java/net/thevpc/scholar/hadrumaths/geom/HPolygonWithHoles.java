package net.thevpc.scholar.hadrumaths.geom;

import java.util.List;

public interface HPolygonWithHoles extends HGeometry {
    boolean is4Edges();
    List<HPoint> getPoints();

    HPolygon getExteriorRing();

    List<HPolygon> getHoles();
}
