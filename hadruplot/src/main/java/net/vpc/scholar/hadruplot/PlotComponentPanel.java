package net.vpc.scholar.hadruplot;

import javax.swing.*;

public interface PlotComponentPanel {
    JComponent toComponent();

    JPopupMenu getPopupMenu();
//    public boolean isSupported()
}
