package net.vpc.scholar.hadruplot.libraries.calc3d.core;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Point3D;

import java.awt.*;

public class Globalsettings {

    public double minX = -5;
    public double maxX = 5;
    public double minY = -5;
    public double maxY = 5;
    public double minZ = -5;
    public double maxZ = 5;

    public Box3D mappedClipBox = new Box3D(-1, 1, -1, 1, -1, 1);

    public Object otherSettings;
    /*
     * Gui Settings
     */
    public ExtraPrefs extra;
    /*
     * Renderer Settings
     */
    /**
     * True if axis are shown
     */
    public boolean axisVisible = true;
    public boolean gridXYVisible = true;
    public boolean antiAliasingEnabled = false;
    public boolean perspectiveEnabled = true;
    public boolean steroscopyEnabled = false;
    public int steroscopicMode = 2;
    public boolean fogEnabled = true;
    public boolean light1Enabled = true;
    public boolean light2Enabled = false;
    public boolean light3Enabled = false;
    public Paint backgroundColor = Color.black;//new Color(230,230,240);//new Color(0,35,7);

    /*
     * bOX sETTINGS
     */
    public boolean boxVisible = true;
    public boolean gridsVisible = false;
    public boolean planesVisible = false;
    public boolean labelsVisible = true;
    public boolean rulersVisible = true;
    public boolean ticksVisible = true;
    public boolean showMajorGrids, showMinorGrids;
    public double divisions = 5;
    public double subdivisions = 3;

    /*
     * Axis Settings
     */
    public boolean xAxisVisible = true;
    public boolean yAxisVisible = true;
    public boolean zAxisVisible = true;
    public boolean xyGridVisible = false;
    public Box3D axesBox = new Box3D(-5, 5, -5, 5, -5, 5);
    public int axisTicks = 5;
    public int axisWidth = 2;
    public Color axisColor = Color.white;//new Color(132,145,135).brighter();
    public Color planeColor = Color.white;//new Color(190,240,220);
    public Color gridColor = planeColor.darker();

    public double fov = 50;

    /**
     * maps clipcoordiantes to value between minx and maxX
     */
    public double mapCliptoX(double x) {
        return minX + (maxX - minX) * (x - mappedClipBox.getMinX()) / (mappedClipBox.getWidth());
    }

    /**
     * maps clipcoordiantes to value between minx and maxX
     */
    public double mapCliptoY(double y) {
        return minY + (maxY - minY) * (y - mappedClipBox.getMinY()) / (mappedClipBox.getHeight());
    }

    /**
     * maps clipcoordiantes to value between minx and maxX
     */
    public double mapCliptoZ(double z) {
        return minZ + (maxZ - minZ) * (z - mappedClipBox.getMinZ()) / (mappedClipBox.getDepth());
    }

