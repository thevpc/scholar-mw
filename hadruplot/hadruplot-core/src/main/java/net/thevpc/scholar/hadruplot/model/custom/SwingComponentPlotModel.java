package net.thevpc.scholar.hadruplot.model.custom;

import javax.swing.*;
import java.awt.*;
import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotValue;
import net.thevpc.scholar.hadruplot.extension.PlotPanelFactory;
import net.thevpc.scholar.hadruplot.model.BasePlotModel;

public class SwingComponentPlotModel extends BasePlotModel implements PlotPanelFactory {
    private JComponent component;
    private PlotValue value;

    public SwingComponentPlotModel(PlotValue value) {
        this.value = value;
        this.component = (JComponent)value.getValue();
    }

    public PlotValue getPlotValue() {
        return value;
    }
    

    public JComponent getComponent() {
        return component;
    }

    @Override
    public PlotPanel create() {
        BasePlotPanel p = new SwingComponentPlotPanel();
        p.setModel(this);
        return p;
    }

    private static class SwingComponentPlotPanel extends TitleBasedPlotPanel {
        public SwingComponentPlotPanel() {
            add(title, BorderLayout.NORTH);
        }

        public SwingComponentPlotModel getComponentModel() {
            return (SwingComponentPlotModel) getModel();
        }

        @Override
        protected void modelChanged() {
            super.modelChanged();
            add(getComponentModel().getComponent(), BorderLayout.CENTER);
        }


    }
}
