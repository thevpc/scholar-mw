package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.MathUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Element3DRenderPrefs;

import java.awt.*;


// superclass for drawable objects
public abstract class PrimitiveElement3D implements Comparable<PrimitiveElement3D> {

    public static final int DRAWMODE_SOLID = 0;
    public static final int DRAWMODE_DASHED = 1;

    public static final int FILLMODE_SOLID = 0;
    public static final int FILLMODE_GRAY = 1;
    public static final int FILLMODE_NONE = 2;
    protected final Element3DRenderPrefs renderPrefs = new Element3DRenderPrefs();
    /**
     * represents z depth of centre of primitiveElement3D, which is needed for z sort
     */
    public double depth;
    /**
     * represents centre of primitiveElement3D (must be defined by its subclasses in their constructors)
     */
    protected Vector3D centre = new Vector3D();
    /**
     * flag to toggle renderability of primitiveElement3D, it should be set to false if
     * this object does not render (eg when it is culled)
     */
    private boolean renderable = true;

    /** true if lines drawn for this primitiveElement3D are dashed*/
//    private boolean isdashed;

//	protected Color lineColor,fillColor,backColor;

//	protected int curveWidth=1;
    /**
     * true if this primitiveElement3D is to be filled
     */
    private boolean isfilled = true;

    /**
     * Compare method for z sort painter algorithm
     */

    public PrimitiveElement3D() {
        prefs().setLineColor(Color.black);
        prefs().setFillColor(Color.white);
        prefs().setBackColor(Color.gray);
    }

    public Element3DRenderPrefs prefs() {
        return renderPrefs;
    }

    public int compareTo(PrimitiveElement3D e) {
        if ((e == null) || (this == null)) return 0;
        if (!MathUtils.isValidNumber(depth) || !MathUtils.isValidNumber(e.depth)) return 0;
        if (depth > e.depth) return -1;
        else if (depth < e.depth) return 1;
        else return 0;
    }

    /**
     * returns centre of primitiveElement3D, needed to calculate depth and normal vector of plane
     */
    public Vector3D getCentre() {
        return centre;
    }

    ;

    public abstract void transform(AffineTransform3D T);

    public boolean isRenderable() {
        return renderable;
    }

    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }

    /**
     * Returns if fill pattern of primitiveElement3D is solid/hollow
     */
    public boolean isFilled() {
        return isfilled;
    }

    /**
     * Sets the primitiveElement3D fill pattern to be hollow/solid
     */
    public void setFilled(boolean isfilled) {
        this.isfilled = isfilled;
    }


    /**
     * updates rendering properties of this primitiveElement3D to primitiveElement3D in parameter
     *
     * @param e
     */
    public void updateElement(PrimitiveElement3D e) {
    	this.prefs().copyFrom(e.prefs());
    }
}

