package net.thevpc.scholar.hadruplot.libraries.calc3d.elements;
import java.awt.Color;



/** Stands for a Color Model

 * @author 
 * @date 13 sep 2012 1:23
 */
public class ColorModel
{
	public static final byte DUALSHADE=0;
	public static final byte SPECTRUM=1;
	public static final byte FOG=2;
	public static final byte OPAQUE=3;
	
	public static float RED_H=0.941896f;
	public static float RED_S=0.7517241f;
	public static float RED_B=0.5686275f;
	
	public static float GOLD_H=0.1f;
	public static float GOLD_S=0.9497207f;
	public static float GOLD_B=0.7019608f;
	
	
	public ColorModel dualshade;
	public ColorModel grayscale;
	public ColorModel spectrum;
	public ColorModel fog;
	public ColorModel opaque;
	
	float hue;
	float sat;
	
	float bright;
	float min; // hue|sat|bright of z=0
	float max; // Hue|sat|bright  of z=1
	byte mode=0;
	
	Color ocolor; // fixed color for opaque mode
	
	
	public ColorModel(byte mode,float hue,float sat, float bright,float min,float max)
	{
		this.mode=mode;
		this.hue=hue;
		this.sat=sat;
		this.bright= bright;
		this.min=-1.2f;
		this.max=1.2f;
	}
	
	
	
	public Color getPolygonColor(float z)
	{
		//if (z<0 || z>1) return Color.WHITE;
		switch(mode)
		{
		case DUALSHADE:
			{
				return color(hue,sat, norm(z));
			}
		case SPECTRUM:
			{
				return color(1-norm(z),sat, bright);
				//return color(norm(1-z),0.3f+z*(0.7f), bright);
			}
		case FOG:
			{
				return color(hue,norm(z), bright);
			}
		case OPAQUE:
			{
				if (ocolor==null) ocolor=color(hue,sat, bright);
				return ocolor;
			}
		}
		return Color.WHITE;//default
	}
	

	/**
	 * @param hue
	 * @param sat
	 * @param bright
	 * @return
	 */
	private Color color(float hue, float sat, float bright) {
		Color hsb = Color.getHSBColor(hue, sat, bright);
		// transparency management: unfortunately we reached power limits of 2010's computers it's laggy
		//return new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), 128);
		return  hsb;
	}



	private float norm(float z)
	{
		//return Math.abs(z);
		if (z<min )return 0;
		if (z>max )return 1;
		return (z-min)/(max-min);
      	//	if (min==max) return min;
	   //	return min+z*(max-min);
	}
	
	public static ColorModel getPreset(int index){
		switch (index){
		case 0: //dualshade
		{
		    return new ColorModel(		ColorModel.DUALSHADE,	RED_H	,	RED_S	,	RED_B	,	-0.4f	,	1f		);
		}
		case 1: //grayscale
		{
			return	 new ColorModel(		ColorModel.DUALSHADE,	0f		,	0f		,	0f		,	0f		,	1f		);
		}
		case 2: //spectrum
		{
			return	new ColorModel(		ColorModel.SPECTRUM	,	0f		,	1f		,	1f		,	-1f		,	1.333f	);    
		}
		case 3: //fog
		{
			return new ColorModel(			ColorModel.FOG		,	RED_H	,	RED_S	,	RED_B	,	-1f		,	1f		);    
		}
		 default: //opaque
		{
			return new ColorModel(			ColorModel.OPAQUE	,	RED_H	,	0.1f	,	1f		,	0f		,	0f		);    
		}
		

     	}
	}
	
}//end of class