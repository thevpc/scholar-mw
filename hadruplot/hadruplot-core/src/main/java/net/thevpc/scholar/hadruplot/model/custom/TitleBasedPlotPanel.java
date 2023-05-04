package net.thevpc.scholar.hadruplot.model.custom;

import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.model.PlotModel;

import javax.swing.*;
import java.awt.*;

public class TitleBasedPlotPanel extends BasePlotPanel {
    JLabel title = new JLabel("???");

    public TitleBasedPlotPanel() {
        super(new BorderLayout());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);
    }

    @Override
    protected void modelChanged() {
        PlotModel m = getModel();
        title.setText(m == null ? "" : m.getPreferredTitle());
    }

}
