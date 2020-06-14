package net.vpc.scholar.hadruplot.extension.defaults;

import net.vpc.scholar.hadruplot.extension.PlotModelProvider;
import net.vpc.scholar.hadruplot.model.PlotModel;
import javax.swing.*;

/**
 * Created by vpc on 6/5/14.
 */
public class SimplePlotModelProvider implements PlotModelProvider {
    private PlotModel model;
    private JComponent component;

    public SimplePlotModelProvider(PlotModel model, JComponent component) {
        this.model = model;
        this.component = component;
    }

    public PlotModel getModel() {
        return model;
    }

    public void setModel(PlotModel model) {
        this.model = model;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }
}
