package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.win.WindowInfo;
import net.thevpc.common.swing.win.WindowInfoListener;
import net.thevpc.common.swing.win.WindowPath;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public interface PlotConsoleFrame {
    PlotConsole getConsole();

    void addPlotConsoleListener(WindowInfoListener listener);

    void removePlotConsoleListener(WindowInfoListener listener);

    void setGlobalInfo(String s);

    void exitFrame();
    void closeFrame();

    void setDefaultCloseOperation(CloseOption closeOption);

    void setTitle(String frameTitle);

    ConsoleWindow getWindow(WindowPath preferredPath);

    JInternalFrame addToolsFrame(WindowInfo info);

    Component frameComponent();

    void addRecentFile(File selectedFile);

    void removeWindow(JComponent toComponent);
}
