package net.vpc.scholar.hadruplot.backends.calc3d.core;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DCurve;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DLine;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DLineSegment;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DParametricSurface;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DPlane;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DPoint;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DSurface;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DVector;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DVectorField;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3Dcartesian2D;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3Dimplicit2D;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DObject;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DPolygon;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3Dfunction;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3DImplicit;

public class commonUtils {
	


	/**Return ObjectCreatePanel according to the object in constructor*/
	public static String getobject3DName(Element3D element) {
		if (element instanceof Element3DPoint){
			return "Point3D";
		}else if (element instanceof Element3DVector){
			return "Vector3D";
		}else if (element instanceof Element3DLine){
			return "Line3D";
        }else if (element instanceof Element3DLineSegment){
        	return "LineSegment3D";
		}else if (element instanceof Element3DCurve){
			return "Curve3D";
        }else if (element instanceof Element3DPlane){
        	return "Plane3D";
		}else if (element instanceof Element3Dcartesian2D){
			return "Explicit Curve2D";
		}else if (element instanceof Element3Dimplicit2D){
			return "Implicit Curve2D";
        }else if (element instanceof Element3DSurface){
			return "Explicit Surface3D";
		}else if (element instanceof Element3DParametricSurface){
			return "Paramteric Surface3D";
		}else if (element instanceof Element3DObject){
			return ((Element3DObject)element).getObjectName(((Element3DObject)element).getObjectCode());
		}else if (element instanceof Element3DPolygon){
			return "Polygon 3D";
		}else if (element instanceof Element3DImplicit){
			return "Implicit Surface3D";
		}else if (element instanceof Element3DVectorField){
			return "3D Vector Field";
		}else{
			return "Element3D";
		}
	
	}
	
	public static String getobject3DInfo(Element3D element) {
		if (element instanceof Element3DPoint){
			return "Represents a point in 3D Space as (x,y,z) <br> Example: (1,2,3)";
		}else if (element instanceof Element3DVector){
			return "Represents Vector joining 2 points in 3D Space (x<su>2</su>-x1)i + (y2-y2)j + (z2-z1)k <br> Example: i + 2j + 3k";
		}else if (element instanceof Element3DLine){
			return "Represents Line joining 2 points in 3D Space (x-x1)/a = (y-y1)/b = (z-z1)/c <br> Example: x/2=y/1=(x+1)/0";
        }else if (element instanceof Element3DLineSegment){
        	return "Represents point in 3D Space (x,y,z) <br> Example: (1,2,3)";
		}else if (element instanceof Element3DCurve){
			return "Represents 3D Parametric curve in 3D Space x=f(t), y=g(t), z=h(t) <br> Example: Helix x=cos(t), y=sin(t), z=t/2";
        }else if (element instanceof Element3DPlane){
        	return "Represents plane in 3D Space of form ax+by+cz+d=0 <br> Example: 2x+y-3z+2=0";
		}else if (element instanceof Element3Dcartesian2D){
			return "Represents 2D cartesian curve in explicit form y = f(x) or r=f(t) <br> Example: sin(x*x)";
		}else if (element instanceof Element3Dimplicit2D){
			return "Represents 2D cartesian curve in implicit form f(x,y)=0 <br> Example: <br> x*x+4*y*y=4, r-3*sin(t)=0" ;
        }else if (element instanceof Element3DSurface){
			return "Represents 3D cartesian Surface in explicit form z = f(x,y) <br> Example: z=sin(x+y)";
		}else if (element instanceof Element3DParametricSurface){
			return "Represents 3D cartesian surface in parametric form x=f(u,v), y=f(u,v), z=f(u,v) <br> Example: Cylinder x=cos(u), y=sin(u), z=v";
		}else if (element instanceof Element3DObject){
			return "Represents 3D Premitives like Sphere Pyramid etc <br> Example: For Sphere, Choose Ellipsoid with parameters R,R,R";
		}else if (element instanceof Element3DPolygon){
			return "Represents 3D Polygon with atleast 3 distict coplanar vertices<br> in 3D Space";
		}else if (element instanceof Element3Dfunction){
			return "Represents 3D Function with parameters u and v both ranging in [0,1]<br>Example: x=cos(u), y=sin(v), z=v";
		}else if (element instanceof Element3DImplicit){
			return "Represents Implicit 3D  Cartesian, Spherical or Cylinderical Function <br> Exmpple: sin(x)+sin(y)+sin(z)=0";
		}else if (element instanceof Element3DVectorField){
			return "Represents Vector Field in 3D  Cartesian,  Example: x=sin(x); y=sin(y), z=(x+y)";

		}else{
			return "";
		}
	
	}
	
		
	public static String getobject3DInfoHTML(Element3D e){
		if(e==null)return "";
		String str;
		str="<html>";
		String temp=e.getName();
		str=str.concat("<b>Name:&nbsp&nbsp</b>");
		str=str.concat(temp +"<br><br>");
		temp=getobject3DName(e);
		str=str.concat("<b>Type:&nbsp&nbsp</b>");
		str=str.concat(temp +"<br><br>");
		
		/*
		temp=String.valueOf(e.isVisible());
		str=str.concat("<b>Visible:&nbsp&nbsp</b><br>");
		str=str.concat(temp +"<br>");
				
		temp=e.getFillColor().toString();
		str=str.concat("<b>FillColor:</b><br>");
		str=str.concat(temp +"<br><br>");
		temp=e.getLineColor().toString();
		str=str.concat("<b>LineColor:</b><br>");
		str=str.concat(temp +"<br>");
		*/
		
		temp=e.getDefinition();
		str=str.concat("<b>Definition:&nbsp&nbsp</b>");
		str=str.concat(temp +"<br><br>");
        str=str.concat("</html>");
       // str=("<html> <b>"+commonUtils.getobject3DName(e) +"</b><br>" +commonUtils.getobject3DInfo(e)+"</html>");
		return str;
	}
	
	
	public static boolean isCurve(Element3D e){
		if ((e instanceof Element3DLine)||(e instanceof Element3DVector)||(e instanceof Element3DCurve)||
				(e instanceof Element3Dcartesian2D)||(e instanceof Element3Dimplicit2D)) return true;
		else return false;
	}
}
