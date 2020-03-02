package net.vpc.scholar.hadruplot.backends.calc3d.engine3d;



import java.io.Serializable;

import net.vpc.scholar.hadruplot.backends.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.backends.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Quat;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;


public class Camera3D implements Serializable{
	private static final double PI_OVER_360 = Math.PI/360;
	private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
	/**
	 * location of camera(eye) in world space (Default value = (0,0,6))
	 */
	public Vector3D eye = new Vector3D();

	/**
	 * location of point in world space through which plane of projection
	 * passes, actually plane of projection or view plane is perpendicular to
	 * vector joining eye and focus and passes through focus. (Default value =
	 * (0,0,0))
	 */
	public Vector3D focus = new Vector3D();
	/**
	 * direction of Up vector in world space (will become +Y in camera Space)
	 * the roll angle (rotation in degrees) of the camera around it's view axis,
	 * roll angle zero means Camera has Up along y. (Default value =0)
	 */
	public double rollAngle = 0;

	/**
	 * view {@link Frustum} of camera, for default values see {@link #reset()}
	 */
	public Frustum frustum = new Frustum();

	/** flag to switch between orthographic and perspective projection*/
	private boolean iIsOrthographic = false;

    /** Field Of Vision */
    private double fov=50;
   
    /** matrix to convert eye coordinates to ClipCoordinates which on homogenisation becomes normal device coordinates*/
    private AffineTransform3D projectionMatrix;
    
    /**Camera Up Direction*/
    public Vector3D up = new Vector3D();
	
	/** Vectors in world space representing local X(U), Y(V) and Z(N) axis of camera Space*/
    private Vector3D iUVN[] = new Vector3D[3];

	// temporary vectors used in calculations
	private Vector3D iTempFocusVec = new Vector3D();
	private Vector3D iTempEyeVec = new Vector3D();
	private Vector3D iTempUpVec = new Vector3D();
	private Vector3D iTempVecU = new Vector3D();
	private Vector3D iTempVecV = new Vector3D();
	private Vector3D iTempVecW = new Vector3D();
	// temporary quaternions used in calculations
	private Quat iTempQuatQ = new Quat();
	private Quat iTempQuatQi = new Quat();
	private Quat iTempQuatEye = new Quat();
	private Quat iTempQuatUp = new Quat();
	private Quat iTempQuatRes1 = new Quat();

	/**
	 * nearZ=near Plane,  farZ=far Plane
	 */
	private double nearZ=-1,farZ=-100;
	
	/**
	 * b=bottom, t=top, l=left, r=right
	 */
	private double b=-1,t=1,l=-1,r=1;

	/**
	 * Constructor - initialises this to some default values.
	 */
	public Camera3D() {
		for (int i = 0; i < 3; ++i) {
			iUVN[i] = new Vector3D();
		}
		reset();
	}

	/**
	 * Constructor - initialises camera with roll value.
	 * 
	 * @param roll
	 *            RollAngle in degrees (Default value=0)
	 */
	public Camera3D(double roll) {
		rollAngle = roll;
		for (int i = 0; i < 3; ++i) {
			iUVN[i] = new Vector3D();
		}
		reset();
	}

	/**
	 * Up vector points to the top of our screen. Calculate it by rotating a
	 * y-vector around the z-axis by the roll-value:
	 * 
	 * @param roll
	 *            the roll angle (rotation) of the camera around it's view axis
	 */
	@SuppressWarnings("unused")
	private void setUpvector(double roll) {
		up.set(Math.sin(roll * Math.PI / 180), -Math.cos(roll * Math.PI / 180),	0.0f);
	}

	/**
	 * Reset this camera to default values. Which are..
	 * 
	 * <pre>
	 * Focus = [0,0,0] 
	 * Eye   = [0,0,5] 
	 * Up    = [0,1,0]	(choose rollAngle=0);
	 * By choosing these values we get the local x, y z or U,V,N in cameraSpace as 
	 * U(x)  = [1,0,0]  
	 * V(y)  = [0,1,0]
	 * N(z)  = [-1,0,0]
	 * </pre>
	 */
	public void reset() {
		eye.set(0.0f, 0.0f, 6.0f); // place camera at (0,0,4) for good perspective fell( as per mine)
		focus.set(0.0f, 0.0f, 0.0f); // set focus to origin in world space
		rollAngle = 0;
		up.set( 0.0f, 1.0f, 0.0f ); // set up to +y axis of world space
		//b=-1;t=1;l=-1;r=1;n=-1;f=-100;
		fov=30;
		recalcFrustum();
		/*
		 * since N (or local Z) in cameraSpace is along -ve Z in world space, so
		 * our near and far planes will be negative, we are looking towards
		 * origin which is along -ve Z of cameraSpace.
		 */
	}

	/**
	 * Get whether this camera uses orthographic projection.
	 * 
	 * @return true if it does; false otherwise.
	 */
	public boolean isOrthographic() {
		return iIsOrthographic;
	}

