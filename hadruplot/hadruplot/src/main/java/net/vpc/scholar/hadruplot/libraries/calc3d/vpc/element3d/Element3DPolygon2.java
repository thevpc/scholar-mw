package net.vpc.scholar.hadruplot.libraries.calc3d.vpc.element3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DPoly;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.*;

import java.awt.*;
import java.util.Random;

/**
 * Class for 3D Premitives
 * 
 * @author mahesh
 * 
 */
public class Element3DPolygon2 extends Element3Db {

	/**
	 *
	 */
	private static final long serialVersionUID = 3060747132508766091L;
	private static Logger LOG = Logger.getLogger(Element3DPolygon2.class.getName());

	private int objectCode;
	private int colorMode=1;
	private boolean closed=true;


	private final static int POLYGON=0;
	private final static int PYRAMID=1;
	private final static int PRISM=2;
	private final static int ELLIPSOID=3;
	private final static int TORUS=4;
	private final static int PIE=5;
	private double[] parameters=new double[3];
	private String[] paramNames;

	/**Creates premetive with default parameters*/
	public Element3DPolygon2(int objectCode) {
		this.objectCode = objectCode;
		this.setName(getObjectName(objectCode));
		paramNames=(getParameterNames(objectCode));
		resetParameters();
	}

	public Element3DPolygon2(int objectCode, double a, double b, double c) {
		this.objectCode = objectCode;
		parameters[0]=a;
		parameters[1]=b;
		parameters[2]=c;
		this.setName(getObjectName(objectCode));
		paramNames=(getParameterNames(objectCode));
	}
	
	public  String getObjectName() {
		return getObjectName(objectCode);
	}
	
	public  String[] getParameterNames() {
		return getParameterNames(objectCode);
	}
	
	/**
	 * Returns the name of Object corresponding to code
	 * @param objectCode
	 * @return
	 */
	public static String getObjectName(int objectCode ) {
		switch (objectCode) {
		case POLYGON: {
			return "Polygon";
		}
		case PYRAMID: {
			return "Pyramid";
		}
		case PRISM: {
			return "Prism";
		}
		case ELLIPSOID: {
			return "Ellipsoid";
		}
		case TORUS: {
			return "Torus";
		}
		case PIE: {
			return "Pie";
		}
		default:{
			return "INVALID OBJECT";
		}
		}
	}

	/**
	 * Resets paramters to default value
	 */
	public void resetParameters() {
		switch (objectCode) {
		case POLYGON: {
			parameters[0]=6;
			parameters[1]=3;
			parameters[2]=0;
			break;
		}
		case PYRAMID: {
			parameters[0]=4;
			parameters[1]=3;
			parameters[2]=1;
			break;
		}
		case PRISM: {
			parameters[0]=6;
			parameters[1]=3;
			parameters[2]=1;
			break;
		}
		case ELLIPSOID: {
			parameters[0]=4;
			parameters[1]=3;
			parameters[2]=3;
			break;
		}
		case TORUS: {
			parameters[0]=3;
			parameters[1]=1;
			parameters[2]=10;
			break;
		}
		case PIE: {
			parameters[0]=3;
			parameters[1]=0;
			parameters[2]=90;
			break;
		}
		default:{
			parameters[0]=3;
			parameters[1]=1;
			parameters[2]=0;
			break;
		}
		}
	}
	 /**
	  * Returns an array of names of its parametres used for displaying/editing onject info
	  * @param code
	  * @return
	  */
	public static String[] getParameterNames(int code) {
		switch (code) {
		case POLYGON: {
			String[] str={"No. of sides" ,"CircumRadius" ,"Offsetangle ( in degrees)"};
			return str ;
		}
		case PYRAMID: {
			String[] str= {"No. of sides of Base" ,"CircumRadius", "Height"};
			return str ;
		}
		case PRISM: {
			String[] str= {"No. of sides of Base" ,"CircumRadius" ,"Height"};
			return str ;
		}
		case ELLIPSOID: {
			String[] str= {"SemiAxis alog x (a)" ,"SemiAxis alog x (b)", "SemiAxis alog x (c) "};
			return str ;
		}
		case TORUS: {
			String[] str= {"Mean Radius" ,"Cross sectional radius" ,"no of Divisions"};
			return str ;
		}
		case PIE: {
			String[] str= {" Radius" ,"Angle Start (degree)" ,"Angle End (degree)"};
			return str ;
			
		}default:{
			String[] str= {"Invalid Object" ,"Invalid Object", "Invalid Object"};
			return str ;
		}
		}
	}
	
	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =createSurface(null);
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	isCreated=true;
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =createSurface(clip);
		if (null== primitiveElement3D) return null;
		primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
     	primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
    	primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
    	primitiveElement3D.prefs().setDashed(prefs().isDashed());
    	isCreated=true;
		return primitiveElement3D;
	}


