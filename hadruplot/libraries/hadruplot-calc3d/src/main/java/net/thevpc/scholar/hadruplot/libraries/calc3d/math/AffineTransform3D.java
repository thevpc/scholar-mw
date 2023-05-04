package net.thevpc.scholar.hadruplot.libraries.calc3d.math;

import java.io.Serializable;

import net.thevpc.scholar.hadruplot.libraries.calc3d.math.exceptions.NoninvertibleTransform3DException;

/**
 * <p>
 * <b>Immutable</b> class for affine transforms in the 3D space. They include
 * rotations, translations, shears, perspective, and combinations of these. Such
 * transformations can be constructed by using coefficients specification, or by
 * creating specialized instances, by using static methods.
 * <p>
 * Such a coordinate transformation can be represented by a 4 row by 4 column
 * matrix with an implied last row of [ 0 0 0 1 ]. This matrix transforms source
 * coordinates (x,y,z) into destination coordinates (x',y',z') by considering
 * them to be a row vector and multiplying the coordinate vector by the matrix
 * according to the following process:
 * 
 * <pre>
 *                     [  m11  m12  m13  m14  ]    
 *       [ x' y' z' 1] [  m21  m22  m23  m24  ] =  [ x'  y'  z'  w' ]
 *                     [  m31  m32  m33  m34  ]
 *                     [   0    0    0    1   ]
 * </pre>
 * <p>
 * This class is used to transform {@link Vector3D}
 */

public class AffineTransform3D implements Constants, Serializable {

	protected double M11, M12, M13, M14, M21, M22, M23, M24, M31, M32, M33,
			M34, M41, M42, M43, M44;

	// *****************************CONSTRUCTERS********************************//
	/**
	 * default constructor : gives Identity Transform
	 */
	public AffineTransform3D() {
		this.M11 = 1;
		this.M12 = 0;
		this.M13 = 0;
		this.M14 = 0;
		this.M21 = 0;
		this.M22 = 1;
		this.M23 = 0;
		this.M24 = 0;
		this.M31 = 0;
		this.M32 = 0;
		this.M33 = 1;
		this.M34 = 0;
		this.M41 = 0;
		this.M42 = 0;
		this.M43 = 0;
		this.M44 = 1;
	}

	/**
	 * Constructs a new <code>AffineTransform</code> that is a copy of the
	 * specified <code>AffineTransform</code> object.
	 * 
	 * @param T
	 *            the <code>AffineTransform</code> object to copy
	 */
	public AffineTransform3D(AffineTransform3D T) {
		this.M11 = T.M11;
		this.M12 = T.M12;
		this.M13 = T.M13;
		this.M14 = T.M13;
		this.M21 = T.M21;
		this.M22 = T.M22;
		this.M23 = T.M23;
		this.M24 = T.M24;
		this.M31 = T.M31;
		this.M32 = T.M32;
		this.M33 = T.M33;
		this.M34 = T.M34;
		this.M41 = T.M41;
		this.M42 = T.M42;
		this.M43 = T.M43;
		this.M44 = T.M44;
	}

	/**
	 * Constructs a new <code>AffineTransform</code> with input as its primitiveElement3DS
	 * specified <code>AffineTransform</code> object.
	 * 
	 * @param Elements
	 *            : first row, second row , third row fourth row
	 */
	public AffineTransform3D(double M11, double M12, double M13, double M14,
			double M21, double M22, double M23, double M24, double M31,
			double M32, double M33, double M34, double M41, double M42,
			double M43, double M44) {
		this.M11 = M11;
		this.M12 = M12;
		this.M13 = M13;
		this.M14 = M14;
		this.M21 = M21;
		this.M22 = M22;
		this.M23 = M23;
		this.M24 = M24;
		this.M31 = M31;
		this.M32 = M32;
		this.M33 = M33;
		this.M34 = M34;
		this.M41 = M41;
		this.M42 = M42;
		this.M43 = M43;
		this.M44 = M44;
	}

	/**
	 * replaces the primitiveElement3DS of this affineTransform3D by the primitiveElement3DS of object
	 * in param..
	 * 
	 * @param T
	 *            the <code>AffineTransform</code> object to be copied in this.
	 */
	public void set(AffineTransform3D T) {
		this.M11 = T.M11;
		this.M12 = T.M12;
		this.M13 = T.M13;
		this.M14 = T.M14;
		this.M21 = T.M21;
		this.M22 = T.M22;
		this.M23 = T.M23;
		this.M24 = T.M24;
		this.M31 = T.M31;
		this.M32 = T.M32;
		this.M33 = T.M33;
		this.M34 = T.M34;
		this.M41 = T.M41;
		this.M42 = T.M42;
		this.M43 = T.M43;
		this.M44 = T.M44;
	}

