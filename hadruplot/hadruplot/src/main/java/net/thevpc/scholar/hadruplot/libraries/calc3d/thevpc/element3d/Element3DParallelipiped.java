package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Clip;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DCollection;
import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Epsilon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Element3DRenderPrefs;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;

import java.awt.*;
import java.util.Arrays;

/**
 * Class for 3D Premitives
 *
 * @author mahesh
 */
public class Element3DParallelipiped extends Element3Db {

    /**
     *
     */
    private static final long serialVersionUID = 3060747132508766091L;
    private static Logger LOG = Logger.getLogger(Element3DParallelipiped.class.getName());

    private int colorMode = 1;
    private boolean closed = true;

    // "No. of sides of Base", "CircumRadius", "Height"
    private Point3D a;
    private Point3D b;
    private Point3D c;
    private Point3D d;

    private Element3DRenderPrefs[] facePrefs = new Element3DRenderPrefs[6];

    /**
     * Creates premetive with default parameters
     */
    public Element3DParallelipiped() {
        this(new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 0, 1), false);
    }

    public Element3DParallelipiped(Point3D a, Point3D b, Point3D c, Point3D d, boolean rect) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        if (rect) {
            Vector3D v2 = new Vector3D(a, b);
            Vector3D v3 = new Vector3D(a, c);
            Vector3D v4 = new Vector3D(a, d);
            Vector3D v3c = v3.Get_Perpendicular_Component(v2);
            Vector3D v4d = v4.Get_Perpendicular_Component(v2);
            v4d = v4d.Get_Perpendicular_Component(v3);
            c = a.add(v3c);
            d = a.add(v4d);
        }
        setName(getObjectName());
    }
//    private double[] parameters = new double[3];
//    private String[] paramNames;

    public Element3DRenderPrefs[] getFacePrefs() {
        return facePrefs;
    }

    public Element3DParallelipiped setFacePrefs(int index, Element3DRenderPrefs f) {
        this.facePrefs[index] = f;
        return this;
    }

    public boolean isBrick() {
        Vector3D v1 = new Vector3D(a, b);
        Vector3D v2 = new Vector3D(a, c);
        Vector3D v3 = new Vector3D(a, d);
        if (v1.isPerpendicular_to(v2)
                && v1.isPerpendicular_to(v3)
                && v2.isPerpendicular_to(v3)) {
            return true;
        }
        return false;
    }

    public boolean isCube() {
        if (!isBrick()) {
            return false;
        }
        double l2 = new Vector3D(a, b).getLength();
        double l3 = new Vector3D(a, c).getLength();
        double l4 = new Vector3D(a, d).getLength();
        return Epsilon.isEq(l2, l3) && Epsilon.isEq(l3, l4);
    }

    public String getObjectName() {
        if (isCube()) {
            return "Cube";
        }
        if (isBrick()) {
            return "Brick";
        }
        return "Parallelipiped";
    }

    /**
     * Creates Premitive Object based on its code and parameters
     *
     * @param clip
     * @return
     */
    private PrimitiveElement3DCollection createSurface(Clip clip) {
        PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();
        float n = 4;
        Point3D abd = a.add(new Point3D(new Vector3D(a, b).add(new Vector3D(a, d))));
        Point3D abc = a.add(new Point3D(new Vector3D(a, b).add(new Vector3D(a, c))));
        Point3D acd = a.add(new Point3D(new Vector3D(a, c).add(new Vector3D(a, d))));
        int i = 0;
        surface3D.addElement(createFace(i++, clip, a, b, d));
        surface3D.addElement(createFace(i++, clip, b, abc, abd));
        surface3D.addElement(createFace(i++, clip, c, abc, acd));
        surface3D.addElement(createFace(i++, clip, a, c, d));
        surface3D.addElement(createFace(i++, clip, a, b, c));
        surface3D.addElement(createFace(i++, clip, d, abd, acd));

        primitiveElement3D = surface3D;
        return (surface3D.primitiveElement3DS.size() > 0) ? surface3D : null;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    @Override
    public String getDefinition() {
//        String str = getObjectName();
        return Arrays.asList(a, b, c, d).toString();
//        //"No. of sides of Base", "CircumRadius", "Height"
//        str = str + "<br>" + "No. of sides of Base" + ": " + sides;
//        str = str + "<br>" + "CircumRadius" + ": " + radius;
//        str = str + "<br>" + "Height" + ": " + height;
//        return str;
    }

    @Override
    public PrimitiveElement3D createElement() {
        primitiveElement3D = createSurface(null);
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        isCreated = true;
        return primitiveElement3D;
    }

    @Override
    public PrimitiveElement3D createElement(Clip clip) {
        primitiveElement3D = createSurface(clip);
        if (null == primitiveElement3D) {
            return null;
        }
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        isCreated = true;
        return primitiveElement3D;
    }

    public Element3DParallelipiped copy() {
        Element3DParallelipiped e = new Element3DParallelipiped(a, b, c, d, false);
        e.prefs().copyFrom(prefs());
        for (int i = 0; i < 6; i++) {
            e.facePrefs[i] = facePrefs[i] == null ? null : facePrefs[i].copy();
        }
        return e;
    }

    public Point3D[] getPoints() {
        Point3D abd = a.add(new Point3D(new Vector3D(a, b).add(new Vector3D(a, d))));
        Point3D abc = a.add(new Point3D(new Vector3D(a, b).add(new Vector3D(a, c))));
        Point3D acd = a.add(new Point3D(new Vector3D(a, c).add(new Vector3D(a, d))));
        return new Point3D[]{a, b, c, d, abd, abc, acd};
    }

    protected PrimitiveElement3D createFace(int faceIndex, Clip clip, Point3D a, Point3D b, Point3D c) {
        if (facePrefs[faceIndex] == null || !facePrefs[faceIndex].isVisible()) {
            return null;
        }
        int n = 6;
        PrimitiveElement3D facePoly = createPoly(clip, abacSurface(a, b, c));
        float colA = faceIndex * 1.0f / n;
        float colB = 0.0f;
        float colC = faceIndex * 1.0f / n;
        if (facePoly == null) {
            return null;
        }
        Element3DRenderPrefs p = facePrefs[faceIndex];
        if (p == null) {
            p = prefs();
        }
        int i = p.getFillmode();
        facePoly.setFilled(true);
        switch (i) {
            case 0:
                facePoly.prefs().setFillColor(p.getFillColor());
                facePoly.prefs().setBackColor(p.getBackColor());
                break;
            case 1:
                facePoly.setFilled(false);
                break;
            case 2:
                facePoly.prefs().setFillColor(new Color(colA, colB, (colA + colB) / 2.0f));
                facePoly.prefs().setBackColor(Color.gray);
                break;
        }
        facePoly.prefs().setLineColor(p.getLineColor());
        facePoly.prefs().setCurveWidth(p.getCurveWidth());
        facePoly.prefs().setDashed(p.isDashed());
        return facePoly;
    }
}
