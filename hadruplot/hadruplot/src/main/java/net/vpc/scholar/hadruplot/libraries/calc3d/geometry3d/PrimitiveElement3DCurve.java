package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.awt.Color;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class PrimitiveElement3DCurve extends PrimitiveElement3D {
	public Vector3D p1, p2;
    public boolean absoluteWidth=true;
    
	public PrimitiveElement3DCurve(Vector3D p1, Vector3D p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.prefs().setLineColor(Color.black);
		this.prefs().setFillColor(Color.black);
		calculateCentre();
	}
	
	public PrimitiveElement3DCurve(Vector3D p1, Vector3D p2, Color color, int curveWidth, boolean absoluteWidth) {
		this(p1,p2);
		this.prefs().setLineColor(color);
		this.prefs().setFillColor(color);
		this.prefs().setCurveWidth(curveWidth);
		this.absoluteWidth=absoluteWidth;
		calculateCentre();
	}
	
	/** calculate the average of both vertices and stores in centre */
   private void calculateCentre() {
		this.centre = (p1.add(p2)).scale(0.5); 
   }
		
		
    public PrimitiveElement3DCurve(PrimitiveElement3DCurve element){
		p1=new Vector3D(element.p1);
		p2=new Vector3D(element.p2);
		this.prefs().setFillColor(element.prefs().getFillColor());
		this.prefs().setLineColor(element.prefs().getLineColor());
		this.centre=element.getCentre();
		this.prefs().setCurveWidth(element.prefs().getCurveWidth());
		this.absoluteWidth=element.absoluteWidth;
    }

    public Line3D getLine3D(){
		return new Line3D(p1,p2);
    }
    
	@Override
	public void transform(AffineTransform3D T) {
		T.transform(p1);
		T.transform(p2);
		T.transform(centre);
	}

}
