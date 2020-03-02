package net.vpc.scholar.hadruplot.backends.calc3d.core;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Box3D;

import java.awt.*;
import java.io.Serializable;

public class Preferences implements Serializable {
    public ExtraPrefs extra;

    /*
     * ClipSettings
     */
    private Box3D clipBox = new Box3D();

    /*
     * Renderer Settings
     */
    private boolean antiAliasingEnabled = false;
    private boolean perspectiveEnabled = true;
    private boolean steroscopyEnabled = false;
    private int steroscopicMode = 2;
    private Color backColor = Color.red;
    private boolean fogEnabled = true;
    private boolean light1Enabled = true;
    private boolean light2Enabled = true;
    private boolean light3Enabled = false;

    /*
     * Axis Settings
     */
    private boolean xAxisVisible = true;
    private boolean yAxisVisible = true;
    private boolean zAxisVisible = true;
    private boolean xyGridVisible = false;
    private Box3D axesBox = new Box3D();
    private int axisTicks = 5;
    private int axisWidth = 1;
    private Color axisColor = Color.black;
    /*
     * Box Settings
     */
    private boolean gridsVisible = true;
    private boolean planesVisible = true;
    private boolean labelsVisible = true;
    private boolean boxVisible = true;
    private boolean ticksVisible = true;

    private boolean ticks = true;
    private int divisions = 5;
    private int subDivisions = 3;

    private double fov;

    public Preferences() {
        clipBox = new Box3D(-5, 5, -5, 5, -5, 5);
        axesBox = new Box3D(-5, 5, -5, 5, -5, 5);
    }

    /**
     * @return Instance of this class carrying default Settings
     */
    public static Preferences getDefaultSettings() {
        Preferences preferences = new Preferences();
        preferences.resetToDefaults();
        return preferences;
    }

    public void resetToDefaults() {
//        lookandFeel = UIManager.LookAndFeelInfo.class.getName();
//        fileToolbarVisible = true;
//        objectToolbarVisible = true;
//        statusbarVisible = true;
//        tipofDayEnabled = true;
//        splashScreenEnabled = true;

        /*
         * ClipSettings
         */
        clipBox = new Box3D(-5, 5, -5, 5, -5, 5);

        /*
         * Renderer Settings
         */
        antiAliasingEnabled = false;
        perspectiveEnabled = true;
        steroscopyEnabled = false;
        steroscopicMode = 2;
        backColor = Color.white;
        fogEnabled = true;
        light1Enabled = true;
        light2Enabled = true;
        light3Enabled = false;

        /*
         * Axis Settings
         */
        xAxisVisible = true;
        yAxisVisible = true;
        zAxisVisible = true;
        xyGridVisible = false;
        axesBox = new Box3D(-5, 5, -5, 5, -5, 5);
        axisTicks = 5;
        axisWidth = 1;
        axisColor = Color.black;
        /*
         * Box Settings
         */
        gridsVisible = true;
        planesVisible = true;
        labelsVisible = true;
        boxVisible = true;
        ticksVisible = true;

        ticks = true;
        divisions = 5;
        subDivisions = 3;
    }


    public <T extends ExtraPrefs> T getExtra() {
        return (T) extra;
    }

//    /**
//     * @return the lookandFeel
//     */
//    public String getLookandFeel() {
//        return lookandFeel;
//    }
//
//
//    /**
//     * @param lookandFeel the lookandFeel to set
//     */
//    public void setLookandFeel(String lookandFeel) {
//        this.lookandFeel = lookandFeel;
//    }
//
//
//    /**
//     * @return the fileToolbarVisible
//     */
//    public boolean isFileToolbarVisible() {
//        return fileToolbarVisible;
//    }
//
//
//    /**
//     * @param fileToolbarVisible the fileToolbarVisible to set
//     */
//    public void setFileToolbarVisible(boolean fileToolbarVisible) {
//        this.fileToolbarVisible = fileToolbarVisible;
//    }
//
//
//    /**
//     * @return the editToolbarVisible
//     */
//    public boolean isObjectToolbarVisible() {
//        return objectToolbarVisible;
//    }
//
//
//    /**
//     * @param editToolbarVisible the editToolbarVisible to set
//     */
//    public void setObjectToolbarVisible(boolean editToolbarVisible) {
//        this.objectToolbarVisible = editToolbarVisible;
//    }
//
//
//    /**
//     * @return the statusbarVisible
//     */
//    public boolean isStatusbarVisible() {
//        return statusbarVisible;
//    }
//
//
//    /**
//     * @param statusbarVisible the statusbarVisible to set
//     */
//    public void setStatusbarVisible(boolean statusbarVisible) {
//        this.statusbarVisible = statusbarVisible;
//    }
//
//
//    /**
//     * @return the tipofDayEnabled
//     */
//    public boolean isTipofDayEnabled() {
//        return tipofDayEnabled;
//    }
//
//
//    /**
//     * @param tipofDayEnabled the tipofDayEnabled to set
//     */
//    public void setTipofDayEnabled(boolean tipofDayEnabled) {
//        this.tipofDayEnabled = tipofDayEnabled;
//    }
//
//
//    /**
//     * @return the splashScreenEnabled
//     */
//    public boolean isSplashScreenEnabled() {
//        return splashScreenEnabled;
//    }
//
//
//    /**
//     * @param splashScreenEnabled the splashScreenEnabled to set
//     */
//    public void setSplashScreenEnabled(boolean splashScreenEnabled) {
//        this.splashScreenEnabled = splashScreenEnabled;
//    }


