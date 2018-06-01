package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import org.jfree.data.xy.VectorXYDataset;

public abstract class PlotCanvasAnyComplexJFreeChart extends PlotCanvasAnyJFreeChart {
    private ValuesPlotXYComplexModelFace data;

    public PlotCanvasAnyComplexJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
    }

    protected VectorXYDataset createVectorXYDataset() {
        VectorSeriesCollection dataset = new VectorSeriesCollection();

        double[] y = data.getY();
        double[] x = data.getX();
        for (int yi = 0; yi < data.size(); yi++) {
            VectorSeries series = new VectorSeries(data.getYTitle(yi));
            double yf = y[yi];
            for (int xi = 0; xi < y.length; xi++) {
                double xf = x[xi];
                Complex c = data.getZ(xi, yi);
                if (!c.isZero() && c.isFinite()) {
                    c = c.div(c.norm());
                }
                series.add(xf, yf, c.getReal(), c.getImag());
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    @Override
    protected int initialIndex(int index) {
        return data.getInitialIndex(index);
    }

    protected void init() {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        loadConfig();
        data = new ValuesPlotXYComplexModelFace(model, config);
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
