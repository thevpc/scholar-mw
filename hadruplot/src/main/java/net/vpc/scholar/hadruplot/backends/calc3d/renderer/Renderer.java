package net.vpc.scholar.hadruplot.backends.calc3d.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.vpc.scholar.hadruplot.backends.calc3d.core.Globalsettings;
import net.vpc.scholar.hadruplot.backends.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.backends.calc3d.engine3d.Light3D;
import net.vpc.scholar.hadruplot.backends.calc3d.engine3d.Scene3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.bsp.BSPTreeTraverseListener;
import net.vpc.scholar.hadruplot.backends.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.backends.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Constants;
import net.vpc.scholar.hadruplot.backends.calc3d.math.MathUtils;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.utils.ColorUtils;

public final class Renderer implements BSPTreeTraverseListener{
	private final static Logger LOG = Logger
			.getLogger(Renderer.class.getName());

	// ;;;;;;;;;;;;;;;
	private boolean isBoxed = true, isMesh = true, isScaleBox = true,
			isDisplayXY = true, isDisplayZ = true, isDisplayGrids = true;
	private float xmin, xmax, ymin;
	private float ymax, zmin, zmax;
	private String xLabel = "X";
	private String yLabel = "Y";
	// ''''''''''''''''''''''''''''
	
    // constants
	private static final int TOP = 0;
	private static final int CENTER = 1;
	private static final int BOTTOM = 2;
	
	private RasterSettings iRasterSettings = null;

	private boolean backfaceCullingEnabled = false;
	private boolean perspectiveEnabled = false;
	private boolean stereoscopyEnabled=false;
	private boolean zBuffer = true;
	private Color backgroundColor ;//= Color.LIGHT_GRAY.brighter();
	/** True if Lights is/are enabled */
	private boolean lightsEnabled = true;
	
	/** True if Polygon has two different faces to be rendered*/
	private boolean doublefaceEnabled = false;
	
	/** True if anti-aliasing should be used (2X MSAA) */
	private boolean antiAliasingEnabled = false;
	
	/**Stereoscopic mode constants*/
	public final int STEREO_NOANAGLYP=0;
	public final int STEREO_REDGREENANGLYP=1;
	public final int  STEREO_COLORANAGLYP=2;
	public final int  STEREO_OPTIMISEDANGLYP=3;
	public final int  STEREO_NOANAGLYPLEFT=4;
	public final int  STEREO_NOANAGLYPRIGHT=5;
	/** SteroScopic Mode (3D anaglyph) */
	private int stereoscopyMode = 3;
	

	/**Draw mode (polygon fill) constants*/
	public final int DRAWMODE_WIREFRAME=0;
	public final int DRAWMODE_SOLID=1;
	public final int DRAWMODE_SOLIDFLAT=2;
	public final int DRAWMODE_GRAYSCALE=3;
	private int drawMode = 3;
	
	/**HSR mode (Hidden Surface removal) constants*/
	public final int HSR_ZSORT=0;
	public final int HSR_BSPTREE=1;
	public final int HSR_ZBUFFER=2;
	private int hsrMode =1;
	
	private Canvas3D iCanvas; 
	private Camera3D iCamera; 
	private AffineTransform3D iViewportMat = new AffineTransform3D();
	public AffineTransform3D iTransformationMatrix = new AffineTransform3D();
    private AffineTransform3D iProjectToScreenMatrix=new AffineTransform3D();
	private float iAspectRatio = 1;

	private Vector3D[] iTransformedVectors1 = new Vector3D[4];
	private Vector3D[] iTransformedVectors2 = new Vector3D[4];

	/** Vector used in backface culling algorithm. */
	private Vector3D iCameraDirection = new Vector3D();
    //testing
	private Vector3D cameraLocation=new Vector3D();
	
	private Vector3D iTempVec1 = new Vector3D();
	private AffineTransform3D iTempMat1 = new AffineTransform3D();
	private StringBuilder iTempStr = new StringBuilder(24);
	private Color iTempCol1 = new Color(0);
	private Vector3D iLightVec = new Vector3D();

	private Light3D[] iLight;
	
	   /** Fog parameters. */
    public double fogStart=3.6;public double fogEnd=7;

	/**
	 * Image to act as BackBuffer for flicker free and fast drawing
	 */
	private BufferedImage dbImage;
	
	private Box3D boundingBox;
	
	private boolean fogEnabled=true;
	private Globalsettings globalsettings;
	public Renderer(Globalsettings globalsettings) {
		this.globalsettings=globalsettings;
		// initialise array to hold a transformed triangle's vectors
		for (int i = 0; i < 3; ++i) {
			iTransformedVectors1[i] = new Vector3D();
			iTransformedVectors2[i] = new Vector3D();
		}
		iRasterSettings = new RasterSettings();
		backgroundColor = new Color(230,230,240);
	}

