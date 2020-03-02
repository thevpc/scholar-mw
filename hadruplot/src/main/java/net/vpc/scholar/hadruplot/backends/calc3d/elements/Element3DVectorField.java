package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DArrow;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3DCollection;
import net.vpc.scholar.hadruplot.backends.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.backends.calc3d.math.MathUtils;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.Calculable;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.ExpressionBuilder;
import net.vpc.scholar.hadruplot.backends.calc3d.utils.ColorUtils;


public class Element3DVectorField extends Element3D {

	private static Logger LOG = Logger.getLogger(Element3DVectorField.class.getName());
	private transient Calculable calc_x,calc_y,calc_z;
	private String exprX="",exprY="",exprZ="";
	
	/**minimum and maximum values of parametric variable t*/
	double minX, maxX, minY, maxY, minZ, maxZ;
	
	/**no of divisions/grids for surface*/
	private int xGrids = 6, yGrids = 6, zGrids =6;
	
	private int numSegments=6;

	public Element3DVectorField (){
		prefs().setSplittable(false);
		
	}
	public int getxGrids() {
		return xGrids;
	}

	public void setxGrids(int xGrids) {
		if (xGrids > 0)
			this.xGrids = xGrids;
		else
			LOG.error("Illegral number of xgrids" + xGrids);
	}

	public int getyGrids() {
		return yGrids;
	}

	public void setyGrids(int yGrids) {
		if (yGrids > 0)
			this.yGrids = yGrids;
		else
			LOG.error("Illegral number of ygrids" + yGrids);
	}

	/**
	 * @return the zGrids
	 */
	public int getzGrids() {
		return zGrids;
	}

	/**
	 * @param zGrids the zGrids to set
	 */
	public void setzGrids(int zGrids) {
		if (zGrids > 0)
			this.zGrids = zGrids;
		else
			LOG.error("Illegral number of zgrids" + zGrids);

	}
	
	@Override
	public PrimitiveElement3D createElement() {
		
		primitiveElement3D =CreateCurve(null);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		
		primitiveElement3D =CreateCurve(clip);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
		return primitiveElement3D;
	}
	
	public PrimitiveElement3DCollection CreateCurve(Clip clip){
		minX=getSceneManager().getSettings().mappedClipBox.getMinX();
		minY=getSceneManager().getSettings().mappedClipBox.getMinY();
		minZ=getSceneManager().getSettings().mappedClipBox.getMinZ();
		maxX=getSceneManager().getSettings().mappedClipBox.getMaxX();
		maxY=getSceneManager().getSettings().mappedClipBox.getMaxY();
		maxZ=getSceneManager().getSettings().mappedClipBox.getMaxZ();
			

		try {
			calc_x = new ExpressionBuilder(exprX).withVariableNames("x","y","z")
					.build();
			calc_y = new ExpressionBuilder(exprY).withVariableNames("x","y","z")
					.build();
			calc_z = new ExpressionBuilder(exprZ).withVariableNames("x","y","z")
					.build();
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	
		PrimitiveElement3DCollection curve3D = new PrimitiveElement3DCollection();

		Vector3D v1, v2;
			double t;
		
		double dx,dy,dz;
		dx=(maxX-minX)/xGrids;
		dy=(maxY-minY)/yGrids;
		dz=(maxZ-minZ)/zGrids;	

		double arrowLength=(maxX-minX)/xGrids/2;
		double min=Double.POSITIVE_INFINITY;
		double max =Double.NEGATIVE_INFINITY;
		
		for (double x = minX; x <= maxX; x +=dx) {
			for (double y = minY; y <= maxY; y += dy) {
				for (double z = minZ; z <= maxZ; z += dz) {
					double vx=fx(x,y,z);
					if (!MathUtils.isValidNumber(x))continue;
					double vy=fy(x,y,z);
					if (!MathUtils.isValidNumber(y))continue;
					double  vz=fz(x,y,z);
					if (!MathUtils.isValidNumber(z))continue;
					
					Vector3D dir=new Vector3D(vx,vy,vz);
					double length=dir.getLength();
					if(length>max)max=length;
					if(length<min)min=length;
					if(length==0)continue;
					dir=dir.getUnitVector();
					dir.scaleEq(arrowLength);
					v1=new Vector3D(x,y,z);
					v2=new Vector3D(x+dir.getX(),y+dir.getY(),z+dir.getZ());
					PrimitiveElement3D ec=new PrimitiveElement3DArrow(v1,v2);
					ec.depth=length;
					if(T!=null)ec.transform(T);
					//if (null!=clip) ec= clip.getClippedElement(ec);
					if (null!=ec) {
					   ec.prefs().setCurveWidth(prefs().getCurveWidth());
					   ec.prefs().setLineColor(prefs().getLineColor());
					   ec.prefs().setFillColor(prefs().getFillColor());
					   ec.prefs().setSplittable(prefs().isSplittable());
					   ec.prefs().setDashed(prefs().isDashed());
					   curve3D.addElement(ec);
					}
				}
			}
		}

		if(!Double.isInfinite(max)&& !Double.isInfinite(min)&& max>min){
			//Assign colors as per magnitude of fields
			for(PrimitiveElement3D e:curve3D.primitiveElement3DS){
				e.prefs().setFillColor( ColorUtils.blendColors(getSceneManager().getSettings().backgroundColor,prefs().getFillColor(), Math.max(0.25,(e.depth-min)/(max-min))));
				e.depth=0;	
			}
		}
		primitiveElement3D =curve3D;
		return curve3D;
		
	}
	public int getNumXSegments() {
		return xGrids;
	}
	public int getNumYSegments() {
		return yGrids;
	}
	public int getNumZSegments() {
		return zGrids;
	}
	public void setNumSegments(int numXSegments,int numYSegments,int numZSegments) {
		this.xGrids = numXSegments;
		this.yGrids = numYSegments;
		this.zGrids = numZSegments;

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
	
	double fx(double x,double y,double z){
		calc_x.setVariable("x", x);
		calc_x.setVariable("y", y);
		calc_x.setVariable("z", z);
		
		return getSceneManager().getSettings().inverseMapX(calc_x.calculate());
	}
	
	double fy(double x,double y,double z){
		calc_y.setVariable("x", x);
		calc_y.setVariable("y", y);
		calc_y.setVariable("z", z);

		return getSceneManager().getSettings().inverseMapZ(calc_y.calculate());
	}
	double fz(double x,double y,double z){
		calc_z.setVariable("x", x);
		calc_z.setVariable("y", y);
		calc_z.setVariable("z", z);

		return getSceneManager().getSettings().inverseMapZ(calc_z.calculate());
	}
	
	@Override
	public String getDefinition(){
		return "<br>x = " + exprX +"<br>" + "y = " + exprY +"<br>" +"z = " + exprZ +
		   "<br>"+" <br> <b>x-range: </b> &nbsp ["+ getSceneManager().getSettings().minX + " , " +getSceneManager().getSettings().maxX +"]"+
		   "<br>"+" <br> <b>y-range: </b> &nbsp ["+ getSceneManager().getSettings().minY + " , " + getSceneManager().getSettings().maxY +"]"+
		   "<br>"+" <br> <b>z-range: </b> &nbsp ["+ getSceneManager().getSettings().minZ + " , " + getSceneManager().getSettings().maxZ +"]" ;

	}
}