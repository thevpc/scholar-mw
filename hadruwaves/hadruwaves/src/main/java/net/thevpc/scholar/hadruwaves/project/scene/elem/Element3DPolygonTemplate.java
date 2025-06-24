package net.thevpc.scholar.hadruwaves.project.scene.elem;

import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3DPolygon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruwaves.project.scene.Point3DTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class Element3DPolygonTemplate extends AbstractElement3DTemplate {

    private List<Point3DTemplate> all = new ArrayList<>();

    public Element3DPolygonTemplate(Polygon polygon) {
        for (Point point : polygon.getPoints()) {
            all.add(new Point3DTemplate(point));
        }
    }

    public Element3DPolygonTemplate(Point3DTemplate... a) {
        for (Point3DTemplate point3DTemplate : a) {
            all.add(point3DTemplate);
        }
    }

    public Point3DTemplate[] getPoints() {
        return all.toArray(new Point3DTemplate[0]);
    }

    @Override
    public Element3D toElements3D(HWConfigurationRun configuration) {
        List<Point3D> pts = new ArrayList<>();
        for (Point3DTemplate a : all) {
            pts.add(a.eval(configuration));
        }
        Element3DPolygon e = new Element3DPolygon(pts.toArray(new Point3D[0]));
        return e;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofArrayBuilder(getClass().getSimpleName())
                .addAll(
                        all.stream().map(x -> x.toTsonElement(context))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
