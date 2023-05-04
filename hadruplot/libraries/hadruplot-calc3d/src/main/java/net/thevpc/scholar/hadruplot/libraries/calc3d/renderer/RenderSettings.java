package net.thevpc.scholar.hadruplot.libraries.calc3d.renderer;

import java.awt.Color;

/**
 * Class to manage Canvas Render preferences.*/
public class RenderSettings {
	/** backgroung colour of Canvas*/
	private Color backgroundColor = Color.black;
	
	/** True if anti-aliasing should be used (2X MSAA) */
	private boolean antiAliasingEnabled = false;
	
	/** True if Lights is/are enabled */
	private boolean lightsEnabled = false;
	
	/** True if Polygon has two different faces to be rendered*/
	private boolean doublefaceEnabled = false;
	
	/** True if SteroScopic Mode is enabled */
	private boolean stereoscopyEnabled = false;

	/**Stereoscopic mode constants*/
	public final int STEREO_NOANAGLYP=0;
	public final int STEREO_REDGREENANGLYP=1;
	public final int  STEREO_COLORANAGLYP=2;
	public final int  STEREO_OPTIMISEDANGLYP=3;
	public final int  STEREO_NOANAGLYPLEFT=4;
	public final int  STEREO_NOANAGLYPRIGHT=5;
	
	/** SteroScopic Mode (3D anaglyph) */
	private int stereoscopyMode = 3;
	
	/** True if Perspective Projection is enabled */
	private boolean perspectiveEnabled = true;
	
	
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
	}
	
	/**
	 * Returns true if steroscopic mode is enabled.
	 * @return boolean
	 */
	public  synchronized boolean isStereoscopyEnabled() {
		return stereoscopyEnabled;
	}
	
	/**
	 * Sets the steroscopic(Anaglyph) mode flag.
	 * @param flag true if stereoscopic mode is enabled
	 */
	public  synchronized void setStereoscopyEnabled(boolean flag) {
		this.stereoscopyEnabled = flag;
		if (stereoscopyMode==0) stereoscopyMode=3;
	}
	
	
	/**
	 * Returns true if perspective mode is enabled.
	 * @return boolean
	 */
	public  synchronized boolean isPerspectiveEnabled() {
		return perspectiveEnabled;
	}
	
	/**
	 * Sets the Perspective projection flag.
	 * @param flag true if perspective projection is enabled
	 */
	public  synchronized void setPerspectiveEnabled(boolean flag) {
		this.perspectiveEnabled = flag;
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
	}
	
	
	
	/**
	 * Returns the background color of rendering surface.
	 * @return color
	 */
	public  Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Sets the background color of rendering surfac.
	 * @param color the color
	 */
	public  void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	/**
	 * Returns the stereoscopyMode mode of 3d display.
	 * @return stereoscopyMode 0=NoAnaglyp, 1=redCyan, 2 =Color, 3=Optimised, 4=left Only, 5=right only
	 */
	public int getStereoscopyMode() {
		return stereoscopyMode;
	}

	/**
	 * Sets the background color of rendering surfac.
	 * @param stereoscopyMode the stereoscopyMode
	 */
	public void setStereoscopyMode(int stereoscopyMode) {
		if (stereoscopyMode<=5 && stereoscopyMode>=0) this.stereoscopyMode = stereoscopyMode;
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
		backgroundColor = Color.black;
		antiAliasingEnabled = false;
		lightsEnabled = true;
		stereoscopyEnabled = false;
	    stereoscopyMode = 3;
	    perspectiveEnabled = true;
		drawMode = 3;
		hsrMode = 1;
	}

	
	
}
