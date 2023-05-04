package net.thevpc.scholar.hadruplot;

import javax.swing.*;

public interface PlotLibrary {
    String getName();
    int getSupportLevel(PlotType type);
    PlotComponentPanel createPlotComponentPanel(PlotComponentContext context);

    default JPopupMenu resolvePopupMenu(JComponent mainComponent){
        return null;
    }
}
