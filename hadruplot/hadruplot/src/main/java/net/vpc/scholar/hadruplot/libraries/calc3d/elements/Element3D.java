package net.vpc.scholar.hadruplot.libraries.calc3d.elements;

import net.vpc.scholar.hadruplot.libraries.calc3d.core.SceneManager;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Angles3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Element3DRenderPrefs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Class for Elements used by scene manager
 *
 * @author mahesh
 */
public abstract class Element3D implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -2177499520844129649L;
    /**
     * PrimitiveElement3D associated with this primitiveElement3D recognised by 3D engine
     */
    protected transient PrimitiveElement3D primitiveElement3D;
    protected Element3DRenderPrefs renderPrefs=new Element3DRenderPrefs();
    protected transient AffineTransform3D T;
    protected transient boolean isCreated = false;
    protected SceneManager sceneManager;
    protected Map<String, Object> userObjects;
    /**
     * name of Object
     */
    private String name = "";
    /**
     * Transform parameters
     */
    private Vector3D translationVector = new Vector3D();
    private Angles3D rotationAngle = new Angles3D();

    public Element3DRenderPrefs prefs() {
        return renderPrefs;
    }


    /**
     * returns name asssociated with this Element3D
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name for this Ellement3D
     */
    public void setName(String name) {
        this.name = name;
    }

    public PrimitiveElement3D getPrimitiveElement3D() {
        if (primitiveElement3D == null) return null;
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        return primitiveElement3D;
    }

    /**
     * returns equation or expression associated with this element3D in HTML format
     */
    public abstract String getDefinition();

    /**
     * return current Transform of primitiveElement3D
     */
    public AffineTransform3D getTransform() {
        return T;
    }

    /**
     * Transform primitiveElement3D with input Transformation
     */
    public void setTransform(AffineTransform3D T) {
        this.T = T;
    }


    /**
     * returns primitiveElement3D recognised by 3D engine, Without cliiping, returns
     * null if there is no primitiveElement3D
     *
     * @return
     */
    public abstract PrimitiveElement3D createElement();

    /**
     * returns List of primitiveElement3DS recognised by 3D engine, Elements are clipped
     * against the box defined by clip Object
     *
     * @param clip Clip Object to clip the primitiveElement3DS against bounding box defined
     *             by it
     * @return Element3D which define this primitiveElement3D, if
     * there is no primitiveElement3D in clipped region it returns <b> null </b>
     */
    public abstract PrimitiveElement3D createElement(Clip clip);


    /**
     * Updates redndering properties of this to primitiveElement3D in parameter
     *
     * @param e
     */
    public void updateElement(Element3D e) {
        prefs().copyFrom(e.prefs());
    }

    /**
     * @return the translationVector
     */
    public Vector3D getTranslation() {
        return translationVector;
    }

    /**
     * @param translationVector the translationVector to set
     */
    public void setTranslation(Vector3D translationVector) {
        this.translationVector = translationVector;
    }

    /**
     * @return the rotationAngle
     */
    public Angles3D getRotation() {
        return rotationAngle;
    }

    /**
     * @param rotationAngle the rotationAngle to set
     */
    public void setRotation(Angles3D rotationAngle) {
        this.rotationAngle = rotationAngle;
    }


    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public Element3D setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        return this;
    }

    public Object getUserObject(String s) {
        if (userObjects != null) {
            return userObjects.get(s);
        }
        return null;
    }

    public void putUserObject(String s, Object v) {
        if (v == null) {
            if (userObjects != null) {
                userObjects.remove(s);
            }
        } else {
            if (userObjects == null) {
                userObjects = new HashMap<>();
            }
            userObjects.put(s, v);
        }
    }

    public Element3D copy() {
        return this;
    }
}