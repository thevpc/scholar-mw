package net.thevpc.scholar.hadruplot.libraries.calc3d.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;


/**
 * Class responsible for drawing stuff onto the viewport.
 */
final class Rasteriser {
	private final static Logger LOG = Logger.getLogger(Rasteriser.class
			.getName());

	private static Rasteriser iInstance = null;

	private int iFrameNumber = 0;

	private Vector3D iTempVec1 = new Vector3D();
	private Vector3D iTempVec2 = new Vector3D();

	private double[][] iZBuffer = null;
	private int[][] iZBufferFrameNumber = null;

	private int iTempInt = -1;
	private float iTempFloat = -1;

	private Rasteriser() {
		//StaticDataManager.register(this);
	}

	public static Rasteriser getInstance() {
		if (null == iInstance) {
			iInstance = new Rasteriser();
		}
		return iInstance;
	}

	/**
	 * Reset the rasteriser's internal state such that it's ready to draw the
	 * next frame.
	 * 
	 * This method should be called at the start of each rendering iteration.
	 */
	public void resetForNextFrame(RasterSettings aSettings) {
		if (aSettings.iZBufferEnabled) {
			resetZBuffer(aSettings);
		}
		
		// inc. the frame number
		if (Integer.MAX_VALUE == ++iFrameNumber) {
			iFrameNumber = 1;
		}
	}