	/**
	 * Multiplies two Transform4x4 matrices
	 * 
	 * @param matLeft
	 *            : left matrix
	 * @param matRight
	 *            : Right matrix
	 * @return result as new AffineTransform3D Object
	 */
	private static AffineTransform3D MatrixMult(AffineTransform3D matLeft,
			AffineTransform3D matRight) {

		AffineTransform3D m = new AffineTransform3D();
		m.M11 = (matRight.M11 * matLeft.M11) + (matRight.M21 * matLeft.M12)
				+ (matRight.M31 * matLeft.M13) + (matRight.M41 * matLeft.M14);
		m.M12 = (matRight.M12 * matLeft.M11) + (matRight.M22 * matLeft.M12)
				+ (matRight.M32 * matLeft.M13) + (matRight.M42 * matLeft.M14);
		m.M13 = (matRight.M13 * matLeft.M11) + (matRight.M23 * matLeft.M12)
				+ (matRight.M33 * matLeft.M13) + (matRight.M43 * matLeft.M14);
		m.M14 = (matRight.M14 * matLeft.M11) + (matRight.M24 * matLeft.M12)
				+ (matRight.M34 * matLeft.M13) + (matRight.M44 * matLeft.M14);
		m.M21 = (matRight.M11 * matLeft.M21) + (matRight.M21 * matLeft.M22)
				+ (matRight.M31 * matLeft.M23) + (matRight.M41 * matLeft.M24);
		m.M22 = (matRight.M12 * matLeft.M21) + (matRight.M22 * matLeft.M22)
				+ (matRight.M32 * matLeft.M23) + (matRight.M42 * matLeft.M24);
		m.M23 = (matRight.M13 * matLeft.M21) + (matRight.M23 * matLeft.M22)
				+ (matRight.M33 * matLeft.M23) + (matRight.M43 * matLeft.M24);
		m.M24 = (matRight.M14 * matLeft.M21) + (matRight.M24 * matLeft.M22)
				+ (matRight.M34 * matLeft.M23) + (matRight.M44 * matLeft.M24);
		m.M31 = (matRight.M11 * matLeft.M31) + (matRight.M21 * matLeft.M32)
				+ (matRight.M31 * matLeft.M33) + (matRight.M41 * matLeft.M34);
		m.M32 = (matRight.M12 * matLeft.M31) + (matRight.M22 * matLeft.M32)
				+ (matRight.M32 * matLeft.M33) + (matRight.M42 * matLeft.M34);
		m.M33 = (matRight.M13 * matLeft.M31) + (matRight.M23 * matLeft.M32)
				+ (matRight.M33 * matLeft.M33) + (matRight.M43 * matLeft.M34);
		m.M34 = (matRight.M14 * matLeft.M31) + (matRight.M24 * matLeft.M32)
				+ (matRight.M34 * matLeft.M33) + (matRight.M44 * matLeft.M34);
		m.M41 = (matRight.M11 * matLeft.M41) + (matRight.M21 * matLeft.M42)
				+ (matRight.M31 * matLeft.M43) + (matRight.M41 * matLeft.M44);
		m.M42 = (matRight.M12 * matLeft.M41) + (matRight.M22 * matLeft.M42)
				+ (matRight.M32 * matLeft.M43) + (matRight.M42 * matLeft.M44);
		m.M43 = (matRight.M13 * matLeft.M41) + (matRight.M23 * matLeft.M42)
				+ (matRight.M33 * matLeft.M43) + (matRight.M43 * matLeft.M44);
		m.M44 = (matRight.M14 * matLeft.M41) + (matRight.M24 * matLeft.M42)
				+ (matRight.M34 * matLeft.M43) + (matRight.M44 * matLeft.M44);

		return m;
	}

	private static AffineTransform3D ScalarMult(AffineTransform3D n,
			double scalar) {

		AffineTransform3D m = new AffineTransform3D();
		m.M11 = n.M11 * scalar;
		m.M12 = n.M12 * scalar;
		m.M13 = n.M13 * scalar;
		m.M14 = n.M14 * scalar;
		m.M21 = n.M21 * scalar;
		m.M22 = n.M22 * scalar;
		m.M23 = n.M23 * scalar;
		m.M24 = n.M24 * scalar;
		m.M31 = n.M31 * scalar;
		m.M32 = n.M32 * scalar;
		m.M33 = n.M33 * scalar;
		m.M34 = n.M34 * scalar;
		m.M41 = n.M41 * scalar;
		m.M42 = n.M42 * scalar;
		m.M43 = n.M43 * scalar;
		m.M44 = n.M44 * scalar;
		return m;
	}

