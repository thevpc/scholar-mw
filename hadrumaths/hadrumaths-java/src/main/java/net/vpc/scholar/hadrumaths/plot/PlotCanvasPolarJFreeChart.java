/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.Plot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * @author vpc
 */
public class PlotCanvasPolarJFreeChart extends JPanel implements PlotComponentPanel {
    private PlotModelProvider plotModelProvider;
    private ChartPanel chartPanel;
    private JColorPalette paintArray= Maths.DEFAULT_PALETTE ;

    public PlotCanvasPolarJFreeChart(PlotModelProvider plotModelProvider) {
        super(new BorderLayout());
        this.plotModelProvider = plotModelProvider;
        ValuesPlotModel model = plotModelProvider.getModel();
        String theTitle = model.getTitle() == null ? "" : model.getTitle();
//        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
//        String theYTitle = model.getZtitle() == null ? "Y" : model.getZtitle();
        String[] ytitles = model.getYtitles();
        double[][] xAxis = model.getX();
        double[][] yAxis = model.getZd();
        if (xAxis == null || xAxis.length == 0 || (xAxis.length==1 && xAxis[0].length==0)) {
            if (yAxis == null || yAxis.length == 0 || (yAxis.length==1 && yAxis[0].length==0)) {
                xAxis = new double[0][];
                yAxis = new double[0][];
            } else {
                double[] xx = Maths.dtimes(0, 2*Math.PI, yAxis[0].length);
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
        HashSet<String> keys=new HashSet<String>();
        MinMax x_minmax = new MinMax();
        MinMax y_minmax = new MinMax();
        //        double[][] zValues=getZ();
        for (int i = 0; i < yAxis.length; i++) {
            if (!model.getYVisible(i)) {
                continue;
            }
            double xmultiplier = ((Number) model.getProperty(i, "xmultiplier", 1.0)).doubleValue()*180/Math.PI; //convert to degree from radian
            double ymultiplier = ((Number) model.getProperty(i, "ymultiplier", 1.0)).doubleValue();
            String key = (ytitles == null || ytitles.length == 0)
                    ? ((model.getZtitle() == null || model.getZtitle().length() == 0) ? "Y" : model.getZtitle())
                    : !(i < ytitles.length) ? ytitles[ytitles.length - 1] : ytitles[i];
            int keyIndex=1;
            while(true){
                String kk=key+((keyIndex==1)?"":keyIndex);
                if(!keys.contains(kk)){
                    keys.add(kk);
                    key=kk;
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
        Integer maxLegend=(Integer) model.getProperty("maxLegend", Plot.Config.getMaxLegendCount());
        if(maxLegend<0){
            maxLegend= Plot.Config.getMaxLegendCount();
        }

        int visibleCount=0;
        for (int i = 0; i < yAxis.length; i++) {
            boolean v=true;
            if (ytitles != null && ytitles.length > i) {
                if (!model.getYVisible(i)) {
                    v=false;
                }
            }
            if(v){
                visibleCount++;
            }
        }


        if (visibleCount > maxLegend && legend) {
            legend = false;
        }

        Boolean tooltips = (Boolean) model.getProperty("showTooltips", true);
        Boolean alternateColor = ((Boolean) model.getProperty("alternateColor", true));
        Boolean alternateNode = ((Boolean) model.getProperty("alternateNode", false));
        Boolean alternateLine = ((Boolean) model.getProperty("alternateLine", false));
        JFreeChart chart = ChartFactory.createPolarChart(theTitle,
                data,
                legend == null ? true : legend,
                tooltips == null ? true : tooltips,
                false);
        PolarPlot polarPlot = (PolarPlot)chart.getPlot();
        boolean clockwise=true;
        double polarAngleOffset=0;
        if(plotModelProvider.getModel().getProperty("polarClockwise")!=null){
            clockwise=(Boolean)plotModelProvider.getModel().getProperty("polarClockwise");
        }
        if(plotModelProvider.getModel().getProperty("polarAngleOffset")!=null){
            polarAngleOffset=((Number)plotModelProvider.getModel().getProperty("polarAngleOffset")).doubleValue();
        }
        polarPlot.setCounterClockwise(!clockwise); // changes the direction of the ticks
        polarPlot.setAxisLocation(PolarAxisLocation.EAST_BELOW); // defines the placement of the axis
        polarPlot.setAngleOffset(polarAngleOffset);

        //Object o=chart.getXYPlot().getRenderer(0);
        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer();
        int jfreeChartIndex = 0;
        for (int i = 0; i < yAxis.length; i++) {
            if (ytitles != null && ytitles.length > i) {
                if (!model.getYVisible(i)) {
                    continue;
                }
            }
            Color color=(Color) model.getProperty(i, "color", null);
            if(color==null){
                color=paintArray.getColor(i*1.0f/yAxis.length);
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
            }else {
                r.setSeriesPaint(jfreeChartIndex, color);
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
//        chart.getXYPlot().setRenderer(0, r);
//        XYPlot plot = (XYPlot) chart.getPlot();
//        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
//        if (!x_minmax.isNaN()) {
//            axis.setRange(x_minmax.getMin(), x_minmax.getMax());
//        }
        //        plot.getRenderer().
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        add(chartPanel);
//        net.vpc.scholar.hadrumaths.Plot.buildJPopupMenu(chartPanel, plotModelProvider);
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
