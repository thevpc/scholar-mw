package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

public abstract class PlotCanvasAnyXYDoubleJFreeChart extends PlotCanvasAnyJFreeChart {
    private ValuesPlotXYDoubleModelFace data;

    public PlotCanvasAnyXYDoubleJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
    }

    @Override
    protected int initialIndex(int index) {
        return data.getInitialIndex(index);
    }

    protected XYZDataset createXYZDataset() {
        DefaultXYZDataset dataset = new DefaultXYZDataset();


        java.util.List<double[]> listOfXYZ = new java.util.ArrayList<>();
        double[] y = data.getY();
        double[] x = data.getX();
        for (int yi = 0; yi < data.size(); yi++) {
            for (int xi = 0; xi < x.length; xi++) {
                double yf = y[yi];
                double xf = x[xi];
                double c = data.getZ(xi, yi);
                if (c != 0 && Double.isFinite(c)) {
                    listOfXYZ.add(new double[]{xf, yf, c});
                }
            }
        }
        double[] xx = new double[listOfXYZ.size()];
        double[] yy = new double[listOfXYZ.size()];
        double[] zz = new double[listOfXYZ.size()];
        double[][] series = new double[][]{xx, yy, zz};
        for (int i = 0; i < listOfXYZ.size(); i++) {
            double[] xyz = listOfXYZ.get(i);
            xx[i] = xyz[0];
            yy[i] = xyz[1];
            zz[i] = xyz[2];
        }
        dataset.addSeries(data.getTitle(), series);
        return dataset;
    }


    protected void init() {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        loadConfig();
        data = new ValuesPlotXYDoubleModelFace(model, config);
        MinMax x_minmax = new MinMax();
//        MinMax y_minmax = new MinMax();


//        for (int i = 0; i < data.size(); i++) {
//            Complex[] y = data.getY(i);
//            double[] x = data.getX(i);
//            for (int k = 0; k < y.length; k++) {
//                Complex yf = y[k];
//                double xf = x[k];
//                x_minmax.registerValue(xf);
//                y_minmax.registerValue(yf);
//            }
//        }

        if (data.size() > config.maxLegendCount.get() && config.showLegend.get()) {
            config.showLegend.set(false);
        }


        JFreeChart chart = createChart(data.getTitle(), config.showLegend.get(), config.showTooltips.get());
        prepareJFreeChart(chart, x_minmax);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        add(chartPanel);
        prepareChartPanel();
    }

    @Override
    protected int dataSize() {
        return data.size();
    }
}
