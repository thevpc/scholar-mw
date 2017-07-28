package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;

/**
 * Created by vpc on 6/5/14.
 */
public class SimplePlotModelProvider implements PlotModelProvider {
    private ValuesPlotModel model;
    private JComponent component;

    public SimplePlotModelProvider(ValuesPlotModel model, JComponent component) {
        this.model = model;
        this.component = component;
    }

    public ValuesPlotModel getModel() {
        return model;
    }

    public void setModel(ValuesPlotModel model) {
        this.model = model;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }
}