/**
 * Creates Premitive Object based on its code and parameters
 * @param clip
 * @return
 */
	private PrimitiveElement3DCollection createSurface(Clip clip) {
		PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();
		Vector3D centre=new Vector3D(0,0,0);
		Vector3D v1,v2;
		
		switch(objectCode){
		
		case POLYGON: {//0
			int n=Math.abs((int) parameters[0]); //no of edges
			double radius=getSceneManager().getSettings().inverseMapX(parameters[1]); //radius
			double theta0=parameters[2]*Math.PI/180; //offset angle in degrees
			PrimitiveElement3DPoly e=new PrimitiveElement3DPoly();
			
			for (int i = 0; i < n; i++) {
				double theta1 =theta0+ 2 * i * Math.PI / n;
				v1 = new Vector3D(radius * Math.cos(theta1), radius
						* Math.sin(theta1), 0);
				e.addPoint(v1);
			}
			Random random=new Random();
			setPolygonColor(0.2f+0.8f*random.nextFloat(),0.2f+0.8f*random.nextFloat(),0.2f+0.8f*random.nextFloat(),e);
			e.prefs().setBackColor(Color.gray);
			if(T!=null) e.transform(T);
			PrimitiveElement3D eTemp=clip.getClippedElement(e);
			if (null!=eTemp)surface3D.addElement(eTemp);
			break;
		}
				
		case PYRAMID:{//1
			int n=Math.abs((int) parameters[0]); //no of edges
			double radius=getSceneManager().getSettings().inverseMapX(parameters[1]); //radius
			double height=getSceneManager().getSettings().inverseMapZ(parameters[2]); //height
			Vector3D vertex=new Vector3D(0,0,height);
			for (int i = 0; i < n; i++) {
				double theta1 = 2 * i * Math.PI / n;
				v1 = new Vector3D(radius * Math.cos(theta1), radius
						* Math.sin(theta1), 0);
				double theta2 = 2 * (i + 1) * Math.PI / n;
				v2 = new Vector3D(radius * Math.cos(theta2), radius
						* Math.sin(theta2), 0);
				PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
				ep.addPoint(vertex.clone());
				ep.addPoint(v1);
				ep.addPoint(v2);
				setPolygonColor(i * 1.0f / n, 0.3f, i * 1.0f / n,ep);
				ep.prefs().setBackColor(Color.gray);
				if(T!=null) ep.transform(T);
				PrimitiveElement3D eTemp=clip.getClippedElement(ep);
				if (null!=eTemp)surface3D.addElement(eTemp);

				if (closed) {
					PrimitiveElement3DPoly ep1 = new PrimitiveElement3DPoly();
					ep1.addPoint(centre.clone());
					ep1.addPoint(v1);
					ep1.addPoint(v2);
					setPolygonColor(i * 1.0f / n, 0.3f, i * 1.0f / n,ep1);
					ep1.prefs().setBackColor(Color.gray);
					if(T!=null) ep1.transform(T);
					eTemp=clip.getClippedElement(ep1);
					if (null!=eTemp)surface3D.addElement(eTemp);
				}
			}
			break;
		}
		case PRISM: {//2
			int n=Math.abs((int) parameters[0]); //no of edges
			double radius=getSceneManager().getSettings().inverseMapX(parameters[1]); //radius
			double height=getSceneManager().getSettings().inverseMapZ(parameters[2]); //height
			Vector3D vertex=new Vector3D(0,0,height);
			for (int i = 0; i < n; i++) {
				Vector3D v3, v4;
				double theta1 = 2 * i * Math.PI / n;
				v1 = new Vector3D(radius * Math.cos(theta1), radius	* Math.sin(theta1), 0);
				v3 = new Vector3D(radius * Math.cos(theta1), radius	* Math.sin(theta1), height);
				double theta2 = 2 * (i + 1) * Math.PI / n;
				v2 = new Vector3D(radius * Math.cos(theta2), radius	* Math.sin(theta2), 0);
				v4 = new Vector3D(radius * Math.cos(theta2), radius	* Math.sin(theta2), height);

				PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
				ep.addPoint(v1);
				ep.addPoint(v2);
				ep.addPoint(v4);
				ep.addPoint(v3);
				setPolygonColor(i * 1.0f / n, 0.0f, i * 1.0f / n,ep);
				if(T!=null) ep.transform(T);
				PrimitiveElement3D eTemp=clip.getClippedElement(ep);
				if (null!=eTemp)surface3D.addElement(eTemp);

				if (closed) {
					PrimitiveElement3DPoly ep1 = new PrimitiveElement3DPoly();
					ep1.addPoint(vertex.clone());
					ep1.addPoint(v3);
					ep1.addPoint(v4);
					setPolygonColor((i+4.0f) * 1.0f / (n+4), 0.0f, (i+4.0f) * 1.0f / (n+4),ep1);
					if(T!=null) ep1.transform(T);
					eTemp=clip.getClippedElement(ep1);
					if (null!=eTemp)surface3D.addElement(eTemp);

					PrimitiveElement3DPoly ep2 = new PrimitiveElement3DPoly();
					ep2.addPoint(centre.clone());
					ep2.addPoint(v1);
					ep2.addPoint(v2);
					setPolygonColor((i+2) * 1.0f / (n+3), 0.0f, (i+2) * 1.0f / (n+3),ep2);
					if(T!=null) ep2.transform(T);
				    eTemp=clip.getClippedElement(ep2);
					if (null!=eTemp)surface3D.addElement(eTemp);
				}
			}	
			break;
		}
		
		case ELLIPSOID:{//3
			int n=15;//no of divisions
			double a=getSceneManager().getSettings().inverseMapX(parameters[0]); //no of edges
			double b=getSceneManager().getSettings().inverseMapY(parameters[1]); //radius
			double c=getSceneManager().getSettings().inverseMapZ(parameters[2]); //height
					
			for (int j=0; j<n;j++){
				double fi1=2 * j * Math.PI / n;
				double fi2=2 * (j+1) * Math.PI / n;
				
				for (int i = 0; i < n; i++) {
					double theta1 =  2*i * Math.PI / n;	
					double theta2 =  2*(i + 1) * Math.PI / n;
					Vector3D v3, v4;
					v1 = new Vector3D(a * Math.sin(theta1/2)*Math.cos(fi1),
							b* Math.sin(theta1/2)*Math.sin(fi1), c * Math.cos(theta1/2));
					
					v2 = new Vector3D(a * Math.sin(theta2/2)*Math.cos(fi1),
							b* Math.sin(theta2/2)*Math.sin(fi1), c * Math.cos(theta2/2));
					
					v3 = new Vector3D(a * Math.sin(theta2/2)*Math.cos(fi2),
							b* Math.sin(theta2/2)*Math.sin(fi2), c * Math.cos(theta2/2));
					
					v4 = new Vector3D(a * Math.sin(theta1/2)*Math.cos(fi2),
							b* Math.sin(theta1/2)*Math.sin(fi2), c * Math.cos(theta1/2));
					PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
					ep.addPoint(v1);
					ep.addPoint(v2);
					ep.addPoint(v3);
					ep.addPoint(v4);
					setPolygonColor(i * 1.0f / n, 0.3f, i * 1.0f / n,ep);
					ep.prefs().setSplittable(false);
					if(T!=null) ep.transform(T);
					PrimitiveElement3D eTemp=clip.getClippedElement(ep);
					if (null!=eTemp)surface3D.addElement(eTemp);

				}
			}
			break;
		}
			
		case TORUS: {//4
			
			
			double radiusR=getSceneManager().getSettings().inverseMapX(parameters[0]); //radius
			double radiusr=getSceneManager().getSettings().inverseMapX(2*parameters[1]); //height
			int n=Math.abs((int) parameters[2]); //no of division
		    if (n<3)n=3;//no of divisions of ring
		    int m=Math.max(10,2*n/3);//no of divisions
			for (int j=0; j<m;j++){
				double fi1=2 * j * Math.PI / m;
				double fi2=2 * (j+1) * Math.PI / m;
				
				for (int i = 0; i < n; i++) {
					double theta1 =  2*i * Math.PI / n;	
					double theta2 =  2*(i + 1) * Math.PI / n;
					Vector3D v3, v4;
					v1 = new Vector3D( Math.cos(theta1)*(radiusR+radiusr*Math.cos(fi1)/2),
							 Math.sin(theta1)*(radiusR+radiusr*Math.cos(fi1)/2), radiusr * Math.sin(fi1)/2);
					
					v2 = new Vector3D(  Math.cos(theta2)*(radiusR+radiusr*Math.cos(fi1)/2),
							 Math.sin(theta2)*(radiusR+radiusr*Math.cos(fi1)/2), radiusr * Math.sin(fi1)/2);
		
					v3 = new Vector3D( Math.cos(theta2)*(radiusR+radiusr*Math.cos(fi2)/2),
							 Math.sin(theta2)*(radiusR+radiusr*Math.cos(fi2)/2), radiusr * Math.sin(fi2)/2);

					v4 = new Vector3D( Math.cos(theta1)*(radiusR+radiusr*Math.cos(fi2)/2),
							 Math.sin(theta1)*(radiusR+radiusr*Math.cos(fi2)/2), radiusr * Math.sin(fi2)/2);
					
					PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
					ep.addPoint(v1);
					ep.addPoint(v2);
					ep.addPoint(v3);
					ep.addPoint(v4);
					setPolygonColor(i * 1.0f / n, 0.3f, i * 1.0f / n,ep);
					ep.prefs().setSplittable(false);
					if(T!=null) ep.transform(T);
					PrimitiveElement3D eTemp=clip.getClippedElement(ep);
					if (null!=eTemp)surface3D.addElement(eTemp);

				}
			}
			break;
		}
		
      case PIE: {//5
			
			double radius=getSceneManager().getSettings().inverseMapX(parameters[0]); //radius
			double angle1=parameters[1]*Math.PI/180;
			double angle2=parameters[2]*Math.PI/180;
			double height=radius/5;
			double angle=angle2-angle1;
			/*no of divisions: make sure that there are atleast 4 division)*/
			int n =(int) Math.max(4,angle*18/Math.PI);
			Vector3D vertex=new Vector3D(0,0,height);
			for (int i = 0; i < n; i++) {
				Vector3D v3, v4;
				double theta1 = angle1+ i*(angle2-angle1) / n;
				v1 = new Vector3D(radius * Math.cos(theta1), radius	* Math.sin(theta1), 0);
				v3 = new Vector3D(radius * Math.cos(theta1), radius	* Math.sin(theta1), height);
				double theta2 =  angle1+ (i+1)*(angle2-angle1) / n;
				v2 = new Vector3D(radius * Math.cos(theta2), radius	* Math.sin(theta2), 0);
				v4 = new Vector3D(radius * Math.cos(theta2), radius	* Math.sin(theta2), height);

				//Add curved surface polygon
				PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
				ep.addPoint(v1);
				ep.addPoint(v2);
				ep.addPoint(v4);
				ep.addPoint(v3);
				setPolygonColor((i+4.0f) * 1.0f / (n+4), 0.0f, (i+4.0f) * 1.0f / (n+4),ep);
				if(T!=null) ep.transform(T);
				PrimitiveElement3D eTemp=clip.getClippedElement(ep);
				if (null!=eTemp)surface3D.addElement(eTemp);


				//Add side Polygons
				if ((i == 0)) {
					PrimitiveElement3DPoly sidePoly = new PrimitiveElement3DPoly();
					sidePoly.addPoint(vertex.clone());
					sidePoly.addPoint(v3);
					sidePoly.addPoint(v1);
					sidePoly.addPoint(new Vector3D(0, 0, 0));
					setPolygonColor((i+4.0f) * 1.0f / (n+4), 0.0f, (i+4.0f) * 1.0f / (n+4),sidePoly);
					if(T!=null) sidePoly.transform(T);
					eTemp=clip.getClippedElement(sidePoly);
					if (null!=eTemp)surface3D.addElement(eTemp);

				}else if(i==n-1){
					PrimitiveElement3DPoly sidePoly = new PrimitiveElement3DPoly();
					sidePoly.addPoint(vertex.clone());
					sidePoly.addPoint(v4);
					sidePoly.addPoint(v2);
					sidePoly.addPoint(new Vector3D(0, 0, 0));
					setPolygonColor((i+4.0f) * 1.0f / (n+4), 0.0f, (i+4.0f) * 1.0f / (n+4),sidePoly);
					if(T!=null) sidePoly.transform(T);
					eTemp=clip.getClippedElement(sidePoly);
					if (null!=eTemp)surface3D.addElement(eTemp);

				}

				//Add top and bottom polygons
				if (closed) {
					PrimitiveElement3DPoly ep1 = new PrimitiveElement3DPoly();
					ep1.addPoint(vertex.clone());
					ep1.addPoint(v3);
					ep1.addPoint(v4);
					setPolygonColor((i+4.0f) * 1.0f / (n+4), 0.0f, (i+4.0f) * 1.0f / (n+4),ep1);
					if(T!=null) ep1.transform(T);
					eTemp=clip.getClippedElement(ep1);
					if (null!=eTemp)surface3D.addElement(eTemp);

					PrimitiveElement3DPoly ep2 = new PrimitiveElement3DPoly();
					ep2.addPoint(centre.clone());
					ep2.addPoint(v1);
					ep2.addPoint(v2);
					setPolygonColor((i+2) * 1.0f / (n+3), 0.0f, (i+2) * 1.0f / (n+3),ep2);
					if(T!=null) ep2.transform(T);
					eTemp=clip.getClippedElement(ep2);
					if (null!=eTemp)surface3D.addElement(eTemp);

				}
			}
			break;
			}
		}
		primitiveElement3D =surface3D;
		return (surface3D.primitiveElement3DS.size() > 0) ? surface3D : null;
	}

	
	public int getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(int objectCode) {
		this.objectCode = objectCode;
	}

	public int getColorMode() {
		return colorMode;
	}

	public void setColorMode(int colorMode) {
		this.colorMode = colorMode;
	}

	@Override
	public String getDefinition() {
		String str = getObjectName(objectCode);
		for (int i = 0; i < 3; i++) {
			str = str + "<br>" + paramNames[i] + ": " + parameters[i];
		}
		return str;
	}

	public double[] getParameters() {
		return parameters;
	}

	public void setParameters(double[] parameters) {
		this.parameters = parameters;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	/**Sets polygon color according to fillmode*/
	private void setPolygonColor(float a, float b,float c, PrimitiveElement3DPoly poly){
		int i=prefs().getFillmode();
		poly.setFilled(true);
		switch(i){
		case 0:
			poly.prefs().setFillColor(prefs().getFillColor());
			poly.prefs().setBackColor(prefs().getBackColor());
			break;
		case 1:
			poly.setFilled(false);
			break;
		case 2:
			poly.prefs().setFillColor(new Color(a, b, (a+b)/2.0f));
			poly.prefs().setBackColor(Color.gray);
			break;
		}
		poly.prefs().setLineColor(prefs().getLineColor());
		poly.prefs().setCurveWidth(prefs().getCurveWidth());
		poly.prefs().setDashed(prefs().isDashed());
	}
}