	// *********************Static Transform GETTER METHODS
	// ******************************//
	/**
	 * Returns a transform representing a scaling transformation. The matrix
	 * representing the returned transform is:
	 * 
	 * <pre>
	 *          [   Sx   0    0   0   ]
	 *          [   0    Sy   0   0   ]
	 *          [   0    0    Sz  0   ]
	 *          [   0    0    0   1   ]
	 * </pre>
	 * 
	 * @param Sx
	 *            the factor by which coordinates are scaled along the X axis
	 *            direction
	 * @param Sy
	 *            the factor by which coordinates are scaled along the Y axis
	 *            direction
	 * @param Sz
	 *            the factor by which coordinates are scaled along the Z axis
	 *            direction
	 * @return an <code>AffineTransform</code> object that scales coordinates by
	 *         the specified factors.
	 */
	public static AffineTransform3D getScaleInstance(double Sx, double Sy,
			double Sz) {
		AffineTransform3D rv = new AffineTransform3D();
		rv.setToScale(Sx, Sy, Sz);
		return rv;
	}

	/**
	 * Returns a transform representing a translation transformation. The matrix
	 * representing the returned transform is:
	 * 
	 * <pre>
	 *          [   1    0    0    0   ]
	 *          [   0    1    0    0   ]
	 *          [   0    0    1    0   ]
	 *          [   Tx   Ty   Tz   1   ]
	 * </pre>
	 * 
	 * @param Tx
	 *            the distance by which coordinates are translated in the X axis
	 *            direction
	 * @param Ty
	 *            the distance by which coordinates are translated in the Y axis
	 *            direction
	 * @param Tz
	 *            the distance by which coordinates are translated in the Z axis
	 *            direction
	 * @return an <code>AffineTransform</code> object that represents a
	 *         translation transformation, created with the specified vector.
	 */
	public static AffineTransform3D getTranslateInstance(double Tx, double Ty,
			double Tz) {

		AffineTransform3D rv = new AffineTransform3D();
		rv.setToTranslate(Tx, Ty, Tz);
		return rv;
	}

	/**
	 * Returns a transform representing a rotation transformation about x axis.
	 * The matrix representing the returned transform is:
	 * 
	 * <pre>
	 *            | 1    0     0   0 |
	 *            | 0   cos   sin  0 |
	 * [x,y,z,1]  | 0   -sin  cos  0 |=[x, ycos-zsin, zcos+ycos, 1]
	 *            | 0    0     0   1 |
	 * </pre>
	 * 
	 * @param theta
	 *            Angle in radians to be rotated about y axis
	 * @return new instance of Transform3D
	 */
	public static AffineTransform3D getRotateX(double theta) {

		AffineTransform3D rv = new AffineTransform3D();
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		rv.M22 = cos;
		rv.M23 = sin;
		rv.M32 = -sin;
		rv.M33 = cos;
		rv.M11 = 1;
		rv.M12 = 0;
		rv.M13 = 0;
		rv.M14 = 0;
		rv.M21 = 0;
		rv.M24 = 0;
		rv.M31 = 0;
		rv.M34 = 0;
		rv.M41 = 0;
		rv.M42 = 0;
		rv.M43 = 0;
		rv.M44 = 1;
		return rv;
	}

	/**
	 * Returns a transform representing a rotation transformation about x axis.
	 * The matrix representing the returned transform is:
	 * 
	 * <pre>
	 *            | cos   0  -sin  0 |
	 *            |  0    1    0   0 |
	 *  [x,y,z,1] | sin   0   cos  1 |=[xcos+zsin, y, zcos-xsin, 1]
	 *            |  0    0    0   1 |
	 * </pre>
	 * 
	 * @param theta
	 *            Angle in radians to be rotated about y axis
	 * @return new instance of Transform3D
	 */
	public static AffineTransform3D getRotateY(double theta) {

		AffineTransform3D rv = new AffineTransform3D();
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		rv.M11 = cos;
		rv.M31 = sin;
		rv.M13 = -sin;
		rv.M33 = cos;
		rv.M22 = 1;
		rv.M12 = 0;
		rv.M23 = 0;
		rv.M14 = 0;
		rv.M21 = 0;
		rv.M24 = 0;
		rv.M32 = 0;
		rv.M34 = 0;
		rv.M41 = 0;
		rv.M42 = 0;
		rv.M43 = 0;
		rv.M44 = 1;
		return rv;
	}

