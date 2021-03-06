package net.thevpc.scholar.hadruplot.console.extension;

import net.thevpc.scholar.hadruplot.console.PlotConsole;

import javax.swing.*;
import java.net.URL;

public interface PlotConsoleTool {
    String getTitle();

    JComponent toComponent();

    URL getIconURL();

    void onInstall(PlotConsole console, JInternalFrame frame);
}
