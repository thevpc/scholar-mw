package net.vpc.scholar.hadruwaves.project.scene.elem;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DPolygon;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Point3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DParallelipiped;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.scene.Point3DTemplate;

import java.util.ArrayList;
import java.util.List;

public class Element3DPolygonTemplate extends AbstractElement3DTemplate{
    private List<Point3DTemplate> all= new ArrayList<>();

    public Element3DPolygonTemplate(Point3DTemplate... a) {
        for (Point3DTemplate point3DTemplate : a) {
            all.add(point3DTemplate);
        }
    }

    @Override
    public Element3D toElements3D(HWProjectEnv env) {
        List<Point3D> pts= new ArrayList<>();
        for (Point3DTemplate a : all) {
            pts.add(a.eval(env));
        }
        Element3DPolygon e = new Element3DPolygon(pts.toArray(new Point3D[0]));
        return e;
    }
}
