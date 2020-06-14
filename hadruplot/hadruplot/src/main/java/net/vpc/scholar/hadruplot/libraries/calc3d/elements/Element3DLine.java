package net.vpc.scholar.hadruplot.libraries.calc3d.elements;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class Element3DLine extends Element3D {


	private Line3D line3D;
	private Vector3D pt1,pt2;
    private boolean lineSegment=true;
    private boolean drawPoints=true;
	public Element3DLine(Vector3D pt1, Vector3D pt2) {
		this.pt1=pt1;
		this.pt2=pt2;
		prefs().setSplittable(true);
		line3D = new Line3D(pt1, pt2);
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		//Vector3D Pt1=new Vector3D(getSceneManager().getSettings().inverseMapX(line3D.Pt3D.getX()),getSceneManager().getSettings().inverseMapY(line3D.Pt3D.getY()),getSceneManager().getSettings().inverseMapZ(line3D.Pt3D.getZ()));
		//Vector3D pt2=pt1.add(line3D.DirVector3D);
		Vector3D tempPt1=new Vector3D(getSceneManager().getSettings().inverseMapX(getPt1().getX()),getSceneManager().getSettings().inverseMapY(getPt1().getY()),getSceneManager().getSettings().inverseMapZ(getPt1().getZ()));
		Vector3D tempPt2=new Vector3D(getSceneManager().getSettings().inverseMapX(getPt2().getX()),getSceneManager().getSettings().inverseMapY(getPt2().getY()),getSceneManager().getSettings().inverseMapZ(getPt2().getZ()));
		if(T!=null)T.transform(tempPt1);
		if(T!=null)T.transform(tempPt2);
		
		primitiveElement3D =new PrimitiveElement3DCurve(tempPt1,tempPt2);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
		primitiveElement3D.prefs().setBackColor(prefs().getBackColor());
		primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		
		PrimitiveElement3DCollection e=new PrimitiveElement3DCollection();
    	if (!lineSegment){
    		Line3D line=new Line3D(tempPt1,tempPt2);
			PrimitiveElement3DCurve ec=clip.getClippedLinefromLine3D(line);
	        ec.updateElement(primitiveElement3D);
	        e.addElement(ec);
	  	}else{
			PrimitiveElement3D ec=new PrimitiveElement3DCurve(tempPt1,tempPt2);
			ec=clip.getClippedElement(ec);
			if (ec!=null){
			   ec.updateElement(primitiveElement3D);
			   e.addElement(ec);
			}
		}
    	
    	if(drawPoints){
    		PrimitiveElement3D ep1= new PrimitiveElement3DPoint(tempPt1);
    		ep1=clip.getClippedElement(ep1);
    		if (ep1!=null){
    			ep1.updateElement(primitiveElement3D);	e.addElement(ep1);
    		}
    		PrimitiveElement3D ep2= new PrimitiveElement3DPoint(tempPt2);
    		ep2=clip.getClippedElement(ep2);
    		if (ep2!=null){
    			ep2.updateElement(primitiveElement3D);e.addElement(ep2);
        	}
       	}
    	
    	primitiveElement3D =e;
		if (null== primitiveElement3D) return null;
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =new Clip(-1,1,-1,1,-1,1).getClippedLinefromLine3D(line3D);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	if(T!=null) primitiveElement3D.transform(T);
    	primitiveElement3D.prefs().setSplittable(prefs().isSplittable());
		return primitiveElement3D;
	}
	@Override
	public String getDefinition() {
		return "Point1 = "+getPt1().getPointText()+" <b>&</b> Point2 = "+getPt2().getPointText()+"<br><br>"+"<b>Equation: </b> "+line3D.toString();
	}

	public String getExpression() {
		return line3D.toString();
	}

	public Line3D getLine() {
		return line3D;
	}

	public void setLine(Line3D line) {
		 line3D=line;
	}

	/**
	 * @return if the primitiveElement3D is a lineSegment or a line joining the 2 points
	 */
	public boolean isLineSegment() {
		return lineSegment;
	}

	/**
	 * @param lineSegment Setting it true creates line segment joining 2 points specified, setting it false creates
	 * infinite line (clipped) joining the points, i.e. line extends beyond the points.
	 */
	public void setLineSegment(boolean lineSegment) {
		this.lineSegment = lineSegment;
	}

	/**
	 * @return if the end points of line are two be drawn or not
	 */
	public boolean isDrawPoints() {
		return drawPoints;
	}

	/**
	 * @param drawPoints Setting it true creates end points as well
	 */
	public void setDrawPoints(boolean drawPoints) {
		this.drawPoints = drawPoints;
	}

	/**
	 * @return the pt1
	 */
	public Vector3D getPt1() {
		return pt1;
		
	}

	/**
	 * @param pt1 the pt1 to set
	 */
	public void setPt1(Vector3D pt1) {
		this.pt1 = pt1;
		line3D = new Line3D(pt1, pt2);
	}

	/**
	 * @return the pt2
	 */
	public Vector3D getPt2() {
		return pt2;
	}

	/**
	 * @param pt2 the pt2 to set
	 */
	public void setPt2(Vector3D pt2) {
		this.pt2 = pt2;
		line3D = new Line3D(pt1, pt2);
	}

}

/**
 * Class for Elements3D representing Line in 3D space
 * 
 * @author mahesh
 * 
 */