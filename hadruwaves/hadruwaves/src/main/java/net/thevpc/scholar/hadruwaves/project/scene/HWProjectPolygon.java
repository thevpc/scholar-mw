package net.thevpc.scholar.hadruwaves.project.scene;

import java.util.ArrayList;
import java.util.List;

import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HPolygon;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;

public class HWProjectPolygon extends AbstractHWProjectComponentMaterial implements HWProjectElementMaterialSurface{

    public HWProjectPolygon(String name, HWMaterialTemplate material, Element3DPolygonTemplate geometry) {
        super(name, material, geometry);
    }

    public HPolygon eval(HWConfigurationRun configuration) {
        List<HPoint> points = new ArrayList<HPoint>();
        Element3DPolygonTemplate t = (Element3DPolygonTemplate) geometry().get();
        for (Point3DTemplate point : t.getPoints()) {
            points.add(point.evalPoint(configuration));
        }

        return GeometryFactory.createPolygon(points);
    }

}