	/**
	 * Returns a transform representing a rotation transformation about z axis.
	 * The matrix representing the returned transform is:
	 * 
	 * <pre>
	 *            |  cos   sin   0   0 |
	 *            | -sin   cos   0   0 |
	 *  [x,y,z,1] |   0     0    1   0 |=[xcos-ysin, ycos+xsin,z,1]
	 *            |   0     0    0   1 |
	 * </pre>
	 * 
	 * @param theta
	 *            Angle in radians to be rotated about y axis
	 * @return new instance of Transform3D
	 */
	public static AffineTransform3D getRotateZ(double theta) {

		AffineTransform3D rv = new AffineTransform3D();
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		rv.M11 = cos;
		rv.M12 = sin;
		rv.M21 = -sin;
		rv.M22 = cos;
		rv.M33 = 1;
		rv.M13 = 0;
		rv.M31 = 0;
		rv.M14 = 0;
		rv.M32 = 0;
		rv.M24 = 0;
		rv.M23 = 0;
		rv.M34 = 0;
		rv.M41 = 0;
		rv.M42 = 0;
		rv.M43 = 0;
		rv.M44 = 1;
		return rv;
	}

	/**
	 * Returns a transform that rotates coordinates around origin by specific
	 * angles around coordinate axes. All coordinates rotate about the specified
	 * anchor coordinates (here it is Origin) by the same amount.
	 * 
	 * @param theta_x
	 *            the angle of rotation measured in radians to rotate around X
	 *            axis
	 * @param theta_y
	 *            the angle of rotation measured in radians to rotate around Y
	 *            axis
	 * @param theta_z
	 *            the angle of rotation measured in radians to rotate around Z
	 *            axis
	 * 
	 */
	public static AffineTransform3D getRotateInstance(double theta_x,
			double theta_y, double theta_z) {
		AffineTransform3D rv = new AffineTransform3D();
		rv = MatrixMult(MatrixMult(getRotateX(theta_x), getRotateY(theta_y)),
				getRotateZ(theta_z));
		return rv;
	}

	/**
	 * Returns a transform that rotates coordinates around an anchor point by
	 * specific angles around coordinate axes. All coordinates rotate about the
	 * specified anchor coordinates by the same amount.
	 * 
	 * @param anchorPoint
	 *            Position Vector point about which rotation is desired
	 * @param theta_x
	 *            the angle of rotation measured in radians to rotate around X
	 *            axis
	 * @param theta_y
	 *            the angle of rotation measured in radians to rotate around Y
	 *            axis
	 * @param theta_z
	 *            the angle of rotation measured in radians to rotate around Z
	 *            axis
	 * @return
	 */
	public static AffineTransform3D getRotateInstance(Vector3D anchorPoint,
			double theta_x, double theta_y, double theta_z) {
		AffineTransform3D rv = new AffineTransform3D();
		// Step1: Shift Origin to AnchorPoint
		rv = getTranslateInstance(-anchorPoint.getX(), -anchorPoint.getY(),
				-anchorPoint.getZ());
		// Step2: Rotate by desired angles
		rv.concatenate(getRotateInstance(theta_x, theta_y, theta_z));
		// Shift Origin back to previous Origin
		rv.concatenate(getTranslateInstance(anchorPoint.getX(),
				anchorPoint.getY(), anchorPoint.getZ()));
		// return rv
		return rv;
	}

	/**
	 * <p>
	 * Returns the determinant of the matrix representation of the transform.
	 * The determinant is useful both to determine if the transform can be
	 * inverted and to get a single value representing the combined X and Y
	 * scaling of the transform.
	 * </p>
	 * <p>
	 * If the determinant is non-zero, then this transform is invertible and the
	 * various methods that depend on the inverse transform do not need to throw
	 * a {@link NoninvertibleTransform3DException}. If the determinant is zero
	 * then this transform can not be inverted since the transform maps all
	 * input coordinates onto a line or a point. If the determinant is near
	 * enough to zero then inverse transform operations might not carry enough
	 * precision to produce meaningful results.
	 * </P>
	 * Mathematically, the determinant is denoted using :
	 * 
	 * <pre>
	 *           |  m11  m12  m13  m14  |
	 *           |  m21  m22  m23  m24  |  = .....
	 *           |  m21  m22  m23  m24  |
	 *           |  m21  m22  m23  m24  |
	 * </pre>
	 * 
	 * </p>
	 */
	public double getDeterminant() {

		return (M14 * M23 * M32 * M41) - (M13 * M24 * M32 * M41)
				- (M14 * M22 * M33 * M41) + (M12 * M24 * M33 * M41)
				+ (M13 * M22 * M34 * M41) - (M12 * M23 * M34 * M41)
				- (M14 * M23 * M31 * M42) + (M13 * M24 * M31 * M42)
				+ (M14 * M21 * M33 * M42) - (M11 * M24 * M33 * M42)
				- (M13 * M21 * M34 * M42) + (M11 * M23 * M34 * M42)
				+ (M14 * M22 * M31 * M43) - (M12 * M24 * M31 * M43)
				- (M14 * M21 * M32 * M43) + (M11 * M24 * M32 * M43)
				+ (M12 * M21 * M34 * M43) - (M11 * M22 * M34 * M43)
				- (M13 * M22 * M31 * M44) + (M12 * M23 * M31 * M44)
				+ (M13 * M21 * M32 * M44) - (M11 * M23 * M32 * M44)
				- (M12 * M21 * M33 * M44) + (M11 * M22 * M33 * M44);
	}

