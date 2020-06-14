package net.vpc.scholar.hadruplot.libraries.calc3d.renderer;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.vpc.scholar.hadruplot.libraries.calc3d.core.Globalsettings;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Light3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Angles3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * A basic interaction handler.
 * 
 * Controls: - Mouse: drag rotates the view in 360 degrees across the x and y
 * axes. - Key: 'r' resets the viewing angles and scaling. - Key: 'w' toggles
 * wireframe mode.
 */
public final class InteractionHandler implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener, ComponentListener, Runnable {
	private static Logger LOG = Logger.getLogger(InteractionHandler.class
			.getName());

	/** The class (struct?) that stores the keyboard constants */
	private static class KeyBoard {
		// The target velocities
		private double targetForward = 0;
		private Point2D.Double targetTranslate = new Point2D.Double(0, 0);
		private Point2D.Double targetRotate = new Point2D.Double(0, 0);
		private double targetBank = 0;
		private Point2D.Double targetPivotRotate = new Point2D.Double(0, 0);
		// The current velocities
		private double velForward = 0;
		private Point2D.Double velTranslate = new Point2D.Double(0, 0);
		private Point2D.Double velRotate = new Point2D.Double(0, 0);
		private double velBank = 0;
		private Point2D.Double velPivotRotate = new Point2D.Double(0, 0);
		
	};

	/** Instanciation of the KeyBoard class... only one needed */
	private KeyBoard keyBoard = new KeyBoard();

	/** Need to smooth Keyboard Interaction */
	private Timer timerKey,timerAnimate;
	private Thread animator; // for the animation
	public int FPS = 30;
    public int period = 1000 / FPS;;
	 private boolean running = false; // stops the animation
	// variables
	private Angles3D iCameraAngles = new Angles3D();
	private Point iMouseXY = new Point();
	private Point iMouseDownXY = new Point();
	private long timeMouseDown;

	/** The rendering surface with which this handler is currently associated. */
	protected Canvas3D iSurface = null;
	private Renderer renderer = null;
	private Camera3D camera = null;
    private Light3D[] lights=null;
    private Globalsettings globalsettings;

	public InteractionHandler(Globalsettings globalsettings) {
		this.globalsettings = globalsettings;
	}

	/**
	 * Associate a rendering surface with this handler.
	 * 
	 * This gets called from
	 *
	 * @param aSurface
	 *            the rendering surface. May be null.
	 */
	public final void associate(Canvas3D aSurface) {
		if (null!=aSurface){
		     iSurface = aSurface;
		     camera = iSurface.getCamera();
		     renderer=iSurface.getRenderer();
		     lights=iSurface.getLights();
		     reset();
		     timerKey = new Timer(50, new TimerListener());
		 }else{
			 iSurface=null;
			 camera=null;
			 renderer = null;
			 lights=null;
			 timerKey=null;
		 }
		
	}
	
	
	 public void startGame() // initialise and start the thread
	    {
	        if (animator == null || !running) {
	            animator = new Thread(this);
	            animator.start();
	        }
	    } // end of startGame()

	    public void stopGame() // called by the user to stop execution
	    {
	        running = false;
	    }
	
	    public void run() /*
	     * /* Repeatedly: update, render, sleep so loop takes close to period ms
	     */ {
	        running = true;
	        long beforeTime, timeDiff, sleepTime;
	        beforeTime = System.currentTimeMillis();

	        while (running) {
	        	Camera3D camera = iSurface.getCamera();
				if (null != camera)
					camera.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				 iSurface.iCameraL.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				 iSurface.iCameraR.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				
				refreshDisplay();
	            timeDiff = System.currentTimeMillis() - beforeTime;
	            sleepTime = period - timeDiff; // time left in this loop
	            if (sleepTime <= 0) // update/render took longer than period
	            {
	                sleepTime = 5; // sleep a bit anyway
	            }
	            try {
	                Thread.sleep(sleepTime); // in ms
	            } catch (InterruptedException ex) {
	            }
	            beforeTime = System.currentTimeMillis();
	        }
	      
	    } // end of run()

	    
	    
	/**
	 * Reset this handler's internal state.
	 * 
	 * This internally calls {@link #doReset()} followed by
	 * {@link #refreshDisplay()}.
	 */
	public final void reset() {
		doReset();
		refreshDisplay();
	}

