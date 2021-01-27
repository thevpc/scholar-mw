package net.thevpc.scholar.hadruwaves.project.scene;

import java.util.ArrayList;
import java.util.List;

import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;

public class HWProjectPolygon extends AbstractHWProjectComponentMaterial implements HWProjectElementMaterialSurface{

    public HWProjectPolygon(String name, HWMaterialTemplate material, Element3DPolygonTemplate geometry) {
        super(name, material, geometry);
    }

    public Polygon eval(HWConfigurationRun configuration) {
        List<Point> points = new ArrayList<Point>();
        Element3DPolygonTemplate t = (Element3DPolygonTemplate) geometry().get();
        for (Point3DTemplate point : t.getPoints()) {
            points.add(point.evalPoint(configuration));
        }

        return GeometryFactory.createPolygon(points);
    }

}
