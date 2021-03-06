/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.libraries.jfreechart;

import net.thevpc.common.util.MinMax;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

/**
 * @author vpc
 */
public class PlotCanvasPolarJFreeChart extends PlotCanvasAnyDoubleJFreeChart {

    public PlotCanvasPolarJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
        init();
    }

//    protected double[] createDefaultXX() {
//        return Maths.dtimes(0, 2 * Math.PI, yAxis[0].length);
//    }

    protected double getDefaultXMultiplier() {
        //convert to degree from radian
        return 180 / Math.PI;
    }


    protected void prepareJFreeChart(JFreeChart chart, MinMax x_minmax) {
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        if (plot instanceof PolarPlot) {
            PolarPlot polarPlot = (PolarPlot) plot;
            polarPlot.setRadiusGridlinePaint(Color.LIGHT_GRAY);
        }
    }

    protected JFreeChart createChart(String theTitle, Boolean legend, Boolean tooltips) {
        XYDataset data = createXYDataset();
        JFreeChart chart = ChartFactory.createPolarChart(theTitle,
                data,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
        PlotModel model = plotModelProvider.getModel();
        Number _polarAngleOffset = null;
        Boolean _polarClockwise = null;
        if (model instanceof ValuesPlotModel) {
            ValuesPlotModel vpm = (ValuesPlotModel) model;
            _polarAngleOffset = (Number) vpm.getProperties().get("polarAngleOffset");
            _polarClockwise = (Boolean) vpm.getProperties().get("polarClockwise");
        }
        PolarPlot polarPlot = (PolarPlot) chart.getPlot();
        boolean clockwise = config.clockwise == null ? (_polarClockwise == null ? true : _polarClockwise.booleanValue()) : config.clockwise.get(false);
        double polarAngleOffset = config.polarAngleOffset == null ? (_polarAngleOffset == null ? 0 : _polarAngleOffset.doubleValue()) : config.polarAngleOffset.get(0);
        polarPlot.setCounterClockwise(!clockwise); // changes the direction of the ticks
        polarPlot.setAxisLocation(PolarAxisLocation.EAST_BELOW); // defines the placement of the axis
        polarPlot.setAngleOffset(polarAngleOffset);
        final DefaultPolarItemRenderer renderer = (DefaultPolarItemRenderer) polarPlot.getRenderer();
        //renderer.setSeriesFilled(2, true);
        renderer.setShapesVisible(config.shapesVisible.get(false) || config.alternateNode.get(false));

        return chart;
    }


}
