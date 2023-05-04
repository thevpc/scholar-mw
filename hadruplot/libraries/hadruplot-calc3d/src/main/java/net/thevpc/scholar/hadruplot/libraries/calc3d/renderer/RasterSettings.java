
package net.thevpc.scholar.hadruplot.libraries.calc3d.renderer;

import java.awt.Dimension;
import java.awt.Graphics2D;


/**
 * Settings for the rasteriser.
 */
class RasterSettings
{
	/**
	 * Viewport size.
	 */
	public Dimension iViewportDimensions = new Dimension();
	/**
	 * Whether Z-buffer is enabled or not.
	 */
	public boolean iZBufferEnabled = false;
	/**
	 * Whether wireframe mode is enabled or not.
	 */
	public boolean iWireframeModeEnabled = false;
	/**
	 * The graphics device context to use
	 */
	public Graphics2D iGraphics = null;
}

