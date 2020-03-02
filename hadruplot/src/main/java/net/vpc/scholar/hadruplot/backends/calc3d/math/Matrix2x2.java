package net.vpc.scholar.hadruplot.backends.calc3d.math;

public class Matrix2x2
{
	public double m11;
	public double m12;
	public double m21;
	public double m22;

	public Matrix2x2()
	{
		this.m11 = 1;
		this.m12 = 0;
		this.m21 = 0;
		this.m22 = 1;
	}
	public Matrix2x2(double m11, double m12, double m21, double m22)
	{
		this.m11 = m11;
		this.m12 = m12;
		this.m21 = m21;
		this.m22 = m22;
	}

	public Matrix2x2(Matrix2x2 m)
	{
		this.m11=m.m11; this.m12=m.m12;this.m21=m.m21;this.m22=m.m22;
	}
	
	public static Matrix2x2 identity()
	{
		return new Matrix2x2(1, 0, 1, 0);
	}
	

	public static Matrix2x2 multiply(Matrix2x2 left, Matrix2x2 right)
	{
		Matrix2x2 returnvalue = new Matrix2x2();
		returnvalue.m11 = left.m11 * right.m11 + left.m12 * right.m21;
		returnvalue.m12 = left.m11 * right.m12 + left.m12 * right.m22;
		returnvalue.m21 = left.m21 * right.m11 + left.m22 * right.m21;
		returnvalue.m22 = left.m21 * right.m12 + left.m22 * right.m22;
		return returnvalue;
	}
	
	public void setTo(Matrix2x2 m)
	{
		this.m11=m.m11; this.m12=m.m12;this.m21=m.m21;this.m22=m.m22;
	}
	
	public  Matrix2x2 scale(double factor)
	{
		Matrix2x2 returnvalue = new Matrix2x2();
		returnvalue.m11 = this.m11 * factor;
		returnvalue.m12 = this.m12 * factor;
		returnvalue.m21 = this.m21 * factor;
		returnvalue.m22 = this.m22 * factor;
		return returnvalue;
	}
	
	
	public  void scaleTo(double factor)
	{
		this.m11 *= factor;
		this.m12 *= factor;
		this.m21 *= factor;
		this.m22 *= factor;
	}
	
	public static Matrix2x2 add(Matrix2x2 left, Matrix2x2 right)
	{
		Matrix2x2 returnvalue = new Matrix2x2();
		returnvalue.m11 = left.m11 + right.m11;
		returnvalue.m12 = left.m12 + right.m12;
		returnvalue.m21 = left.m21 + right.m21;
		returnvalue.m22 = left.m22 + right.m22;
		return returnvalue;
	}
	
	public static Matrix2x2 subtraction(Matrix2x2 left, Matrix2x2 right)
	{
		Matrix2x2 returnvalue = new Matrix2x2();
		returnvalue.m11 = left.m11 - right.m11;
		returnvalue.m12 = left.m12 - right.m12;
		returnvalue.m21 = left.m21 - right.m21;
		returnvalue.m22 = left.m22 - right.m22;
		return returnvalue;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Matrix2x2)
		{
			Matrix2x2 m=(Matrix2x2)obj;
			if ((this.m11==m.m11) & (this.m12==m.m12) & (this.m21==m.m21)&(this.m22==m.m22)) return true;
			return false;
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
		builder.append(" | {0} {1} |" + "\n" + m11 + m12);
		builder.append(" | {0} {1} |" + "\n" + m21 + m22);
		return builder.toString();
	}
	
	
	public final double getDeterminant()
	{
		return m11 * m22 - m12 * m21;
	}

	public final Matrix2x2 getTranspose()
	{
		return new Matrix2x2(m11, m21, m12, m22);
	}
	
	public final Matrix2x2 getAdjoint()
	{
		return new Matrix2x2(this.m22, -this.m21, -this.m12, this.m22);

	}
	
	public final Matrix2x2 getInverse()
	{
		return (new Matrix2x2(this.m22, -this.m21, -this.m12, this.m22)).scale(1 / this.getDeterminant());
	}

}