	/**
	 * Set whether this camera should use orthographic projection.
	 * 
	 * @param isOrthographic
	 *            true if it should; false otherwise.
	 */
	public void setOrthographic(boolean isOrthographic) {
		LOG.info("Orthographic projection enabled: " + isOrthographic);
		iIsOrthographic = isOrthographic;
		//Calculate frustum and proojection matrix again
		recalcFrustum();
	}

	/**
	 * Get Field of view of Camera
	 * @return
	 */
	public double getFov() {return fov;}
	
	/**
	 * Set field of View of Camera
	 * */
	public void setFov(double fov) {
		this.fov=fov; 
		recalcFrustum();
		LOG.info("Field of View Of camera Changed to: " + fov);
	}
	
	/**
	 * whenever fov changes(zooming) we have to recalculate View frustum values.
	 */
	private void recalcFrustum(){
		 l = -nearZ * Math.tan(fov * PI_OVER_360);
		 if (iIsOrthographic) l=4*l; 
		 r=-l;
		 b=l;
		 t=-l;
		// this.frustum=new Frustum(l,r,t,b,nearZ,farZ,fov);
		 if (!isOrthographic()) {
			        //perspective projection
			        projectionMatrix= new AffineTransform3D(
						2*nearZ/(r-l),         0,                   0,	                       0, 
						    0,             2*nearZ/(t-b),           0,                         0, 
						(r+l)/(r-l),       (t+b)/(t-b),        -(farZ+nearZ)/(farZ-nearZ),     -1,
						    0,                 0,              -2*farZ*nearZ/(farZ-nearZ),     0 );
				}else{
					//orthographic projection
					projectionMatrix= new AffineTransform3D(
						2/(r-l),               0,                   0,	                        0, 
						    0,              2/(t-b),                0,                          0, 
						    0,                 0,                2/(farZ-nearZ),               0,
						-(r+l)/(r-l),     -(t+b)/(t-b),         -(farZ+nearZ)/(farZ-nearZ),     1 );
				}
	}
	
	/**
	 * returns current View Frustom of camera
	 */
	public Frustum getFrustom(){
		return new Frustum(l,r,t,b,nearZ,farZ,fov);
	}
	
	/**
	 * Sets View frustom of Camera
	 * @param f View Frustom Class/Structure Object needed to from ClipMatrix(Projection Matrix)
	 */
	public void setFrustum(Frustum f){
		 l = f.left;
		 r = f.right;
		 b = f.bottom;
		 t = f.top;
		 nearZ=f.near;
		 farZ=f.far;
		 fov=2*(180/Math.PI)*Math.atan((t-b)/(2*nearZ));
	}
	
	/**
	 * Sets View frustom of Camera
	 * @param Parameters needed to specify View frustom of camera
	 */
	public void setFrustum(double left, double right, double bottom, double top, double near, double far){
		 l = left;
		 r = right;
		 b = bottom;
		 t = top;
		 nearZ= near;
		 farZ= far;
		 fov=2*(180/Math.PI)*Math.atan((t-b)/(2*nearZ));
	}
	
	/**
	 * this matrix transforms World Coordinates to eye Coordinates with new origin at eye(cameraLocation) and new axes defined 
	 * by CameraUp, cameraRight and CameraLookat Directions.
	 * @return
	 */
	public synchronized AffineTransform3D getCameraMatrix(){
		updateUVNAxes();
		return new AffineTransform3D(
				iUVN[0].getX(),                       iUVN[1].getX(),                    iUVN[2].getX(),	                  0, 
				iUVN[0].getY(),                       iUVN[1].getY(),                    iUVN[2].getY(),                      0, 
				iUVN[0].getZ(),                       iUVN[1].getZ(),                    iUVN[2].getZ(),                      0,
				-iUVN[0].DotProduct(eye),            -iUVN[1].DotProduct(eye),           -iUVN[2].DotProduct(eye),            1);
	}
	
	
	/**
	 * returns matrix which transforms eyeCoordinates to clipCoordinates which on homogenising becomes world Normal device coordinates.
	 * @return
	 */
	public AffineTransform3D getProjectionMatrix(){
		return projectionMatrix;
	}
	
		
	/**
	 * Get 3 vectors representing the UVN axes of this camera.
	 * 
	 * @return an array of 3 vectors.
	 */
	public Vector3D[] getCameraUVNAxes() {
		updateUVNAxes();
		return iUVN;
	}

	/**
	 * Update the UVN axes vectors to reflect the current position, orientation
	 * and focus of this camera.
	 */
	private void updateUVNAxes() {
		// V (y) axis
		iUVN[1].set(up);
		iUVN[1].normalize();
		// N (z) axis
		iUVN[2].set(eye);
		iUVN[2].subtractEq(focus);
	    iUVN[2].negate();
		iUVN[2].normalize();
		// U (x) axis
		iUVN[0] = Vector3D.crossProduct(iUVN[1], iUVN[2]);

	}

