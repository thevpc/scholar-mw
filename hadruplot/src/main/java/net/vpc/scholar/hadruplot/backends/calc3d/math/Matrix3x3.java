package net.vpc.scholar.hadruplot.backends.calc3d.math;

public class Matrix3x3
{
	public double m11;
	public double m12;
	public double m13;
	public double m21;
	public double m22;
	public double m23;
	public double m31;
	public double m32;
	public double m33;

	public Matrix3x3()
	{
		this.m11 = 0;
		this.m12 = 0;
		this.m13 = 0;
		this.m21 = 0;
		this.m22 = 0;
		this.m23 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 0;
	}
	public Matrix3x3(double m11, double m12, double m13, double m21, double m22, double m23, double m31, double m32, double m33)
	{
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}
	public static Matrix3x3 identity()
	{
		return new Matrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);
	}
	
	public static Matrix3x3 NullMatrix3x3()
	{
		return new Matrix3x3(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	// Copy a matrix3x3.
	public void  setTo( Matrix3x3 m)
	{
		this.m11=m.m11;this.m12=m.m12;this.m13=m.m13;
		this.m21=m.m21;this.m22=m.m22;this.m23=m.m23;
		this.m31=m.m31;this.m32=m.m32;this.m33=m.m33;
		
	}

	public static Matrix3x3 multiply(Matrix3x3 left, Matrix3x3 right)
	{
		Matrix3x3 returnvalue = new Matrix3x3();
		returnvalue.m11 = left.m11 * right.m11 + left.m12 * right.m21 + left.m13 * right.m31;
		returnvalue.m12 = left.m11 * right.m12 + left.m12 * right.m22 + left.m13 * right.m32;
		returnvalue.m13 = left.m11 * right.m13 + left.m12 * right.m23 + left.m13 * right.m33;
		returnvalue.m21 = left.m21 * right.m11 + left.m22 * right.m21 + left.m23 * right.m31;
		returnvalue.m22 = left.m21 * right.m12 + left.m22 * right.m22 + left.m23 * right.m32;
		returnvalue.m23 = left.m21 * right.m13 + left.m22 * right.m23 + left.m23 * right.m33;
		returnvalue.m31 = left.m31 * right.m11 + left.m32 * right.m21 + left.m33 * right.m31;
		returnvalue.m32 = left.m31 * right.m12 + left.m32 * right.m22 + left.m33 * right.m32;
		returnvalue.m33 = left.m31 * right.m13 + left.m32 * right.m23 + left.m33 * right.m33;
		return returnvalue;
	}

	public Matrix3x3 multiply( Matrix3x3 m)
	{
		Matrix3x3 returnvalue = new Matrix3x3();
		returnvalue.m11 = m.m11 * m.m11 + this.m12 * m.m21 + this.m13 * m.m31;
		returnvalue.m12 = this.m11 * m.m12 + this.m12 * m.m22 + this.m13 * m.m32;
		returnvalue.m13 = this.m11 * m.m13 + this.m12 * m.m23 + this.m13 * m.m33;
		returnvalue.m21 = this.m21 * m.m11 + this.m22 * m.m21 + this.m23 * m.m31;
		returnvalue.m22 = this.m21 * m.m12 + this.m22 * m.m22 + this.m23 * m.m32;
		returnvalue.m23 = this.m21 * m.m13 + this.m22 * m.m23 + this.m23 * m.m33;
		returnvalue.m31 = this.m31 * m.m11 + this.m32 * m.m21 + this.m33 * m.m31;
		returnvalue.m32 = this.m31 * m.m12 + this.m32 * m.m22 + this.m33 * m.m32;
		returnvalue.m33 = this.m31 * m.m13 + this.m32 * m.m23 + this.m33 * m.m33;
		return returnvalue;
	}

	
	public void multiplyTo( Matrix3x3 m)
	{
		this.setTo(this.multiply(m));
	}

	public Matrix3x3 scale(double factor)
	{
		Matrix3x3 returnvalue = new Matrix3x3();
		returnvalue.m11 = this.m11 * factor;
		returnvalue.m12 = this.m12 * factor;
		returnvalue.m13 = this.m13 * factor;
		returnvalue.m21 = this.m21 * factor;
		returnvalue.m22 = this.m22 * factor;
		returnvalue.m23 = this.m23 * factor;
		returnvalue.m31 = this.m31 * factor;
		returnvalue.m32 = this.m32 * factor;
		returnvalue.m33 = this.m33 * factor;
		return returnvalue;
	}
	
	public void scaleTo(double factor)
	{
		this.m11 = this.m11 * factor;
		this.m12 = this.m12 * factor;
		this.m13 = this.m13 * factor;
		this.m21 = this.m21 * factor;
		this.m22 = this.m22 * factor;
		this.m23 = this.m23 * factor;
		this.m31 = this.m31 * factor;
		this.m32 = this.m32 * factor;
		this.m33 = this.m33 * factor;
	}
	
	
	public  Matrix3x3 add(Matrix3x3 m)
	{
		Matrix3x3 returnvalue = new Matrix3x3();
		returnvalue.m11 = this.m11 + m.m11;
		returnvalue.m12 = this.m12 + m.m12;
		returnvalue.m13 = this.m13 + m.m13;
		returnvalue.m21 = this.m21 + m.m21;
		returnvalue.m22 = this.m22 + m.m22;
		returnvalue.m23 = this.m23 + m.m23;
		returnvalue.m31 = this.m31 + m.m31;
		returnvalue.m32 = this.m32 + m.m32;
		returnvalue.m33 = this.m33 + m.m33;
		return returnvalue;
	}
	
	public  void addTo(Matrix3x3 m)
	{
		this.m11 += m.m11;
		this.m12 += m.m12;
		this.m13 += m.m13;
		this.m21 += m.m21;
		this.m22 += m.m22;
		this.m23 += m.m23;
		this.m31 += m.m31;
		this.m32 += m.m32;
		this.m33 += m.m33;
	}
	
	public  Matrix3x3 subtract(Matrix3x3 m)
	{
		Matrix3x3 returnvalue = new Matrix3x3();
		returnvalue.m11 = this.m11 - m.m11;
		returnvalue.m12 = this.m12 - m.m12;
		returnvalue.m13 = this.m13 - m.m13;
		returnvalue.m21 = this.m21 - m.m21;
		returnvalue.m22 = this.m22 - m.m22;
		returnvalue.m23 = this.m23 - m.m23;
		returnvalue.m31 = this.m31 - m.m31;
		returnvalue.m32 = this.m32 - m.m32;
		returnvalue.m33 = this.m33 - m.m33;
		return returnvalue;
	}
	
	public void subtractTo(Matrix3x3 m)
	{
		this.m11 -= m.m11;
		this.m12 -= m.m12;
		this.m13 -= m.m13;
		this.m21 -= m.m21;
		this.m22 -= m.m22;
		this.m23 -= m.m23;
		this.m31 -= m.m31;
		this.m32 -= m.m32;
		this.m33 -= m.m33;
	}
	
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Matrix3x3)
		{
			return (this == (Matrix3x3)obj);
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(" | {0} {1} {2} |" + "\n"+ m11 + m12 + m13);
		builder.append(" | {0} {1} {2} |" + "\n"+ m21 + m22 + m23);
		builder.append(String.format(" | %1$s %2$s %3$s |"+ m31+ m32+ m33));
		return builder.toString();
	}
	
	public final void CopyFrom(double[] array, int index)
	{
		m11 = array[index];
		m12 = array[index + 1];
		m13 = array[index + 2];
		m21 = array[index + 3];
		m22 = array[index + 4];
		m23 = array[index + 5];
		m31 = array[index + 6];
		m32 = array[index + 7];
		m33 = array[index + 8];
	}

	public final double getDeterminant()
	{
		double cofactor00 = m22 * m33 - m23 * m32;
		double cofactor10 = m23 * m31 - m21 * m33;
		double cofactor20 = m21 * m32 - m22 * m31;
		double returnvalue = m11 * cofactor00 + m12 * cofactor10 + m13 * cofactor20;
		return returnvalue;
	}
	
	public final Matrix3x3 getTranspose()
	{
		return new Matrix3x3(m11, m21, m31, m12, m22, m32, m13, m23, m33);
	}
	
	public final Matrix3x3 getAdjoint()
	{
		Matrix3x3 rv = new Matrix3x3();
		rv.m11 = m22 * m33 - m23 * m32;
		rv.m21 = m23 * m31 - m21 * m33;
		rv.m31 = m21 * m32 - m22 * m31;
		rv.m12 = m31 * m23 - m22 * m33;
		rv.m22 = m11 * m33 - m31 * m13;
		rv.m32 = m21 * m13 - m22 * m11;
		rv.m13 = m32 * m21 - m22 * m31;
		rv.m23 = m31 * m12 - m32 * m11;
		rv.m33 = m11 * m22 - m21 * m12;
		return rv.getTranspose();
	}
	
	public final Matrix3x3 getInverse()
	{
		return this.getTranspose().scale(1/(this.getDeterminant()));
	}
}