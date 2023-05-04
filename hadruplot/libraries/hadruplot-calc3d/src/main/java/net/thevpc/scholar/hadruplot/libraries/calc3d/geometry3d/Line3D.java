package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import java.io.Serializable;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.MathUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class Line3D implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1943241076215743682L;
	public Vector3D Pt3D; //Point through which line passes
	public Vector3D DirVector3D; //Direction vector or line


	public Line3D(Vector3D Pt1, Vector3D Pt2) // 2 point form
	{
		Pt3D = Pt1;
		DirVector3D = Pt1.subtract(Pt2);
	}

	public static Line3D xAxis()
	{
		return new Line3D(new Vector3D(), new Vector3D(1, 0, 0));
	}
	public static Line3D yAxis()
	{
		return new Line3D(new Vector3D(), new Vector3D(0, 1, 0));
	}
	public static Line3D zAxis()
	{
		return new Line3D(new Vector3D(), new Vector3D(0, 0, 1));
	}
	//Checks if point line sin line or not
	public final boolean ContainsPt(Vector3D Pt)
	{
		if (DirVector3D.isCollinearTo(new Vector3D(Pt3D, Pt)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//checks if line is perpendicular to given vector or not
	public final boolean Is_Perpendicularto(Vector3D direction)
	{
		if (Vector3D.dotProduct(direction, DirVector3D) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**returns point on the line for parametric value t
	 * Line has equation r=a+tb where b is unit vector parallel to line
	 */
	public Vector3D getParametricPoint(double t){
		return Pt3D.add(DirVector3D.scale(t));
	}
	
	//checks if line is perpendicular to given Line or not
	public final boolean Is_Perpendicularto(Line3D line)
	{
		return Is_Perpendicularto(line.DirVector3D);
	}
	//checks if line is parallel to given vector or not 
	public final boolean Is_Parallelto(Vector3D direction)
	{
		return this.DirVector3D.isCollinearTo(direction);
	}
	//checks if line is parallel to given line or not 
	public final boolean Is_Parallelto(Line3D line)
	{
		return this.DirVector3D.isCollinearTo(line.DirVector3D);
	}
	//gives Foot Of perpendicular of point on the line 
	public final Vector3D get_FootOfPt(Vector3D Pt)
	{
		Vector3D n = null;
		if (Pt3D.equals(Pt))
		{
			return Pt3D;
		}
		//find vector joining points
		n = new Vector3D(this.Pt3D, Pt);
		//get projection of vector along the line
		n = n.Get_Parallel_Component(this.DirVector3D);
		//add projection vector in PV of point on line to get foot and return it as point3dtype
		return Pt3D.add(n);
	}
	public final Vector3D get_ImageOfPt(Vector3D Pt)
	{
		//ptA.....footofA.....ImageA
		return Vector3D.intepolate(Pt,this.get_FootOfPt(Pt), -0.05d);
	}
	
	public final float get_DistanceOfPt(Vector3D Pt)
	{
		Vector3D n = null;
		//find vector joining points
		n = new Vector3D(this.Pt3D, Pt);
		//get perpendicular projection of vector along the line
		n = n.Get_Perpendicular_Component(this.DirVector3D);
		return (float)(n.getLength());
	}
	//check if lines are same
	public final boolean Is_identicalto(Line3D line)
	{
		//[n1,n1,(p1-p2)] =0 then coplanar
		if (this.Is_Parallelto(line) && this.ContainsPt(line.Pt3D))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//check if lines are coplanar
	public final boolean Is_Coplanarto(Line3D line)
	{
		//[n1,n1,(p1-p2)] =0 then coplanar
		if (Vector3D.scalarTripleProduct(this.DirVector3D, line.DirVector3D, new Vector3D(this.Pt3D, line.Pt3D)) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//check if lines are coplanar
	public final boolean Is_Skewto(Line3D line)
	{
		//[n1,n1,(p1-p2)] <>0 then Skew
		if (Vector3D.scalarTripleProduct(this.DirVector3D, line.DirVector3D, new Vector3D(this.Pt3D, line.Pt3D)) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//check if lines are intersecting at unique point
	public final boolean Is_Intersectingto(Line3D line)
	{
		//[n1,n1,(p1-p2)] <>0  and lines must not be parallel===> then intersecting
		if (this.Is_Coplanarto(line) & ! (this.Is_Parallelto(line)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//returns point of intersection
	//return 0: disjoint
	//       1: unique point
	//       2: lines are coincident
	public final int get_IntersectionPoint(Line3D line, Vector3D P_return)
	{
		//[n1,n1,(p1-p2)] <>0  and lines must not be parallel===> then intersecting
		Double Lembda = 0d;
		Double Mu = 0d;
		Vector3D v = null;
		if (this.Is_Coplanarto(line) == false)
		{
			return 0; //Line cant meet they are skew
		}
		else
		{
			if (this.Is_Parallelto(line))
			{
				if (this.ContainsPt(line.Pt3D))
				{
					return 2; //Lines are coincident
				}
				else
				{
					return 0; //Lines are parallel but not coincident
				}
			}
			else //Lines are non parallel coplananr so must meet
			{
				//Solve Lenear2 to get point of intersection
				Vector3D result=new Vector3D();
				MathUtils.SolveLenear2(this.DirVector3D.getX(), -line.DirVector3D.getX(), line.Pt3D.getX() - this.Pt3D.getX(), this.DirVector3D.getY(), -line.DirVector3D.getY(), line.Pt3D.getY() - this.Pt3D.getY(), result);
				v = this.DirVector3D.scale(result.getX()).add(this.Pt3D);
				P_return = v;
				return 1;
			}
		}

	}

	//Finds Shorest distance of intersection
	//Logic : For skew lines   AB.(n1xn2)
	//                        ----------
	//                         |n1xn2|
	public final float get_DistanceOfline(Line3D line)
	{
		//[n1,n1,(p1-p2)] <>0  and lines must not be parallel===> then intersecting
		//Lines ..Identical==>distance=0
		//Lines...prallel ===>Project AB perpendicular to any line
		//Line....Skew  =====>Project line calong common normal of lines
		Vector3D n1xn2 = null;
		Vector3D AB = null;
		if (this.Is_Skewto(line))
		{
			n1xn2 = this.DirVector3D.CrossProduct(line.DirVector3D);
			AB = new Vector3D(this.Pt3D, line.Pt3D);
			AB = AB.Get_Parallel_Component(n1xn2);
			return (float)(AB.getLength());
		}
		else if (this.Is_identicalto(line))
		{
			return 0F;
		}
		else //lines are non intersecting parallel
		{
			AB = new Vector3D(this.Pt3D, line.Pt3D);
			return (float)(AB.Get_Perpendicular_Component(this.DirVector3D).getLength());
		}
	}

	//returns angle of lines
	public final float get_AngleWithline(Line3D line)
	{
		return (float)(Vector3D.calculateAngle(this.DirVector3D, line.DirVector3D));
	}

	//return Equation of line in symmetrical form
	//   (x-x1)/a=(y-y1)/b=(z-z1)/c
	@Override
	public String toString()
	{
		double c = 0F;
		String temp = null;
		String s = null;
		temp = "";
		c = -this.Pt3D.getX();
		if (c != 0)
		{
			temp = ((c > 0) ? "+" + (new Float(c)).toString() : (new Float(c)).toString());
		}
		s = ((c == 0) ? "x/" : "(x" + temp + ")/") + this.DirVector3D.getX() + " = ";
		temp = "";
		c = -this.Pt3D.getY();
		if (c != 0)
		{
			temp = ((c > 0) ? "+" + (new Float(c)).toString() : (new Float(c)).toString());
		}
		s = s + ((c == 0) ? "y/" : "(y" + temp + ")/") + this.DirVector3D.getY() + " = ";
		temp = "";
		c = -this.Pt3D.getZ();
		if (c != 0)
		{
			temp = ((c > 0) ? "+" + (new Float(c)).toString() : (new Float(c)).toString());
		}
		s = s + ((c == 0) ? "z/" : "(z" + temp + ")/") + this.DirVector3D.getZ();
		return s;
	}
}
