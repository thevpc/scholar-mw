

package net.vpc.scholar.hadruplot.backends.calc3d.math;

import java.io.Serializable;



/**
 * Structure to hold a set of 3D angles in radians.
 */
public final class Angles3D implements Serializable
{
	/**
	 * Pitch.
	 */
	public double iAngleX = 0;
	/**
	 * Yaw.
	 */
	public double iAngleY = 0;
	/**
	 * Roll.
	 */
	public double iAngleZ = 0;
	
	
	
	/**
	 * Constructor - initialises angles to 0.
	 */
	public Angles3D()
	{
		set(0,0,0);
	}

	
	/**
	 * Constructor.
	 * @param d the yaw angle.
	 * @param e the pitch angle.
	 * @param f the roll angle.
	 */
	public Angles3D(double d, double e, double f)
	{
		set(d, e, f);
	}
	
	
	
	/**
	 * Set the angle values.
	 * @param d the pitch angle.
	 * @param e the yaw angle.
	 * @param f the roll angle.
	 */
	public void set(double d, double e, double f)
	{
		iAngleX = d;
		iAngleY = e;
		iAngleZ = f;		
	}
	
	public Angles3D getAnglesinDegrees(){
		return new Angles3D(iAngleX*MathUtils.DEGREES_PER_RADIAN,iAngleY*MathUtils.DEGREES_PER_RADIAN,iAngleZ*MathUtils.DEGREES_PER_RADIAN);
	}
	
	
	/**
	 * Increase the X-axis (pitch) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incX(float aAmount, float aMaxAngleValue)
	{
		iAngleX = (iAngleX + aAmount) % aMaxAngleValue;
		if (0 > iAngleX)
		{
			iAngleX += aMaxAngleValue;
		}
	}
	
	
	
	/**
	 * Increase the Y-axis (yaw) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incY(float aAmount, float aMaxAngleValue)
	{
		iAngleY = (iAngleY + aAmount) % aMaxAngleValue;
		if (0 > iAngleY)
		{
			iAngleY += aMaxAngleValue;
		}
	}

	
	
	/**
	 * Increase the Z-axis (roll) angle by the given amount.
	 * 
	 * @param aAmount the amount to increase by. If negative then angle 
	 * value will be decreased.
	 * @param aMaxValue the maximum allowable final value. The final angle will 
	 * be wrapped as necessary such that its value falls between 0 and this 
	 * parameter. 
	 */
	public void incZ(float aAmount, float aMaxAngleValue)
	{
		iAngleZ = (iAngleZ + aAmount) % aMaxAngleValue;
		if (0 > iAngleZ)
		{
			iAngleZ += aMaxAngleValue;
		}
	}
	
	
	
	/**
	 * Multiply these angles by a given value.
	 * @param aVal the scalar value to multiply by.
	 * @return a non-null {@link Angles3D}.
	 */
	public Angles3D mult(float aVal)
	{
		return new Angles3D(iAngleX * aVal, iAngleY * aVal, iAngleZ * aVal);
	}
	
	
	
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder(24);
		buf.append("{");
		buf.append("a");
		buf.append(": ");
		buf.append(iAngleX);
		buf.append(", ");
		buf.append(iAngleY);
		buf.append(", ");
		buf.append(iAngleZ);
		buf.append("}");
		return buf.toString();		
	}

	public Angles3D copy() {
		return new Angles3D(iAngleX,iAngleY,iAngleZ);
	}
}