	/**
	 * <p>
	 * Returns an AffineTransform object representing the inverse
	 * transformation. The inverse transform Tx' of this transform Tx maps
	 * coordinates transformed by Tx back to their original coordinates. In
	 * other words, <code> Tx'(Tx(p)) = p = Tx(Tx'(p)) </code>.
	 * </p>
	 * <p>
	 * If this transform maps all coordinates onto a point or a line then it
	 * will not have an inverse, since coordinates that do not lie on the
	 * destination point or line will not have an inverse mapping. The
	 * getDeterminant method can be used to determine if this transform has no
	 * inverse, in which case an exception will be thrown if the invert method
	 * is called.
	 * </p>
	 * 
	 * @return a new AffineTransform object representing the inverse
	 *         transformation.
	 * @throws NoninvertibleTransform3DException
	 *             - if the matrix cannot be inverted.
	 */

	public AffineTransform3D getInverse()
			throws NoninvertibleTransform3DException {
		AffineTransform3D inverseMat = new AffineTransform3D();
		double Det = getDeterminant();

		if (Det == 0) {
			throw new NoninvertibleTransform3DException();
		}
		AffineTransform3D rv = new AffineTransform3D();
		rv.M11 = (M23 * M34 * M42) - (M24 * M33 * M42) + (M24 * M32 * M43)
				- (M22 * M34 * M43) - (M23 * M32 * M44) + (M22 * M33 * M44);
		rv.M12 = (M14 * M33 * M42) - (M13 * M34 * M42) - (M14 * M32 * M43)
				+ (M12 * M34 * M43) + (M13 * M32 * M44) - (M12 * M33 * M44);
		rv.M13 = (M13 * M24 * M42) - (M14 * M23 * M42) + (M14 * M22 * M43)
				- (M12 * M24 * M43) - (M13 * M22 * M44) + (M12 * M23 * M44);
		rv.M14 = (M14 * M23 * M32) - (M13 * M24 * M32) - (M14 * M22 * M33)
				+ (M12 * M24 * M33) + (M13 * M22 * M34) - (M12 * M23 * M34);
		rv.M21 = (M24 * M33 * M41) - (M23 * M34 * M41) - (M24 * M31 * M43)
				+ (M21 * M34 * M43) + (M23 * M31 * M44) - (M21 * M33 * M44);
		rv.M22 = (M13 * M34 * M41) - (M14 * M33 * M41) + (M14 * M31 * M43)
				- (M11 * M34 * M43) - (M13 * M31 * M44) + (M11 * M33 * M44);
		rv.M23 = (M14 * M23 * M41) - (M13 * M24 * M41) - (M14 * M21 * M43)
				+ (M11 * M24 * M43) + (M13 * M21 * M44) - (M11 * M23 * M44);
		rv.M24 = (M13 * M24 * M31) - (M14 * M23 * M31) + (M14 * M21 * M33)
				- (M11 * M24 * M33) - (M13 * M21 * M34) + (M11 * M23 * M34);
		rv.M31 = (M22 * M34 * M41) - (M24 * M32 * M41) + (M24 * M31 * M42)
				- (M21 * M34 * M42) - (M22 * M31 * M44) + (M21 * M32 * M44);
		rv.M32 = (M14 * M32 * M41) - (M12 * M34 * M41) - (M14 * M31 * M42)
				+ (M11 * M34 * M42) + (M12 * M31 * M44) - (M11 * M32 * M44);
		rv.M33 = (M12 * M24 * M41) - (M14 * M22 * M41) + (M14 * M21 * M42)
				- (M11 * M24 * M42) - (M12 * M21 * M44) + (M11 * M22 * M44);
		rv.M34 = (M14 * M22 * M31) - (M12 * M24 * M31) - (M14 * M21 * M32)
				+ (M11 * M24 * M32) + (M12 * M21 * M34) - (M11 * M22 * M34);
		rv.M41 = (M23 * M32 * M41) - (M22 * M33 * M41) - (M23 * M31 * M42)
				+ (M21 * M33 * M42) + (M22 * M31 * M43) - (M21 * M32 * M43);
		rv.M42 = (M12 * M33 * M41) - (M13 * M32 * M41) + (M13 * M31 * M42)
				- (M11 * M33 * M42) - (M12 * M31 * M43) + (M11 * M32 * M43);
		rv.M43 = (M13 * M22 * M41) - (M12 * M23 * M41) - (M13 * M21 * M42)
				+ (M11 * M23 * M42) + (M12 * M21 * M43) - (M11 * M22 * M43);
		rv.M44 = (M12 * M23 * M31) - (M13 * M22 * M31) + (M13 * M21 * M32)
				- (M11 * M23 * M32) - (M12 * M21 * M33) + (M11 * M22 * M33);

		Det = (1 / getDeterminant());
		inverseMat = ScalarMult(new AffineTransform3D(), Det); // MatrixScaling4(VectorInput(Det,
																// Det, Det),
																// Det)
		rv = MatrixMult(rv, inverseMat);
		return rv;
	}

	
	
