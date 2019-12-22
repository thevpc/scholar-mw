package net.vpc.scholar.hadruplot;

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
