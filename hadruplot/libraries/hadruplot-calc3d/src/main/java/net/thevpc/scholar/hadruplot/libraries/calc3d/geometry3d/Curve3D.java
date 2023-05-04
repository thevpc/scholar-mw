package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class Curve3D extends Object3D<PrimitiveElement3D>{
	   private Color curveColor;
	   private int curveWidth;boolean absoluteWidth;
	   public Collection<PrimitiveElement3DCurve> quads = new ArrayList<PrimitiveElement3DCurve>();
	   
		public Collection<PrimitiveElement3DCurve> elements = new ArrayList<PrimitiveElement3DCurve>();

		/**Default Constructor: does nothing*/
		public Curve3D() {}

		/**
		 * Creates a new PrimitiveElement3DCurve set and allocate memory for storing the points.
		 * 
		 * @param n
		 *            the number of points to store
		 */
		public Curve3D(int n) {
			this.elements = new ArrayList<PrimitiveElement3DCurve>(n);
		}

		public Curve3D(PrimitiveElement3DCurve[] elements) {
			for (PrimitiveElement3DCurve element : elements)
				this.elements.add(element);
		}
		
		/**
		 * Makes a copy of curve
		 * @param curve3D
		 */
		public Curve3D(Curve3D curve3D) {
			this();
			this.set(curve3D);
		}

		
		public void set(Curve3D curve3D) {
			this.elements.clear();
			for (PrimitiveElement3DCurve element : curve3D.elements)
				this.elements.add(new PrimitiveElement3DCurve(element));
			this.curveColor=curve3D.curveColor;
			this.curveWidth=curve3D.curveWidth;
			this.absoluteWidth=curve3D.absoluteWidth;
		}

		/**
		 * Adds a new PrimitiveElement3DCurve to the set of point.
		 */
		public void addElement(PrimitiveElement3DCurve element) {
			this.elements.add(element);
		}

		/**
		 * Add a series of PrimitiveElement3DCurve
		 * 
		 * @param elements
		 *            an array of PrimitiveElement3DCurve
		 */
		public void addElements(PrimitiveElement3DCurve[] elements) {
			for (PrimitiveElement3DCurve element : elements)
				this.addElement(element);
		}

		public void addCurve(Curve3D curve3D) {
			this.elements.addAll(curve3D.elements);
		}

	
}
