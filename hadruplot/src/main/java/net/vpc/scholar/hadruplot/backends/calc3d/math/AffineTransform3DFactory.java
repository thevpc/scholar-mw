/**
 * 
 */
package net.vpc.scholar.hadruplot.backends.calc3d.math;

/**
 * @author mahesh
 *
 */
public final class AffineTransform3DFactory {
	/**Vectors-based matrix rotation, rotate from a free point to another free point (arbitrary rotation or planar rotation).
	 * @param VecFrom
	 * @param VecTo
	 * @return
	 */
	 
	//matrix to reflect about XY plane
		public static AffineTransform3D matrixReflectXY()
		{
			//          | 1   0   0   0 |
			//          | 0   1   0   0 |
			//[x,y,z,1] | 0   0   -1  1 |=[x, y, -z, 1]
			//          | 0   0   0   1 |
			//            
			return AffineTransform3D.getScaleInstance(1, 1, -1);
		}

		//matrix to reflect about YZ plane
		public static AffineTransform3D matrixReflectYZ()
		{
			//          | -1  0   0   0 |
			//          | 0   1   0   0 |
			//[x,y,z,1] | 0   0   1   1 |=[-x, y, z, 1]
			//          | 0   0   0   1 |
			//            
			 return AffineTransform3D.getScaleInstance(-1, 1, 1);
		}

		//matrix to reflect about XZ plane
		public static AffineTransform3D matrixReflectXZ()
		{
			//          | 1   0   0   0 |
			//          | 0   -1  0   0 |
			//[x,y,z,1] | 0   0   1   1 |=[x, -y, z, 1]
			//          | 0   0   0   1 |
			//            
			return AffineTransform3D.getScaleInstance(1, -1, 1);
		}
		
		
		/**
		 * matrix to rotate pt about z axis such that the given point lies in xYplane
		 * @param pt
		 * @return
		 */
		 //
		public static AffineTransform3D matrixRotateIntoXY(Vector3D pt)
		{
			/*
			Gives vector that can rotate point(x,y,z) about x axis(we can do same about y axis also) such that it lieas in XY plane
			          |  cos   sin   0   0 | |  x/d   -y/d  0   0 |
			          | -sin   cos   0   0 | |  y/d   x/d   0   0 |
			[x,y,z,1] |   0     0    1   0 |=|   0     0    1   0 | = [d, 0, z, 1]
			          |   0     0    0   1 | |   0     0    0   1 |
			where d=sqrt(x^2+y^2)
			Note that legth of radius vector of point remains same.
			Its projection in xy plane is rotated about z axis till it lies in xz plane.
			*/
			AffineTransform3D rv = new AffineTransform3D();
			float d = 0F;
			if (pt.z == 0) //pt is already in Xyplane
			{
				return rv;
			}
			d = (float) Math.sqrt(pt.y*pt.y + pt.z*pt.z);
			rv = AffineTransform3D.getRotateX(Math.acos(pt.y / d));
			return rv;
			
					
		}
		
		 /**matrix to rotate pt about z axis such that the point lies in xZplane
		  * 
		  * @param pt
		  * @return
		  */
		public static AffineTransform3D matrixRotateIntoXZ(Vector3D pt)
		{
			/*Givaes vector that can rotate point(x,y,z) about z axis(we can do same about x axis also) such that it lieas in XZ plane
			          |  cos   sin   0   0 | |  x/d   -y/d  0   0 |
			          | -sin   cos   0   0 | |  y/d   x/d   0   0 |
			[x,y,z,1] |   0     0    1   0 |=|   0     0    1   0 | = [d, 0, z, 1]
			          |   0     0    0   1 | |   0     0    0   1 |
			where d=sqrt(y^2+z^2)
			Note that legth of radius vector of point remains same.
			Its orojection in xy plane is rotated about z axis till it lies in xz plane.
			*/
			AffineTransform3D rv = new AffineTransform3D();
			float d = 0F;
			if (pt.y == 0) //pt is already in XZplane
			{
				return rv;
			}
			d = (float) Math.sqrt((Math.pow(pt.x, 2)) + (Math.pow(pt.y, 2)));
			rv.M11 = pt.x / d;
			rv.M12 = -pt.y / d;
			rv.M21 = pt.y / d;
			rv.M22 = pt.x / d;
			return rv;
		}
		
