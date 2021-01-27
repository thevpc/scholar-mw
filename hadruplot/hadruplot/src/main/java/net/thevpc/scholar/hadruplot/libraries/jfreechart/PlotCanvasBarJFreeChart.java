/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.libraries.jfreechart;

import net.thevpc.common.util.MinMax;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;

/**
 * @author vpc
 */
public class PlotCanvasBarJFreeChart extends PlotCanvasAnyDoubleJFreeChart {

    public PlotCanvasBarJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
        init();
    }

//    protected double[] createDefaultXX(){
//        return Maths.dtimes(1.0, yAxis[0].length, yAxis[0].length);
//    }

    protected double getDefaultXMultiplier() {
        return 1;
    }


    protected void prepareJFreeChart(JFreeChart chart, MinMax x_minmax) {
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.LIGHT_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
    }

    protected JFreeChart createChart(String theYTitle, Boolean legend, Boolean tooltips) {
        CategoryDataset data = createCategoryDataset();
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        String theTitle = model.getPreferredTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
        if (config.threeD.get()) {
//            return ChartFactory.createBarChart3D(theTitle, theXTitle, theYTitle,
//                    data,
//                    PlotOrientation.VERTICAL,
//                    legend == null ? true : legend,
//                    tooltips == null ? true : tooltips,
//                    false);
        }
        JFreeChart barChart = ChartFactory.createBarChart(theTitle, theXTitle, theYTitle,
                data,
                PlotOrientation.VERTICAL,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        AxisSpace space = new AxisSpace();
        space.setTop(300);
        space.setBottom(0);
//        plot.setFixedRangeAxisSpace(space);
        return barChart;
    }
}