	// ***************************** SETTER METHODS
	// ******************************//
	/**
	 * Sets this transform to a rotation transformation about X axis (anchor Point = Origin).
	 * @param theta_x the angle of rotation measured in radians to rotate around X axis
	 */
	public void setToRotationX(double theta_x) {
		this.set(getRotateX(theta_x));
	}

	
	
	/**
	 * Sets this transform to a rotation transformation about Y axis (anchor Point = Origin).
	 * @param theta_y the angle of rotation measured in radians to rotate around Y axis
	 */
	public void setToRotationY(double theta_y) {
		this.set(getRotateX(theta_y));
	}
	
	

	/**
	 *  Sets this transform to a rotation transformation about Z axis (anchor Point = Origin).
	 * @param theta_z the angle of rotation measured in radians to rotate around z axis
	 */
	public void setToRotationZ(double theta_z) {
		this.set(getRotateX(theta_z));
	}

	
	
	/**
	 * Sets this transform to a  rotation transformation around coordinate axes (anchorPoint= Origin).
	 * @param theta_x the angle of rotation measured in radians to rotate around X axis
	 * @param theta_y the angle of rotation measured in radians to rotate around Y axis
	 * @param theta_z the angle of rotation measured in radians to rotate around Z axis
	 */
	public void setToRotation(double theta_x, double theta_y, double theta_z) {
		this.set(getRotateInstance(theta_x, theta_y, theta_z));
	}
	
	
	
	/**
	 * Sets this transform to a translated rotation transformation. This
	 * operation is equivalent to translating the coordinates so that the anchor
	 * point is at the origin (S1), then rotating them about the new origin
	 * (S2), and finally translating so that the intermediate origin is restored
	 * to the coordinates of the original anchor point (S3). 
	 * <p>This operation is
	 * equivalent to the following sequence of calls:
	 * <pre>
	 * Step1: setToTranslation(-anchorx, -anchory,-anchorz);
	 * Step2: rotate(theta_x,.....);   
	 * Step3: Translate(anchorx, anchory, anchorz);  </pre> </p>
	 * @param anchorPoint
	 *            Position Vector point about which rotation is desired
	 * @param theta_x
	 *            the angle of rotation measured in radians to rotate around X
	 *            axis
	 * @param theta_y
	 *            the angle of rotation measured in radians to rotate around Y
	 *            axis
	 * @param theta_z
	 *            the angle of rotation measured in radians to rotate around Z
	 *            axis
	 * @return
	 */
	public void setToRotation(Vector3D anchorPoint, double theta_x,
			double theta_y, double theta_z) {
		this.set(getRotateInstance(anchorPoint, theta_x, theta_y, theta_z));
	}

	
	
	/**
	 * Sets this transform to a translation transformation. The matrix
	 * representing this transform becomes:
	 * 
	 * <pre>
	 *          [   1    0    0    0   ]
	 *          [   0    1    0    0   ]
	 *          [   0    0    1    0   ]
	 *          [   Tx   Ty   Tz   1   ]
	 * </pre>
	 * 
	 * @param Tx
	 *            the distance by which coordinates are translated in the X axis
	 *            direction
	 * @param Ty
	 *            the distance by which coordinates are translated in the Y axis
	 *            direction
	 * @param Tz
	 *            the distance by which coordinates are translated in the Z axis
	 *            direction
	 */
	public void setToTranslate(double Tx, double Ty, double Tz) {

		this.M11 = 1;
		this.M12 = 0;
		this.M13 = 0;
		this.M14 = 0;
		this.M21 = 0;
		this.M22 = 1;
		this.M23 = 0;
		this.M24 = 0;
		this.M31 = 0;
		this.M32 = 0;
		this.M33 = 1;
		this.M34 = 0;
		this.M41 = Tx;
		this.M42 = Ty;
		this.M43 = Tz;
		this.M44 = 1;
	}