    /**
     * @return the antiAliasingEnabled
     */
    public boolean isAntiAliasingEnabled() {
        return antiAliasingEnabled;
    }


    /**
     * @param antiAliasingEnabled the antiAliasingEnabled to set
     */
    public void setAntiAliasingEnabled(boolean antiAliasingEnabled) {
        this.antiAliasingEnabled = antiAliasingEnabled;
    }


    /**
     * @return the perspectiveEnabled
     */
    public boolean isPerspectiveEnabled() {
        return perspectiveEnabled;
    }


    /**
     * @param perspectiveEnabled the perspectiveEnabled to set
     */
    public void setPerspectiveEnabled(boolean perspectiveEnabled) {
        this.perspectiveEnabled = perspectiveEnabled;
    }


    /**
     * @return the steroscopyEnabled
     */
    public boolean isSteroscopyEnabled() {
        return steroscopyEnabled;
    }


    /**
     * @param steroscopyEnabled the steroscopyEnabled to set
     */
    public void setSteroscopyEnabled(boolean steroscopyEnabled) {
        this.steroscopyEnabled = steroscopyEnabled;
    }


    /**
     * @return the steroscopicMode
     */
    public int getSteroscopicMode() {
        return steroscopicMode;
    }


    /**
     * @param steroscopicMode the steroscopicMode to set
     */
    public void setSteroscopicMode(int steroscopicModeEnabled) {
        this.steroscopicMode = steroscopicModeEnabled;
    }


    /**
     * @return the backColor
     */
    public Color getBackColor() {
        return backColor;
    }


