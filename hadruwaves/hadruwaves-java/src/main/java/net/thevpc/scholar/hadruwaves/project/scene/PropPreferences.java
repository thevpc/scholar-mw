/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project.scene;

import java.awt.Color;
import java.awt.Paint;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import net.thevpc.common.props.impl.PropertyBase;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.ExtraPrefs;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Preferences;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;

/**
 *
 * @author vpc
 */
public class PropPreferences extends PropertyBase {

    protected DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    public ExtraPrefs extra;

    /*
     * ClipSettings
     */
    private WritableValue<Box3D> clipBox = Props.of("clipBox").valueOf(Box3D.class, null);

    /*
     * Renderer Settings
     */
    private WritableBoolean antiAliasingEnabled = Props.of("antiAliasingEnabled").booleanOf(false);
    private WritableBoolean perspectiveEnabled = Props.of("perspectiveEnabled").booleanOf(false);
    private WritableBoolean steroscopyEnabled = Props.of("steroscopyEnabled").booleanOf(false);
    private WritableValue<Integer> steroscopicMode = Props.of("steroscopicMode").valueOf(Integer.class, 0);
    private WritableValue<Paint> backColor = Props.of("backColor").valueOf(Paint.class, Color.red);
    private WritableBoolean fogEnabled = Props.of("fogEnabled").booleanOf(false);
    private WritableBoolean light1Enabled = Props.of("light1Enabled").booleanOf(false);
    private WritableBoolean light2Enabled = Props.of("light2Enabled").booleanOf(false);
    private WritableBoolean light3Enabled = Props.of("light3Enabled").booleanOf(false);

    /*
     * Axis Settings
     */
    private WritableBoolean xAxisVisible = Props.of("xAxisVisible").booleanOf(false);
    private WritableBoolean yAxisVisible = Props.of("yAxisVisible").booleanOf(false);
    private WritableBoolean zAxisVisible = Props.of("zAxisVisible").booleanOf(false);
    private WritableBoolean xyGridVisible = Props.of("xyGridVisible").booleanOf(false);
    private WritableValue<Box3D> axesBox = Props.of("axesBox").valueOf(Box3D.class, null);
    private WritableValue<Integer> axisTicks = Props.of("axisTicks").valueOf(Integer.class, 0);
    private WritableValue<Integer> axisWidth = Props.of("axisWidth").valueOf(Integer.class, 0);
    private WritableValue<Color> axisColor = Props.of("axisColor").valueOf(Color.class, Color.black);
    /*
     * Box Settings
     */
    private WritableBoolean gridsVisible = Props.of("gridsVisible").booleanOf(false);
    private WritableBoolean planesVisible = Props.of("planesVisible").booleanOf(false);
    private WritableBoolean labelsVisible = Props.of("labelsVisible").booleanOf(false);
    private WritableBoolean boxVisible = Props.of("boxVisible").booleanOf(false);
    private WritableBoolean ticksVisible = Props.of("ticksVisible").booleanOf(false);

    private WritableBoolean ticks = Props.of("ticks").booleanOf(false);
    private WritableValue<Integer> divisions = Props.of("divisions").valueOf(Integer.class, 0);
    private WritableValue<Integer> subDivisions = Props.of("subDivisions").valueOf(Integer.class, 0);

    private WritableValue<Double> fov = Props.of("fov").valueOf(Double.class, 0.0);
    private WritableBoolean axisVisible = Props.of("axisVisible").booleanOf( true);
    private WritableBoolean gridXYVisible = Props.of("gridXYVisible").booleanOf( true);

    public PropPreferences(String name) {
        super(name);
        listeners.addDelegate(axisVisible);
        listeners.addDelegate(gridXYVisible);
        listeners.addDelegate(clipBox);
        listeners.addDelegate(antiAliasingEnabled);
        listeners.addDelegate(perspectiveEnabled);
        listeners.addDelegate(steroscopyEnabled);
        listeners.addDelegate(steroscopicMode);
        listeners.addDelegate(backColor);
        listeners.addDelegate(fogEnabled);
        listeners.addDelegate(light1Enabled);
        listeners.addDelegate(light2Enabled);
        listeners.addDelegate(light3Enabled);

        /*
     * Axis Settings
         */
        listeners.addDelegate(xAxisVisible);
        listeners.addDelegate(yAxisVisible);
        listeners.addDelegate(zAxisVisible);
        listeners.addDelegate(xyGridVisible);
        listeners.addDelegate(axesBox);
        listeners.addDelegate(axisTicks);
        listeners.addDelegate(axisWidth);
        listeners.addDelegate(axisColor);
        /*
     * Box Settings
         */
        listeners.addDelegate(gridsVisible);
        listeners.addDelegate(planesVisible);
        listeners.addDelegate(labelsVisible);
        listeners.addDelegate(boxVisible);
        listeners.addDelegate(ticksVisible);

        listeners.addDelegate(ticks);
        listeners.addDelegate(divisions);
        listeners.addDelegate(subDivisions);

        listeners.addDelegate(fov);
    }

    public ExtraPrefs extra() {
        return extra;
    }

    public WritableValue<Box3D> clipBox() {
        return clipBox;
    }

    public WritableBoolean antiAliasingEnabled() {
        return antiAliasingEnabled;
    }

    public WritableBoolean perspectiveEnabled() {
        return perspectiveEnabled;
    }

    public WritableBoolean steroscopyEnabled() {
        return steroscopyEnabled;
    }

