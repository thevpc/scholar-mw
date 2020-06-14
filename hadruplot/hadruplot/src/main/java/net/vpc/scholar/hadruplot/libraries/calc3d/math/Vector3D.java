package net.vpc.scholar.hadruplot.libraries.calc3d.math;

import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Point3D;

import java.io.Serializable;



/**
 * Define a vector in 3 Dimensions. Provides methods to compute cross product
 * and dot product, addition and subtraction of vectors.
 *
 * @author 
 */
public class Vector3D implements Constants, Serializable{

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
    protected double w=1;
    //testing for bsp
    public boolean edge=true;
    // ===================================================================
    // Constructors
    /**
     * creates a vector [0,0,0]
     *
     * @return null Vector
     */
    public Vector3D() {
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
    public Vector3D(double X, double Y, double Z) {
        x = X;
        y = Y;
        z = Z;
    }
    
    /**
     * Constructs a new <code>Vector</code> that is the copy of specified object
     *
     * @param v : the <code>Vector</code> object to copy
     */
    public Vector3D(Vector3D v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
        edge=v.edge;
    }

    public Vector3D(Point3D point) {
        this(point.getX(),point.getY(),point.getZ());
    }

    /**
     * construct a new vector between two points
     * @param point1 : initial vector
     * @param point2 : final vector
     */
    public Vector3D(Point3D point1,Point3D point2) {
        this(point2.getX()-point1.getX(), point2.getY()-point1.getY(), point2
                .getZ()
                -point1.getZ());
    }

    /**
     * construct a new vector between two points
     * @param point1 : initial vector
     * @param point2 : final vector
     */
    public Vector3D(Vector3D point1,Vector3D point2) {
        this(point2.getX()-point1.getX(), point2.getY()-point1.getY(), point2
                .getZ()
                -point1.getZ());
    }

    
    // ===================================================================
    // static methods
    /**
     * Computes the sum of the two vectors.
     * @param v1 : left vector
     * @param v1: right Vector
     * @return new vector representing sum of two vectors
     */
    public Vector3D addVectors(Vector3D v1, Vector3D v2) {
        return  new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    /**
     * Computes the difference of the two vectors.
     * @param v1 : left vector
     * @param v2 : right Vector
     * @return new vector obtained by subtracting right vector from left vector
     */
    public Vector3D subtractVectors(Vector3D v1, Vector3D v2) {
    	  return  new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }
    
    /**
     * Computes the dot product of the two vectors, defined by :
     * <p>
     * <code> x1*x2 + y1*y2 + z1*z2</code>
     * <p>
     * Dot product is zero if the vectors defined by the 2 vectors are
     * orthogonal. It is positive if vectors are in the same direction, and
     * negative if they are in opposite direction.
     */
    public final static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.getX()*v2.getX()+v1.getY()*v2.getY()+v1.getZ()*v2.getZ();
    }

    /**
     * Computes the cross product of the two vectors. Cross product is zero for
     * colinear vectors. It is positive if angle between vector 1 and vector 2
     * is comprised between 0 and PI, and negative otherwise.
     */
    public final static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.y*v2.z-v1.z*v2.y, v1.z*v2.x-v1.x*v2.z, v1.x*v2.y
                -v1.y*v2.x);
    }

    /**
     * test if the two vectors are collinear
     * @param v1 : first vector to be checked
     * @param v2 : second vector to be checked
     * @return true if the vectors are colinear
     */
    public final static boolean checkCollinear(Vector3D v1, Vector3D v2) {
        return Vector3D.crossProduct(v1, v2).getLengthSq()<EPSILON;
    }

    /**
     * test if the two vectors are identical
     * @param v1 : first vector to be checked
     * @param v2 : second vector to be checked
     * @return true if the vectors are colinear
     */
    public final static boolean checkCoincide(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1, v2).getLengthSq()<EPSILON;
    }

    
    
    
    /**
     * test if the two vectors are orthogonal
     * 
     * @return true if the vectors are orthogonal
     */
    public final static boolean checkOrthogonal(Vector3D v1, Vector3D v2) {
        return Vector3D.dotProduct(v1.getUnitVector(), v2
                .getUnitVector())<EPSILON;
    }

    /**
     * computes angle(in radian) between two vectors
     * 
     * @return angle in radians
     */
    public final static double calculateAngle(Vector3D v1, Vector3D v2) {
        return Math.acos(Vector3D.dotProduct(v1.getUnitVector(), v2.getUnitVector()));
    }  
    
    
    
    // ===================================================================
    // Instance Methods
    
    /**
     * returns a copy of this vector
     */
    public Vector3D clone() {
       return new Vector3D(this);
    }
  
    
    /**
     * sets the vector to new vector [X,Y,Z]
     * @param X taking this as x-coordinate
     * @param Y taking this as y-coordinate
     * @param Z taking this as z-coordinate
     */
    public void set(double X, double Y, double Z) {
        x = X;
        y = Y;
        z = Z;
    }
  
    /**
     * sets the vector to new vector 
     * @param V Vector to be set
    */
    public void set(Vector3D v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
    }

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
    
    /**
     * @return W-coordinate of vector
     */
    public double getW() {
        return this.w;
    }
    
    /**
     * set x-coordinate of vector
     */
    public void setX(double x) {
         this.x=x;
    }
    /**
     * set y-coordinate of vector
     */
    public void setY(double y) {
        this.y=y;
    }
    /**
     * set z-coordinate of vector
     */
    public void setZ(double z) {
        this.z=z;
    }
    
  
    /**
     * @return length of vector
     */
    public double getLength() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    /**
     * Computes the square of the norm of the vector. This avoids to compute the
     * square root.
     * 
     * @return the euclidean norm of the vector
     */
    public double getLengthSq() {
        return x*x+y*y+z*z;
    }
    /**
     * Normalise this vector
     */
    public void normalize() {
        Double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x = this.x / length;
        this.y = this.y / length;
        this.z = this.z / length;
    }

      
    /**
     * homgenise this vector4D to Vector 3D (divide x and y by w and then make w=1)
     */
    public void homogenise() {
        this.x = this.x / w;
        this.y = this.y / w;
        this.w = 1;
    }

    /**
     * Gives dot product of this vector with input vector     
     * @param V input Vector with which dot product is taken 
     * @return a scalar which corresponds to length of projection of the original vector on Vector "V"
     */
    public double DotProduct(Vector3D V) {
        return (this.x * V.x + this.y * V.y + this.z * V.z);
    }
    
    /**
     * Gives dot product of this vector with input vector 
     * @param V input Vector with which cross product is taken 
     * @return new vector representing  Cross product of instance vector with input vector
     */
    public Vector3D CrossProduct(Vector3D V) {
        return new Vector3D(this.y * V.z - this.z * V.y, -this.x * V.z + this.z * V.x, this.x * V.y - this.y * V.x);
    }
    
       
    /**
     * Get the result of multiplying this vector by a scalar value.
     * @param n scalar which is multiplied to this vector
     * @return new vector stretched this vector by n times
     */
    public Vector3D scale(double s) {
        return new Vector3D(this.x * s, this.y * s, this.z * s);
    }
    
       
    /**
     * Return the sum of current vector with vector given as parameter. Inner
     * fields are not modified.
     * @param V vector added to original vector
     * @return new vector obatained after addition of V 
     */
    public Vector3D add(Vector3D V) {
        return new Vector3D(this.x + V.x, this.y + V.y, this.z + V.z);
    }  
    
    /**
     * Get the difference of this vector and another vector.
     * @param V vector subtracted from original vector
     * @return a new vector after Subtraction of V from this 
     */
    public Vector3D subtract(Vector3D V) {
        return new Vector3D(this.x - V.x, this.y - V.y, this.z - V.z);
    }
    
    
    /**
     * Multiply this vector by a scalar value.
     * @param n scalar which is multiplied to the vector
     */
    public void scaleEq(double s) {
    	 this.x *= s;
         this.y *= s;
         this.z *= s;
    }
    
       
    /**
     * Add another vector to this vector.
     * @param V vector to be added
     */
    public void addEq(Vector3D V) {
    	 this.x += V.x;
         this.y += V.y;
         this.z += V.z;
    }  
    
    /**
     * Subtract another vector from this vector.
     * @param V vector to be subtracted
     */
    public void subtractEq(Vector3D V) {
        this.x -= V.x;
        this.y -= V.y;
        this.z -= V.z;
    }
    
    /**
     * reverses direction of this vector
     */
    public void negate() {
       this.x*=-1;
       this.y*=-1;
       this.z*=-1;
    }
    /**
     * checks if vector is perpendicular to given vector
     * @return true if vectors are perpendicular 
     * else false  
     */
    public boolean isPerpendicular_to(Vector3D V) {
        if (this.DotProduct(V) == 0) {
            return true;
        } else {
            return false;
        }
    }

    
    
    /**
     * checks if vector is parallel to given vector
     * @return true if vectors are parallel 
     *  else false  
     */
    public boolean isParallel_to(Vector3D V) {
        if (this.DotProduct(V) == this.getLength() * V.getLength()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * checks if vector is Collinear to given vector
     * @return true if vectors are Collinear
     *  else false  
     */
    public boolean isCollinearTo(Vector3D V) {
        if (Math.abs(this.DotProduct(V)) == this.getLength() * V.getLength()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Immutable function i.e. original vector is not modified instead a new modified vector is returned
     * @return a vector with length modified to unity but same directions
     */
    public Vector3D getUnitVector() {
        Double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new Vector3D(this.x / length, this.y / length, this.z / length);
   
    }
    
    /**
     * Returns the opposite vector v2 of this, such that the sum of this and v2
     * equals the null vector.
     * 
     * @return the vector opposite to <code>this</code>.
     */
    public Vector3D getOpposite() {
        return new Vector3D(-x, -y, -z);
    }

    /**
	 * Calculate the unit vector surface normal for the plane represented by the 3 
	 * given points (assuming clockwise order).
	 * 
	 * @param aVec1 the first point on the plane.
	 * @param aVec2 the second point on the plane.
	 * @param aVec3 the third point on the plane.
	 * @return aResultVector the vector to store the result in.
	 */
	public static Vector3D getSurfaceNormal(
			Vector3D Vec1, Vector3D Vec2, Vector3D Vec3)
	{
		
        // calculate normal for this polygon
		Vector3D edge1 = new Vector3D(Vec2);
		edge1.subtractEq(Vec1);
		
		Vector3D edge2 = new Vector3D(Vec3);
		edge2.subtractEq(Vec2);
		
		if (checkCollinear(edge1,edge2)) return null;
		return crossProduct(edge2, edge1).getUnitVector();

	}
    
	/**
	 * Returns true if this {@link Vector2} is the zero {@link Vector2}.
	 * @return boolean
	 */
	public boolean isZero() {
		return Math.abs(this.x) <= Epsilon.E && Math.abs(this.y) <= Epsilon.E && Math.abs(this.z) <= Epsilon.E;
	}
	
    /**
     * checks if vector is equal to given object
     * @param obj : object with which check is to be made
     * @return true if O is same is as original vector
     * else false 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3D))
            return false;

        Vector3D v = (Vector3D) obj;
        if (Math.abs(x-v.x)>EPSILON)
            return false;
        if (Math.abs(y-v.y)>EPSILON)
            return false;
        if (Math.abs(z-v.z)>EPSILON)
            return false;
        return true;
    }
    
    /**
     * @return Component of this vector parallel to given vector
     */
	public Vector3D Get_Parallel_Component(Vector3D dirVector3D) {
		return dirVector3D.scale(this.DotProduct(dirVector3D.getUnitVector()));
	}
	
	
	/**
	 * Calulates vector which divides v1 and v2 in 1:s
     * @return Vector dividing v1,v2 in 1:s
     */
	public static Vector3D intepolate(Vector3D v1,Vector3D v2,double s) {
		if (s==-1) return null;
		return (v1.scale(s).add(v2)).scale(1/(s+1));
	}
	
	
	/**
     * @return Component of this vector perpendicular to given vector
     */
	public Vector3D Get_Perpendicular_Component(Vector3D dirVector3D) {
		return this.subtract(Get_Parallel_Component(dirVector3D));
	}
	
	/**
     * @return [v1 v2 v3]
     */
    public static double scalarTripleProduct(Vector3D v1,Vector3D v2,Vector3D v3){
		return v1.DotProduct(v2.CrossProduct(v3));
    }
    
    /**
     * @return v1.v3(v2)-v1.v2(v3)
     */
      public static Vector3D vectorTripleProduct(Vector3D v1,Vector3D v2,Vector3D v3){
		Vector3D rv;
		rv=v3.scale(v1.DotProduct(v3));
		rv=rv.subtract(v2.scale(v1.DotProduct(v2)));
       	return rv;
    }
    
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
