/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryDataset;
//import org.jfree.util.TableOrder;

import java.awt.*;

/**
 * @author vpc
 */
public class PlotCanvasPieJFreeChart extends PlotCanvasAnyDoubleJFreeChart {

    public PlotCanvasPieJFreeChart(PlotModelProvider plotModelProvider) {
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
        if (chart.getPlot() instanceof PiePlot) {
            PiePlot plot = (PiePlot) chart.getPlot();
        } else if (chart.getPlot() instanceof MultiplePiePlot) {
            MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
        }
//        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
//        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        //        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
//        chart.getXYPlot().setRenderer(0,prepareXYRenderer());
//        chart.getPlot().setBackgroundPaint(Color.WHITE);
//        ((XYPlot) chart.getPlot()).setDomainGridlinePaint(Color.LIGHT_GRAY);
//        ((XYPlot) chart.getPlot()).setRangeGridlinePaint(Color.LIGHT_GRAY);
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
        if (config.threeD.get()) {
            return ChartFactory.createMultiplePieChart3D(theTitle,
                    data,
                    TableOrder.BY_ROW,
                    legend == null ? true : legend,
                    tooltips == null ? true : tooltips,
                    false);
        }
        return ChartFactory.createMultiplePieChart(theTitle,
                data, TableOrder.BY_ROW,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
    }

}
