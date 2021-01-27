/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.plot;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotXYComplexModelFace;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Calc3dTool;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.util.PlotUtils;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import org.jfree.data.xy.VectorXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author vpc
 */
public abstract class PlotCanvasAnyCalc3d extends JPanel implements PlotComponentPanel {
    protected PlotModelProvider plotModelProvider;
    protected Calc3dTool chartPanel;
    protected ColorPalette paintArray = PlotConfigManager.DEFAULT_PALETTE;
    protected PlotViewConfig config;

    public PlotCanvasAnyCalc3d(PlotModelProvider plotModelProvider) {
        super(new BorderLayout());
        this.plotModelProvider = plotModelProvider;
        chartPanel=new Calc3dTool();
        add(chartPanel.getCanvas(),BorderLayout.CENTER);
        chartPanel.getCanvas().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    //onUserAnnotation(event, !event.getTrigger().isControlDown());
                }
            }
        });
    }

    protected double getDefaultXMultiplier() {
        return 1;
    }


    protected void loadConfig() {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        config = (PlotViewConfig) model.getProperty("config", null);
        config = PlotViewConfig.copy(config).validate(model.getZ().length);
        config.defaultXMultiplier.set(getDefaultXMultiplier());
    }

    protected void prepareChartPanel() {

    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return PlotUtils.getOrCreateComponentPopupMenu(chartPanel.getCanvas());
    }


    protected VectorXYDataset createVectorXYDataset() {
        VectorSeriesCollection dataset = new VectorSeriesCollection();
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        loadConfig();
        ValuesPlotXYComplexModelFace data = new ValuesPlotXYComplexModelFace(model, config);

        double[] y = data.getY();
        double[] x = data.getX();
        for (int yi = 0; yi < data.size(); yi++) {
            VectorSeries series = new VectorSeries(data.getYTitle(yi));
            double yf = y[yi];
            for (int xi = 0; xi < y.length; xi++) {
                double xf = x[xi];
                Object c = data.getZ(xi, yi);
                PlotDoubleComplex cc = PlotConfigManager.Numbers.toDoubleComplex(c);
                if (!(cc.real == 0 && cc.imag == 0) && PlotUtils.isDoubleFinite(cc.real) && PlotUtils.isDoubleFinite(cc.real)) {
                    double nn = Math.sqrt(cc.real * cc.real + cc.imag * cc.imag);
                    cc = new PlotDoubleComplex(
                            cc.real / nn,
                            cc.imag / nn
                    );
                }
                series.add(xf, yf, cc.real, cc.imag);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
}