	private final String[] iKeyAndMouseCommands = {
			"Mouse drag - rotate object",
			"leftMouseDown Drag  : Rotate Camera Around focus",
			"RightMouseDown Drag : Rotate about Z axis",
	        "MiddleMouseDown Drag: Translate",
	        "Drag + Control : rotate camera about Z and Y axis (world axis):," +
	        "Drag + Alt     : Translate ViewPort by shifting Origin",
	        "Drag + Shift   : Roll Camera about N axis (Rotate ViewPort)",
	     	"R - reset camera",
			"W - toggle wireframe mode",
			"B - toggle backface culling",
			"Z - toggle z-buffer", 
			"Arrows - Rotate cameraAround focus",
			"Arrows + Control - rotate camera about Z and Y axis (world axis)",
			"Arrows + alt - Translate ViewPort by shifting Origin",
			"Arrows + shift -  Roll Camera about N axis (Rotate ViewPort)",
			"PageUp - Zoom in",
			"pageDown - Zoom out",
			"Autorotate: Try to give Angular Impuse using mouse"
			};

	public void doReset() {
		if (null != iSurface) {
			Camera3D camera = iSurface.getCamera();
			if (null != camera) {
				camera.reset();
			}
		}
	}

	public void keyPressed(KeyEvent e) {

		if (null != iSurface) {
			renderer = iSurface.getRenderer();
			camera = iSurface.getCamera();
		}
		// In case if there is no renderer or camera associated
		if (((null != renderer) == false) || ((null != renderer) == false))
			return;
	
		if (null != timerAnimate)	timerAnimate.stop();
		//flag to check if timerkey is needed to be activated or not
		boolean startTimer = true;
		
		switch (e.getKeyCode()) {
		  case KeyEvent.VK_R:
			  reset();
			  break;
		  case KeyEvent.VK_B:
			  // toggle backface culling
			 // renderer.enableBackfaceCulling(!renderer.isBackfaceCullingEnabled());
			  break;
		  case KeyEvent.VK_W:
			  // toggle wireframe mode
			  //renderer.enableWireframeMode(!renderer.isWireframeModeEnabled());
			  break;
		  case KeyEvent.VK_Z:
			  // toggle z-buffer
			 // renderer.enableZBuffer(!renderer.isZBufferEnabled());
			  break;
		  case KeyEvent.VK_P:
			  // toggle perspective projection
			  //camera.setOrthographic(!camera.isOrthographic());
			  break;
		  case KeyEvent.VK_S:
			  iSurface.stereoEnabled=!iSurface.stereoEnabled;
			  LOG.info("steroScopic View Enabled: " + iSurface.stereoEnabled);
		  case KeyEvent.VK_PAGE_UP:
			    Camera3D camera = iSurface.getCamera();
				if (null != camera) {
					   camera.setFov((camera.getFov() + 1) % 360);
				if (globalsettings.steroscopyEnabled){
					   iSurface.iCameraL.setFov(camera.getFov());
					   iSurface.iCameraR.setFov(camera.getFov());
				}
					globalsettings.fov=camera.getFov();
				}
				break;
		  case KeyEvent.VK_PAGE_DOWN:
			    camera = iSurface.getCamera();
				if (null != camera) {
				    	camera.setFov((camera.getFov() + 359) % 360);
				if (globalsettings.steroscopyEnabled){
					   iSurface.iCameraL.setFov(camera.getFov());
					   iSurface.iCameraR.setFov(camera.getFov());
				}
					globalsettings.fov=camera.getFov();
				}
			  // dec FOV angle (Zoom out)
			  break;
		  case KeyEvent.VK_DOWN:
		      if (e.isAltDown()) keyBoard.targetTranslate.y=-0.15;
              else if (e.isControlDown()) keyBoard.targetPivotRotate.y=0.15;
              else if (e.isShiftDown()) keyBoard.targetBank=-0.15;
              else keyBoard.targetRotate.y=0.15;
              break;
		  case KeyEvent.VK_UP:
              if (e.isAltDown()) keyBoard.targetTranslate.y=0.15;
              else if (e.isControlDown()) keyBoard.targetPivotRotate.y=-0.15;
              else if (e.isShiftDown()) keyBoard.targetBank=0.15;
              else keyBoard.targetRotate.y=-0.15;
              break;
          case KeyEvent.VK_LEFT:
              if (e.isAltDown()) keyBoard.targetTranslate.x=0.15;
              else if (e.isControlDown()) keyBoard.targetPivotRotate.x=0.15;
              else if (e.isShiftDown()) keyBoard.targetBank=-0.15;
              else keyBoard.targetRotate.x=0.15;
              break;
          case KeyEvent.VK_RIGHT:
              if (e.isAltDown()) keyBoard.targetTranslate.x=-0.15;
              else if (e.isControlDown()) keyBoard.targetPivotRotate.x=-0.15;
              else if (e.isShiftDown()) keyBoard.targetBank=0.15;
              else keyBoard.targetRotate.x=-0.15;
              break;
		  default:
			startTimer = false;
		}
		if (startTimer) timerKey.start();
		refreshDisplay();
	}

