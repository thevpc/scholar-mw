package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DPoly;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;

import java.awt.*;

/**
 * Class for 3D Premitives
 *
 * @author mahesh
 */
public class Element3DPrism extends Element3Db {

    /**
     *
     */
    private static final long serialVersionUID = 3060747132508766091L;
    private static Logger LOG = Logger.getLogger(Element3DPrism.class.getName());

    private int colorMode = 1;
    private boolean closed = true;

    // "No. of sides of Base", "CircumRadius", "Height"
	private int sides=6;
	private double radius=3;
	private double height=1;


//    private double[] parameters = new double[3];
//    private String[] paramNames;

    /**
     * Creates premetive with default parameters
     */
    public Element3DPrism() {
    	this(6,3,1);
    }

    public Element3DPrism(int sides, double radius, double height) {
    	this.sides=sides;
    	this.radius=radius;
    	this.height=height;
    	setName(getObjectName());
    }





    public String getObjectName() {
		switch (sides){
			case 0:{
				return ("Empty Prism 0");
			}
			case 1:{
				return ("Empty Prism 1");
			}
			case 2:{
				return ("Empty Prism 2");
			}
			case 3:{
				return ("Triangular Brick");
			}
			case 4:{
				return ("Rectangular Brick");
			}
		}
		return ("Prism");
    }


    /**
     * Creates Premitive Object based on its code and parameters
     *
     * @param clip
     * @return
     */
    private PrimitiveElement3DCollection createSurface(Clip clip) {
        PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();
        Vector3D centre = new Vector3D(0, 0, 0);
        Vector3D v1, v2;

		int n = Math.abs(this.sides); //no of edges
		double radius = getSceneManager().getSettings().inverseMapX(this.radius); //radius
		double height = getSceneManager().getSettings().inverseMapZ(this.height); //height
		Vector3D vertex = new Vector3D(0, 0, height);
		for (int i = 0; i < n; i++) {
			Vector3D v3, v4;
			double theta1 = 2 * i * Math.PI / n;
			v1 = new Vector3D(radius * Math.cos(theta1), radius * Math.sin(theta1), 0);
			v3 = new Vector3D(radius * Math.cos(theta1), radius * Math.sin(theta1), height);
			double theta2 = 2 * (i + 1) * Math.PI / n;
			v2 = new Vector3D(radius * Math.cos(theta2), radius * Math.sin(theta2), 0);
			v4 = new Vector3D(radius * Math.cos(theta2), radius * Math.sin(theta2), height);

			PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
			ep.addPoint(v1);
			ep.addPoint(v2);
			ep.addPoint(v4);
			ep.addPoint(v3);
			setPolygonColor(i * 1.0f / n, 0.0f, i * 1.0f / n, ep);
			if (T != null) ep.transform(T);
			PrimitiveElement3D eTemp = clip.getClippedElement(ep);
			if (null != eTemp) surface3D.addElement(eTemp);

			if (closed) {
				PrimitiveElement3DPoly ep1 = new PrimitiveElement3DPoly();
				ep1.addPoint(vertex.clone());
				ep1.addPoint(v3);
				ep1.addPoint(v4);
				setPolygonColor((i + 4.0f) * 1.0f / (n + 4), 0.0f, (i + 4.0f) * 1.0f / (n + 4), ep1);
				if (T != null) ep1.transform(T);
				eTemp = clip.getClippedElement(ep1);
				if (null != eTemp) surface3D.addElement(eTemp);

				PrimitiveElement3DPoly ep2 = new PrimitiveElement3DPoly();
				ep2.addPoint(centre.clone());
				ep2.addPoint(v1);
				ep2.addPoint(v2);
				setPolygonColor((i + 2) * 1.0f / (n + 3), 0.0f, (i + 2) * 1.0f / (n + 3), ep2);
				if (T != null) ep2.transform(T);
				eTemp = clip.getClippedElement(ep2);
				if (null != eTemp) surface3D.addElement(eTemp);
			}
		}
        primitiveElement3D = surface3D;
        return (surface3D.primitiveElement3DS.size() > 0) ? surface3D : null;
    }

	private double getParameter0() {
		return sides;
	}

	public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    @Override
    public String getDefinition() {
        String str = getObjectName();
        //"No. of sides of Base", "CircumRadius", "Height"
		str = str + "<br>" + "No. of sides of Base" + ": " + sides;
		str = str + "<br>" + "CircumRadius" + ": " + radius;
		str = str + "<br>" + "Height" + ": " + height;
        return str;
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
        if (null == primitiveElement3D) return null;
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        isCreated = true;
        return primitiveElement3D;
    }

    /**
     * Sets polygon color according to fillmode
     */
    private void setPolygonColor(float a, float b, float c, PrimitiveElement3DPoly poly) {
        int i = prefs().getFillmode();
        poly.setFilled(true);
        switch (i) {
            case 0:
                poly.prefs().setFillColor(prefs().getFillColor());
                poly.prefs().setBackColor(prefs().getBackColor());
                break;
            case 1:
                poly.setFilled(false);
                break;
            case 2:
                poly.prefs().setFillColor(new Color(a, b, (a + b) / 2.0f));
                poly.prefs().setBackColor(Color.gray);
                break;
        }
        poly.prefs().setLineColor(prefs().getLineColor());
        poly.prefs().setCurveWidth(prefs().getCurveWidth());
        poly.prefs().setDashed(prefs().isDashed());
    }
}