    /**
     * @param backColor the backColor to set
     */
    public void setBackColor(Color backColor) {
        this.backColor = backColor;
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
     * @return the light1Enabled
     */
    public boolean isLight1Enabled() {
        return light1Enabled;
    }


    /**
     * @param light1Enabled the light1Enabled to set
     */
    public void setLight1Enabled(boolean light1Enabled) {
        this.light1Enabled = light1Enabled;
    }


    /**
     * @return the light2Enabled
     */
    public boolean isLight2Enabled() {
        return light2Enabled;
    }


    /**
     * @param light2Enabled the light2Enabled to set
     */
    public void setLight2Enabled(boolean light2Enabled) {
        this.light2Enabled = light2Enabled;
    }


    /**
     * @return the light3Enabled
     */
    public boolean isLight3Enabled() {
        return light3Enabled;
    }


    /**
     * @param light3Enabled the light3Enabled to set
     */
    public void setLight3Enabled(boolean light3Enabled) {
        this.light3Enabled = light3Enabled;
    }


    /**
     * @return the xAxisVisible
     */
    public boolean isxAxisVisible() {
        return xAxisVisible;
    }


    /**
     * @param xAxisVisible the xAxisVisible to set
     */
    public void setxAxisVisible(boolean xAxisVisible) {
        this.xAxisVisible = xAxisVisible;
    }


    /**
     * @return the yAxisVisible
     */
    public boolean isyAxisVisible() {
        return yAxisVisible;
    }


    /**
     * @param yAxisVisible the yAxisVisible to set
     */
    public void setyAxisVisible(boolean yAxisVisible) {
        this.yAxisVisible = yAxisVisible;
    }


    /**
     * @return the zAxisVisible
     */
    public boolean iszAxisVisible() {
        return zAxisVisible;
    }


    /**
     * @param zAxisVisible the zAxisVisible to set
     */
    public void setzAxisVisible(boolean zAxisVisible) {
        this.zAxisVisible = zAxisVisible;
    }


    /**
     * @return the xyGridVisible
     */
    public boolean isXyGridVisible() {
        return xyGridVisible;
    }


    /**
     * @param xyGridVisible the xyGridVisible to set
     */
    public void setXyGridVisible(boolean xyGridVisible) {
        this.xyGridVisible = xyGridVisible;
    }


    /**
     * @return the axesBox
     */
    public Box3D getAxesBox() {
        return axesBox;
    }


    /**
     * @param axesBox the axesBox to set
     */
    public void setAxesBox(Box3D axesBox) {
        this.axesBox = axesBox;
    }


    /**
     * @return the axisTicks
     */
    public int getAxisTicks() {
        return axisTicks;
    }


    /**
     * @param axisTicks the axisTicks to set
     */
    public void setAxisTicks(int axisTicks) {
        this.axisTicks = axisTicks;
    }


    /**
     * @return the axisWidth
     */
    public int getAxisWidth() {
        return axisWidth;
    }


    /**
     * @param axisWidth the axisWidth to set
     */
    public void setAxisWidth(int axisWidth) {
        this.axisWidth = axisWidth;
    }


    /**
     * @return the axisColor
     */
    public Color getAxisColor() {
        return axisColor;
    }


    /**
     * @param axisColor the axisColor to set
     */
    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }


    /**
     * @return the gridsVisible
     */
    public boolean isGridsVisible() {
        return gridsVisible;
    }


    /**
     * @param gridsVisible the gridsVisible to set
     */
    public void setGridsVisible(boolean gridsVisible) {
        this.gridsVisible = gridsVisible;
    }


    /**
     * @return the planesVisible
     */
    public boolean isPlanesVisible() {
        return planesVisible;
    }


    /**
     * @param planesVisible the planesVisible to set
     */
    public void setPlanesVisible(boolean planesVisible) {
        this.planesVisible = planesVisible;
    }


    /**
     * @return the labelsVisible
     */
    public boolean isLabelsVisible() {
        return labelsVisible;
    }


    /**
     * @param labelsVisible the labelsVisible to set
     */
    public void setLabelsVisible(boolean labelsVisible) {
        this.labelsVisible = labelsVisible;
    }


    /**
     * @return the boxVisible
     */
    public boolean isBoxVisible() {
        return boxVisible;
    }


    /**
     * @param boxVisible the boxVisible to set
     */
    public void setBoxVisible(boolean boxVisible) {
        this.boxVisible = boxVisible;
    }


    /**
     * @return the ticksVisible
     */
    public boolean isTicksVisible() {
        return ticksVisible;
    }


    /**
     * @param ticksVisible the ticksVisible to set
     */
    public void setTicksVisible(boolean ticksVisible) {
        this.ticksVisible = ticksVisible;
    }


    /**
     * @return the ticks
     */
    public boolean isTicks() {
        return ticks;
    }


    /**
     * @param ticks the ticks to set
     */
    public void setTicks(boolean ticks) {
        this.ticks = ticks;
    }


    /**
     * @return the divisions
     */
    public int getDivisions() {
        return divisions;
    }


    /**
     * @param divisions the divisions to set
     */
    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }


    /**
     * @return the subDivisions
     */
    public int getSubDivisions() {
        return subDivisions;
    }


    /**
     * @param subDivisions the subDivisions to set
     */
    public void setSubDivisions(int subDivisions) {
        this.subDivisions = subDivisions;
    }


    /**
     * @return the clipBox
     */
    public Box3D getClipBox() {
        return clipBox;
    }


    /**
     * @param clipBox the clipBox to set
     */
    public void setClipBox(Box3D clipBox) {
        this.clipBox = clipBox;
    }

    /**
     * @return the fov
     */
    public double getFov() {
        return fov;
    }

    /**
     * @param fov the fov to set
     */
    public void setFov(double fov) {
        this.fov = fov;
    }

    public Preferences setExtra(ExtraPrefs extra) {
        this.extra = extra;
        return this;
    }
}