	/**
	 * Sets this transform to a scaling transformation. The matrix representing
	 * this transform becomes:
	 * 
	 * <pre>
	 *          [   Sx   0    0   0   ]
	 *          [   0    Sy   0   0   ]
	 *          [   0    0    Sz  0   ]
	 *          [   0    0    0   1   ]
	 * </pre>
	 * 
	 * @param Sx
	 *            the factor by which coordinates are scaled along the X axis
	 *            direction
	 * @param Sy
	 *            the factor by which coordinates are scaled along the Y axis
	 *            direction
	 * @param Sz
	 *            the factor by which coordinates are scaled along the Z axis
	 *            direction
	 */
	public void setToScale(double Sx, double Sy, double Sz) {
		this.M11 = Sx;
		this.M22 = Sy;
		this.M33 = Sz;
		this.M44 = 1;
		this.M12 = 0;
		this.M13 = 0;
		this.M14 = 0;
		this.M21 = 0;
		this.M23 = 0;
		this.M24 = 0;
		this.M31 = 0;
		this.M32 = 0;
		this.M34 = 0;
		this.M41 = 0;
		this.M42 = 0;
		this.M43 = 0;
	}

	/**
	 * Concatenates an <code>AffineTransform</code> <code>Tx</code> to this
	 * <code>AffineTransform</code> Cx in the most commonly useful way to
	 * provide a new user space that is mapped to the former user space by
	 * <code>Tx</code>. Cx is updated to perform the combined transformation.
	 * Transforming a point p by the updated transform Cx' is equivalent to
	 * first transforming p by <code>Tx</code> and then transforming the result
	 * by the original transform Cx like this: Cx'(p) = Cx(Tx(p)) In matrix
	 * notation, if this transform Cx is represented by the matrix [this] and
	 * <code>Tx</code> is represented by the matrix [Tx] then this method does
	 * the following:
	 * 
	 * <pre>
	 *          [this] = [this] x [Tx]
	 * </pre>
	 * 
	 * @param Tx
	 *            the <code>AffineTransform</code> object to be concatenated
	 *            with this <code>AffineTransform</code> object.
	 * @see #preConcatenate(AffineTransform3D Tx)
	 */
	public void concatenate(AffineTransform3D Tx) {
		this.set(MatrixMult(this, Tx));
	}

	
	/**PreConcatenate AffineTransform to this
	 * <pre>
	 *          [this] = [Tx] x [this]	  </pre>
	 * @param Tx AffineTransform3D to be preConcatenated to this.
	 * @see #concatenate(AffineTransform3D Tx)
	 */
	public void preConcatenate(AffineTransform3D Tx) {
		this.set(MatrixMult(Tx, this));
	}

	/**
	 * Checks if two Transforms are equal checking against each primitiveElement3D
	 * 
	 * @param obj
	 *            : the object to test for equality with this AffineTransform
	 * @return true if this AffineTransform represents the same affine
	 *         coordinate transform as the specified argument.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AffineTransform3D))
			return false;
		AffineTransform3D T = (AffineTransform3D) obj;
		if (Math.abs(M11 - T.M11) > EPSILON)
			return false;
		if (Math.abs(M12 - T.M12) > EPSILON)
			return false;
		if (Math.abs(M13 - T.M13) > EPSILON)
			return false;
		if (Math.abs(M14 - T.M14) > EPSILON)
			return false;
		if (Math.abs(M21 - T.M21) > EPSILON)
			return false;
		if (Math.abs(M22 - T.M22) > EPSILON)
			return false;
		if (Math.abs(M23 - T.M23) > EPSILON)
			return false;
		if (Math.abs(M24 - T.M24) > EPSILON)
			return false;
		if (Math.abs(M31 - T.M31) > EPSILON)
			return false;
		if (Math.abs(M32 - T.M32) > EPSILON)
			return false;
		if (Math.abs(M33 - T.M33) > EPSILON)
			return false;
		if (Math.abs(M34 - T.M34) > EPSILON)
			return false;
		if (Math.abs(M41 - T.M41) > EPSILON)
			return false;
		if (Math.abs(M42 - T.M42) > EPSILON)
			return false;
		if (Math.abs(M43 - T.M43) > EPSILON)
			return false;
		if (Math.abs(M44 - T.M44) > EPSILON)
			return false;

		return true;

	}

	/**
	 * Returns true if this AffineTransform is an identity transform.
	 * 
	 * @return true if this AffineTransform is an identity transform; false
	 *         otherwise.
	 */
	public boolean isIdentity() {
		return this.equals(new AffineTransform3D());
	}

	/**
	 * Returns true if this AffineTransform is invertible.
	 * 
	 * @return true if its Determinant is non zero (is greater than some very
	 *         small value e.g. 10e-12)
	 */
	public boolean isInvertible() {
		// If the
		// determinant is near enough to zero then inverse transform operations
		// might not carry enough precision to produce meaningful results. so we
		// are using EPSILON
		return (Math.abs(getDeterminant()) > EPSILON);
	}

