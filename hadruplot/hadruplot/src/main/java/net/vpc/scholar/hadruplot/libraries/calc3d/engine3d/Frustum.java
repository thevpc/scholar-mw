package net.vpc.scholar.hadruplot.libraries.calc3d.engine3d;


/**
  *<p> Represents a rectangular <i> view frustum </i>, the volume of space containing everything
       that is visible in a three-dimensional scene. </p>
   <p> The view frustum is shaped like a pyramid whose apex lies at the <b> camera </b> position. It is bounded by six
       planes, four of which correspond to the edges of the screen and are called the <i> left,
       right, bottom, </i> and <i> top </i> frustum planes. The remaining two planes are called the
       near and far frustum planes, and define the minimum and maximum distances at
       which objects in a scene are visible to the camera. </p>
   <p> The view frustum is aligned to camera space. Camera space, also called eye
       space, is the coordinate system in which the camera lies at the origin, the x axis
       points to the right, and the y axis points upward. Z axis is calculated by using Left Handed system which
       turns out to be vector against the direction of vision (same in OpenGL).  </p>
  
   @author Mahesh Kurmi
   @version 1.0
   @see Camera3D
 */
public final class Frustum
{
	/**
	 * location of planes in terms of their position relative to camera space
	 */
	public double left = 0;
	public double right = 0;
	public double top = 0;
	public double bottom = 0;
	public double near = 0;
	public double far = 0;
	
	/**
	 * The vertical field-of-view angle in degrees.
	 */
	public double verticalFOV = 0;
	
	/**
	 * Constructor - initialises this to some default values.
	 */
	public Frustum()
	{
		this(-1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -100.0f, 90);
	}
	
	
	/**
	 * Constructor.
	 * @param left the left edge plane x-coordinate.
	 * @param right the right edge plane x-coordinate.
	 * @param top the top edge plane y-coordinate.
	 * @param bottom the bottom edge plane y-coordinate.
	 * @param near the front edge plane z-coordinate.
	 * @param far the back edge plane z-coordinate.
	 * @param verticalFOV the vertical field-of-view angle in degrees.
	 */
	public Frustum(double left, double right, double top, double bottom, double near, double far, double verticalFOV)
	{
		set(left,right,top,bottom,near,far,verticalFOV);
	}
	

	/**
	 * Set this frustum's values.
	 * @param left the left edge plane x-coordinate.
	 * @param right the right edge plane x-coordinate.
	 * @param top the top edge plane y-coordinate.
	 * @param bottom the bottom edge plane y-coordinate.
	 * @param near the front edge plane z-coordinate.
	 * @param far the back edge plane z-coordinate.
	 * @param verticalFOV the vertical field-of-view angle in radians.
	 */
	public void set(double left, double right, double top, double bottom, double near, double far, double verticalFOV)
	{
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.near = near;
		this.far = far;
		this.verticalFOV = verticalFOV;
	}
	
	
	/**
	 * Calculate this frustum's view plane depth (distance of view plane from apex/camera,
	 * also called focal length of the camera) based on its current values.
	 * @return the view plane depth.
	 */
	public double viewPlane()
	{
		/*
		 * tan (theta / 2) = (total frustum height / 2) / viewplane
		 */
		return (top-bottom) * 0.5f / (double)Math.tan(verticalFOV*Math.PI/360);
		
	}
	

}