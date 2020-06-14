package net.vpc.scholar.hadruplot.libraries.calc3d.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.vpc.scholar.hadruplot.libraries.calc3d.core.Globalsettings;
import net.vpc.scholar.hadruplot.libraries.calc3d.anaglyph.AnaglyphFactory;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Light3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Scene3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Object3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.bsp.BSPTree;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.bsp.BSPTreeBuilder;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.bsp.BSPTreeTraverser;
import net.vpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.utils.ColorUtils;

/**
 * A surface which renders graphics and handles user input.
 */
public final class Canvas3D extends JPanel implements Printable {

    private static Logger LOG = Logger.getLogger(Canvas3D.class.getName());

    private static final long serialVersionUID = -6195462797470825673L;

    private InteractionHandler iHandler;
    private Renderer iRenderer;
    private Scene3D iScene = null;
    private Dimension iDimensions = new Dimension();
    public Camera3D iCamera, iCameraL, iCameraR;
    private Light3D[] iLights;
    private BufferedImage dbImageL, dbImageR, dbImage;
    private double cx = 0, cy = 0;
    public int stereoMode = 2;
    public boolean stereoEnabled = false;
    AnaglyphFactory anaglyphFactory = new AnaglyphFactory();

    BSPTreeBuilder bspBuilder = new BSPTreeBuilder();
    BSPTree currenttree;
    BSPTreeTraverser bspTraverser;
    private boolean isRendering = false;

