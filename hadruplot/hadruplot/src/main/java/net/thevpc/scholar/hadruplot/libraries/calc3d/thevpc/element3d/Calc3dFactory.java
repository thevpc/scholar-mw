package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3DPolygon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;

public class Calc3dFactory {

    public static Element3Db createCurve(Point3D... a) {
        return new Element3DCurve2(a);
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
}
