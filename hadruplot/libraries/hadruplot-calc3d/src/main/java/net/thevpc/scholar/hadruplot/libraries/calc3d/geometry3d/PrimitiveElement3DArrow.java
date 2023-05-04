package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.awt.Color;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class PrimitiveElement3DArrow extends PrimitiveElement3D {
	public Vector3D p1, p2;
    public boolean absoluteWidth=true;
    private int arrowSize=3;
    
	public PrimitiveElement3DArrow(Vector3D p1, Vector3D p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.prefs().setLineColor(Color.black);
		this.prefs().setFillColor(Color.white);
		calculateCentre();
	}
	
	public PrimitiveElement3DArrow(Vector3D p1, Vector3D p2, Color color, int curveWidth, boolean absoluteWidth) {
		this(p1,p2);
		this.prefs().setLineColor(color);
		this.prefs().setFillColor(color);
		this.prefs().setCurveWidth(curveWidth);
		this.absoluteWidth=absoluteWidth;
	}
	
	/** calculate the average of both vertices and stores in centre */
   private void calculateCentre() {
		this.centre = (p1.add(p2)).scale(0.5); 
   }
		
		
    public PrimitiveElement3DArrow(PrimitiveElement3DArrow element){
		p1=new Vector3D(element.p1);
		p2=new Vector3D(element.p2);
		this.prefs().setFillColor(element.prefs().getFillColor());
		this.prefs().setLineColor(element.prefs().getLineColor());
		this.centre=element.getCentre();
		this.prefs().setCurveWidth(element.prefs().getCurveWidth());
		this.absoluteWidth=element.absoluteWidth;
	}

	@Override
	public void transform(AffineTransform3D T) {
		T.transform(p1);
		T.transform(p2);
		T.transform(centre);
	}

	public int getArrowSize() {
		return arrowSize;
	}

	public void setArrowSize(int arrowSize) {
		this.arrowSize = arrowSize;
	}

}