    /**
     * maps x (value between -1 and 1) to value between minx and maxX
     */
    public double mapX(double x) {
        double p = (x - mappedClipBox.getMinX()) / (mappedClipBox.getWidth());
        if (p > 1) {
            return Double.POSITIVE_INFINITY;
        }
        if (p < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return minX + (maxX - minX) * p;
    }

    /**
     * maps y (value between -1 and 1) to value between minx and maxX
     */
    public double mapY(double y) {
        double p = (y - mappedClipBox.getMinY()) / (mappedClipBox.getHeight());
        if (p > 1) {
            return Double.POSITIVE_INFINITY;
        }
        if (p < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return minY + (maxY - minY) * p;
    }

    /**
     * maps z (value between -1 and 1) to value between minx and maxX
     */
    public double mapZ(double z) {
        double p = (z - mappedClipBox.getMinZ()) / (mappedClipBox.getDepth());
        if (p > 1) {
            return Double.POSITIVE_INFINITY;
        }
        if (p < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return minZ + (maxZ - minZ) * p;
    }

    /**
     * maps z(value between minz to max z) to value between -1 to 1
     */
    public double inverseMapZ(double z) {
        if (z == Double.POSITIVE_INFINITY) {
            return mappedClipBox.getMinZ() + mappedClipBox.getDepth() + 1;
        }
        if (z == Double.NEGATIVE_INFINITY) {
            return mappedClipBox.getMinZ() - 1;
        }
        return mappedClipBox.getMinZ() + mappedClipBox.getDepth() * (z - minZ) / (maxZ - minZ);
    }

    /**
     * maps y(value between minz to max z) to value between -1 to 1
     */
    public double inverseMapY(double y) {
        if (y == Double.POSITIVE_INFINITY) {
            return mappedClipBox.getMinY() + mappedClipBox.getHeight() + 1;
        }
        if (y == Double.NEGATIVE_INFINITY) {
            return mappedClipBox.getMinY() - 1;
        }
        return mappedClipBox.getMinY() + mappedClipBox.getHeight() * (y - minY) / (maxY - minY);
    }

    /**
     * maps x(value between minz to max z) to value between -1 to 1
     */
    public double inverseMapX(double x) {
        if (x == Double.POSITIVE_INFINITY) {
            return mappedClipBox.getMinX() + mappedClipBox.getWidth() + 1;
        }
        if (x == Double.NEGATIVE_INFINITY) {
            return mappedClipBox.getMinX() - 1;
        }
        return mappedClipBox.getMinX() + mappedClipBox.getWidth() * (x - minX) / (maxX - minX);
    }

    public Vector3D inverseMap(Vector3D v) {
        return new Vector3D(inverseMapX(v.getX()), inverseMapY(v.getY()), inverseMapZ(v.getZ()));
    }

    public Point3D inverseMap(Point3D v) {
        return new Point3D(inverseMapX(v.getX()), inverseMapY(v.getY()), inverseMapZ(v.getZ()));
    }

    public Vector3D[] inverseMapAll(Vector3D... v) {
        Vector3D[] a = new Vector3D[v.length];
        for (int i = 0; i < v.length; i++) {
            a[i] = inverseMap(v[i]);
        }
        return a;
    }

    public Point3D[] inverseMapAll(Point3D... v) {
        Point3D[] a = new Point3D[v.length];
        for (int i = 0; i < v.length; i++) {
            a[i] = inverseMap(v[i]);
        }
        return a;
    }

    public Vector3D[] mapAll(Vector3D... v) {
        Vector3D[] a = new Vector3D[v.length];
        for (int i = 0; i < v.length; i++) {
            a[i] = map(v[i]);
        }
        return a;
    }

    public Point3D[] mapAll(Point3D... v) {
        Point3D[] a = new Point3D[v.length];
        for (int i = 0; i < v.length; i++) {
            a[i] = map(v[i]);
        }
        return a;
    }

    public Vector3D map(Vector3D v) {
        return new Vector3D(mapX(v.getX()), mapY(v.getY()), mapZ(v.getZ()));
    }

    public Point3D map(Point3D v) {
        return new Point3D(mapX(v.getX()), mapY(v.getY()), mapZ(v.getZ()));
    }

    public void saveSettings(Preferences preferences) {
        minX = preferences.getClipBox().getMinX();
        maxX = preferences.getClipBox().getMaxX();
        minY = preferences.getClipBox().getMinY();
        maxY = preferences.getClipBox().getMaxY();
        minZ = preferences.getClipBox().getMinZ();
        maxZ = preferences.getClipBox().getMaxZ();

        axisVisible = preferences.isAxisVisible();
        gridXYVisible = preferences.isGridXYVisible();
        divisions = preferences.getDivisions();
        subdivisions = preferences.getSubDivisions();
        showMajorGrids = preferences.isGridsVisible();
        showMinorGrids = preferences.isGridsVisible();
        backgroundColor = preferences.getBackColor();

        boxVisible = preferences.isBoxVisible();
        gridsVisible = preferences.isGridsVisible();
        planesVisible = preferences.isPlanesVisible();
        labelsVisible = preferences.isLabelsVisible();
        rulersVisible = preferences.isTicksVisible();
        ticksVisible = preferences.isTicksVisible();
        xAxisVisible = preferences.isxAxisVisible();
        yAxisVisible = preferences.isyAxisVisible();
        zAxisVisible = preferences.iszAxisVisible();
        xyGridVisible = preferences.isXyGridVisible();
        axesBox = preferences.getAxesBox();
        axisTicks = preferences.getAxisTicks();
        axisWidth = preferences.getAxisWidth();
        axisColor = preferences.getAxisColor();

        extra = preferences.getExtra() == null ? null : preferences.getExtra().copy();
//        fileToolbarVisible = preferences.isFileToolbarVisible();
//        objectToolbarVisible = preferences.isObjectToolbarVisible();
//        statusbarVisible = preferences.isStatusbarVisible();
//        tipofDayEnabled = preferences.isTipofDayEnabled();
//        splashScreenEnabled = preferences.isSplashScreenEnabled();

        antiAliasingEnabled = preferences.isAntiAliasingEnabled();
        perspectiveEnabled = preferences.isPerspectiveEnabled();
        steroscopyEnabled = preferences.isSteroscopyEnabled();
        steroscopicMode = preferences.getSteroscopicMode();
        backgroundColor = preferences.getBackColor();
        fogEnabled = preferences.isFogEnabled();
        light1Enabled = preferences.isLight1Enabled();
        light2Enabled = preferences.isLight2Enabled();
        light3Enabled = preferences.isLight3Enabled();
        recalculateClip();
    }

    public void recalculateClip() {
        double max = 1;
        max = Math.max(Math.abs(minX), Math.abs(maxX));
        max = Math.max(max, Math.abs(minY));
        max = Math.max(max, Math.abs(maxY));
        max = Math.max(max, Math.abs(minZ));
        max = Math.max(max, Math.abs(maxZ));
        mappedClipBox = new Box3D(minX / max, maxX / max, minY / max, maxY / max, minZ / max, maxZ / max);
    }

    public ExtraPrefs getExtra() {
        return extra;
    }

    public Preferences getPreferences() {
        Preferences preferences = new Preferences();
        preferences.setExtra(extra == null ? null : extra.copy());
//        preferences.setLookandFeel(lookandFeel);
//        preferences.setLookandFeel(lookandFeel);
//
//        preferences.setFileToolbarVisible(fileToolbarVisible);
//        preferences.setObjectToolbarVisible(objectToolbarVisible);
//        preferences.setStatusbarVisible(statusbarVisible);
//        preferences.setTipofDayEnabled(tipofDayEnabled);
//        preferences.setSplashScreenEnabled(splashScreenEnabled);

        preferences.setClipBox(new Box3D(minX, maxX, minY, maxY, minZ, maxZ));

        preferences.setAntiAliasingEnabled(antiAliasingEnabled);
        preferences.setPerspectiveEnabled(perspectiveEnabled);
        preferences.setSteroscopyEnabled(steroscopyEnabled);
        preferences.setSteroscopicMode(steroscopicMode);

        preferences.setFogEnabled(fogEnabled);
        preferences.setLight1Enabled(light1Enabled);
        preferences.setLight2Enabled(light2Enabled);
        preferences.setLight3Enabled(light3Enabled);

        preferences.setxAxisVisible(xAxisVisible);
        preferences.setyAxisVisible(yAxisVisible);
        preferences.setzAxisVisible(zAxisVisible);

        preferences.setAxesBox(axesBox);
        preferences.setAxisColor(axisColor);
        preferences.setAxisWidth(axisWidth);
        preferences.setAxisTicks(axisTicks);
        preferences.setBackColor(backgroundColor);

        preferences.setGridsVisible(gridsVisible);
        preferences.setLabelsVisible(labelsVisible);
        preferences.setBoxVisible(boxVisible);
        preferences.setPlanesVisible(planesVisible);
        preferences.setTicksVisible(ticksVisible);
        preferences.setDivisions((int) divisions);
        preferences.setSubDivisions((int) subdivisions);

        preferences.setAxisVisible(axisVisible);
        preferences.setGridXYVisible(gridXYVisible);
        return preferences;
    }

    /**
     * @return the clipBox
     */
    public Box3D getClipBox() {
        return new Box3D(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public void setExtra(ExtraPrefs s) {
        this.extra = s;
    }

}
