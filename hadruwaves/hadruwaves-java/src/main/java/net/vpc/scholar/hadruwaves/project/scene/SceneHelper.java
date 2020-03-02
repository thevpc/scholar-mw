package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DPolygon;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.Point3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Calc3dFactory;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DCurve2;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DParallelipiped;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DPrism;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;

import java.util.ArrayList;
import java.util.List;

public class SceneHelper {
    public static Element3D createCurve(Point3D... a) {
        return new Element3DCurve2(a);
    }

    public static Element3D createPolygon(Geometry geometry, double z) {

        List<Point3D> a = new ArrayList<>();
        for (Point point : geometry.toPolygon().getPoints()) {
            a.add(new Point3D(point.x, point.y, z));
        }
        return Calc3dFactory.createPolygon(a.toArray(new Point3D[0]));
    }

    public static Element3DTemplate createPolygonTemplate(Geometry geometry, double z) {

        List<Point3DTemplate> a = new ArrayList<>();
        for (Point point : geometry.toPolygon().getPoints()) {
            a.add(new Point3DTemplate(point.x, point.y, z));
        }
        return new Element3DPolygonTemplate(a.toArray(new Point3DTemplate[0]));
    }

    public static Element3D createPolygon(Point3D... a) {
        return new Element3DPolygon(a);
    }

    public static Element3DParallelipiped createBrick(Point3D a, Point3D b, Point3D c, Point3D d) {
        return new Element3DParallelipiped(a, b, c, d, true);
    }

    public static Element3DParallelipiped createParallelipiped(Point3D a, Point3D b, Point3D c, Point3D d) {
        return new Element3DParallelipiped(a, b, c, d, false);
    }

    public static Element3DPrism createPrism(int sides, double radius, double height, Point3D center) {
        Element3DPrism p = new Element3DPrism(sides, radius, height);
        if (center != null) {
            if (center.getX() != 0 || center.getY() != 0 || center.getZ() != 0) {
                p.setTranslation(new Vector3D(center.getX(), center.getY(), center.getZ()));
            }
        }
        return p;
    }


    public static Element3DParallelipipedTemplate createBrickTemplate(DomainTemplate domain) {
        return new Element3DParallelipipedTemplate(
                new Point3DTemplate(domain.xmin().get(), domain.ymin().get(), domain.zmin().get()),
                new Point3DTemplate(domain.xmax().get(), domain.ymin().get(), domain.zmin().get()),
                new Point3DTemplate(domain.xmin().get(), domain.ymax().get(), domain.zmin().get()),
                new Point3DTemplate(domain.xmin().get(), domain.ymin().get(), domain.zmax().get())
        );
    }

    public static Element3DParallelipiped createBrick(Domain domain) {
        return Calc3dFactory.createBrick(
                new Point3D(domain.xmin(), domain.ymin(), domain.zmin()),
                new Point3D(domain.xmax(), domain.ymin(), domain.zmin()),
                new Point3D(domain.xmin(), domain.ymax(), domain.zmin()),
                new Point3D(domain.xmin(), domain.ymin(), domain.zmax())
        );
    }

    public static double resolveInfinityValue(Domain domain) {
        if (domain == null) {
            return 1;
        }
        MinMax m = new MinMax();
        m.setInfinite(false);
        m.registerValue(domain.xmin());
        m.registerValue(domain.xmax());
        m.registerValue(domain.ymin());
        m.registerValue(domain.ymax());
        m.registerValue(domain.zmin());
        m.registerValue(domain.zmax());
        if (m.isUnset()) {
            m.registerValue(-1);
            m.registerValue(1);
        }
        return m.getMax()-m.getMin();
    }

    public static Domain ensureBounds(Domain domain) {
        if (domain == null) {
            domain = Maths.domain(-1, 1, -1, 1, -1, 1);
        }
        MinMax m = new MinMax();
        m.setInfinite(false);
        m.registerValue(domain.xmin());
        m.registerValue(domain.xmax());
        m.registerValue(domain.ymin());
        m.registerValue(domain.ymax());
        m.registerValue(domain.zmin());
        m.registerValue(domain.zmax());
        if (m.isUnset()) {
            m.registerValue(-1);
            m.registerValue(1);
        }
        domain = domain.ensureFiniteBounds(Maths.domain(m.getMin(), m.getMax(), m.getMin(), m.getMax(), m.getMin(), m.getMax()));
        return domain;
    }
}
