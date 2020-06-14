package net.vpc.scholar.hadruplot.libraries.calc3d.elements;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DPoint;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.awt.*;

/**
 * Class for Elements3D representing point in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DPoint extends Element3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7718288181668445927L;
	private Vector3D point;
	private int radius=3;
	private String text;
	public Element3DPoint(double x, double y, double z) {
		primitiveElement3D = new PrimitiveElement3DPoint(x, y, z);
		point=new Vector3D(x,y,z);
	}

	public Element3DPoint(Vector3D point) {
		primitiveElement3D = new PrimitiveElement3DPoint(point);
		this.point=new Vector3D(point);
	}
	
	public Element3DPoint(Vector3D point, String name,Color color) {
		primitiveElement3D = new PrimitiveElement3DPoint(point,name,color);
		this.point=new Vector3D(point);
		prefs().setFillColor(color);
		prefs().setLineColor(color);
		super.setName(name);
	}
	
	@Override
	public String getDefinition() {
		return "(" +point.getX() + ","
				+ point.getY() + "," +point.getZ()
				+ ")";
	}
    
	@Override
	public void setName(String name){
		super.setName(name);
		((PrimitiveElement3DPoint) primitiveElement3D).setText(name);
	}

	public Vector3D getPoint() {
		// TODO Auto-generated method stub
		return point;
	}
	
	public void setPoint(Vector3D point) {
		this.point=new Vector3D(point);
		primitiveElement3D =new PrimitiveElement3DPoint(point);
	}

	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		if (radius>=1) this.radius=radius;
	}

	@Override
	public PrimitiveElement3D createElement() {
		Vector3D pt=new Vector3D(getSceneManager().getSettings().inverseMapX(point.getX()),getSceneManager().getSettings().inverseMapY(point.getY()),getSceneManager().getSettings().inverseMapZ(point.getZ()));
		PrimitiveElement3DPoint e=new PrimitiveElement3DPoint(pt);
		e.setText(getText());
		primitiveElement3D =e;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
	    primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
	    primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
	    if (radius>=1) ((PrimitiveElement3DPoint) primitiveElement3D).setRadius((int)radius);
	    primitiveElement3D.prefs().setDashed(prefs().isDashed());
	    isCreated=true;
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		Vector3D pt=new Vector3D(getSceneManager().getSettings().inverseMapX(point.getX()),getSceneManager().getSettings().inverseMapY(point.getY()),getSceneManager().getSettings().inverseMapZ(point.getZ()));
		PrimitiveElement3DPoint e=new PrimitiveElement3DPoint(pt);
		e.setText(getText());
		if(T!=null)e.transform(T);
		primitiveElement3D =clip.getClippedElement(e);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
	    primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
	    primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
	    primitiveElement3D.prefs().setDashed(prefs().isDashed());
	    if (radius>=1) ((PrimitiveElement3DPoint) primitiveElement3D).setRadius((int)radius);
	    isCreated=true;
		return primitiveElement3D;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}

/**
 * Class for Elements3D representing Line in 3D space
 * 
 * @author mahesh
 * 
 */