	/**
	 * Rotate the camera around its focal point.
	 * @param  ya : angle to be rotated about U axis (in degrees).
	 * @param roll : angle to be rotated about V axis (in degrees).
	 * @param pitch : angle to be rotated about N axis (in degrees).
	 * 
	 */
	public void rotateAroundFocus(double ya, double pitch, double roll) {
		ya = ya * Math.PI / 180;
		pitch = pitch * Math.PI / 180;
		roll = roll * Math.PI / 180;
		// vectors
		iTempFocusVec.set(focus);
		iTempEyeVec.set(eye);
		iTempEyeVec.subtractEq(focus);
		iTempUpVec.set(up);

		// u,v,w axes (camera coordinate system based at focal point)
		iTempVecV.set(iTempUpVec);
		iTempVecV.normalize();
		iTempVecW.set(iTempEyeVec);
		iTempVecW.normalize();
		iTempVecU = Vector3D.crossProduct(iTempVecV, iTempVecW);
		iTempVecU.normalize();

		// rotation quaternions (we're rotating around u,v,w axes)
		Quat.calculateEulerRotationQuat(ya, pitch, roll, iTempVecU, iTempVecV,
				iTempVecW, iTempQuatQ);

		Quat.calculateInverse(iTempQuatQ, iTempQuatQi);

		// vector quaternions (input vectors)
		iTempQuatEye.set(0, iTempVecW);
		iTempQuatUp.set(0, iTempVecV);

		/*
		 * do rotation -> q_eye_new = q * q_eye * qi -> 
		 */

		// eye
		iTempQuatRes1.set(iTempQuatQ).multEq(iTempQuatEye);
		iTempQuatRes1.multEq(iTempQuatQi);
		iTempQuatRes1.iVec.normalize();
		iTempQuatRes1.iVec.scaleEq(iTempEyeVec.getLength());
		iTempFocusVec.addEq(iTempQuatRes1.iVec);
		eye.set(iTempFocusVec);

		/*
		q_up_new = q * q_up * qi;
		*/
		
		// up
		iTempQuatRes1.set(iTempQuatQ).multEq(iTempQuatUp);
		iTempQuatRes1.multEq(iTempQuatQi);
		iTempQuatRes1.iVec.normalize();
		up.set(iTempQuatRes1.iVec);
	}

	/**
	 * Rotate the camera around Axis in World space.
	 * 
	 * @param the
	 *            3D rotation angles (in Degrees).
	 * 
	 */
	public void rotateAboutAxes(double theta_x, double theta_y, double theta_z) {
		
		//convert to radians
		theta_x = theta_x * Math.PI / 180;
		theta_y = theta_y * Math.PI / 180;
		theta_z = theta_z * Math.PI / 180;
		AffineTransform3D T =AffineTransform3D.getRotateInstance(theta_x, theta_y, theta_z);
		
		T.transform(eye);
		T.transform(up);
		updateUVNAxes();
	}

	/**
	 * Translate the camera in World space in such a way the projection on view Plane translates accordingly.
	 * It is achieved by translating eye(camera position) as well as focus by x and y along U and V axis (in world space) respectively. 
	 * 
	 * @param x Amount camera translates along its right (U axis).
	 * @param y Amount camera translates along its up (V axis)
	 */
	public void translate(double x, double y) {
		//Vector3D nv;
		//nv=eye.subtract(focus);
		//nv.addEq(iUVN[0].scale(x));
		//nv.addEq(iUVN[1].scale(y));
		//eye=nv.add(focus);
		eye=eye.subtract(iUVN[0].scale(x));
		eye=eye.add(iUVN[1].scale(y));
		//focus=focus.add(iUVN[0].scale(x));
		//focus=focus.add(iUVN[1].scale(y));
		updateUVNAxes();
	}
	
	/**
	 * Banks camera,or we can say rolls cameras UP vector clockwise, so that the projection on view Plane seems to be rotating
	 * about a line joining focus and eye(N Vector).
	 * @param theta the angle in Degrees by which camera UP vector rotates clockwise.
	 */
	public void bank(double theta){
		theta=theta*Math.PI/180;
		// To rotate U by angle t along V
		// U=Ucos(t)+Vsin(t);
		// since U and V are perpendicular unit Vectors U obtained here will be a unit vector.
		
		up.scaleEq(Math.cos(theta));
		up.addEq(iUVN[0].scale(Math.sin(theta)));
		up.normalize();
		//iUVN[0].set(up.getUnitVector());
		updateUVNAxes();
	}
	
	/**
	 * Banks camera,or we can say rolls cameras UP vector clockwise, so that the projection on view Plane seems to be rotating
	 * about a line joining focus and eye(N Vector).
	 * @param theta the angle in Degrees by which camera UP vector rotates clockwise.
	 */
	public void forward(double delta){
		//eye.addEq(eye.scale(delta));
		//updateUVNAxes();
	}
	

}
