package net.vpc.scholar.hadruplot.backends.calc3d.elements;

import net.vpc.scholar.hadruplot.backends.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.backends.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.backends.calc3d.math.MathUtils;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.Calculable;
import net.vpc.scholar.hadruplot.backends.calc3d.mathparser.ExpressionBuilder;

import java.awt.*;

/**
 * Class for Elements3DSurfacd representing Surface in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DSurface extends Element3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8584466900933868834L;
	private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
	private transient  Calculable calc;
	private String expr="";
	/** Dimension bounding the surfce */
	double minX, maxX, minY, maxY, minZ, maxZ;
	/**no of divisions/grids for surface*/
	private int xGrids = 20, yGrids = 20;
	public Element3DSurface(String expr) {
		this.minX = -1;
		this.minY = -1;
		this.minZ = -1;
		this.maxX = 1;
		this.maxY = 1;
		this.maxZ = 1;
		this.expr = expr;
		this.prefs().setFillmode(2);
	}

	public Element3DSurface(String expr, Box3D box) {
		this.minX = box.getMinX();
		this.minY = box.getMinY();
		this.minZ = box.getMinZ();
		this.maxX = box.getMaxX();
		this.maxY = box.getMaxY();
		this.maxZ = box.getMaxZ();
		this.expr = expr;
	}

	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =createSurface(null);
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =createSurface(clip);
		return primitiveElement3D;
	}

	@Override
	public String getDefinition() {
		return "z = " + expr +
			   "<br>"+" <br> <b>x-range: </b> &nbsp ["+ getSceneManager().getSettings().minX + " , " +getSceneManager().getSettings().maxX +"]"+
			   "<br>"+" <br> <b>y-range: </b> &nbsp ["+ getSceneManager().getSettings().minY + " , " + getSceneManager().getSettings().maxY +"]";
	}

	public String getExpression() {
		return expr;
	}
	
	public void setExpression(String expr) {
		this.expr = expr;
	}

	public Box3D getBox() {
		return new Box3D(minX, maxX, minY, maxY, minZ, maxZ);
	}

	public void setBox(Box3D box) {
		this.minX = box.getMinX();
		this.minY = box.getMinY();
		this.minZ = box.getMinZ();
		this.maxX = box.getMaxX();
		this.maxY = box.getMaxY();
		this.maxZ = box.getMaxZ();
	}

	private PrimitiveElement3DCollection createSurface(Clip clip) {
		try {
			calc = new ExpressionBuilder(expr).withVariableNames("x", "y")
					.build();
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
		float i, j;
		double x, y, z;
       
		PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();
    
		Vector3D v1, v2, v3, v4;
		for (i = 0; i < xGrids; i++) {
			Inner:
			for (j = 0; j < yGrids; j++) {

				x = minX + (maxX - minX) * i / xGrids;
				y = minY + (maxY - minY) * j / yGrids;
				z = f(x, y);
				if (!MathUtils.isValidNumber(z))continue Inner;
				v1 = new Vector3D(x, y, z);
				x = minX + (maxX - minX) * i / xGrids;
				y = minY + (maxY - minY) * (j + 1) / yGrids;
				z = f(x, y);
				if (!MathUtils.isValidNumber(z))continue Inner;
				v2 = new Vector3D(x, y, z);
				x = minX + (maxX - minX) * (i + 1) / xGrids;
				y = minY + (maxY - minY) * (j + 1) / yGrids;
				z = f(x, y);
				if (!MathUtils.isValidNumber(z))continue Inner;
				v3 = new Vector3D(x, y, z);
				x = minX + (maxX - minX) * (i + 1) / xGrids;
				y = minY + (maxY - minY) * j / yGrids;
				z = f(x, y);
				if (!MathUtils.isValidNumber(z))continue Inner;
				v4 = new Vector3D(x, y, z);
				PrimitiveElement3DPoly element = new PrimitiveElement3DPoly();
				element.addPoint(v1);
				element.addPoint(v2);
				element.addPoint(v3);
				element.addPoint(v4);
				if (T!=null)element.transform(T);
				element.drawContours=true;
				if (prefs().getFillmode()==1)
				{
					element.setFilled(false); 
			     	element.prefs().setLineColor(new Color(i / xGrids, 0, j / yGrids));
		        	element.prefs().setCurveWidth(Math.max(1,prefs().getCurveWidth()));
		           	element.prefs().setFillColor(new Color(i / xGrids, 0, j / yGrids));
				}
				else if (prefs().getFillmode()==3){
					element.prefs().setFillColor(prefs().getFillColor());
					element.prefs().setFillColor(ColorModel.getPreset(2).getPolygonColor((float) element.getCentre().getZ()));
					element.prefs().setBackColor(Color.gray);
			     	element.prefs().setLineColor(prefs().getLineColor());
			    	element.prefs().setCurveWidth(prefs().getCurveWidth());
				}
				else if(prefs().getFillmode()==2){
					element.prefs().setFillColor(prefs().getFillColor());
					element.prefs().setBackColor(prefs().getBackColor());
				    element.prefs().setLineColor(prefs().getLineColor());
				    element.prefs().setCurveWidth(prefs().getCurveWidth());
				}
				else{
					element.prefs().setFillColor(new Color(i / xGrids, 0, j / yGrids));
					//primitiveElement3D.setFillColor( Color.getHSBColor((float) (0.3+0.7*(z-minZ)/(maxZ - minZ)), 1f, 1f));
					element.prefs().setBackColor(Color.gray);
				    element.prefs().setLineColor(prefs().getLineColor());
				    element.prefs().setCurveWidth(prefs().getCurveWidth());
				 }
				if (null != clip) {
					PrimitiveElement3DPoly clippoly = new PrimitiveElement3DPoly();
					if (clip.getClippedPoly(element.vertices, clippoly.vertices) != 2) {
						clippoly.reCalculateNormalandCentre();
						clippoly.prefs().setFillColor(element.prefs().getFillColor());
						clippoly.prefs().setBackColor(element.prefs().getBackColor());
						clippoly.prefs().setLineColor(element.prefs().getLineColor());
						clippoly.prefs().setCurveWidth(element.prefs().getCurveWidth());
						if (prefs().getFillmode()==1)clippoly.setFilled(false); else clippoly.setFilled(true);
						clippoly.prefs().setSplittable(prefs().isSplittable());
						clippoly.drawContours=true;
						if (clippoly.vertices.size()>2)surface3D.addElement(clippoly);
					}
				} else {
					surface3D.addElement(element);
				}
			}
		}
		primitiveElement3D =surface3D;
		return (surface3D.primitiveElement3DS.size() > 0) ? surface3D : null;
	}

	private double f(double x, double y) {
		double z;
		calc.setVariable("x", getSceneManager().getSettings().mapX(x));
		calc.setVariable("y", getSceneManager().getSettings().mapY(y));
		z=calc.calculate();
		z=getSceneManager().getSettings().inverseMapZ(z);
		/*
		if (z < minZ)
			return minZ;
		if (z > maxZ)
			return maxZ;
		*/
		return z;
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
			LOG.error("Illegral number of xgrids" + yGrids);
	}


}
