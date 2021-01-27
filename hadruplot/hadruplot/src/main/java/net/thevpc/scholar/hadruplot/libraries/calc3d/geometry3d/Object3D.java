package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;

public  class Object3D<T> {
	/**
	 * shinyNess: Lower values will spread out the shine shineIntensity: Maximum
	 * intensity of the shine
	 */
	public double shinyNess = 0.15, shineIntensity = 0.5;

	/** edge color of surface */
	public Color curveColor;

	/** fill color of surface */
	public Color surfaceColor;

	public Collection<T> elements = new ArrayList<T>();

	/**Default Constructor: does nothing*/
	public Object3D() {}

	/**
	 * Creates a new PrimitiveElement3DRect set and allocate memory for storing the points.
	 * 
	 * @param n
	 *            the number of points to store
	 */
	public Object3D(int n) {
		this.elements = new ArrayList<T>(n);
	}

	public Object3D(T[] elements) {
		for (T element : elements)
			this.elements.add(element);
	}
	
	/**
	 * Makes a copy of surface
	 * @param surface3D
	 */
	public Object3D(Object3D<T> obj3D) {
		this();
		this.set(obj3D);
	}

	
	public void set(Object3D<T> object3D) {
		this.elements.clear();
		for (T element : object3D.elements)
			this.elements.add(element);
	}

	/**
	 * Adds a new PrimitiveElement3DRect to the set of point.
	 */
	public void addElement(T element) {
		this.elements.add(element);
	}

	/**
	 * Add a series of PrimitiveElement3DRect
	 * 
	 * @param elements
	 *            an array of PrimitiveElement3DRect
	 */
	public void addElements(T[] elements) {
		for (T element : elements)
			this.addElement(element);
	}

	public void addSurface(Object3D<T> object3D) {
		this.elements.addAll(object3D.elements);
	}

	public void addElements(Collection<T> elements) {
		this.elements.addAll(elements);
	}

	public void transform(AffineTransform3D T){
		for (T e : elements) ((PrimitiveElement3D) e).transform(T);
	}

}
