package net.vpc.scholar.hadruplot.libraries.calc3d.vpc;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.Constants;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.io.Serializable;


/**
 * Define a vector in 3 Dimensions. Provides methods to compute cross product
 * and dot product, addition and subtraction of vectors.
 *
 * @author 
 */
public class Point3D implements Constants, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7345555118646387159L;
	// ===================================================================
    // class variables
    /**
     * coordinates of the vector
     */
    protected double x;
    protected double y;
    protected double z;
    // ===================================================================
    // Constructors
    /**
     * creates a vector [0,0,0]
     *
     * @return null Vector
     */
    public Point3D() {
        x = 0;
        y = 0;
        z = 0;
    }

     /**
     * creating a vector
     *
     * @param X taking this as x-coordinate
     * @param Y taking this as y-coordinate
     * @param Z taking this as z-coordinate
     */
    public Point3D(double X, double Y, double Z) {
        x = X;
        y = Y;
        z = Z;
    }

    /**
     * Constructs a new <code>Vector</code> that is the copy of specified object
     *
     * @param v : the <code>Vector</code> object to copy
     */
    public Point3D(Point3D v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
    }

    public Point3D(Vector3D v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
    }


    
    // ===================================================================
    // Instance Methods
    
    /**
     * returns a copy of this vector
     */
    public Point3D clone() {
       return new Point3D(this);
    }

    public Vector3D toVector() {
       return new Vector3D(this);
    }

    
//    /**
//     * sets the vector to new vector [X,Y,Z]
//     * @param X taking this as x-coordinate
//     * @param Y taking this as y-coordinate
//     * @param Z taking this as z-coordinate
//     */
//    public void set(double X, double Y, double Z) {
//        x = X;
//        y = Y;
//        z = Z;
//    }
//
//
//    public void set(Point3D v) {
//        x = v.getX();
//        y = v.getY();
//        z = v.getZ();
//    }

    /**
     * @return x-coordinate of vector
     */
    public double getX() {
        return this.x;
    }
    /**
     * @return y-coordinate of vector
     */
    public double getY() {
        return this.y;
    }
    /**
     * @return z-coordinate of vector
     */
    public double getZ() {
        return this.z;
    }
    
//
//    /**
//     * set x-coordinate of vector
//     */
//    public void setX(double x) {
//         this.x=x;
//    }
//    /**
//     * set y-coordinate of vector
//     */
//    public void setY(double y) {
//        this.y=y;
//    }
//    /**
//     * set z-coordinate of vector
//     */
//    public void setZ(double z) {
//        this.z=z;
//    }
//
  

    /**
     * Get the result of multiplying this vector by a scalar value.
     * @return new vector stretched this vector by n times
     */
    public Point3D scale(double s) {
        return new Point3D(this.x * s, this.y * s, this.z * s);
    }
    
       
    /**
     * Return the sum of current vector with vector given as parameter. Inner
     * fields are not modified.
     * @param V vector added to original vector
     * @return new vector obatained after addition of V 
     */
    public Point3D add(Point3D V) {
        return new Point3D(this.x + V.x, this.y + V.y, this.z + V.z);
    }  
    
    public Point3D add(Vector3D V) {
        return new Point3D(this.x + V.getX(), this.y + V.getY(), this.z + V.getZ());
    }

    /**
     * Get the difference of this vector and another vector.
     * @param V vector subtracted from original vector
     * @return a new vector after Subtraction of V from this 
     */
    public Point3D subtract(Point3D V) {
        return new Point3D(this.x - V.x, this.y - V.y, this.z - V.z);
    }
    
    
//    /**
//     * Multiply this vector by a scalar value.
//     * @param n scalar which is multiplied to the vector
//     */
//    public void scaleEq(double s) {
//    	 this.x *= s;
//         this.y *= s;
//         this.z *= s;
//    }
//
//
//    /**
//     * Add another vector to this vector.
//     * @param V vector to be added
//     */
//    public void addEq(Point3D V) {
//    	 this.x += V.x;
//         this.y += V.y;
//         this.z += V.z;
//    }
//
//    /**
//     * Subtract another vector from this vector.
//     * @param V vector to be subtracted
//     */
//    public void subtractEq(Point3D V) {
//        this.x -= V.x;
//        this.y -= V.y;
//        this.z -= V.z;
//    }
//
//    /**
//     * reverses direction of this vector
//     */
//    public void negate() {
//       this.x*=-1;
//       this.y*=-1;
//       this.z*=-1;
//    }
//    /**
//     * checks if vector is perpendicular to given vector
//     * @return true if vectors are perpendicular
//     * else false
//     */
//    public boolean isPerpendicular_to(Point3D V) {
//        if (this.DotProduct(V) == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    
    