	/**
	 * <p>
	 * Sets this transform to the inverse of itself. The inverse transform Tx'
	 * of this transform Tx maps coordinates transformed by Tx back to their
	 * original coordinates. In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
	 * </p>
	 * <p>
	 * If this transform maps all coordinates onto a point or a line then it
	 * will not have an inverse, since coordinates that do not lie on the
	 * destination point or line will not have an inverse mapping. The
	 * getDeterminant method can be used to determine if this transform has no
	 * inverse, in which case an exception will be thrown if the invert method
	 * is called.
	 * </p>
	 * 
	 * @throws NoninvertibleTransform3DException
	 *             - if the matrix cannot be inverted.
	 */
	public void invert() throws NoninvertibleTransform3DException {
		this.set(getInverse());
	}

	/**
	 * Resets Transform3D to identity
	 */
	public void setToIdentity() {
		this.set(new AffineTransform3D());
	}

	/**
	 * Concatenates this transform with a scaling transformation. This is
	 * equivalent to calling concatenate(S), where S is an AffineTransform
	 * represented by the following matrix:
	 * 
	 * <pre>
	 *                [   sx   0    0    0   ]
	 *                [   0    sy   0    0   ]
	 *                [   0    0    sz   0   ]
	 *                [   0    0    0    1   ]
	 * </pre>
	 * 
	 * @param Sx
	 *            the factor by which coordinates are scaled along the X axis
	 *            direction
	 * @param Sy
	 *            the factor by which coordinates are scaled along the Y axis
	 *            direction
	 * @param Sz
	 *            the factor by which coordinates are scaled along the Z axis
	 *            direction
	 */
	public void scale(double Sx, double Sy, double Sz) {
		this.set(MatrixMult(this, getScaleInstance(Sx, Sy, Sz)));
	}

	/**
	 * Concatenates this transform with a rotation transformation. This is
	 * equivalent to calling concatenate with RotateX(theta_x), RotateY(theta_y)
	 * and RotateZ(theta_z) respectively
	 * 
	 * @param theta_x
	 *            the angle of rotation measured in radians to rotate around X
	 *            axis
	 * @param theta_y
	 *            the angle of rotation measured in radians to rotate around Y
	 *            axis
	 * @param theta_z
	 *            the angle of rotation measured in radians to rotate around Z
	 *            axis
	 */
	public void rotate(double theta_x, double theta_y, double theta_z) {
		this.set(MatrixMult(this, getRotateInstance(theta_x, theta_y, theta_z)));
	}

	/**
	 * concatenates this transform with a translation transformation. This is
	 * equivalent to calling concatenate(T), where T is an AffineTransform
	 * represented by the following matrix:
	 * 
	 * <pre>
	 *           [   1    0    0    0]
	 *           [   0    1    0    0]
	 *           [   0    0    1    0] 
	 *           [   tx   ty   tz   1]
	 * </pre>
	 * 
	 * @param Tx
	 *            the distance by which coordinates are translated in the X axis
	 *            direction
	 * @param Ty
	 *            the distance by which coordinates are translated in the Y axis
	 *            direction
	 * @param Tz
	 *            the distance by which coordinates are translated in the Z axis
	 *            direction
	 */
	public void translate(double Tx, double Ty, double Tz) {
		this.set(MatrixMult(this, getTranslateInstance(Tx, Ty, Tz)));
	}

	/**
	 * transforms the input vector
	 * 
	 * @param V
	 *            vector to be transformed
	 * 
	 */
	public void transform(Vector3D V) {
		Vector3D rv = new Vector3D();
		rv.x = V.x * M11 + V.y * M21 + V.z * M31 + V.w * M41;
		rv.y = V.x * M12 + V.y * M22 + V.z * M32 + V.w * M42;
		rv.z = V.x * M13 + V.y * M23 + V.z * M33 + V.w * M43;
		rv.w = V.x * M14 + V.y * M24 + V.z * M34 + V.w * M44;
		V.set(rv);
	}

	/**
	 * transforms the input vector and returns new transformed vector
	 * 
	 * @param V
	 *            vector to be transformed
	 * @return new transformed vector(input vector remains unchanged)
	 */
	public Vector3D getTransformedVector(Vector3D V) {
		Vector3D rv = new Vector3D();
		rv.x = V.x * M11 + V.y * M21 + V.z * M31 + V.w * M41;
		rv.y = V.x * M12 + V.y * M22 + V.z * M32 + V.w * M42;
		rv.z = V.x * M13 + V.y * M23 + V.z * M33 + V.w * M43;
		rv.w = V.x * M14 + V.y * M24 + V.z * M34 + V.w * M44;
		return rv;
	}

	public AffineTransform3D copy() {
		return new AffineTransform3D(this);
	}
}
