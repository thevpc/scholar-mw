

package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;


/**
 * A three-dimensional plane. A plane is defined by a point and a normal.
 */
public final class Plane
{
	public Vector3D point;
	public Vector3D normal;
	
	
	/**
	 * Constructor.
	 * @param aPoint a point on the plane.
	 * @param aNormal the normal to the plane.
	 */
	public Plane(Vector3D aPoint, Vector3D aNormal)
	{
		set(aPoint, aNormal);
	}
	
	
	/**
	 * Set this plane's point and normal.
	 * @param aPoint a point on the plane.
	 * @param aNormal the normal to the plane.
	 */
	public void set(Vector3D aPoint, Vector3D aNormal)
	{
		point.set(aPoint);
		normal.set(aNormal);
	}
}