	/**
	 * Clear the Z-buffer.
	 */
	private void resetZBuffer(RasterSettings aSettings) {
		final int width = aSettings.iViewportDimensions.width;
		final int height = aSettings.iViewportDimensions.height;

		// Ensure that z-buffer has been initialised and that it is big
		// enough to hold values for the whole viewport
		if (null == iZBuffer || iZBuffer.length < width
				|| iZBuffer[0].length < height) {
			iZBuffer = new double[width][height];
			iZBufferFrameNumber = new int[width][height];
			iFrameNumber = 1;
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				iZBuffer[i][j] = Double.NEGATIVE_INFINITY;
			}
		}
	}

	/**
	 * Draw a triangle using the Z-buffer.
	 * 
	 * @param aTriangleColor
	 *            the color of the triangle.
	 * @param aPoint1
	 *            the first point.
	 * @param aNormal1
	 *            the first point's normal.
	 * @param aColor1
	 *            the first point's color.
	 * @param aPoint2
	 *            the second point.
	 * @param aNormal2
	 *            the second point's normal.
	 * @param aColor2
	 *            the second point's color.
	 * @param aPoint3
	 *            the third point.
	 * @param aNormal3
	 *            the third point's normal.
	 * @param aColor3
	 *            the third point's color.
	 * @param aWireframeMode
	 *            whether to draw in wireframe mode.
	 */
	public void drawTriangle(RasterSettings aSettings, Color aTriangleColor,
			Vector3D aPoint1, Vector3D aNormal1, Color aColor1,
			Vector3D aPoint2, Vector3D aNormal2, Color aColor2,
			Vector3D aPoint3, Vector3D aNormal3, Color aColor3) {

		//*************************TESTING FOR CLOCKWISE
		   Vector3D AToB =aPoint2.subtract(aPoint1);
		   Vector3D BToC =aPoint3.subtract(aPoint2);
		   Vector3D CToA =aPoint1.subtract(aPoint3);
		   double crossz = AToB.getX() * BToC.getY()  - AToB.getY()  * BToC.getX() ;
		 //  if ((AToB.getLengthSq()<2)||(BToC.getLengthSq()<2)||(CToA.getLengthSq()<2)|| (Math.abs(crossz)<3))return;
		//   if ((VecIntEqual(aPoint1,aPoint2))||(VecIntEqual(aPoint2,aPoint3))||(VecIntEqual(aPoint3,aPoint1))) return;
		   if ( crossz >= 0f )
		   {
		     // clockwise
			   aSettings.iGraphics.setColor(aTriangleColor.brighter());
		   }
		   else
		   {
		     // counter-clockwise.  
			   aSettings.iGraphics.setColor( new Color(255-aTriangleColor.getRed(),255-aTriangleColor.getRed(),255-aTriangleColor.getRed()).darker());
		   }
		
		//***************************************************
		
		
		if (null != aTriangleColor) {
		//	aSettings.iGraphics.setColor(aTriangleColor);
		}

		// point 1 and 2 lie on same horizontal line
		if ((int) aPoint1.getY() == (int) aPoint2.getY()) {
			drawTopDownOrBottomUpTriangle(aSettings, aTriangleColor, aPoint1,
					aNormal1, aColor1, aPoint2, aNormal2, aColor2, aPoint3,
					aNormal3, aColor3, true);
		}
		// point 2 and 3 lie on same horizontal line
		else if ((int) aPoint2.getY() == (int) aPoint3.getY()) {
			drawTopDownOrBottomUpTriangle(aSettings, aTriangleColor, aPoint2,
					aNormal2, aColor2, aPoint3, aNormal3, aColor3, aPoint1,
					aNormal1, aColor1, true);
		}
		// point 1 and 3 lie on same horizontal line
		else if ((int) aPoint1.getY() == (int) aPoint3.getY()) {
			drawTopDownOrBottomUpTriangle(aSettings, aTriangleColor, aPoint1,
					aNormal1, aColor1, aPoint3, aNormal3, aColor3, aPoint2,
					aNormal2, aColor2, true);
		}

		/* at this point we know that no 2 points have the same y-value. */

		// point 1 is higher than point 2
		else if ((int) aPoint1.getY() < (int) aPoint2.getY()) {
			// point 2 is higher than point 3
			if ((int) aPoint2.getY() < (int) aPoint3.getY()) {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint1, aNormal1, aColor1, aPoint2,
						aNormal2, aColor2, aPoint3, aNormal3, aColor3);
			}
			// point 3 is higher than point 2 but lower than point 1
			else if ((int) aPoint1.getY() < (int) aPoint3.getY()) {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint1, aNormal1, aColor1, aPoint3,
						aNormal3, aColor3, aPoint2, aNormal2, aColor2);
			}
			// point 3 is higher than both point 1 and 2
			else {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint3, aNormal3, aColor3, aPoint1,
						aNormal1, aColor1, aPoint2, aNormal2, aColor2);
			}
		}
		// point 1 is NOT higher than point 2
		else {
			// point 3 is higher than point 2
			if ((int) aPoint3.getY() < (int) aPoint2.getY()) {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint3, aNormal3, aColor3, aPoint2,
						aNormal2, aColor2, aPoint1, aNormal1, aColor1);
			}
			// point 3 is lower than point 2 but higher than point 1
			else if ((int) aPoint3.getY() < (int) aPoint1.getY()) {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint2, aNormal2, aColor2, aPoint3,
						aNormal3, aColor3, aPoint1, aNormal1, aColor1);
			}
			// point 3 is lower than both point 1 and 2
			else {
				drawTriangleWithDifferingPointYValues(aSettings,
						aTriangleColor, aPoint2, aNormal2, aColor2, aPoint1,
						aNormal1, aColor1, aPoint3, aNormal3, aColor3);
			}
		}
	}

	/**
	 * Draw a triangle whose three points all have different y-values.
	 * 
	 * The points are supplied in ascending order of y-value (i.e. lowest to
	 * highest).
	 * 
	 * @param aTriangleColor
	 *            the color of the triangle.
	 * @param aPoint1
	 *            the first point.
	 * @param aNormal1
	 *            the first point's normal.
	 * @param aColor1
	 *            the first point's color.
	 * @param aPoint2
	 *            the second point.
	 * @param aNormal2
	 *            the second point's normal.
	 * @param aColor2
	 *            the second point's color.
	 * @param aPoint3
	 *            the third point.
	 * @param aNormal3
	 *            the third point's normal.
	 * @param aColor3
	 *            the third point's color.
	 */
	private void drawTriangleWithDifferingPointYValues(
			RasterSettings aSettings, Color aTriangleColor, Vector3D aPoint1,
			Vector3D aNormal1, Color aColor1, Vector3D aPoint2,
			Vector3D aNormal2, Color aColor2, Vector3D aPoint3,
			Vector3D aNormal3, Color aColor3) {
		/*
		 * We draw this triangle by splitting it into a top-down and bottom-up
		 * triangle pair and then drawing those.
		 * 
		 * [t] o o o [m] o o [p] o o o o oo o [b]
		 * 
		 * 
		 * y(p) = y(t) + d * (y(b) - y(t)) where d = delta x(p) = x(t) + d *
		 * (x(b) - x(t)) z(p) = z(t) + d * (z(b) - z(t))
		 * 
		 * But we know that y(p) = y(m), thus:
		 * 
		 * y(m) = y(t) + d * (y(b) - y(t))
		 * 
		 * d = (y(m) - y(t)) / (y(b) - y(t))
		 */

		float delta = (float) ((aPoint2.getY() - aPoint1.getY()) / (aPoint3
				.getY() - aPoint1.getY()));

		// calculate point P
		iTempVec1.set(aPoint1.getX() + delta
				* (aPoint3.getX() - aPoint1.getX()), aPoint1.getY() + delta
				* (aPoint3.getY() - aPoint1.getY()), aPoint1.getZ() + delta
				* (aPoint3.getZ() - aPoint1.getZ()));

		// calculate normal at point P
		iTempVec2.set(aNormal1);
		iTempVec2.addEq(aNormal3);
		iTempVec2.normalize();

		// TODO: calculate correct interpolated color for the point P

		// draw the bottom-up bit
		drawTopDownOrBottomUpTriangle(aSettings, aTriangleColor, aPoint2,
				aNormal2, aColor2, iTempVec1, iTempVec2, aColor1, aPoint1,
				aNormal1, aColor1, false);

		// draw the top-down bit
		drawTopDownOrBottomUpTriangle(aSettings, aTriangleColor, aPoint2,
				aNormal2, aColor2, iTempVec1, iTempVec2, aColor1, aPoint3,
				aNormal3, aColor3, false);

	}

	/**
	 * Draw a triangle where atleast two of the points have the same y-value.
	 * 
	 * The first two points form the base line. The third point is the other
	 * corner of the triangle to which the triangle is incrementally rendered.
	 * 
	 * @param aTriangleColor
	 *            the color of the triangle.
	 * @param aPoint1
	 *            the first point.
	 * @param aNormal1
	 *            the first point's normal.
	 * @param aColor1
	 *            the first point's color.
	 * @param aPoint2
	 *            the second point.
	 * @param aNormal2
	 *            the second point's normal.
	 * @param aColor2
	 *            the second point's color.
	 * @param aPoint3
	 *            the third point.
	 * @param aNormal3
	 *            the third point's normal.
	 * @param aColor3
	 *            the third point's color.
	 * @param aDrawLineBetweenPoints1And2
	 *            whether to draw a line between aPoint1 and aPoint2.
	 */
	private void drawTopDownOrBottomUpTriangle(RasterSettings aSettings,
			Color aTriangleColor, Vector3D aPoint1, Vector3D aNormal1,
			Color aColor1, Vector3D aPoint2, Vector3D aNormal2, Color aColor2,
			Vector3D aPoint3, Vector3D aNormal3, Color aColor3,
			boolean aDrawLineBetweenPoints1And2) {
		final int END_Y = (int) aPoint3.getY();
		final int START_Y = (int) aPoint1.getY();

		float x1 = (float) aPoint1.getX(), x2 = (float) aPoint2.getX();
		float old_x1 = x1, old_x2 = x2;

		float z1 = (float) aPoint1.getZ(), z2 = (float) aPoint2.getZ();
		float old_z1 = z1, old_z2 = z2;

		final float x3 = (float) aPoint3.getX();
		final float z3 = (float) aPoint3.getZ();

		int old_y = START_Y;

		/* work out how much to inc/dec x and z by as we inc/dec y */

		// height
		final int y_diff = END_Y - START_Y;
		final int y_diff_abs = (y_diff > 0 ? y_diff : -y_diff);

		// dx/dy
		final float x_inc1 = (x3 - x1) * 1.0f / y_diff_abs;
		final float x_inc2 = (x3 - x2) * 1.0f / y_diff_abs;
		// dz/dy
		final float z_inc1 = (z3 - z1) * 1.0f / y_diff_abs;
		final float z_inc2 = (z3 - z2) * 1.0f / y_diff_abs;

		// draw base line?
		if (aDrawLineBetweenPoints1And2) {
			drawHorizontalLine(aSettings, aTriangleColor, (int) x1, z1,
					aColor1, (int) x2, z2, aColor2, (int) aPoint1.getY());
		}

		/* now draw the other two lines */

		// if triangle is just a straight line
		if (0 == y_diff) {
			if (x3 < x1) {
				drawHorizontalLine(aSettings, aTriangleColor, (int) x3, z3,
						aColor3, (int) x2, z2, aColor2, START_Y);
			} else if (x3 > x2) {
				drawHorizontalLine(aSettings, aTriangleColor, (int) x1, z1,
						aColor1, (int) x3, z3, aColor3, START_Y);
			} else {
				drawHorizontalLine(aSettings, aTriangleColor, (int) x1, z1,
						aColor1, (int) x2, z2, aColor2, START_Y);
			}
		} else {
			// top-down
			if (y_diff > 0) {
				for (int y = START_Y; y <= END_Y; y += 1.0f) {
					// if we're not in the first loop iteration
					if (y != old_y) {
						// draw more of the line from point 1 to point 3
						drawHorizontalLine(aSettings, aTriangleColor,
								(int) old_x1, old_z1, aColor1, (int) x1, z1,
								aColor1, old_y);
						// draw more of the line from point 2 to point 3
						drawHorizontalLine(aSettings, aTriangleColor,
								(int) old_x2, old_z2, aColor2, (int) x2, z2,
								aColor2, old_y);

						// shading mode
						if (!aSettings.iWireframeModeEnabled) {
							// draw line from x1 to x2
							drawHorizontalLine(aSettings, aTriangleColor,
									(int) x1, z1, aColor1, (int) x2, z2,
									aColor1, old_y);
						}
					}
					// save x's
					old_x1 = x1;
					old_x2 = x2;
					// save z's
					old_z1 = z1;
					old_z2 = z2;
					// save y
					old_y = y;
					// next values
					x1 += x_inc1;
					x2 += x_inc2;
					z1 += z_inc1;
					z2 += z_inc2;
				} // end for y
			} // end top-down
				// bottom-up
			else /* if (y_diff < 0) */
			{
				for (int y = START_Y; y >= END_Y; y -= 1.0f) {
					// if we're not in the first loop iteration
					if (y != old_y) {
						// draw more of the line from point 1 to point 3
						drawHorizontalLine(aSettings, aTriangleColor,
								(int) old_x1, old_z1, aColor1, (int) x1, z1,
								aColor1, old_y);
						// draw more of the line from point 2 to point 3
						drawHorizontalLine(aSettings, aTriangleColor,
								(int) old_x2, old_z2, aColor2, (int) x2, z2,
								aColor2, old_y);

						// shading mode
						if (!aSettings.iWireframeModeEnabled) {
							// draw line from x1 to x2
							drawHorizontalLine(aSettings, aTriangleColor,
									(int) x1, z1, aColor1, (int) x2, z2,
									aColor1, old_y);
						}
					}
					// save x's
					old_x1 = x1;
					old_x2 = x2;
					// save z's
					old_z1 = z1;
					old_z2 = z2;
					// save y
					old_y = y;
					// next values
					x1 += x_inc1;
					x2 += x_inc2;
					z1 += z_inc1;
					z2 += z_inc2;
				} // end for y
			} // end bottom-up

			// draw final part of line from point 1 to point 3
			drawHorizontalLine(aSettings, aTriangleColor, (int) old_x1, old_z1,
					aColor1, (int) x3, z3, aColor3, old_y);
			// draw final part of line from point 2 to point 3
			drawHorizontalLine(aSettings, aTriangleColor, (int) old_x2, old_z2,
					aColor2, (int) x3, z3, aColor3, old_y);

		} // end triangle is not just a line

	}

	/**
	 * Draw a horizontal line.
	 * 
	 * @param aSettings
	 * @param aFlatShadeColor
	 *            the line color if flat-shading.
	 * @param x1
	 *            the x-coordinate of the start point.
	 * @param z1
	 *            the z-coordinate of the start point.
	 * @param color1
	 *            the color of the start point.
	 * @param x2
	 *            the x-coordinate of the end point.
	 * @param z2
	 *            the z-coordinate of the end point.
	 * @param color2
	 *            the color of the end point.
	 * @param y
	 *            the y-coordinate.
	 */
	private void drawHorizontalLine(RasterSettings aSettings,
			Color aFlatShadeColor, int x1, float z1, Color color1, int x2,
			float z2, Color color2, int y) {
		// check that it's visible
		if (0 > y || aSettings.iViewportDimensions.height <= y)
			return;

		// if z-buffer is enabled
		if (aSettings.iZBufferEnabled) {
			// ensure x1 is the left-most point
			if (x1 > x2) {
				iTempInt = x1;
				x1 = x2;
				x2 = iTempInt;
				iTempFloat = z1;
				z1 = z2;
				z2 = iTempFloat;
			}

			// work out z-increment (use 0 if the line is actually just a dot)
			float z_inc = (x1 < x2) ? (z2 - z1) / (x2 - x1) : 0;

			// clip to viewport boundaries
			if (0 > x1) {
				if (0 > x2)
					return;

				z1 = z1 + (-x1) * z_inc;
				x1 = 0;
			}
			if (aSettings.iViewportDimensions.width <= x2) {
				if (aSettings.iViewportDimensions.width <= x1)
					return;

				z2 = z2 - (x2 - aSettings.iViewportDimensions.width - 1)
						* z_inc;
				x2 = aSettings.iViewportDimensions.width - 1;
			}

			int x = x1;
			float z = z1;

			while (x <= x2) {
				// iterate past visible pixels until we hit one that's
				// occluded or until we pass the last pixel
				while (x <= x2
						&& (iZBufferFrameNumber[x][y] != iFrameNumber || z > iZBuffer[x][y])) {
					++x;
					z += z_inc;
					continue;
				}

				/*
				 * NOTE: if we've passed the last pixel (i.e. x > x2) then it
				 * means that the rest of the line is visible
				 */

				// draw a line up until the occluded pixel and update the
				// z-buffer and start points
				if (x > x1) {
					aSettings.iGraphics.drawLine(x1, y, x - 1, y);

					z = z1;
					for (int i = x1; i < x; ++i) {
						iZBuffer[i][y] = z;
						iZBufferFrameNumber[i][y] = iFrameNumber;
						z += z_inc;
					}
					// restore value of z to what it should be
					z += z_inc;
				}

				// iterate past occluded pixels until we hit one that's
				// visible or until we pass the last pixel
				while (x <= x2
						&& (iZBufferFrameNumber[x][y] == iFrameNumber && z <= iZBuffer[x][y])) {
					++x;
					z += z_inc;
					continue;
				}

				// next set of visible pixels start at this point
				x1 = x;
				z1 = z;
			}
		}
		// if z-buffer is off
		else {
			aSettings.iGraphics.drawLine(x1, y, x2, y);
		}
	}

	/**
	 * Draw a vertical line.
	 * 
	 * @param aSettings
	 * @param aFlatShadeColor
	 *            the line color if flat-shading.
	 * @param y1
	 *            the y-coordinate of the start point.
	 * @param z1
	 *            the z-coordinate of the start point.
	 * @param color1
	 *            the color of the start point.
	 * @param y2
	 *            the y-coordinate of the end point.
	 * @param z2
	 *            the z-coordinate of the end point.
	 * @param color2
	 *            the color of the end point.
	 * @param x
	 *            the x-coordinate.
	 */
	private void drawVerticalLine(RasterSettings aSettings,
			Color aFlatShadeColor, int y1, float z1, Color color1, int y2,
			float z2, Color color2, int x) {
		// check that it's visible
		if (0 > x || aSettings.iViewportDimensions.width <= x)
			return;

		// if z-buffer is enabled
		if (aSettings.iZBufferEnabled) {
			// ensure y1 is the top-most point
			if (y1 > y2) {
				iTempInt = y1;
				y1 = y2;
				y2 = iTempInt;
				iTempFloat = z1;
				z1 = z2;
				z2 = iTempFloat;
			}

			// work out z-increment (use 0 if the line is actually just a dot)
			float z_inc = (y1 < y2) ? (z2 - z1) / (y2 - y1) : 0;

			// clip to viewport boundaries
			if (0 > y1) {
				if (0 > y2)
					return;

				z1 = z1 + (-y1) * z_inc;
				y1 = 0;
			}
			if (aSettings.iViewportDimensions.width <= y2) {
				if (aSettings.iViewportDimensions.width <= y1)
					return;

				z2 = z2 - (y2 - aSettings.iViewportDimensions.width - 1)
						* z_inc;
				y2 = aSettings.iViewportDimensions.height - 1;
			}

			int y = y1;
			float z = z1;

			while (y <= y2) {
				// iterate past visible pixels until we hit one that's
				// occluded or until we pass the last pixel
				while (y <= y2
						&& (iZBufferFrameNumber[x][y] != iFrameNumber || z > iZBuffer[x][y])) {
					++y;
					z += z_inc;
					continue;
				}

				/*
				 * NOTE: if we've passed the last pixel (i.e. x > x2) then it
				 * means that the rest of the line is visible
				 */

				// draw a line up until the occluded pixel and update the
				// z-buffer and start points
				if (y > y1) {
					aSettings.iGraphics.drawLine(x, y1, x, y - 1);
					z = z1;
					for (int i = y1; i < y; ++i) {
						iZBuffer[x][i] = z;
						iZBufferFrameNumber[x][i] = iFrameNumber;
						z += z_inc;
					}
					// restore value of z to what it should be
					z += z_inc;
				}

				// iterate past occluded pixels until we hit one that's
				// visible or until we pass the last pixel
				while (y <= y2
						&& (iZBufferFrameNumber[x][y] == iFrameNumber && z <= iZBuffer[x][y])) {
					++y;
					z += z_inc;
					continue;
				}

				// next set of visible pixels start at this point
				y1 = y;
				z1 = z;
			}
		}
		// if z-buffer is off
		else {
			aSettings.iGraphics.drawLine(x, y1, x, y2);
		}
	}

	public void resetStaticData() {
		iInstance = null;
	}

	// Painter algo no zBuffer
	public void drawQuad(RasterSettings iRasterSettings, Color iTempCol1,
			Vector3D vector3d1, Vector3D vector3d2, Vector3D vector3d3,
			Vector3D vector3d4) {
		   Vector3D AToB = vector3d2.subtract( vector3d1);
		   Vector3D BToC = vector3d3.subtract( vector3d2);
		   Vector3D CToA = vector3d1.subtract( vector3d3);
		   double crossz = AToB.getX() * BToC.getY()  - AToB.getY()  * BToC.getX() ;
		
		   if ( crossz >= 0f )
		   {
		     // clockwise
			   iRasterSettings.iGraphics.setColor(iTempCol1.brighter());
		   }
		   else
		   {
		     // counter-clockwise.  
			   iRasterSettings.iGraphics.setColor( new Color(255-iTempCol1.getRed(),255-iTempCol1.getRed(),255-iTempCol1.getRed()));
		   }
		Polygon poly = new Polygon();
		poly.addPoint((int) vector3d1.getX(), (int) vector3d1.getY());
		poly.addPoint((int) vector3d2.getX(), (int) vector3d2.getY());
		poly.addPoint((int) vector3d3.getX(), (int) vector3d3.getY());
		poly.addPoint((int) vector3d4.getX(), (int) vector3d4.getY());
		//iRasterSettings.iGraphics.setPaint(iTempCol1);
		// iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			
		 iRasterSettings.iGraphics.fill(poly);

		//iRasterSettings.iGraphics.setPaint(new Color(37, 191, 0));
		
		// iRasterSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		iRasterSettings.iGraphics.setPaint( Color.black);
		iRasterSettings.iGraphics.draw(poly);
		// TODO Auto-generated method stub

	}

	public void drawWiredQuad(RasterSettings aSettings, Color aTriangleColor,
			Vector3D aPoint1, Vector3D aPoint2, Vector3D aPoint3,
			Vector3D aPoint4) {
		// aSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		drawLine(aSettings, aTriangleColor, aPoint1, aPoint2, 1, true);
		drawLine(aSettings, aTriangleColor, aPoint2, aPoint3, 1, true);
		drawLine(aSettings, aTriangleColor, aPoint3, aPoint4, 1, true);
		drawLine(aSettings, aTriangleColor, aPoint4, aPoint1, 1, true);
	}

	public void drawWiredTriangle(RasterSettings aSettings,
			Color aTriangleColor, Vector3D aPoint1, Vector3D aPoint2,
			Vector3D aPoint3) {

		drawLine(aSettings, aTriangleColor, aPoint1, aPoint2, 1, true);
		drawLine(aSettings, aTriangleColor, aPoint2, aPoint3, 1, true);
		drawLine(aSettings, aTriangleColor, aPoint3, aPoint1, 1, true);
	}

	private boolean VecIntEqual(Vector3D v1, Vector3D v2){
		//if(((int)v1.getX() ==(int)v2.getX()) && ((int)v1.getY() ==(int)v2.getY())) return true;
		if((Math.abs(v1.getX()-v2.getX())+Math.abs(v1.getY()-v2.getY()))<=2) return true;
		return false;
		
	}
	public void drawLine(RasterSettings aSettings, Color color, Vector3D pt1,
			Vector3D pt2, int width, boolean zBuffer) {
		if (!zBuffer) {
			aSettings.iGraphics.setPaint(color);
			aSettings.iGraphics.drawLine((int) pt1.getX(), (int) pt1.getY(),
					(int) pt2.getX(), (int) pt2.getY());
		} else {
			//draw ZBufferedLine
			int x1, y1, x2, y2;
			double z1;
			double z2;
			double z;
			x1 = (int) pt1.getX();
			y1 = (int) pt1.getY();
			x2 = (int) pt2.getX();
			y2 = (int) pt2.getY();
			z1 = pt1.getZ();
			z2 = pt2.getZ();
			aSettings.iGraphics.setPaint(color);
			// aSettings.iGraphics.drawLine(x1,y1,x2,y2);
			Line2D line = new Line2D.Double(x1, y1, x2, y2);
			Point2D current;
			aSettings.iGraphics.setStroke(new BasicStroke(width));
			int x, y;
			Point2D point1 = new Point2D.Double(x1, y1);
			// aSettings.iGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			for (Iterator<Point2D> iter = new LineIterator(line); iter
					.hasNext();) {
				current = iter.next();
				point1 = current;
				x = (int) current.getX();
				y = (int) current.getY();
				if (0 > y || aSettings.iViewportDimensions.height <= y || 0 > x
						|| aSettings.iViewportDimensions.width <= x)
					continue;

				if (x2 != x1) {
					z = z1 + (z2 - z1) * (current.getX() - x1) / (x2 - x1)
							+ .000; //increase z by some value for wireframe to appear
				} else {
					z = z1 + (z2 - z1) * (current.getY() - y1) / (y2 - y1)
							+ 0.000;
				}

				if (z >= iZBuffer[x][y]) {
					iZBuffer[x][y] = (float) z;
					aSettings.iGraphics.drawLine((int) current.getX(),
							(int) current.getY(), (int) current.getX(),
							(int) current.getY());

				}
			}

		}
	}
	
	
}