//    /**
//     * checks if vector is parallel to given vector
//     * @return true if vectors are parallel
//     *  else false
//     */
//    public boolean isParallel_to(Point3D V) {
//        if (this.DotProduct(V) == this.getLength() * V.getLength()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * checks if vector is Collinear to given vector
//     * @return true if vectors are Collinear
//     *  else false
//     */
//    public boolean isCollinearTo(Point3D V) {
//        if (Math.abs(this.DotProduct(V)) == this.getLength() * V.getLength()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    
//    /**
//     * Immutable function i.e. original vector is not modified instead a new modified vector is returned
//     * @return a vector with length modified to unity but same directions
//     */
//    public Point3D getUnitVector() {
//        Double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
//        return new Point3D(this.x / length, this.y / length, this.z / length);
//
//    }
    
//    /**
//     * Returns the opposite vector v2 of this, such that the sum of this and v2
//     * equals the null vector.
//     *
//     * @return the vector opposite to <code>this</code>.
//     */
//    public Point3D getOpposite() {
//        return new Point3D(-x, -y, -z);
//    }

//    /**
//	 * Calculate the unit vector surface normal for the plane represented by the 3
//	 * given points (assuming clockwise order).
//	 *
//	 * @param aVec1 the first point on the plane.
//	 * @param aVec2 the second point on the plane.
//	 * @param aVec3 the third point on the plane.
//	 * @return aResultVector the vector to store the result in.
//	 */
//	public static Point3D getSurfaceNormal(
//            Point3D Vec1, Point3D Vec2, Point3D Vec3)
//	{
//
//        // calculate normal for this polygon
//		Point3D edge1 = new Point3D(Vec2);
//		edge1.subtractEq(Vec1);
//
//		Point3D edge2 = new Point3D(Vec3);
//		edge2.subtractEq(Vec2);
//
//		if (checkCollinear(edge1,edge2)) return null;
//		return crossProduct(edge2, edge1).getUnitVector();
//
//	}
//
//	/**
//	 * Returns true if this {@link Vector2} is the zero {@link Vector2}.
//	 * @return boolean
//	 */
//	public boolean isZero() {
//		return Math.abs(this.x) <= Epsilon.E && Math.abs(this.y) <= Epsilon.E && Math.abs(this.z) <= Epsilon.E;
//	}
	
    /**
     * checks if vector is equal to given object
     * @param obj : object with which check is to be made
     * @return true if O is same is as original vector
     * else false 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point3D))
            return false;

        Point3D v = (Point3D) obj;
        if (Math.abs(x-v.x)>EPSILON)
            return false;
        if (Math.abs(y-v.y)>EPSILON)
            return false;
        if (Math.abs(z-v.z)>EPSILON)
            return false;
        return true;
    }
    
//    /**
//     * @return Component of this vector parallel to given vector
//     */
//	public Point3D Get_Parallel_Component(Point3D dirVector3D) {
//		return dirVector3D.scale(this.DotProduct(dirVector3D.getUnitVector()));
//	}
	
	
	/**
	 * Calulates vector which divides v1 and v2 in 1:s
     * @return Vector dividing v1,v2 in 1:s
     */
	public static Point3D intepolate(Point3D v1, Point3D v2, double s) {
		if (s==-1) return null;
		return (v1.scale(s).add(v2)).scale(1/(s+1));
	}
	
	
//	/**
//     * @return Component of this vector perpendicular to given vector
//     */
//	public Point3D Get_Perpendicular_Component(Point3D dirVector3D) {
//		return this.subtract(Get_Parallel_Component(dirVector3D));
//	}
//
//	/**
//     * @return [v1 v2 v3]
//     */
//    public static double scalarTripleProduct(Point3D v1, Point3D v2, Point3D v3){
//		return v1.DotProduct(v2.CrossProduct(v3));
//    }
//
//    /**
//     * @return v1.v3(v2)-v1.v2(v3)
//     */
//      public static Point3D vectorTripleProduct(Point3D v1, Point3D v2, Point3D v3){
//		Point3D rv;
//		rv=v3.scale(v1.DotProduct(v3));
//		rv=rv.subtract(v2.scale(v1.DotProduct(v2)));
//       	return rv;
//    }
    
    /**
     * @return vector representation as in e.g. for Vector(2,3,4) => "2i +3j +4k" 
     */
    @Override
    public String toString(){
    	double c = 0d;

		String s = null;
		s = "";
		c = x;
		if (c != 0)
		{
			s = ((Math.abs(c) == 1) ? "i" : (new Float(c)).toString() + "i");
		}
		c = y;
		if (c != 0)
		{
			s = s + ((c > 0) ? ((s.equals("")) ? "" : "+") : "-") + ((Math.abs(c) == 1) ? "j" : Math.abs(c) + "j");
		}
		c = z;
		if (c != 0)
		{
			s = s + ((c > 0) ? ((s.equals("")) ? "" : "+") : "-") + ((Math.abs(c) == 1) ? "k" : Math.abs(c) + "k");
		}
		return s;
    }

    /**
     * returns cartesian representation of vector (x,y,z)
     * @return
     */
   public String getPointText(){
	   return "("+ x + "," + y + "," + z + ")";
   }
}
