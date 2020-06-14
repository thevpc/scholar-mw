/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.libraries.jfreechart;

import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadruplot.extension.PlotModelProvider;
import net.vpc.scholar.hadruplot.model.ValuesPlotModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;

/**
 * @author vpc
 */
public class PlotCanvasAreaJFreeChart extends PlotCanvasAnyDoubleJFreeChart {

    public PlotCanvasAreaJFreeChart(PlotModelProvider plotModelProvider) {
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
//        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setRenderer(0, prepareAreaRenderer());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        categoryPlot.setDomainCrosshairVisible(true);
        categoryPlot.setRangeCrosshairVisible(true);
//
//        XYPlot plot = (XYPlot) chart.getPlot();
//        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
//        if (!x_minmax.isNaN() && x_minmax.getLength() > 0) {
//            axis.setRange(x_minmax.getMin(), x_minmax.getMax());
//        }
//        if (model.getXformat() != null) {
//            axis.setNumberFormatOverride(new NumberFormat() {
//                @Override
//                public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
//                    return toAppendTo.append(model.getXformat().formatDouble(number));
//                }
//
//                @Override
//                public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
//                    return toAppendTo.append(model.getXformat().formatDouble(number));
//                }
//
//                @Override
//                public Number parse(String source, ParsePosition parsePosition) {
//                    throw new IllegalArgumentException("Unsupported");
//                }
//            });
//        }
//        if (model.getYformat() != null) {
//            NumberAxis yaxis = (NumberAxis) plot.getRangeAxis();
//            yaxis.setNumberFormatOverride(new NumberFormat() {
//                @Override
//                public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
//                    return toAppendTo.append(model.getYformat().formatDouble(number));
//                }
//
//                @Override
//                public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
//                    return toAppendTo.append(model.getYformat().formatDouble(number));
//                }
//
//                @Override
//                public Number parse(String source, ParsePosition parsePosition) {
//                    throw new IllegalArgumentException("Unsupported");
//                }
//            });
//        }
        //        plot.getRenderer().

    }

    protected JFreeChart createChart(String theYTitle, Boolean legend, Boolean tooltips) {
        CategoryDataset data = createCategoryDataset();
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        String theTitle = model.getPreferredTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
        return ChartFactory.createAreaChart(theTitle, theXTitle, theYTitle,
                data,
                PlotOrientation.VERTICAL,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
    }

}
