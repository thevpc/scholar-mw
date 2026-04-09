package net.thevpc.scholar.hadrumaths.geom;

import java.util.List;

public interface HPolygon extends HGeometry {
    List<HPoint> getPoints();

    boolean is4Edges();
}
