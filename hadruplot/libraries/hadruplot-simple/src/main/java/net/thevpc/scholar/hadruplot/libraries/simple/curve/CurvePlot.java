package net.thevpc.scholar.hadruplot.libraries.simple.curve;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.libraries.simple.PlotCanvasCurveSimple;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CurvePlot extends JPanel implements PlotComponentPanel {
    private PlotModelProvider modelProvider;
    private PlotComponentPanel subPanel;

    public CurvePlot(PlotModelProvider modelProvider) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        ValuesPlotModel model = (ValuesPlotModel) this.modelProvider.getModel();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateChart();
            }
        });
        updateChart();
    }

    private void updateChart() {
        for (Component component : getComponents()) {
            remove(component);
        }
        add((subPanel = new PlotCanvasCurveSimple(this.modelProvider)).toComponent());
        invalidate();
        revalidate();
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return subPanel.getPopupMenu();
    }

}

