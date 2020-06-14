
package net.vpc.scholar.hadruplot.libraries.calc3d.math;

import java.io.Serializable;



/**
 * Represents a quaternion. Quaternion has the capability to rotate a vector v.
 * <p><b>How to use:</b>
 * If we want to rotate a vector about axis(vector) u (say xi+yj+zk) by an angle θ then, the 
 * quaternion used for this transformation is
 * </p>
 * <pre>
 * q = cos(θ/2) + sin(θ/2)*u
 * or
 * q = cos(θ/2) + sin(θ/2)*(xi + yj + zk)
 *
 * <b><i>Right-handed system:</i></b>
 * vq' = q' * vq * q,   clockwise rotation
 * vq' = q  * vq * q',   counter-clockwise rotation
 * <b><i>Left-handed system:</i></b>
 * vq' = q  * vq * q',   clockwise rotation
 * vq' = q' * vq * q,   counter-clockwise rotation * 
 * 
 * where,
 * vq is a vector to be rotated, encoded in a quaternion with q0 = 0,
 * vq'is vector after rotation by angle and direction as specified by q,
 * </pre>
 */
public final class Quat implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7809018775714830562L;
	/**
	 * The real part.
	 */
	public float iReal = 0;
	/**
	 * The imaginary (xi,yi,ji) part.
	 */
	public Vector3D iVec = new Vector3D();
	
	
	// Temporary vectors for calculations
	private volatile Vector3D iTempVec1 = new Vector3D();
	private volatile Vector3D iTempVec2 = new Vector3D();
	
		
	
	/**
	 * Constructor - initialise this to default values.
	 */
	public Quat()
	{
		this(0, new Vector3D());
	}
	
	
	/**
	 * Constructor.
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 */
	public Quat(float aReal, Vector3D aVec)
	{
		set(aReal, aVec);
	}
	
	
	/**
	 * Constructor.
	 * @param aQuat the quaternion whose values to copy.
	 */
	public Quat(Quat aQuat)
	{
		set(aQuat);
	}	
	
	
	
	
	/**
	 * Constructor.
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 */
	private Quat(double aReal, Vector3D aVec)
	{
		this((float)aReal, aVec);
	}	
	
	
	
	/**
	 * Set this quaternion's value.
	 * 
	 * @param aReal the real part.
	 * @param aVec the imaginary part.
	 * @return this.
	 */
	public Quat set(float aReal, Vector3D aVec)
	{
		this.iReal = aReal;
		this.iVec.set(aVec);
		return this;
	}
	
	
	
	/**
	 * Set this quaternion's value.
	 * @param aQ the quaternion whose values to copy.
	 * @return this.
	 */
	public Quat set(Quat aQ)
	{
		return set(aQ.iReal, aQ.iVec);
	}	
	
	/**
	 * Get the length of this quaternion.
	 * @return a value >= 0.
	 */
    public float getLength()
    {
        return (float)Math.sqrt(iReal*iReal +iVec.getLengthSq());
    }
    
    
	/**
	 * Normalise this quaternion so that it becomes a unit quaternion whose 
	 * {@link #getLength()} equals 1. 
	 */
    public void setNormalised()
    {
        float len = getLength();
        if (0 == len)
        	return;
        iReal /= len;
        iVec.normalize();
    }

    
    /**
     * Calculate the conjugate of this quaternion.
     * 
     * @param aOrig the original quaternion. 
     * @param aResult will hold the result.
     */
    public static void calculateConjugate(Quat aOrig, Quat aResult)
    {
    	aResult.iReal = aOrig.iReal;
    	aResult.iVec=aOrig.iVec.scale(-1);
    }

    
    
    /**
     * Calculate the inverse of a quaternion.
     * 
     * @param aOrig the original quaternion. 
     * @param aResult will hold the result.
     */
    public static void calculateInverse(Quat aOrig, Quat aResult)
    {
    	calculateConjugate(aOrig, aResult);
        float len = aOrig.getLength();
        if (0.0 != len)
        {
        	aResult.multEq(1/len);
        }
    }	

    
    	
	/**
	 * Add another quaternion to this one.
	 * @param aQuat the quaternion to add to this one.
	 */
	public void plusEq(Quat aQuat)
	{
		iReal += aQuat.iReal;
		iVec=iVec.add(aQuat.iVec);
	}
	
	

	/**
	 * Subtract another quaternion from this one.
	 * 
	 * @param aQuat the quaternion to subtract from this one.
	 */
	public void minusEq(Quat aQuat)
	{
		iReal -= aQuat.iReal;
		iVec=iVec.subtract(aQuat.iVec);
	}	
	
	
	
		/**
	 * Multiply this quaternion by a scalar value.
	 * 
	 * @param aVal the scalar value to multiply this quaternion by.
	 */
	public void multEq(float aVal)
	{
		iReal *= aVal;
		iVec=iVec.scale(aVal);
	}
	
	
		
	/**
	 * Multiply this quaternion by another one.
	 * @param aQuat the quaternion to multiply with this one.
	 */
	public synchronized void multEq(Quat aQuat)
	{
		/*
		 * ret.qr = qr*other.qr - qv.dot(other.qv);
		 * ret.qv = other.qv*qr + qv*other.qr + qv.cross(other.qv);
		 */
			
		// real part
		final float newReal = (float) (iReal*aQuat.iReal - iVec.DotProduct(aQuat.iVec));

		// imaginary part
		iTempVec2.set(aQuat.iVec);
		iTempVec2.scaleEq(iReal);
		
		iTempVec1.set(iVec);
		iTempVec1.scaleEq(aQuat.iReal);
		iTempVec2.addEq(iTempVec1);
		
		iTempVec1=Vector3D.crossProduct(iVec, aQuat.iVec);
		iTempVec2.addEq(iTempVec1);
		
		
		this.iVec.set(iTempVec2);
		this.iReal = newReal;
	}	
	
 	
    /**
     * Get a rotation quaternion for the given 3D Euler angles(radians) around the
     * global coordinate axis system.
     * <code>
     * <pre>
     * If x=yaw, y=roll, z=pitch then
     * 
     * q<sub>x</sub>θ = cos(x/2) +  sin(y/2)*i + 0*j        + 0*k
	 * q<sub>y</sub>θ = cos(y/2) +  0*i        + sin(y/2)*j + 0*k
	 * q<sub>z</sub>θ = cos(z/2) +  0*i        + 0*j        + sin(zθ/2)*k
	 * 
	 * Now Combine these to get final qusternion
	 * q<sub>final</sub> = q<sub>xθ</sub> * q<sub>yθ</sub> * q<sub>zθ</sub>
	 * </pre>
     * </code>
     * @param aResult will hold the final quaternion.
     */
	public static void calculateEulerRotationQuat(double ya, double pitch , double roll, Quat aResult)
	{
		Quat qx = new Quat( Math.cos(ya*0.5), new Vector3D((float)Math.sin(ya*0.5),0,0) );
		Quat qy = new Quat( Math.cos(roll*0.5), new Vector3D(0,(float)Math.sin(roll*0.5),0) );
		Quat qz = new Quat( Math.cos(pitch*0.5), new Vector3D(0,0,(float)Math.sin(pitch*0.5)) );
		
		/*
		 * q = qz * qy * qx
		 */
		qz.multEq(qy);
		qz.multEq(qx);
		
		aResult.set(qz);
	}
	
		
    /**
     * Get a rotation quaternion for the given 3D Euler angles around the
     * given axis vectors.
     *
     * @param u a unit vector representing the x-axis.
     * @param v a unit vector representing the y-axis.
     * @param w a unit vector representing the z-axis.
     *  
     * @param aResult will hold the result.
     */
	public static void calculateEulerRotationQuat(double ya, double pitch , double roll, Vector3D u, Vector3D v, Vector3D w, Quat aResult)
	{
		Vector3D newU = new Vector3D(u);
		newU.scaleEq((float)Math.sin(ya*0.5));
		
		Vector3D newV = new Vector3D(v);
		newV.scaleEq((float)Math.sin(pitch * 0.5));

		Vector3D newW = new Vector3D(w);
		newW.scaleEq((float)Math.sin(roll * 0.5));

		//Quat to rotate by angle ya about x axis
		Quat qx = new Quat( Math.cos(ya * 0.5), newU );
		//Quat to rotate by angle pitch about y axis
		Quat qy = new Quat( Math.cos(pitch * 0.5), newV );
		//Quat to rotate by angle roll about z axis
		Quat qz = new Quat( Math.cos(roll * 0.5), newW );
		
		/*
		 * Combine the three quaternions
		 * q = qz * qy * qx
		 */
		qz.multEq(qy);
		qz.multEq(qx);
		
		aResult.set(qz);
	}	
	

    /**
     * Get a rotation quaternion for the given angle around the
     * given axis vector.
     *
     * @param theta .
     * @param axid unit vector representing the y-axis.
     * @param aResult will hold the result.
     */
	public static void calculateAxisRotationQuat(double theta, Vector3D axis, Quat aResult)
	{
		Vector3D newU = new Vector3D(axis);
		newU.scaleEq((float)Math.sin(theta*0.5));
		
		//Quat to rotate by angle theta about given axis
		Quat q = new Quat( Math.cos(theta * 0.5), newU );
				
		aResult.set(q);
	}	
	
}



