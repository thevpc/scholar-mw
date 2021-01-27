package net.thevpc.scholar.hadruplot.model;

import javax.swing.*;
import java.awt.*;
import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotValue;
import net.thevpc.scholar.hadruplot.extension.PlotPanelFactory;

public class SwingComponentPlotModel extends BasePlotModel implements PlotPanelFactory {
    private JComponent component;
    private PlotValue value;

    public SwingComponentPlotModel(PlotValue value) {
        this.value = value;
        this.component = (JComponent)value.getValue();
    }

//    @Override
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

    private static class SwingComponentPlotPanel extends BasePlotPanel {
        JLabel title = new JLabel("???");
        {
            title.setHorizontalAlignment(SwingConstants.CENTER);
            add(title, BorderLayout.NORTH);
        }

        public SwingComponentPlotPanel() {
            super(new BorderLayout());
        }

        public SwingComponentPlotModel getComponentModel() {
            return (SwingComponentPlotModel) getModel();
        }

        @Override
        protected void modelChanged() {
            title.setText(getModel().getPreferredTitle());
            add(getComponentModel().getComponent(), BorderLayout.CENTER);
        }


    }
}