    /**
     * Constructor.
     *
     * @param iContainer the top-level container which holds this surface. This
     * may be null.
     */
    public Canvas3D(Globalsettings globalsettings) {
        super();
        iLights = new Light3D[3];
        iLights[0] = new Light3D();
        iLights[1] = new Light3D(new Vector3D(0, 5, 0), new Vector3D(0, -1, 0), Color.white, true);
        iLights[2] = new Light3D(new Vector3D(10, 10, 10), new Vector3D(-1, -1, -1), Color.white, true);
        iCamera = new Camera3D();
        iCameraL = new Camera3D();
        iCameraR = new Camera3D();
        iCameraL.rotateAroundFocus(0, 2.5, 0);
        iCameraR.rotateAroundFocus(0, -2.5, 0);
        iRenderer = new Renderer(globalsettings);
        dbImageL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        dbImageR = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        dbImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        iRenderer.setLightCameraandImage(this, iLights, iCamera, dbImage);
        iHandler = new InteractionHandler(globalsettings);
        setInteractionHandler(iHandler);
        updateViewPort();
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Get the scene camera.
     *
     * @return the scene camera. Default is null.
     */
    public Camera3D getCamera() {
        return iCamera;
    }

    /**
     * Set the scene camera.
     *
     * @param camera the camera. If null then there is no camera positioning.
     */
    public void setCamera(Camera3D camera) {
        iCamera = camera;
        refresh();
    }

    /**
     * Get the light source of scene.
     *
     * @return the lighSource of scene. Default is null.
     */
    public Light3D[] getLights() {
        return iLights;
    }

    /**
     * Set the scene light.
     *
     * @param light light source of the scene. If null then there is no
     * lightening.
     */
    public void setLights(Light3D[] light) {
        iLights = light;
        refresh();
    }

    /**
     * Get the scene that is being rendered.
     *
     * @return the scene. Default is null.
     */
    public Scene3D getScene() {
        return iScene;
    }

    /**
     * Set the scene to render.
     *
     * @param scene the scene. If null then no scene is rendered.
     */
    public void setScene(Scene3D scene) {
        iScene = scene;
        BSPTreeBuilder bspBuilder = new BSPTreeBuilder();

        List<PrimitiveElement3D> primitiveElement3DS = new ArrayList<PrimitiveElement3D>();
        for (Object3D<PrimitiveElement3D> obj : scene.object3Ds) {
            for (PrimitiveElement3D e : obj.elements) {
                primitiveElement3DS.add(e);
            }
        }
        currenttree = bspBuilder.build(primitiveElement3DS);
        refresh();
    }

    /**
     * Get the current renderer.
     *
     * @return the current renderer. Default is null.
     */
    public Renderer getRenderer() {
        return iRenderer;
    }

    /**
     * Set the current renderer.
     *
     * @param aRenderer the new renderer. Use null to disable rendering..
     */
    public void setRenderer(Renderer aRenderer) {
        iRenderer = aRenderer;
        iRenderer.setLightCameraandImage(this, this.iLights, this.iCamera, dbImage);
        bspTraverser = new BSPTreeTraverser(aRenderer);
        refresh();
    }

    /**
     * Get the current interaction handler for this window.
     *
     * @return the current interaction handler. Default is null.
     */
    public InteractionHandler getInteractonHandler() {
        return iHandler;
    }

    /**
     * Set the current interaction handler.
     *
     * This window should now be considered as the the owner of this handler.
     * {@link InteractionHandler#reset()} will get called automatically.
     *
     * @param aHandler the new interaction handler. Use null to disable
     * interaction handling.
     */
    public void setInteractionHandler(InteractionHandler aHandler) {
        if (null != iHandler) {
            //AppBridge.getApp().removeKeyListener(iHandler);
            this.removeKeyListener(iHandler);
            this.removeMouseListener(iHandler);
            this.removeMouseMotionListener(iHandler);
            this.removeMouseWheelListener(iHandler);
            this.removeComponentListener(iHandler);
            iHandler.associate(null);
        }

        iHandler = aHandler;

        if (null != iHandler) {
            iHandler.associate(this);
            iHandler.reset();

            addKeyListener(iHandler);
            addMouseListener(iHandler);
            addMouseMotionListener(iHandler);
            addMouseWheelListener(iHandler);
            this.addComponentListener(iHandler);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //check if everything is ready to go
        if (null == iRenderer || null == iCamera || null == iScene) {
            super.paintComponent(g);
            LOG.error("Either Camera, Scene or Renderer is not associated with canvas");
            return;
        }
        //check if image is ready
        if (!(null != dbImage)) {
            super.paintComponent(g);
            LOG.error("paintComponent: Renderer is not initialised or is not Ready to render the scene");
            return;
        } else {
            g.drawImage(dbImage, 0, 0, getWidth() + 1, getHeight() + 1, this);
            //g.drawImage(dbImageR, 0, 140, getWidth()+1, getHeight()+1,this);
        }

    }

    /**
     * Shifts centre of viewport(screen)
     *
     * @param deltaX
     * @param deltaY
     */
    public void translateViewPort(double deltaX, double deltaY) {
        cx += deltaX;
        cy += deltaY;
        updateViewPort();
    }

    /**
     * Called whenever canvas resizes
     */
    public void updateViewPort() {
        iDimensions.width = getWidth();
        iDimensions.height = getHeight();
        //cx, cy are centre coordinates of canvas
        iRenderer.setupViewport(iDimensions, cx, cy);
        refresh();
    }

    /**
     * Re-render the scene.(Synchronized since main thread and interaction
     * handeler thread both amy call it together)
     */
    public void refresh() {
        if (isRendering) {
            return;
        }
        isRendering = true;
        if (stereoEnabled) {

            // Create left eye scene
            dbImageL = new BufferedImage(this.getWidth() + 1, this.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
            iRenderer.setLightCameraandImage(this, this.iLights, this.iCameraL, dbImageL);
            iRenderer.render(iScene);
            iRenderer.drawBox();
            // iCameraL.setOrthographic(iCamera.isOrthographic());
            if (null != currenttree) {
                bspTraverser.traverse(currenttree, iCameraL.eye);
            }
            iRenderer.drawBoundingLines();

            if (null != currenttree) {
                bspTraverser.traverse(currenttree, iCamera.eye);
            }
            dbImageR = new BufferedImage(this.getWidth() + 1, this.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
            // iCameraR.setOrthographic(iCamera.isOrthographic());
            iRenderer.setLightCameraandImage(this, this.iLights, this.iCameraR, dbImageR);
            iRenderer.render(iScene);
            iRenderer.drawBox();
            if (null != currenttree) {
                bspTraverser.traverse(currenttree, iCameraR.eye);
            }
            iRenderer.drawBoundingLines();
            anaglyphFactory.setImage1(dbImageL);
            anaglyphFactory.setImage2(dbImageR);
            // dbImage=dbImageL;
            dbImage = new BufferedImage(this.getWidth() + 1, this.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
            anaglyphFactory.CreateAnaglyph(stereoMode, dbImage); // best look at 3
            repaint();
        } else {
            dbImage = new BufferedImage(this.getWidth() + 1, this.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
            iRenderer.setLightCameraandImage(this, this.iLights, this.iCamera, dbImage);
            iRenderer.render(iScene);
            iRenderer.drawBox();
            if (null != currenttree) {
                bspTraverser.traverse(currenttree, iCamera.eye);
            }
            iRenderer.drawBoundingLines();
            repaint();
        }
        isRendering = false;
    }

    public void saveImage(String fileName) throws IOException {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D)img.getGraphics();
        int width = img.getWidth(), height = img.getHeight();
        iRenderer.setLightCameraandImage(this, this.iLights, this.iCamera, img);
        iRenderer.render(iScene);
        iRenderer.drawBox();
        if (null != currenttree) {
            bspTraverser.traverse(currenttree, iCamera.eye);
        }
        iRenderer.drawBoundingLines();
        Paint bg = iRenderer.getBackgroundColor();
        if (bg instanceof Color) {
            g.setPaint(ColorUtils.blendColors(Color.BLACK, bg, .5));
        } else {
            g.setPaint(bg);
        }
        g.drawRect(1, 1, width - 3, height - 3);
        g.setPaint(ColorUtils.blendColors(Color.WHITE, bg, .5));
        g.drawRect(0, 0, width - 1, height - 1);
        ImageIO.write(img, "png", new File(fileName));
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        if (pageIndex > 0) {
            /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }
        /*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        paintComponent(g2d);
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

}
