/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.plot;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Calc3dTool;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

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
    protected ColorPalette paintArray = ColorPalettes.DEFAULT_PALETTE;
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


}
