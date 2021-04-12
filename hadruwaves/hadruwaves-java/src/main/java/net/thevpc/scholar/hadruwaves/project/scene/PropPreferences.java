/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project.scene;

import java.awt.Color;
import java.awt.Paint;
import net.thevpc.common.props.PropertyListeners;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WithListeners;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.impl.PropertyListenersImpl;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.ExtraPrefs;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Preferences;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;

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
    private WritableValue<Box3D> clipBox = Props.of("clipBox").valueOf(Box3D.class, null);

    /*
     * Renderer Settings
     */
    private WritableValue<Boolean> antiAliasingEnabled = Props.of("antiAliasingEnabled").valueOf(Boolean.class, false);
    private WritableValue<Boolean> perspectiveEnabled = Props.of("perspectiveEnabled").valueOf(Boolean.class, false);
    private WritableValue<Boolean> steroscopyEnabled = Props.of("steroscopyEnabled").valueOf(Boolean.class, false);
    private WritableValue<Integer> steroscopicMode = Props.of("steroscopicMode").valueOf(Integer.class, 0);
    private WritableValue<Paint> backColor = Props.of("backColor").valueOf(Paint.class, Color.red);
    private WritableValue<Boolean> fogEnabled = Props.of("fogEnabled").valueOf(Boolean.class, false);
    private WritableValue<Boolean> light1Enabled = Props.of("light1Enabled").valueOf(Boolean.class, false);
    private WritableValue<Boolean> light2Enabled = Props.of("light2Enabled").valueOf(Boolean.class, false);
    private WritableValue<Boolean> light3Enabled = Props.of("light3Enabled").valueOf(Boolean.class, false);

    /*
     * Axis Settings
     */
    private WritableValue<Boolean> xAxisVisible = Props.of("xAxisVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> yAxisVisible = Props.of("yAxisVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> zAxisVisible = Props.of("zAxisVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> xyGridVisible = Props.of("xyGridVisible").valueOf(Boolean.class, false);
    private WritableValue<Box3D> axesBox = Props.of("axesBox").valueOf(Box3D.class, null);
    private WritableValue<Integer> axisTicks = Props.of("axisTicks").valueOf(Integer.class, 0);
    private WritableValue<Integer> axisWidth = Props.of("axisWidth").valueOf(Integer.class, 0);
    private WritableValue<Color> axisColor = Props.of("axisColor").valueOf(Color.class, Color.black);
    /*
     * Box Settings
     */
    private WritableValue<Boolean> gridsVisible = Props.of("gridsVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> planesVisible = Props.of("planesVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> labelsVisible = Props.of("labelsVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> boxVisible = Props.of("boxVisible").valueOf(Boolean.class, false);
    private WritableValue<Boolean> ticksVisible = Props.of("ticksVisible").valueOf(Boolean.class, false);

    private WritableValue<Boolean> ticks = Props.of("ticks").valueOf(Boolean.class, false);
    private WritableValue<Integer> divisions = Props.of("divisions").valueOf(Integer.class, 0);
    private WritableValue<Integer> subDivisions = Props.of("subDivisions").valueOf(Integer.class, 0);

    private WritableValue<Double> fov = Props.of("fov").valueOf(Double.class, 0.0);
    private WritableValue<Boolean> axisVisible = Props.of("axisVisible").valueOf(Boolean.class, true);
    private WritableValue<Boolean> gridXYVisible = Props.of("gridXYVisible").valueOf(Boolean.class, true);

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

    public WritableValue<Box3D> clipBox() {
        return clipBox;
    }

    public WritableValue<Boolean> antiAliasingEnabled() {
        return antiAliasingEnabled;
    }

    public WritableValue<Boolean> perspectiveEnabled() {
        return perspectiveEnabled;
    }

    public WritableValue<Boolean> steroscopyEnabled() {
        return steroscopyEnabled;
    }

    public WritableValue<Integer> steroscopicMode() {
        return steroscopicMode;
    }

    public WritableValue<Paint> backColor() {
        return backColor;
    }

    public WritableValue<Boolean> fogEnabled() {
        return fogEnabled;
    }

    public WritableValue<Boolean> light1Enabled() {
        return light1Enabled;
    }

    public WritableValue<Boolean> light2Enabled() {
        return light2Enabled;
    }

    public WritableValue<Boolean> light3Enabled() {
        return light3Enabled;
    }

    public WritableValue<Boolean> xAxisVisible() {
        return xAxisVisible;
    }

    public WritableValue<Boolean> yAxisVisible() {
        return yAxisVisible;
    }

    public WritableValue<Boolean> zAxisVisible() {
        return zAxisVisible;
    }

    public WritableValue<Boolean> xyGridVisible() {
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

    public WritableValue<Boolean> gridsVisible() {
        return gridsVisible;
    }

    public WritableValue<Boolean> planesVisible() {
        return planesVisible;
    }

    public WritableValue<Boolean> labelsVisible() {
        return labelsVisible;
    }

    public WritableValue<Boolean> boxVisible() {
        return boxVisible;
    }

    public WritableValue<Boolean> ticksVisible() {
        return ticksVisible;
    }

    public WritableValue<Boolean> ticks() {
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

    public WritableValue<Boolean> axisVisible() {
        return axisVisible;
    }

    public WritableValue<Boolean> gridXYVisible() {
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
