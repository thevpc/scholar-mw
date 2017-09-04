/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
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
        if(plot instanceof PolarPlot) {
            PolarPlot polarPlot=(PolarPlot) plot;
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
        PolarPlot polarPlot = (PolarPlot) chart.getPlot();
        boolean clockwise = config.clockwise == null ? true : config.clockwise;
        double polarAngleOffset = config.polarAngleOffset == null ? 0 : config.polarAngleOffset.doubleValue();
        polarPlot.setCounterClockwise(!clockwise); // changes the direction of the ticks
        polarPlot.setAxisLocation(PolarAxisLocation.EAST_BELOW); // defines the placement of the axis
        polarPlot.setAngleOffset(polarAngleOffset);
        return chart;
    }


}
