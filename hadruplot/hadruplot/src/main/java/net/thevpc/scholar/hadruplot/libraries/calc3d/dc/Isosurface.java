package net.thevpc.scholar.hadruplot.libraries.calc3d.dc;

import java.awt.Color;

import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * Isosurface:This class is the interface for implementing the field function d = f(x,y,z) , by providing a 
 * pure virtual function Isosurface::fDensity that computes the field function.
 */
public abstract class Isosurface
{
    /**
     * class to store material information of surface
     */
	public static class SurfaceMaterialRenderer
	{
		public Color color = Color.blue;
		public Color ambient = Color.black;
		public float diffuse;
		public float brilliance;
		public float specular;
	}
	/**
	 * field function of implicit surface
	 * @param x
	 * @param y
	 * @param z
	 * @return value of scalar field function at point (x,y,z)
	 */

	public abstract double f(double x, double y, double z);

	/**
	 * can compute any number of points, starting at a point specified by three parameters (x0,y0,z0) 
	 * and progressing towards the +z axis by a delta specified by the parameter dz.
	 */
	public void fDensity(double x0, double y0, double z0, double dz, int num_points, double[] densities){
		for (int i = 0; i < num_points; ++i)
		{
				densities[i] = f(x0,y0,z0);
				z0 += dz;
		}
	}

	public Vector3D fNormal(double x, double y, double z)
	{
		double nx = f(x +  0.0001f, y,     z)     - f(x, y, z);
		double ny = f(x,     y + 0.0001, z)     - f(x, y, z);
		double nz = f(x,     y,     z + 0.0001) - f(x, y, z);
		return new Vector3D(nx,ny,nz).getUnitVector();
	}

	/**
	 * Returns Bounding box of surface (surface is clipped against this box)
	 * @return
	 */
	
	public Box3D getBoundingBox()
	{
		Box3D bbox = new Box3D();
		return bbox;
	}
	
	/**
	 * sets Bounding box of surface (surface is clipped against this box)
	 * @param bbox
	 */
	public void setBoundingBox(Box3D bbox)
	{
		_bbox=bbox;
	}

	/*
	* data
	*/
	protected Box3D _bbox = new Box3D();
}

