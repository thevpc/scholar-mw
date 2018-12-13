/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.MinMax;
//import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.VectorXYDataset;
//import org.jfree.ui.RectangleInsets;

import java.awt.*;

/**
 * @author vpc
 */
public class PlotCanvasVectorJFreeChart extends PlotCanvasAnyComplexJFreeChart {

    public PlotCanvasVectorJFreeChart(PlotModelProvider plotModelProvider) {
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
        XYPlot categoryPlot = chart.getXYPlot();
//        categoryPlot.setRenderer(0,prepareXYRenderer());
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
        VectorXYDataset data = createVectorXYDataset();
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        String theTitle = model.getPreferredTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
//        return ChartFactory.createAreaChart(theTitle, theXTitle, theYTitle,
//                data,
//                PlotOrientation.VERTICAL,
//                legend == null ? true : legend,
//                tooltips == null ? true : tooltips,
//                false);


        NumberAxis localNumberAxis1 = new NumberAxis(theXTitle);
        localNumberAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        localNumberAxis1.setLowerMargin(0.01D);
        localNumberAxis1.setUpperMargin(0.01D);
        localNumberAxis1.setAutoRangeIncludesZero(false);
        NumberAxis localNumberAxis2 = new NumberAxis(theYTitle);
        localNumberAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        localNumberAxis2.setLowerMargin(0.01D);
        localNumberAxis2.setUpperMargin(0.01D);
        localNumberAxis2.setAutoRangeIncludesZero(false);
        VectorRenderer localVectorRenderer = new VectorRenderer();
        localVectorRenderer.setSeriesPaint(0, Color.blue);
//        localVectorRenderer.setBaseToolTipGenerator(new VectorToolTipGenerator());
        XYPlot localXYPlot = new XYPlot(data, localNumberAxis1, localNumberAxis2, localVectorRenderer);
        localXYPlot.setBackgroundPaint(Color.lightGray);
        localXYPlot.setDomainGridlinePaint(Color.white);
        localXYPlot.setRangeGridlinePaint(Color.white);
        localXYPlot.setAxisOffset(new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D));
        localXYPlot.setOutlinePaint(Color.black);
        JFreeChart localJFreeChart = new JFreeChart(theTitle, localXYPlot);
        ChartUtils.applyCurrentTheme(localJFreeChart);
        return localJFreeChart;

    }

}
