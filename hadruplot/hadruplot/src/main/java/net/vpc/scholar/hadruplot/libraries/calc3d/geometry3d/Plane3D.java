package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.io.Serializable;
import java.util.ArrayList;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.MathUtils;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * Class to represent hyperplane in 3D, whose implecit function is f(x, y, z) =
 * Ax + By + Cz + D = 0 This class is neeeded for polygon clipppping (in BSP)
 * etc
 * 
 * @author mahesh
 * 
 */
public class Plane3D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4308026071657209210L;
	public static final int BACK = -1;
	public static final int COLLINEAR = 0;
	public static final int FRONT = 1;
	public static final int SPANNING = 2;
	private float a, b, c, d;
	private Vector3D normal;

	/**
	 * Constructor of plane with given coefficients in implicit form ax + by +
	 * cz + d = 0
	 * 
	 * @param a
	 *            Coefficient of x
	 * @param b
	 *            Coefficient of y
	 * @param c
	 *            Coefficient of z
	 * @param d
	 *            Constant term
	 */
	public Plane3D(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;	
		normal = new Vector3D(a, b, c).getUnitVector();
	}

	public Plane3D(double a, double b, double c, double d) {
		
		this.a = (float) a;
		this.b = (float) b;
		this.c = (float) c;
		this.d = (float) d;
		normal = new Vector3D(a, b, c).getUnitVector();
	}
	
	/**
	 * Constructor of plane passing through 3 nonCollinear points in 3D Space
	 */
	public Plane3D(Vector3D v1, Vector3D v2, Vector3D v3) {
		normal = Vector3D.getSurfaceNormal(v1, v2, v3);
		a=(float) normal.getX();
		b=(float) normal.getY();
		c=(float) normal.getZ();
		d=-(float) normal.DotProduct(v1);
	}

	/**
	 * Constructor of plane passing through given point and perpendicular to
	 * given normal
	 */
	public Plane3D(Vector3D pt, Vector3D normal) {
		this.normal = normal.getUnitVector();
		a=(float) normal.getX();
		b=(float) normal.getY();
		c=(float) normal.getZ();
		d=-(float) normal.DotProduct(pt);
	}

	public static Plane3D XYplane() {
		return new Plane3D(new Vector3D(0,0,0), new Vector3D(0, 0, 1));
	}

	public static Plane3D YZplane() {
		return new Plane3D(new Vector3D(0,0,0), new Vector3D(1, 0, 0));
	}

	public static Plane3D XZplane() {
		return new Plane3D(new Vector3D(0,0,0), new Vector3D(0, 1, 0));
	}
	
	/** Sets the plane to another plane having specified coefficents a, b,c,d */
	public void set(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		normal = new Vector3D(a, b, c).getUnitVector();
	}

	/** Sets the plane to another specified plane */
	public void set(Plane3D p) {
		this.a = p.get_a();
		this.b = p.get_b();
		this.c = p.get_c();
		this.d = p.get_d();
		normal = new Vector3D(a, b, c).getUnitVector();
	}

	/* getters and setters */
	public float get_a() {
		return a;
	}

	public void set_a(float a) {
		this.a = a;
	}

	public float get_b() {
		return b;
	}

	public void set_b(float b) {
		this.b = b;
	}

	public float get_c() {
		return c;
	}

	public void set_c(float c) {
		this.c = c;
	}

	public float get_d() {
		return d;
	}

	public void set_d(float d) {
		this.d = d;
	}

	public Vector3D getNormal() {
		return new Vector3D(a, b, c);
	}

	/**
	 * Normalizes the normal of this line (make the normal's length 1).
	 */
	public void normalize() {
		float length = (float) Math.sqrt(a * a + b * b + c * c);
		a /= length;
		b /= length;
		c /= length;
	}

	/** Calculate power of point (x,y,z) with respect to plane */
	public double f(double x, double y, double z) {
		return a * x + b * y + c * z + d;
	}

	/** Calculate power of point (x,y,z) with respect to plane */
	public double f(Vector3D pt) {
		return a * pt.getX() + b * pt.getY() + c * pt.getZ() + d;
	}

	/**
	 * Gets the side of this line the specified point is on. Because of floating
	 * point inaccuracy, a collinear line will be rare. For this to work
	 * correctly, the normal of this line must be normalized, either by setting
	 * this line to a polygon or by calling normalize(). Returns either FRONT,
	 * BACK, or COLLINEAR.
	 */
	public int getSideThin(double e, double f, double g) {
		// dot product between vector to the point and the normal
		double side = a * e + b * f + c * g + d;
		return (side < 0) ? BACK : (side > 0) ? FRONT : COLLINEAR;
	}

	
	@SuppressWarnings("unused")
	private int getSideThin(Vector3D v) {
		return getSideThin(v.getX(),v.getY(),v.getZ());
	}

	
	/**
	 * Gets the side of this line the specified point is on. This method treats
	 * the line as 1-unit thick, so points within this 1-unit border are
	 * considered collinear. For this to work correctly, the normal of this line
	 * must be normalized, either by setting this line to a polygon or by
	 * calling normalize(). Returns either FRONT, BACK, or COLLINEAR. The
	 * getSideThin() method treats the line normally, while getSideThick()
	 * treats the line as being 1 unit thick. Both methods return either FRONT,
	 * BACK, or COLLINEAR.
	 */
	public int getSideThick(double x, double y, double z) {
		int frontSide = getSideThin(x - a / 100, y - b / 100, z - c /100);
		//return  getSideThin(x,y,z);
		
		if (frontSide == FRONT) {
			return FRONT;
		} else if (frontSide == BACK) {
			int backSide = getSideThin(x + a / 100, y + b / 100, z + c / 100);
			if (backSide == BACK) {
				return BACK;
			}
		}
		return COLLINEAR;
		
	}
	
	public int getSideThick(Vector3D v) {
		return getSideThick(v.getX(),v.getY(),v.getZ());
	}
	
		
	/**
	 * Gets the side of this line that the specified polygon is on. Returns
	 * either FRONT, BACK, COLLINEAR, or SPANNING.
	 */
	public int getSide(ArrayList<Vector3D> poly) {
		boolean onFront = false;
		boolean onBack = false;

		// check every point
		for (int i = 0; i < poly.size(); i++) {
			Vector3D v = poly.get(i);
			int side = getSideThick(v.getX(), v.getY(), v.getZ());
			if (side == FRONT) {
				onFront = true;
			} else if (side == BACK) {
				onBack = true;
			}
		}

		// classify the polygon
		if (onFront && onBack) {
			return SPANNING;
		} else if (onFront) {
			return FRONT;
		} else if (onBack) {
			return BACK;
		} else {
			return COLLINEAR;
		}
	}

	
	/**
	 * Gets the side of the primitiveElement3D wrt this plane. Returns
	 * either FRONT, BACK, COLLINEAR, or SPANNING.
	 */
	public int getSide(PrimitiveElement3D primitiveElement3D) {
		if (primitiveElement3D instanceof PrimitiveElement3DString) {
			return (f(primitiveElement3D.centre) >= 0) ? FRONT : BACK;
		} else if (primitiveElement3D instanceof PrimitiveElement3DCurve) {
			PrimitiveElement3DCurve e = (PrimitiveElement3DCurve) primitiveElement3D;
			ArrayList<Vector3D> list = new ArrayList<Vector3D>();
			list.add(e.p1);
			list.add(e.p2);
			return getSide(list);
		}else if (primitiveElement3D instanceof PrimitiveElement3DArrow){
			PrimitiveElement3DArrow e = (PrimitiveElement3DArrow) primitiveElement3D;
			ArrayList<Vector3D> list = new ArrayList<Vector3D>();
			list.add(e.p1);
			list.add(e.p2);
			return getSide(list);
			
		} else if (primitiveElement3D instanceof PrimitiveElement3DPoly) {
			return getSide(((PrimitiveElement3DPoly) primitiveElement3D).vertices);
		} else {
			return (f(primitiveElement3D.centre) >= 0) ? FRONT : BACK;
		}
	}

	/**
	 * Calculate point of intersection of line joining 2 vectors to plane
	 * 
	 * @return point lying on intersection of line and plane (internal of
	 *         external division) returns null if line is parallel to the plane
	 * */

	public Vector3D getIntersection(Vector3D v1, Vector3D v2) {
		// check if line is not parallel to vector
		double t;
		Vector3D v2minusv1 = v2.subtract(v1);
		t = normal.DotProduct(v2minusv1);
		// if (Math.abs(t)<10e-20) return null;
		t = (-d - normal.DotProduct(v1)) / t;
		Vector3D rv=v1.add(v2minusv1.scale(t));
		rv.edge=false;
		return rv;
	}

	/**
	 * Splits a Ploygon to two polygons (if plane intersects it).
	 * <p>
	 * <b>Sutherland-Hodgman polygon clipping algorithm: clipping a polygon
	 * against a plane:</b> Simple algorithm: go around all your points
	 * determining if they are ''in front'' or ''behind'' the plane. If the
	 * previous point tested was on the opposite side of the plane, you have to
	 * work out the intersection point (a simple ray/plane intersection). This
	 * will leave you with two sets of points - those on one side of the plane
	 * and those on the other.
	 * </p>
	 * 
	 * @param poly
	 *            Polygon to be splitted by this plane
	 * @param frontPoly
	 *            polygon to store front part of splitted polygon
	 * @param backPoly
	 *            polygon to store back part of splitted polygon
	 * @return true if polygon is splitted
	 */
	public boolean splitPoly(ArrayList<Vector3D> poly,
			ArrayList<Vector3D> front, ArrayList<Vector3D> back) {
		int tr = 0;
		for (Vector3D vec : poly) {
			if (f(vec) >= 0)
				tr++;
			if (f(vec) <= 0)
				tr--;
		}
		if (Math.abs(tr) == poly.size())
			return false;

		Vector3D s = poly.get(poly.size() - 1);
		double fs = f(s);
		for (int i = 0; i < poly.size(); i++) {
			Vector3D p = poly.get(i);
			double fp = f(p);
			if (fs < 0) {
				if (fp < 0)
					back.add(p); /* case 1 */
				else {
					Vector3D vi = this.getIntersection(s, p); /* case 2 */
					back.add(vi);
					front.add(vi);
					p.edge=true;
					front.add(p);
				}
			} else {
				
				if (fp >= 0){
					if (fp==0) p.edge=false;
					front.add(p); /* case 3 */
				}
				else {
					Vector3D vi = this.getIntersection(s, p); /* case 4 */
					back.add(vi);
					back.add(p);
					front.add(vi);
				}
			}
			s = p;
			fs = fp;
		}
		if ((front.size() + back.size() >= 6))
			return true;
		return false;
	}
	
	
	public boolean getIntersectionLine_withPoly(ArrayList<Vector3D> poly,
			Vector3D v1, Vector3D v2) {
		boolean firstPointObtained=false,secondPointObtained=false;
		int tr = 0;
		for (Vector3D vec : poly) {
			if (f(vec) >= 0)
				tr++;
			if (f(vec) <= 0)
				tr--;
		}
		if (Math.abs(tr) == poly.size())
			return false;

		Vector3D s = poly.get(poly.size() - 1);
		double fs = f(s);
		for (int i = 0; i < poly.size(); i++) {
			Vector3D p = poly.get(i);
			double fp = f(p);
			if (fs < 0) {
				if (fp > 0){
					v1.set(this.getIntersection(s, p)); /* case 2 */
				    firstPointObtained =true;
				}
			} else {
				if (fp < 0){
			    	v2.set(this.getIntersection(s, p)); /* case 4 */
				    secondPointObtained=true;
				}
			}
			s = p;
			fs = fp;
		}
		
		if (firstPointObtained && secondPointObtained)
			return true;
		return false;
	}
	
	
	
	
	
	
	public final boolean ContainsPoint(Vector3D pt) {
		return (f(pt) == 0);
	}
	
	public final boolean ContainsLine(Line3D Line) {
		return (f(Line.Pt3D) == 0)
				&& this.normal.isPerpendicular_to(Line.DirVector3D);
	}

	/**get plane normal to given planes and passing through given pt.*/
	public final double get_DistanceOfPt(Vector3D pt)
	{
		Vector3D ptonPlane=new Vector3D(-d,0,0);
		return Math.abs(normal.DotProduct(pt.subtract(ptonPlane)));
	}
	
	/** returns angle this plane makes with given plane*/
	public final double get_Angle(Plane3D Plane) {
		return Vector3D.calculateAngle(this.normal, Plane.normal);
	}
	
	/** returns angle this plane makes with given vector*/
	public final double get_Angle(Vector3D dir) {
		return Vector3D.calculateAngle(this.normal, dir);
	}

	/** returns angle this plane makes with given line3D*/
	public final double get_Angle(Line3D line) {
		return Vector3D.calculateAngle(this.normal, line.DirVector3D);
	}
		
	// Returns 0: if projection line does not exits (i.e. line is perpendicualr
	// to plane)
	// 1: if projection line exits
	public final int get_ProjectionOfLine(Line3D line,	Line3D L_return) {
		// if line is parallel to or conatianed in plane its projection will be
		// the line itself
		if (line.Is_Perpendicularto(this.normal)) {
			L_return = line;
			return 1;
		}
		// If line is perpendicular to plane its projection will not be a line
		// it will be a point
		if (line.Is_Parallelto(this.normal)) {
			return 0;
		}
		Vector3D pt1;
		Vector3D pt2=new Vector3D();
		// check if pt1 already lies in plane if yes choose another point on
		// line
		pt1 = ((this.ContainsPoint(line.Pt3D)) ? line.Pt3D.add(line.DirVector3D): line.Pt3D);
		this.get_IntersectionPt(line, pt2);
		L_return = new Line3D(pt1, this.get_FootOfPt(pt2));
		return 1;
	}

	
	public final Line3D get_imageOfLine(Line3D line)
	{
			// If Plane1.DirVector3D.IsCollinearto(Plane2.DirVector3D) Then Exit Sub
		 Vector3D pt1 = null;
		 Vector3D pt2 = null;
			pt1 = ((this.ContainsPoint(line.Pt3D)) ? line.Pt3D.add(line.DirVector3D) : line.Pt3D);
			this.get_IntersectionPt(line, pt2);
			return new Line3D(pt2, this.get_ImageOfPt(pt1));
	}
	
	
	public final Vector3D get_FootOfPt(Vector3D pt)
	{
		//choose arbitrary point on plane
		Vector3D point=new Vector3D(-d,0,0);	
		Vector3D n = null;
			//find vector joiing pt and point on plane 
			n = new Vector3D(pt,point );
			//get  projection of  this vector along normal vector of plane
			n = pt.add(n.Get_Parallel_Component(this.normal));
			return n;
	}

	public final Vector3D get_ImageOfPt(Vector3D pt)
	{
			Vector3D n = null;
			Vector3D point=new Vector3D(-d,0,0);	
			//find vector joiing pt and point on plane 
			n = new Vector3D(pt,point );
			//get  projection of  this vector along normal vector of plane
			n = pt.add(n.Get_Parallel_Component(this.normal).scale(2));
			return n;
	}

		//returns point of intersection
		//Returns 0: if line is parallel to the plane Image does not exist
		//        1: if Line cuts plane at unique point
		//        2: if Line lies in plane
	public final int get_IntersectionPt(Line3D line, Vector3D P_return)
		{
			//if line.dirvector3d.isperpendicularto(plane.dirvector3d) then exit sub
			if (line.Is_Parallelto(this.normal))
			{
				return 0;
			}
			if (this.ContainsLine(line))
			{
				return 2;
			}
			P_return.set(getIntersection(line.Pt3D,line.Pt3D.add(line.DirVector3D)));
			return 1;
		}
	
		
		//check if planes are Identical
		public final boolean Is_identicalto(Plane3D P)
		{
			if (this.ContainsPoint(new Vector3D(-P.d,0,0)) & this.normal.isCollinearTo(P.normal))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		//check if planes are Parallel
		public final boolean Is_parallellto(Plane3D P)
		{
			if (this.normal.isCollinearTo(P.normal))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		//check if planes are perpendicualr
		public final boolean Is_Perpendicualrto(Plane3D P)
		{
			if (this.normal.isPerpendicular_to(P.normal))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		//get plane parallel to given plane and passing through given pt.
		public final Plane3D get_parallelPlane(Vector3D pt)
		{
			return new Plane3D(pt, this.normal);
		}
		
		//returns point of intersection of planes of form ax+by+cz=d
		//Returns 0: If plane do not meet
		//        1: If planes meet at unique line
		//        2: If planes are coincident
		public final int get_IntersectionLine(Plane3D plane, Line3D L_return)
		{

			if (this.Is_identicalto(plane))
			{
				return 2; //infinite lines
			}
			else if (this.Is_parallellto(plane))
			{
				return 0; //Planes do not meet
			}
			else //Planes are meeting in a line
			{
				Vector3D n1xn2 = null;
				Vector3D pt = null;
				Vector3D resultIntersection=new Vector3D();
				n1xn2 = this.normal.CrossProduct(plane.normal);
				
				if (MathUtils.UNIQUE_SOLUTION==MathUtils.SolveLenear2(a,b, d, plane.a, plane.b, plane.d, resultIntersection))
				{
					pt=new Vector3D(resultIntersection);
				}
				else
				{
					MathUtils.SolveLenear2(a, c, d, plane.a, plane.c, plane.d,resultIntersection);
					pt=new Vector3D(resultIntersection.getX(),0,resultIntersection.getY());
				}
			
				L_return.Pt3D=pt;
				L_return.DirVector3D=n1xn2;
			    return 1;
			}
		}

	
	//Returns eqn of plane in cartesian form
		@Override
		public String toString()
		{
			float c = 0F;

			String s = null;
			s = "";
			c = a;
			if (c != 0)
			{
				s = ((Math.abs(c) == 1) ? "x" : (new Float(c)).toString() + "x");
			}
			c = b;
			if (c != 0)
			{
				s = s + ((c > 0) ? ((s.equals("")) ? "" : "+") : "-") + ((Math.abs(c) == 1) ? "y" : Math.abs(c) + "y");
			}
			c = this.c;
			if (c != 0)
			{
				s = s + ((c > 0) ? ((s.equals("")) ? "" : "+") : "-") + ((Math.abs(c) == 1) ? "z" : Math.abs(c) + "z");
			}
			c =d;
			if (c != 0)
			{
				s = s + ((c > 0) ? "+" : "-") + Math.abs(c);
			}
			s = s + "=0";
			// builder.AppendFormat("{0}x+{1}y+{2}z-{3}=0", IIf(Me.DirVector3D.x = 1, "", Me.DirVector3D.x.ToString), IIf(Me.DirVector3D.y = 1, "", Me.DirVector3D.y.ToString), Me.DirVector3D.z, c)
			return s; //builder.ToString()
		}
	
	
	
}
