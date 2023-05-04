package net.thevpc.scholar.hadruplot.libraries.calc3d.engine3d;

import java.awt.Color;

import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

public class Light3D {
	/** Location of light Source in world space */
	public Vector3D lightLocation;
	
	/** Color of light Source */
	public Color lightColor;
	
	/** direction of LightSource in world Space: incase if light source is INFINITE SOURCE*/
	public Vector3D lightDirection;
	
	/** flag to switch between pointSource and infinite Source*/
	public boolean isPointSource = true;
	
	/** Enables fog. */
    public boolean fogEnabled=false;
     
    /** Enables Specular light*/
    public boolean specularEnabled=true;
    
    /** Specular parameter: greater the value greater the final effect  */
    public double SpecularK=0.03; 
    /** Specular parameter: The greater the specularPower value the more shine it is.*/
    public float SpecularN=1.2f;
    
    
    private boolean enabled=true;
    
    private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
    
	/**
	 * Constructor - initialises this to some default values.
	 */
	public Light3D() {
		isPointSource=true;
		lightLocation=new Vector3D(0,0,5);
		lightDirection=new Vector3D(0,0,1);
		lightColor=Color.white;
	}
	
	
	/**
	 * Constructor -
	 * @param location location/direction of camera in world space
	 * @param lightColor Color of light Source
	 * @param isPointSource nature of light source (point Source/infinite source)
	 */
	public Light3D(Vector3D location, Vector3D Direction,Color lightColor,boolean isPointSource) {
		this.isPointSource=isPointSource;
		this.lightLocation=location;
		this.lightDirection=Direction.getUnitVector();
		this.lightColor=lightColor;
	}

	
	/**
	 * Rotate the light around Axis in World space.
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
		//Transform Camera Location and its direction both
		T.transform(lightLocation);
		T.transform(lightDirection);
	}

	
	/*
	
	/**
	 * Returns  new color representing color of primitiveElement3D after imposing effect of light source on it
	 * @param color Color of surface/curve
	 * @param depth Depth of surface/curve: needed for fogging
	 * @param normal unitVector of surface, it can be null also(for curves stringd etc)
	 * @return
	 
         //private Color getSurfaceColor(Color color,double depth, Vector3D normal){
	/**
	 * shinyNess: Lower values will spread out the shine shineIntensity:
	 * Maximum intensity of the shine
	 
		double shinyNess = 0.2, shineIntensity = 0.9;
		return color;}
     */


	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
     
}
