package net.vpc.scholar.hadruplot.backends.calc3d.geometry3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import net.vpc.scholar.hadruplot.backends.calc3d.math.AffineTransform3D;

public class Surface3D extends Object3D<PrimitiveElement3D>{
	/**
	 * shinyNess: Lower values will spread out the shine shineIntensity: Maximum
	 * intensity of the shine
	 */
	public double shinyNess = 0.15, shineIntensity = 0.5;

	/** edge color of surface */
	public Color curveColor;

	/** fill color of surface */
	public Color surfaceColor;

	public Collection<PrimitiveElement3DRect> elements = new ArrayList<PrimitiveElement3DRect>();

	/**Default Constructor: does nothing*/
	public Surface3D() {}

	/**
	 * Creates a new PrimitiveElement3DRect set and allocate memory for storing the points.
	 * 
	 * @param n
	 *            the number of points to store
	 */
	public Surface3D(int n) {
		this.elements = new ArrayList<PrimitiveElement3DRect>(n);
	}

	public Surface3D(PrimitiveElement3DRect[] elements) {
		for (PrimitiveElement3DRect element : elements)
			this.elements.add(element);
	}
	
	/**
	 * Makes a copy of surface
	 * @param surface3D
	 */
	public Surface3D(Surface3D surface3D) {
		this();
		this.set(surface3D);
	}

	
	public void set(Surface3D surface3D) {
		this.elements.clear();
		for (PrimitiveElement3DRect element : surface3D.elements)
			this.elements.add(new PrimitiveElement3DRect(element));
		this.surfaceColor=surface3D.surfaceColor;
		this.curveColor=surface3D.surfaceColor;
	}

	/**
	 * Adds a new PrimitiveElement3DRect to the set of point.
	 */
	public void addElement(PrimitiveElement3DRect element) {
		this.elements.add(element);
	}

	/**
	 * Add a series of PrimitiveElement3DRect
	 * 
	 * @param elements
	 *            an array of PrimitiveElement3DRect
	 */
	public void addElements(PrimitiveElement3DRect[] elements) {
		for (PrimitiveElement3DRect element : elements)
			this.addElement(element);
	}

	public void addSurface(Surface3D surface3D) {
		this.elements.addAll(surface3D.elements);
	}


	public void transform(AffineTransform3D T){
		for (PrimitiveElement3D e : elements) e.transform(T);
	}

}
