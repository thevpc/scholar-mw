package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import java.awt.Color;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DCollection;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.backends.calc3d.math.MathUtils;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.Calculable;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.ExpressionBuilder;

public class Element3DParametricSurface extends Element3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5024052169135459326L;
	private static Logger LOG = Logger.getLogger(Element3DParametricSurface.class.getName());
	private transient Calculable calc_x,calc_y,calc_z,calc_R,calc_G,calc_B;
	private String exprX="2*cos(u) / (sqrt(2) + sin(v))",exprY="2*sin(u) / (sqrt(2) + sin(v))",exprZ="2 / (sqrt(2) + cos(v))";
    //	private String exprX="cos(u) * cos(v)",exprY="sin(u) * cos(v)",exprZ="sin(v)";

	/*Shankha settings
	String exprX = "0.2*(1.2 ^ u) * (Cos(u) * (1 + 1.5 * (Cos(1.2) * Cos(v) + Sin(1.2) * Sin(v))))";
	String exprY = "0.2*(1.2 ^ u) * (Sin(u) * (1 + 1.5 * (Cos(1.2) * Cos(v) + Sin(1.2) * Sin(v)))) ";
	String exprZ = "0.2*(1.2 ^ u) * (2.3 + 1.5 * (Cos(v) * Sin(1.2) - Cos(1.2) * Sin(v))) ";
    private double min_u =-25 , max_u =0.833;
	private double min_v =  -Math.PI, max_v =  Math.PI;
	*/
	
	/**minimum and maximum values of parametric variable t*/
	private double min_u , max_u ;
	private double min_v, max_v ;//Math.PI/2;
	
	/**No of segments of the surface*/
	private int uDivisions;
	private int vDivisions;
	
	public Element3DParametricSurface(){
		prefs().setSplittable(false);
		uDivisions=20;
		vDivisions=20;
		min_u=0;
		max_u=2*Math.PI;
		min_v=0;
		max_v=Math.PI;
	}
	
	public double getMin_u() {
		return min_u;
	}
	public void setMin_u(double min_u) {
		this.min_u = min_u;
	}
	public double getMax_u() {
		return max_u;
	}
	public void setMax_u(double max_u) {
		this.max_u = max_u;
	}
	
	public double getMin_v() {
		return min_v;
	}
	public void setMin_v(double min_v) {
		this.min_u = min_v;
	}
	public double getMax_v() {
		return max_v;
	}
	public void setMax_v(double max_v) {
		this.max_v = max_v;
	}
	
	public int getuDivisions() {
		return uDivisions;
	}
	public void setuDivisions(int uDivisions) {
		this.uDivisions = uDivisions;
	}
	public int getvDivisions() {
		return vDivisions;
	}
	public void setvDivisions(int vDivisions) {
		this.vDivisions = vDivisions;
	}
	
	
	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =CreateSurface(null);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =CreateSurface(clip);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
		return primitiveElement3D;
	}
	
	
	public PrimitiveElement3DCollection CreateSurface(Clip clip){
		try {
			calc_x = new ExpressionBuilder(exprX).withVariableNames("u","v")
					.build();
			calc_y = new ExpressionBuilder(exprY).withVariableNames("u","v")
					.build();
			calc_z = new ExpressionBuilder(exprZ).withVariableNames("u","v")
					.build();
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	
		PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();

		double du = (max_u - min_u) / (uDivisions - 1);
		double dv = (max_v - min_v) / (vDivisions - 1);

		double u,v;
		Vector3D v1,v2,v3,v4;
		for (u = min_u; u< max_u ; u+= du){
			Inner:
			for (v = min_v; v< max_v ; v+= dv){
				double x,y,z;
				x = fx(u,v);
		        y = fy(u,v);
		        z = fz(u,v);
		    	if (!MathUtils.isValidNumber(x))continue Inner;
		    	if (!MathUtils.isValidNumber(y))continue Inner;
		    	if (!MathUtils.isValidNumber(z))continue Inner;
				v1 = new Vector3D(x, y, z);
				x = fx(u+du,v);
		        y = fy(u+du,v);
		        z = fz(u+du,v);
		     	if (!MathUtils.isValidNumber(x))continue Inner;
		    	if (!MathUtils.isValidNumber(y))continue Inner;
		    	if (!MathUtils.isValidNumber(z))continue Inner;
				v2 = new Vector3D(x, y, z);
				x = fx(u+du,v+dv);
		        y = fy(u+du,v+dv);
		        z = fz(u+du,v+dv);
		    	if (!MathUtils.isValidNumber(z))continue Inner;
				v3 = new Vector3D(x, y, z);
				x = fx(u,v+dv);
		        y = fy(u,v+dv);
		        z = fz(u,v+dv);
		     	if (!MathUtils.isValidNumber(x))continue Inner;
		    	if (!MathUtils.isValidNumber(y))continue Inner;
		    	if (!MathUtils.isValidNumber(z))continue Inner;
				v4 = new Vector3D(x, y, z);
				
				PrimitiveElement3DPoly element = new PrimitiveElement3DPoly();
				element.addPoint(v1);
				element.addPoint(v2);
				element.addPoint(v3);
				element.addPoint(v4);
				if (prefs().getFillmode()==1)element.setFilled(false); else element.setFilled(true);
				element.prefs().setSplittable(false);
				if (prefs().getFillmode()==0){
					element.prefs().setFillColor(prefs().getFillColor());
			     	element.prefs().setLineColor(prefs().getLineColor());
			    	element.prefs().setCurveWidth(prefs().getCurveWidth());
				}
				else{
					element.prefs().setFillColor(new Color((float)((Math.sin(u) + 1) * 0.5), (float)((Math.cos(u) + 1) * 0.5), (float)((Math.cos(v) + 1) * 0.5)));
				    element.prefs().setLineColor(prefs().getLineColor());
				}
				if (null != clip) {
					PrimitiveElement3DPoly clippoly = new PrimitiveElement3DPoly();
					if (clip.getClippedPoly(element.vertices, clippoly.vertices) != 2) {
						clippoly.reCalculateNormalandCentre();
						clippoly.prefs().setFillColor(element.prefs().getFillColor());
						clippoly.prefs().setLineColor(element.prefs().getLineColor());
						clippoly.prefs().setCurveWidth(element.prefs().getCurveWidth());
						if (prefs().getFillmode()==1)clippoly.setFilled(false); else clippoly.setFilled(true);
						clippoly.prefs().setSplittable(false);
						if (clippoly.vertices.size()>2) surface3D.addElement(clippoly);
						
					}
				} else {
					surface3D.addElement(element);
				}
				
                /* lines
				PrimitiveElement3DCurve c1,c2,c3,c4;
				c1=new PrimitiveElement3DCurve(v1,v2);
				c2=new PrimitiveElement3DCurve(v2,v3);
				c3=new PrimitiveElement3DCurve(v3,v4);
				c4=new PrimitiveElement3DCurve(v4,v1);
				surface3D.addElement(c1);surface3D.addElement(c2);
				surface3D.addElement(c3);surface3D.addElement(c4);
				*/
			} //for loop v
		}//for loop u
		
		primitiveElement3D =surface3D;
		return surface3D;
		
	}
	
	
	public String getExprX() {
		return exprX;
	}
	public void setExprX(String exprX) {
		this.exprX = exprX;
	}
	public String getExprY() {
		return exprY;
	}
	public void setExprY(String exprY) {
		this.exprY = exprY;
	}
	public String getExprZ() {
		return exprZ;
	}
	public void setExprZ(String exprZ) {
		this.exprZ = exprZ;
	}
	
	double fx(double u,double v){
		calc_x.setVariable("u", u);
		calc_x.setVariable("v", v);
		return getSceneManager().getSettings().inverseMapX(calc_x.calculate());
	}
	
	double fy(double u,double v){
		calc_y.setVariable("u", u);
		calc_y.setVariable("v", v);
		return getSceneManager().getSettings().inverseMapY(calc_y.calculate());
	}
	
	double fz(double u,double v){
		calc_z.setVariable("u", u);
		calc_z.setVariable("v", v);
		return getSceneManager().getSettings().inverseMapZ(calc_z.calculate());
	}
	
	@Override
	public String getDefinition(){
		return "<br>x = " + exprX +"<br>" + "y = " + exprY +"<br>" +"z = " + exprZ +
			   "<br>"+" <br> <b>u-range: </b> &nbsp ["+ min_u + " , " + max_u +"]"+
			   "<br>"+" <br> <b>v-range: </b> &nbsp ["+ min_v + " , " + max_v +"]";
	}
	
}

