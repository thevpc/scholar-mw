package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DArrow;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;

/**
 * Class for Elements3D representing Line in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DVector extends Element3D {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1312137775113914325L;
	private Vector3D vector3D;
    public Vector3D point1,point2;
	public Element3DVector(double x, double y, double z) {
		//primitiveElement3D = new PrimitiveElement3DArrow(new Vector3D(0, 0, 0), new Vector3D(x, y, z));
		vector3D = new Vector3D(x, y, z);
		point1=new Vector3D(0,0,0);
		point2=new Vector3D(x,y,z);
	}

	public Element3DVector(Vector3D pt) {
		this(pt.getX(),pt.getY(),pt.getZ());
	}

	public Element3DVector(Vector3D pt1, Vector3D pt2) {
		point1=new Vector3D(pt1);
		point2=new Vector3D(pt2);
		//primitiveElement3D = new PrimitiveElement3DArrow(point1, point2);
		vector3D = new Vector3D(pt1, pt2);
	}

	
	public Vector3D getPoint1() {
		return point1;
	}
	
	public Vector3D getPoint2() {
		return point2;
	}
	
	public void setPoint1(Vector3D pt) {
		 point1=new Vector3D(pt);
		 //primitiveElement3D = new PrimitiveElement3DArrow(point1, point2);
	}
	
	public void setPoint2(Vector3D pt) {
		point1=new Vector3D(pt);
		//primitiveElement3D = new PrimitiveElement3DArrow(point1, point2);
	}

	@Override
	public String getDefinition() {
		return vector3D.toString();
	}

	@Override
	public PrimitiveElement3D createElement() {
		
		Vector3D pt1,pt2;
		pt1=new Vector3D(getSceneManager().getSettings().inverseMapX(point1.getX()),getSceneManager().getSettings().inverseMapY(point1.getY()),getSceneManager().getSettings().inverseMapZ(point1.getZ()));
		pt2=new Vector3D(getSceneManager().getSettings().inverseMapX(point2.getX()),getSceneManager().getSettings().inverseMapY(point2.getY()),getSceneManager().getSettings().inverseMapZ(point2.getZ()));
		if(T!=null)T.transform(pt1);
		if(T!=null)T.transform(pt2);
		primitiveElement3D =new PrimitiveElement3DArrow(pt1, pt2);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
	    primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
	    primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
	    primitiveElement3D.prefs().setDashed(prefs().isDashed());
		primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
	    isCreated=true;
		return primitiveElement3D;
	}
	
	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		Vector3D pt1,pt2;
		pt1=new Vector3D(getSceneManager().getSettings().inverseMapX(point1.getX()),getSceneManager().getSettings().inverseMapY(point1.getY()),getSceneManager().getSettings().inverseMapZ(point1.getZ()));
		pt2=new Vector3D(getSceneManager().getSettings().inverseMapX(point2.getX()),getSceneManager().getSettings().inverseMapY(point2.getY()),getSceneManager().getSettings().inverseMapZ(point2.getZ()));
		if(T!=null)T.transform(pt1);
		if(T!=null)T.transform(pt2);
		primitiveElement3D =new PrimitiveElement3DArrow(pt1, pt2);
		primitiveElement3D =clip.getClippedElement(primitiveElement3D);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
	    primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
	    primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
	    primitiveElement3D.prefs().setDashed(prefs().isDashed());
		primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
	    isCreated=true;
		return primitiveElement3D;
	}

}
