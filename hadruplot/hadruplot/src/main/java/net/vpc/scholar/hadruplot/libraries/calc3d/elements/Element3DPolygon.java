package net.vpc.scholar.hadruplot.libraries.calc3d.elements;


import net.vpc.scholar.hadruplot.libraries.calc3d.core.LocMessages;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DPoly;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Point3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;

import java.util.ArrayList;

/**
 * Class for Elements3D representing Plane in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DPolygon extends Element3D {

	public ArrayList<Vector3D> vertices=new ArrayList<Vector3D>();
	
	public Element3DPolygon(Vector3D... points) {
		for(Vector3D v:points)vertices.add(v);
		
	}
	public Element3DPolygon(Point3D... points) {
		for(Point3D v:points)vertices.add(v.toVector());

	}

	public Element3DPolygon(PrimitiveElement3DPoly e) {
		vertices = e.vertices;
	}

	public Element3DPolygon() {
		
	}
        
        public Point3D[] getPoints(){
            Point3D[] p=new Point3D[vertices.size()];
            for (int i = 0; i < p.length; i++) {
                p[i]=new Point3D(vertices.get(i));
            }
            return p;
        }
	
	@Override
	public String getDefinition() {
		return ((PrimitiveElement3DPoly) primitiveElement3D).getPlane3D().toString();
	}

	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =new PrimitiveElement3DPoly();
		if (vertices.size()<3) return null;
		for (Vector3D v:vertices){
			vertices.add(new Vector3D(getSceneManager().getSettings().mapX(v.getX()),getSceneManager().getSettings().mapX(v.getX()),getSceneManager().getSettings().mapX(v.getX())));
		}
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
		primitiveElement3D.prefs().setBackColor(prefs().getBackColor());
	   	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =new PrimitiveElement3DPoly();
		if (vertices.size()<3) return null;
		for (Vector3D v:vertices){
		  ((PrimitiveElement3DPoly) primitiveElement3D).vertices.add(new Vector3D(getSceneManager().getSettings().inverseMapX(v.getX()),getSceneManager().getSettings().inverseMapY(v.getY()),getSceneManager().getSettings().inverseMapZ(v.getZ())));
		}
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	if(T!=null) primitiveElement3D.transform(T);
		if (null != clip) {
			PrimitiveElement3DPoly clippoly = new PrimitiveElement3DPoly();
			if (clip.getClippedPoly(((PrimitiveElement3DPoly) primitiveElement3D).vertices, clippoly.vertices) != 2) {
				clippoly.reCalculateNormalandCentre();
				clippoly.prefs().setFillColor(primitiveElement3D.prefs().getFillColor());
				clippoly.prefs().setBackColor(primitiveElement3D.prefs().getBackColor());
				clippoly.prefs().setLineColor(primitiveElement3D.prefs().getLineColor());
				clippoly.prefs().setCurveWidth(primitiveElement3D.prefs().getCurveWidth());
				if (prefs().getFillmode()==1)clippoly.setFilled(false); else clippoly.setFilled(true);
				clippoly.prefs().setSplittable(prefs().isSplittable());
				if (clippoly.vertices.size()>2) primitiveElement3D =clippoly;
		      }
		
		}
		return primitiveElement3D;
	}

	public Plane3D getPlane3D() {
		((PrimitiveElement3DPoly) primitiveElement3D).getPlane3D();
		return null;
	}

	public static Element3DPolygon createUnitCirclePolygon(int defaultCount,
			double defaultRadius) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Full constructor.
	 * <p>
	 * Creates a new {@link PrimitiveElement3DPoly} using the given vertices.  The center of the polygon
	 * is calculated using an area weighted method.
	 * <p>
	 * A polygon must have 3 or more vertices, of which one is not colinear with the other two.
	 * <p>
	 * A polygon must also be convex and have anti-clockwise winding.
	 * @param vertices the array of vertices
	 * @throws NullPointerException if vertices is null or contains a null primitiveElement3D
	 * @throws IllegalArgumentException if vertices contains less than 3 points, contains coincident points, is not convex, or has clockwise winding
	 */
	public static void createPolygon(Vector3D... vertices) {
		// check the vertex array
		if (vertices == null) throw new NullPointerException(LocMessages.getString("geometry.polygon.nullArray"));
		// get the size
		int size = vertices.length;
		// check the size
		if (size < 3) throw new IllegalArgumentException(LocMessages.getString("geometry.polygon.lessThan3Vertices"));
		// check for null vertices
		for (int i = 0; i < size; i++) {
			if (vertices[i] == null) throw new NullPointerException(LocMessages.getString("geometry.polygon.nullVertices"));
		}
		// check for convex
		for (int i = 0; i < size; i++) {
			Vector3D p0 = (i - 1 < 0) ? vertices[size - 1] : vertices[i - 1];
			Vector3D p1 = vertices[i];
			Vector3D p2 = (i + 1 == size) ? vertices[0] : vertices[i + 1];
		
			// check for coincident vertices
			if (p1.equals(p2)) {
				throw new IllegalArgumentException(LocMessages.getString("geometry.polygon.coincidentVertices"));
			}
		
		}
		
		// set the vertices
		//this.vertices = vertices;
		
	}
	
	
}