	public void keyReleased(KeyEvent e) {
		//reset Keyboard variables
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_Z:
			keyBoard.targetForward = 0;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_UP:
			keyBoard.targetTranslate.y = 0;
			keyBoard.targetPivotRotate.y = 0;
			keyBoard.targetRotate.y = 0;
			keyBoard.targetBank = 0;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			keyBoard.targetTranslate.x = 0;
			keyBoard.targetPivotRotate.x = 0;
			keyBoard.targetRotate.x = 0;
			keyBoard.targetBank = 0;
			break;
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_Q:
		case KeyEvent.VK_PAGE_DOWN:
		case KeyEvent.VK_E:
			keyBoard.targetBank = 0;
			break;
		}
	}

	
	public void keyTyped(KeyEvent arg0) {
	}

	
	public void mouseClicked(MouseEvent arg0) {
		if (!iSurface.stereoEnabled)
			return;
		iSurface.stereoMode++;
		if (iSurface.stereoMode > 5)
			iSurface.stereoMode = 0;
		LOG.info("stereoScopic Mode Changed to : " + iSurface.stereoMode);
		refreshDisplay();
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) {
		iSurface.requestFocus();
		iMouseXY.setLocation(e.getX(), e.getY());
		iMouseDownXY.setLocation(e.getX(), e.getY());
		timeMouseDown = System.nanoTime();
		if (null != timerKey)
			timerKey.stop();
		if (null != timerAnimate)
			timerAnimate.stop();
		stopGame();
	}

	
	public void mouseReleased(MouseEvent e) {
        //this event is being used for autorotation of Canvas3D
		//Perform autorotation only when leftMouse button is used
		if (SwingUtilities.isLeftMouseButton(e)==false){
			return;
		}
		//calculate difference in time from mousedown and mouse released event
		long timeTakenInNanos = System.nanoTime() - timeMouseDown + 1; // ensure >0
		//AutoRotate only when time is small(impulsive feel) and there is enough movement of mouse
		if ((iMouseXY.distance(iMouseDownXY) <= 1)
				|| (timeTakenInNanos > 600000000))
			return;

		if (null != timerAnimate) 
			timerAnimate.stop();
		//calculate angles based on mouse displacement, greater the diplacement grater is angle and hence the speed
		iCameraAngles.iAngleX = -(float) (e.getY() - iMouseDownXY.y);
		iCameraAngles.iAngleY = -(float) (e.getX() - iMouseDownXY.x);
		iCameraAngles.iAngleZ = 0;

		//limit maximum velocity
		if (Math.abs(iCameraAngles.iAngleX) > 18)
			iCameraAngles.iAngleX = Math.signum(iCameraAngles.iAngleX) * 18;
		if (Math.abs(iCameraAngles.iAngleY) > 24)
			iCameraAngles.iAngleY = Math.signum(iCameraAngles.iAngleY) * 18;

		//Initialise and start timer
		/*
		timerAnimate = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Camera3D camera = iSurface.getCamera();
				if (null != camera)
					camera.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				 iSurface.iCameraL.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				 iSurface.iCameraR.rotateAroundFocus(iCameraAngles.iAngleX / 3,	iCameraAngles.iAngleY / 3, iCameraAngles.iAngleZ);
				
				refreshDisplay();
			}
		});
		timerAnimate.start();
		*/
		startGame();

	}

	
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
        /*
         * RightMouse Down: Rotate about Z axis
         * MiddleMouseDown: translate
         * LeftMouseDown ==> Control : rotate camera about Z and Y axis (world axis)
         * LeftMouseDown ==> Alt     : Translate ViewPort by shifting Origin
         * LeftMouseDown ==> Shift   : Roll Camera about N axis (Rotate ViewPort)
         * leftMouseDown  : Rotate Camera Around focus
         */
		
		//calculate MouseDisplacement along x and y
		//We are putting -ve sign here since to move world we must move camera in inverse direction
		iCameraAngles.iAngleX = -(float) (y - iMouseXY.y);
		iCameraAngles.iAngleY = -(float) (x - iMouseXY.x);
		iCameraAngles.iAngleZ = 0;
		
        //Reset values to current position
		iMouseXY.x = x;
		iMouseXY.y = y;

		running=false;
		Camera3D camera = iSurface.getCamera();
		if (null != camera)
			if (SwingUtilities.isRightMouseButton(e)){
				Rectangle r=iSurface.getBounds();
	            double xdir=x-(r.x+r.width/2),ydir=y-(r.y+r.height/2);
	            double length=Math.sqrt(xdir*xdir+ydir*ydir);
				camera.rotateAboutAxes(0, 0, (iCameraAngles.iAngleY *ydir-iCameraAngles.iAngleX *xdir)/length);
				refreshDisplay();
				return;
			}
			
			if (e.isAltDown()){
				//translate viewPort
				iSurface.translateViewPort(iCameraAngles.iAngleY, iCameraAngles.iAngleX);
			}else if (e.isShiftDown()) {
				/*roll camera/rotate viewPort
				Rectangle r=iSurface.getBounds();
	            double xdir=x-(r.x+r.width/2),ydir=y-(r.y+r.height/2);
	            double length=Math.sqrt(xdir*xdir+ydir*ydir);
	            */
	            //rotates light
				lights[0].rotateAboutAxes(iCameraAngles.iAngleX,iCameraAngles.iAngleY, iCameraAngles.iAngleZ);
			}else if (e.isControlDown()){
				//roate camera about Z axis
				Rectangle r=iSurface.getBounds();
	            double xdir=x-(r.x+r.width/2),ydir=y-(r.y+r.height/2);
	            double length=Math.sqrt(xdir*xdir+ydir*ydir);
				//camera.rotateAboutAxes(0, 0, (iCameraAngles.iAngleY *ydir+iCameraAngles.iAngleX *xdir)/length);
				camera.rotateAboutAxes( 0,iCameraAngles.iAngleY,0);
				camera.rotateAboutAxes(iCameraAngles.iAngleX,0,0);
				if (globalsettings.steroscopyEnabled){
	            	 iSurface.iCameraL.eye=new Vector3D(camera.eye);
	            	 iSurface.iCameraR.eye=new Vector3D(camera.eye);
	            	 iSurface.iCameraL.up=new Vector3D(camera.up);
	            	 iSurface.iCameraR.up=new Vector3D(camera.up);
	            	 iSurface.iCameraL.rotateAboutAxes( 0,iCameraAngles.iAngleY,0);
	            	 iSurface.iCameraL.rotateAboutAxes(iCameraAngles.iAngleX,0,0);
	            	 iSurface.iCameraR.rotateAboutAxes( 0,iCameraAngles.iAngleY,0);
	            	 iSurface.iCameraR.rotateAboutAxes(iCameraAngles.iAngleX,0,0);
	            }
			}else{
				//Rotate camera about focus
			    camera.rotateAroundFocus(iCameraAngles.iAngleX,iCameraAngles.iAngleY, iCameraAngles.iAngleZ);
	            if (globalsettings.steroscopyEnabled){
	            	 iSurface.iCameraL.eye=new Vector3D(camera.eye);
	            	 iSurface.iCameraR.eye=new Vector3D(camera.eye);
	            	 iSurface.iCameraL.up=new Vector3D(camera.up);
	            	 iSurface.iCameraR.up=new Vector3D(camera.up);
	            	 iSurface.iCameraL.rotateAroundFocus(0,-4,0);
	            	 iSurface.iCameraR.rotateAroundFocus(0,4,0);
	            }
			}
		refreshDisplay();
	}

	
	public void mouseMoved(MouseEvent arg0) {
	}

	
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		Camera3D camera = iSurface.getCamera();
		if (null != camera) {
			if (arg0.getWheelRotation() > 0) // zoom in
			   camera.setFov((camera.getFov() + 1) % 360);
			if (arg0.getWheelRotation() < 0) // zoom out
			   camera.setFov((camera.getFov() + 359) % 360);
			if (globalsettings.steroscopyEnabled){
			   iSurface.iCameraL.setFov(camera.getFov());
			   iSurface.iCameraR.setFov(camera.getFov());
			}
			globalsettings.fov=camera.getFov();
			refreshDisplay();
		}
	}

	protected void refreshDisplay() {
		if (null != iSurface) {
			iSurface.refresh();
		}
	}

	public String[] getKeyAndMouseCommands() {
		return iKeyAndMouseCommands;
	}

	//TimerKey Listener for KeyBoard interaction for elastic effects etc
	private class TimerListener implements ActionListener {
		private long lastTime = 0;

		public void actionPerformed(ActionEvent e) {
			camera = iSurface.getCamera();
			long curTime = System.currentTimeMillis();
			long delta = curTime - lastTime;
			if (delta <= 0 || delta > 2 * timerKey.getDelay())
			  	  delta = timerKey.getDelay();
			double mult = (double) delta / 100; // To maintain acceleration as
												// if timer was paced at 100ms
			lastTime = curTime;
			boolean stopTimer = true;
			final double elas = .25; // 0 means instant accel, near 1 means slow
									// accel
			double elasticity = Math.pow(elas, mult);
			keyBoard.velForward = keyBoard.velForward * elasticity
					+ keyBoard.targetForward * mult * (1 - elasticity);
			
			if (Math.abs(keyBoard.velForward) > 0.001) {
				stopTimer = false;
				camera.forward(keyBoard.velForward);
			}
			
			keyBoard.velTranslate.x = keyBoard.velTranslate.x * elasticity
					+ keyBoard.targetTranslate.x * mult * (1 - elasticity);
			keyBoard.velTranslate.y = keyBoard.velTranslate.y * elasticity
					+ keyBoard.targetTranslate.y * mult * (1 - elasticity);
			
			if (Math.abs(keyBoard.velTranslate.x) > .001
					|| Math.abs(keyBoard.velTranslate.y) > .001) {
				stopTimer = false;
				iSurface.translateViewPort(keyBoard.velTranslate.x*70, keyBoard.velTranslate.y*70);
			}
			
			keyBoard.velRotate.x = keyBoard.velRotate.x * elasticity
					+ keyBoard.targetRotate.x * mult * (1 - elasticity);
			keyBoard.velRotate.y = keyBoard.velRotate.y * elasticity
					+ keyBoard.targetRotate.y * mult * (1 - elasticity);
			
			if (Math.abs(keyBoard.velRotate.x) > .001
					|| Math.abs(keyBoard.velRotate.y) > .001) {
				stopTimer = false;
				camera.rotateAroundFocus(keyBoard.velRotate.x*114,
						keyBoard.velRotate.y*114, 0);
			}
			
			keyBoard.velBank = keyBoard.velBank * elasticity
					+ keyBoard.targetBank * mult * (1 - elasticity);
			
			if (Math.abs(keyBoard.velBank) > .001) {
				stopTimer = false;
				camera.bank(keyBoard.velBank*57);
			}
			
			keyBoard.velPivotRotate.x = keyBoard.velPivotRotate.x * elasticity
					+ keyBoard.targetPivotRotate.x * mult * (1 - elasticity);
			keyBoard.velPivotRotate.y = keyBoard.velPivotRotate.y * elasticity
					+ keyBoard.targetPivotRotate.y * mult * (1 - elasticity);
			
			if (Math.abs(keyBoard.velPivotRotate.x) > .001
					|| Math.abs(keyBoard.velPivotRotate.y) > .001) {
				stopTimer = false;
				camera.rotateAboutAxes(0,0,keyBoard.velPivotRotate.x*57);
				camera.rotateAboutAxes(keyBoard.velPivotRotate.y*57,0,0);
			}
			
			if (stopTimer)
				timerKey.stop();
			else
				timerKey.restart();
			refreshDisplay();
		}
	}


	@Override
	public void componentResized(ComponentEvent evt) {
		 Component c = (Component)evt.getSource();
         if (c instanceof Canvas3D) ((Canvas3D)c).updateViewPort();
	}

	@Override
	public void componentShown(ComponentEvent evt) {
		 Component c = (Component)evt.getSource();
         if (c instanceof Canvas3D) ((Canvas3D)c).updateViewPort();
	}
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	
}
