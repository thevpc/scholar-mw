package net.vpc.scholar.hadruplot.libraries.calc3d.dc;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DCollection;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * IsoMesher
 */
public abstract class IsoMesher
{
	final double  TOLERANCE_DENSITY =1e-3;
	final double  TOLERANCE_COORD  = 1e-5;
	/** Construct a mesh generator
	 */
	public IsoMesher(Isosurface iso)
	{
		_iso = iso;
	}


	/** Set the voxel size
	 */
	public void setVoxelSize(float x, float y, float z)
	{
		_voxelSize = new Vector3D(x, y, z);
	}


	/**
	 */
	public abstract PrimitiveElement3DCollection createMesh();


	protected static class Point
	{
		public double density;
		public Vector3D v;
	}

	/** Intersect a voxel edge along the x axis
	 */
	public void intersect_xaxis(Point p0, Point p1, Point out)
	{
		double xa;
		double xb;
		if (booldsign(p0.density)) // p0 < 0  ,  p1 > 0
		{
			xa = p0.v.getX();
			xb = p1.v.getX();
		} // p1 < 0  ,  p0 > 0
		else
		{
			xa = p1.v.getX();
			xb = p0.v.getX();
		}
		double y = p0.v.getY();
		double z = p0.v.getZ();
		double xm;
		double density;
    
		while (true)
		{
			xm = (xa + xb) * 0.5f;
			density=_iso.f(xm, y, z);
			density += 1e-4f;
			if (Math.abs(density) < TOLERANCE_DENSITY)
				break;
			if (Math.abs(xa - xb) < TOLERANCE_COORD)
				break;
    
			if (booldsign(density)) // pm < 0
				xa = xm;
			else // pm > 0
				xb = xm;
		}
    
		out.density = density;
		out.v.set(xm, y, z);
	}


	/** Intersect a voxel edge along the y axis
	 */
	public void intersect_yaxis(Point p0, Point p1, Point out)
	{
		double ya;
		double yb;
		if (booldsign(p0.density)) // p0 < 0  ,  p1 > 0
		{
			ya = p0.v.getY();
			yb = p1.v.getY();
		} // p1 < 0  ,  p0 > 0
		else
		{
			ya = p1.v.getY();
			yb = p0.v.getY();
		}
		double x = p0.v.getX();
		double z = p0.v.getZ();
		double ym;
		double density;
    
		while (true)
		{
			ym = (ya + yb) * 0.5f;
			density=_iso.f(x, ym, z);
			density += 1e-4f;
			if (Math.abs(density) < TOLERANCE_DENSITY)
				break;
			if (Math.abs(ya - yb) < TOLERANCE_COORD)
				break;
    
			if (booldsign(density)) // pm < 0
				ya = ym;
			else // pm > 0
				yb = ym;
		}
    
		out.density = density;
		out.v.set(x, ym, z);
	}

	/** Intersect a voxel edge along the z axis
	 */
	public void intersect_zaxis(Point p0, Point p1, Point out)
	{
		double za;
		double zb;
		if (booldsign(p0.density)) // p0 < 0  ,  p1 > 0
		{
			za = p0.v.getZ();
			zb = p1.v.getZ();
		} // p1 < 0  ,  p0 > 0
		else
		{
			za = p1.v.getZ();
			zb = p0.v.getZ();
		}
		double x = p0.v.getX();
		double y = p0.v.getY();
		double zm;
		double density;
    
		while (true)
		{
			zm = (za + zb) * 0.5f;
			density=_iso.f(x, y, zm);
			density += 1e-4f;
			if (Math.abs(density) < TOLERANCE_DENSITY)
				break;
			if (Math.abs(za - zb) < TOLERANCE_COORD)
				break;
    
			if (booldsign(density)) // pm < 0
				za = zm;
			else // pm > 0
				zb = zm;
		}
    
		out.density = density;
		out.v.set(x, y, zm);
	}


	/*
	 * data
	 */
	protected Isosurface _iso;
	protected Vector3D _voxelSize = new Vector3D();
	protected PrimitiveElement3DCollection _mesh;
	
	/**
	 * sign of a float value: returns 0 for positive, 1 for negative
	 * @return
	 */
	private int dsign(double d){
		return (d>=0)?0:1;
	}
	/**
	 * sign of a float value: returns false for positive, true for negative
	 * @return
	 */
	protected boolean booldsign(double d){
		return (d>=0)?false:true;
	}
	

}
