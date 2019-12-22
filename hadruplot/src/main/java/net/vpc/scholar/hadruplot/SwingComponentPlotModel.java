package net.vpc.scholar.hadruplot;

import javax.swing.*;
import java.awt.*;

public class SwingComponentPlotModel extends BasePlotModel implements PlotPanelFactory {
    private JComponent component;

    public SwingComponentPlotModel(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    @Override
    public PlotPanel create(PlotModel model) {
        BasePlotPanel p = new SwingComponentPlotPanel();
        p.setModel(model);
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
