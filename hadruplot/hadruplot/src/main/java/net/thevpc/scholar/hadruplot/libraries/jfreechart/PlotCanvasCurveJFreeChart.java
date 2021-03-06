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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * @author vpc
 */
public class PlotCanvasCurveJFreeChart extends PlotCanvasAnyDoubleJFreeChart {

    public PlotCanvasCurveJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
        init();
    }

//    protected CategoryDataset createCategoryDataset() {
//        //check if swappable
//        boolean swappable = data.size() > 1;
//        if (swappable) {
//            for (int i = 0; i < data.size(); i++) {
//                double[] x = data.getX(i);
//                double[] y = data.getY(i);
//                if (y.length != 1) {
//                    swappable = false;
//                    break;
//                }
//            }
//        }
//        if (swappable) {
//            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            for (int i = 0; i < data.size(); i++) {
//                String key = data.getYTitle(i);
//                double[] x = data.getX(i);
//                double[] y = data.getY(i);
//                dataset.addValue((Number) (y[0]), key, x[i]);
//            }
//            return dataset;
//        }
//
//        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        for (int i = 0; i < data.size(); i++) {
//            String key = data.getYTitle(i);
//            double[] x = data.getX(i);
//            double[] y = data.getY(i);
//            for (int k = 0; k < y.length; k++) {
//                dataset.addValue((Number) (y[k]), key, x[k]);
//            }
//        }
//        return dataset;
//    }


//    protected double[] createDefaultXX(){
//        return Maths.dtimes(1.0, yAxis[0].length, yAxis[0].length);
//    }

    @Override
    protected double getDefaultXMultiplier() {
        return 1;
    }


    @Override
    protected void prepareJFreeChart(JFreeChart chart, MinMax x_minmax) {
        final ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        if (chart.getPlot() instanceof XYPlot) {
            XYPlot localXYPlot = chart.getXYPlot();
            localXYPlot.setRenderer(0, prepareXYRenderer());
            localXYPlot.setBackgroundPaint(Color.WHITE);
            localXYPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            localXYPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            localXYPlot.setDomainCrosshairVisible(true);
            localXYPlot.setRangeCrosshairVisible(true);

            XYPlot plot = (XYPlot) chart.getPlot();
            NumberAxis axis = (NumberAxis) plot.getDomainAxis();
            if (!x_minmax.isNaN() && x_minmax.getLength() > 0) {
                axis.setRange(x_minmax.getMin(), x_minmax.getMax());
            }
            if (model.getXformat() != null) {
                axis.setNumberFormatOverride(new NumberFormat() {
                    @Override
                    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                        return toAppendTo.append(model.getXformat().formatDouble(number));
                    }

                    @Override
                    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                        return toAppendTo.append(model.getXformat().formatDouble(number));
                    }

                    @Override
                    public Number parse(String source, ParsePosition parsePosition) {
                        throw new IllegalArgumentException("Unsupported");
                    }
                });
            }
            if (model.getYformat() != null) {
                NumberAxis yaxis = (NumberAxis) plot.getRangeAxis();
                yaxis.setNumberFormatOverride(new NumberFormat() {
                    @Override
                    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                        return toAppendTo.append(model.getYformat().formatDouble(number));
                    }

                    @Override
                    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                        return toAppendTo.append(model.getYformat().formatDouble(number));
                    }

                    @Override
                    public Number parse(String source, ParsePosition parsePosition) {
                        throw new IllegalArgumentException("Unsupported");
                    }
                });
            }
        } else if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setRenderer(0, prepareAreaRenderer());
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            categoryPlot.setDomainCrosshairVisible(true);
            categoryPlot.setRangeCrosshairVisible(true);
        }
        //        plot.getRenderer().

    }

    @Override
    protected JFreeChart createChart(String theYTitle, Boolean legend, Boolean tooltips) {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        String theTitle = model.getPreferredTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
//        if(config.threeD){
//            CategoryDataset data= createCategoryDataset();
//            return ChartFactory.createLineChart3D(theTitle, theXTitle, theYTitle,
//                    data,
//                    PlotOrientation.VERTICAL,
//                    legend == null ? true : legend,
//                    tooltips == null ? true : tooltips,
//                    false);
//        }
        XYDataset data = createXYDataset();
        return ChartFactory.createXYLineChart(theTitle, theXTitle, theYTitle,
                data,
                PlotOrientation.VERTICAL,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
    }

}
