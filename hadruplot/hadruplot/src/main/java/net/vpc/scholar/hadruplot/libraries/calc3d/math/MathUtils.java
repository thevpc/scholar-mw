package net.vpc.scholar.hadruplot.libraries.calc3d.math;

import java.util.Random;

public class MathUtils {
	public static final double PI = 3.14159265358979;
	public static final double HALF_PI = Math.PI / 2;
	public static final double e = 2.71;
	public static final double RADIANS_PER_DEGREE = (double)(PI / 180.0);
	public static final double DEGREES_PER_RADIAN = (double)(180.0F / PI);
	
	public static final int UNIQUE_SOLUTION=0;
	public static final int NO_SOLUTION=1;
	public static final int INFINITE_SOLUTION=2;
	
	public final double Max(double val1, double val2)
	{
			if (val1 > val2)
			{
				return val1;
			}
			if (Double.isNaN(val1))
			{
				return val1;
			}
			return val2;
		}
		public final double Min(double val1, double val2)
		{
			if (val1 < val2)
			{
				return val1;
			}
			if (Double.isNaN(val1))
			{
				return val1;
			}
			return val2;
		}
		public final boolean FloatEqual(double a, double b)
		{
			return FloatEqual(a, b, 0.00001F);
		}
		public final boolean FloatEqual(double a, double b, double tolerance)
		{
			if (Math.abs(b - a) <= tolerance)
			{
				return true;
			}
			return false;
		}
		public final double DegreesToRadians(double degrees)
		{
			return degrees * RADIANS_PER_DEGREE;
		}
		public final double RadiansToDegrees(double radians)
		{
			return radians * DEGREES_PER_RADIAN;
		}
		
		
	/**Solves 2 linear eqn of form ax+by+c=0*/
		public final static int SolveLenear2(double a1, double b1, double c1, double a2, double b2, double c2, Vector3D rv)
		{
			Matrix2x2 m = null;
			Matrix2x2 XM = null;
			Matrix2x2 YM = null;
			c1=-c1;
			c2=-c2;
			m = new Matrix2x2(a1, b1, a2, b2);
			XM = new Matrix2x2(c1, b1, c2, b2);
			YM = new Matrix2x2(a1, c1, a2, c2);
			if (m.getDeterminant() == 0)
			{
				if (XM.getDeterminant() == 0 && YM.getDeterminant() == 0)
				{
					return INFINITE_SOLUTION;
				}
				else
				{
					return NO_SOLUTION;
				}
			}

			rv.set(XM.getDeterminant() / m.getDeterminant(), YM.getDeterminant() / m.getDeterminant(),0);
			return UNIQUE_SOLUTION;

		}

		
		/**Solves 3 linear eqn of form ax+by+cz+d=0*/
		public final static int SolveLenear3(double a1, double b1, double c1, double d1, double a2, double b2, double c2, double d2, double a3, double b3, double c3, double d3, Vector3D rv)
		{
			Matrix3x3 M = null;
			Matrix3x3 XM = null;
			Matrix3x3 YM = null;
			Matrix3x3 ZM = null;
			d1=-d1;
			d2=-d2;
			d3=-d3;
			M = new Matrix3x3(a1, b1, c1, a2, b2, c2, a3, b3, c3);
			XM = new Matrix3x3(d1, b1, c1, d2, b2, c2, d3, b3, c3);
			YM = new Matrix3x3(a1, d1, c1, a2, d2, c2, a3, d3, c3);
			ZM = new Matrix3x3(a1, b1, d1, a2, b2, d2, a3, b3, d3);

			if (M.getDeterminant() == 0)
			{
				if (XM.getDeterminant() == 0 && YM.getDeterminant() == 0 && ZM.getDeterminant() == 0)
				{
					return INFINITE_SOLUTION;
				}
				else
				{
					return NO_SOLUTION;
				}
			}

			rv.set(XM.getDeterminant() / M.getDeterminant(),YM.getDeterminant() / M.getDeterminant(),ZM.getDeterminant() / M.getDeterminant());
			return UNIQUE_SOLUTION;

		}
		
		
		public final double RangeRandom(double min, double max)
		{
			Random r=new Random();
			return min+(max - min) *r.nextDouble();
		}
		
		
		public static boolean isValidNumber(double d)
		{
			if (Double.isInfinite(d)||Double.isNaN(d))
			return false;
			return true;
			
		}
		
}




