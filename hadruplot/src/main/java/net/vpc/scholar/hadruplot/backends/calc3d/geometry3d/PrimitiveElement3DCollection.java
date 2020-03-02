package net.vpc.scholar.hadruplot.backends.calc3d.geometry3d;

import java.util.ArrayList;
import java.util.List;

import net.vpc.scholar.hadruplot.backends.calc3d.math.AffineTransform3D;

public class PrimitiveElement3DCollection extends PrimitiveElement3D {

	public ArrayList<PrimitiveElement3D> primitiveElement3DS = new ArrayList<PrimitiveElement3D>();

	/**Default Constructor: does nothing*/
	public PrimitiveElement3DCollection() {}

	/**
	 * Creates a new PrimitiveElement3DRect set and allocate memory for storing the points.
	 * 
	 * @param n
	 *            the number of points to store
	 */
	public PrimitiveElement3DCollection(int n) {
		this.primitiveElement3DS = new ArrayList<PrimitiveElement3D>(n);
	}

	public PrimitiveElement3DCollection(PrimitiveElement3D[] primitiveElement3DS) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			this.primitiveElement3DS.add(primitiveElement3D);
		}
	}
	
	public PrimitiveElement3DCollection(List<PrimitiveElement3D> primitiveElement3DS) {
		this.primitiveElement3DS.addAll(primitiveElement3DS);
	}
		
	
	/**
	 * Adds a new PrimitiveElement3DRect to the set of point.
	 */
	public void addElement(PrimitiveElement3D primitiveElement3D) {
		if(primitiveElement3D !=null) {
			this.primitiveElement3DS.add(primitiveElement3D);
		}
	}

	/**
	 * Add a series of PrimitiveElement3DRect
	 * 
	 * @param primitiveElement3DS
	 *            an array of PrimitiveElement3DRect
	 */
	public void addElements(PrimitiveElement3D[] primitiveElement3DS) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS)
			this.addElement(primitiveElement3D);
	}

	public void addCollection(PrimitiveElement3DCollection collection) {
		this.primitiveElement3DS.addAll(collection.primitiveElement3DS);
	}


	public void transform(AffineTransform3D T){
		for (PrimitiveElement3D e : primitiveElement3DS) e.transform(T);
	}

	@Override
	public void setRenderable(boolean renderable) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS)
			primitiveElement3D.setRenderable(renderable);
	}
	
	public void updateCollection(PrimitiveElement3D e){
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS)
			primitiveElement3D.updateElement(e);
	}

}
