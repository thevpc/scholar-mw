/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project.scene;

import java.awt.Color;
import java.awt.Paint;
import net.vpc.common.props.PropertyListeners;
import net.vpc.common.props.Props;
import net.vpc.common.props.WithListeners;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.props.impl.PropertyListenersImpl;
import net.vpc.scholar.hadruplot.libraries.calc3d.core.ExtraPrefs;
import net.vpc.scholar.hadruplot.libraries.calc3d.core.Preferences;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;

/**
 *
 * @author vpc
 */
public class PropPreferences implements WithListeners {

    protected PropertyListenersImpl listeners = new PropertyListenersImpl(this);

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    public ExtraPrefs extra;

    /*
     * ClipSettings
     */
    private WritablePValue<Box3D> clipBox = Props.of("clipBox").valueOf(Box3D.class, null);

    /*
     * Renderer Settings
     */
    private WritablePValue<Boolean> antiAliasingEnabled = Props.of("antiAliasingEnabled").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> perspectiveEnabled = Props.of("perspectiveEnabled").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> steroscopyEnabled = Props.of("steroscopyEnabled").valueOf(Boolean.class, false);
    private WritablePValue<Integer> steroscopicMode = Props.of("steroscopicMode").valueOf(Integer.class, 0);
    private WritablePValue<Paint> backColor = Props.of("backColor").valueOf(Paint.class, Color.red);
    private WritablePValue<Boolean> fogEnabled = Props.of("fogEnabled").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> light1Enabled = Props.of("light1Enabled").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> light2Enabled = Props.of("light2Enabled").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> light3Enabled = Props.of("light3Enabled").valueOf(Boolean.class, false);

    /*
     * Axis Settings
     */
    private WritablePValue<Boolean> xAxisVisible = Props.of("xAxisVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> yAxisVisible = Props.of("yAxisVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> zAxisVisible = Props.of("zAxisVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> xyGridVisible = Props.of("xyGridVisible").valueOf(Boolean.class, false);
    private WritablePValue<Box3D> axesBox = Props.of("axesBox").valueOf(Box3D.class, null);
    private WritablePValue<Integer> axisTicks = Props.of("axisTicks").valueOf(Integer.class, 0);
    private WritablePValue<Integer> axisWidth = Props.of("axisWidth").valueOf(Integer.class, 0);
    private WritablePValue<Color> axisColor = Props.of("axisColor").valueOf(Color.class, Color.black);
    /*
     * Box Settings
     */
    private WritablePValue<Boolean> gridsVisible = Props.of("gridsVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> planesVisible = Props.of("planesVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> labelsVisible = Props.of("labelsVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> boxVisible = Props.of("boxVisible").valueOf(Boolean.class, false);
    private WritablePValue<Boolean> ticksVisible = Props.of("ticksVisible").valueOf(Boolean.class, false);

    private WritablePValue<Boolean> ticks = Props.of("ticks").valueOf(Boolean.class, false);
    private WritablePValue<Integer> divisions = Props.of("divisions").valueOf(Integer.class, 0);
    private WritablePValue<Integer> subDivisions = Props.of("subDivisions").valueOf(Integer.class, 0);

    private WritablePValue<Double> fov = Props.of("fov").valueOf(Double.class, 0.0);
    private WritablePValue<Boolean> axisVisible = Props.of("axisVisible").valueOf(Boolean.class, true);
    private WritablePValue<Boolean> gridXYVisible = Props.of("gridXYVisible").valueOf(Boolean.class, true);

    public PropPreferences() {
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

    public WritablePValue<Box3D> clipBox() {
        return clipBox;
    }

    public WritablePValue<Boolean> antiAliasingEnabled() {
        return antiAliasingEnabled;
    }

    public WritablePValue<Boolean> perspectiveEnabled() {
        return perspectiveEnabled;
    }

    public WritablePValue<Boolean> steroscopyEnabled() {
        return steroscopyEnabled;
    }

    public WritablePValue<Integer> steroscopicMode() {
        return steroscopicMode;
    }

    public WritablePValue<Paint> backColor() {
        return backColor;
    }

    public WritablePValue<Boolean> fogEnabled() {
        return fogEnabled;
    }

    public WritablePValue<Boolean> light1Enabled() {
        return light1Enabled;
    }

    public WritablePValue<Boolean> light2Enabled() {
        return light2Enabled;
    }

    public WritablePValue<Boolean> light3Enabled() {
        return light3Enabled;
    }

    public WritablePValue<Boolean> xAxisVisible() {
        return xAxisVisible;
    }

    public WritablePValue<Boolean> yAxisVisible() {
        return yAxisVisible;
    }

    public WritablePValue<Boolean> zAxisVisible() {
        return zAxisVisible;
    }

    public WritablePValue<Boolean> xyGridVisible() {
        return xyGridVisible;
    }

    public WritablePValue<Box3D> axesBox() {
        return axesBox;
    }

    public WritablePValue<Integer> axisTicks() {
        return axisTicks;
    }

    public WritablePValue<Integer> axisWidth() {
        return axisWidth;
    }

    public WritablePValue<Color> axisColor() {
        return axisColor;
    }

    public WritablePValue<Boolean> gridsVisible() {
        return gridsVisible;
    }

    public WritablePValue<Boolean> planesVisible() {
        return planesVisible;
    }

    public WritablePValue<Boolean> labelsVisible() {
        return labelsVisible;
    }

    public WritablePValue<Boolean> boxVisible() {
        return boxVisible;
    }

    public WritablePValue<Boolean> ticksVisible() {
        return ticksVisible;
    }

    public WritablePValue<Boolean> ticks() {
        return ticks;
    }

    public WritablePValue<Integer> divisions() {
        return divisions;
    }

    public WritablePValue<Integer> subDivisions() {
        return subDivisions;
    }

    public WritablePValue<Double> fov() {
        return fov;
    }

    public WritablePValue<Boolean> axisVisible() {
        return axisVisible;
    }

    public WritablePValue<Boolean> gridXYVisible() {
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
