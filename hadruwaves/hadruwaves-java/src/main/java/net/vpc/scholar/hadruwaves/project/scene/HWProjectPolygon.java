package net.vpc.scholar.hadruwaves.project.scene;

import java.util.ArrayList;
import java.util.List;

import net.vpc.scholar.hadrumaths.GeometryFactory;
import net.vpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;

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
