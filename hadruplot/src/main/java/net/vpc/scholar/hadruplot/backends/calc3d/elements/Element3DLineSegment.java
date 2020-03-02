package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DCurve;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;


public class Element3DLineSegment extends Element3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5198536529439559238L;
	private Vector3D point1,point2;
	public Element3DLineSegment(Vector3D pt1, Vector3D pt2) {
		prefs().setSplittable(true);
		primitiveElement3D = new PrimitiveElement3DCurve(pt1, pt2);
		point1=new Vector3D(pt1);
		point2=new Vector3D(pt2);
	}

	public Element3DLineSegment(Element3DPoint pt1, Element3DPoint pt2) {
		primitiveElement3D = new PrimitiveElement3DCurve(pt1.primitiveElement3D.getCentre().clone(),
				pt2.primitiveElement3D.getCentre().clone());
		point1=new Vector3D(pt1.primitiveElement3D.getCentre());
		point2=new Vector3D(pt2.primitiveElement3D.getCentre());
	}

	@Override
	public String getDefinition() {
		return ((PrimitiveElement3DCurve) primitiveElement3D).getLine3D().toString();
	}

	public Vector3D getPoint1() {
		return point1;
	}
	
	public Vector3D getPoint2() {
		return point2;
	}
	
	public void setPoint1(Vector3D pt) {
		 point1=new Vector3D(pt);
		 primitiveElement3D = new PrimitiveElement3DCurve(point1, point2);
	}
	
	public void setPoint2(Vector3D pt) {
		point1=new Vector3D(pt);
		primitiveElement3D = new PrimitiveElement3DCurve(point1, point2);
	}

	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =new PrimitiveElement3DCurve(point1, point2);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =new PrimitiveElement3DCurve(point1, point2);
		primitiveElement3D =clip.getClippedElement(primitiveElement3D);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		return primitiveElement3D;
	}

	
}