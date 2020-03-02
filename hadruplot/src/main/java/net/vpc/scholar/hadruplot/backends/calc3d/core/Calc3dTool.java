package net.vpc.scholar.hadruplot.backends.calc3d.core;

import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Clip;
import net.vpc.scholar.hadruplot.backends.calc3d.renderer.Canvas3D;
import net.vpc.scholar.hadruplot.backends.calc3d.renderer.InteractionHandler;
import net.vpc.scholar.hadruplot.backends.calc3d.renderer.Renderer;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Calc3dTool {
    /**
     * Drawing Canvas
     */
    private Canvas3D canvas3D;
    /**
     * Renderer asssociated with canvas
     */
    private net.vpc.scholar.hadruplot.backends.calc3d.renderer.Renderer renderer;
    /**
     * Scenemanager keeps and manages Elements of Canvas
     */
    private SceneManager sceneManager;
    /**
     * true means file has been modified and needs to be saved
     */
    private boolean dirty = false;
    /**
     * Last Working directories/files
     */
    private String lastDirectory = null;
    private String lastFileName = null;

    public Calc3dTool() {
        sceneManager = new SceneManager();
        renderer = new net.vpc.scholar.hadruplot.backends.calc3d.renderer.Renderer(sceneManager.getSettings());
        canvas3D = new Canvas3D(sceneManager.getSettings());
        Camera3D cam = new Camera3D();
        cam.setOrthographic(!sceneManager.getSettings().perspectiveEnabled);
        canvas3D.setCamera(cam);
        canvas3D.setRenderer(renderer);

        //canvas3D.setBackgroundColor(Color.lightGray.brighter());
        canvas3D.setScene(sceneManager.createScene(true));//ObjectFactory.createDemo());
        canvas3D.setRequestFocusEnabled(false);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();

        canvas3D.getRenderer().setBackgroundColor(sceneManager.getSettings().backgroundColor);

        canvas3D.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        //canvas3D.setBackground(Color.white);

        // scenes
        //setFocusable(true);
        //.setf
        canvas3D.setInteractionHandler(new InteractionHandler(sceneManager.getSettings()));
    }

    public void createDemo() {
        sceneManager.createDemo();
    }

    public boolean isDirty() {
        return dirty;
    }

    public Calc3dTool setDirty(boolean dirty) {
        this.dirty = dirty;
        return this;
    }

    public boolean switchPerspective() {
        sceneManager.getSettings().perspectiveEnabled = !canvas3D.getRenderer().isPerspectiveEnabled();
        canvas3D.getRenderer().setPerspectiveEnabled(!canvas3D.getRenderer().isPerspectiveEnabled());
        return canvas3D.getRenderer().isPerspectiveEnabled();
    }

    public boolean switchStereoscope() {
        sceneManager.getSettings().steroscopyEnabled = !canvas3D.getRenderer().isStereoscopyEnabled();
        canvas3D.getRenderer().setStereoscopyEnabled(!canvas3D.getRenderer().isStereoscopyEnabled());
        return sceneManager.getSettings().steroscopyEnabled;
    }

    public void resetCamera() {
        canvas3D.iCamera.reset();
        canvas3D.refresh();
    }

    public void zoomOut() {
        canvas3D.iCamera.setFov((canvas3D.iCamera.getFov() + 1) % 360);
        if (sceneManager.getSettings().steroscopyEnabled) {
            canvas3D.iCameraL.setFov(canvas3D.iCamera.getFov());
            canvas3D.iCameraR.setFov(canvas3D.iCamera.getFov());
        }
        sceneManager.getSettings().fov = canvas3D.iCamera.getFov();
        canvas3D.refresh();
    }

    public void zoomIn() {
        canvas3D.iCamera.setFov((canvas3D.iCamera.getFov() + 359) % 360);
        if (sceneManager.getSettings().steroscopyEnabled) {
            canvas3D.iCameraL.setFov(canvas3D.iCamera.getFov());
            canvas3D.iCameraR.setFov(canvas3D.iCamera.getFov());
        }
        sceneManager.getSettings().fov = canvas3D.iCamera.getFov();
        canvas3D.refresh();
    }

    public void removeElementAt(int index) {
        sceneManager.removeElement(sceneManager.getElement3D(index));
    }

    public void addElement(Element3D element) {
        sceneManager.addElement(element);
        refreshCanvas();
    }

    public void refreshCanvas() {
        refreshCanvas(false);
    }

    public void refreshCanvas(boolean reCreateEachElement) {
        canvas3D.setScene(sceneManager.createScene(reCreateEachElement));
        canvas3D.refresh();
    }

    public boolean switchGridxy() {
        sceneManager.setGridXYVisible(!sceneManager.isGridXYVisible());
        refreshCanvas();
        return sceneManager.isGridXYVisible();
    }

    public boolean switchLight() {
        canvas3D.getRenderer().setLightsEnabled(!canvas3D.getRenderer().isLightsEnabled());
        canvas3D.refresh();
        return (canvas3D.getRenderer().isLightsEnabled());
    }

    public boolean switchAxis() {
        sceneManager.setAxisVisible(!sceneManager.isAxisVisible());
        refreshCanvas();
        return sceneManager.isAxisVisible();
    }

    public boolean switchAntiAlias() {
        sceneManager.getSettings().antiAliasingEnabled = !canvas3D.getRenderer().isAntiAliasingEnabled();
        canvas3D.getRenderer().setAntiAliasingEnabled(!canvas3D.getRenderer().isAntiAliasingEnabled());
        canvas3D.refresh();
        return (sceneManager.getSettings().antiAliasingEnabled);
    }

    public boolean switchBox() {
        sceneManager.getSettings().boxVisible = !sceneManager.getSettings().boxVisible;
        canvas3D.refresh();
        return (sceneManager.getSettings().boxVisible);
    }

    public void saveImage(String fileName) throws IOException {
        canvas3D.saveImage(fileName);
    }

    public String getLastDirectory() {
        return lastDirectory == null ? System.getProperty("user.dir") : lastDirectory;
    }

    public Calc3dTool setLastDirectory(String lastDirectory) {
        this.lastDirectory = lastDirectory;
        return this;
    }

    public String getLastFileName() {
        return lastFileName;
    }

    public Calc3dTool setLastFileName(String lastFileName) {
        this.lastFileName = lastFileName;
        if (this.lastFileName != null) {
            lastDirectory = this.lastFileName;
            // it also works if the next line is commented out!
            lastDirectory = lastDirectory.substring(0, lastDirectory.lastIndexOf(File.separatorChar));
        }

        return this;
    }

    public void newDocument() {
        sceneManager.getElement3DList().clear();
        refreshCanvas(true);
        setDirty(false);
        setLastFileName(null);
    }

    public Preferences loadFile(String fileName) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            ArrayList<Element3D> list = (ArrayList<Element3D>) is.readObject();
            sceneManager.getElement3DList().clear();
            sceneManager.getElement3DList().addAll(list);
            Preferences preferences = null;
            preferences = (Preferences) is.readObject();
            setDirty(false);
            return preferences;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new UncheckedIOException(new IOException(e));
        }

    }


    /**
     * Apply Settings
     *
     * @param reCreateScene set it true if scene is to be reCreated from scratch
     */
    public void applySettings(Preferences preferences, boolean reCreateScene) {
        this.canvas3D.getRenderer().setAntiAliasingEnabled(preferences.isAntiAliasingEnabled());
        this.canvas3D.getRenderer().setPerspectiveEnabled(preferences.isPerspectiveEnabled());
        this.canvas3D.stereoMode = preferences.getSteroscopicMode();
        this.canvas3D.stereoEnabled = preferences.isSteroscopyEnabled();
        this.canvas3D.getRenderer().setFogEnabled(preferences.isFogEnabled());
        this.canvas3D.getRenderer().setBackgroundColor(preferences.getBackColor());
        this.sceneManager.getSettings().axisColor = preferences.getAxisColor();
        this.canvas3D.getLights()[0].setEnabled(preferences.isLight1Enabled());
        this.canvas3D.getLights()[1].setEnabled(preferences.isLight2Enabled());
        this.canvas3D.getLights()[2].setEnabled(preferences.isLight3Enabled());
        this.sceneManager.getSettings().saveSettings(preferences);
        this.sceneManager.setClip(new Clip(this.sceneManager.getSettings().mappedClipBox));
        refreshCanvas(reCreateScene);
    }

    public void refreshScene() {
        this.canvas3D.setScene(this.sceneManager.createScene(true));
        this.canvas3D.refresh();
    }

    public void seVisibleElement(int row, boolean value) {
        sceneManager.getElement3D(row).prefs().setVisible((Boolean) value);
        refreshCanvas();
    }

    public int getElementCount() {
        return sceneManager.getElementCount();
    }

    public Element3D getElement(int i) {
        return sceneManager.getElement3D(i);
    }

    public void updateElement(int i, Element3D element) {
        sceneManager.setElement3D(i, element);
        refreshCanvas();
    }

    public void setElements(List<Element3D> list) {
        sceneManager.getElement3DList().clear();
        sceneManager.getElement3DList().addAll(list);
    }

    public void setElementValidName(Element3D element3D) {
        sceneManager.setValidName(element3D);
    }

    /**
     * Prints the graphics on Canvas
     */
    public void printGraphics() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(canvas3D);
        if (pj.printDialog()) {
            try {
                pj.print();
            } catch (PrinterException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    public void setSteroscopicMode(int i) {
        this.getSettings().steroscopyEnabled = true;
        this.canvas3D.getRenderer().setStereoscopyEnabled(true);
        this.getSettings().steroscopicMode = i;
        this.canvas3D.stereoMode = this.getSettings().steroscopicMode;
        this.canvas3D.refresh();
    }

    public Globalsettings getSettings() {
        return sceneManager.getSettings();
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public Canvas3D getCanvas() {
        return canvas3D;
    }

    public Renderer getCanvasRenderer() {
        return canvas3D.getRenderer();
    }

    public void writeFile(String fileName) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(sceneManager.getElement3DList());
            os.writeObject(getSettings().getPreferences());
            setDirty(false);
            setLastFileName(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
