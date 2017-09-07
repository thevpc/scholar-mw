package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.TableOrder;

public abstract class PlotCanvasAnyDoubleJFreeChart extends PlotCanvasAnyJFreeChart{
    protected ValuesPlotXDoubleModelFace data;

    public PlotCanvasAnyDoubleJFreeChart(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);
    }

    protected CategoryDataset createCategoryDataset() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < data.size(); i++) {
            String key = data.getYTitle(i);
            double[] x = data.getX(i);
            double[] y = data.getY(i);
            for (int k = 0; k < y.length; k++) {
                dataset.addValue((Number) (y[k]), key, x[k]);
            }
        }
        return dataset;
    }

    protected PieDataset createPieDataset() {
        return new CategoryToPieDataset(createCategoryDataset(), TableOrder.BY_COLUMN, 0);
    }

    protected XYDataset createXYDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < data.size(); i++) {
            String key = data.getYTitle(i);
            double[] x = data.getX(i);
            double[] y = data.getY(i);
            XYSeries series = new XYSeries(key);
            for (int k = 0; k < y.length; k++) {
                series.add(x[k] , y[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    @Override
    protected int dataSize() {
        return data.size();
    }

    protected void init() {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        loadConfig();
        data = new ValuesPlotXDoubleModelFace(model, config);
        MinMax x_minmax = new MinMax();
        MinMax y_minmax = new MinMax();
        for (int i = 0; i < data.size(); i++) {
            double[] y = data.getY(i);
            double[] x = data.getX(i);
            for (int k = 0; k < y.length; k++) {
                double yf = y[k];
                double xf = x[k];
                x_minmax.registerValue(xf);
                y_minmax.registerValue(yf);
            }
        }

        if (data.size() > config.maxLegendCount && config.showLegend) {
            config.showLegend = false;
        }


        JFreeChart chart = createChart(data.getTitle(), config.showLegend, config.showTooltips);
        prepareJFreeChart(chart, x_minmax);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        add(chartPanel);
        prepareChartPanel();
    }
}
