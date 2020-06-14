package net.vpc.scholar.hadruplot.libraries.calc3d.vpc.element3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DPoly;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Point3D;

public abstract class Element3Db extends Element3D {
    protected Vector3D[] abacSurface(Point3D a, Point3D b, Point3D c) {
//        Vector3D ab = new Vector3D(a, b);
        Vector3D ac = new Vector3D(a, c);
        Point3D d = b.add(ac);
//        Vector3D bd = new Vector3D(b, d);
//        Vector3D dc = new Vector3D(d, c);
//        Vector3D ca = new Vector3D(c, a);
        return new Vector3D[]{a.toVector(),b.toVector(), d.toVector(), c.toVector()};
    }

    public void copyFrom(Element3Db other){
        this.setName(other.getName());
        this.setSceneManager(other.getSceneManager());
        this.prefs().setLineColor(other.prefs().getLineColor());
        this.prefs().setFillColor(other.prefs().getFillColor());
        this.prefs().setBackColor(other.prefs().getBackColor());
        this.prefs().setVisible(other.prefs().isVisible());
        this.prefs().setCurveWidth(other.prefs().getCurveWidth());
        this.prefs().setDashed(other.prefs().isDashed());
        this.setRotation(other.getRotation()==null?null:other.getRotation().copy());
        this.setTransform(other.getTransform()==null?null:other.getTransform().copy());
        this.prefs().setSplittable(other.prefs().isSplittable());
        this.prefs().setFillmode(other.prefs().getFillmode());
    }

    protected PrimitiveElement3D applyTransform(Clip clip, PrimitiveElement3D v) {
        if (v != null) {
            if (T != null) {
                v.transform(T);
            }
            if (clip != null) {
                return clip.getClippedElement(v);
            }
            v.prefs().setCurveWidth(prefs().getCurveWidth());
            v.prefs().setFillColor(prefs().getFillColor());
            v.prefs().setLineColor(prefs().getLineColor());
            v.prefs().setSplittable(prefs().isSplittable());
            v.prefs().setDashed(prefs().isDashed());
        }
        return v;
    }

    protected PrimitiveElement3D createPoly(Clip clip, Vector3D[] v) {
        PrimitiveElement3DPoly elementPoly = new PrimitiveElement3DPoly(getSceneManager().getSettings().inverseMapAll(v));
        return applyTransform(clip, elementPoly);
    }


}