		/**matrix to rotate pt about z axis such that the point lies in YZplane
		 * 
		 * @param pt
		 * @return
		 */
		public static AffineTransform3D matrixRotateIntoYZ(Vector3D pt)
		{
			/*Givaes vector that can rotate point(x,y,z) about z axis(we can do same about x axis also) such that it lieas in YZ plane
			          |  cos   sin   0   0 | |  y/d   x/d   0   0 |
			          | -sin   cos   0   0 | | -x/d   y/d   0   0 |
			[x,y,z,1] |   0     0    1   0 |=|   0     0    1   0 | = [d, 0, z, 1]
			          |   0     0    0   1 | |   0     0    0   1 |
			where d=sqrt(x^2+y^2)
			Note that legth of radius vector of point remains same.
			Its orojection in xy plane is rotated about z axis till it lies in xz plane.
			*/
			AffineTransform3D rv = new AffineTransform3D();
			float d = 0F;
			if (pt.x == 0) //pt is already in YZplane
			{
				return rv;
			}
			d = (float) Math.sqrt((Math.pow(pt.x, 2)) + (Math.pow(pt.y, 2)));
			rv.M11 = pt.y / d;
			rv.M12 = pt.x / d;
			rv.M21 = -pt.x / d;
			rv.M22 = pt.y / d;
			return rv;
		}
		
		
		 /**matrix to scale Keeping given Point fixed [Zooming considering given point as origin]
		  * 
		  * @param Sx
		  * @param Sy
		  * @param Sz
		  * @param Pt
		  * @return
		  */
		public static AffineTransform3D matrixScale3Dat(double Sx, double Sy, double Sz, Vector3D Pt)
		{
			//1: Translate the point(centre of object ) to origin)===> pt*T
			//2: Scale point by given factor                      ===> pt*(T*S)
			//3: translate the centre back to initial position    ===> pt*(T*S)*T-inv
			AffineTransform3D T = null;
			AffineTransform3D S = null;
			AffineTransform3D T_inv = null;
			T = AffineTransform3D.getTranslateInstance(-Pt.x, -Pt.y, -Pt.z);
			S = AffineTransform3D.getScaleInstance(Sx, Sy, Sz);
			T_inv = AffineTransform3D.getTranslateInstance(Pt.x, Pt.y, Pt.z);
			T.concatenate(S);
			T.concatenate(T_inv);
			return T;
		}
		
		/** Vectors-based matrix rotation, rotate from a free point to another free point (arbitrary rotation or planar rotation).
		 * 
		 * @param VecFrom
		 * @param VecTo
		 * @return
		 */
		public static AffineTransform3D matrixRotationByVectors(Vector3D VecFrom, Vector3D VecTo)
		{
			//This is very important function in this program, because we can use it for:
			// - Orienting the cameras
			// - Making the Primitives (the most of them)
			AffineTransform3D rv = new AffineTransform3D();
			Vector3D N = null;
			Vector3D U = null;
			Vector3D V = null;
			N = VecTo.getUnitVector();
			U = (Vector3D.crossProduct(VecFrom.getUnitVector(), N)).getUnitVector();
			V = Vector3D.crossProduct(N, U); //The cross-product gives a normalized vector,
			//because both input vectors are normalized,
			//so we don't need to normalize.
			rv.M11 = U.x;
			rv.M12 = U.y;
			rv.M13 = U.z;
			rv.M21 = N.x;
			rv.M22 = N.y;
			rv.M23 = N.z;
			rv.M31 = V.x;
			rv.M32 = V.y;
			rv.M33 = V.z;
			rv.M44 = 1;
			return rv;
		}
		
