/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MinMax;
import net.vpc.scholar.hadrumaths.Plot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashSet;

/**
 * @author vpc
 */
public class PlotCanvasCurveJFreeChart extends JPanel implements PlotComponentPanel{
    private PlotModelProvider plotModelProvider;
    private ChartPanel chartPanel;
    public PlotCanvasCurveJFreeChart(PlotModelProvider plotModelProvider) {
        super(new BorderLayout());
        this.plotModelProvider = plotModelProvider;
        ValuesPlotModel model = plotModelProvider.getModel();
        String theTitle = model.getTitle() == null ? "" : model.getTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
        String theYTitle = model.getYtitle() != null ? model.getYtitle() : model.getZtitle() != null ? model.getZtitle() : "Y" ;
        String[] ytitles = model.getYtitles();
        double[][] xAxis = model.getX();
        double[][] yAxis = model.getZd();
        if (xAxis == null || xAxis.length == 0) {
            if (yAxis == null || yAxis.length == 0) {
                xAxis = new double[0][];
                yAxis = new double[0][];
            } else {
                double[] xx = Maths.dtimes(1.0, yAxis[0].length, yAxis[0].length);
                xAxis = new double[yAxis.length][];
                for (int i = 0; i < yAxis.length; i++) {
                    xAxis[i] = xx;
                }
            }
        }


        XYSeriesCollection data = new XYSeriesCollection();
        //        if (model.getZ() != null) {
//            throw new IllegalArgumentException();
//        }
        HashSet<String> keys = new HashSet<String>();
        MinMax x_minmax = new MinMax();
        MinMax y_minmax = new MinMax();
        //        double[][] zValues=getZ();
        for (int i = 0; i < yAxis.length; i++) {
            if (!model.getYVisible(i)) {
                continue;
            }
            double xmultiplier = ((Number) model.getProperty(i, "xmultiplier", 1.0)).doubleValue();
            double ymultiplier = ((Number) model.getProperty(i, "ymultiplier", 1.0)).doubleValue();
            String key = (ytitles == null || ytitles.length == 0)
                    ? ((model.getZtitle() == null || model.getZtitle().length() == 0) ? "Y" : model.getZtitle())
                    : !(i < ytitles.length) ? ytitles[ytitles.length - 1] : ytitles[i];
            int keyIndex = 1;
            while (true) {
                String kk = key + ((keyIndex == 1) ? "" : keyIndex);
                if (!keys.contains(kk)) {
                    keys.add(kk);
                    key = kk;
                    break;
                }
                keys.add(kk);
                keyIndex++;
            }
            XYSeries series = new XYSeries(key);
            for (int k = 0; k < yAxis[i].length; k++) {
                double yf = yAxis[i][k];
                //                if (Double.isNaN(yf)) {
//                    yf = 0;
//                }
                double[] xarr = xAxis.length <= i ? xAxis[xAxis.length - 1] : xAxis[i];
                double xf = xarr == null ? k : xarr[k];
                //                if (Double.isNaN(xf)) {
//                    xf = 0;
//                }
                x_minmax.registerValue(xf * xmultiplier);
                y_minmax.registerValue(yf * ymultiplier);
                series.add(xf * xmultiplier, yf * ymultiplier);
            }
            data.addSeries(series);
        }
        Boolean legend = (Boolean) model.getProperty("showLegend", true);
        if (yAxis.length > 20 && legend) {
            legend = false;
        }
        Boolean tooltips = (Boolean) model.getProperty("showTooltips", true);
        Boolean alternateColor = ((Boolean) model.getProperty("alternateColor", true));
        Boolean alternateNode = ((Boolean) model.getProperty("alternateNode", false));
        Boolean alternateLine = ((Boolean) model.getProperty("alternateLine", false));
//        alternateNode=true;
        JFreeChart chart = ChartFactory.createXYLineChart(theTitle, theXTitle, theYTitle,
                data,
                PlotOrientation.VERTICAL,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
        //Object o=chart.getXYPlot().getRenderer(0);
        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer();
        int jfreeChartIndex = 0;
        for (int i = 0; i < yAxis.length; i++) {
            if (ytitles != null && ytitles.length > i) {
                if (!model.getYVisible(i)) {
                    continue;
                }
            }
            int nodeType = (Integer) model.getProperty(i, "nodeType", 0) % 6;
            if (nodeType <= 0) {
                nodeType = ((Integer) model.getProperty("defaultNodeType", 0)) % 6;
            }
            int lineType = ((Integer) model.getProperty(i, "lineType", 0)) % 6;
            if (lineType <= 0) {
                lineType = ((Integer) model.getProperty("defaultLineType", 0)) % 6;
            }
            if (!alternateColor) {
                r.setSeriesPaint(jfreeChartIndex, Color.BLACK);
            }
            if (alternateNode) {
                nodeType = 2;
            }
            if (alternateLine) {
                lineType = i;
            }
            switch (nodeType) {
                case 0: {
                    //leave defaults
                    r.setSeriesShapesVisible(jfreeChartIndex, false);
                    break;
                }
                case 1: {
                    r.setSeriesShapesVisible(jfreeChartIndex, false);
                    break;
                }
                case 2: {
                    r.setSeriesShapesVisible(jfreeChartIndex, true);
                    r.setSeriesShapesFilled(jfreeChartIndex, true);
                    break;
                }
                case 3: {
                    r.setSeriesShapesVisible(jfreeChartIndex, true);
                    r.setSeriesShapesFilled(jfreeChartIndex, false);
                    break;
                }
//                    case 4:{
//                        r.setSeriesShapesVisible(jfreeChartIndex, true);
//                        r.setSeriesShapesFilled(jfreeChartIndex, false);
//                        r.setSeriesShape(jfreeChartIndex, ShapeUtilities.createDiamond(4F));
//                        break;
//                    }
//                    case 5:{
//                        r.setSeriesShapesVisible(jfreeChartIndex, true);
//                        r.setSeriesShapesFilled(jfreeChartIndex, true);
//                        r.setSeriesShape(jfreeChartIndex, ShapeUtilities.createDiamond(4F));
//                        break;
//                    }
                default: {
                    r.setSeriesShapesVisible(jfreeChartIndex, true);
                    r.setSeriesShapesFilled(jfreeChartIndex, false);
                    break;
                }
            }

            switch (lineType) {
                case 0: {
                    //leave defaults
                    break;
                }
                case 1: {
                    //visible line
                    r.setSeriesLinesVisible(jfreeChartIndex, true);
                    break;
                }
                case 2: {
                    //visible line
                    r.setSeriesLinesVisible(jfreeChartIndex, false);
                    break;
                }
                case 3: {
                    //visible line
                    r.setSeriesLinesVisible(jfreeChartIndex, true);
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{10F, 6F}, 0.0F));
                    break;
                }
                case 4: {
                    //visible line
                    r.setSeriesLinesVisible(jfreeChartIndex, true);
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
                    break;
                }
                case 5: {
                    //visible line
                    r.setSeriesLinesVisible(jfreeChartIndex, true);
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{2.0F, 6F}, 0.0F));
                    break;
                }
            }

            jfreeChartIndex++;
        }
        chart.getXYPlot().setRenderer(0, r);
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
        if (!x_minmax.isNaN() && x_minmax.getLength()>0) {
            axis.setRange(x_minmax.getMin(), x_minmax.getMax());
        }
        if(model.getXformat()!=null) {
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
        if(model.getYformat()!=null) {
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
        //        plot.getRenderer().
        chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        add(chartPanel);
        //Plot.buildJPopupMenu(chartPanel, plotModelProvider);
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return chartPanel.getPopupMenu();
    }
}
