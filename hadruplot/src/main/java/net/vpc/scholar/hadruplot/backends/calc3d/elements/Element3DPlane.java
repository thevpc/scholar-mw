package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DPoly;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Plane3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;

/**
 * Class for Elements3D representing Plane in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DPlane extends Element3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7685553015321609440L;
	private Plane3D plane3D;
	
	public Element3DPlane(Vector3D pt1, Vector3D pt2, Vector3D pt3) {
		PrimitiveElement3DPoly e = new PrimitiveElement3DPoly();
		e.addPoint(pt1);
		e.addPoint(pt2);
		e.addPoint(pt3);
		plane3D = new Plane3D(pt1, pt2, pt3);
	}

	
	public Element3DPlane(double a, double b, double c, double d) {
		plane3D = new Plane3D(a,b, c, d);
	}

	public Element3DPlane(Plane3D plane) {
		this(plane.get_a(),plane.get_b(),plane.get_c(),plane.get_d());
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		if (T!=null){	//Apply transform to normal of plane only	
			Vector3D v=new Vector3D(plane3D.get_a(),plane3D.get_b(),plane3D.get_c());
			T.transform(v);
			primitiveElement3D =clip.getClippedPolygonfromPlane(new Plane3D(v.getX(),v.getY(),
					v.getZ(), plane3D.get_d()));
		}else{
			primitiveElement3D =clip.getClippedPolygonfromPlane(new Plane3D(getSceneManager().getSettings().mapX(plane3D.get_a()), getSceneManager().getSettings().mapY(plane3D.get_b()),
					getSceneManager().getSettings().mapZ(plane3D.get_c()), plane3D.get_d()));
		}
		
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
		primitiveElement3D.prefs().setBackColor(prefs().getBackColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		return primitiveElement3D;
	}

	public String getExpression() {
		return plane3D.toString();
	}

	@Override
	public String getDefinition() {
		return plane3D.toString();
	}
	public Plane3D getPlane3D() {
		return plane3D;
	}

	public void setPlane3D(Plane3D plane3D1) {
		this.plane3D = plane3D1;
	}

	@Override
	public PrimitiveElement3D createElement() {
		return createElement(new Clip(-1,1,-1,1,-1,1));
	}

}