    public WritableValue<Integer> steroscopicMode() {
        return steroscopicMode;
    }

    public WritableValue<Paint> backColor() {
        return backColor;
    }

    public WritableBoolean fogEnabled() {
        return fogEnabled;
    }

    public WritableBoolean light1Enabled() {
        return light1Enabled;
    }

    public WritableBoolean light2Enabled() {
        return light2Enabled;
    }

    public WritableBoolean light3Enabled() {
        return light3Enabled;
    }

    public WritableBoolean xAxisVisible() {
        return xAxisVisible;
    }

    public WritableBoolean yAxisVisible() {
        return yAxisVisible;
    }

    public WritableBoolean zAxisVisible() {
        return zAxisVisible;
    }

    public WritableBoolean xyGridVisible() {
        return xyGridVisible;
    }

    public WritableValue<Box3D> axesBox() {
        return axesBox;
    }

    public WritableValue<Integer> axisTicks() {
        return axisTicks;
    }

    public WritableValue<Integer> axisWidth() {
        return axisWidth;
    }

    public WritableValue<Color> axisColor() {
        return axisColor;
    }

    public WritableBoolean gridsVisible() {
        return gridsVisible;
    }

    public WritableBoolean planesVisible() {
        return planesVisible;
    }

    public WritableBoolean labelsVisible() {
        return labelsVisible;
    }

    public WritableBoolean boxVisible() {
        return boxVisible;
    }

    public WritableBoolean ticksVisible() {
        return ticksVisible;
    }

    public WritableBoolean ticks() {
        return ticks;
    }

    public WritableValue<Integer> divisions() {
        return divisions;
    }

    public WritableValue<Integer> subDivisions() {
        return subDivisions;
    }

    public WritableValue<Double> fov() {
        return fov;
    }

    public WritableBoolean axisVisible() {
        return axisVisible;
    }

    public WritableBoolean gridXYVisible() {
        return gridXYVisible;
    }

    public Preferences getAll() {
        Preferences p = new Preferences();
        p.setClipBox(clipBox.get());

        /*
     * Renderer Settings
         */
        p.setAntiAliasingEnabled(antiAliasingEnabled.get());
        p.setPerspectiveEnabled(perspectiveEnabled.get());
        p.setSteroscopyEnabled(steroscopyEnabled.get());
        p.setSteroscopicMode(steroscopicMode.get());
        p.setBackColor(backColor.get());
        p.setFogEnabled(fogEnabled.get());
        p.setLight1Enabled(light1Enabled.get());
        p.setLight2Enabled(light2Enabled.get());
        p.setLight3Enabled(light3Enabled.get());

        /*
     * Axis Settings
         */
        p.setxAxisVisible(xAxisVisible.get());
        p.setyAxisVisible(yAxisVisible.get());
        p.setzAxisVisible(zAxisVisible.get());
        p.setXyGridVisible(xyGridVisible.get());
        p.setAxesBox(axesBox.get());
        p.setAxisTicks(axisTicks.get());
        p.setAxisWidth(axisWidth.get());
        p.setAxisColor(axisColor.get());
        /*
     * Box Settings
         */
        p.setGridsVisible(gridsVisible.get());
        p.setPlanesVisible(planesVisible.get());
        p.setLabelsVisible(labelsVisible.get());
        p.setBoxVisible(boxVisible.get());
        p.setTicksVisible(ticksVisible.get());

        p.setTicks(ticks.get());
        p.setDivisions(divisions.get());
        p.setSubDivisions(subDivisions.get());
        p.setAxisVisible(axisVisible.get());
        p.setGridXYVisible(gridXYVisible.get());

        p.setFov(fov.get());
        return p;
    }

    public void setAll(Preferences p) {
        if (p != null) {
            clipBox.set(p.getClipBox());

            /*
     * Renderer Settings
             */
            antiAliasingEnabled.set(p.isAntiAliasingEnabled());
            perspectiveEnabled.set(p.isPerspectiveEnabled());
            steroscopyEnabled.set(p.isSteroscopyEnabled());
            steroscopicMode.set(p.getSteroscopicMode());
            backColor.set(p.getBackColor());
            fogEnabled.set(p.isFogEnabled());
            light1Enabled.set(p.isLight1Enabled());
            light2Enabled.set(p.isLight2Enabled());
            light3Enabled.set(p.isLight3Enabled());

            /*
     * Axis Settings
             */
            xAxisVisible.set(p.isxAxisVisible());
            yAxisVisible.set(p.isyAxisVisible());
            zAxisVisible.set(p.iszAxisVisible());
            xyGridVisible.set(p.isXyGridVisible());
            axesBox.set(p.getAxesBox());
            axisTicks.set(p.getAxisTicks());
            axisWidth.set(p.getAxisWidth());
            axisColor.set(p.getAxisColor());
            /*
     * Box Settings
             */
            gridsVisible.set(p.isGridsVisible());
            planesVisible.set(p.isPlanesVisible());
            labelsVisible.set(p.isLabelsVisible());
            boxVisible.set(p.isBoxVisible());
            ticksVisible.set(p.isTicksVisible());

            ticks.set(p.isTicks());
            divisions.set(p.getDivisions());
            subDivisions.set(p.getSubDivisions());

            axisVisible.set(p.isAxisVisible());
            gridXYVisible.set(p.isGridXYVisible());
            fov.set(p.getFov());

        }
    }

}
