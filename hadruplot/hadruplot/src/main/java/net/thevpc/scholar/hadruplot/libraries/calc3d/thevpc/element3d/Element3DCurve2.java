package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.utils.ColorUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Calc3dLibraryUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;

public class Element3DCurve2 extends Element3Db {

    private static Logger LOG = Logger.getLogger(Element3DCurve2.class.getName());
    private Point3D[] points;

    /**
     * No of segments of the curve
     */
//	private int numSegments=80;
    public Element3DCurve2(Point3D[] points) {
        prefs().setSplittable(false);
        this.points = points;
    }

    public Element3DCurve2 copy() {
        Element3DCurve2 e = new Element3DCurve2(points);
        e.copyFrom(this);
        return e;
    }

    public Point3D[] getPoints() {
        return points;
    }

    @Override
    public String getDefinition() {
        return "Custom Curve";
    }

    @Override
    public PrimitiveElement3D createElement() {
        primitiveElement3D = CreateCurve(null);
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        return primitiveElement3D;
    }

    @Override
    public PrimitiveElement3D createElement(Clip clip) {
        primitiveElement3D = CreateCurve(clip);
        if (null == primitiveElement3D) {
            return null;
        }
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        return primitiveElement3D;
    }

    public PrimitiveElement3DCollection CreateCurve(Clip clip) {

        Vector3D[] points = Calc3dLibraryUtils.inverseMap(this, Calc3dLibraryUtils.toVectors(this.points));
        if (points.length < 2) {
            return null;
        }
        PrimitiveElement3DCollection curve3D = new PrimitiveElement3DCollection();

        for (int i = 1; i < points.length - 1; i++) {
            Vector3D p1 = points[i];
            Vector3D p2 = points[i + 1];
            if (Calc3dLibraryUtils.isFinite(p1) && Calc3dLibraryUtils.isFinite(p2)) {
                PrimitiveElement3D ec = applyTransform(clip, (i % 10 == 0) ? new PrimitiveElement3DArrow(p1, p2) : new PrimitiveElement3DCurve(p1, p2));
                if (null != ec) {
                    ec.prefs().setFillColor(ColorUtils.blendColors(prefs().getFillColor(), prefs().getBackColor(), ((double) i) / points.length));
                    curve3D.addElement(ec);
                }
            }
        }
        primitiveElement3D = curve3D;
        return curve3D;
    }

}