	public void setLightCameraandImage(Canvas3D canvas,Light3D[] light,Camera3D camera, BufferedImage bImg){
		iCanvas=canvas;
		iCamera=camera;
		iLight= light;
		dbImage=bImg;
		iRasterSettings.iGraphics=dbImage.createGraphics();
		if(antiAliasingEnabled)iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public synchronized void render(Scene3D scene) {
		if (dbImage.getWidth()<2) return;
		try {
			final long startTime = System.nanoTime();
			// rasteriser settings
			//iRasterSettings.iGraphics = aGraphics;
			//setBackgroundColor(Color.red);
			iRasterSettings.iGraphics.setColor(backgroundColor);
			iRasterSettings.iGraphics.fillRect(0,0,iRasterSettings.iViewportDimensions.width,
			                                       iRasterSettings.iViewportDimensions.height);
			// reset rasteriser
			Rasteriser.getInstance().resetForNextFrame(iRasterSettings);
	        //Set up camera local axes
			setupCamera();
			// render the scene
		    renderScene(scene);
			// show fps
			final long timeTakenInNanos = System.nanoTime() - startTime + 1; // >0
			iTempStr.setLength(0);
			iTempStr.append(Math
					.round((1.0f / (timeTakenInNanos / 1000000000.0f))));
			iTempStr.append(" fps");
			iRasterSettings.iGraphics.setColor(Color.red);
			iRasterSettings.iGraphics.setFont(new Font("Serif",Font.PLAIN,10));
		//	drawBox();
		} catch (Exception e) {
			LOG.error("Error rendering frame", e);
		}
	}

	/**
	 * Traverse down the scenegraph starting at the given node and processing
	 * all its kids.
	 * 
	 * @param aNode
	 *            the node to start from. If null then nothing happens.
	 * 
	 */
	
	private void renderScene(Scene3D scene) {
		//iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// drawBoxGridsTicksLabels(iRasterSettings.iGraphics,false);
		process(scene);
		//drawAxes();//
		//drawBox();
	    //drawTicks();
	}

	/**
	 * Setup the rendering camera.
	 * 
	 * This always gets called after {@link #setupViewport(Dimensions2D)} has
	 * been called.
	 * 
	 * @param aCam
	 *            the camera. If null then is unused.
	 */
	protected void setupCamera() {
		/* setup camera matrix */
		iCameraDirection.set(iCamera.focus);
		iCameraDirection.subtractEq(iCamera.eye);
		cameraLocation=iCamera.eye;
		iCameraDirection.normalize();
		perspectiveEnabled = !iCamera.isOrthographic();
		iTransformationMatrix.set(iCamera.getCameraMatrix());
		iTransformationMatrix.concatenate(iCamera.getProjectionMatrix());
		iProjectToScreenMatrix.set(iTransformationMatrix);
		iProjectToScreenMatrix.concatenate(iViewportMat);
	}

	/**
	 * Setup the rendering viewport.
	 * 
	 * This always gets called before {@link #setupCamera(Camera)} has been
	 * called.
	 * 
	 * @param aViewport   the viewport dimensions.
	 * @param cx Centre of Viewport (Screen) 
	 * @param cy Centre of ViewPort (Screen), 
	 * where (0,0) corresponds to midpoint of viewport           
	 */
	protected void setupViewport(Dimension aViewport,double cx, double cy) {
	
		iRasterSettings.iViewportDimensions.setSize(aViewport);
        //create Image
		//dbImage=new BufferedImage(aViewport.width+1,aViewport.height+1,BufferedImage.TYPE_INT_ARGB);
	
		//iRasterSettings.iGraphics=dbImage.createGraphics();
		/**
		 * Setup world-to-viewport transformation
		 * 
		 * x_screen = (a + x_orig * a) y_screen = (b - y_orig * b)
		 * 
		 * where: a = (0.5 * width - 0.5) b = (0.5 * height - 0.5)
		 */

		float a = (float) Math
				.floor(0.5f * iRasterSettings.iViewportDimensions.width - 0.5f);
		float b = (float) Math
				.floor(0.5f * iRasterSettings.iViewportDimensions.height - 0.5f);

		iAspectRatio = (float) iRasterSettings.iViewportDimensions.width/ (float) iRasterSettings.iViewportDimensions.height;

		iViewportMat.setToIdentity();
		iViewportMat = new AffineTransform3D
				(a / iAspectRatio,  0,        0,     0,
					 0,             b,	      0,     0,
					 0, 	        0,        1,     0, 
					 a-cx,          b-cy,     0,     1);
	}

	
	/**Projects Point in world coordinates to Screen coordinates, also applies perspective correction if active
	 * @param v Vector in World Space
	 * @return Corresponding Point on Plane of projection (pt also contains Z value for z Buffer/ zsort)
	 */
	public Vector3D ProjectToScreen(Vector3D v){
		Vector3D rv = iProjectToScreenMatrix.getTransformedVector(v);
		if (perspectiveEnabled)rv.homogenise();
		if (rv.getW()!=1) {
			System.out.println(rv.getW());
		}
		//Divide by w to Homogeneous 4D Vector
		rv.edge=v.edge;
		return rv;
	}
	
	public void processold(Scene3D scene) {
		LOG.trace("Processing mesh: " + scene);
		// arrays for depth sort;
		//if ( scene==null)return;
		ArrayList<PrimitiveElement3D> primitiveElement3DS =new ArrayList<PrimitiveElement3D>();
		
		for (Object3D<PrimitiveElement3D> obj : scene.object3Ds) primitiveElement3DS.addAll(obj.elements);
		
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			primitiveElement3D.depth=-(iTransformationMatrix.getTransformedVector(primitiveElement3D.getCentre()).getZ());
		}
		
		Collections.sort(primitiveElement3DS);
		
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
				drawElement(primitiveElement3D);
		}
    }

	public void process(Scene3D scene) {
		//LOG.trace("Processing mesh: " + scene);
		// arrays for depth sort;
		//if ( scene==null)return;
		//ArrayList<PrimitiveElement3D> primitiveElement3DS=new ArrayList<PrimitiveElement3D>();
		
		//for (Object3D<PrimitiveElement3D> obj : scene.object3Ds) primitiveElement3DS.addAll(obj.primitiveElement3DS);
	
    }
	public void processTranslation(float ax, float ay, float az) {
		LOG.trace("processTranslation: x=" + ax + ", y=" + ay + ", z=" + az);

		/* construct a translation matrix
		   iTempMat1.setIdentity();
		   iTempMat1.val[0][3] = ax;
		   iTempMat1.val[1][3] = ay;
		   iTempMat1.val[2][3] = az;
		*/
		iTempMat1 = AffineTransform3D.getTranslateInstance(ax, ay, az);
		// add to current transformation matrix
		iTransformationMatrix.preConcatenate(iTempMat1);
	}

	public void processRotation(float ax, float ay, float az) {
		LOG.trace("processRotation: ax=" + ax + ", ay=" + ay + ", az=" + az);
		/*
		 * // X rotation iTempMat1.setIdentity(); iTempMat1.val[1][1] =
		 * (float)Math.cos(ax); iTempMat1.val[1][2] = -(float)Math.sin(ax);
		 * iTempMat1.val[2][1] = (float)Math.sin(ax); iTempMat1.val[2][2] =
		 * (float)Math.cos(ax); iTransformationMatrix.multEq(iTempMat1);
		 * 
		 * // Y rotation iTempMat1.setIdentity(); iTempMat1.val[0][0] =
		 * (float)Math.cos(ay); iTempMat1.val[0][2] = (float)Math.sin(ay);
		 * iTempMat1.val[2][0] = -(float)Math.sin(ay); iTempMat1.val[2][2] =
		 * (float)Math.cos(ay); iTransformationMatrix.multEq(iTempMat1);
		 * 
		 * // Z rotation iTempMat1.setIdentity(); iTempMat1.val[0][0] =
		 * (float)Math.cos(az); iTempMat1.val[0][1] = -(float)Math.sin(az);
		 * iTempMat1.val[1][0] = (float)Math.sin(az); iTempMat1.val[1][1] =
		 * (float)Math.cos(az);
		 * 
		 * //iTransformationMatrix.multEq(iTempMat1);
		 */
		iTempMat1 = AffineTransform3D.getRotateInstance(ax, ay, az);
		iTransformationMatrix.concatenate(iTempMat1);
	}

	public void processDirectionalLight(Vector3D aVec) {
		// iLightVec.set(aVec.val[_X_], aVec.val[_Y_], aVec.val[_Z_], 0);
		iLightVec.set(aVec);
	}

	
	/**
	 * Enable or disable backface culling.
	 * 
	 * @param aVal
	 *            true to enable; false to disable. It's enabled by default.
	 */
	public void enableBackfaceCulling(boolean val) {
		backfaceCullingEnabled = val;
		LOG.info("Backface culling enabled: " + val);
	}

	/**
	 * Get whether backface culling is enabled or not.
	 * 
	 * @return true if enabled; false otherwise.
	 */
	public boolean isBackfaceCullingEnabled() {
		return backfaceCullingEnabled;
	}

	/**
	 * Enable or disable wireframe mode.
	 * 
	 * @param aVal
	 *            true to enable; false to disable. It's disabled by default.
	 */
	public void enableWireframeMode(boolean val) {
		iRasterSettings.iWireframeModeEnabled = val;
		LOG.info("Wireframe mode enabled: " + val);
	}

	/**
	 * Get whether wireframe mode is enabled or not.
	 * 
	 * @return true if enabled; false otherwise.
	 */
	public boolean isWireframeModeEnabled() {
		return iRasterSettings.iWireframeModeEnabled;
	}

	/**
	 * Enable or disable per-pixel Z-buffer.
	 * 
	 * @param aVal
	 *            true to enable; false to disable. It's enabled by default.
	 */
	public void enableZBuffer(boolean val) {
		iRasterSettings.iZBufferEnabled = val;
		zBuffer=val;
		LOG.info("Z-buffer enabled: " + val);
	}

	/**
	 * Get whether per-pixel Z-buffer is enabled or not.
	 * 
	 * @return true if enabled; false otherwise.
	 */
	public boolean isZBufferEnabled() {
		return iRasterSettings.iZBufferEnabled;
	}

	

	
	public void drawBoundingLines() {
		if (!globalsettings.boxVisible)return;
		  double minX,maxX,minY,maxY,minZ,maxZ;
	        Box3D box=globalsettings.mappedClipBox;
	        minX=box.getMinX();
			maxX=box.getMaxX();
			minY=box.getMinY();
			maxY=box.getMaxY();
			minZ=box.getMinZ();
			maxZ=box.getMaxZ();
		Vector3D vo=ProjectToScreen(new Vector3D());
		Vector3D vx=ProjectToScreen(new Vector3D(maxX,0,0));
		Vector3D vy=ProjectToScreen(new Vector3D(0,maxY,0));
		Vector3D vz=ProjectToScreen(new Vector3D(0,0,maxZ));
        Color color=ColorUtils.blendColors(getBackgroundColor(), ColorUtils.getForegroundColorFromBackgroundColor(getBackgroundColor()),0.5);
	
        double x1,x2,y1,y2,z1,z2;
		x1=(vx.getZ()>=vo.getZ())?-maxX:-minX;
		x2=(vx.getZ()<vo.getZ())?-maxX:-minX;
        y1=(vy.getZ()>=vo.getZ())?-maxY:-minY;
        y2=(vy.getZ()<vo.getZ())?-maxY:-minY;
    	z1=(vz.getZ()>=vo.getZ())?-maxZ:-minZ;
		z2=(vz.getZ()<vo.getZ())?-maxZ:-minZ;

		
		PrimitiveElement3DCurve linex=new PrimitiveElement3DCurve(new Vector3D(-x1,-y1,-z1),new Vector3D(-x2,-y1,-z1));
		PrimitiveElement3DCurve liney=new PrimitiveElement3DCurve(new Vector3D(-x1,-y1,-z1),new Vector3D(-x1,-y2,-z1));
		PrimitiveElement3DCurve linez=new PrimitiveElement3DCurve(new Vector3D(-x1,-y1,-z1),new Vector3D(-x1,-y1,-z2));
		linex.prefs().setLineColor(color);
		liney.prefs().setLineColor(color);
		linez.prefs().setLineColor(color);
		linex.prefs().setFillColor(color);
		liney.prefs().setFillColor(color);
		linez.prefs().setFillColor(color);
	
		drawElement(linex);
		drawElement(liney);
		drawElement(linez);
		
	    ArrayList<PrimitiveElement3D> edges=new ArrayList<PrimitiveElement3D>();
	    PrimitiveElement3DPoint e=new PrimitiveElement3DPoint(1,0,1);
	    e.depth=(iTransformationMatrix.getTransformedVector(e.getCentre()).getY());
	    edges.add(e);
	    
	   
	}	
	
	
	public void drawBox() {
		if (!globalsettings.boxVisible)return;
		float div=(float) globalsettings.divisions;
		float subdiv=(float) globalsettings.subdivisions;
        double minX,maxX,minY,maxY,minZ,maxZ;
        Box3D box=globalsettings.mappedClipBox;
        minX=box.getMinX();
		maxX=box.getMaxX();
		minY=box.getMinY();
		maxY=box.getMaxY();
		minZ=box.getMinZ();
		maxZ=box.getMaxZ();

		Vector3D vo=ProjectToScreen(new Vector3D());
		Vector3D vx=ProjectToScreen(new Vector3D(maxX,0,0));
		Vector3D vy=ProjectToScreen(new Vector3D(0,maxY,0));
		Vector3D vz=ProjectToScreen(new Vector3D(0,0,maxZ));

		double x1=1, y1=1, z1=1;
		double x2=1, y2=1, z2=1;
		
		//find the plane which are farthest in scene so drawn first
		x1=(vx.getZ()>=vo.getZ())?-minX:-maxX;
		y1=(vy.getZ()>=vo.getZ())?-minY:-maxY;
		z1=(vz.getZ()>=vo.getZ())?-minZ:-maxZ;
		x2=(vx.getZ()<vo.getZ())?-minX:-maxX;
		y2=(vy.getZ()<vo.getZ())?-minY:-maxY;
		z2=(vz.getZ()<vo.getZ())?-minZ:-maxZ;
		
		Color color=ColorUtils.blendColors(getBackgroundColor(), ColorUtils.getForegroundColorFromBackgroundColor(getBackgroundColor()),0.5);
		
		/* Draw BackPlanes*/
		Clip clip = new Clip(globalsettings.mappedClipBox);
		ArrayList<PrimitiveElement3D> planes = new ArrayList<PrimitiveElement3D>();
		PrimitiveElement3DPoly ex = clip.getClippedPolygonfromPlane(new Plane3D(1, 0, 0,
				x1));
		ex.reCalculateNormalandCentre();
		ex.depth = -(iTransformationMatrix.getTransformedVector(ex.getCentre())
				.getZ());
		ex.prefs().setFillColor(globalsettings.planeColor);
		ex.prefs().setBackColor(globalsettings.planeColor);
		
		ex.prefs().setLineColor(globalsettings.gridsVisible ?Color.gray.darker():color);
		ex.setFilled(globalsettings.planesVisible);
		ex.prefs().setCurveWidth(globalsettings.gridsVisible ? 0 : 1);
		PrimitiveElement3DPoly ey = clip.getClippedPolygonfromPlane(new Plane3D(0, 1, 0,
				y1));
		ey.reCalculateNormalandCentre();
		ey.depth = -(iTransformationMatrix.getTransformedVector(ey.getCentre())
				.getZ());
		ey.prefs().setFillColor(globalsettings.planeColor);
		ey.prefs().setBackColor(globalsettings.planeColor);

		ey.setFilled(globalsettings.planesVisible);
		ey.prefs().setLineColor(globalsettings.gridsVisible ?Color.gray.darker():color);
		ey.prefs().setCurveWidth(globalsettings.gridsVisible ? 0 : 1);

		PrimitiveElement3DPoly ez = clip.getClippedPolygonfromPlane(new Plane3D(0, 0, 1,
				z1));
		ez.reCalculateNormalandCentre();
		ez.depth = -(iTransformationMatrix.getTransformedVector(ez.getCentre())
				.getZ());
		ez.prefs().setFillColor(globalsettings.planeColor);
		ez.prefs().setBackColor(globalsettings.planeColor);

		ez.setFilled(globalsettings.planesVisible);
		
		ez.prefs().setLineColor(globalsettings.gridsVisible ?Color.gray.darker():color);
		ez.prefs().setCurveWidth(globalsettings.gridsVisible ? 0 : 1);
		planes.add(ex);
		planes.add(ey);
		planes.add(ez);
		Collections.sort(planes);
		drawElements(planes);
		
		/*Draw Grids on backPlanes*/
		if (globalsettings.gridsVisible) {
			double delta;
			color = Color.gray;
			Vector3D v1, v2;
			// Plane parallel to X grids
			delta = (maxY-minY) / (div * subdiv);
			for (int i = 0; i <= div; i++) {
				for (int j = 0; j < subdiv; j++) {
					color = (j == 0) ? Color.gray : Color.gray.brighter();
					delta = (maxY-minY) / (div * subdiv);
					v1 = new Vector3D(-x1, minY + (i * subdiv + j) * delta,
							minZ);
					v2 = new Vector3D(-x1, minY + (i * subdiv + j) * delta,
							maxZ);
					PrimitiveElement3D e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);
					drawElement(e);
					delta = (maxZ-minZ) / (div * subdiv);
					v1 = new Vector3D(-x1, minY, minZ + (i * subdiv + j)
							* delta);
					v2 = new Vector3D(-x1, maxY, minZ + (i * subdiv + j)
							* delta);
					e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);
					drawElement(e);
					if (i == div)
						break;
				}
			}
			
		// Plane parallel to Y grids
			for (int i = 0; i <= div; i++) {
				for (int j = 0; j < subdiv; j++) {
					color = (j == 0) ? Color.gray : Color.gray.brighter();
					delta = (maxX-minX) / (div * subdiv);
					v1 = new Vector3D(minX+ (i * subdiv + j) * delta, -y1,
							minZ);
					v2 = new Vector3D(minX + (i * subdiv + j) * delta, -y1,
							maxZ);
					PrimitiveElement3D e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);
					drawElement(e);
					delta = (maxZ-minZ) / (div * subdiv);
					v1 = new Vector3D(minX, -y1, minZ + (i * subdiv + j)
							* delta);
					v2 = new Vector3D(maxX, -y1, minZ + (i * subdiv + j)
							* delta);
					e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);

					drawElement(e);
					if (i == div)
						break;
				}

			}

			// Plane parallel to Z grids
			for (int i = 0; i <= div; i++) {
				for (int j = 0; j < subdiv; j++) {
					color = (j == 0) ? Color.gray : Color.gray.brighter();
					delta = (maxX-minX) / (div * subdiv);
					v1 = new Vector3D(minX + (i * subdiv + j) * delta, minY,
							-z1);
					v2 = new Vector3D(minX + (i * subdiv + j) * delta, maxY,
							-z1);
					PrimitiveElement3D e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);

					drawElement(e);
					delta = (maxY-minY) / (div * subdiv);
					
					v1 = new Vector3D(minX, minY + (i * subdiv + j) * delta,
							-z1);
					v2 = new Vector3D(maxX, minY + (i * subdiv + j) * delta,
							-z1);
					e = new PrimitiveElement3DCurve(v1, v2);
					e.prefs().setLineColor(color);
					e.prefs().setFillColor(color);
					drawElement(e);
					if (i == div)
						break;
				}
			}
		}
		
	/*draw Axis division values*/	
		if (globalsettings.rulersVisible) {
		
			vo = new Vector3D(-x1, -y1, -z1);
			vx = ProjectToScreen(new Vector3D(-x2, -y1, -z1));
			vy = ProjectToScreen(new Vector3D(-x1, -y2, -z1));
			vz = ProjectToScreen(new Vector3D(-x1, -y1, -z2));

			Vector3D vCentre = ProjectToScreen(new Vector3D(0, 0, 0));
			if ((vx.getY() >= vz.getY()) && (vy.getY() >= vz.getY())) {
				vo = new Vector3D(-x2, -y2, -z1);
				drawRulerAxis(vo, new Vector3D(-x2, -y1, -z1),
						globalsettings.mapCliptoY(-y2),
						globalsettings.mapCliptoY(-y1), div, subdiv,
						new Vector3D(-x1, 0, 0));
				drawRulerAxis(vo, new Vector3D(-x1, -y2, -z1),
						globalsettings.mapCliptoX(-x2),
						globalsettings.mapCliptoX(-x1), div, subdiv,
						new Vector3D(0, -y1, 0));
				if (ProjectToScreen(new Vector3D(-x1, -y2, 0)).getZ() > ProjectToScreen(
						new Vector3D(-x2, -y1, 0)).getZ()) {
					vo = new Vector3D(-x1, -y2, -z1);
					drawRulerAxis(vo, new Vector3D(-x1, -y2, -z2),
							globalsettings.mapCliptoZ(-z1),
							globalsettings.mapCliptoZ(-z2), div, subdiv,
							new Vector3D(0, -y1, 0));
				} else {
					vo = new Vector3D(-x2, -y1, -z1);
					drawRulerAxis(vo, new Vector3D(-x2, -y1, -z2),
							globalsettings.mapCliptoZ(-z1),
							globalsettings.mapCliptoZ(-z2), div, subdiv,
							new Vector3D(-x1, 0, 0));

				}

			} else if ((vx.getY() >= vy.getY()) && (vz.getY() >= vy.getY())) {
				vo = new Vector3D(-x2, -y1, -z2);
				drawRulerAxis(vo, new Vector3D(-x1, -y1, -z2),
						globalsettings.mapCliptoX(-x2),
						globalsettings.mapCliptoX(-x1), div, subdiv,
						new Vector3D(0, 0, -z1));
				drawRulerAxis(vo, new Vector3D(-x2, -y1, -z1),
						globalsettings.mapCliptoZ(-z2),
						globalsettings.mapCliptoZ(-z1), div, subdiv,
						new Vector3D(-x1, 0, 0));

				if (ProjectToScreen(new Vector3D(-x1, 0, -z2)).getZ() > ProjectToScreen(
						new Vector3D(-x2, 0, -z1)).getZ()) {
					vo = new Vector3D(-x1, -y1, -z2);
					drawRulerAxis(vo, new Vector3D(-x1, -y2, -z2),
							globalsettings.mapCliptoY(-y1),
							globalsettings.mapCliptoY(-y2), div, subdiv,
							new Vector3D(0, 0, -z1));
				} else {
					vo = new Vector3D(-x2, -y1, -z1);
					drawRulerAxis(vo, new Vector3D(-x2, -y2, -z1),
							globalsettings.mapCliptoY(-y1),
							globalsettings.mapCliptoY(-y2), div, subdiv,
							new Vector3D(-x1, 0, 0));
				}
			} else if ((vy.getY() >= vx.getY()) && (vz.getY() >= vx.getY())) {
				vo = new Vector3D(-x1, -y2, -z2);
				drawRulerAxis(vo, new Vector3D(-x1, -y1, -z2),
						globalsettings.mapCliptoY(-y2),
						globalsettings.mapCliptoY(-y1), div, subdiv,
						new Vector3D(0, 0, -z1));
				drawRulerAxis(vo, new Vector3D(-x1, -y2, -z1),
						globalsettings.mapCliptoZ(-z2),
						globalsettings.mapCliptoZ(-z1), div, subdiv,
						new Vector3D(0, -y1, 0));

				if (ProjectToScreen(new Vector3D(0, -y1, -z2)).getZ() > ProjectToScreen(
						new Vector3D(0, -y2, -z1)).getZ()) {
					vo = new Vector3D(-x1, -y1, -z2);
					drawRulerAxis(vo, new Vector3D(-x2, -y1, -z2),
							globalsettings.mapCliptoX(-x1),
							globalsettings.mapCliptoX(-x2), div, subdiv,
							new Vector3D(0, 0, -z1));
				} else {
					vo = new Vector3D(-x1, -y2, -z1);
					drawRulerAxis(vo, new Vector3D(-x2, -y2, -z1),
							globalsettings.mapCliptoX(-x1),
							globalsettings.mapCliptoX(-x2), div, subdiv,
							new Vector3D(0, -y1, 0));
				}

			}
		}

	}

	/**
	 * Draws interrpolated values on line joining the vectors. Used for axis scale values display
	 * @param v1
	 * @param v2
	 * @param min minimum Value on scale
	 * @param max maximum value on scale
	 * @param div no of main divisions on scale
	 * @param subdiv no of subdivisions on scale
	 * @param tickDir direction in which ruler lines are drawn it can be null if no ruler lines are drawn
	 */
	private void drawRulerAxis(Vector3D v1, Vector3D v2,double min, double max,double div,double subdiv, Vector3D tickDir){
		double delta=(max-min)/div;
		int xalign,yalign;
		xalign=getXAlignment(ProjectToScreen(v1),ProjectToScreen(v2),ProjectToScreen(new Vector3D()));
		yalign=getYAlignment(ProjectToScreen(v1),ProjectToScreen(v2),ProjectToScreen(new Vector3D()));
		
		double ticklength=0.03;
		Color color=ColorUtils.blendColors(getBackgroundColor(), ColorUtils.getForegroundColorFromBackgroundColor(getBackgroundColor()),0.5);
		iRasterSettings.iGraphics.setPaint(color);
		iRasterSettings.iGraphics.setPaint(color);
		Vector3D va,vb;
        
		for (int i=0; i<div;i++){
			//Draw values on scale
			Vector3D v=ProjectToScreen(v1.add((v2.subtract(v1)).scale((float)i/div)));
			float value=(float) (min+i*delta);
		    if (i>0 && globalsettings.labelsVisible)outFloat(iRasterSettings.iGraphics,(int)v.getX(),(int)v.getY(),value,xalign,yalign);
		   
		    //Draw ticks
			if (tickDir!=null && globalsettings.ticksVisible){
				for (int j=0; j<subdiv;j++){
					ticklength=(j==0)?0.07:0.04;
					v=v1.add((v2.subtract(v1)).scale((float)(i*subdiv+j)/(div*subdiv)));
					va=ProjectToScreen(v);
					vb=ProjectToScreen(v.add(tickDir.scale(ticklength)));
					iRasterSettings.iGraphics.draw(new Line2D.Double(va.getX(),va.getY(),vb.getX(),vb.getY()));
				}
			}
			
		}
	}
	
	/**
	 * Returns proper X alignment of ruler text
	 * @param v1 ruler's starting point
	 * @param v2 ruler's end point
	 * @param centre centre of box whose edges contain rulers
	 * @return
	 */
	private int getXAlignment(Vector3D v1, Vector3D v2,Vector3D centre){
		if (v1.getX()>=centre.getX()){
			if (v2.getX()>=centre.getX())
				return Label.LEFT;
			else
				return CENTER;	
		}else {
			if (v2.getX()<centre.getX())
				return Label.RIGHT;
			else
				return Label.CENTER;	
		}

	}
	
	/**
	 * Returns proper Y alignment of ruler text
	 * @param v1 ruler's starting point
	 * @param v2 ruler's end point
	 * @param centre centre of box whose edges contain rulers
	 * @return
	 */
	private int getYAlignment(Vector3D v1, Vector3D v2,Vector3D centre){
		if (v1.getY()>=centre.getY()){
			if (v2.getY()>=centre.getY())
				return TOP;
			else
				return CENTER;	
		}else {
			if (v2.getY()<centre.getY())
				return BOTTOM;
			else
				return CENTER;	
		}
	}
	

	/**
	 * Sets the axes scaling factor. Computes the proper axis lengths based on
	 * the ratio of variable ranges. The axis lengths will also affect the size
	 * of bounding box.
	 */
	private int t_x, t_y, t_z; // determines ticks density
	private final void setAxesScale() {
		float scale_x, scale_y, scale_z, divisor;
		int longest;

		if (!isScaleBox) {
			// projector.setScaling(1);
			t_x = t_y = t_z = 4;
			return;
		}

		scale_x = xmax - xmin;
		scale_y = ymax - ymin;
		scale_z = zmax - zmin;

		if (scale_x < scale_y) {
			if (scale_y < scale_z) {
				longest = 3;
				divisor = scale_z;
			} else {
				longest = 2;
				divisor = scale_y;
			}
		} else {
			if (scale_x < scale_z) {
				longest = 3;
				divisor = scale_z;
			} else {
				longest = 1;
				divisor = scale_x;
			}
		}
		scale_x /= divisor;
		scale_y /= divisor;
		scale_z /= divisor;

		if ((scale_x < 0.2f) || (scale_y < 0.2f) && (scale_z < 0.2f)) {
			switch (longest) {
			case 1:
				if (scale_y < scale_z) {
					scale_y /= scale_z;
					scale_z = 1.0f;
				} else {
					scale_z /= scale_y;
					scale_y = 1.0f;
				}
				break;
			case 2:
				if (scale_x < scale_z) {
					scale_x /= scale_z;
					scale_z = 1.0f;
				} else {
					scale_z /= scale_x;
					scale_x = 1.0f;
				}
				break;
			case 3:
				if (scale_y < scale_x) {
					scale_y /= scale_x;
					scale_x = 1.0f;
				} else {
					scale_x /= scale_y;
					scale_y = 1.0f;
				}
				break;
			}
		}
		if (scale_x < 0.2f)
			scale_x = 1.0f;
		// projector.setXScaling(scale_x);
		if (scale_y < 0.2f)
			scale_y = 1.0f;
		// projector.setYScaling(scale_y);
		if (scale_z < 0.2f)
			scale_z = 1.0f;
		// projector.setZScaling(scale_z);

		if (scale_x < 0.5f)
			t_x = 8;
		else
			t_x = 4;
		if (scale_y < 0.5f)
			t_y = 8;
		else
			t_y = 4;
		if (scale_z < 0.5f)
			t_z = 8;
		else
			t_z = 4;
	}

	


	public void drawElement(PrimitiveElement3D primitiveElement3D){
		if (!primitiveElement3D.isRenderable())
			return;
		if ( primitiveElement3D instanceof PrimitiveElement3DRect) {
			drawElementRect((PrimitiveElement3DRect) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DCurve){
			drawElementCurve((PrimitiveElement3DCurve) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DString){
			drawElementString((PrimitiveElement3DString) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DPoly){
			drawElementPoly((PrimitiveElement3DPoly) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DPoint){
			drawElementPoint((PrimitiveElement3DPoint) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DArrow){
			drawElementArrow((PrimitiveElement3DArrow) primitiveElement3D);
		}else if (primitiveElement3D instanceof PrimitiveElement3DCollection){
			drawElements(((PrimitiveElement3DCollection) primitiveElement3D).primitiveElement3DS);
		}
		
    }
	
	
	private void drawElementString(PrimitiveElement3DString element) {
		if (!element.isRenderable())
			return;
		Vector3D v;
		v = ProjectToScreen(element.getCentre());
		iRasterSettings.iGraphics.setColor(element.color);
		Vector3D v1,v2;
		v1=ProjectToScreen(element.p1);
		v2=ProjectToScreen(element.p2);
	
		/*get tickDir on screen*/
        double deltax=((int)v2.getX()-(int)v1.getX());
        double deltay=((int)v2.getY()-(int)v1.getY());
 
        int i,j;
        i=(v1.getX()>v2.getX())?-1:1;
        //j=(v1.getY()<v2.getY())?-1:1;
        
        //iRasterSettings.iGraphics.rotate(Math.atan2(deltay,deltax),v1.getX(),v1.getY());
        int y=(int)v1.getY();
        int x=(int)v1.getX();
        outString(iRasterSettings.iGraphics,x,y,element.string,1,1);
        //iRasterSettings.iGraphics.setTransform(new AffineTransform());
	}

	private void drawElementPoint(PrimitiveElement3DPoint element) {
		// Vector3D v=new Vector3D();
		if (!element.isRenderable()) return;
		Vector3D v=ProjectToScreen(element.getCentre());
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                  BasicStroke.CAP_BUTT,
                  BasicStroke.JOIN_MITER,
                  1f, //miter limit
                  dash,
                  0
                  ));	
		}
		iRasterSettings.iGraphics.setPaint(	element.prefs().getFillColor());
		Ellipse2D.Double ellipse=new Ellipse2D.Double(v.getX()-element.getRadius(),v.getY()-element.getRadius(),2*element.getRadius(),2*element.getRadius());
		if (element.isFilled())iRasterSettings.iGraphics.fill(ellipse);
		iRasterSettings.iGraphics.setPaint(element.prefs().getLineColor());//ColorUtils.blendColors(primitiveElement3D.curveColor,iBgColor,iLight.getBlendAmt(primitiveElement3D.depth)));
		iRasterSettings.iGraphics.draw(ellipse);
		iRasterSettings.iGraphics.drawString(element.getText(), (int)v.getX(),  (int)v.getY()-1);
	}

	private void drawElementCurve(PrimitiveElement3DCurve element) {
		if (!element.isRenderable()) return;
		Object hints=iRasterSettings.iGraphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try{
		Stroke oldStroke=iRasterSettings.iGraphics.getStroke();
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                   BasicStroke.CAP_BUTT,
                   BasicStroke.JOIN_MITER,
                   1f, //miter limit
                   dash,
                   0
                   ));	
		}
		iRasterSettings.iGraphics.setColor(getSurfaceColor(element,iLight));
		Vector3D v1,v2;
		v1=ProjectToScreen(element.p1);
		v2=ProjectToScreen(element.p2);
		
		
		iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY()));
		iRasterSettings.iGraphics.setStroke(oldStroke);
		}
		catch(Exception e) {
			System.out.println("Caught exception in elementcurvedraw"+e.getMessage());
		}
		iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hints);
	}

	private void drawElementArrow(PrimitiveElement3DArrow element) {

		if (element instanceof PrimitiveElement3DRuler) {
			drawElementRuler((PrimitiveElement3DRuler)element);
			return;
		}
		if (!element.isRenderable()) return;
		
		Object hints=iRasterSettings.iGraphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		//iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Stroke oldStroke=iRasterSettings.iGraphics.getStroke();
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                  BasicStroke.CAP_ROUND,
                  BasicStroke.JOIN_MITER,
                  1f, //miter limit
                  dash,
                  0
                  ));	
		}		Vector3D v1,v2;
		
		v1=ProjectToScreen(element.p1);
		v2=ProjectToScreen(element.p2);
		
		/*
		Vector3D v=primitiveElement3D.p2.subtract(primitiveElement3D.p1).getUnitVector();
		v.scaleEq(0.06);
		Vector3D arrowVertex1=primitiveElement3D.p2.subtract(v);
		arrowVertex1.addEq(new Vector3D(-primitiveElement3D.curveWidth*0.4*v.getY(),primitiveElement3D.curveWidth*0.4*v.getX(),0));
		Vector3D arrowVertex2=primitiveElement3D.p2.subtract(v);
		arrowVertex2.addEq(new Vector3D(primitiveElement3D.curveWidth*0.4*v.getY(),-primitiveElement3D.curveWidth*0.4*v.getX(),0));
		
		arrowVertex1=ProjectToScreen(arrowVertex1);
		arrowVertex2=ProjectToScreen(arrowVertex2);
		
		Vector3D arrowVertex3=primitiveElement3D.p2.subtract(v.scale(0.8));
		arrowVertex3=ProjectToScreen(arrowVertex3);
	
		
		iRasterSettings.iGraphics.setColor(primitiveElement3D.color);
		iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY()));
		Polygon s=new Polygon();
		s.addPoint((int)v2.getX(), (int)v2.getY());
		s.addPoint((int)arrowVertex1.getX(), (int)arrowVertex1.getY());
		s.addPoint((int)arrowVertex3.getX(), (int)arrowVertex3.getY());
		s.addPoint((int)arrowVertex2.getX(), (int)arrowVertex2.getY());
		*/
		double dx,dy,r;
		
		dx=((int)v2.getX()-(int)v1.getX());
		dy=((int)v2.getY()-(int)v1.getY());
		r=Math.sqrt(dx*dx+dy*dy);
		if (r==0) {
		   	iRasterSettings.iGraphics.setColor(element.prefs().getLineColor());
			iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY()));
			return;
		}
		dx/=r;
		dy/=r;
		double x,y;
		//primitiveElement3D.setArrowSize(5);
		x=((int)v2.getX()-2*dx*element.getArrowSize());
		y=((int)v2.getY()-2*dy*element.getArrowSize());
		double temp;
        temp=dx;
        dx=dy;
        dy=-temp;
        Polygon s=new Polygon();
        s.addPoint((int)v2.getX(), (int)v2.getY());
		s.addPoint((int)(x+dx*element.getArrowSize()), (int)(y+dy*element.getArrowSize()));
		s.addPoint((int)(0.2*v2.getX()+.8*x),(int)(0.2*v2.getY()+0.8*y));
		s.addPoint((int)(x-dx*element.getArrowSize()), (int)(y-dy*element.getArrowSize()));
		
		iRasterSettings.iGraphics.setColor(element.prefs().getFillColor());
	   	iRasterSettings.iGraphics.fill(s);
	   	iRasterSettings.iGraphics.setColor(element.prefs().getLineColor());
		iRasterSettings.iGraphics.setColor(getSurfaceColor(element,iLight));
	   	iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY()));
      	iRasterSettings.iGraphics.draw(s);
      
      	iRasterSettings.iGraphics.setStroke(oldStroke);
      	iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hints);
	}
	
    private void drawElementRulerNakli(PrimitiveElement3DRuler element){
    	if (!element.isRenderable()) return;
		Stroke oldStroke=iRasterSettings.iGraphics.getStroke();
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                  BasicStroke.CAP_ROUND,
                  BasicStroke.JOIN_MITER,
                  1f, //miter limit
                  dash,
                  0
                  ));	
		}		
		
		Vector3D v1,v2,v3,v4;
		
		double div,subDiv;
		if (null!=element.getParent()){
		v1=ProjectToScreen(element.getParent().p1);
		v2=ProjectToScreen(element.getParent().p2);
		div=element.getParent().getDivisions();
		subDiv=element.getParent().getSubdivisions();
	
		}else{
		v1=ProjectToScreen(element.p1);
		v2=ProjectToScreen(element.p2);
		div=element.getDivisions();
		subDiv=element.getSubdivisions();
		
		}
			v3=ProjectToScreen(element.p1);
		v4=ProjectToScreen(element.p2);
		/*Draw ruler base line*/
	  	iRasterSettings.iGraphics.setColor(element.prefs().getLineColor());
		iRasterSettings.iGraphics.draw(new Line2D.Double(v3.getX(),v3.getY(),v4.getX(),v4.getY()));

		/*get tickDir on screen*/
		double dx,dy,r;
		if (null != element.getTickDir()) {
			 Vector3D temp1=new Vector3D(0,0,0);
	         Vector3D temp2=element.getTickDir();
	         temp1=ProjectToScreen(temp1);
	    	 temp2=ProjectToScreen(temp2);
	    	 temp2.subtractEq(temp1);
	    	 temp2.scaleEq(0.015);
	    	 if (temp2.getLengthSq()<10e-10)return;
	    	 //temp2.normalize();
	    	 dx=temp2.getX();
	    	 dy=temp2.getY();
		} else {
			 dx = ((int) v2.getX() - (int) v1.getX());
			 dy = ((int) v2.getY() - (int) v1.getY());
			 r = Math.sqrt(dx * dx + dy * dy);
			 if (r == 0) return;
			 dx /= r;
			 dy /= r;
			 double temp;
			 temp = dx;
			 dx = dy;
			 dy = -temp;
		}

        double deltax=((int)v2.getX()-(int)v1.getX())/(div*subDiv);
        double deltay=((int)v2.getY()-(int)v1.getY())/(div*subDiv);
        double tickLength=6;
       
        int cross=(int) Math.signum(deltax*dy-deltay*dx);
        for (int i=0;i<div;i++){
            for (int j=0;j<subDiv;j++){
        		tickLength=(j==0)?6:3;
        		double x,y;
        		x=v1.getX()+(i*subDiv+j)*deltax;
        		y=v1.getY()+(i*subDiv+j)*deltay;
        		if (element.getParent()==null)
        			iRasterSettings.iGraphics.draw(new Line2D.Double(x,y, x+tickLength*dx,y+tickLength*dy));
        		else if (((v3.getX()-x)*(v4.getX()-x)<=0)&&((v3.getY()-y)*(v4.getY()-y)<=0))
        			iRasterSettings.iGraphics.draw(new Line2D.Double(x,y, x+tickLength*dx,y+tickLength*dy));
        	}
        }
        tickLength=6;
		iRasterSettings.iGraphics.draw(new Line2D.Double(v2.getX(),v2.getY(),v2.getX()+tickLength*dx,v2.getY()+tickLength*dy));

		if( !element.isValuesShown()) return;
		
        double length=(v1.subtract(v2)).getLength();
     double theta;
     if (deltay==0)
    	 theta=(deltax>0)?Math.PI/2:-Math.PI/2;
     else if(deltay>0)
    	 theta=(deltax>0)?deltax/ deltay:-deltax/ deltay;	 
     else
    	 theta=(deltax>0)?-deltax/ deltay:Math.PI+deltax/ deltay;	 
  //   	iRasterSettings.iGraphics.translate(v1.getX(),v1.getY());
     iRasterSettings.iGraphics.rotate(-Math.atan2(v2.getX()-v1.getX(),v2.getY()-v1.getY()),v1.getX(),v1.getY());
     deltay=length/(div*subDiv);
    
    // iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v1.getX(),v1.getY()+length));
    	
        for (int i=0;i<div;i++){
            /*
        	for (int j=0;j<primitiveElement3D.getSubdivisions();j++){
        		tickLength=(j==0)?6:3;
        		iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY()+(i*primitiveElement3D.getSubdivisions()+j)*deltay,
        				v1.getX()-tickLength,
        				v1.getY()+(i*primitiveElement3D.getSubdivisions()+j)*deltay));
        	}
        	*/	
        	int y=(int) (v1.getY()+(i*subDiv)*deltay);
        	if ((element.getParent()==null)||((v1.getY()-y)*(v2.getY()-y)<0))
            if (i>0)outFloat(iRasterSettings.iGraphics,(int)v1.getX()+4*cross,y,
            		       (float) (element.getMinValue()+(float)i/subDiv*(element.getMaxValue()-element.getMinValue())),-cross+1,1);

        }
        
       // tickLength=6;
	
		
    	//iRasterSettings.iGraphics.rotate(Math.atan(-dx / dy),v1.getX()+(i*primitiveElement3D.getSubdivisions())*deltax,v1.getY()+(i*primitiveElement3D.getSubdivisions())*deltay);
		//iRasterSettings.iGraphics.drawString(i+"", (int)( v1.getX()+(i*primitiveElement3D.getSubdivisions())*deltax) -12,(int)( v1.getY()+(i*primitiveElement3D.getSubdivisions())*deltay) );
		iRasterSettings.iGraphics.setTransform(new AffineTransform());
    
     }
	
    private void drawElementRuler(PrimitiveElement3DRuler element){
    	if (!element.isRenderable()) return;
		Stroke oldStroke=iRasterSettings.iGraphics.getStroke();
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                  BasicStroke.CAP_ROUND,
                  BasicStroke.JOIN_MITER,
                  1f, //miter limit
                  dash,
                  0
                  ));	
		}		
		
		Vector3D v1,v2,v3,v4;
		
		double div,subDiv;
		v1=ProjectToScreen(element.p1);
		v2=ProjectToScreen(element.p2);
		div=element.getDivisions();
		subDiv=element.getSubdivisions();
	
		/*Draw ruler base line*/
	  	iRasterSettings.iGraphics.setColor(element.prefs().getLineColor());
		iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY()));

		/*get tickDir on screen*/
		double dx,dy,r;
		if (null != element.getTickDir()) {
			 Vector3D temp1=new Vector3D(0,0,0);
	         Vector3D temp2=element.getTickDir();
	         temp1=ProjectToScreen(temp1);
	    	 temp2=ProjectToScreen(temp2);
	    	 temp2.subtractEq(temp1);
	    	 temp2.scaleEq(0.015);
	    	 if (temp2.getLengthSq()<10e-10)return;
	    	 //temp2.normalize();
	    	 dx=temp2.getX();
	    	 dy=temp2.getY();
		} else {
			 dx = ((int) v2.getX() - (int) v1.getX());
			 dy = ((int) v2.getY() - (int) v1.getY());
			 r = Math.sqrt(dx * dx + dy * dy);
			 if (r == 0) return;
			 dx /= r;
			 dy /= r;
			 double temp;
			 temp = dx;
			 dx = dy;
			 dy = -temp;
		}

        double deltax=((int)v2.getX()-(int)v1.getX())/(div*subDiv);
        double deltay=((int)v2.getY()-(int)v1.getY())/(div*subDiv);
        double tickLength=6;
       
        int cross=(int) Math.signum(deltax*dy-deltay*dx);
        for (int i=0;i<div;i++){
            for (int j=0;j<subDiv;j++){
        		tickLength=(j==0)?6:3;
        		double x,y;
        		x=v1.getX()+(i*subDiv+j)*deltax;
        		y=v1.getY()+(i*subDiv+j)*deltay;
        		iRasterSettings.iGraphics.draw(new Line2D.Double(x,y, x+tickLength*dx,y+tickLength*dy));
        	}
        }
        tickLength=6;
		iRasterSettings.iGraphics.draw(new Line2D.Double(v2.getX(),v2.getY(),v2.getX()+tickLength*dx,v2.getY()+tickLength*dy));

		if( !element.isValuesShown()) return;
		
        double length=(v1.subtract(v2)).getLength();
     double theta;
     if (deltay==0)
    	 theta=(deltax>0)?Math.PI/2:-Math.PI/2;
     else if(deltay>0)
    	 theta=(deltax>0)?deltax/ deltay:-deltax/ deltay;	 
     else
    	 theta=(deltax>0)?-deltax/ deltay:Math.PI+deltax/ deltay;	 
  //   	iRasterSettings.iGraphics.translate(v1.getX(),v1.getY());
     iRasterSettings.iGraphics.rotate(-Math.atan2(v2.getX()-v1.getX(),v2.getY()-v1.getY()),v1.getX(),v1.getY());
     deltay=length/(div*subDiv);
    
    // iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY(),v1.getX(),v1.getY()+length));
    	
        for (int i=0;i<div;i++){
            /*
        	for (int j=0;j<primitiveElement3D.getSubdivisions();j++){
        		tickLength=(j==0)?6:3;
        		iRasterSettings.iGraphics.draw(new Line2D.Double(v1.getX(),v1.getY()+(i*primitiveElement3D.getSubdivisions()+j)*deltay,
        				v1.getX()-tickLength,
        				v1.getY()+(i*primitiveElement3D.getSubdivisions()+j)*deltay));
        	}
        	*/	
        	int y=(int) (v1.getY()+(i*subDiv)*deltay);
        	if ((element.getParent()==null)||((v1.getY()-y)*(v2.getY()-y)<0))
            if (i>0)outFloat(iRasterSettings.iGraphics,(int)v1.getX()+4*cross,y,
            		       (float) (element.getMinValue()+(float)i/subDiv*(element.getMaxValue()-element.getMinValue())),-cross+1,1);

        }
        
       // tickLength=6;
	
		
    	//iRasterSettings.iGraphics.rotate(Math.atan(-dx / dy),v1.getX()+(i*primitiveElement3D.getSubdivisions())*deltax,v1.getY()+(i*primitiveElement3D.getSubdivisions())*deltay);
		//iRasterSettings.iGraphics.drawString(i+"", (int)( v1.getX()+(i*primitiveElement3D.getSubdivisions())*deltax) -12,(int)( v1.getY()+(i*primitiveElement3D.getSubdivisions())*deltay) );
		iRasterSettings.iGraphics.setTransform(new AffineTransform());
    
     }
	private void drawElementRect(PrimitiveElement3DRect element) {
		// Vector3D v=new Vector3D();
		if (!element.isRenderable()) return;
		
		Polygon s=new Polygon();
		for (Vector3D v: element.vertices ){
			 v=ProjectToScreen(v);
			 s.addPoint((int)v.getX(),(int)v.getY());
		}
		iRasterSettings.iGraphics.setPaint(	getSurfaceColor(element,iLight));
		iRasterSettings.iGraphics.fillPolygon(s);
		iRasterSettings.iGraphics.setPaint(element.prefs().getLineColor());//ColorUtils.blendColors(primitiveElement3D.curveColor,iBgColor,iLight.getBlendAmt(primitiveElement3D.depth)));
		//iRasterSettings.iGraphics.drawPolygon(s);
	}

	
	private void drawElementPoly(PrimitiveElement3DPoly element) {
		// Vector3D v=new Vector3D();
		if (!element.isRenderable()) return;
		//if (primitiveElement3D.vertices.size()<=2) return;
		if (element.normal==null) return;
		Polygon s=new Polygon();
		for (Vector3D v: element.vertices ){
			 v=ProjectToScreen(v);
			 s.addPoint((int)v.getX(),(int)v.getY());
		}
		
		if (element.isFilled()){
		     iRasterSettings.iGraphics.setPaint(getSurfaceColor(element,iLight));//primitiveElement3D.color);//	/
	         iRasterSettings.iGraphics.fillPolygon(s);
		}
		
	    //Skip border if curvewidth=0
		if (element.prefs().getCurveWidth()==0){
			return;
		}
		
		//Draw wireframe
		if (!element.isFilled()){
			iRasterSettings.iGraphics.setPaint(getBlendedLineColor(element));
			iRasterSettings.iGraphics.setStroke(new BasicStroke(Math.max(1,element.prefs().getCurveWidth()),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			iRasterSettings.iGraphics.drawPolygon(s);
			return;
		}
		
		
		iRasterSettings.iGraphics.setPaint(getBlendedLineColor(element));//ColorUtils.blendColors(primitiveElement3D.curveColor,iBgColor,iLight.getBlendAmt(primitiveElement3D.depth)));

		Stroke oldStroke=iRasterSettings.iGraphics.getStroke();
		if (!element.prefs().isDashed())
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(element.prefs().getCurveWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		else {
			 float dash[] = {3.0f,3.0f};
		     iRasterSettings.iGraphics.setStroke(new BasicStroke(
				   element.prefs().getCurveWidth(),
                  BasicStroke.CAP_ROUND,
                  BasicStroke.JOIN_MITER,
                  1f, //miter limit
                  dash,
                  0
                  ));	
		}
		
		if (!element.drawContours){
			iRasterSettings.iGraphics.draw(s);
		}else{
			Vector3D v1 = element.vertices.get(0);
			for (int i = 1; i < element.vertices.size(); i++) {
				Vector3D v2 = element.vertices.get(i);
				if ((Math.abs(v1.getY() - v2.getY()) < 10e-6)
						|| (Math.abs(v1.getX() - v2.getX()) < 10e-6)) {
					Vector3D v3 = ProjectToScreen(v1);
					Vector3D v4 = ProjectToScreen(v2);
					iRasterSettings.iGraphics.drawLine((int) v3.getX(),
							(int) v3.getY(), (int) v4.getX(), (int) v4.getY());
				}
				v1 = v2;
			}
			Vector3D v2 = element.vertices.get(0);
			if ((Math.abs(v1.getY() - v2.getY()) < 10e-6)
					|| (Math.abs(v1.getX() - v2.getX()) < 10e-6)) {
				Vector3D v3 = ProjectToScreen(v1);
				Vector3D v4 = ProjectToScreen(v2);
				iRasterSettings.iGraphics.drawLine((int) v3.getX(),
						(int) v3.getY(), (int) v4.getX(), (int) v4.getY());
			}
		}
	
		
	}
	
	
	private Color getBlendedLineColor(PrimitiveElement3D primitiveElement3D){
		Color color;
		color= primitiveElement3D.prefs().getLineColor();
		if (globalsettings.fogEnabled) {
			double blendAmt = getBlendAmt(primitiveElement3D.depth);
			return ColorUtils.blendColors(color,
					backgroundColor, blendAmt);}
		else{
			return color;
		}
	}
	
	
	
	private Color getSurfaceColor(PrimitiveElement3D primitiveElement3D, Light3D[] lights) {
	
		if (primitiveElement3D instanceof PrimitiveElement3DString) {
			double blendAmt = getBlendAmt(primitiveElement3D.depth);
			return ColorUtils.blendColors(primitiveElement3D.prefs().getFillColor(),
					backgroundColor, blendAmt);
		} else if (primitiveElement3D instanceof PrimitiveElement3DCurve) {
			double blendAmt = getBlendAmt(primitiveElement3D.depth);
			return ColorUtils.blendColors(primitiveElement3D.prefs().getFillColor(),
					backgroundColor, blendAmt);
		} else if ((primitiveElement3D instanceof PrimitiveElement3DRect)
				|| (primitiveElement3D instanceof PrimitiveElement3DPoly)) {
			Vector3D n = new Vector3D();
			if (primitiveElement3D instanceof PrimitiveElement3DPoly) {
				n = ((PrimitiveElement3DPoly) primitiveElement3D).normal;
				if ((null == n) || (n.getLengthSq() < Constants.EPSILON))
					return primitiveElement3D.prefs().getFillColor();
				else
					n = ((PrimitiveElement3DPoly) primitiveElement3D).normal;
			}
           
						if(globalsettings.fogEnabled){
			double blendAmt = getBlendAmt(primitiveElement3D.depth);
			iTempCol1= ColorUtils.blendColors(primitiveElement3D.prefs().getFillColor(),
					backgroundColor, blendAmt);
			}else{
			iTempCol1 = primitiveElement3D.prefs().getFillColor();
			}
						
			if(n.DotProduct(iCamera.eye)>0)iTempCol1= primitiveElement3D.prefs().getBackColor();
			

			if (!lightsEnabled)	return iTempCol1;

			
			double intensity = 0;
			/**
			 * shinyNess: Lower values will spread out the shine shineIntensity:
			 * Maximum intensity of the shine
			 */
			double shinyNess = 0.15, shineIntensity = 0.5;
			
			for (Light3D light : lights) {
				if (!light.isEnabled()) continue;
				Vector3D v = light.isPointSource ? light.lightLocation
						.subtract(primitiveElement3D.getCentre()) : light.lightDirection;
				v.normalize();
				// get dot product with camera direction vector
				// double dotProduct =
				// iTempVec1.DotProduct((primitiveElement3D.getCentre().subtract(cameraLocation)).getUnitVector());
				double dotProduct = v.DotProduct(n);
				intensity += dotProduct;// iTempVec1.DotProduct(iLightVec);
				intensity = Math.abs(intensity);
				intensity = (intensity - 1) / (1 - shinyNess) + 1;
				if (intensity < 0)
					intensity = 0;
				else if (intensity > 1)
					intensity = 1;
				iTempCol1 = ColorUtils.blendColors(iTempCol1, light.lightColor,
						intensity * shineIntensity);
				iTempCol1 = ColorUtils.blendColors(iTempCol1, backgroundColor,
						getBlendAmt(primitiveElement3D.depth));

				/* Specular */
				if (dotProduct > 0) {
					// v= light vector
					// rv=reflected vector
					// n=normal vector
					// Find the reflected vector rv.
					v.negate();
					Vector3D rv = v.subtract(n.scale(2 * dotProduct));
					// rv.normalize();
					v = cameraLocation.subtract(primitiveElement3D.getCentre());
					v.normalize();
					double RdotV = Vector3D.dotProduct(rv, v);
					// Calculate the specular component.When n is small,
					// Cosn(f)=(r.v)^n does not decrease as quickly when f
					// increases. That allows more reflected light at
					// larger angles f. This produces a larger, less intense
					// highlight on the surface.
					if (RdotV > 0) {
						double specular_factor = light.SpecularK
								* Math.pow(RdotV, light.SpecularN);
						iTempCol1 = ColorUtils.addColors(iTempCol1,
								light.lightColor, specular_factor);
					}
				}

			}
			return iTempCol1;
		} else
			return primitiveElement3D.prefs().getFillColor();
	}

	
	@Override
	public void visitElements(List<PrimitiveElement3D> primitiveElement3DS) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			//if (primitiveElement3D.getCentre()==null)System.out.println("Invalid centre");
			primitiveElement3D.depth=-(iTransformationMatrix.getTransformedVector(primitiveElement3D.getCentre()).getZ());
			if (!MathUtils.isValidNumber(primitiveElement3D.depth)){
				System.out.println("Invalid centre "+ primitiveElement3D.depth);
			}
			//if (primitiveElement3D instanceof PrimitiveElement3DPoly) primitiveElement3D.depth-=0.001;
		}
	
		try{
		Collections.sort(primitiveElement3DS);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			drawElement(primitiveElement3D);
		}
		/*
		for (PrimitiveElement3D primitiveElement3D: primitiveElement3DS) {
			if (primitiveElement3D instanceof PrimitiveElement3DPoly) drawElement(primitiveElement3D);
	    }
		
		for (PrimitiveElement3D primitiveElement3D: primitiveElement3DS) {
			if (!(primitiveElement3D instanceof PrimitiveElement3DPoly) & !(primitiveElement3D instanceof PrimitiveElement3DPoint))	drawElement(primitiveElement3D);
		}
		for (PrimitiveElement3D primitiveElement3D: primitiveElement3DS) {
			if (primitiveElement3D instanceof PrimitiveElement3DPoint)	drawElement(primitiveElement3D);
		}
		*/
	}
	
	
	public void drawElements(List<PrimitiveElement3D> primitiveElement3DS) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			primitiveElement3D.depth=-(iTransformationMatrix.getTransformedVector(primitiveElement3D.getCentre()).getZ());
			//if (primitiveElement3D instanceof PrimitiveElement3DPoly) primitiveElement3D.depth-=0.001;
		}
		Collections.sort(primitiveElement3DS);
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			drawElement(primitiveElement3D);
	    }
	}
	
	private String format(float f) {
		return f+"";//String.format("%.1G", f);
	}

	/**
	 * Draws string at the specified coordinates with the specified alignment.
	 * 
	 * @param g
	 *            graphics context to draw
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param s
	 *            the string to draw
	 * @param x_align
	 *            the alignment in x direction (left=0, centre=1 right=2)
	 * @param y_align
	 *            the alignment in y direction (top=0, centre =1)
	 */

	private final void outString(Graphics g, int x, int y, String s, int x_align, int y_align) {
		switch (y_align) {
		case TOP:
			y += g.getFontMetrics(g.getFont()).getAscent();
			break;
		case CENTER:
			y += g.getFontMetrics(g.getFont()).getAscent() / 2;
			break;
		case BOTTOM:
			break;
		}
		switch (x_align) {
		case Label.LEFT:
			g.drawString(s, x+3, y);
			break;
		case Label.RIGHT:
			g.drawString(s, x - g.getFontMetrics(g.getFont()).stringWidth(s)-3, y);
			break;
		case Label.CENTER:
			g.drawString(s, x - g.getFontMetrics(g.getFont()).stringWidth(s) / 2, y);
			break;
		}
	}

	
	/**
	 * Draws float at the specified coordinates with the specified alignment.
	 * 
	 * @param g
	 *            graphics context to draw
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param f
	 *            the float to draw
	 * @param x_align
	 *            the alignment in x direction
	 * @param y_align
	 *            the alignment in y direction
	 */

	private final void outFloat(Graphics g, int x, int y, float f, int x_align, int y_align) {
		// String s = Float.toString(f);
		String s = format(f);
		outString(g, x, y, s, x_align, y_align);
	}

	
	
	
	
	/**
	 * Returns the background color of rendering surface.
	 * @return color
	 */
	public synchronized Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Sets the background color of rendering surfac.
	 * @param color the color
	 */
	public synchronized void setBackgroundColor(Color color) {
		this.backgroundColor = color;
		iCanvas.refresh();
	}
	
	/**
	 * Returns true if perspective mode is enabled.
	 * @return boolean
	 */
	public synchronized boolean isPerspectiveEnabled() {
		return !iCamera.isOrthographic();
	}
	
	/**
	 * Sets the Perspective projection flag.
	 * @param flag true if perspective projection is enabled
	 */
	public synchronized void setPerspectiveEnabled(boolean flag) {
		//this.perspectiveEnabled = flag;
	    iCamera.setOrthographic(!flag);
	    iCanvas.iCameraL.setOrthographic(iCamera.isOrthographic());
		iCanvas.iCameraR.setOrthographic(iCamera.isOrthographic());
		iCanvas.refresh();
	}
	
	/**
	 * Returns true if steroscopic mode is enabled.
	 * @return boolean
	 */
	public  synchronized boolean isStereoscopyEnabled() {
		return iCanvas.stereoEnabled;
	}
	
	/**
	 * Sets the steroscopic(Anaglyph) mode flag.
	 * @param flag true if stereoscopic mode is enabled
	 */
	public  synchronized void setStereoscopyEnabled(boolean flag) {
		this.stereoscopyEnabled = flag;
		iCanvas.stereoEnabled=flag;
		if (flag==true)if (stereoscopyMode==0) stereoscopyMode=3;
		if (flag==true){
		   iCanvas.iCameraL.setFov(iCanvas.iCamera.getFov());
		   iCanvas.iCameraR.setFov(iCanvas.iCamera.getFov());
		   iCanvas.iCameraL.setOrthographic(iCanvas.iCamera.isOrthographic());
		   iCanvas.iCameraR.setOrthographic(iCanvas.iCamera.isOrthographic());
		   iCanvas.iCameraL.eye= new Vector3D(iCanvas.iCamera.eye);
		   iCanvas.iCameraR.eye= new Vector3D(iCanvas.iCamera.eye);
		   iCanvas.iCameraL.up= new Vector3D(iCanvas.iCamera.up);
		   iCanvas.iCameraR.up= new Vector3D(iCanvas.iCamera.up);
	       iCanvas.iCameraL.rotateAroundFocus(0, -3, 0);
		   iCanvas.iCameraR.rotateAroundFocus(0, 3, 0);
		}
		
		iCanvas.refresh();
	}
	
	/**
	 * Returns true if Lights is/are enabled.
	 * @return boolean
	 */
	public  synchronized boolean isLightsEnabled() {
		return lightsEnabled;
	}
	
	/**
	 * Sets the LightsEnabled mode flag.
	 * @param flag true if anti-aliasing should be used
	 */
	public  synchronized void setLightsEnabled(boolean flag) {
		this.lightsEnabled = flag;
		iCanvas.refresh();
	}
	
	/**
	 * Returns true if anti-aliasing should be used.
	 * @return boolean
	 */
	public  synchronized boolean isAntiAliasingEnabled() {
		return antiAliasingEnabled;
	}
	
	/**
	 * Sets the anti-aliasing flag.
	 * @param flag true if anti-aliasing should be used
	 */
	public  synchronized void setAntiAliasingEnabled(boolean flag) {
		this.antiAliasingEnabled = flag;
		iCanvas.refresh();
	}
	
	/**
	 * Returns true if polygon has two faces(different colour on different face) 
	 * @return
	 */
	public boolean isDoublefaceEnabled() {
		return doublefaceEnabled;
	}

	/**
	 * Sets doublefaceEnabled Flag (whether polyfon has same color on either side
	 * @param doublefaceEnabled
	 */
	public void setDoublefaceEnabled(boolean doublefaceEnabled) {
		this.doublefaceEnabled = doublefaceEnabled;
		iCanvas.refresh();
	}
	
	/**
	 * Returns the drawMode mode fro polygon rendering.
	 * @return drawMode 0=Wireaframe, 1=solid, 2=solid frame, 3=grayScale
	 */
	public int getDrawMode() {
		return drawMode;
	}


	/**
	 * Sets the drawMode mode fro polygon rendering.
	 * @param drawMode
	 */
	public void setDrawMode(int drawMode) {
		if (drawMode<=3 && drawMode>=0) this.drawMode = drawMode;
	}


	/**
	 * Returns the hsrMode mode for Hidden Surafce removal algorithm.
	 * @return hsrMode 0=Z sort, 1=BSP Tree, 2=Z Buffer
	 */
	public int getHsrMode() {
		return hsrMode;
	}


	/**
	 * Sets the hsrMode mode for Hidden Surface removal algorithm.
	 * @param hsrMode
	 */
	public void setHsrMode(int hsrMode) {
		if (hsrMode<=2 && hsrMode>=0) this.hsrMode = hsrMode;
	}
	
	/**
	 * Restores all settings to their default values
	 */
	public void restoreDefault(){
		backgroundColor = Color.lightGray.brighter();
		antiAliasingEnabled = false;
		lightsEnabled = true;
		stereoscopyEnabled = false;
	    stereoscopyMode = 3;
	    perspectiveEnabled = true;
		drawMode = 3;
		hsrMode = 1;
	}

	public Box3D getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Box3D boundingBox) {
		this.boundingBox = boundingBox;
	}

	/**
	 * @return the fogEnabled
	 */
	public boolean isFogEnabled() {
		return fogEnabled;
	}

	/**
	 * @param fogEnabled the fogEnabled to set
	 */
	public void setFogEnabled(boolean fogEnabled) {
		this.fogEnabled = fogEnabled;
	}

	/**
	 * returns blendind amount(lies btween 0 and 1) for given depth: This used for fog effect
	 * @param depth
	 * @return 0 if depth is less than fog start; 1 if depth is more than fog end; 
	 *           a value between 0 and 1 if depth is in range of fog
	 */
	private double getBlendAmt(double depth) {
         double blendAmt;
         
         if (fogEnabled) {
             blendAmt=(depth-fogStart)/(fogEnd-fogStart);
             //System.out.println(depth+":"+blendAmt);
             if (blendAmt>1) return 1;
             else if (blendAmt<0) return 0;
             else return blendAmt;
         } else return 0;
     }
}
	

