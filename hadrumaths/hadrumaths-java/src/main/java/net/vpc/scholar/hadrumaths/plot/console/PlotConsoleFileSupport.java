package net.vpc.scholar.hadrumaths.plot.console;

import java.io.File;

public interface PlotConsoleFileSupport {
    String getFileExtension();
    String getFileDesc();
    ConsoleAction createLoadAction(File file);
    ConsoleAction createLoadAction(File[] files);
}
