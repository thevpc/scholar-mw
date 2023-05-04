package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.model.PlotModel;
import javax.swing.*;
import java.awt.*;

public class SimplePlotComponent extends BasePlotComponent {
    public SimplePlotComponent(JComponent component) {
        super(new BorderLayout());
        add(component);
    }

    @Override
    public PlotModel getModel() {
        return null;
    }
}
