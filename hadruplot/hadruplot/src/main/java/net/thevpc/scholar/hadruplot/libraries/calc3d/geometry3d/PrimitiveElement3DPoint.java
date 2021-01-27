package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.awt.Color;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**class to represent Point in 3D*/
public class PrimitiveElement3DPoint extends PrimitiveElement3D {
	/** Alignment for text 0=centre 1=left 2=right 3=top 4=bottom*/
	private int alignment;
	/** radius of point to be drawn*/
	private double radius;
	/** text representing name of Point (if any), use "" for no text*/
	private String text="";
	
	public PrimitiveElement3DPoint(Vector3D pos) {
		super();
		this.text = "";
		this.centre = pos;
		this.prefs().setLineColor(Color.black);
		this.prefs().setFillColor(Color.white);
		this.radius=2.5;
	}

	public PrimitiveElement3DPoint(double x, double y, double z) {
		this(new Vector3D(x,y,z));
	}
	
	public PrimitiveElement3DPoint(Vector3D pos, String str, Color color) {
		super();
		this.text = str;
		this.prefs().setFillColor(color);
		this.prefs().setLineColor(color);
		this.centre = pos;
		this.radius=2;
	}

	@Override
	public void transform(AffineTransform3D T) {
		T.transform(centre);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

}