		/** Create a transformation matrix for rotating through angle theta around a line passing
		 * through Point  'pt'  in direction vector 'n'.
		 * 
		 * @param pt
		 * @param n
		 * @param theta_D
		 * @return
		 */
		public static AffineTransform3D matrixLineRotate(Vector3D pt, Vector3D n, float theta_D)
		{
			// Theta is measured counterclockwise as you look down the line opposite the line's direction.
			//1. Translate the line to the origin.
			//2. Rotate around the Z axis until the line lies in the Y-Z plane.
			//3. Rotate around the X axis until the line lies along the Y axis.
			//4. Rotate around the Y axis.
			//5. Reverse the second rotation.
			//6. Reverse the first rotation.
			//7. Reverse the translation.
			AffineTransform3D T = null;
			AffineTransform3D R1 = null;
			AffineTransform3D R2 = null;
			AffineTransform3D R3 = null;
			AffineTransform3D R2_inv = null;
			AffineTransform3D R1_inv = null;
			AffineTransform3D T_inv = null;
			float d = 0F;
			float L = 0F;
			// Translate the line to the origin.
			T = AffineTransform3D.getTranslateInstance(-pt.x, -pt.y, -pt.z);
			
			T_inv = AffineTransform3D.getTranslateInstance(-pt.x, -pt.y, -pt.z);
			// Rotate around Z-axis until the line is in
			// the Y-Z plane.
			d = (float) Math.sqrt(n.x*n.x + n.y*n.y);
			R1 = matrixRotateIntoYZ(n);
			/////////////////////////////
			// R1 = MatrixRotateIntoYZ(n) can be implemented in following way also
			//            '  d = Sqrt(n.x ^ 2 + n.y ^ 2)
			//               With R1
			//                 .M11 = n.y / d
			//                 .M12 = n.x / ds
			//                 .M21 = -.M12
			//                 .M22 = .M11
			//                End With
			/////////////////////////////////////////
			R1_inv = new AffineTransform3D();
			R1_inv.M11 = R1.M11;
			R1_inv.M12 = -R1.M12;
			R1_inv.M21 = -R1.M21;
			R1_inv.M22 = R1.M22;
			// Rotate around the X-axis until the line
			// lies along the Y axis.
			R2 =new AffineTransform3D();
			L = (float) n.getLength();
			R2.M22 = d / L;
			R2.M23 = -n.z / L;
			R2.M32 = - R2.M23;
			R2.M33 = R2.M22;
			R2_inv = new AffineTransform3D();
			R2_inv.M22 = R2.M22;
			R2_inv.M23 = -R2.M23;
			R2_inv.M32 = -R2.M32;
			R2_inv.M33 = R2.M33;

			// Rotate around the line (Y axis).
			R3 = AffineTransform3D.getRotateY(theta_D);

			//combine the matrices and return
			T.concatenate(R1);
			T.concatenate(R2);
			T.concatenate(R3);
			T.concatenate(R2_inv);
			T.concatenate(R1_inv);
			T.concatenate(T_inv);
			
			return T;  //T * R1 * R2 * R3 * R2_inv * R1_inv * T_inv; //left to right precedence
		}
		
		 //matrix to rotate pt about plane through pt and having normal along n
		public static AffineTransform3D matrixReflectInPlane(Vector3D pt, Vector3D n)
		{
			//T*R1*R2i*S*R2i*R1i*T1i
			//1:Shift origin to pt (make plane pass through origin)                      ===> pt*T
			//2:Rotate n(now it is a vector through origin)so that it lies in YZ Plane   ===> pt*(T*R1)
			//3:Rotate n again about x axis so that it becomes paallel to  x axis        ===> pt*(T*R1)*R2
			//4:Now the plane becomes XZ plane (x,y,z)-->(x,-y,z)                        ===> pt(*(T*R1)*R2)*S
			//5:reverse operation for R2                                                 ===> pt((*(T*R1)*R2)*S*)R2i
			//5:reverse operation for R1                                                 ===> pt(((*(T*R1)*R2)*S*)*R2i)*R1i
			//6:reverse operation for T                                                  ===> pt(((*(T*R1)*R2)*S*)*R2i)*R1i*Ti

			AffineTransform3D T = null;
			AffineTransform3D R1 = null;
			AffineTransform3D R2 = null;
			AffineTransform3D S = null;
			AffineTransform3D R1_inv = null;
			AffineTransform3D R2_inv = null;
			AffineTransform3D T_inv = null;
			float d = 0F;
			float L = 0F;

			//translate the plane to origin
			T = AffineTransform3D.getTranslateInstance(-pt.x, -pt.y, -pt.z);
			T_inv = AffineTransform3D.getTranslateInstance(pt.x, pt.y, pt.z);

			//rotate n about z axis until the normal lies in YZ plane, since n has initial position at origin its tip must be made to lie in yz plane
			d = (float) Math.sqrt(n.x *n.x + n.y *n.y);
			R1 = matrixRotateIntoYZ(n);
		
			R1_inv = new AffineTransform3D();
			R1_inv.M11 = R1.M11;
			R1_inv.M12 = -R1.M12;
			R1_inv.M21 = -R1.M21;
			R1_inv.M22 = R1.M22;

			//rotate around axis until the normal n lies along Y axis
			R2 = new AffineTransform3D();
			d = (float) Math.sqrt((Math.pow(n.x, 2)) + (Math.pow(n.y, 2)));
			L = (float) n.getLength();
			R2.M22 = d / L;
			R2.M23 = -n.z / L;
			R2.M32 = - R2.M23;
			R2.M33 = R2.M22;
			R2_inv =new AffineTransform3D();
			R2_inv.M22 = R2.M22;
			R2_inv.M23 = -R2.M23;
			R2_inv.M32 = -R2.M32;
			R2_inv.M33 = R2.M33;

			//Reflect about XZ plane
			S = matrixReflectXZ();

			//combine the matrices and return
			//combine the matrices and return
			T.concatenate(R1);
			T.concatenate(R2);
			T.concatenate(S);
			T.concatenate(R2_inv);
			T.concatenate(R1_inv);
			T.concatenate(T_inv);
			
			return T;//T * R1 * R2 * S * R2_inv * R1_inv * T_inv; //left to right precedence

		}

		
		
}
